package com.tm.invoice.mongo.repository.impl;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.tm.invoice.domain.BillingQueue;
import com.tm.invoice.mongo.repository.BillingQueueRepositoryCustom;

@Repository
public class BillingQueueRepositoryImpl implements BillingQueueRepositoryCustom {

    private final MongoTemplate mongoTemplate;
    
    @Inject
    public BillingQueueRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    
   @Override
    public List<BillingQueue> getBillingQueueByPoId(UUID poNumber){
    	Query query = new Query();
             query.addCriteria(Criteria.where("purchaseOrderId").is(poNumber));
             query.addCriteria(Criteria.where("status").is("Active"));
             
    	 List<BillingQueue> invoiceSetupDetailsList = mongoTemplate.find(query, BillingQueue.class);
 		return invoiceSetupDetailsList;
    }

   /* @Override
    public Page<BillingQueue> getPendingBillingQueueList(Pageable pageable,
            Long billingSpecialistId,String status) {
        Query query = new Query();
        Criteria criteria;
        if (null != billingSpecialistId) {
            criteria =
                    Criteria.where(InvoiceConstants.BILLING_SPECIALIST_ID).is(billingSpecialistId);
            query.addCriteria(criteria);
        }
        if (null != status) {
            criteria =
                    Criteria.where(InvoiceConstants.STATUS).is(status);
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
        List<BillingQueue> pendingBillingQueueList =
                mongoTemplate.find(query, BillingQueue.class);
        long totalSize = mongoTemplate.count(query, BillingQueue.class);
        return new PageImpl<>(pendingBillingQueueList, pageable, totalSize);
    }*/
    
   
    
}
