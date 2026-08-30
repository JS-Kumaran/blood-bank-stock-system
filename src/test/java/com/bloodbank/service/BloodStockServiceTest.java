package com.bloodbank.service;

import com.bloodbank.dto.BloodStockCreateDto;
import com.bloodbank.entity.BloodGroup;
import com.bloodbank.entity.BloodStock;
import com.bloodbank.entity.StockTransaction;
import com.bloodbank.exception.BusinessException;
import com.bloodbank.exception.ResourceNotFoundException;
import com.bloodbank.repository.BloodStockRepository;
import com.bloodbank.repository.StockTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class BloodStockServiceTest {

    @Mock
    private BloodStockRepository bloodStockRepository;

    @Mock
    private StockTransactionRepository transactionRepository;

    @InjectMocks
    private BloodStockService bloodStockService;

    private BloodStock existingStock;

    @BeforeEach
    void setUp() {
        existingStock = new BloodStock(BloodGroup.A_POSITIVE, 10);
    }

    @Test
    void addStock_ShouldIncreaseAvailableUnits() {
        // Given
        BloodStockCreateDto dto = new BloodStockCreateDto("A+", 5);
        when(bloodStockRepository.findByBloodGroup(BloodGroup.A_POSITIVE))
                .thenReturn(Optional.of(existingStock));
        when(bloodStockRepository.save(any(BloodStock.class))).thenReturn(existingStock);
        when(transactionRepository.save(any(StockTransaction.class))).thenReturn(new StockTransaction());

        // When
        bloodStockService.addStock(dto);

        // Then
        assertThat(existingStock.getAvailableUnits()).isEqualTo(15);
        verify(bloodStockRepository).save(existingStock);
    }

    @Test
    void addStock_ShouldCreateInTransaction() {
        // Given
        BloodStockCreateDto dto = new BloodStockCreateDto("A+", 5);
        when(bloodStockRepository.findByBloodGroup(BloodGroup.A_POSITIVE))
                .thenReturn(Optional.of(existingStock));
        when(bloodStockRepository.save(any(BloodStock.class))).thenReturn(existingStock);
        when(transactionRepository.save(any(StockTransaction.class))).thenReturn(new StockTransaction());

        // When
        bloodStockService.addStock(dto);

        // Then
        verify(transactionRepository).save(any(StockTransaction.class));
    }

    @Test
    void addStock_ShouldCreateNewStockWhenNotExists() {
        // Given
        BloodStockCreateDto dto = new BloodStockCreateDto("B+", 5);
        when(bloodStockRepository.findByBloodGroup(BloodGroup.B_POSITIVE))
                .thenReturn(Optional.empty());
        when(bloodStockRepository.save(any(BloodStock.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionRepository.save(any(StockTransaction.class))).thenReturn(new StockTransaction());

        // When
        bloodStockService.addStock(dto);

        // Then
        verify(bloodStockRepository).save(any(BloodStock.class));
    }

    @Test
    void deductStock_ShouldDeductUnitsAndCreateTransaction() {
        // Given
        when(bloodStockRepository.findByBloodGroup(BloodGroup.A_POSITIVE))
                .thenReturn(Optional.of(existingStock));
        when(bloodStockRepository.save(any(BloodStock.class))).thenReturn(existingStock);
        when(transactionRepository.save(any(StockTransaction.class))).thenReturn(new StockTransaction());

        // When
        bloodStockService.deductStock(BloodGroup.A_POSITIVE, 3);

        // Then
        assertThat(existingStock.getAvailableUnits()).isEqualTo(7);
        verify(bloodStockRepository).save(existingStock);
        verify(transactionRepository).save(any(StockTransaction.class));
    }

    @Test
    void deductStock_ShouldThrowExceptionWhenInsufficientStock() {
        // Given
        when(bloodStockRepository.findByBloodGroup(BloodGroup.A_POSITIVE))
                .thenReturn(Optional.of(existingStock));

        // When & Then
        assertThatThrownBy(() -> bloodStockService.deductStock(BloodGroup.A_POSITIVE, 15))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Insufficient stock");
        
        verify(bloodStockRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void deductStock_ShouldThrowExceptionWhenBloodGroupNotFound() {
        // Given
        when(bloodStockRepository.findByBloodGroup(BloodGroup.A_NEGATIVE))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> bloodStockService.deductStock(BloodGroup.A_NEGATIVE, 5))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void hasSufficientStock_ShouldReturnTrueWhenEnoughStock() {
        // Given
        when(bloodStockRepository.findByBloodGroup(BloodGroup.A_POSITIVE))
                .thenReturn(Optional.of(existingStock));

        // When
        boolean result = bloodStockService.hasSufficientStock(BloodGroup.A_POSITIVE, 5);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void hasSufficientStock_ShouldReturnFalseWhenNotEnoughStock() {
        // Given
        when(bloodStockRepository.findByBloodGroup(BloodGroup.A_POSITIVE))
                .thenReturn(Optional.of(existingStock));

        // When
        boolean result = bloodStockService.hasSufficientStock(BloodGroup.A_POSITIVE, 15);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void hasSufficientStock_ShouldReturnFalseWhenBloodGroupNotFound() {
        // Given
        when(bloodStockRepository.findByBloodGroup(BloodGroup.A_NEGATIVE))
                .thenReturn(Optional.empty());

        // When
        boolean result = bloodStockService.hasSufficientStock(BloodGroup.A_NEGATIVE, 5);

        // Then
        assertThat(result).isFalse();
    }
}