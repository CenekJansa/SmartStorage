package com.example.SecureStorage.utils;

import com.example.SecureStorage.commons.OperationResult;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class PdfTextExtractor {

    public static OperationResult<String> extractTextFromPdf(byte[] pdfData) {
        try (PDDocument document = Loader.loadPDF(pdfData)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            return OperationResult.success(text);
        } catch (Exception e) {
            return OperationResult.error(
                "PDF text extraction failed: " + e.getMessage());
        }
    }
}
