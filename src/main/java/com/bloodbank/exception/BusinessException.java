package com.bloodbank.exception;

public class BusinessException extends RuntimeException {
    
    private final String errorCode;
    private final String message;
    
    public BusinessException(String message, String errorCode) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }
    
    public BusinessException(String message) {
        super(message);
        this.message = message;
        this.errorCode = "BUSINESS_ERROR";
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
}