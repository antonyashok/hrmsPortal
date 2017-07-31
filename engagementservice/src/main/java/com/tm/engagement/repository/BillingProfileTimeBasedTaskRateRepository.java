package com.tm.engagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.engagement.domain.BillingProfileTimeBasedTaskRate;

public interface BillingProfileTimeBasedTaskRateRepository extends JpaRepository<BillingProfileTimeBasedTaskRate, UUID> {
	
	BillingProfileTimeBasedTaskRate findByEmployeeBillingProfileEmployeeBillingProfileId(UUID employeeBillingProfileId);

}
