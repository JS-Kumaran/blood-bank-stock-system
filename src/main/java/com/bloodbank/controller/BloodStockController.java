package com.bloodbank.controller;

import com.bloodbank.dto.BloodStockCreateDto;
import com.bloodbank.dto.BloodStockResponseDto;
import com.bloodbank.dto.StockSummaryDto;
import com.bloodbank.entity.BloodGroup;
import com.bloodbank.service.BloodStockService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blood-stock")
public class BloodStockController {

    private final BloodStockService bloodStockService;

    public BloodStockController(BloodStockService bloodStockService) {
        this.bloodStockService = bloodStockService;
    }

    @PostMapping("/add")
    public ResponseEntity<BloodStockResponseDto> addStock(@Valid @RequestBody BloodStockCreateDto dto) {
        BloodStockResponseDto response = bloodStockService.addStock(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BloodStockResponseDto>> getAllStock() {
        List<BloodStockResponseDto> stockList = bloodStockService.getAllStock();
        return ResponseEntity.ok(stockList);
    }

    @GetMapping("/summary")
    public ResponseEntity<StockSummaryDto> getStockSummary() {
        StockSummaryDto summary = bloodStockService.getStockSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{bloodGroup}")
    public ResponseEntity<BloodStockResponseDto> getStockByBloodGroup(@PathVariable String bloodGroup) {
        BloodGroup group = BloodGroup.fromDisplayName(bloodGroup);
        BloodStockResponseDto stock = bloodStockService.getStockByBloodGroup(group);
        return ResponseEntity.ok(stock);
    }

    @GetMapping("/check/{bloodGroup}/{units}")
    public ResponseEntity<Boolean> hasSufficientStock(@PathVariable String bloodGroup, 
                                                      @PathVariable int units) {
        BloodGroup group = BloodGroup.fromDisplayName(bloodGroup);
        boolean hasStock = bloodStockService.hasSufficientStock(group, units);
        return ResponseEntity.ok(hasStock);
    }

    @PostMapping("/deduct/{bloodGroup}/{units}")
    public ResponseEntity<BloodStockResponseDto> deductStock(@PathVariable String bloodGroup,
                                                             @PathVariable int units) {
        BloodGroup group = BloodGroup.fromDisplayName(bloodGroup);
        BloodStockResponseDto response = bloodStockService.deductStock(group, units);
        return ResponseEntity.ok(response);
    }
}