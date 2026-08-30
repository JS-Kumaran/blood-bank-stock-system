package com.bloodbank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_transactions")
public class StockTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Blood group is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", nullable = false)
    private BloodGroup bloodGroup;
    
    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;
    
    @Min(value = 1, message = "Units must be at least 1")
    @Column(name = "units", nullable = false)
    private Integer units;
    
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
    
    @Column(name = "notes")
    private String notes;
    
    // Default constructor
    public StockTransaction() {
        this.transactionDate = LocalDateTime.now();
    }
    
    // Constructor with fields
    public StockTransaction(BloodGroup bloodGroup, TransactionType transactionType, Integer units) {
        this.bloodGroup = bloodGroup;
        this.transactionType = transactionType;
        this.units = units;
        this.transactionDate = LocalDateTime.now();
    }
    
    // Constructor with all fields
    public StockTransaction(BloodGroup bloodGroup, TransactionType transactionType, 
                           Integer units, String notes) {
        this.bloodGroup = bloodGroup;
        this.transactionType = transactionType;
        this.units = units;
        this.notes = notes;
        this.transactionDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }
    
    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
    
    public TransactionType getTransactionType() {
        return transactionType;
    }
    
    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }
    
    public Integer getUnits() {
        return units;
    }
    
    public void setUnits(Integer units) {
        this.units = units;
    }
    
    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }
    
    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    @PrePersist
    protected void onCreate() {
        this.transactionDate = LocalDateTime.now();
    }
}