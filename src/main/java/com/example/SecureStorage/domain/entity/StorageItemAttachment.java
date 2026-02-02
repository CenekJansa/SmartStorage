package com.example.SecureStorage.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StorageItemAttachment extends BaseEntity {
    @Column(nullable = false)
    private String fileName;
    private String fullObjectName;
    @JoinColumn(name = "storage_item_id", nullable = false)
    @ManyToOne
    private StorageItem storageItem;
}
