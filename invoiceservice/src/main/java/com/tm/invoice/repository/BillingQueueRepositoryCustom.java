package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.invoice.mongo.domain.BillingQueue;

public interface BillingQueueRepositoryCustom {

	Page<BillingQueue> getPendingBillingQueueList(Pageable pageable,
			UUID engagementId, String status);
	
	Page<BillingQueue> getPendingBillingQueueList(Pageable pageable,
			Long employeeId, String status);
	
	List<BillingQueue> checkPendingBillingQueue(String engagementId,long employeeId);
	
	List<BillingQueue> checkPendingBillingQueueInActive(String engagementId,long employeeId);
	
	List<BillingQueue> checkUpdatePendingBillingQueue(String engagementId,long employeeId,UUID billingQueueId);
	
}
