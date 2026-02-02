package com.example.SecureStorage.domain.service;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.SecureStorage.commons.OperationResult;
import com.example.SecureStorage.domain.entity.StorageItem;
import com.example.SecureStorage.domain.entity.StorageItemAttachment;
import com.example.SecureStorage.domain.repository.StorageItemAttachmentRepository;
import com.example.SecureStorage.domain.repository.StorageItemRepository;
import com.example.SecureStorage.domain.service.StorageItemServiceKit.StorageItemResultVo;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;

@Service
public class StorageItemSericeImpl implements StorageItemService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private StorageItemRepository storageItemRepository;

    @Autowired
    private StorageItemAttachmentRepository storageItemAttachmentRepository;

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
    public OperationResult<Long> uploadFile(@NotNull String fileName,
        @NotNull byte[] fileData) {
        try {
            // Generate unique object name for MinIO
            String uniqueObjectName = UUID.randomUUID().toString() + "_" + fileName;

            // Upload file to MinIO
            ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData);
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(uniqueObjectName)
                    .stream(inputStream, fileData.length, -1)
                    .build()
            );

            // Create StorageItemAttachment entity
            StorageItemAttachment attachment = new StorageItemAttachment();
            attachment.setFileName(fileName);
            attachment.setFullObjectName(uniqueObjectName);

            // Save attachment to database
            StorageItemAttachment savedAttachment =
             storageItemAttachmentRepository.save(attachment);

            // Return attachment ID
            return OperationResult.success(savedAttachment.getId());

        } catch (Exception e) {
            return OperationResult.error("Failed to upload file: " + e.getMessage());
        }
    }

    /**
     * This method uses AI reasoninig model for parsing the attachment
     *  into storage item.
     * 
     * 
     * @param itemId
     * @param fileName
     * @param fileData
     */
    private OperationResult<StorageItemResultVo> createStorageItemFromAttachment(Long itemId,
         String fileName, byte[] fileData) {
        throw new UnsupportedOperationException(
    "Unimplemented method 'createStorageItemFromAttachment'");
    }

    private OperationResult<Void> saveVecorizedData(Long itemId, byte[] fileData) {
        throw new UnsupportedOperationException("Unimplemented method 'saveVecorizedData'");
    }


}
