package com.bloodbank.dto;

import com.bloodbank.entity.BloodStock;

public class StockSummaryDto {

    private String bloodGroup;
    private int availableUnits;

    public StockSummaryDto() {}

    public StockSummaryDto(BloodStock stock) {
        this.bloodGroup = stock.getBloodGroup().getDisplayName();
        this.availableUnits = stock.getAvailableUnits();
    }

    public StockSummaryDto(String bloodGroup, int availableUnits) {
        this.bloodGroup = bloodGroup;
        this.availableUnits = availableUnits;
    }

    // Getters and Setters
    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public int getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(int availableUnits) {
        this.availableUnits = availableUnits;
    }
}