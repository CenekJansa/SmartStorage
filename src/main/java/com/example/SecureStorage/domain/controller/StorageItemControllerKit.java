package com.example.SecureStorage.domain.controller;

import java.util.Map;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class StorageItemControllerKit {

    @Builder
    @Getter
    @Setter
    public static class StorageItemResult {
        private Long id;
        private String name;
        private String fileName;
        private Map<String, String> metadata;
    }

    @Getter
    @Setter
    public static class UploadResult {
        private Boolean success;
        private Long attachmentId;
        private String errorMessage;
    }

    // Mappers

    public static class StorageItemResultMapper {
        public static StorageItemResult mapFrom(
                com.example.SecureStorage.domain.service.StorageItemServiceKit.StorageItemResultVo vo) {
            Map<String, Object> sourceMetadata = vo.getMetadata();
            Map<String, String> stringMetadata = sourceMetadata.entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> entry.getValue() != null ? entry.getValue().toString() : null));
            return StorageItemResult.builder()
                    .id(vo.getId())
                    .name(vo.getName())
                    .metadata(stringMetadata)
                    .build();
        }
    }
}
