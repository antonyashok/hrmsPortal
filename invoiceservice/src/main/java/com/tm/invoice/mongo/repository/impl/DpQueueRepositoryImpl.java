package com.tm.invoice.mongo.repository.impl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.mongo.domain.DpQueue;
import com.tm.invoice.mongo.repository.DpQueueRepositoryCustom;

public class DpQueueRepositoryImpl implements DpQueueRepositoryCustom {

	private final MongoTemplate mongoTemplate;

	@Inject
	public DpQueueRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Page<DpQueue> getDpQueues(Long billToClientId, String status, Pageable pageable) {
		Query query = new Query();
		Pageable pageableRequest = pageable;
		Criteria criteria = null;
		if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
			String sortBy = InvoiceConstants.UPDATED_ON_STR;
			pageableRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC,
					sortBy);
		}
		if (StringUtils.isNoneBlank(status)) {
			query = query.addCriteria(Criteria.where(InvoiceConstants.STATUS).is(status));
		}
		if (null != billToClientId) {
			criteria = Criteria.where(InvoiceConstants.BILL_TO_CLIENT_ID).is(billToClientId);
		}
		if (null != criteria) {
			query.addCriteria(criteria);
		}
		query.fields().include(InvoiceConstants.ID_STR).include(InvoiceConstants.CONTRACTOR_NAME)
				.include(InvoiceConstants.BILL_TO_CLIENT_NAME).include(InvoiceConstants.LOCATION_NAME)
				.include(InvoiceConstants.ACCOUNT_MANAGER_NAME).include(InvoiceConstants.RECRUITER_NAME)
				.include(InvoiceConstants.START_DATE).include(InvoiceConstants.LINE_OF_BUSINESS_NAME);
		query.with(pageableRequest);
		List<DpQueue> dpQueue = mongoTemplate.find(query, DpQueue.class);
		long totalSize = mongoTemplate.count(query, DpQueue.class);
		return new PageImpl<>(dpQueue, pageableRequest, totalSize);
	}
	

	@Override
	public List<DpQueue> getDpQueues(List<UUID> dpQueueIds) {
		Query query = new Query();
	    Criteria criteria = Criteria.where(InvoiceConstants.ID_STR).in(dpQueueIds);
	    query.addCriteria(criteria);
	    return mongoTemplate.find(query, DpQueue.class);
	}

	@Override
	public List<DpQueue> getNotGenereatedDpQueues(Long billToClientId) {
		Query query = new Query();		
		query = query.addCriteria(Criteria.where(InvoiceConstants.STATUS).is(InvoiceConstants.STATUS_NOT_GENERATED_STR));
		Criteria criteria = null;
		if (null != billToClientId) {
			criteria = Criteria.where(InvoiceConstants.BILL_TO_CLIENT_ID).is(billToClientId);
		}
		if (null != criteria) {
			query.addCriteria(criteria);
		}
		query.fields().include(InvoiceConstants.ID_STR).include(InvoiceConstants.CONTRACTOR_NAME);
		return mongoTemplate.find(query, DpQueue.class);
	}

}
