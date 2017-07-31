package com.tm.invoice.mongo.repository.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.tm.invoice.mongo.domain.InvoiceBillWatch;
import com.tm.invoice.mongo.repository.InvoiceBillWatchRepositoryCustom;

public class InvoiceBillWatchRepositoryImpl implements InvoiceBillWatchRepositoryCustom {

	private final MongoTemplate mongoTemplate;

	@Inject
	public InvoiceBillWatchRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<InvoiceBillWatch> getBillWatchByInvoiceNumber(String invoiceNumber) {
		Query query = new Query();
		Criteria criteria = null;

		if (null != invoiceNumber) {
			criteria = Criteria.where("invoiceNumber").is(invoiceNumber);
		}
		query.addCriteria(criteria);
		return mongoTemplate.find(query, InvoiceBillWatch.class);
	}

}
