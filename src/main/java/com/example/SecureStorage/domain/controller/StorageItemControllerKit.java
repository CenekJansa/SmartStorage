package com.example.SecureStorage.domain.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.SecureStorage.domain.service.StorageItemServiceKit.StorageItemResultVo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class StorageItemControllerKit {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class DataPair {
        private String key;
        private String value;
    }

    @Builder
    @Getter
    @Setter
    public static class StorageItemResult {
        private Long id;
        private String name;
        private String fileName;
        private List<DataPair> metadata;
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
            List<DataPair> metadataList;
            if (sourceMetadata == null) {
                metadataList = Collections.emptyList();
            } else {
                metadataList = sourceMetadata.entrySet().stream()
                        .filter(entry -> entry.getValue() != null)
                        .map(entry -> DataPair.builder()
                                .key(entry.getKey())
                                .value(entry.getValue().toString())
                                .build())
                        .sorted((a, b) -> a.getKey().compareTo(b.getKey()))
                        .collect(Collectors.toList());
            }
            return StorageItemResult.builder()
                    .id(vo.getId())
                    .name(vo.getName())
                    .metadata(metadataList)
                    .build();
        }
    }
}
