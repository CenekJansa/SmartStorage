package com.example.SecureStorage.domain.service;

import com.example.SecureStorage.commons.OperationResult;

public interface StorageItemService {
    
    OperationResult<Long> uploadFile(Long sectionId,
         String fileName, byte[] fileData);
}
