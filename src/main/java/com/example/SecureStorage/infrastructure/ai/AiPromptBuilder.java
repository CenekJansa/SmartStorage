package com.example.SecureStorage.infrastructure.ai;

import com.example.SecureStorage.commons.OperationResult;
import com.example.SecureStorage.infrastructure.config.AiPromptConfig;

import java.util.List;

/**
 * Static utility class for building AI prompts using configurable templates.
 * Constructs prompts from configuration without hardcoding.
 */
public final class AiPromptBuilder {

    private AiPromptBuilder() {
        // Prevent instantiation
    }

    /**
     * Build a prompt for extracting attributes from PDF text.
     *
     * @param config the prompt configuration
     * @param pdfText the text extracted from PDF
     * @param sectionAttributes the list of attributes to extract
     * @return OperationResult containing the constructed prompt or error
     */
    public static OperationResult<String> buildExtractionPrompt(AiPromptConfig config,
                                                                 String pdfText,
                                                                 List<String> sectionAttributes) {
        if (config == null) {
            return OperationResult.error("Prompt configuration is null");
        }

        if (pdfText == null || pdfText.trim().isEmpty()) {
            return OperationResult.error("PDF text is null or empty");
        }

        if (sectionAttributes == null || sectionAttributes.isEmpty()) {
            return OperationResult.error("Section attributes list is null or empty");
        }

        try {
            StringBuilder prompt = new StringBuilder();

            prompt.append(config.getInstructionTemplate());

            String contentSection = config.getContentSectionTemplate()
                .replace("{pdfText}", pdfText);
            prompt.append(contentSection);

            prompt.append(config.getFillingInstructionTemplate());

            String attributesSection = config.getAttributesTemplate()
                .replace("{attributes}", String.join(", ", sectionAttributes));
            prompt.append(attributesSection);

            prompt.append(config.getJsonStructureHeader());

            String attributesList = buildAttributesList(config, sectionAttributes);
            String jsonObject = config.getJsonObjectTemplate()
                .replace("{attributesList}", attributesList);
            prompt.append(jsonObject);

            String finalInstruction = config.getFinalInstructionTemplate()
                .replace("{boundary}", config.getResponseBoundary());
            prompt.append(finalInstruction);

            String example = config.getExampleTemplate()
                .replace("{boundary}", config.getResponseBoundary());
            prompt.append(example);

            return OperationResult.success(prompt.toString());
        } catch (Exception e) {
            return OperationResult.error("Failed to build prompt: " + e.getMessage());
        }
    }

    /**
     * Build the attributes list for the JSON template.
     */
    private static String buildAttributesList(AiPromptConfig config, List<String> sectionAttributes) {
        StringBuilder attributesList = new StringBuilder();

        for (int i = 0; i < sectionAttributes.size(); i++) {
            String attr = sectionAttributes.get(i);
            String entry = config.getAttributeEntryTemplate()
                .replace("{attributeName}", attr);
            attributesList.append(entry);

            if (i < sectionAttributes.size() - 1) {
                attributesList.append(",");
            }
            attributesList.append("\n");
        }
        return attributesList.toString();
    }
}

