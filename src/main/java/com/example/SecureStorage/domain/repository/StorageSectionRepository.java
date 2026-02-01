package com.example.SecureStorage.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SecureStorage.domain.entity.StorageSection;

@Repository
public interface StorageSectionRepository extends JpaRepository<StorageSection, Long> {
    // Add JPA-specific queries if needed, e.g.:
    // Optional<StorageSection> findByName(String name);
}
