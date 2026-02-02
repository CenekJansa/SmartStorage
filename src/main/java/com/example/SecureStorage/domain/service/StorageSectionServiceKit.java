package com.example.SecureStorage.domain.service;

import java.util.List;

import com.example.SecureStorage.domain.entity.StorageSection;
import com.example.SecureStorage.domain.entity.StorageSectionDocument;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class StorageSectionServiceKit {

    @Getter
    @Setter
    @Builder
    public static class StorageSectionResVo {
        private String id;
        private String name;
        private List<String> attributes;
    }

    @Getter
    @Setter
    @Builder
    public static class StorageSectionInputVo {
        private String name;
        private List<String> attributes;
    }

    // Mappers

    public static class StorageSectionMapper {
        public static StorageSection mapFrom(StorageSectionInputVo inputVo) {
            StorageSection section = new StorageSection();
            section.setName(inputVo.getName());
            section.setAttributes(inputVo.getAttributes());
            return section;
        }
    }

    public static class StorageSectionDocumentMapper {
        public static StorageSectionDocument mapFrom(StorageSection section) {
            StorageSectionDocument doc = new StorageSectionDocument();
            doc.setId(section.getId().toString());
            doc.setName(section.getName());
            doc.setAttributes(section.getAttributes());
            return doc;
        }
    }

    public static class StorageSectionResVoMapper {
        public static StorageSectionResVo mapFrom(StorageSection section) {
            return StorageSectionResVo.builder()
                    .id(section.getId().toString())
                    .name(section.getName())
                    .attributes(section.getAttributes())
                    .build();
        }
    }
}
