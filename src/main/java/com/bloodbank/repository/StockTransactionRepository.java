package com.bloodbank.repository;

import com.bloodbank.entity.BloodGroup;
import com.bloodbank.entity.StockTransaction;
import com.bloodbank.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    
    List<StockTransaction> findByBloodGroupOrderByTransactionDateDesc(BloodGroup bloodGroup);
    
    List<StockTransaction> findByTransactionTypeOrderByTransactionDateDesc(TransactionType type);
    
    @Query("SELECT t FROM StockTransaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate ORDER BY t.transactionDate DESC")
    List<StockTransaction> findTransactionsBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                                       @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(t.units) FROM StockTransaction t WHERE t.bloodGroup = :bloodGroup AND t.transactionType = :type")
    Integer getTotalUnitsByBloodGroupAndType(@Param("bloodGroup") BloodGroup bloodGroup, 
                                            @Param("type") TransactionType type);
}