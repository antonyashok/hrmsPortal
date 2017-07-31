package com.tm.invoice.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.client.RestTemplate;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.tm.invoice.dto.InvoiceQueueDTO;
import com.tm.invoice.mongo.domain.AuditFields;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.repository.HistoricalRepository;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.service.impl.InvoiceQueueServiceImpl;
import com.tm.invoice.util.MailManager;

public class InvoiceQueueServiceTest {

	@InjectMocks
	InvoiceQueueServiceImpl invoiceQueueServiceImpl;

	private InvoiceQueueRepository invoiceQueueRepository;
	private HistoricalRepository historicalRepository;
	private MailManager mailManager;
	private RestTemplate restTemplate;
	private DiscoveryClient discoveryClient;

	@BeforeMethod
	@BeforeTest
	public void setUp() throws Exception {
		this.invoiceQueueRepository = Mockito.mock(InvoiceQueueRepository.class);
		this.historicalRepository = Mockito.mock(HistoricalRepository.class);
		this.mailManager = Mockito.mock(MailManager.class);
		this.invoiceQueueServiceImpl = new InvoiceQueueServiceImpl(invoiceQueueRepository, historicalRepository,
				mailManager, restTemplate, discoveryClient);

	}

	@Test
	public void getInvoiceQueues() {

		Pageable pageable = null;
		Pageable pageableRequest = pageable;

		AuditFields auditFields = new AuditFields();
		auditFields.setBy(2l);
		auditFields.setOn(new Date());

		InvoiceQueue invoiceQueue = new InvoiceQueue();
		invoiceQueue.setAmount(100);
		invoiceQueue.setUpdated(auditFields);
		List<InvoiceQueue> invoiceQueueList = new ArrayList<>();
		invoiceQueueList.add(invoiceQueue);
		Page<InvoiceQueue> invoiceQueues = new PageImpl<>(invoiceQueueList, pageable, 1);
		List<String> statuses = new ArrayList<>();
		when(invoiceQueueRepository.getInvoiceQueues(Mockito.anyLong(), Mockito.anyListOf(String.class),
				(Pageable) Mockito.anyObject(), Mockito.anyBoolean())).thenReturn(invoiceQueues);
		invoiceQueueServiceImpl.getInvoiceQueues(2l, pageableRequest);
	}

	@Test
	public void updateInvoiceQueueStatus() {
		InvoiceQueueDTO invoiceQueueDTO = new InvoiceQueueDTO();
		invoiceQueueDTO.setComments("comment");
		invoiceQueueDTO.setStatus("Delivered");

		InvoiceQueueDTO invoiceQueueDTO1 = new InvoiceQueueDTO();
		invoiceQueueDTO.setComments("comment");
		invoiceQueueDTO.setStatus("Discarded");

		List<UUID> invoiceQueueIds = new ArrayList<>();
		invoiceQueueIds.add(UUID.randomUUID());
		invoiceQueueIds.add(UUID.randomUUID());
		invoiceQueueRepository.getInvoiceQueues(invoiceQueueIds);
		invoiceQueueDTO.setInvoiceQueueIds(invoiceQueueIds);
		InvoiceQueue invoiceQueue = new InvoiceQueue();
		invoiceQueue.setAmount(100);
		invoiceQueue.setDelivery("Email");
		List<InvoiceQueue> invoiceQueueList = new ArrayList<>();
		invoiceQueueList.add(invoiceQueue);
		when(invoiceQueueRepository.getInvoiceQueues(invoiceQueueIds)).thenReturn(invoiceQueueList);

		AssertJUnit.assertEquals("ok", invoiceQueueServiceImpl.updateInvoiceQueueStatus(invoiceQueueDTO));
		AssertJUnit.assertEquals("ok", invoiceQueueServiceImpl.updateInvoiceQueueStatus(invoiceQueueDTO1));
	}

	@Test
	public void getInvoiceException() {
		Pageable pageable = null;
		Pageable pageableRequest = pageable;

		AuditFields auditFields = new AuditFields();
		auditFields.setBy(2l);
		auditFields.setOn(new Date());

		InvoiceQueue invoiceQueue = new InvoiceQueue();
		invoiceQueue.setAmount(100);
		invoiceQueue.setUpdated(auditFields);
		List<InvoiceQueue> invoiceQueueList = new ArrayList<>();
		invoiceQueueList.add(invoiceQueue);

		Page<InvoiceQueue> invoiceQueues = new PageImpl<>(invoiceQueueList, pageable, 1);
		when(invoiceQueueRepository.getInvoiceException(Mockito.anyLong(), (Pageable) Mockito.anyObject(),
				Mockito.anyBoolean())).thenReturn(invoiceQueues);
		invoiceQueueServiceImpl.getInvoiceException(2l, pageableRequest);
	}

}
