package com.example.SecureStorage.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for AI prompt templates and response parsing.
 * Allows customization of prompts without code changes.
 */
@Configuration
@ConfigurationProperties(prefix = "ai.prompt")
@Getter
@Setter
public class AiPromptConfig {

    /**
     * Boundary marker used to delimit JSON response from AI
     */
    private String responseBoundary = "|||";

    /**
     * Markdown JSON code block prefix
     */
    private String markdownJsonPrefix = "```json";

    /**
     * Markdown code block prefix
     */
    private String markdownPrefix = "```";

    /**
     * Template for the main instruction
     */
    private String instructionTemplate = "Your task is to read content of the document and create a json object from it.\n\n";

    /**
     * Template for content section
     */
    private String contentSectionTemplate = "The content of the file:\n///\n{pdfText}\n///\n\n";

    /**
     * Template for filling instruction
     */
    private String fillingInstructionTemplate = "Based on the file fill in the json object. Pay attention to the data types.\n\n";

    /**
     * Template for attributes list
     */
    private String attributesTemplate = "The section has the following attributes: {attributes}\n\n";

    /**
     * Template for JSON structure header
     */
    private String jsonStructureHeader = "JSON object structure:\n";

    /**
     * Template for JSON object structure
     */
    private String jsonObjectTemplate = "{\n    \"name\": \"name of the item\",\n    \"metadata\": {\n{attributesList}\n    }\n}\n\n";

    /**
     * Template for individual attribute in metadata
     */
    private String attributeEntryTemplate = "        \"{attributeName}\": \"value\"";

    /**
     * Template for final instruction
     */
    private String finalInstructionTemplate = "Return ONLY the JSON object, no additional text or explanation. The JSON response must be bounded by characters {boundary}\n";

    /**
     * Template for example
     */
    private String exampleTemplate = "For example: {boundary} { \"name\": \"...\", \"metadata\": { ... } } {boundary}";
}

