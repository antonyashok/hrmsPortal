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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.testng.annotations.DataProvider;

import com.tm.invoice.mongo.domain.InvoiceQueue;

public class InvoiceServiceTestDataProvider {
	
	
	/*
	@DataProvider(name = "getReturnRequest")
	public static Iterator<Object[]> getReturnRequest() throws ParseException {
		Pageable pageRequest = new PageRequest(0, 100, Sort.Direction.ASC, "test");
	
		
		List<InvoiceQueue> myList=new ArrayList<InvoiceQueue>();
		
		InvoiceQueue invoice=new InvoiceQueue();
		invoice.setBillDate(new Date());
		invoice.setBillToClientName("test");
		myList.add(invoice);
		
		Page invoiceQueues = new PageImpl<>(myList, pageRequest, 1);
		
		Query query = new Query();
		 query = query.addCriteria(
                Criteria.where("invoiceNumber").is("invoiceNumber")
                        .and("status").is("status"));
		 
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {pageRequest,myList,query,invoiceQueues});
		return testData.iterator();
	}*/
	
	/*@DataProvider(name = "getReturnRequestById")
	public static Iterator<Object[]> getReturnRequestById() throws ParseException {
		UUID invoicequeueid=UUID.randomUUID();
		InvoiceQueue invoiceQueue=new InvoiceQueue();
		invoiceQueue.setId(UUID.randomUUID());
		invoiceQueue.setBillDate(new Date());
		invoiceQueue.setBillToClientId(10L);
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {invoicequeueid,invoiceQueue});
		return testData.iterator();
	}*/
	
	

}
