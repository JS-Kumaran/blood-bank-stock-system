package com.bloodbank.dto;

import com.bloodbank.entity.BloodRequest;
import com.bloodbank.entity.BloodGroup;
import com.bloodbank.entity.RequestStatus;
import java.time.LocalDateTime;

public class BloodRequestResponseDto {

    private Long requestId;
    private String requesterName;
    private String bloodGroup;
    private int requestedUnits;
    private String status;
    private LocalDateTime requestDate;

    public BloodRequestResponseDto() {}

    public BloodRequestResponseDto(BloodRequest request) {
        this.requestId = request.getRequestId();
        this.requesterName = request.getRequesterName();
        this.bloodGroup = request.getBloodGroup().getDisplayName();
        this.requestedUnits = request.getRequestedUnits();
        this.status = request.getStatus().name();
        this.requestDate = request.getRequestDate();
    }

    // Getters and Setters
    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

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

    public int getRequestedUnits() {
        return requestedUnits;
    }

    public void setRequestedUnits(int requestedUnits) {
        this.requestedUnits = requestedUnits;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }
}