package com.example.SecureStorage.domain.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
public class StorageItemAttachment extends BaseEntity {
    private String fileName;
    @JoinColumn(name = "storage_item_id", nullable = false)
    @ManyToOne
    private StorageItem storageItem;
}
