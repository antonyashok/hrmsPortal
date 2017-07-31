package com.tm.invoice.reader;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tm.invoice.domain.GlobalInvoiceSetup;
import com.tm.invoice.domain.InvoiceQueue;
import com.tm.invoice.domain.PoInvoiceSetupDetailsView;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.repository.GlobalInvoiceRepository;
import com.tm.invoice.repository.PoInvoiceSetupDetailsViewRepository;

public class InvoiceEngineReaderTest {
	 
	@InjectMocks
	private InvoiceEngineMigrationReader invoiceEngineServiceImpl;
	
	@Mock
	private PoInvoiceSetupDetailsViewRepository poInvoiceSetupDetailsViewRepository;
	@Mock
	private InvoiceQueueRepository invoiceQueueRepository;

	
	@BeforeMethod
	private void setUp(){ 
		this.poInvoiceSetupDetailsViewRepository = mock(PoInvoiceSetupDetailsViewRepository.class);
		this.invoiceQueueRepository = mock(InvoiceQueueRepository.class);
		invoiceEngineServiceImpl = new InvoiceEngineMigrationReader(poInvoiceSetupDetailsViewRepository, invoiceQueueRepository); 
		
	}
	
	@Test 
	private void prepareInvoiceEngineReader(){
		int fromId = 0; 
		int toId = 10;
		LocalDate runCronDate = LocalDate.now(); 
		Date invoiceLiveDate = new Date();
		Pageable pageableRequest = new PageRequest(fromId, toId, Sort.Direction.ASC, "invoiceStartDate");

		Date sDate = new Date();
		Calendar scalendar = Calendar.getInstance(); 
		scalendar.setTime(sDate); 
		scalendar.add(Calendar.DATE, -1);
		sDate = scalendar.getTime();
		
		
		Date edate = new Date();
		Calendar ecalendar = Calendar.getInstance(); 
		ecalendar.setTime(edate); 
		ecalendar.add(Calendar.DATE, +1);
		edate = ecalendar.getTime();
		
		UUID invoiceSetupId = UUID.randomUUID();
		List<PoInvoiceSetupDetailsView> PoInvoiceSetupDetailsViewList = new ArrayList<>();
		PoInvoiceSetupDetailsView poInvoiceSetDetView = new PoInvoiceSetupDetailsView();
		poInvoiceSetDetView.setProjectId(UUID.randomUUID());
		poInvoiceSetDetView.setPoNumber("123");
		poInvoiceSetDetView.setInvoiceSetupId(invoiceSetupId);
		poInvoiceSetDetView.setInvoiceStartDate(sDate);
		poInvoiceSetDetView.setInvoiceEndDate(edate);
//		poInvoiceSetDetView.setInvoiceSetupSource("cron");
		poInvoiceSetDetView.setPurchaseOrderId(UUID.randomUUID());
		poInvoiceSetDetView.setEngagementStartDate(new Date());
		poInvoiceSetDetView.setEngagementEndDate(edate);
		poInvoiceSetDetView.setInvoiceTypeName("REGULAR");
//		poInvoiceSetDetView.setInvoiceSetupSource("Global");
		
		
		PoInvoiceSetupDetailsViewList.add(poInvoiceSetDetView);
		
		
		Page<PoInvoiceSetupDetailsView> poInvoiceSetupDetailsView = new PageImpl<>(PoInvoiceSetupDetailsViewList, pageableRequest, PoInvoiceSetupDetailsViewList.size()+1);
		
		InvoiceQueue latestInvoiceQueue = new InvoiceQueue();
		latestInvoiceQueue.setId(UUID.randomUUID());
		latestInvoiceQueue.setAmount(3.00);
		latestInvoiceQueue.setInvoiceNumber("1245");
		latestInvoiceQueue.setBillDate(new Date());
		
		
		when(poInvoiceSetupDetailsViewRepository.findAll(pageableRequest)).thenReturn(poInvoiceSetupDetailsView);
 		
		when(invoiceQueueRepository.getOneInvoiceQueueOrderByPurchaseOrderId(invoiceSetupId)).thenReturn(null);
	
		invoiceEngineServiceImpl.prepareInvoiceEngineReader(fromId,toId, runCronDate , invoiceLiveDate);
	}
	

}
