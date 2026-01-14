package com.aritra.truck_ai_reimburse.service;

import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class OCRService {

    private final Tesseract tesseract;

    public OCRService() {
        tesseract = new Tesseract();

        // ✅ ONLY THIS PATH (NO src/main/resources)
        tesseract.setDatapath("C:/Program Files/Tesseract-OCR/tessdata");
        tesseract.setLanguage("eng");

        tesseract.setTessVariable("user_defined_dpi", "300");
    }

    public String extractText(MultipartFile file) {
        try {
            File tempFile = File.createTempFile("ocr-", file.getOriginalFilename());
            file.transferTo(tempFile);

            String text = tesseract.doOCR(tempFile);
            tempFile.delete();

            log.info("OCR extracted text:\n{}", text);
            return text;

        } catch (Exception e) {
            log.error("OCR failed", e);
            return "";
        }
    }
}
