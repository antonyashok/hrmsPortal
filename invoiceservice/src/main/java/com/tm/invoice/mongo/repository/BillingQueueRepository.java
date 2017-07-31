package com.tm.invoice.mongo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.tm.invoice.mongo.domain.BillingQueue;
import com.tm.invoice.repository.BillingQueueRepositoryCustom;

@Repository
public interface BillingQueueRepository extends MongoRepository<BillingQueue, UUID>, BillingQueueRepositoryCustom {

	@Query("{'pendingBillingQueueId':?0}")
	BillingQueue getPendingBillingQueueDetail(UUID pendingBillingQueueId);

	List<BillingQueue> findByEngagementIdAndEmployeeIdAndIdNotAndStatusNot(String engagementId, long employeeId,
			UUID id, String status);

	List<BillingQueue> findByEngagementIdAndEmployeeIdAndStatusAndIdNot(String engagementId, long employeeId, String status, UUID id);

	List<BillingQueue> findByContractorIdAndEngagementId(String contractorId, String engagementId);

	List<BillingQueue> findByContractorIdAndEngagementIdAndIdNot(String contractorId, String engagementId, UUID id);

	List<BillingQueue> findByContractorMailIdAndEngagementId(String contractorMailId, String engagementId);

	List<BillingQueue> findByContractorMailIdAndEngagementIdAndIdNot(String contractorMailId, String engagementId,
			UUID id);
}
