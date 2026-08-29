package com.bloodbank.repository;

import com.bloodbank.entity.BloodGroup;
import com.bloodbank.entity.BloodStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface BloodStockRepository extends JpaRepository<BloodStock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT bs FROM BloodStock bs WHERE bs.bloodGroup = :bloodGroup")
    Optional<BloodStock> findByBloodGroupWithLock(@Param("bloodGroup") BloodGroup bloodGroup);

    Optional<BloodStock> findByBloodGroup(BloodGroup bloodGroup);
}