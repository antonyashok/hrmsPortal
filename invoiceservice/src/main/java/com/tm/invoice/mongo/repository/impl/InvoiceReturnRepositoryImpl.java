package com.tm.invoice.mongo.repository.impl;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.repository.InvoiceReturnRepositoryCustom;

public class InvoiceReturnRepositoryImpl implements InvoiceReturnRepositoryCustom {

	private final MongoTemplate mongoTemplate;
	
	@Inject
	public InvoiceReturnRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}
	
	@Override
	public Long getInvoiceReturnCountByReportingManagerIdAndStatus(String status, Long reportingManagerId) {
		
		Query query = new Query();
		query = query.addCriteria(Criteria.where("reportingManagerId").is(reportingManagerId).and("status").is(status));

		return mongoTemplate.count(query, InvoiceQueue.class);
	}
}
