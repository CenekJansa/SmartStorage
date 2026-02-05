package com.example.SecureStorage.infrastructure.storage;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SecureStorage.commons.OperationResult;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    private static final String CONTENT_TYPE_DEFAULT = "application/pdf";

    public OperationResult<Void> storeFile(byte[] fileData, String fileName, String bucketName, String uniqueObjectName) {
        try {
            log.debug("Checking if bucket '{}' exists", bucketName);
            boolean isBucketExists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build()
            );
            if (!isBucketExists) {
                log.info("Bucket '{}' does not exist, creating it", bucketName);
                minioClient.makeBucket(
                    MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build()
                );
                log.info("Successfully created bucket '{}'", bucketName);
            }
            log.info("Uploading file '{}' to bucket '{}' as object '{}' (size: {} bytes, content-type: {})",
                fileName, bucketName, uniqueObjectName, fileData.length, CONTENT_TYPE_DEFAULT);

            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData)) {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(uniqueObjectName)
                        .stream(inputStream, fileData.length, -1)
                        .contentType(CONTENT_TYPE_DEFAULT)
                        .build());
            }

            log.info("Successfully uploaded file '{}' to MinIO bucket '{}' as object '{}'",
                fileName, bucketName, uniqueObjectName);
        } catch (Exception e) {
            log.error("Failed to store file '{}' in MinIO bucket '{}': {}",
                fileName, bucketName, e.getMessage(), e);
            return OperationResult.error("MinIO storage error: " + e.getMessage());
        }
        return OperationResult.success(null);
    }
}
