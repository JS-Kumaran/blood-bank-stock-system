package com.bloodbank.repository;

import com.bloodbank.entity.BloodGroup;
import com.bloodbank.entity.BloodStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.List; // Added missing import
import java.util.Optional;

public interface BloodStockRepository extends JpaRepository<BloodStock, Long> {
    
    Optional<BloodStock> findByBloodGroup(BloodGroup bloodGroup);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BloodStock b WHERE b.bloodGroup = :bloodGroup")
    Optional<BloodStock> findByBloodGroupWithLock(@Param("bloodGroup") BloodGroup bloodGroup);
    
    @Query("SELECT b FROM BloodStock b WHERE b.availableUnits > 0 ORDER BY b.bloodGroup")
    List<BloodStock> findAvailableStock();
}