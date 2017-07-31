package com.tm.engagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.engagement.domain.BillingProfileUnitsBasedTaskRate;

public interface BillingProfileUnitsBasedTaskRateRepository extends JpaRepository<BillingProfileUnitsBasedTaskRate, UUID>{
	
	BillingProfileUnitsBasedTaskRate findByEmployeeBillingProfileEmployeeBillingProfileId(UUID employeeBillingProfileId);

}
