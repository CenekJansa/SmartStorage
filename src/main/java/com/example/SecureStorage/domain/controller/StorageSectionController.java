package com.example.SecureStorage.domain.controller;

import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import com.example.SecureStorage.domain.controller.StorageSectionControllerKit.StorageSectionResult;
import com.example.SecureStorage.domain.controller.StorageSectionControllerKit.StorageSectionResultMapper;
import com.example.SecureStorage.domain.service.StorageSectionService;
import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionInputVo;
import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionResVo;

@Controller
public class StorageSectionController {
    @Autowired
    private StorageSectionService storageSectionService;

    /**
     * Create a new storage section
     * 
     * @param name name of section
     * @param attributes columns of section
     * @return created section
    */
    @MutationMapping
    public StorageSectionResult createStorageSection(@NotNull String name,
         @NotNull Map<String, String> attributes) {
        StorageSectionInputVo inputVo = StorageSectionInputVo.builder()
                .name(name)
                .attributes(attributes)
                .build();
        StorageSectionResVo result = storageSectionService.createStorageSection(inputVo);
        return StorageSectionResultMapper.mapFrom(result);
    }

    // TODO rest of the methods

}
