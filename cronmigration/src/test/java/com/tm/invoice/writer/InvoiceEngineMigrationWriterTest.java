package com.tm.invoice.writer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tm.common.domain.CompanyProfile;
import com.tm.engagement.repository.EngagementRepository;
import com.tm.invoice.domain.InvoiceDetail;
import com.tm.invoice.domain.InvoiceQueue;
import com.tm.invoice.domain.PurchaseOrder;
import com.tm.invoice.dto.BillToManagerDTO;
import com.tm.invoice.dto.BillingProfileDTO;
import com.tm.invoice.dto.ClientInfoDTO;
import com.tm.invoice.dto.InvoiceDTO;
import com.tm.invoice.dto.InvoiceExceptionDetailsDTO;
import com.tm.invoice.dto.InvoiceSetupDTO;
import com.tm.invoice.dto.PDFAttachmentDTO;
import com.tm.invoice.dto.PurchaseOrderDTO;
import com.tm.invoice.dto.UserPreferenceDTO;
import com.tm.invoice.mongo.domain.Historical;
import com.tm.invoice.mongo.repository.HistoricalRepository;
import com.tm.invoice.mongo.repository.InvoiceDetailRepository;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.repository.PurchaseOrderRepository;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.repository.TimesheetRepository;

public class InvoiceEngineMigrationWriterTest {
	
	@InjectMocks
	private InvoiceEngineMigrationWriter invoiceEngineMigrationWriter;
	
	@Mock
	private PurchaseOrderRepository purchaseOrderRepository;
	
	@Mock
	private InvoiceDetailRepository invoiceDetailRepository;
	@Mock
	private TimesheetRepository timesheetRepository;
	@Mock
	private InvoiceQueueRepository invoiceQueueRepository;
	@Mock
	private HistoricalRepository historicalRepository;
	@Mock
	private MongoTemplate mongoTemplate;
	@Mock
	private EngagementRepository engagementRepository;
	
	@BeforeMethod
	private void setUp(){
		this.engagementRepository = mock(EngagementRepository.class);
		this.invoiceDetailRepository = mock(InvoiceDetailRepository.class);
		this.timesheetRepository = mock(TimesheetRepository.class);
		this.invoiceQueueRepository = mock(InvoiceQueueRepository.class);
		this.historicalRepository = mock(HistoricalRepository.class);
		this.mongoTemplate = mock(MongoTemplate.class);
		invoiceEngineMigrationWriter = new InvoiceEngineMigrationWriter(invoiceDetailRepository,
				timesheetRepository, invoiceQueueRepository, historicalRepository, mongoTemplate, engagementRepository);
	}
	
	@Test
	private void saveInvoices(){
		UserPreferenceDTO userPreferenceDTO = new UserPreferenceDTO();
		userPreferenceDTO.setTimesheetInclude(true);
		userPreferenceDTO.setExpenseInclude(true);
		userPreferenceDTO.setAutoDelivery(true);
		userPreferenceDTO.setContractorNameExclude(true);
		
		 InvoiceDTO invoiceDTO = new InvoiceDTO();
		 invoiceDTO.setId(UUID.randomUUID());
		 invoiceDTO.setInvoiceDate(new Date());
		 invoiceDTO.setUserPreference(userPreferenceDTO);
		 invoiceDTO.setInvoiceQueueId(UUID.randomUUID());
		 
		 InvoiceDetail invoiceDetail = new InvoiceDetail();
		 invoiceDetail.set_id(UUID.randomUUID());
		 invoiceDetail.setInvoiceQueueId(UUID.randomUUID());
		 invoiceDetail.setInvoiceSetupId(UUID.randomUUID());
		 
		 List<InvoiceDetail> invoiceDetailList = new ArrayList<>();
		 invoiceDetailList.add(invoiceDetail);
		 invoiceDTO.setInvoiceDetails(invoiceDetailList);
		 
		 InvoiceExceptionDetailsDTO invoiceExceptionDetailsDTO = new InvoiceExceptionDetailsDTO();
		 
		 List<InvoiceExceptionDetailsDTO> invoiceExceptionDetailsList = new ArrayList<>();
		 invoiceExceptionDetailsList.add(invoiceExceptionDetailsDTO);
		 
		 invoiceDTO.setInvoiceExceptionDetail(invoiceExceptionDetailsList);
		 
		 ClientInfoDTO clientInfoDTO = new ClientInfoDTO();
		 CompanyProfile companyProfile = new CompanyProfile();
		 companyProfile.setCompanyProfileId(123L);
		 companyProfile.setCompanyName("SMI");
		 companyProfile.setCompanyAddress("Madurai");
		 clientInfoDTO.setCompanyProfile(companyProfile);
		 
		 invoiceDTO.setClientInfo(clientInfoDTO);
		 
		 InvoiceSetupDTO invoiceSetup = new InvoiceSetupDTO();
		 invoiceSetup.setInvoiceSetupId(UUID.randomUUID());
		 invoiceSetup.setPaymentTerms("bank");
		 
		 invoiceDTO.setInvoiceSetup(invoiceSetup);
		 
		 Timesheet timesheet = new Timesheet();
		 timesheet.setId(UUID.randomUUID());
		 timesheet.setEndDate(new Date());
		 
		 List<Timesheet> timesheetList = new ArrayList<>();
		 timesheetList.add(timesheet);
		 
		 invoiceDTO.setTimesheetList(timesheetList);
		 invoiceDTO.setBillToClientName("Good");
		 
		 invoiceDTO.setTimesheetAttachment("Y");
		 
		 BillToManagerDTO billToManagerDTO = new BillToManagerDTO();
		 billToManagerDTO.setBillAddress("Madurai");
		 billToManagerDTO.setStateName("client");
		 billToManagerDTO.setBillToMgrName("jo");
		 invoiceDTO.setBillToManager(billToManagerDTO);
		 
		 invoiceDTO.setBillingAmount(new BigDecimal(1000));
		 BillingProfileDTO billingProfileDTO = new BillingProfileDTO();
		 billingProfileDTO.setWorkHours(10.00);
		 billingProfileDTO.setAmount(new BigDecimal(525252));
		 billingProfileDTO.setRate(58.00);
		 
		 List<BillingProfileDTO>  billingProfileDTOList = new ArrayList<>();
		 billingProfileDTOList.add(billingProfileDTO);
		 invoiceDTO.setBillingProfiles(billingProfileDTOList);
		 
		 PurchaseOrderDTO purchasOrderDTO = new PurchaseOrderDTO();
		 purchasOrderDTO.setPurchaseOrderId(UUID.randomUUID());
		 invoiceDTO.setPurchaseOrder(purchasOrderDTO);
		 invoiceDTO.setAmount(new BigDecimal(451.00));
		 PDFAttachmentDTO invoicePDF = new PDFAttachmentDTO();
		 invoicePDF.setSourceReferenceName("testing");
		 invoicePDF.setContentType("text");
		 invoicePDF.setFileName("welcome");
		 invoiceDTO.setInvoicePDF(invoicePDF);
		 
		 
		 invoiceDTO.setBillableExpensesAttachment("Y");
		 
		 List<InvoiceDTO> invoiceDTOList = new ArrayList<>();
		 invoiceDTOList.add(invoiceDTO);
	 
		 List<InvoiceDetail> invoiceDetails = new ArrayList<>(); 
		 List<Timesheet> timesheets = new ArrayList<>();
		 List<InvoiceQueue> invoiceQueues = new ArrayList<>();
		 Historical historical = new Historical();
		 historical.setAmount(12457);
		 List<Historical> historicals = new ArrayList<>();
		 
		 when(invoiceDetailRepository.save(invoiceDetails)).thenReturn(Mockito.anyList());
		 when(timesheetRepository.save(timesheets)).thenReturn(Mockito.anyList());
		 when(invoiceQueueRepository.save(invoiceQueues)).thenReturn(null);
		 when(historicalRepository.save(historicals)).thenReturn(null);
		 when(mongoTemplate.getDb()).thenReturn(Mockito.any());
		 invoiceEngineMigrationWriter.startInvoiceEngineMigrationWriter(invoiceDTOList);
	}

}
