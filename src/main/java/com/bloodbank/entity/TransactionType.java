package com.bloodbank.entity;

public enum TransactionType {
    IN("Stock Added"),
    OUT("Stock Deducted"),
    TRANSFER("Stock Transferred");
    
    private final String description;
    
    TransactionType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}