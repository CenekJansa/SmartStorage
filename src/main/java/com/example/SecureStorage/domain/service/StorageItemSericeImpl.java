package com.example.SecureStorage.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.SecureStorage.commons.OperationResult;
import com.example.SecureStorage.domain.repository.StorageItemRepository;
import com.example.SecureStorage.domain.service.StorageItemServiceKit.StorageItemResultVo;

import io.minio.MinioClient;

@Service
public class StorageItemSericeImpl implements StorageItemService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private StorageItemRepository storageItemRepository;

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
    public OperationResult<Long> uploadFile(Long itemId, String fileName, byte[] fileData) {
        throw new UnsupportedOperationException("Unimplemented method 'uploadFile'");
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
