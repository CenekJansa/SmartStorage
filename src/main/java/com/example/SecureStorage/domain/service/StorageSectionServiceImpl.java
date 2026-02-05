package com.example.SecureStorage.domain.service;

import com.example.SecureStorage.domain.entity.StorageSection;
import com.example.SecureStorage.domain.repository.StorageSectionRepository;
import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionInputVo;
import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionMapper;
import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionResVo;
import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionResVoMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StorageSectionServiceImpl implements StorageSectionService {

    @Autowired
    private StorageSectionRepository jpaRepository;

    @Override
    @Transactional
    public StorageSectionResVo createStorageSection(StorageSectionInputVo inputVo) {
        StorageSection section = StorageSectionMapper.mapFrom(inputVo);
        StorageSection saved = jpaRepository.save(section);
        return StorageSectionResVoMapper.mapFrom(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StorageSectionResVo> retrieveStorageSections() {
        List<StorageSection> sections = jpaRepository.findAll();
        return sections.stream()
                .map(StorageSectionResVoMapper::mapFrom)
                .toList();
    }

}
