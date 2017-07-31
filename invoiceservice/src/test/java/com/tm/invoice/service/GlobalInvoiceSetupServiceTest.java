package com.tm.invoice.service;

import java.util.List;
import java.util.UUID;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.tm.invoice.domain.GlobalInvoiceSetup;
import com.tm.invoice.domain.GlobalInvoiceSetupGrid;
import com.tm.invoice.domain.InvoiceTemplate;
import com.tm.invoice.dto.GlobalInvoiceSetupDTO;
import com.tm.invoice.mongo.repository.ActivityLogRepository;
import com.tm.invoice.repository.GlobalInvoiceRepository;
import com.tm.invoice.repository.GlobalInvoiceSetupGridRepository;
import com.tm.invoice.repository.InvoiceTemplateRepository;
import com.tm.invoice.service.impl.GlobalInvoiceSetupServiceImpl;

public class GlobalInvoiceSetupServiceTest {

		@InjectMocks
		GlobalInvoiceSetupServiceImpl globalInvoiceSetupServiceImpl;
		
		@Mock
		private GlobalInvoiceSetupGridRepository globalInvoiceSetupGridRepository;

		@Mock
	    private InvoiceTemplateRepository invoiceTemplateRepository;
	
		@Mock
	    private GlobalInvoiceRepository globalInvoiceRepository; 
		
		@Mock
	    private ActivityLogRepository activityLogRepository;
		
		@Mock
		private GlobalInvoiceSetupDTO objGlobalInvoiceSetupDTO;
		
    @BeforeTest
    public void setUpGlobalInvoiceSetupService() {
        this.globalInvoiceSetupGridRepository = mock(GlobalInvoiceSetupGridRepository.class);
        this.invoiceTemplateRepository = mock(InvoiceTemplateRepository.class);
        this.globalInvoiceRepository = mock(GlobalInvoiceRepository.class);
        this.activityLogRepository = mock(ActivityLogRepository.class);
        this.objGlobalInvoiceSetupDTO = mock(GlobalInvoiceSetupDTO.class);
        globalInvoiceSetupServiceImpl =
                new GlobalInvoiceSetupServiceImpl(globalInvoiceSetupGridRepository,
                        invoiceTemplateRepository, globalInvoiceRepository, activityLogRepository);

    }

		@Test(dataProviderClass = GlobalInvoiceSetupServiceTestDataProvider.class, dataProvider = "getGlobalInvoiceSetups", description = "")
		public void getGlobalInvoiceSetups(String status,Pageable pageRequest,Page<GlobalInvoiceSetupGrid> pageGlobalInvoiceSetupGrid)
		{		
			when(globalInvoiceSetupGridRepository.findByInvoiceStatus( Mockito.anyString(),(Pageable)Mockito.anyObject())).thenReturn(pageGlobalInvoiceSetupGrid);			
			globalInvoiceSetupServiceImpl.getGlobalInvoiceSetups(status, pageRequest);
			
		}
		
	/*	@Test(dataProviderClass = GlobalInvoiceSetupServiceTestDataProvider.class, dataProvider = "getGlobalInvoiceContractorsSetups", description = "")
		public void getGlobalInvoiceContractorsSetups(String status,Pageable pageRequestNull,Pageable pageRequest,
				Page<GlobalInvoiceContractorsSetupGrid> pageGlobalInvoiceSetupGrid,UUID globalInvoiceSetupId)
		{
			when(globalInvoiceContractorsSetupGridRepository.findByGlobalInvoiceSetupId(globalInvoiceSetupId,pageRequest)).thenReturn(pageGlobalInvoiceSetupGrid);
			globalInvoiceSetupServiceImpl.getGlobalInvoiceContractorsSetups(UUID.randomUUID(), pageRequestNull);
			
		}*/
		
		@Test(dataProviderClass = GlobalInvoiceSetupServiceTestDataProvider.class, dataProvider = "getTemplateDetails", description = "")
		public void getTemplateDetails( List<InvoiceTemplate> invoicetemplate)
		{
			when(invoiceTemplateRepository.findAll()).thenReturn(invoicetemplate);
			globalInvoiceSetupServiceImpl.getTemplateDetails();
		}
		
		
		@Test(dataProviderClass = GlobalInvoiceSetupServiceTestDataProvider.class, dataProvider = "saveGlobalInvoiceSetup", description = "")
		public void saveGlobalInvoiceSetup(GlobalInvoiceSetupDTO globalInvoiceSetupDTO,GlobalInvoiceSetup globalInvoiceSetup, List<GlobalInvoiceSetup> listGlobalInvoiceSetup)
		{
			InvoiceTemplate invoiceTemplateOldValue=mock(InvoiceTemplate.class);
			when(objGlobalInvoiceSetupDTO.getInvoiceSetupId()).thenReturn(UUID.randomUUID());
			when(objGlobalInvoiceSetupDTO.getInvoiceSetupName()).thenReturn("Invoice");
			//when(objGlobalInvoiceSetupDTO.getTerms()).thenReturn("Terms");
			when(objGlobalInvoiceSetupDTO.getInvoiceType()).thenReturn("Terms");
			when(objGlobalInvoiceSetupDTO.getInvTemplateId()).thenReturn(10L);
			when(objGlobalInvoiceSetupDTO.getDelivery()).thenReturn("Terms");
			when(objGlobalInvoiceSetupDTO.getInvoiceSpecialistNotes()).thenReturn("Terms");
			when(objGlobalInvoiceSetupDTO.getBillCycleFrequency()).thenReturn("billCycleFrequency");				
			when(invoiceTemplateRepository.findOne(globalInvoiceSetup.getInvTemplateId())).thenReturn(invoiceTemplateOldValue);
			when(invoiceTemplateRepository.findOne(objGlobalInvoiceSetupDTO.getInvTemplateId())).thenReturn(invoiceTemplateOldValue);
			when(globalInvoiceRepository.getInvoiceSetupsByInvoiceNameFormat(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(Mockito.anyListOf(GlobalInvoiceSetup.class));
			when(globalInvoiceRepository.findOne(globalInvoiceSetupDTO.getInvoiceSetupId())).thenReturn(globalInvoiceSetup);
			when(globalInvoiceRepository.save(globalInvoiceSetup)).thenReturn(globalInvoiceSetup);
			globalInvoiceSetupServiceImpl.saveGlobalInvoiceSetup(objGlobalInvoiceSetupDTO);
			
		}
		
		@Test(dataProviderClass = GlobalInvoiceSetupServiceTestDataProvider.class, dataProvider = "getGlobalInvoiceSetup", description = "")
		public void getGlobalInvoiceSetup(UUID invoiceSetupId,GlobalInvoiceSetup globalInvoiceSetup)
		{
			when(globalInvoiceRepository.findByGlobalInvoiceSetupById(invoiceSetupId)).thenReturn(globalInvoiceSetup);
	    	globalInvoiceSetupServiceImpl.getGlobalInvoiceSetup(invoiceSetupId.toString());
			
		}		
		
		@Test(dataProviderClass = GlobalInvoiceSetupServiceTestDataProvider.class, dataProvider = "updateGlobalInvoiceSetupStatus", description = "")
		public void updateGlobalInvoiceSetupStatus(UUID invoiceSetupId,GlobalInvoiceSetupDTO globalInvoiceSetupDTO)
		{			
			globalInvoiceSetupServiceImpl.updateGlobalInvoiceSetupStatus(globalInvoiceSetupDTO);
		}
		
		
		@Test(dataProviderClass = GlobalInvoiceSetupServiceTestDataProvider.class, dataProvider = "getGlobalInvoiceSetupList", description = "")
		public void getGlobalInvoiceSetupList(List<GlobalInvoiceSetup> glopalSetupList)
		{
			when(globalInvoiceRepository.findByActiveFlagAndInvoiceStatus(null,null)).thenReturn(glopalSetupList);
			globalInvoiceSetupServiceImpl.getGlobalInvoiceSetup();
			
		}
		
}
