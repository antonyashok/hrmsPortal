package com.tm.invoice.service;

import java.util.UUID;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.tm.invoice.mongo.domain.Historical;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.repository.HistoricalRepository;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.service.impl.HistoricalServiceImpl;

public class HistoricalServiceTest {
	
	@InjectMocks
	HistoricalServiceImpl historicalServiceImpl;
	
	@Mock
	private HistoricalRepository historicalRepository;
	
	@Mock
	private InvoiceQueueRepository invoiceQueueRepository;
	
	@BeforeTest
	public void setUpHistoricalServiceTest()
	{
		this.historicalRepository=mock(HistoricalRepository.class);
		this.invoiceQueueRepository=mock(InvoiceQueueRepository.class);
		historicalServiceImpl=new HistoricalServiceImpl(historicalRepository, invoiceQueueRepository);
	}

	
	@Test(dataProviderClass = HistoricalServiceTestDataProvider.class, dataProvider = "getHistoricals", description = "")
	public void getHistoricals(Long billingSpecialistId,Pageable pageRequest,Page<Historical> historicalPage)
	{
		when(historicalRepository.getHistoricals(billingSpecialistId, pageRequest)).thenReturn(historicalPage);
		historicalServiceImpl.getHistoricals(billingSpecialistId, pageRequest);
	}
	
	@Test(dataProviderClass = HistoricalServiceTestDataProvider.class, dataProvider = "saveHistoricals", description = "")
	public void saveHistoricals(UUID invoiceQueueId,InvoiceQueue invoiceQueue,Historical historical )
	{
		
		when(invoiceQueueRepository.findOne(invoiceQueueId)).thenReturn(invoiceQueue);
		when(historicalRepository.save(historical)).thenReturn(historical);
		historicalServiceImpl.saveHistoricals(invoiceQueueId);
		
	}
	
	@Test(dataProviderClass = HistoricalServiceTestDataProvider.class, dataProvider = "saveInvoiceHistoricals", description = "")
	public void saveInvoiceHistoricals(UUID invoiceQueueId,InvoiceQueue invoiceQueue,Historical historical,String status,Long employeeId)
	{
		when(invoiceQueueRepository.findOne(invoiceQueueId)).thenReturn(invoiceQueue);
		when(historicalRepository.save(historical)).thenReturn(historical);
		historicalServiceImpl.saveInvoiceHistoricals(invoiceQueueId, status, employeeId);
	}
}
