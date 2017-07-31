 package com.tm.invoice.mongo.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.domain.InvoiceQueue;
import com.tm.invoice.mongo.repository.InvoiceQueueRepositoryCustom;
 
@Service
public class InvoiceQueueRepositoryImpl implements InvoiceQueueRepositoryCustom {

	private final MongoTemplate mongoTemplate;

	@Inject
	public InvoiceQueueRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<InvoiceQueue> getInvoiceQueueByBillingDateAndCronDate(Date billingDate, UUID purchaseOrderId) {
		Query query = new Query();
		if (Objects.nonNull(billingDate) && Objects.nonNull(purchaseOrderId)) {
			query.addCriteria(new Criteria().and("billDate").is(billingDate).and("purchaseOrderId").is(purchaseOrderId));
		}
		return mongoTemplate.find(query, InvoiceQueue.class);
	}

	@Override
	public InvoiceQueue getOneInvoiceQueueOrderByPurchaseOrderId(UUID purchaseOrderId) {
		Query query = new Query();
		if (Objects.nonNull(purchaseOrderId)) {
			query.addCriteria(new Criteria().and("purchaseOrderId").is(purchaseOrderId)).with(new Sort(new Order(Direction.DESC, "billDate")));
		}
		return mongoTemplate.findOne(query, InvoiceQueue.class);
	}

	@Override
	public long getAllInvoiceExceptionCount (){
		Query query = new Query();
		  query = query.addCriteria(Criteria.where(
				InvoiceConstants.EXCEPTION_SOURCE).in(
				InvoiceConstants.INVOICE_RETURN,
				InvoiceConstants.TIMESHEET_NOT_APPROVAL,
				InvoiceConstants.INVOICE_SETUP_NOT_APPROVAL,
				InvoiceConstants.INVOICE_QUEUE_REJECTED));		
		long invoiceQueueCnt = mongoTemplate.count(query, InvoiceQueue.class);
		return invoiceQueueCnt;
	}
	
	@Override
	public Page<InvoiceQueue> getAllInvoiceQueue(Pageable pageable) {
		Query query = new Query();
		  query = query.addCriteria(Criteria.where(
				InvoiceConstants.EXCEPTION_SOURCE).in(
				InvoiceConstants.INVOICE_RETURN,
				InvoiceConstants.TIMESHEET_NOT_APPROVAL,
				InvoiceConstants.INVOICE_SETUP_NOT_APPROVAL,
				InvoiceConstants.INVOICE_QUEUE_REJECTED));		
 		query.with(pageable);
		List<InvoiceQueue> myList = mongoTemplate.find(query, InvoiceQueue.class);
		long invoiceQueueCnt = mongoTemplate.count(query, InvoiceQueue.class);
		return new PageImpl<>(myList, pageable, invoiceQueueCnt);
	}
	
	@Override
	public InvoiceQueue getOneInvoiceQueueOrderByInvoiceSetupId(UUID invoiceSetupId) {
		Query query = new Query();
		if (Objects.nonNull(invoiceSetupId)) {
			query.addCriteria(new Criteria().and("invoiceSetupId").is(invoiceSetupId)).with(new Sort(new Order(Direction.ASC, "invoiceSetupNumber")));
		}
		return mongoTemplate.findOne(query, InvoiceQueue.class);
	}
	
	
}
