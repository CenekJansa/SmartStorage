package com.example.SecureStorage.domain.entity;

import java.util.Map;

import org.checkerframework.checker.units.qual.C;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    @Column(nullable = false)
    private Map<String, String> metadata;
    @Column(nullable = false)
    @ManyToOne
    private StoreageSection storageSection;

}
