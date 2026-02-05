package com.example.SecureStorage.infrastructure.ai;

import com.example.SecureStorage.commons.OperationResult;

/**
 * Static utility class for cleaning AI responses.
 * Removes boundaries, markdown code blocks, and extra whitespace.
 */
public final class AiResponseCleaner {

    private AiResponseCleaner() {
        // Prevent instantiation
    }

    /**
     * Clean AI response by removing boundaries and markdown formatting.
     *
     * @param response the raw AI response
     * @param boundary the boundary marker to remove
     * @param markdownJsonPrefix the markdown JSON prefix to remove
     * @param markdownPrefix the markdown prefix to remove
     * @return OperationResult containing cleaned response or error
     */
    public static OperationResult<String> clean(String response, String boundary,
                                                 String markdownJsonPrefix, String markdownPrefix) {
        if (response == null || response.isEmpty()) {
            return OperationResult.error("AI response is null or empty");
        }
        try {
            String cleaned = response.trim();
            cleaned = removeBoundaries(cleaned, boundary);
            cleaned = removeMarkdown(cleaned, markdownJsonPrefix, markdownPrefix);
            cleaned = cleaned.trim();
            if (cleaned.isEmpty()) {
                return OperationResult.error("Cleaned response is empty");
            }
            return OperationResult.success(cleaned);
        } catch (Exception e) {
            return OperationResult.error("Failed to clean AI response: " + e.getMessage());
        }
    }

    /**
     * Remove boundary markers from the response.
     */
    private static String removeBoundaries(String response, String boundary) {
        String result = response;
        if (result.startsWith(boundary)) {
            result = result.substring(boundary.length()).trim();
        }
        if (result.endsWith(boundary)) {
            result = result.substring(0, result.length() - boundary.length()).trim();
        }
        return result;
    }

    /**
     * Remove markdown code block formatting from the response.
     */
    private static String removeMarkdown(String response, String markdownJsonPrefix, String markdownPrefix) {
        String result = response;
        if (result.startsWith(markdownJsonPrefix)) {
            result = result.substring(markdownJsonPrefix.length()).trim();
        }
        if (result.startsWith(markdownPrefix)) {
            result = result.substring(markdownPrefix.length()).trim();
        }
        if (result.endsWith(markdownPrefix)) {
            result = result.substring(0, result.length() - markdownPrefix.length()).trim();
        }
        return result;
    }
}

