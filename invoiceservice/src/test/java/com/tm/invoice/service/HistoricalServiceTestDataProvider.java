package com.tm.invoice.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.testng.annotations.DataProvider;
import com.tm.invoice.mongo.domain.AuditFields;
import com.tm.invoice.mongo.domain.Historical;
import com.tm.invoice.mongo.domain.InvoiceQueue;

public class HistoricalServiceTestDataProvider {
	
	@DataProvider(name = "getHistoricals")
	public static Iterator<Object[]> getHistoricals() throws ParseException {
		
		Long billingSpecialistId=10L; 
		Historical historical=new Historical();
		historical.setId(UUID.randomUUID());
		historical.setStatus("status");
		historical.setBillingSpecialistId(10L);
		historical.setBillToClientId(10L);
		historical.setBillToClientName(null);
		
		List<Historical> listHistorical=new ArrayList<Historical>();
		listHistorical.add(historical);
		Page<Historical> historicalPage = new PageImpl<Historical>(listHistorical);
		Pageable pageRequest = new PageRequest(0, 100, Sort.Direction.ASC, "alertDate");
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {billingSpecialistId,pageRequest,historicalPage });
		return testData.iterator();
	}
	
	
	@DataProvider(name = "saveHistoricals")
	public static Iterator<Object[]> saveHistoricals() throws ParseException {
		
		UUID invoiceQueueId=(UUID.fromString("017b050c-469d-11e7-a919-92ebcb67fe33"));
	
		InvoiceQueue invoiceQueue=new InvoiceQueue();
		invoiceQueue.setId(UUID.randomUUID());
		invoiceQueue.setBillToClientId(10L);
		invoiceQueue.setBillingSpecialistId(10L);
		invoiceQueue.setStatus("status");
		invoiceQueue.setAttentionManagerName(null);
		
		Historical historical =new Historical();
		historical.setId(UUID.randomUUID());
		historical.setBillingSpecialistId(10L);
		historical.setAmount(500.00);
		historical.setComments(null);
		historical.setBillToClientId(10L);
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {invoiceQueueId,invoiceQueue,historical});
		return testData.iterator();
	}
	
	
	
	@DataProvider(name = "saveInvoiceHistoricals")
	public static Iterator<Object[]> saveInvoiceHistoricals() throws ParseException {
			
		UUID invoiceQueueId=(UUID.fromString("017b050c-469d-11e7-a919-92ebcb67fe33"));
		Long employeeId=10L;
		AuditFields auditFields = new AuditFields();
		auditFields.setBy(employeeId);
		auditFields.setOn(new Date());
		
		InvoiceQueue invoiceQueue=new InvoiceQueue();
		invoiceQueue.setId(UUID.randomUUID());
		invoiceQueue.setBillToClientId(10L);
		invoiceQueue.setBillingSpecialistId(10L);
		invoiceQueue.setStatus("status");
		invoiceQueue.setAttentionManagerName(null);
		
		Historical historical =new Historical();
		historical.setId(UUID.randomUUID());
		historical.setBillingSpecialistId(10L);
		historical.setAmount(500.00);
		historical.setComments(null);
		historical.setBillToClientId(10L);
		historical.setStatus("Approved");	
		
		String status="Approved";
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {invoiceQueueId,invoiceQueue,historical,status,employeeId});
		return testData.iterator();
	}
	
	

}
