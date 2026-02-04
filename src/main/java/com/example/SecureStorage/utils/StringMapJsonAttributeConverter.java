package com.example.SecureStorage.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class StringMapJsonAttributeConverter implements
    AttributeConverter<Map<String, Object>, String> {

    class JpaJacksonObjectMapperFactory {

    static ObjectMapper create() {
        return new ObjectMapper()
            .findAndRegisterModules();
        //  .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature());
    }
}

    @Override
    public String convertToDatabaseColumn(final Map<String, Object> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            ObjectMapper objectMapper = JpaJacksonObjectMapperFactory.create();
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public Map<String, Object> convertToEntityAttribute(final String json) {
        if (StringUtils.isBlank(json)) {
            return new HashMap<>();
        }
        try {
            String unescapedJson = JsonUnescaper.unescape(json);
            ObjectMapper objectMapper = JpaJacksonObjectMapperFactory.create();
            return (Map<String, Object>) objectMapper.readValue(unescapedJson, Map.class);
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }
}