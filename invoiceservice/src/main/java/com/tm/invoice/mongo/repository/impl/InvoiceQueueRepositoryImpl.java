package com.tm.invoice.mongo.repository.impl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.repository.InvoiceQueueRepositoryCustom;

public class InvoiceQueueRepositoryImpl implements InvoiceQueueRepositoryCustom {

	private final MongoTemplate mongoTemplate;
	
	private static final String STATUS = "status";

	@Inject
	public InvoiceQueueRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Page<InvoiceQueue> getInvoiceQueues(Long billingSpecialistId,
			List<String> statuses, Pageable pageable, boolean isHistorical) {
		Query query = new Query();
		Criteria criteria = null;
		Pageable pageableRequest = pageable;
		if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
			String sortBy = InvoiceConstants.CREATED_ON_STR;
			if (isHistorical) {
				sortBy = InvoiceConstants.UPDATED_ON_STR;
			}
			pageableRequest = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), Sort.Direction.ASC, sortBy);
		}
		if (null != billingSpecialistId) {
			criteria = Criteria.where(InvoiceConstants.BILLING_SPECIALIST_ID)
					.is(billingSpecialistId);
		}
		query = query.addCriteria(Criteria.where(InvoiceConstants.STATUS).in(
				statuses));
		if (null != criteria) {
			query.addCriteria(criteria);
		}
		query.fields().include(InvoiceConstants.ID_STR)
				.include(InvoiceConstants.INVOICE_NUMBER_STR)
				.include(InvoiceConstants.BILL_TO_CLIENT_NAME)
				.include(InvoiceConstants.END_CLIENT_NAME)
				.include(InvoiceConstants.BILLING_SPECIALIST_NAME)
				.include(InvoiceConstants.INVOICE_TYPE_STR)
				.include(InvoiceConstants.TIMESHEET_TYPE_STR)
				.include(InvoiceConstants.BILL_CYCLE)
				.include(InvoiceConstants.LOCATION_STR)
				.include(InvoiceConstants.COUNTRY_STR)
				.include(InvoiceConstants.DELIVERY)
				.include(InvoiceConstants.CURRENCY_TYPE_STR)
				.include(InvoiceConstants.AMOUNT_STR)
				.include(InvoiceConstants.UPDATED_ON_STR)
				.include(InvoiceConstants.STATUS)
				.include(InvoiceConstants.INVOICE_SETUP_NAME_FIELD)
				.include(InvoiceConstants.TIMESHEET_ATTACHMENT_STR)
				.include(InvoiceConstants.EXPENSES_ATTACHMENT_STR)
				.include(InvoiceConstants.UPDATED_ON_STR);
		query.with(pageableRequest);
		List<InvoiceQueue> invoiceQueues = mongoTemplate.find(query,
				InvoiceQueue.class);
		long totalSize = mongoTemplate.count(query, InvoiceQueue.class);
		return new PageImpl<>(invoiceQueues, pageable, totalSize);

	}

	@Override
	public Page<InvoiceQueue> getInvoiceException(Long billingSpecialistId,
			Pageable pageable, Boolean isHistorical) {
		Query query = new Query();
		Pageable pageableRequest = pageable;
		Criteria criteria = null;
		if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
			String sortBy = InvoiceConstants.CREATED_ON_STR;
			if (isHistorical) {
				sortBy = InvoiceConstants.UPDATED_ON_STR;
			}
			pageableRequest = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), Sort.Direction.ASC, sortBy);
		}
		if (null != billingSpecialistId) {
			criteria = Criteria.where(InvoiceConstants.BILLING_SPECIALIST_ID)
					.is(billingSpecialistId);
		}
		if (null != criteria) {
			query.addCriteria(criteria);
		}
		query = query.addCriteria(Criteria.where(
				InvoiceConstants.EXCEPTION_SOURCE).in(
				InvoiceConstants.INVOICE_RETURN,
				InvoiceConstants.TIMESHEET_NOT_APPROVAL,
				InvoiceConstants.STATUS_INSUFFICIENT_BALANCE_STR,
				InvoiceConstants.INVOICE_QUEUE_REJECTED));
		query.fields()
				.include(InvoiceConstants.ID_STR)
				.include(InvoiceConstants.INVOICE_SETUP_NAME_FIELD)
				.include(InvoiceConstants.START_DATE)
				.include(InvoiceConstants.END_DATE)
				.include(InvoiceConstants.BILL_TO_CLIENT_NAME)
				.include(InvoiceConstants.END_CLIENT_NAME)
				.include(InvoiceConstants.BILLING_SPECIALIST_NAME)
				.include(InvoiceConstants.BILL_CYCLE)
				.include(InvoiceConstants.COUNTRY_STR)
				.include(InvoiceConstants.AMOUNT_STR)
				.include(InvoiceConstants.LOCATION_STR)
				.include(InvoiceConstants.ATTENTION_MANAGER_NAME)
				.include(InvoiceConstants.EXCEPTION_DETAIL_CONTRACTOR_NAME)
				.include(InvoiceConstants.EXCEPTION_DETAIL_WEEK_END_DATE)
				.include(InvoiceConstants.EXCEPTION_DETAIL_AMOUNT)
				.include(InvoiceConstants.EXCEPTION_DETAIL_STATUS)
				.include(InvoiceConstants.EXCEPTION_DETAIL_RETURN_COMMENTS)
				.include(
						InvoiceConstants.EXCEPTION_DETAIL_RETURN_APPROVAL_COMMENTS)
				.include(InvoiceConstants.AMOUNT_STR)
				.include(InvoiceConstants.CURRENCY_TYPE_STR)
				.include(InvoiceConstants.EXCEPTION_DETAIL_FILE_NUMBER)
				.include(InvoiceConstants.EXCEPTION_DETAIL_TOTAL_HOURS)
				.include(InvoiceConstants.INV_NUMBER)
				.include(InvoiceConstants.EXCEPTION_DETAIL_CURRENCY_TYPE)
				.include(InvoiceConstants.EXCEPTION_DETAIL_REJECT_COMMMENTS)
				.include(InvoiceConstants.EXCEPTION_SOURCE);
		query.with(pageableRequest);
		List<InvoiceQueue> invoiceQueues = mongoTemplate.find(query,
				InvoiceQueue.class);
		long totalSize = mongoTemplate.count(query, InvoiceQueue.class);
		return new PageImpl<>(invoiceQueues, pageable, totalSize);
	}

	@Override
	public List<InvoiceQueue> getInvoiceQueues(List<UUID> invoiceQueueIds) {
		Query query = new Query();
		Criteria criteria = Criteria.where(InvoiceConstants.ID_STR).in(
				invoiceQueueIds);
		query.addCriteria(criteria);
		return mongoTemplate.find(query, InvoiceQueue.class);
	}

	@Override
	public List<InvoiceQueue> getInvoiceQueuesByInvoiceNumber(
			String invoiceNumber, List<String> statuses) {
		Query query = new Query();
		Criteria criteria = null;

		if (null != invoiceNumber) {
			criteria = Criteria.where("invoiceNumber").is(
					java.util.regex.Pattern.compile(invoiceNumber));
		}
		query = query.addCriteria(Criteria.where(InvoiceConstants.STATUS).in(
				statuses));
		if (null != criteria) {
			query.addCriteria(criteria);
		}
		// query.fields().include(InvoiceConstants.ID_STR).include(InvoiceConstants.INVOICE_NUMBER_STR)
		// .include(InvoiceConstants.BILL_TO_CLIENT_NAME).include(InvoiceConstants.END_CLIENT_NAME)
		// .include(InvoiceConstants.BILLING_SPECIALIST_NAME)
		// .include(InvoiceConstants.INVOICE_TYPE_STR).include(InvoiceConstants.TIMESHEET_TYPE_STR)
		// .include(InvoiceConstants.BILL_CYCLE).include(InvoiceConstants.LOCATION_STR)
		// .include(InvoiceConstants.COUNTRY_STR).include(InvoiceConstants.DELIVERY)
		// .include(InvoiceConstants.CURRENCY_TYPE_STR).include(InvoiceConstants.AMOUNT_STR)
		// .include(InvoiceConstants.UPDATED_ON_STR).include(InvoiceConstants.STATUS)
		// .include(InvoiceConstants.TIMESHEET_ATTACHMENT_STR)
		// .include(InvoiceConstants.EXPENSES_ATTACHMENT_STR).include(InvoiceConstants.UPDATED_ON_STR);

		return mongoTemplate.find(query, InvoiceQueue.class);
	}

	@Override
	public Long getInvoiceQueueCountByBillingSpecialistIdAndStatus(String status, Long billingSpecialistId) {

		Query query = new Query();
		query = query.addCriteria(Criteria.where("billingSpecialistId").is(billingSpecialistId).and(STATUS).is(status));

		return mongoTemplate.count(query, InvoiceQueue.class);
	}

}
