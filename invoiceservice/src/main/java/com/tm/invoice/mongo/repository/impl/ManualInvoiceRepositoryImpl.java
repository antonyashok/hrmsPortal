package com.tm.invoice.mongo.repository.impl;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.tm.invoice.mongo.domain.ManualInvoice;
import com.tm.invoice.mongo.repository.ManualInvoiceRepositoryCustom;

public class ManualInvoiceRepositoryImpl implements
		ManualInvoiceRepositoryCustom {

	private static final String INVOICE_ID = "invoiceId";
	private final MongoTemplate mongoTemplate;

	@Inject
	public ManualInvoiceRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public List<ManualInvoice> findManualInvoices(List<UUID> invoiceIds) {
		Query query = new Query();
		Criteria criteria = Criteria.where(INVOICE_ID).in(invoiceIds);
		query.addCriteria(criteria);
		return mongoTemplate.find(query, ManualInvoice.class);
	}

	@Override
	public Long getManualInvoiceCountByFinanceRepIdAndStatus(String status, Long financeRepId) {

		Query query = new Query();
		query = query.addCriteria(Criteria.where("financeRepId").is(financeRepId).and("status").is(status));

		return mongoTemplate.count(query, ManualInvoice.class);
	}

}
