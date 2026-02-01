package com.example.SecureStorage.domain.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.SecureStorage.domain.entity.StorageSectionDocument;

@Repository
public interface StorageSectionElasticsearchRepository extends ElasticsearchRepository<StorageSectionDocument, String> {
    // Add custom search methods if needed, e.g.:
    // List<StorageSectionDocument> findByName(String name);
}