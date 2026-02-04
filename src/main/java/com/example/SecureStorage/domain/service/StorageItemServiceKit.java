package com.example.SecureStorage.domain.service;

import java.util.Map;

import javax.validation.constraints.NotNull;

import com.example.SecureStorage.domain.entity.StorageItem;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class StorageItemServiceKit {

    @Builder
    @Setter
    @Getter
    public static class StorageItemResultVo {
        private Long id;
        private String name;
        private Map<String, Object> metadata;
    }

    // Mappers

    public static class StorageItemResultVoMapper {
        public static StorageItemResultVo mapFrom(@NotNull StorageItem item) {
            if (item.getMetadata() == null) {
                throw new IllegalArgumentException(
                    "Metadata in StorageItem cannot be null");
            }
            return StorageItemResultVo.builder()
                .id(item.getId())
                .name(item.getName())
                .metadata(item.getMetadata())
                .build();
        }
    }
}
