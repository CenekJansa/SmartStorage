package com.example.SecureStorage.domain.service;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SecureStorage.commons.OperationResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AIDocumentProcessingService {
    
    @Autowired
    private ChatClient.Builder chatClientBuilder;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Process a PDF document using AI to extract structured data
     * 
     * @param pdfText the extracted text from the PDF
     * @param sectionAttributes the list of attributes for the section
     * @return Map containing "name" (String) and "metadata" (Map<String, String>)
     * @throws Exception if AI processing fails
     */
    public OperationResult<Map<String, Object>> processDocument(String pdfText, List<String> sectionAttributes){
        try {
            String prompt = buildPrompt(pdfText, sectionAttributes);
            ChatClient chatClient = chatClientBuilder.build();
            String aiResponse = chatClient.prompt()
                .user(prompt)
                .call()
                .content();
            Map<String, Object> result = parseAIResponse(aiResponse);
            return OperationResult.success(result);
        } catch (Exception e) {
            return OperationResult.error("AI processing failed: " + e.getMessage());
        }
    }
    
    /**
     * Build the prompt for AI using the user's template
     */
    private String buildPrompt(String pdfText, List<String> sectionAttributes) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Your task is to read content of the document and create a json object from it.\n\n");
        prompt.append("The content of the file:\n");
        prompt.append("///\n");
        prompt.append(pdfText);
        prompt.append("\n///\n\n");
        prompt.append("Based on the file fill in the json object. Pay attention to the data types.\n\n");
        prompt.append("The section has the following attributes: ");
        prompt.append(String.join(", ", sectionAttributes));
        prompt.append("\n\n");
        prompt.append("JSON object structure:\n");
        prompt.append("{\n");
        prompt.append("    \"name\": \"name of the item\",\n");
        prompt.append("    \"metadata\": {\n");
        
        // Add each attribute as a key in metadata
        for (int i = 0; i < sectionAttributes.size(); i++) {
            String attr = sectionAttributes.get(i);
            prompt.append("        \"").append(attr).append("\": \"value\"");
            if (i < sectionAttributes.size() - 1) {
                prompt.append(",");
            }
            prompt.append("\n");
        }
        
        prompt.append("    }\n");
        prompt.append("}\n\n");
        prompt.append("Return ONLY the JSON object, no additional text or explanation.");
        
        return prompt.toString();
    }
    
    /**
     * Parse AI response into structured data
     */
    private Map<String, Object> parseAIResponse(String aiResponse) throws Exception {
        // Clean the response - remove markdown code blocks if present
        String cleanedResponse = aiResponse.trim();
        if (cleanedResponse.startsWith("```json")) {
            cleanedResponse = cleanedResponse.substring(7);
        }
        if (cleanedResponse.startsWith("```")) {
            cleanedResponse = cleanedResponse.substring(3);
        }
        if (cleanedResponse.endsWith("```")) {
            cleanedResponse = cleanedResponse.substring(0, cleanedResponse.length() - 3);
        }
        cleanedResponse = cleanedResponse.trim();
        
        // Parse JSON
        Map<String, Object> parsed = objectMapper.readValue(
            cleanedResponse, 
            new TypeReference<Map<String, Object>>() {}
        );
        
        // Validate structure
        if (!parsed.containsKey("name")) {
            throw new IllegalArgumentException("AI response missing 'name' field");
        }
        if (!parsed.containsKey("metadata")) {
            throw new IllegalArgumentException("AI response missing 'metadata' field");
        }
        
        return parsed;
    }
}

