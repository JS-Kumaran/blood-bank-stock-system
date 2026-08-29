package com.bloodbank.controller;

import com.bloodbank.dto.DonationRequest;
import com.bloodbank.service.DonationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping
    public ResponseEntity<String> recordDonation(@Valid @RequestBody DonationRequest request) {
        donationService.recordDonation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Donation recorded successfully");
    }
}