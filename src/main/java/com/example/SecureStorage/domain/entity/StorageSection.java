package com.example.SecureStorage.domain.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.SecureStorage.utils.StorageSectionFieldListConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@Entity
@Getter
@Setter
public class StorageSection extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column(name = "attributes_json", columnDefinition = "TEXT")
    @Convert(converter = StorageSectionFieldListConverter.class)
    @Builder.Default
    private List<StorageSectionField> attributes = new ArrayList<>();

    @OneToMany(mappedBy = "storageSection",
     cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StorageItem> storageItems = new HashSet<>();

    public List<String> retrieveAttributeNames() {
        List<String> attributeNames = new ArrayList<>();
        for (StorageSectionField field : attributes) {
            attributeNames.add(field.getName());
        }
        return attributeNames;
    }

    public List<String> retrieveIdentifiers() {
        List<String> identifiers = new ArrayList<>();
        for (StorageSectionField field : attributes) {
            if (field.isIdentifier()) {
                identifiers.add(field.getName());
            }
        }
        return identifiers;
    }
}
