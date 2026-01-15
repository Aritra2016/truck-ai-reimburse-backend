package com.aritra.truck_ai_reimburse.exception;

public class BusinessException extends RuntimeException {
    public BusinessException(String s) {
        super(s);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
