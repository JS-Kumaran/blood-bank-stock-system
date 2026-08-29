package com.bloodbank.service;

import com.bloodbank.entity.*;
import com.bloodbank.dto.BloodRequestCreateDto;
import com.bloodbank.dto.BloodRequestResponseDto;
import com.bloodbank.exception.BusinessException;
import com.bloodbank.exception.ResourceNotFoundException;
import com.bloodbank.repository.BloodRequestRepository;
import com.bloodbank.repository.BloodStockRepository;
import com.bloodbank.repository.StockTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BloodRequestService {

    private final BloodRequestRepository requestRepository;
    private final BloodStockRepository bloodStockRepository;
    private final StockTransactionRepository transactionRepository;

    public BloodRequestService(BloodRequestRepository requestRepository,
                              BloodStockRepository bloodStockRepository,
                              StockTransactionRepository transactionRepository) {
        this.requestRepository = requestRepository;
        this.bloodStockRepository = bloodStockRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public BloodRequestResponseDto createRequest(BloodRequestCreateDto dto) {
        BloodGroup bloodGroup = BloodGroup.fromDisplayName(dto.getBloodGroup());
        
        BloodRequest request = new BloodRequest(
            dto.getRequesterName(),
            bloodGroup,
            dto.getRequestedUnits()
        );
        
        BloodRequest saved = requestRepository.save(request);
        return new BloodRequestResponseDto(saved);
    }

    @Transactional
    public BloodRequestResponseDto fulfillRequest(Long requestId) {
        // 1. Find the request
        BloodRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Blood request", "requestId", requestId));

        // 2. Check if already processed
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new BusinessException(
                "Request is already " + request.getStatus().name().toLowerCase() + 
                " and cannot be processed again",
                "REQUEST_ALREADY_PROCESSED"
            );
        }

        BloodGroup bloodGroup = request.getBloodGroup();
        int requestedUnits = request.getRequestedUnits();

        // 3. Find blood stock with pessimistic lock
        BloodStock stock = bloodStockRepository.findByBloodGroup(bloodGroup)
                .orElse(null);

        boolean hasSufficientStock = (stock != null && stock.getAvailableUnits() >= requestedUnits);

        if (!hasSufficientStock) {
            // Reject the request
            request.setStatus(RequestStatus.REJECTED);
            requestRepository.save(request);
            return new BloodRequestResponseDto(request);
        }

        // 4. Fulfill the request
        // Deduct stock
        stock.deductUnits(requestedUnits);
        bloodStockRepository.save(stock);

        // Update request status
        request.setStatus(RequestStatus.FULFILLED);
        requestRepository.save(request);

        // Create OUT transaction
        StockTransaction transaction = new StockTransaction(
            bloodGroup, 
            TransactionType.OUT, 
            requestedUnits
        );
        transactionRepository.save(transaction);

        return new BloodRequestResponseDto(request);
    }

    public List<BloodRequestResponseDto> getAllRequests() {
        return requestRepository.findAll().stream()
                .map(BloodRequestResponseDto::new)
                .collect(Collectors.toList());
    }

    public BloodRequestResponseDto getRequestById(Long requestId) {
        BloodRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Blood request", "requestId", requestId));
        return new BloodRequestResponseDto(request);
    }
}