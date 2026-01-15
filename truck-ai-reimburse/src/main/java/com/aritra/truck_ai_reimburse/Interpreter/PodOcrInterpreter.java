package com.aritra.truck_ai_reimburse.Interpreter;

import org.springframework.stereotype.Component;

@Component
public class PodOcrInterpreter {

    public boolean isPodValid(String ocrText, String destinationName) {

        if (ocrText == null || ocrText.isBlank()) {
            return false;
        }

        // very basic rules (can be improved later)
        boolean hasSignature =
                ocrText.toLowerCase().contains("signature")
                        || ocrText.toLowerCase().contains("signed");

        boolean hasDestination =
                ocrText.toLowerCase().contains(destinationName.toLowerCase());

        return hasSignature && hasDestination;
    }

}
