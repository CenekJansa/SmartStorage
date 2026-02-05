package com.example.SecureStorage.infrastructure;

import com.example.SecureStorage.commons.OperationResult;
import java.util.List;
import java.util.Map;

public interface AiGateway {

    /**
     * Sends a prompt to AI chatbot API.
     *
     * @param pdfText text on which the output is solely based
     * @param sectionAttributes the list of attributes the AI should fill from the text
     * @return Map of filled attributes
     */
    OperationResult<Map<String, Object>> extractAttributesFromText(String pdfText,
                                                                   List<String> sectionAttributes);
}
