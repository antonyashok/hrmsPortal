package com.tm.invoice.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tm.invoice.domain.CompanyProfile;
import com.tm.invoice.domain.EmployeeEngagementDetailsView;
import com.tm.invoice.domain.InvoiceTemplate;
import com.tm.invoice.dto.EmployeeProfileDTO;
import com.tm.invoice.mongo.domain.Historical;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.domain.ManualInvoice;
import com.tm.invoice.mongo.dto.ManualInvoiceDTO;
import com.tm.invoice.mongo.repository.HistoricalRepository;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.mongo.repository.ManualInvoiceRepository;
import com.tm.invoice.repository.CompanyProfileRepository;
import com.tm.invoice.repository.EmployeeEngagementDetailsViewRepository;
import com.tm.invoice.repository.InvoiceTemplateRepository;
import com.tm.invoice.service.impl.ManualInvoiceServiceImpl;
import com.tm.invoice.util.JasperReportUtil;


public class ManualInvoiceServiceTest {
	
		@InjectMocks
		ManualInvoiceServiceImpl manualInvoiceServiceImpl;
	
		@Mock
		private ManualInvoiceRepository manualInvoiceRepository;
		
		@Mock
	    private InvoiceService invoiceService;

		@Mock
	    private EmployeeEngagementDetailsViewRepository employeeEngagementDetailsViewRepository;
	
		@Mock
	    private InvoiceTemplateRepository invoiceTemplateRepository;
	
		@Mock
	    private InvoiceQueueRepository invoiceQueueRepository;    
	
		@Mock
	    private HistoricalRepository historicalRepository;
	
		@Mock
	    private MongoTemplate mongoTemplate;
	
		@Mock
	    private CompanyProfileRepository companyProfileRepository;
		
		@Mock
		private JasperReportUtil jasperReportUtil;
	
		@Mock
		private String jasperReport;
	
		@BeforeTest
		public void setUpManualInvoiceServiceTest()
		{
			this.manualInvoiceRepository=mock(ManualInvoiceRepository.class);
			this.employeeEngagementDetailsViewRepository=mock(EmployeeEngagementDetailsViewRepository.class);
			this.invoiceTemplateRepository=mock(InvoiceTemplateRepository.class);
			this.invoiceQueueRepository=mock(InvoiceQueueRepository.class);
			this.historicalRepository=mock(HistoricalRepository.class);
			this.mongoTemplate=mock(MongoTemplate.class);
			this.companyProfileRepository=mock(CompanyProfileRepository.class);
			this.jasperReportUtil = mock(JasperReportUtil.class);
			this.invoiceService=mock(InvoiceService.class);
			this.jasperReport="/data/innoPeople/jasperReport/";
			
			manualInvoiceServiceImpl=new ManualInvoiceServiceImpl(manualInvoiceRepository, invoiceService, 	employeeEngagementDetailsViewRepository, invoiceTemplateRepository, 
					invoiceQueueRepository, mongoTemplate, historicalRepository, 
					companyProfileRepository, jasperReportUtil);
			
		}
		
		
		@Test(dataProviderClass = ManualInvoiceServiceTestDataProvider.class, dataProvider = "generateManualInvoice", description = "")
		public void generateManualInvoice(ManualInvoiceDTO manualInvoiceDTO,
				EmployeeProfileDTO employeeProfileDTO,ManualInvoice manualInvoice)
		{
			ManualInvoiceDTO objManualInvoiceDTO=mock(ManualInvoiceDTO.class);	
			when(invoiceService.getLoggedInUser()).thenReturn(employeeProfileDTO);
			when(manualInvoiceRepository.save(manualInvoice)).thenReturn(manualInvoice);
			manualInvoiceServiceImpl.generateManualInvoice(objManualInvoiceDTO);
		}
		
		@Test(dataProviderClass = ManualInvoiceServiceTestDataProvider.class, dataProvider = "getAllManualInvoices", description = "")
		public void getAllManualInvoices(Pageable pageable,Page<ManualInvoice> pagemanualInvoice,ManualInvoice manualInvoice,ManualInvoiceDTO manualInvoiceDTO)
		{
			when(manualInvoiceRepository.getAllManualInvoices(Mockito.anyString(),(Pageable)Mockito.anyObject())).thenReturn(pagemanualInvoice);
			manualInvoiceServiceImpl.getAllManualInvoices("Review", pageable);
				
		}
	/*	
		@Test(dataProviderClass = ManualInvoiceServiceTestDataProvider.class, dataProvider = "updateManualInvoiceStatus", description = "")
		public void updateManualInvoiceStatus(ManualInvoiceDTO manualInvoiceDTO,List<ManualInvoice> invoicesList,
				CompanyProfile companyProfile,ManualInvoice manualInvoice,InvoiceQueue invoiceQueue,Historical historicals,InvoiceTemplate invoiceTemplate) throws IOException
		{
		
			when(companyProfileRepository.getProfileDetails()).thenReturn(companyProfile);
			when(manualInvoiceRepository.findManualInvoices(manualInvoiceDTO.getManualInvoiceIds())).thenReturn(invoicesList);
			when(invoiceTemplateRepository.findByInvoiceTemplateId(manualInvoice.getTemplateId())).thenReturn(invoiceTemplate);
			when(manualInvoiceRepository.save(manualInvoice)).thenReturn(manualInvoice);			
			when(invoiceQueueRepository.save(invoiceQueue)).thenReturn(invoiceQueue);			
			when(historicalRepository.save(historicals)).thenReturn(historicals);			
			manualInvoiceServiceImpl.updateManualInvoiceStatus(manualInvoiceDTO);
			
		}
		*/
		
		
		@Test(dataProviderClass = ManualInvoiceServiceTestDataProvider.class, dataProvider = "getManualInvoices", description = "")
		public void getManualInvoices(ManualInvoice manualInvoice)
		{		
			UUID invoiceId=UUID.randomUUID();
			when(manualInvoiceRepository.findByInvoiceId(invoiceId)).thenReturn(manualInvoice);
			manualInvoiceServiceImpl.getManualInvoices(invoiceId);			
		}
		
		@Test(dataProviderClass = ManualInvoiceServiceTestDataProvider.class, dataProvider = "getContractorsDetailsByEngagement", description = "")
		public void getContractorsDetailsByEngagement(Page<EmployeeEngagementDetailsView> pageemployee,Pageable page)
		{
			List<Long> longVal=new ArrayList<Long>();
			longVal.add(10L);
			UUID engagementId=UUID.randomUUID();
			      
			//when(employeeEngagementDetailsViewRepository.findByEmployeeName(Mockito.anyString(),(Pageable)Mockito.anyObject())).thenReturn(pageemployee);
			  when(employeeEngagementDetailsViewRepository.getUnmappedContractors("contractorName",engagementId, longVal, page)).thenReturn(pageemployee);
		      manualInvoiceServiceImpl.getContractorsDetailsByEngagement("contractorName",engagementId, longVal,page);
		}
		
		@Test(dataProviderClass = ManualInvoiceServiceTestDataProvider.class, dataProvider = "deleteRejectedManualInvoice", description = "")
		public void deleteRejectedManualInvoice(UUID invoiceId)
		{
			
			when(manualInvoiceRepository.findByInvoiceId(invoiceId)).thenReturn(Mockito.any());
			manualInvoiceServiceImpl.deleteRejectedManualInvoice(invoiceId);
		}
		
	
}
