package com.example.SecureStorage.domain.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.example.SecureStorage.utils.JsonMapConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StorageItem extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "storageItem",
     cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<StorageItemAttachment> attachments = new HashSet<>();
    @Column(name = "metadata_json", columnDefinition = "TEXT")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> metadata = new HashMap<>();
    @JoinColumn(name = "storage_section_id", nullable = false)
    @ManyToOne
    private StorageSection storageSection;
}
