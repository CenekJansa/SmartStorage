package com.example.SecureStorage.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.SecureStorage.domain.entity.StorageItem;

public interface StorageItemRepository extends JpaRepository<StorageItem, Long> {

    
}
