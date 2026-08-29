package com.bloodbank.controller;

import com.bloodbank.dto.StockSummaryDto;
import com.bloodbank.service.BloodStockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class BloodStockController {

    private final BloodStockService stockService;

    public BloodStockController(BloodStockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<List<StockSummaryDto>> getStockSummary() {
        return ResponseEntity.ok(stockService.getStockSummary());
    }
}