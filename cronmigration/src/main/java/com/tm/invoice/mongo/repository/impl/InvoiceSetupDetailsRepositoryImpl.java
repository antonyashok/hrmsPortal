package com.tm.invoice.mongo.repository.impl;

import java.util.Date;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.tm.invoice.domain.InvoiceSetupDetails;
import com.tm.invoice.mongo.repository.InvoiceSetupDetailsRepositoryCustom;

@Repository
public class InvoiceSetupDetailsRepositoryImpl implements InvoiceSetupDetailsRepositoryCustom {

	private final MongoTemplate mongoTemplate;

	@Inject
	public InvoiceSetupDetailsRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public InvoiceSetupDetails getInvoiceSetupDetailsListByInvoiceSetupId(Long invoiceSetupId, Date runDate) {
		Query invoiceSetupDetailsQuery = new Query();
		invoiceSetupDetailsQuery.addCriteria(Criteria.where("invoiceSetupId").is(invoiceSetupId));
		Criteria criteria = prepareDateSearchCriteria(runDate);
		invoiceSetupDetailsQuery.addCriteria(criteria);
		invoiceSetupDetailsQuery.fields().include("invoiceSetupId").include("poDetailsList");
		return mongoTemplate.findOne(invoiceSetupDetailsQuery, InvoiceSetupDetails.class);

	}

	private Criteria prepareDateSearchCriteria(Date runDate) {
		return new Criteria().andOperator(Criteria.where("poDetailsList.startDate").lte(runDate)
				.andOperator(Criteria.where("poDetailsList.endDate").gte(runDate)));
	}

}
