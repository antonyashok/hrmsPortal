package com.tm.invoice.mongo.repository.impl.test;

import org.testng.annotations.Test;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.testng.annotations.BeforeMethod;
import com.tm.invoice.domain.BillingQueue;
import com.tm.invoice.mongo.repository.impl.BillingQueueRepositoryImpl;

public class BillingQueueRepositoryImplTest {
	
	@InjectMocks
	private BillingQueueRepositoryImpl billingQueueRepositoryImpl;
	
	@Mock
	private MongoTemplate mongoTemplate;

	@BeforeMethod
	public void setUpBillingQueueRepositoryImplTest() throws Exception {
		
		mongoTemplate = Mockito.mock(MongoTemplate.class);	
		billingQueueRepositoryImpl=new BillingQueueRepositoryImpl(mongoTemplate);
	}
	
	@Test
	public void getBillingQueueByPoId()
	{
		UUID id=UUID.randomUUID();
		UUID poid=UUID.randomUUID();
		List<BillingQueue> invoiceSetupDetailsList=new ArrayList<BillingQueue>();
		BillingQueue billingQueue=new BillingQueue();
		billingQueue.setId(id);
		billingQueue.setPurchaseOrderId(poid);
		invoiceSetupDetailsList.add(billingQueue);		
		Query query = new Query();
		query = query.addCriteria(
				Criteria.where("purchaseOrderId").is(poid).and("status").is("Active"));
		when(mongoTemplate.find(query, BillingQueue.class)).thenReturn(invoiceSetupDetailsList);
		billingQueueRepositoryImpl.getBillingQueueByPoId(id);
	}

}
