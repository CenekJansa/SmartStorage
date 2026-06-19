package com.example.SecureStorage.domain.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.example.SecureStorage.commons.OperationResult;
import com.example.SecureStorage.domain.service.StorageItemServiceKit.StorageItemResultVo;
import com.example.SecureStorage.domain.service.StorageItemServiceKit.StorageItemDetailResultVo;
import com.example.SecureStorage.domain.service.StorageItemServiceKit.StorageItemEditInputVo;

public interface StorageItemService {
    
    OperationResult<Long> uploadFile(@NotNull Long sectionId,
         @NotNull String fileName, @NotNull byte[] fileData);

    List<StorageItemResultVo> retrieveStorageItems(@NotNull Long sectionId);

    StorageItemDetailResultVo retrieveStorageItemDetail(@NotNull Long itemId);

    void removeStorageItem(@NotNull Long itemId);

    StorageItemDetailResultVo editStorageItem(@NotNull Long itemId, StorageItemEditInputVo inputVo);

    void removeAttachment(@NotNull Long attachmentId);
}
