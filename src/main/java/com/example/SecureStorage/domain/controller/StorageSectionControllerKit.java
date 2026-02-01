package com.example.SecureStorage.domain.controller;

import java.util.Map;
import java.util.UUID;

import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionResVo;

import lombok.Getter;
import lombok.Setter;

public class StorageSectionControllerKit {

    @Getter
    @Setter
    public static class StorageSectionResult {
        private UUID id;
        private String name;
        private Map<String, String> attributes;
    }

    // Mappers

    public static class StorageSectionResultMapper {
        public static StorageSectionResult mapFrom(StorageSectionResVo resVo) {
            StorageSectionResult result = new StorageSectionResult();
            result.setId(UUID.fromString(resVo.getId()));
            result.setName(resVo.getName());
            result.setAttributes(resVo.getAttributes());
            return result;
        }
    }

}
