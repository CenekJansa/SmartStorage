package com.example.SecureStorage.messaging;

import com.example.SecureStorage.commons.OperationResult;
import com.example.SecureStorage.config.RabbitMQConfig;
import com.example.SecureStorage.domain.entity.AttachmentStatus;
import com.example.SecureStorage.domain.entity.StorageItem;
import com.example.SecureStorage.domain.entity.StorageItemAttachment;
import com.example.SecureStorage.domain.entity.StorageItemFactory;
import com.example.SecureStorage.domain.entity.StorageSection;
import com.example.SecureStorage.domain.repository.StorageItemAttachmentRepository;
import com.example.SecureStorage.domain.repository.StorageItemRepository;
import com.example.SecureStorage.domain.repository.StorageSectionRepository;
import com.example.SecureStorage.domain.service.AIDocumentProcessingService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class DocumentProcessingConsumer {

    @Autowired
    private StorageSectionRepository storageSectionRepository;

    @Autowired
    private StorageItemRepository storageItemRepository;

    @Autowired
    private StorageItemAttachmentRepository storageItemAttachmentRepository;

    @Autowired
    private AIDocumentProcessingService aiDocumentProcessingService;

    /**
     * Process document asynchronously using AI
     *
     * @param message the document processing message from RabbitMQ
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    @Transactional
    public void processDocument(DocumentProcessingMessage message) {
        try {
            OperationResult<String> pdfTextRes = extractTextFromPDF(message.getFileData());
            if (!pdfTextRes.isSuccess()) {
                markAttachmentAsFailed(message.getAttachmentId(), pdfTextRes.getErrorMessage());
                return;
            }
            String pdfText = pdfTextRes.getData();
            log.info("Extracted {} characters from PDF", pdfText.length());

            Optional<StorageSection> sectionOpt =
                storageSectionRepository.findById(message.getSectionId());
            if (!sectionOpt.isPresent()) {
                markAttachmentAsFailed(message.getAttachmentId(),
                    "StorageSection not found with ID: " + message.getSectionId());
                return;
            }
            StorageSection section = sectionOpt.get();
            List<String> uniqueKeys = section.getUniqueKeys();
            List<String> sectionAttributes = section.getAttributes();
            log.info("Found section '{}' with {} attributes", section.getName(),
                sectionAttributes.size());

            OperationResult<Map<String, Object>> aiResultRes =
                aiDocumentProcessingService.processDocument(pdfText, sectionAttributes);
            if (!aiResultRes.isSuccess()) {
                markAttachmentAsFailed(message.getAttachmentId(), aiResultRes.getErrorMessage());
                return;
            }
            Map<String, Object> aiResult = aiResultRes.getData();

            log.info("AI processing completed successfully");

            OperationResult<StorageItem> storageItemRes =
                StorageItemFactory.createStorageItemFromAttachment(aiResult);
            if (!storageItemRes.isSuccess()) {
                markAttachmentAsFailed(message.getAttachmentId(),
                    "Failed to create StorageItem: " + storageItemRes.getErrorMessage());
                return;
            }
            StorageItem storageItem = storageItemRes.getData();
            Map<String, Object> metadata = storageItem.getMetadata();

            // look for duplicates
            log.info("Looking for duplicates for unique keys: {}", uniqueKeys);
            for (String uniqueKey : uniqueKeys) {
                Object metadataValue = metadata.get(uniqueKey);
                String keyValueStr;
                if (metadataValue == null) {
                    keyValueStr = "";
                } else {
                    keyValueStr = metadataValue.toString();
                }
                log.info("Searching for duplicate with key='{}', value='{}' in section {}",
                    uniqueKey, keyValueStr, section.getId());
                Optional<StorageItem> existingItemOpt =
                    storageItemRepository.findByMetadataAttribute(
                        section.getId(),
                        uniqueKey,
                        keyValueStr
                    );
                if (existingItemOpt.isPresent()) {
                    log.info("Duplicate item found for unique key '{}' with value '{}'", uniqueKey,
                        keyValueStr);
                    mergeItems(existingItemOpt.get(), storageItem, message.getAttachmentId());
                    return;
                } else {
                    log.info("No duplicate found for key='{}', value='{}'", uniqueKey,
                        keyValueStr);
                }
            }
            log.info("No duplicates found");
            Optional<StorageItemAttachment> attachmentOpt = storageItemAttachmentRepository
                .findById(message.getAttachmentId());
            if (!attachmentOpt.isPresent()) {
                markAttachmentAsFailed(message.getAttachmentId(),
                    "Attachment not found with ID: " + message.getAttachmentId());
                return;
            }
            StorageItemAttachment attachment = attachmentOpt.get();

            // save item
            storageItem.setStorageSection(section);
            StorageItem savedItem = storageItemRepository.save(storageItem);
            // update attachment
            attachment.setStorageItem(savedItem);
            attachment.setStatus(AttachmentStatus.COMPLETED);
            storageItemAttachmentRepository.save(attachment);

            log.info("Successfully processed document for attachment ID: {}",
                message.getAttachmentId());
        } catch (Exception e) {
            log.error("Failed to process document for attachment ID: {}",
                message.getAttachmentId(), e);
            markAttachmentAsFailed(message.getAttachmentId(), e.getMessage());
        }
    }

    private void mergeItems(StorageItem existingItem, StorageItem newItem, Long attachmentId) {
        // of each key set
        Map<String, Object> existingMetadata = existingItem.getMetadata();
        Map<String, Object> newMetadata = newItem.getMetadata();
        for (String key : newMetadata.keySet()) {
            if (!existingMetadata.containsKey(key) || existingMetadata.get(key) == null) {
                existingMetadata.put(key, newMetadata.get(key));
            }
        }
        Optional<StorageItemAttachment> attachmentOpt = storageItemAttachmentRepository
            .findById(attachmentId);
        if (!attachmentOpt.isPresent()) {
            log.error("Attachment not found with ID: {}", attachmentId);
            return;
        }
        StorageItemAttachment attachment = attachmentOpt.get();
        attachment.setStorageItem(existingItem);
        storageItemAttachmentRepository.save(attachment);
        existingItem.setMetadata(existingMetadata);
        storageItemRepository.save(existingItem);
    }

    private void markAttachmentAsFailed(Long attachmentId, String errorMessage) {
        log.error("Failed to process document for attachment ID: {}", attachmentId,
            errorMessage);
        Optional<StorageItemAttachment> attachmentOpt = storageItemAttachmentRepository
            .findById(attachmentId);
        if (!attachmentOpt.isPresent()) {
            log.error("Attachment not found with ID: {}", attachmentId);
            return;
        }
        StorageItemAttachment attachment = attachmentOpt.get();
        attachment.setStatus(AttachmentStatus.FAILED);
        storageItemAttachmentRepository.save(attachment);
    }

    /**
     * Extract text from PDF using PDFBox
     *
     * @param pdfData the PDF file data
     * @return extracted text
     * @throws Exception if PDF extraction fails
     */
    private OperationResult<String> extractTextFromPDF(byte[] pdfData) {
        try (PDDocument document = Loader.loadPDF(pdfData)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return OperationResult.success(text);
        } catch (Exception e) {
            log.error("Failed to extract text from PDF", e);
            return OperationResult.error("PDF text extraction failed: " + e.getMessage());
        }
    }
}
