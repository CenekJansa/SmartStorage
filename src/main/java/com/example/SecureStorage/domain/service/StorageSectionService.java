package com.example.SecureStorage.domain.service;

import java.util.Map;

import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionVo;

public interface StorageSectionService {

    StorageSectionVo createStorageSection(String name, Map<String, String> attributes);

    // TODO rest of the methods
}
