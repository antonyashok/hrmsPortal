package com.tm.invoice.mongo.repository.impl;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.tm.invoice.mongo.domain.InvoiceAlertDetails;
import com.tm.invoice.mongo.repository.InvoiceAlertDetailRepositoryCustom;

@Repository
public class InvoiceAlertDetailRepositoryImpl implements InvoiceAlertDetailRepositoryCustom {

	private final MongoTemplate mongoTemplate;

    private static final String PO_ID = "poId";
    private static final String ENGMT_ID = "engmtId";
    
    @Inject
    public InvoiceAlertDetailRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

	@Override
	public List<InvoiceAlertDetails> getInvoiceAlertDetails(List<UUID> poIds, List<UUID> engagementIds) {
		Query query = new Query();
		query = query.addCriteria(Criteria.where(PO_ID).in(poIds).and(ENGMT_ID).in(engagementIds));
		return mongoTemplate.find(query, InvoiceAlertDetails.class);
	}
	
}
