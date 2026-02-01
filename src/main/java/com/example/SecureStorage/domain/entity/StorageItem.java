package com.example.SecureStorage.domain.entity;

import java.util.Map;

import com.example.SecureStorage.utils.JsonMapConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StorageItem extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String fileName;
    @Column(name = "metadata_json", columnDefinition = "TEXT")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> metadata;
    @JoinColumn(name = "storage_section_id", nullable = false)
    @ManyToOne
    private StorageSection storageSection;
}
