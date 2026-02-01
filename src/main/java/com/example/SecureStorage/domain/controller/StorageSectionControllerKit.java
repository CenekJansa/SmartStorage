package com.example.SecureStorage.domain.controller;

import java.util.Map;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class StorageSectionControllerKit {

    @Getter
    @Setter
    public static class StorageSectionResult {
        private UUID id;
        private String name;
        private Map<String, String> attributes;
    }

}
