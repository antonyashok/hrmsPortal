package com.tm.invoice.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.invoice.domain.PoContractorsView;

public interface PoContractorsViewRepository extends JpaRepository<PoContractorsView, Long> {

    Page<PoContractorsView> findByEngagementId(UUID engagementId, Pageable pageRequest);

    @Query("Select poContractorsView from PoContractorsView poContractorsView where poContractorsView.engagementId=:engagementId and poContractorsView.employeeName LIKE %:employeeName%")
    Page<PoContractorsView> findByContractorName(@Param("engagementId")UUID engagementId, @Param("employeeName") String employeeName,
            Pageable pageRequest);

}
