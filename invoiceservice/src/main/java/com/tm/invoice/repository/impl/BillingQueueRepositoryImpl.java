package com.tm.invoice.repository.impl;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.mongo.domain.BillingQueue;
import com.tm.invoice.repository.BillingQueueRepositoryCustom;

@Service
public class BillingQueueRepositoryImpl implements BillingQueueRepositoryCustom {

	private final MongoTemplate mongoTemplate;

	@Inject
	public BillingQueueRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Page<BillingQueue> getPendingBillingQueueList(Pageable pageable,
			UUID engagementId, String status) {
		Query query = new Query();
		Criteria criteria;
		if (null != engagementId) {
			criteria = Criteria.where(InvoiceConstants.ENGAGEMENTID)
					.is(engagementId.toString());
			query.addCriteria(criteria);
		}
		if (null != status) {
			criteria = Criteria.where(InvoiceConstants.STATUS).is(status);
			query.addCriteria(criteria);
		}
		query.fields().include(InvoiceConstants.CONTRCTOR_NAME)
				.include(InvoiceConstants.BILLING_QUEUE_ID)
				.include(InvoiceConstants.BILL_TO_CLIENT)
				.include(InvoiceConstants.OFFICE_NAME)
				.include(InvoiceConstants.PROJECT_NAME)
				.include(InvoiceConstants.BILLING_SPECIALIST_ID)
				.include(InvoiceConstants.EFFECTIVE_START_DATE)
				.include(InvoiceConstants.BILLING_SPECIALIST)
				.include(InvoiceConstants.LAST_UPDATED_ON);

		query.with(pageable);
		List<BillingQueue> pendingBillingQueueList = mongoTemplate.find(query,
				BillingQueue.class);
		long totalSize = mongoTemplate.count(query, BillingQueue.class);
		return new PageImpl<>(pendingBillingQueueList, pageable, totalSize);
	}
	
	@Override
	public Page<BillingQueue> getPendingBillingQueueList(Pageable pageable,
			Long employeeId, String status) {
		Query query = new Query();
		Criteria criteria;
		if (null != employeeId) {
			criteria = Criteria.where(InvoiceConstants.EMPLOYEE_ID)
					.is(employeeId);
			query.addCriteria(criteria);
		}
		if (null != status) {
			criteria = Criteria.where(InvoiceConstants.STATUS).is(status);
			query.addCriteria(criteria);
		}
		query.fields().include(InvoiceConstants.CONTRCTOR_NAME)
				.include(InvoiceConstants.BILLING_QUEUE_ID)
				.include(InvoiceConstants.BILL_TO_CLIENT)
				.include(InvoiceConstants.OFFICE_NAME)
				.include(InvoiceConstants.PROJECT_NAME)
				.include(InvoiceConstants.BILLING_SPECIALIST_ID)
				.include(InvoiceConstants.EFFECTIVE_START_DATE)
				.include(InvoiceConstants.EFFECTIVE_END_DATE)
				.include(InvoiceConstants.BILLING_SPECIALIST)
				.include(InvoiceConstants.LAST_UPDATED_ON);

		query.with(pageable);
		List<BillingQueue> pendingBillingQueueList = mongoTemplate.find(query,
				BillingQueue.class);
		long totalSize = mongoTemplate.count(query, BillingQueue.class);
		return new PageImpl<>(pendingBillingQueueList, pageable, totalSize);
	}
	
	
	@Override
	public List<BillingQueue> checkPendingBillingQueue(String engagementId,long employeeId) {
		Query query = new Query();
		Criteria criteria;
			criteria = Criteria.where(InvoiceConstants.ENGAGEMENTID)
					.is(engagementId).andOperator(Criteria.where(InvoiceConstants.EMPLOYEE_ID)
					.is(employeeId).andOperator(Criteria.where("status")
					.in("Active","Pending")));
			query.addCriteria(criteria);

		List<BillingQueue> pendingBillingQueueList = mongoTemplate.find(query,
				BillingQueue.class);
		return pendingBillingQueueList;
	}
	
	@Override
	public List<BillingQueue> checkPendingBillingQueueInActive(String engagementId, long employeeId) {
		Query query = new Query();
		Criteria criteria;
		criteria = Criteria.where(InvoiceConstants.ENGAGEMENTID).is(engagementId)
				.andOperator(Criteria.where(InvoiceConstants.EMPLOYEE_ID).is(employeeId)
						.andOperator(Criteria.where("status").is("Inactive")));
		
		query.addCriteria(criteria);
		
		

		List<BillingQueue> pendingBillingQueueList = mongoTemplate.find(query, BillingQueue.class);
		return pendingBillingQueueList;
	}
	
	@Override
	public List<BillingQueue> checkUpdatePendingBillingQueue(String engagementId,long employeeId,UUID billingQueueId) {
		Query query = new Query();
		Criteria criteria;
			criteria = Criteria.where(InvoiceConstants.ENGAGEMENTID)
					.is(engagementId).andOperator(Criteria.where(InvoiceConstants.EMPLOYEE_ID)
					.is(employeeId).andOperator(Criteria.where(InvoiceConstants.BILLING_QUEUE_ID)
					.nin(billingQueueId).andOperator(Criteria.where("status")
					.in("Active","Pending"))));
			query.addCriteria(criteria);

		List<BillingQueue> pendingBillingQueueList = mongoTemplate.find(query,
				BillingQueue.class);
		return pendingBillingQueueList;
	}

}
