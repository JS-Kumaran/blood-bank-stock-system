package com.bloodbank.service;

import com.bloodbank.entity.BloodGroup;
import com.bloodbank.entity.BloodStock;
import com.bloodbank.entity.StockTransaction;
import com.bloodbank.entity.TransactionType;
import com.bloodbank.dto.BloodStockCreateDto;
import com.bloodbank.dto.BloodStockResponseDto;
import com.bloodbank.dto.StockSummaryDto;
import com.bloodbank.exception.BusinessException;
import com.bloodbank.exception.ResourceNotFoundException;
import com.bloodbank.repository.BloodStockRepository;
import com.bloodbank.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BloodStockService {

    private final BloodStockRepository bloodStockRepository;
    private final StockTransactionRepository transactionRepository;

    public BloodStockService(BloodStockRepository bloodStockRepository,
                            StockTransactionRepository transactionRepository) {
        this.bloodStockRepository = bloodStockRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public BloodStockResponseDto addStock(BloodStockCreateDto dto) {
        BloodGroup bloodGroup = BloodGroup.fromDisplayName(dto.getBloodGroup());
        
        // Check if stock already exists for this blood group
        BloodStock existingStock = bloodStockRepository.findByBloodGroup(bloodGroup)
                .orElse(null);
        
        BloodStock stock;
        if (existingStock != null) {
            // Update existing stock
            existingStock.addUnits(dto.getUnits());
            stock = bloodStockRepository.save(existingStock);
        } else {
            // Create new stock
            stock = new BloodStock(bloodGroup, dto.getUnits());
            stock = bloodStockRepository.save(stock);
        }
        
        // Create IN transaction
        StockTransaction transaction = new StockTransaction(
            bloodGroup,
            TransactionType.IN,
            dto.getUnits()
        );
        transactionRepository.save(transaction);
        
        return new BloodStockResponseDto(stock);
    }

    @Transactional
    public BloodStockResponseDto deductStock(BloodGroup bloodGroup, int units) {
        BloodStock stock = bloodStockRepository.findByBloodGroup(bloodGroup)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Blood stock not found for group: " + bloodGroup.getDisplayName()));
        
        if (stock.getAvailableUnits() < units) {
            throw new BusinessException(
                "Insufficient stock. Available: " + stock.getAvailableUnits() + 
                ", Requested: " + units,
                "INSUFFICIENT_STOCK"
            );
        }
        
        stock.deductUnits(units);
        BloodStock updatedStock = bloodStockRepository.save(stock);
        
        // Create OUT transaction
        StockTransaction transaction = new StockTransaction(
            bloodGroup,
            TransactionType.OUT,
            units
        );
        transactionRepository.save(transaction);
        
        return new BloodStockResponseDto(updatedStock);
    }

    public List<BloodStockResponseDto> getAllStock() {
        return bloodStockRepository.findAll().stream()
                .map(BloodStockResponseDto::new)
                .collect(Collectors.toList());
    }

    public BloodStockResponseDto getStockByBloodGroup(BloodGroup bloodGroup) {
        BloodStock stock = bloodStockRepository.findByBloodGroup(bloodGroup)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Blood stock not found for group: " + bloodGroup.getDisplayName()));
        return new BloodStockResponseDto(stock);
    }

    // Get stock summary - ALL null safety issues fixed
    public StockSummaryDto getStockSummary() {
        List<BloodStock> allStock = bloodStockRepository.findAll();
        
        // Calculate total available units - with explicit null checks
        int totalAvailableUnits = 0;
        for (BloodStock stock : allStock) {
            if (stock != null && stock.getAvailableUnits() != null) {
                totalAvailableUnits += stock.getAvailableUnits();
            }
        }
        
        // Calculate total blood bags (count of stock entries)
        int totalBloodBags = allStock.size();
        
        // Get blood group distribution - with explicit null checks
        Map<String, Integer> distribution = new HashMap<>();
        for (BloodStock stock : allStock) {
            if (stock != null && stock.getBloodGroup() != null) {
                String key = stock.getBloodGroup().getDisplayName();
                Integer value = stock.getAvailableUnits() != null ? stock.getAvailableUnits() : 0;
                distribution.put(key, distribution.getOrDefault(key, 0) + value);
            }
        }
        
        // Count total donors (in a real system, this would come from a Donor repository)
        int totalDonors = 0; // Placeholder - implement actual donor count if available
        
        return new StockSummaryDto(totalBloodBags, totalAvailableUnits, totalDonors, distribution);
    }

    // Get all stock quantities - with null safety
    public List<Integer> getAllStockQuantities() {
        List<Integer> quantities = new ArrayList<>();
        List<BloodStock> allStock = bloodStockRepository.findAll();
        for (BloodStock stock : allStock) {
            if (stock != null && stock.getAvailableUnits() != null) {
                quantities.add(stock.getAvailableUnits());
            } else {
                quantities.add(0);
            }
        }
        return quantities;
    }

    // Check if sufficient stock exists
    public boolean hasSufficientStock(BloodGroup bloodGroup, int requiredUnits) {
        Optional<BloodStock> stockOpt = bloodStockRepository.findByBloodGroup(bloodGroup);
        if (stockOpt.isPresent()) {
            BloodStock stock = stockOpt.get();
            return stock.getAvailableUnits() != null && stock.getAvailableUnits() >= requiredUnits;
        }
        return false;
    }

    // Get total available units across all blood groups - with null safety
    public int getTotalAvailableUnits() {
        int total = 0;
        List<BloodStock> allStock = bloodStockRepository.findAll();
        for (BloodStock stock : allStock) {
            if (stock != null && stock.getAvailableUnits() != null) {
                total += stock.getAvailableUnits();
            }
        }
        return total;
    }

    // Get stock count per blood group - with null safety
    public Map<String, Integer> getStockDistribution() {
        Map<String, Integer> distribution = new HashMap<>();
        List<BloodStock> allStock = bloodStockRepository.findAll();
        for (BloodStock stock : allStock) {
            if (stock != null && stock.getBloodGroup() != null) {
                String key = stock.getBloodGroup().getDisplayName();
                Integer value = stock.getAvailableUnits() != null ? stock.getAvailableUnits() : 0;
                distribution.put(key, distribution.getOrDefault(key, 0) + value);
            }
        }
        return distribution;
    }
}