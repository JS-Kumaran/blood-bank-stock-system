package com.bloodbank.controller;

import com.bloodbank.dto.BloodRequestCreateDto;
import com.bloodbank.dto.BloodRequestResponseDto;
import com.bloodbank.service.BloodRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class BloodRequestController {

    private final BloodRequestService requestService;

    public BloodRequestController(BloodRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ResponseEntity<BloodRequestResponseDto> createRequest(
            @Valid @RequestBody BloodRequestCreateDto request) {
        BloodRequestResponseDto response = requestService.createRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{requestId}/fulfill")
    public ResponseEntity<BloodRequestResponseDto> fulfillRequest(@PathVariable Long requestId) {
        BloodRequestResponseDto response = requestService.fulfillRequest(requestId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BloodRequestResponseDto>> getAllRequests() {
        return ResponseEntity.ok(requestService.getAllRequests());
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<BloodRequestResponseDto> getRequestById(@PathVariable Long requestId) {
        return ResponseEntity.ok(requestService.getRequestById(requestId));
    }
}