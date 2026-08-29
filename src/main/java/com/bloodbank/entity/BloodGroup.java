package com.bloodbank.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum BloodGroup {
    A_POSITIVE("A+"),
    A_NEGATIVE("A-"),
    B_POSITIVE("B+"),
    B_NEGATIVE("B-"),
    AB_POSITIVE("AB+"),
    AB_NEGATIVE("AB-"),
    O_POSITIVE("O+"),
    O_NEGATIVE("O-");

    private final String displayName;

    BloodGroup(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    public static BloodGroup fromDisplayName(String displayName) {
        for (BloodGroup group : BloodGroup.values()) {
            if (group.displayName.equals(displayName)) {
                return group;
            }
        }
        throw new IllegalArgumentException("Invalid blood group: " + displayName);
    }
}