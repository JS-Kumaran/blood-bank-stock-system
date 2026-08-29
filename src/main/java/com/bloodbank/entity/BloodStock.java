package com.bloodbank.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "blood_stock", uniqueConstraints = {
    @UniqueConstraint(columnNames = "blood_group")
})
public class BloodStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_group", nullable = false, unique = true)
    private BloodGroup bloodGroup;

    @Column(name = "available_units", nullable = false)
    private int availableUnits = 0;

    // Constructors
    public BloodStock() {}

    public BloodStock(BloodGroup bloodGroup, int availableUnits) {
        this.bloodGroup = bloodGroup;
        this.availableUnits = availableUnits;
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

    public int getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(int availableUnits) {
        this.availableUnits = availableUnits;
    }

    public void addUnits(int units) {
        this.availableUnits += units;
    }

    public void deductUnits(int units) {
        if (this.availableUnits < units) {
            throw new IllegalStateException("Insufficient stock");
        }
        this.availableUnits -= units;
    }
}