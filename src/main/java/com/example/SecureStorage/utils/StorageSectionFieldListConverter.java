package com.example.SecureStorage.utils;

import com.example.SecureStorage.domain.entity.StorageSectionField;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Converter
public class StorageSectionFieldListConverter implements AttributeConverter<List<StorageSectionField>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TypeReference<List<StorageSectionField>> typeRef = new TypeReference<>() {};

    @Override
    public String convertToDatabaseColumn(List<StorageSectionField> fields) {
        if (fields == null || fields.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(fields);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize List<StorageSectionField> to JSON", e);
        }
    }

    @Override
    public List<StorageSectionField> convertToEntityAttribute(String json) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Could not deserialize JSON to List<StorageSectionField>", e);
        }
    }
}
