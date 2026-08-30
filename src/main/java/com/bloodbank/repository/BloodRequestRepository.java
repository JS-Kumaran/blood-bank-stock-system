package com.bloodbank.repository;

import com.bloodbank.entity.BloodRequest;
import com.bloodbank.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BloodRequestRepository extends JpaRepository<BloodRequest, Long> {
    
    List<BloodRequest> findByStatus(RequestStatus status);
    
    @Query("SELECT r FROM BloodRequest r WHERE r.status = :status ORDER BY r.requestDate DESC")
    List<BloodRequest> findRecentRequestsByStatus(@Param("status") RequestStatus status);
}