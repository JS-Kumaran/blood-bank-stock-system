package com.bloodbank.service;

import com.bloodbank.dto.BloodRequestCreateDto;
import com.bloodbank.entity.*;
import com.bloodbank.exception.BusinessException;
import com.bloodbank.exception.ResourceNotFoundException;
import com.bloodbank.repository.BloodRequestRepository;
import com.bloodbank.repository.BloodStockRepository;
import com.bloodbank.repository.StockTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")  // Suppress null type safety warnings in tests
class BloodRequestServiceTest {

    @Mock
    private BloodRequestRepository requestRepository;

    @Mock
    private BloodStockRepository bloodStockRepository;

    @Mock
    private StockTransactionRepository transactionRepository;

    @InjectMocks
    private BloodRequestService bloodRequestService;

    private BloodRequest pendingRequest;
    private BloodStock stock;

    @BeforeEach
    void setUp() {
        pendingRequest = new BloodRequest("John Doe", BloodGroup.A_POSITIVE, 5);
        stock = new BloodStock(BloodGroup.A_POSITIVE, 10);
    }

    @Test
    void createRequest_ShouldSetStatusToPending() {
        // Given
        BloodRequestCreateDto dto = new BloodRequestCreateDto();
        dto.setRequesterName("John Doe");
        dto.setBloodGroup("A+");
        dto.setRequestedUnits(5);

        when(requestRepository.save(any(BloodRequest.class))).thenReturn(pendingRequest);

        // When
        bloodRequestService.createRequest(dto);

        // Then
        ArgumentCaptor<BloodRequest> captor = ArgumentCaptor.forClass(BloodRequest.class);
        verify(requestRepository).save(captor.capture());
        BloodRequest savedRequest = captor.getValue();
        assertThat(savedRequest.getStatus()).isEqualTo(RequestStatus.PENDING);
    }

    @Test
    void fulfillRequest_ShouldDecreaseStockAndFulfillRequest() {
        // Given
        when(requestRepository.findById(1L)).thenReturn(Optional.of(pendingRequest));
        when(bloodStockRepository.findByBloodGroup(BloodGroup.A_POSITIVE))
                .thenReturn(Optional.of(stock));
        when(requestRepository.save(any(BloodRequest.class))).thenReturn(pendingRequest);
        when(bloodStockRepository.save(any(BloodStock.class))).thenReturn(stock);
        when(transactionRepository.save(any(StockTransaction.class))).thenReturn(new StockTransaction());

        // When
        bloodRequestService.fulfillRequest(1L);

        // Then
        assertThat(stock.getAvailableUnits()).isEqualTo(5);
        assertThat(pendingRequest.getStatus()).isEqualTo(RequestStatus.FULFILLED);
        verify(bloodStockRepository).save(stock);
        verify(transactionRepository).save(any(StockTransaction.class));
    }

    @Test
    void fulfillRequest_ShouldCreateOutTransaction() {
        // Given
        when(requestRepository.findById(1L)).thenReturn(Optional.of(pendingRequest));
        when(bloodStockRepository.findByBloodGroup(BloodGroup.A_POSITIVE))
                .thenReturn(Optional.of(stock));
        when(requestRepository.save(any(BloodRequest.class))).thenReturn(pendingRequest);
        when(bloodStockRepository.save(any(BloodStock.class))).thenReturn(stock);
        when(transactionRepository.save(any(StockTransaction.class))).thenReturn(new StockTransaction());

        // When
        bloodRequestService.fulfillRequest(1L);

        // Then
        ArgumentCaptor<StockTransaction> captor = ArgumentCaptor.forClass(StockTransaction.class);
        verify(transactionRepository).save(captor.capture());
        StockTransaction transaction = captor.getValue();
        assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.OUT);
        assertThat(transaction.getBloodGroup()).isEqualTo(BloodGroup.A_POSITIVE);
        assertThat(transaction.getUnits()).isEqualTo(5);
    }

    @Test
    void fulfillRequest_ShouldRejectWhenInsufficientStock() {
        // Given
        BloodStock lowStock = new BloodStock(BloodGroup.A_POSITIVE, 3);
        when(requestRepository.findById(1L)).thenReturn(Optional.of(pendingRequest));
        when(bloodStockRepository.findByBloodGroup(BloodGroup.A_POSITIVE))
                .thenReturn(Optional.of(lowStock));
        when(requestRepository.save(any(BloodRequest.class))).thenReturn(pendingRequest);

        // When
        bloodRequestService.fulfillRequest(1L);

        // Then
        assertThat(pendingRequest.getStatus()).isEqualTo(RequestStatus.REJECTED);
        assertThat(lowStock.getAvailableUnits()).isEqualTo(3);
        verify(bloodStockRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void fulfillRequest_ShouldThrowExceptionWhenRequestNotFound() {
        // Given
        when(requestRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bloodRequestService.fulfillRequest(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void fulfillRequest_ShouldThrowExceptionWhenAlreadyFulfilled() {
        // Given
        BloodRequest fulfilledRequest = new BloodRequest("John Doe", BloodGroup.A_POSITIVE, 5);
        fulfilledRequest.setStatus(RequestStatus.FULFILLED);
        
        when(requestRepository.findById(1L)).thenReturn(Optional.of(fulfilledRequest));

        // When & Then
        assertThatThrownBy(() -> bloodRequestService.fulfillRequest(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already fulfilled");
    }

    @Test
    void fulfillRequest_ShouldThrowExceptionWhenAlreadyRejected() {
        // Given
        BloodRequest rejectedRequest = new BloodRequest("John Doe", BloodGroup.A_POSITIVE, 5);
        rejectedRequest.setStatus(RequestStatus.REJECTED);
        
        when(requestRepository.findById(1L)).thenReturn(Optional.of(rejectedRequest));

        // When & Then
        assertThatThrownBy(() -> bloodRequestService.fulfillRequest(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("already rejected");
    }
}