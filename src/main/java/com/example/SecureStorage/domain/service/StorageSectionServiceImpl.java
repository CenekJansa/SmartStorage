package com.example.SecureStorage.domain.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SecureStorage.domain.entity.StorageSection;
import com.example.SecureStorage.domain.entity.StorageSectionDocument;
import com.example.SecureStorage.domain.repository.StorageSectionElasticsearchRepository;
import com.example.SecureStorage.domain.repository.StorageSectionRepository;
import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionVo;

@Service
public class StorageSectionServiceImpl implements StorageSectionService {

    @Autowired
    private StorageSectionRepository jpaRepository;

    @Autowired
    private StorageSectionElasticsearchRepository esRepository;

    @Override
    public StorageSectionVo createStorageSection(String name, Map<String, String> attributes) {
        // Create JPA entity
        StorageSection section = new StorageSection();
        section.setName(name);
        section.setAttributes(attributes);

        // Save to JPA
        StorageSection saved = jpaRepository.save(section);

        // Index to Elasticsearch
        StorageSectionDocument doc = new StorageSectionDocument();
        doc.setId(saved.getId().toString());
        doc.setName(saved.getName());
        doc.setAttributes(saved.getAttributes());
        esRepository.save(doc);

        // Return VO
        return new StorageSectionVo(saved.getId(), saved.getName(), saved.getAttributes());
    }

}
