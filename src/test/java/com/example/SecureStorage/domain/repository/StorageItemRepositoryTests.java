package com.example.SecureStorage.domain.repository;

import com.example.SecureStorage.configurations.BaseIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;

public class StorageItemRepositoryTests extends BaseIntegrationTest {

    @Autowired
    private StorageItemRepository repository;

    void findByMetadataAttribute_existingAttribute_returnsMatchingItems() {

    }
}
