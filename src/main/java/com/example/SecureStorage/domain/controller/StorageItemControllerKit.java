package com.example.SecureStorage.domain.controller;

import java.util.Map;

import com.google.auto.value.AutoValue.Builder;

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

}
