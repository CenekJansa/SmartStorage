package com.example.SecureStorage.domain.entity;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Getter;
import lombok.Setter;

@Document(indexName = "storage_sections")
@Getter
@Setter
public class StorageSectionDocument {
    private String id;
    private String name;

    @Field(type = FieldType.Keyword)
    private List<String> attributes;

    // Note: We don't include storageItems here for search purposes, as it's a relationship
    // If needed, we can add item counts or summaries
}