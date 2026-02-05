package com.example.SecureStorage.domain.service;

import com.example.SecureStorage.infrastructure.AiGateway;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.SecureStorage.commons.OperationResult;
import com.example.SecureStorage.domain.entity.AttachmentStatus;
import com.example.SecureStorage.domain.entity.StorageItem;
import com.example.SecureStorage.domain.entity.StorageItemAttachment;
import com.example.SecureStorage.domain.entity.StorageItemFactory;
import com.example.SecureStorage.domain.entity.StorageSection;
import com.example.SecureStorage.domain.repository.StorageItemAttachmentRepository;
import com.example.SecureStorage.domain.repository.StorageItemRepository;
import com.example.SecureStorage.domain.repository.StorageSectionRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for orchestrating document processing workflow.
 * This service coordinates PDF extraction, AI processing, and storage item creation.
 */
@Service
@Slf4j
public class DocumentProcessingService {

    @Autowired
    private StorageSectionRepository storageSectionRepository;

    @Autowired
    private StorageItemRepository storageItemRepository;

    @Autowired
    private StorageItemAttachmentRepository storageItemAttachmentRepository;

    @Autowired
    private PdfTextExtractionService pdfTextExtractionService;

    @Autowired
    private AiGateway aiGateway;

    /**
     * Process a document by extracting text, using AI to parse it, and creating a storage item.
     * This is the main orchestration method that coordinates all processing steps.
     *
     * @param fileData     the PDF file data
     * @param sectionId    the storage section ID
     * @param attachmentId the attachment ID
     * @return OperationResult indicating success or failure
     */
    @Transactional
    public OperationResult<Void> processDocument(@NotNull byte[] fileData,
                                                   @NotNull Long sectionId,
                                                   @NotNull Long attachmentId) {
        log.info("Starting document processing for attachmentId: {}, sectionId: {}", attachmentId, sectionId);

        // Step 1: Extract text from PDF
        OperationResult<String> pdfTextResult = pdfTextExtractionService.extractText(fileData);
        if (!pdfTextResult.isSuccess()) {
            markAttachmentAsFailed(attachmentId, pdfTextResult.getErrorMessage());
            return OperationResult.error(pdfTextResult.getErrorMessage());
        }
        String pdfText = pdfTextResult.getData();

        // Step 2: Fetch storage section
        Optional<StorageSection> sectionOpt = storageSectionRepository.findById(sectionId);
        if (!sectionOpt.isPresent()) {
            String errorMsg = "StorageSection not found with ID: " + sectionId;
            markAttachmentAsFailed(attachmentId, errorMsg);
            return OperationResult.error(errorMsg);
        }
        StorageSection section = sectionOpt.get();
        log.info("Found section '{}' with {} attributes", section.getName(), section.getAttributes().size());

        // Step 3: Process document with AI
        OperationResult<Map<String, Object>> aiResult =
            aiGateway.extractAttributesFromText(pdfText, section.getAttributes());
        if (!aiResult.isSuccess()) {
            markAttachmentAsFailed(attachmentId, aiResult.getErrorMessage());
            return OperationResult.error(aiResult.getErrorMessage());
        }

        // Step 4: Create storage item from AI result
        OperationResult<StorageItem> storageItemResult =
            StorageItemFactory.createStorageItemFromAttachment(aiResult.getData());
        if (!storageItemResult.isSuccess()) {
            String errorMsg = "Failed to create StorageItem: " + storageItemResult.getErrorMessage();
            markAttachmentAsFailed(attachmentId, errorMsg);
            return OperationResult.error(errorMsg);
        }
        StorageItem storageItem = storageItemResult.getData();

        // Step 5: Check for duplicates and merge or create new item
        OperationResult<Void> saveResult = handleDuplicatesAndSave(storageItem, section, attachmentId);
        if (!saveResult.isSuccess()) {
            markAttachmentAsFailed(attachmentId, saveResult.getErrorMessage());
            return saveResult;
        }

        log.info("Successfully processed document for attachment ID: {}", attachmentId);
        return OperationResult.success(null);
    }

    /**
     * Handle duplicate detection and either merge with existing item or create new one.
     */
    private OperationResult<Void> handleDuplicatesAndSave(StorageItem storageItem,
                                                           StorageSection section,
                                                           Long attachmentId) {
        List<String> uniqueKeys = section.getUniqueKeys();
        Map<String, Object> metadata = storageItem.getMetadata();

        // Check for duplicates based on unique keys
        log.info("Checking for duplicates using unique keys: {}", uniqueKeys);
        for (String uniqueKey : uniqueKeys) {
            Object metadataValue = metadata.get(uniqueKey);
            String keyValueStr = (metadataValue == null) ? "" : metadataValue.toString();

            log.debug("Searching for duplicate with key='{}', value='{}' in section {}",
                uniqueKey, keyValueStr, section.getId());

            Optional<StorageItem> existingItemOpt = storageItemRepository.findByMetadataAttribute(
                section.getId(), uniqueKey, keyValueStr);

            if (existingItemOpt.isPresent()) {
                log.info("Duplicate item found for unique key '{}' with value '{}'", uniqueKey, keyValueStr);
                return mergeWithExistingItem(existingItemOpt.get(), storageItem, attachmentId);
            }
        }

        // No duplicates found, create new item
        log.info("No duplicates found, creating new storage item");
        return createNewStorageItem(storageItem, section, attachmentId);
    }




    /**
     * Merge new item metadata with existing item and link attachment
     */
    private OperationResult<Void> mergeWithExistingItem(StorageItem existingItem,
                                                          StorageItem newItem,
                                                          Long attachmentId) {
        try {
            // Merge metadata - only add keys that don't exist or have null values
            Map<String, Object> existingMetadata = existingItem.getMetadata();
            Map<String, Object> newMetadata = newItem.getMetadata();

            for (Map.Entry<String, Object> entry : newMetadata.entrySet()) {
                if (!existingMetadata.containsKey(entry.getKey()) || existingMetadata.get(entry.getKey()) == null) {
                    existingMetadata.put(entry.getKey(), entry.getValue());
                }
            }

            existingItem.setMetadata(existingMetadata);
            storageItemRepository.save(existingItem);

            // Link attachment to existing item
            Optional<StorageItemAttachment> attachmentOpt =
                storageItemAttachmentRepository.findById(attachmentId);
            if (!attachmentOpt.isPresent()) {
                return OperationResult.error("Attachment not found with ID: " + attachmentId);
            }

            StorageItemAttachment attachment = attachmentOpt.get();
            attachment.setStorageItem(existingItem);
            attachment.setStatus(AttachmentStatus.COMPLETED);
            storageItemAttachmentRepository.save(attachment);

            log.info("Successfully merged metadata and linked attachment {} to existing item {}",
                attachmentId, existingItem.getId());
            return OperationResult.success(null);
        } catch (Exception e) {
            log.error("Failed to merge items: {}", e.getMessage(), e);
            return OperationResult.error("Failed to merge items: " + e.getMessage());
        }
    }

    /**
     * Create a new storage item and link attachment
     */
    private OperationResult<Void> createNewStorageItem(StorageItem storageItem,
                                                         StorageSection section,
                                                         Long attachmentId) {
        try {
            // Fetch attachment
            Optional<StorageItemAttachment> attachmentOpt =
                storageItemAttachmentRepository.findById(attachmentId);
            if (!attachmentOpt.isPresent()) {
                return OperationResult.error("Attachment not found with ID: " + attachmentId);
            }
            StorageItemAttachment attachment = attachmentOpt.get();

            // Set section and save item
            storageItem.setStorageSection(section);
            StorageItem savedItem = storageItemRepository.save(storageItem);

            // Update section relationship
            section.getStorageItems().add(savedItem);
            storageSectionRepository.save(section);

            // Link attachment to new item
            attachment.setStorageItem(savedItem);
            attachment.setStatus(AttachmentStatus.COMPLETED);
            storageItemAttachmentRepository.save(attachment);

            log.info("Successfully created new storage item {} and linked attachment {}",
                savedItem.getId(), attachmentId);
            return OperationResult.success(null);
        } catch (Exception e) {
            log.error("Failed to create new storage item: {}", e.getMessage(), e);
            return OperationResult.error("Failed to create storage item: " + e.getMessage());
        }
    }

    /**
     * Mark attachment as failed with error message
     */
    private void markAttachmentAsFailed(Long attachmentId, String errorMessage) {
        log.error("Marking attachment {} as failed: {}", attachmentId, errorMessage);
        try {
            Optional<StorageItemAttachment> attachmentOpt =
                storageItemAttachmentRepository.findById(attachmentId);
            if (!attachmentOpt.isPresent()) {
                log.error("Cannot mark attachment as failed - Attachment not found with ID: {}", attachmentId);
                return;
            }

            StorageItemAttachment attachment = attachmentOpt.get();
            attachment.setStatus(AttachmentStatus.FAILED);
            storageItemAttachmentRepository.save(attachment);
            log.info("Successfully marked attachment {} as FAILED", attachmentId);
        } catch (Exception e) {
            log.error("Failed to mark attachment {} as failed: {}", attachmentId, e.getMessage(), e);
        }
    }
}