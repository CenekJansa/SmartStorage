package com.example.SecureStorage.messaging;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

@Component
public class DocumentProcessingConsumer {

    private static final Logger logger = LoggerFactory.getLogger(DocumentProcessingConsumer.class);

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
    public void processDocument(DocumentProcessingMessage message) {
        logger.info("Received document processing message for attachment ID: {}", message.getAttachmentId());

        try {
            String pdfText = extractTextFromPDF(message.getFileData());
            logger.info("Extracted {} characters from PDF", pdfText.length());

            Optional<StorageSection> sectionOpt = storageSectionRepository.findById(message.getSectionId());
            if (!sectionOpt.isPresent()) {
                throw new IllegalArgumentException("StorageSection not found with ID: " + message.getSectionId());
            }
            StorageSection section = sectionOpt.get();
            List<String> sectionAttributes = section.getAttributes();
            logger.info("Found section '{}' with {} attributes", section.getName(), sectionAttributes.size());

            Map<String, Object> aiResult = aiDocumentProcessingService.processDocument(pdfText, sectionAttributes);
            logger.info("AI processing completed successfully");

            OperationResult<StorageItem> storageItemRes = StorageItemFactory.createStorageItemFromAttachment(aiResult);
            if (!storageItemRes.isSuccess()) {
                throw new IllegalArgumentException("Failed to create StorageItem: " + storageItemRes.getErrorMessage());
            }
            StorageItem storageItem = storageItemRes.getData();

            Optional<StorageItemAttachment> attachmentOpt = storageItemAttachmentRepository
                    .findById(message.getAttachmentId());
            if (!attachmentOpt.isPresent()) {
                throw new IllegalArgumentException("Attachment not found with ID: " + message.getAttachmentId());
            }
            StorageItemAttachment attachment = attachmentOpt.get();

            // save item
            storageItem.setStorageSection(section);
            storageItem.getAttachments().add(attachment);
            StorageItem savedItem = storageItemRepository.save(storageItem);
            // update section
            section.getStorageItems().add(savedItem);
            storageSectionRepository.save(section);
            // update attachment
            attachment.setStorageItem(savedItem);
            attachment.setStatus(AttachmentStatus.COMPLETED);
            storageItemAttachmentRepository.save(attachment);

            logger.info("Successfully processed document for attachment ID: {}", message.getAttachmentId());

        } catch (Exception e) {
            logger.error("Failed to process document for attachment ID: {}", message.getAttachmentId(), e);
            Optional<StorageItemAttachment> attachmentOpt = storageItemAttachmentRepository
                    .findById(message.getAttachmentId());
            if (!attachmentOpt.isPresent()) {
                logger.error("Attachment not found with ID: {}", message.getAttachmentId());
                return;
            }
            StorageItemAttachment attachment = attachmentOpt.get();
            attachment.setStatus(AttachmentStatus.FAILED);
            storageItemAttachmentRepository.save(attachment);
        }
    }

    /**
     * Extract text from PDF using PDFBox
     *
     * @param pdfData the PDF file data
     * @return extracted text
     * @throws Exception if PDF extraction fails
     */
    private String extractTextFromPDF(byte[] pdfData) throws Exception {
        try (PDDocument document = Loader.loadPDF(pdfData)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return text;
        }
    }
}
