package com.example.SecureStorage.domain.entity;

import java.util.Map;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StorageSection extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Map<String, String> attributes;
    @Column(nullable = false)
    @OneToMany( mappedBy = "storageSection")
    private Set<StorageItem> storageItems;
}
