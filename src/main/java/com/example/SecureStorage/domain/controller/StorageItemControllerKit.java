package com.example.SecureStorage.domain.controller;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.SecureStorage.domain.service.StorageItemServiceKit.StorageItemResultVo;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
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
        public static StorageItemResult mapFrom(@NonNull StorageItemResultVo vo) {
            Map<String, Object> sourceMetadata = vo.getMetadata();
            Map<String, String> stringMetadata = null;
            if (sourceMetadata == null) {
                stringMetadata = Collections.emptyMap();
            } else {
                stringMetadata = sourceMetadata.entrySet().stream()
                        .filter(entry -> entry.getValue() != null)
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                entry -> entry.getValue().toString()));
            }
            return StorageItemResult.builder()
                    .id(vo.getId())
                    .name(vo.getName())
                    .metadata(stringMetadata)
                    .build();
        }
    }
}
