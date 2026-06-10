package com.example.SecureStorage.domain.entity;

import java.util.Map;

import com.example.SecureStorage.commons.OperationResult;

public class StorageItemFactory {

    public static OperationResult<StorageItem> createStorageItemFromAttachment(
        Map<String, Object> attachmentData) {
        if (attachmentData == null) {
            return OperationResult.error("Attachment data is null");
        }
        
        if (!attachmentData.containsKey("name")) {
            return OperationResult.error("Attachment data missing 'name' field");
        }
        
        if (!attachmentData.containsKey("metadata")) {
            return OperationResult.error("Attachment data missing 'metadata' field");
        }

        Object nameObj = attachmentData.get("name");
        if (!(nameObj instanceof String)) {
            return OperationResult.error("Invalid 'name' field");
        }

        String name = (String) nameObj;
        if (name.trim().isEmpty()) {
            return OperationResult.error("'name' field cannot be empty");
        }

        Map<String, Object> metadataObj;
        try {
            metadataObj = (Map<String, Object>) attachmentData.get("metadata");
        } catch (ClassCastException e) {
            return OperationResult.error("Invalid 'metadata' field format");
        }

        StorageItem item = new StorageItem();
        item.setName(name);
        item.setMetadata(metadataObj);
        return OperationResult.success(item);
        
    }

}
