package com.tm.invoice.mongo.repository.impl.test;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.testng.annotations.BeforeMethod;

import com.tm.util.InvoiceConstants;
import com.tm.invoice.domain.InvoiceQueue;
import com.tm.invoice.mongo.repository.impl.InvoiceQueueRepositoryImpl;

public class InvoiceQueueRepositoryImplTest {
	
	@InjectMocks
	private  InvoiceQueueRepositoryImpl  invoiceQueueRepositoryImpl;
	
	@Mock
	private MongoTemplate mongoTemplate;

	@BeforeMethod
	public void setUpInvoiceQueueRepositoryImplTest() throws Exception {

		mongoTemplate = Mockito.mock(MongoTemplate.class);	
		invoiceQueueRepositoryImpl=new InvoiceQueueRepositoryImpl(mongoTemplate);
	}
	
	@Test
	public void getInvoiceQueueByBillingDateAndCronDate()
	{
		List<InvoiceQueue> invoiceQueryList=new ArrayList<InvoiceQueue>();
		InvoiceQueue invoiceQueue=new InvoiceQueue();
		invoiceQueue.setAmount(100.00);
		invoiceQueue.setId(UUID.randomUUID());
		invoiceQueue.setBillDate(new Date());
		invoiceQueue.setBillToClientId(10L);
		
		String billDate="billDate";
		String pOrderId="purchaseOrderId";
		invoiceQueryList.add(invoiceQueue);
		
		Date billingDate=new Date();
		UUID purchaseOrderId=UUID.randomUUID();
		Query query = new Query();
		query.addCriteria(new Criteria().and(billDate).is(billingDate).and(pOrderId).is(purchaseOrderId));
		
		when(mongoTemplate.find(query, InvoiceQueue.class)).thenReturn(invoiceQueryList);
		invoiceQueueRepositoryImpl.getInvoiceQueueByBillingDateAndCronDate(billingDate, purchaseOrderId);
	}
	
	@Test
	public void getOneInvoiceQueueOrderByPurchaseOrderId()
	{
		InvoiceQueue invoiceQueue=new InvoiceQueue();
		invoiceQueue.setAmount(100.00);
		invoiceQueue.setId(UUID.randomUUID());
		invoiceQueue.setBillDate(new Date());
		invoiceQueue.setBillToClientId(10L);
		
		String billDate="billDate";
		String pOrderId="purchaseOrderId";
		UUID purchaseOrderId=UUID.randomUUID();
		
		Query query = new Query();
		query.addCriteria(new Criteria().and(pOrderId).is(purchaseOrderId)).with(new Sort(new Order(Direction.DESC, billDate)));
		when(mongoTemplate.findOne(query, InvoiceQueue.class)).thenReturn(invoiceQueue);
		invoiceQueueRepositoryImpl.getOneInvoiceQueueOrderByPurchaseOrderId(purchaseOrderId);
		
	}
	
	@Test
	public void getAllInvoiceExceptionCount()
	{
		Long lonVal=10L;
		Query query = new Query();
		  query = query.addCriteria(Criteria.where(
				InvoiceConstants.EXCEPTION_SOURCE).in(
				InvoiceConstants.INVOICE_RETURN,
				InvoiceConstants.TIMESHEET_NOT_APPROVAL,
				InvoiceConstants.INVOICE_SETUP_NOT_APPROVAL,
				InvoiceConstants.INVOICE_QUEUE_REJECTED));		
		when(mongoTemplate.count(query, InvoiceQueue.class)).thenReturn(lonVal);
		invoiceQueueRepositoryImpl.getAllInvoiceExceptionCount();
		
	}
	
	@Test(dataProvider="getAllInvoiceQueueTest")
	public void getAllInvoiceQueue(Query query,Pageable pageRequest,
			Page<InvoiceQueue> pageInvoiceQueue,List<InvoiceQueue> myList)
	{
		when(mongoTemplate.find(query, InvoiceQueue.class)).thenReturn(myList);
		invoiceQueueRepositoryImpl.getAllInvoiceQueue(pageRequest);
	}
	
	@Test
	public void getOneInvoiceQueueOrderByInvoiceSetupId()
	{
		UUID invoiceId=UUID.randomUUID();				
		InvoiceQueue invoiceQueue=new InvoiceQueue();
		invoiceQueue.setAmount(100.00);
		invoiceQueue.setId(UUID.randomUUID());
		invoiceQueue.setBillDate(new Date());
		invoiceQueue.setBillToClientId(10L);
		
		String invoiceSetupId="invoiceSetupId";
		String invoiceSetupNumber="invoiceSetupNumber";		
		Query query = new Query();	
		query.addCriteria(new Criteria().and(invoiceSetupId).is(invoiceSetupId)).with(new Sort(new Order(Direction.ASC, invoiceSetupNumber)));
		
		when(mongoTemplate.findOne(query, InvoiceQueue.class)).thenReturn(invoiceQueue);
		invoiceQueueRepositoryImpl.getOneInvoiceQueueOrderByInvoiceSetupId(invoiceId);
	}
	
	
	@DataProvider(name = "getAllInvoiceQueueTest")
	public static Iterator<Object[]> getAllInvoiceQueueTest(){
		Pageable pageRequest = new PageRequest(0, 100, Sort.Direction.DESC,	"auditFields.on");
		Query query = new Query();
		query = query.addCriteria(Criteria.where(
				InvoiceConstants.EXCEPTION_SOURCE).in(
				InvoiceConstants.INVOICE_RETURN,
				InvoiceConstants.TIMESHEET_NOT_APPROVAL,
				InvoiceConstants.INVOICE_SETUP_NOT_APPROVAL,
				InvoiceConstants.INVOICE_QUEUE_REJECTED));
		query.with(pageRequest);		
		
		List<InvoiceQueue> myList =new ArrayList<InvoiceQueue>();	
		InvoiceQueue invoiceQueue=new InvoiceQueue();
		invoiceQueue.setAmount(100.00);
		invoiceQueue.setId(UUID.randomUUID());
		invoiceQueue.setBillDate(new Date());
		invoiceQueue.setBillToClientId(10L);
		myList.add(invoiceQueue);
		Page<InvoiceQueue> pageInvoiceQueue=new PageImpl<InvoiceQueue>(myList);

		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {query,pageRequest,pageInvoiceQueue,myList});
		return testData.iterator();
			
	  }

}
