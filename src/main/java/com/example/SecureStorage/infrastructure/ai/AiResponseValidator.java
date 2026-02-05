package com.example.SecureStorage.infrastructure.ai;

import com.example.SecureStorage.commons.OperationResult;

import java.util.Map;

/**
 * Static utility class for validating AI responses.
 * Ensures the response has the required structure.
 */
public final class AiResponseValidator {

    private AiResponseValidator() {
        // Prevent instantiation
    }

    /**
     * Validate that the parsed response has the required structure.
     *
     * @param parsed the parsed response map
     * @return OperationResult indicating validation success or failure
     */
    public static OperationResult<Void> validate(Map<String, Object> parsed) {
        if (parsed == null) {
            return OperationResult.error("Parsed response is null");
        }
        if (!parsed.containsKey("name")) {
            return OperationResult.error("AI response missing 'name' field");
        }
        if (!parsed.containsKey("metadata")) {
            return OperationResult.error("AI response missing 'metadata' field");
        }
        Object metadata = parsed.get("metadata");
        if (!(metadata instanceof Map)) {
            String type = metadata != null ? metadata.getClass().getName() : "null";
            return OperationResult.error("AI response 'metadata' field must be a Map, got: " + type);
        }
        return OperationResult.success(null);
    }
}

