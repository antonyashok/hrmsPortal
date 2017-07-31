package com.tm.invoice.mongo.repository;

import java.util.List;
import java.util.UUID;

import com.tm.invoice.domain.BillingQueue;

public interface BillingQueueRepositoryCustom {

	List<BillingQueue> getBillingQueueByPoId(UUID poNumber);

}
