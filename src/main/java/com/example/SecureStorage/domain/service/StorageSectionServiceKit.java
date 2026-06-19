package com.example.SecureStorage.domain.service;

import java.util.List;

import com.example.SecureStorage.domain.entity.FieldType;
import com.example.SecureStorage.domain.entity.StorageSection;
import com.example.SecureStorage.domain.entity.StorageSectionDocument;
import com.example.SecureStorage.domain.entity.StorageSectionField;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class StorageSectionServiceKit {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StorageSectionFieldInputVo {
        private String name;
        private FieldType type;
        private boolean identifier;
    }

    @Getter
    @Builder
    public static class StorageSectionFieldResVo {
        private String name;
        private FieldType type;
        private boolean identifier;
    }

    @Getter
    @Setter
    @Builder
    public static class StorageSectionResVo {
        private Long id;
        private String name;
        private List<StorageSectionFieldResVo> attributes;
    }

    @Getter
    @Setter
    @Builder
    public static class StorageSectionInputVo {
        private String name;
        private List<StorageSectionFieldInputVo> attributes;
    }

    // Mappers

    public static class StorageSectionFieldMapper {
        public static StorageSectionField mapFrom(StorageSectionFieldInputVo vo) {
            return StorageSectionField.builder()
                    .name(vo.getName())
                    .type(vo.getType())
                    .isIdentifier(vo.isIdentifier())
                    .build();
        }

        public static StorageSectionFieldResVo mapFrom(StorageSectionField field) {
            return StorageSectionFieldResVo.builder()
                    .name(field.getName())
                    .type(field.getType())
                    .identifier(field.isIdentifier())
                    .build();
        }
    }

    public static class StorageSectionMapper {
        public static StorageSection mapFrom(StorageSectionInputVo inputVo) {
            StorageSection section = new StorageSection();
            section.setName(inputVo.getName());
            section.setAttributes(inputVo.getAttributes().stream()
                    .map(StorageSectionFieldMapper::mapFrom)
                    .toList());
            return section;
        }
    }

    public static class StorageSectionDocumentMapper {
        public static StorageSectionDocument mapFrom(StorageSection section) {
            StorageSectionDocument doc = new StorageSectionDocument();
            doc.setId(section.getId().toString());
            doc.setName(section.getName());
            doc.setAttributes(section.retrieveAttributeNames());
            return doc;
        }
    }

    public static class StorageSectionResVoMapper {
        public static StorageSectionResVo mapFrom(StorageSection section) {
            return StorageSectionResVo.builder()
                    .id(section.getId())
                    .name(section.getName())
                    .attributes(section.getAttributes().stream()
                            .map(StorageSectionFieldMapper::mapFrom)
                            .toList())
                    .build();
        }
    }
}
