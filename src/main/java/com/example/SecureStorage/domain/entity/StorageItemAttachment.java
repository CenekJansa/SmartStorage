package com.example.SecureStorage.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Column(nullable = false)
    private String fullObjectName;

    @Column(nullable = false)
    private String bucketName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttachmentStatus status = AttachmentStatus.PROCESSING;

    @JoinColumn(name = "storage_item_id", nullable = true)
    @ManyToOne
    private StorageItem storageItem;
}
