package com.example.SecureStorage.domain.service;

import com.example.SecureStorage.domain.entity.AttachmentStatus;
import java.util.List;
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

    @Builder
    @Setter
    @Getter
    public static class AttachmentResultVo{
        private Long id;
        private String name;
        private AttachmentStatus status;
    }

    @Builder
    @Setter
    @Getter
    public static class StorageItemDetailResultVo{
        private Long id;
        private String name;
        private List<AttachmentResultVo> attachments;
        private Map<String, Object> metadata;
    }

    @Builder
    @Setter
    @Getter
    public static class StorageItemEditInputVo {
        private String name;
        private Map<String, Object> metadata;
    }

    // Mappers

    public static class StorageItemResultVoMapper {
        public static StorageItemResultVo mapFrom(@NotNull StorageItem item) {
            // Handle null metadata gracefully by using empty map
            Map<String, Object> metadata = item.getMetadata();
            if (metadata == null) {
                metadata = new java.util.HashMap<>();
            }
            return StorageItemResultVo.builder()
                .id(item.getId())
                .name(item.getName())
                .metadata(metadata)
                .build();
        }
    }
}
