package com.example.SecureStorage.domain.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.example.SecureStorage.commons.OperationResult;
import com.example.SecureStorage.domain.service.StorageItemServiceKit.StorageItemResultVo;

public interface StorageItemService {
    
    OperationResult<Long> uploadFile(Long sectionId,
         String fileName, byte[] fileData);

    List<StorageItemResultVo> retrieveStorageItems(@NotNull Long sectionId);
}
