package com.example.SecureStorage.infrastructure.ai;

import com.example.SecureStorage.commons.OperationResult;
import com.example.SecureStorage.config.AiPromptConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of AiGateway for communicating with external AI service (Gemini).
 * Handles prompt building, AI communication, and response parsing.
 */
@Service
@Slf4j
public class AiGatewayImpl implements AiGateway {

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AiPromptConfig promptConfig;

    /**
     * Extract attributes from PDF text using AI.
     * Builds prompt, calls AI service, and parses response.
     *
     * @param pdfText the text extracted from PDF
     * @param sectionAttributes the list of attributes to extract
     * @return OperationResult containing extracted attributes map
     */
    @Override
    public OperationResult<Map<String, Object>> extractAttributesFromText(String pdfText,
                                                             List<String> sectionAttributes) {
        // 1. Build prompt using static utility
        OperationResult<String> promptResult = AiPromptBuilder.buildExtractionPrompt(
            promptConfig, pdfText, sectionAttributes);
        if (!promptResult.isSuccess()) {
            return OperationResult.error("Failed to build prompt: " + promptResult.getErrorMessage());
        }
        String prompt = promptResult.getData();
        log.debug("Built prompt with {} characters", prompt.length());

        // 2. Call AI service
        OperationResult<String> aiResponseResult = callAiService(prompt);
        if (!aiResponseResult.isSuccess()) {
            return OperationResult.error(aiResponseResult.getErrorMessage());
        }
        String aiResponse = aiResponseResult.getData();
        log.info("Received AI response with {} characters", aiResponse.length());

        // 3. Parse and validate response
        OperationResult<Map<String, Object>> parseResult = parseAndValidateResponse(aiResponse);
        if (!parseResult.isSuccess()) {
            return parseResult;
        }

        Map<String, Object> result = parseResult.getData();
        log.info("AI processing completed successfully. Extracted {} attributes",
            result.containsKey("metadata") ? ((Map<?, ?>) result.get("metadata")).size() : 0);

        return OperationResult.success(result);
    }

    /**
     * Call the AI service with the given prompt.
     *
     * @param prompt the prompt to send
     * @return OperationResult containing the AI response or error
     */
    private OperationResult<String> callAiService(String prompt) {
        try {
            ChatClient chatClient = chatClientBuilder.build();
            log.debug("Sending prompt to AI service");

            String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

            if (response == null || response.isEmpty()) {
                return OperationResult.error("AI service returned empty response");
            }

            return OperationResult.success(response);
        } catch (Exception e) {
            log.error("AI service call failed: {}", e.getMessage(), e);
            return OperationResult.error("AI service call failed: " + e.getMessage());
        }
    }

    /**
     * Parse and validate AI response.
     * Cleans the response, parses JSON, and validates structure.
     *
     * @param aiResponse the raw AI response
     * @return OperationResult containing parsed and validated map or error
     */
    private OperationResult<Map<String, Object>> parseAndValidateResponse(String aiResponse) {
        // 1. Clean the response using static utility
        OperationResult<String> cleanResult = AiResponseCleaner.clean(
            aiResponse,
            promptConfig.getResponseBoundary(),
            promptConfig.getMarkdownJsonPrefix(),
            promptConfig.getMarkdownPrefix()
        );

        if (!cleanResult.isSuccess()) {
            return OperationResult.error("Failed to clean response: " + cleanResult.getErrorMessage());
        }

        String cleanedResponse = cleanResult.getData();
        log.debug("Cleaned response: {}", cleanedResponse);

        // 2. Parse JSON
        OperationResult<Map<String, Object>> parseResult = parseJson(cleanedResponse);
        if (!parseResult.isSuccess()) {
            return parseResult;
        }

        Map<String, Object> parsed = parseResult.getData();

        // 3. Validate structure using static utility
        OperationResult<Void> validationResult = AiResponseValidator.validate(parsed);
        if (!validationResult.isSuccess()) {
            return OperationResult.error("Validation failed: " + validationResult.getErrorMessage());
        }

        return OperationResult.success(parsed);
    }

    /**
     * Parse JSON string into Map.
     *
     * @param jsonString the JSON string to parse
     * @return OperationResult containing parsed map or error
     */
    private OperationResult<Map<String, Object>> parseJson(String jsonString) {
        try {
            Map<String, Object> parsed = objectMapper.readValue(
                jsonString,
                new TypeReference<Map<String, Object>>() {}
            );

            log.debug("Successfully parsed JSON with {} top-level keys", parsed.size());

            return OperationResult.success(parsed);
        } catch (Exception e) {
            log.error("JSON parsing failed: {}", e.getMessage(), e);
            return OperationResult.error("JSON parsing failed: " + e.getMessage());
        }
    }
}
