package com.tm.invoice.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.tm.invoice.dto.BillingDetailsDTO;
import com.tm.invoice.dto.DPQueueListDTO;
import com.tm.invoice.dto.DpQueueDTO;
import com.tm.invoice.mongo.domain.DpQueue;
import com.tm.invoice.mongo.repository.DpQueueRepository;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.service.impl.DpQueueServiceImpl;
import com.tm.invoice.service.mapper.DpQueueMapper;

public class DpQueueServiceTest {
	
	@InjectMocks
	DpQueueServiceImpl dpQueueServiceImpl;
	
	@Mock
	private DpQueueRepository dpQueueRepository;
	
	@Mock
	private InvoiceQueueRepository invoiceQueueRepository;
	
	@BeforeTest
	public void setUpDpQueueServiceTest() throws Exception {
		this.dpQueueRepository=mock(DpQueueRepository.class);
		this.invoiceQueueRepository=mock(InvoiceQueueRepository.class);
		
		dpQueueServiceImpl = new DpQueueServiceImpl(dpQueueRepository,invoiceQueueRepository);
		
	}
	
	@Test(dataProviderClass = DpQueueServiceTestDataProvider.class, dataProvider = "getDpQueues", description = "")
	public void getDpQueues(Long billToClientId, String status, Page<DpQueue> dpQueue)
	{
		Pageable pageRequest = new PageRequest(0, 100, Sort.Direction.DESC, "updated.on");
		when(dpQueueRepository.getDpQueues(Mockito.anyLong(), Mockito.anyString(), (Pageable)Mockito.anyObject())).thenReturn(dpQueue);
		dpQueueServiceImpl.getDpQueues(billToClientId, status, pageRequest);
	}

	@Test(dataProviderClass = DpQueueServiceTestDataProvider.class, dataProvider = "getDpQueueDetails", description = "")
	public void getDpQueueDetails(DpQueue dpQueue)
	{
		when(dpQueueRepository.findById(Mockito.any())).thenReturn(dpQueue);
		dpQueueServiceImpl.getDpQueueDetails(UUID.randomUUID());
	}
	
	@Test(dataProviderClass = DpQueueServiceTestDataProvider.class, dataProvider = "saveDirectPlacement", description = "")
	public void saveDirectPlacement(DpQueueDTO dpQueueDTO,DpQueue dpQueue)
	{
		//	UUID uid = UUID.fromString("017b050c-469d-11e7-a919-92ebcb67fe34"); 
		when(dpQueueRepository.findById(Mockito.any())).thenReturn(null);		
		when(dpQueueRepository.save(Mockito.any(DpQueue.class))).thenReturn(dpQueue);
		dpQueueServiceImpl.saveDirectPlacement(dpQueueDTO);
	}
	
	@Test(dataProviderClass = DpQueueServiceTestDataProvider.class, dataProvider = "updateDirectPlacement", description = "")
	public void updateDirectPlacement(DpQueueDTO dpQueueDTO,DpQueue dpQueue)
	{
		//	UUID uid = UUID.fromString("017b050c-469d-11e7-a919-92ebcb67fe34"); 
		when(dpQueueRepository.findById(Mockito.any())).thenReturn(dpQueue);		
		when(dpQueueRepository.save(Mockito.any(DpQueue.class))).thenReturn(dpQueue);
		dpQueueServiceImpl.updateDirectPlacement(dpQueueDTO);
	}
	
	@Test(dataProviderClass = DpQueueServiceTestDataProvider.class, dataProvider = "generateInvoice", description = "")
	public void generateInvoice(List<UUID> dpQueueIds,List<DpQueue> dpQueues,BillingDetailsDTO billingDetailsDTO)
	{
		when(dpQueueRepository.getDpQueues(dpQueueIds)).thenReturn(dpQueues);
		when(dpQueueRepository.save(dpQueues)).thenReturn(dpQueues);		
		dpQueueServiceImpl.generateInvoice(billingDetailsDTO);
	}
	
	@Test(dataProviderClass = DpQueueServiceTestDataProvider.class, dataProvider = "getNotGeneratedDpQueues", description = "")
	public void getNotGeneratedDpQueues(List<DpQueue> dpQueues,DPQueueListDTO dPQueueListDTO)
	{
		when(dpQueueRepository.getNotGenereatedDpQueues(10L)).thenReturn(dpQueues);
		dpQueueServiceImpl.getNotGeneratedDpQueues(10L);
	}
	
	
	
}
