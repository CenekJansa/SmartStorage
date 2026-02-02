package com.example.SecureStorage.domain.controller;

import java.util.List;

import com.example.SecureStorage.domain.service.StorageSectionServiceKit.StorageSectionResVo;

import lombok.Getter;
import lombok.Setter;

public class StorageSectionControllerKit {

    @Getter
    @Setter
    public static class StorageSectionResult {
        private Long id;
        private String name;
        private List<String> attributes;
    }

    // Mappers

    public static class StorageSectionResultMapper {
        public static StorageSectionResult mapFrom(StorageSectionResVo resVo) {
            StorageSectionResult result = new StorageSectionResult();
            result.setId(resVo.getId());
            result.setName(resVo.getName());
            result.setAttributes(resVo.getAttributes());
            return result;
        }
    }

}
