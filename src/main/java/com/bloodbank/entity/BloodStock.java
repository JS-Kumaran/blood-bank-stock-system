package com.bloodbank.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "blood_stock", uniqueConstraints = {
    @UniqueConstraint(columnNames = "blood_group")
})
public class BloodStock {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Blood group is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", nullable = false, unique = true)
    private BloodGroup bloodGroup;
    
    @Min(value = 0, message = "Available units cannot be negative")
    @Column(name = "available_units", nullable = false)
    private Integer availableUnits = 0;
    
    @Min(value = 0, message = "Total units cannot be negative")
    @Column(name = "total_units", nullable = false)
    private Integer totalUnits = 0;
    
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;
    
    // Default constructor
    public BloodStock() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    // Constructor with blood group and units
    public BloodStock(BloodGroup bloodGroup, Integer units) {
        this.bloodGroup = bloodGroup;
        this.availableUnits = units;
        this.totalUnits = units;
        this.lastUpdated = LocalDateTime.now();
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
    
    public Integer getAvailableUnits() {
        return availableUnits;
    }
    
    public void setAvailableUnits(Integer availableUnits) {
        this.availableUnits = availableUnits;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public Integer getTotalUnits() {
        return totalUnits;
    }
    
    public void setTotalUnits(Integer totalUnits) {
        this.totalUnits = totalUnits;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    // Business methods
    public void addUnits(Integer units) {
        if (units != null && units > 0) {
            this.availableUnits += units;
            this.totalUnits += units;
            this.lastUpdated = LocalDateTime.now();
        }
    }
    
    public void deductUnits(Integer units) {
        if (units != null && units > 0) {
            if (this.availableUnits < units) {
                throw new IllegalStateException("Insufficient stock. Available: " + 
                    this.availableUnits + ", Requested: " + units);
            }
            this.availableUnits -= units;
            this.lastUpdated = LocalDateTime.now();
        }
    }
    
    public boolean hasSufficientStock(Integer requiredUnits) {
        return this.availableUnits >= requiredUnits;
    }
    
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "BloodStock{" +
                "id=" + id +
                ", bloodGroup=" + bloodGroup +
                ", availableUnits=" + availableUnits +
                ", totalUnits=" + totalUnits +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}