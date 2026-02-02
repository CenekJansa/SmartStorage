package com.example.SecureStorage.domain.service;

import java.util.Map;

public class StorageItemServiceKit {

    public static class StorageItemResultVo {
        private Long id;
        private String name;
        private String fileName;
        private Map<String, String> metadata;
    }
}
