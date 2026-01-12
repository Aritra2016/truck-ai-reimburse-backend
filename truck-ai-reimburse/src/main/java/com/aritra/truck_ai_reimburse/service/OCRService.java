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
        tesseract.setDatapath("src/main/resources/tessdata"); // tessdata folder
        tesseract.setLanguage("eng");
    }

    public String extractText(MultipartFile file) {
        try {
            File imageFile = convert(file);
            String extractedText = tesseract.doOCR(imageFile);
            log.info("OCR extracted text: {}", extractedText);
            return extractedText;
        } catch (Exception e) {
            log.error("OCR failed", e);
            throw new RuntimeException("Failed to extract text from document");
        }
    }

    private File convert(MultipartFile file) throws IOException {
        File convFile = new File(
                System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename()
        );
        file.transferTo(convFile);
        return convFile;
    }


}
