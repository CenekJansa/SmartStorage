package com.example.SecureStorage.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.Map;
import java.util.Collections;

// The autoApply=true annotation means this converter will be used for ALL 
// Map<String, String> fields across ALL entities.
// If you only want it for a specific field, remove autoApply=true and use @Convert on the field.
@Converter(autoApply = true)
public class JsonMapConverter implements AttributeConverter<Map<String, String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TypeReference<Map<String, String>> typeRef = new TypeReference<>() {};

    /**
     * Converts the Map<String, String> to a JSON String for storage in the database.
     */
    @Override
    public String convertToDatabaseColumn(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null; // or "{}" if you prefer an empty JSON object
        }
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            // Log the error and/or throw a runtime exception
            throw new RuntimeException("Could not serialize Map to JSON string", e);
        }
    }

    /**
     * Converts the JSON String from the database back to a Map<String, String>.
     */
    @Override
    public Map<String, String> convertToEntityAttribute(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(jsonString, typeRef);
        } catch (IOException e) {
            // Log the error and/or throw a runtime exception
            throw new RuntimeException("Could not deserialize JSON string to Map", e);
        }
    }
}