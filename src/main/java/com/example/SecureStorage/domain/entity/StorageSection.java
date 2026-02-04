package com.example.SecureStorage.domain.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.SecureStorage.utils.JsonListConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
    @Column(name = "attributes_json", columnDefinition = "TEXT")
    @Convert(converter = JsonListConverter.class)
    private List<String> attributes = new ArrayList<>();
    @OneToMany(mappedBy = "storageSection",
     cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StorageItem> storageItems = new HashSet<>();
}
