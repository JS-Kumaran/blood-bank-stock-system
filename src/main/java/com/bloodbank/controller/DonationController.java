package com.bloodbank.controller;

import com.bloodbank.dto.DonationRequest;
import com.bloodbank.service.DonationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/donations")
@Tag(name = "Donation Management", description = "APIs for recording blood donations")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping
    @Operation(summary = "Record a blood donation", 
               description = "Records a blood donation, increases stock, and creates an IN transaction")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Donation recorded successfully",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request (e.g., units <= 0, invalid blood group)"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> recordDonation(
            @Parameter(description = "Donation request with blood group and units", required = true)
            @Valid @RequestBody DonationRequest request) {
        donationService.recordDonation(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Donation recorded successfully");
    }
}