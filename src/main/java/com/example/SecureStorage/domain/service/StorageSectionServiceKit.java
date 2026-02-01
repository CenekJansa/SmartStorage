package com.example.SecureStorage.domain.service;

import java.util.Map;

import com.google.auto.value.AutoValue.Builder;

import lombok.Getter;
import lombok.Setter;

public class StorageSectionServiceKit {
    
    @Getter
    @Setter
    @Builder
    public static class StorageSectionVo {
        private String id;
        private String name;
        private Map<String, String> attributes;
    }
}
