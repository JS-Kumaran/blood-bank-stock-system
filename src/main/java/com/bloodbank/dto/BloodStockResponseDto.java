package com.bloodbank.dto;

// Remove this unused import - Line 3
// import com.bloodbank.entity.BloodGroup;

import com.bloodbank.entity.BloodStock;

import java.time.LocalDateTime;

public class BloodStockResponseDto {
    
    private Long id;
    private String bloodGroup;
    private String bloodGroupDisplayName;
    private Integer availableUnits;
    private Integer totalUnits;
    private LocalDateTime lastUpdated;
    
    // Default constructor
    public BloodStockResponseDto() {
    }
    
    // Constructor from entity
    public BloodStockResponseDto(BloodStock stock) {
        if (stock != null) {
            this.id = stock.getId();
            this.bloodGroup = stock.getBloodGroup().name();
            this.bloodGroupDisplayName = stock.getBloodGroup().getDisplayName();
            this.availableUnits = stock.getAvailableUnits();
            this.totalUnits = stock.getTotalUnits();
            this.lastUpdated = stock.getLastUpdated();
        }
    }
    
    // Constructor with all fields
    public BloodStockResponseDto(Long id, String bloodGroup, String bloodGroupDisplayName, 
                                 Integer availableUnits, Integer totalUnits, LocalDateTime lastUpdated) {
        this.id = id;
        this.bloodGroup = bloodGroup;
        this.bloodGroupDisplayName = bloodGroupDisplayName;
        this.availableUnits = availableUnits;
        this.totalUnits = totalUnits;
        this.lastUpdated = lastUpdated;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getBloodGroup() {
        return bloodGroup;
    }
    
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
    
    public String getBloodGroupDisplayName() {
        return bloodGroupDisplayName;
    }
    
    public void setBloodGroupDisplayName(String bloodGroupDisplayName) {
        this.bloodGroupDisplayName = bloodGroupDisplayName;
    }
    
    public Integer getAvailableUnits() {
        return availableUnits;
    }
    
    public void setAvailableUnits(Integer availableUnits) {
        this.availableUnits = availableUnits;
    }
    
    public Integer getTotalUnits() {
        return totalUnits;
    }
    
    public void setTotalUnits(Integer totalUnits) {
        this.totalUnits = totalUnits;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}