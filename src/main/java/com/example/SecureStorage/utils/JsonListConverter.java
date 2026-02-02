package com.example.SecureStorage.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.List;
import java.util.Collections;

@Converter
public class JsonListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TypeReference<List<String>> typeRef = new TypeReference<>() {};

    /**
     * Converts the List<String> to a JSON String for storage in the database.
     */
    @Override
    public String convertToDatabaseColumn(List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not serialize List to JSON string", e);
        }
    }

    /**
     * Converts the JSON String from the database back to a List<String>.
     */
    @Override
    public List<String> convertToEntityAttribute(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(jsonString, typeRef);
        } catch (IOException e) {
            throw new RuntimeException("Could not deserialize JSON string to List", e);
        }
    }
}

