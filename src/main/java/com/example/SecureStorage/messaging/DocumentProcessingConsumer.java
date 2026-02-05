package com.example.SecureStorage.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.SecureStorage.commons.OperationResult;
import com.example.SecureStorage.config.RabbitMQConfig;
import com.example.SecureStorage.domain.service.DocumentProcessingService;

import lombok.extern.slf4j.Slf4j;

/**
 * RabbitMQ consumer for document processing messages.
 * This class is responsible ONLY for:
 * - Receiving messages from RabbitMQ
 * - Validating message content
 * - Delegating processing to DocumentProcessingService
 *
 * Follows Single Responsibility Principle - handles only message consumption and validation.
 */
@Component
@Slf4j
public class DocumentProcessingConsumer {

    @Autowired
    private DocumentProcessingService documentProcessingService;

    /**
     * Receives and processes document processing messages from RabbitMQ.
     * Validates the message and delegates to DocumentProcessingService.
     *
     * @param message the document processing message from RabbitMQ
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(DocumentProcessingMessage message) {
        log.info("Received document processing message for attachmentId: {}, sectionId: {}",
            message.getAttachmentId(), message.getSectionId());

        // Validate message
        OperationResult<Void> validationResult = validateMessage(message);
        if (!validationResult.isSuccess()) {
            log.error("Message validation failed: {}", validationResult.getErrorMessage());
            return;
        }

        // Delegate to service
        OperationResult<Void> processingResult = documentProcessingService.processDocument(
            message.getFileData(),
            message.getSectionId(),
            message.getAttachmentId()
        );

        if (!processingResult.isSuccess()) {
            log.error("Document processing failed for attachmentId {}: {}",
                message.getAttachmentId(), processingResult.getErrorMessage());
        } else {
            log.info("Document processing completed successfully for attachmentId: {}",
                message.getAttachmentId());
        }
    }

    /**
     * Validates the incoming message to ensure all required fields are present.
     *
     * @param message the message to validate
     * @return OperationResult indicating validation success or failure
     */
    private OperationResult<Void> validateMessage(DocumentProcessingMessage message) {
        if (message == null) {
            return OperationResult.error("Message is null");
        }

        if (message.getFileData() == null || message.getFileData().length == 0) {
            return OperationResult.error("File data is null or empty");
        }

        if (message.getSectionId() == null) {
            return OperationResult.error("Section ID is null");
        }

        if (message.getAttachmentId() == null) {
            return OperationResult.error("Attachment ID is null");
        }

        if (message.getFileName() == null || message.getFileName().trim().isEmpty()) {
            log.warn("File name is null or empty for attachmentId: {}", message.getAttachmentId());
        }

        return OperationResult.success(null);
    }
}
