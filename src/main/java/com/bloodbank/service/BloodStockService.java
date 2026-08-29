package com.bloodbank.service;

import com.bloodbank.dto.StockSummaryDto;
import com.bloodbank.entity.BloodGroup;
import com.bloodbank.entity.BloodStock;
import com.bloodbank.repository.BloodStockRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BloodStockService {

    private final BloodStockRepository bloodStockRepository;

    public BloodStockService(BloodStockRepository bloodStockRepository) {
        this.bloodStockRepository = bloodStockRepository;
    }

    public List<StockSummaryDto> getStockSummary() {
        List<BloodStock> stocks = bloodStockRepository.findAll();
        
        // If no stocks exist, return zero for all blood groups
        if (stocks.isEmpty()) {
            List<StockSummaryDto> allGroups = new ArrayList<>();
            for (BloodGroup group : BloodGroup.values()) {
                allGroups.add(new StockSummaryDto(group.getDisplayName(), 0));
            }
            return allGroups;
        }

        // Map existing stocks
        Map<String, Integer> stockMap = stocks.stream()
                .collect(Collectors.toMap(
                    s -> s.getBloodGroup().getDisplayName(),
                    BloodStock::getAvailableUnits
                ));

        // Ensure all blood groups are included
        List<StockSummaryDto> result = new ArrayList<>();
        for (BloodGroup group : BloodGroup.values()) {
            String displayName = group.getDisplayName();
            int units = stockMap.getOrDefault(displayName, 0);
            result.add(new StockSummaryDto(displayName, units));
        }
        
        return result;
    }
}