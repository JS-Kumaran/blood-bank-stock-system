package com.bloodbank.dto;

import java.util.Map;

public class StockSummaryDto {
    
    private Integer totalBloodBags;
    private Integer totalAvailableUnits;
    private Integer totalDonors;
    private Map<String, Integer> bloodGroupDistribution;
    
    // Default constructor
    public StockSummaryDto() {
    }
    
    // Constructor with all fields
    public StockSummaryDto(Integer totalBloodBags, Integer totalAvailableUnits, 
                          Integer totalDonors, Map<String, Integer> bloodGroupDistribution) {
        this.totalBloodBags = totalBloodBags;
        this.totalAvailableUnits = totalAvailableUnits;
        this.totalDonors = totalDonors;
        this.bloodGroupDistribution = bloodGroupDistribution;
    }
    
    // Getters and Setters
    public Integer getTotalBloodBags() {
        return totalBloodBags;
    }
    
    public void setTotalBloodBags(Integer totalBloodBags) {
        this.totalBloodBags = totalBloodBags;
    }
    
    public Integer getTotalAvailableUnits() {
        return totalAvailableUnits;
    }
    
    public void setTotalAvailableUnits(Integer totalAvailableUnits) {
        this.totalAvailableUnits = totalAvailableUnits;
    }
    
    public Integer getTotalDonors() {
        return totalDonors;
    }
    
    public void setTotalDonors(Integer totalDonors) {
        this.totalDonors = totalDonors;
    }
    
    public Map<String, Integer> getBloodGroupDistribution() {
        return bloodGroupDistribution;
    }
    
    public void setBloodGroupDistribution(Map<String, Integer> bloodGroupDistribution) {
        this.bloodGroupDistribution = bloodGroupDistribution;
    }
}