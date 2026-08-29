package com.bloodbank.service;

import com.bloodbank.entity.BloodGroup;
import com.bloodbank.entity.BloodStock;
import com.bloodbank.entity.StockTransaction;
import com.bloodbank.entity.TransactionType;
import com.bloodbank.dto.DonationRequest;
import com.bloodbank.repository.BloodStockRepository;
import com.bloodbank.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DonationService {

    private final BloodStockRepository bloodStockRepository;
    private final StockTransactionRepository transactionRepository;

    public DonationService(BloodStockRepository bloodStockRepository, 
                          StockTransactionRepository transactionRepository) {
        this.bloodStockRepository = bloodStockRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void recordDonation(DonationRequest request) {
        // Convert blood group string to enum
        BloodGroup bloodGroup = BloodGroup.fromDisplayName(request.getBloodGroup());
        int units = request.getUnits();

        // Find or create BloodStock record
        BloodStock stock = bloodStockRepository.findByBloodGroup(bloodGroup)
                .orElse(new BloodStock(bloodGroup, 0));

        // Update stock
        stock.addUnits(units);
        bloodStockRepository.save(stock);

        // Create transaction
        StockTransaction transaction = new StockTransaction(bloodGroup, TransactionType.IN, units);
        transactionRepository.save(transaction);
    }
}