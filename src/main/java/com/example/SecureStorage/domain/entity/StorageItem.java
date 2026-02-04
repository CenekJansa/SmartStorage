package com.example.SecureStorage.domain.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.example.SecureStorage.utils.StringMapJsonAttributeConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class StorageItem extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @OneToMany(mappedBy = "storageItem", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<StorageItemAttachment> attachments = new HashSet<>();
    @Convert(converter = StringMapJsonAttributeConverter.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "metadata_json", columnDefinition = "jsonb")
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();
    @JoinColumn(name = "storage_section_id", nullable = false)
    @ManyToOne
    private StorageSection storageSection;
}
