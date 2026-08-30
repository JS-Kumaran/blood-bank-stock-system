package com.bloodbank.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BloodStockCreateDto {
    
    @NotBlank(message = "Blood group is required")
    private String bloodGroup;
    
    @NotNull(message = "Units are required")
    @Min(value = 1, message = "Units must be at least 1")
    private Integer units;
    
    // Default constructor
    public BloodStockCreateDto() {
    }
    
    // Constructor with fields
    public BloodStockCreateDto(String bloodGroup, Integer units) {
        this.bloodGroup = bloodGroup;
        this.units = units;
    }
    
    // Getters and Setters
    public String getBloodGroup() {
        return bloodGroup;
    }
    
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
    
    public Integer getUnits() {
        return units;
    }
    
    public void setUnits(Integer units) {
        this.units = units;
    }
}