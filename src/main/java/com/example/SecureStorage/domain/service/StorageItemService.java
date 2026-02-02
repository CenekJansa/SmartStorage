package com.example.SecureStorage.domain.service;

import com.example.SecureStorage.commons.OperationResult;

public interface StorageItemService {
    
    OperationResult<Long> uploadFile(Long itemId, String fileName, byte[] fileData);
}
