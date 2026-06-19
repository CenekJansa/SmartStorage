package com.example.SecureStorage.domain.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.SecureStorage.configurations.BaseIntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class StorageSectionServiceTests extends BaseIntegrationTest {

    @Autowired
    private StorageSectionService service;

    @Test
    void createStorageSection_nullInput_throwsException() {
        StorageSectionServiceKit.StorageSectionResVo result = service.createStorageSection(null);
        assertNull(result);

    }

    @Test
    void createStorageSection_validInput_finishesSuccessfully() {

    }

    @Test
    void createStorageSection_emptyAttributes_throwsException() {
        StorageSectionServiceKit.StorageSectionInputVo sectionInputVo =
            StorageSectionServiceKit.StorageSectionInputVo.builder()
                .name("test")
                .attributes(List.of())
                .build();
    }

    @Test
    void createStorageSection_nullAttributes_throwsException() {
        StorageSectionServiceKit.StorageSectionInputVo sectionInputVo =
            StorageSectionServiceKit.StorageSectionInputVo.builder()
                .name("test")
                .attributes(null)
                .build();
    }

    @Test
    void createStorageSection_nullName_throwsException() {

    }

    @Test
    void retrieveStorageSections_noFilter_retrievesAllSections() {

    }
}
