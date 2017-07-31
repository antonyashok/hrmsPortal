package com.tm.invoice.mongo.repository.impl;

import java.util.List;
import java.util.Objects;

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
import com.tm.invoice.mongo.domain.Historical;
import com.tm.invoice.mongo.repository.HistoricalRepositoryCustom;

public class HistoricalRepositoryImpl implements HistoricalRepositoryCustom {

  private final MongoTemplate mongoTemplate;

  @Inject
  public HistoricalRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Page<Historical> getHistoricals(Long billingSpecialistId, Pageable pageable) {
    Query query = new Query();
    Criteria criteria = null;
    Pageable pageableRequest = pageable;
    if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
      String sortBy = InvoiceConstants.CREATED_ON_STR;
      pageableRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
          Sort.Direction.DESC, sortBy);
    }
    if (null != billingSpecialistId) {
      criteria = Criteria.where(InvoiceConstants.BILLING_SPECIALIST_ID).is(billingSpecialistId);
    }
    if (null != criteria) {
      query.addCriteria(criteria);
    }
    query.fields().include(InvoiceConstants.ID_STR).include(InvoiceConstants.INVOICE_QUEUE_ID_STR)
        .include(InvoiceConstants.INVOICE_NUMBER_STR).include(InvoiceConstants.BILL_TO_CLIENT_NAME)
        .include(InvoiceConstants.END_CLIENT_NAME).include(InvoiceConstants.BILLING_SPECIALIST_NAME)
        .include(InvoiceConstants.INVOICE_TYPE_STR).include(InvoiceConstants.TIMESHEET_TYPE_STR)
        .include(InvoiceConstants.BILL_CYCLE).include(InvoiceConstants.LOCATION_STR)
        .include(InvoiceConstants.COUNTRY_STR).include(InvoiceConstants.DELIVERY)
        .include(InvoiceConstants.CURRENCY_TYPE_STR).include(InvoiceConstants.AMOUNT_STR)
        .include(InvoiceConstants.CREATED_ON_STR).include(InvoiceConstants.STATUS)
        .include(InvoiceConstants.TIMESHEET_ATTACHMENT_STR)
        .include(InvoiceConstants.EXPENSES_ATTACHMENT_STR).include(InvoiceConstants.UPDATED_ON_STR);
    query.with(pageableRequest);
    List<Historical> historicals = mongoTemplate.find(query, Historical.class);
    long totalSize = mongoTemplate.count(query, Historical.class);
    return new PageImpl<>(historicals, pageable, totalSize);
  }

}
