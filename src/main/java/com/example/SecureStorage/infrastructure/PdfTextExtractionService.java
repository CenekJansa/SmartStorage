package com.example.SecureStorage.infrastructure;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import com.example.SecureStorage.commons.OperationResult;

import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for extracting text from PDF documents.
 * Follows Single Responsibility Principle - only handles PDF text extraction.
 */
@Service
@Slf4j
public class PdfTextExtractionService {

    /**
     * Extract text from PDF using PDFBox
     *
     * @param pdfData the PDF file data
     * @return OperationResult containing extracted text or error message
     */
    public OperationResult<String> extractText(byte[] pdfData) {
        if (pdfData == null || pdfData.length == 0) {
            return OperationResult.error("PDF data is null or empty");
        }

        try (PDDocument document = Loader.loadPDF(pdfData)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            
            if (text == null || text.trim().isEmpty()) {
                log.warn("Extracted text from PDF is empty");
                return OperationResult.error("Extracted text from PDF is empty");
            }
            
            log.info("Successfully extracted {} characters from PDF", text.length());
            return OperationResult.success(text);
        } catch (Exception e) {
            log.error("Failed to extract text from PDF: {}", e.getMessage(), e);
            return OperationResult.error("PDF text extraction failed: " + e.getMessage());
        }
    }
}

