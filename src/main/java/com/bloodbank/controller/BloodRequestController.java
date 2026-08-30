package com.bloodbank.controller;

import com.bloodbank.dto.BloodRequestCreateDto;
import com.bloodbank.dto.BloodRequestResponseDto;
import com.bloodbank.service.BloodRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-requests")
public class BloodRequestController {

    private final BloodRequestService bloodRequestService;

    public BloodRequestController(BloodRequestService bloodRequestService) {
        this.bloodRequestService = bloodRequestService;
    }

    @PostMapping
    public ResponseEntity<BloodRequestResponseDto> createRequest(@Valid @RequestBody BloodRequestCreateDto dto) {
        BloodRequestResponseDto response = bloodRequestService.createRequest(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{requestId}/fulfill")
    public ResponseEntity<BloodRequestResponseDto> fulfillRequest(@PathVariable @NonNull Long requestId) {
        BloodRequestResponseDto response = bloodRequestService.fulfillRequest(requestId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BloodRequestResponseDto>> getAllRequests() {
        List<BloodRequestResponseDto> requests = bloodRequestService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<BloodRequestResponseDto> getRequestById(@PathVariable @NonNull Long requestId) {
        BloodRequestResponseDto request = bloodRequestService.getRequestById(requestId);
        return ResponseEntity.ok(request);
    }
}