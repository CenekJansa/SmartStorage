package com.example.SecureStorage.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SecureStorage.domain.entity.StorageItemAttachment;

@Repository
public interface StorageItemAttachmentRepository extends JpaRepository<StorageItemAttachment, Long> {
    
}

