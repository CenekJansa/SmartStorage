package com.example.SecureStorage.domain.service;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.SecureStorage.commons.OperationResult;
import com.example.SecureStorage.domain.entity.AttachmentStatus;
import com.example.SecureStorage.domain.entity.StorageItemAttachment;
import com.example.SecureStorage.domain.entity.StorageSection;
import com.example.SecureStorage.domain.repository.StorageItemAttachmentRepository;
import com.example.SecureStorage.domain.repository.StorageSectionRepository;
import com.example.SecureStorage.messaging.DocumentProcessingMessage;
import com.example.SecureStorage.messaging.DocumentProcessingProducer;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Service
public class StorageItemSericeImpl implements StorageItemService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private StorageSectionRepository storageSectionRepository;

    @Autowired
    private StorageItemAttachmentRepository storageItemAttachmentRepository;

    @Autowired
    private DocumentProcessingProducer documentProcessingProducer;

    @Value("${minio.bucket.name}")
    private String bucketName;
    
    /**
     * Method uploads a file to minIO and creates a StorageItemAttachment.
     *
     * @param itemId   id of the item
     * @param fileName name of the file
     * @param fileData data of the file
     * @return id of the attachment
     */
    @Override
    public OperationResult<Long> uploadFile(@NotNull Long sectionId,
         @NotNull String fileName, @NotNull byte[] fileData) {
        Optional<StorageSection> sectionOpt = storageSectionRepository.findById(sectionId);
        if (!sectionOpt.isPresent()) {
            return OperationResult.error("StorageSection not found with ID: " + sectionId);
        }
        try {
            String uniqueObjectName = UUID.randomUUID().toString() + "_" + fileName;

            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(uniqueObjectName)
                    .stream(inputStream, fileData.length, -1)
                    .build()
            );

            StorageItemAttachment attachment = new StorageItemAttachment();
            attachment.setFileName(fileName);
            attachment.setBucketName(bucketName);
            attachment.setFullObjectName(uniqueObjectName);
            attachment.setStatus(AttachmentStatus.PROCESSING);

            StorageItemAttachment savedAttachment =
             storageItemAttachmentRepository.save(attachment);

            DocumentProcessingMessage message = new DocumentProcessingMessage(
                fileData,
                sectionId,
                savedAttachment.getId(),
                fileName
            );
            documentProcessingProducer.sendProcessingMessage(message);

            return OperationResult.success(savedAttachment.getId());

        } catch (Exception e) {
            return OperationResult.error("Failed to upload file: " + e.getMessage());
        }
    }

}
