package com.example.SecureStorage.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SecureStorage.domain.entity.StorageSection;
// import com.example.SecureStorage.domain.entity.StorageSectionDocument;
// import com.example.SecureStorage.domain.repository.StorageSectionElasticsearchRepository;
import com.example.SecureStorage.domain.repository.StorageSectionRepository;
// import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionDocumentMapper;
import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionInputVo;
import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionMapper;
import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionResVo;
import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionResVoMapper;

@Service
public class StorageSectionServiceImpl implements StorageSectionService {

    @Autowired
    private StorageSectionRepository jpaRepository;

    // TODO: Re-enable Elasticsearch once basic CRUD is working
    // @Autowired
    // private StorageSectionElasticsearchRepository esRepository;

    @Override
    public StorageSectionResVo createStorageSection(StorageSectionInputVo inputVo) {
        StorageSection section = StorageSectionMapper.mapFrom(inputVo);

        StorageSection saved = jpaRepository.save(section);

        // TODO: Re-enable Elasticsearch indexing once basic CRUD is working
        // StorageSectionDocument doc = StorageSectionDocumentMapper.mapFrom(saved);
        // esRepository.save(doc);

        return StorageSectionResVoMapper.mapFrom(saved);
    }

}
