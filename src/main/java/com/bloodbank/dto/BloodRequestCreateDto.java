package com.bloodbank.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BloodRequestCreateDto {

    @NotBlank(message = "Requester name is required")
    private String requesterName;

    @NotBlank(message = "Blood group is required")
    private String bloodGroup;

    @NotNull(message = "Requested units is required")
    @Min(value = 1, message = "Requested units must be greater than 0")
    private Integer requestedUnits;

    // Constructors
    public BloodRequestCreateDto() {}

    public BloodRequestCreateDto(String requesterName, String bloodGroup, Integer requestedUnits) {
        this.requesterName = requesterName;
        this.bloodGroup = bloodGroup;
        this.requestedUnits = requestedUnits;
    }

    // Getters and Setters
    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Integer getRequestedUnits() {
        return requestedUnits;
    }

    public void setRequestedUnits(Integer requestedUnits) {
        this.requestedUnits = requestedUnits;
    }
}