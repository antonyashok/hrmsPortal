package com.tm.invoice.processor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tm.common.domain.CompanyProfile;
import com.tm.invoice.dto.BillToManagerDTO;
import com.tm.invoice.dto.BillingProfileDTO;
import com.tm.invoice.dto.ClientInfoDTO;
import com.tm.invoice.dto.InvoiceDTO;
import com.tm.invoice.dto.InvoiceSetupBatchDTO;
import com.tm.invoice.dto.InvoiceSetupDTO;
import com.tm.invoice.dto.UserPreferenceDTO;
import com.tm.timesheetgeneration.domain.Timesheet;

public class InvoiceEngineMigrationProcessorTest {
	
	@InjectMocks
	private InvoiceEngineMigrationProcessor invoiceEngineMigrationProcessor;
	
	@BeforeMethod
	private void setUp(){
		invoiceEngineMigrationProcessor = new InvoiceEngineMigrationProcessor();
	}
	
	@Test
	private void startInvoiceEngineMigrationProcessor(){
		UserPreferenceDTO userPreferenceDTO = new UserPreferenceDTO();
		userPreferenceDTO.setTimesheetInclude(true);
		userPreferenceDTO.setExpenseInclude(true);
		userPreferenceDTO.setAutoDelivery(true);
		userPreferenceDTO.setContractorNameExclude(true);
		
		 InvoiceSetupBatchDTO item = new InvoiceSetupBatchDTO();
		 item.setName("invoiceSetupbatch");
		 item.setRunCronDate(LocalDate.now());
		 
		 InvoiceDTO invoiceDTO = new InvoiceDTO();
		 invoiceDTO.setId(UUID.randomUUID());
		 invoiceDTO.setInvoiceDate(new Date());
		 invoiceDTO.setUserPreference(userPreferenceDTO);
		 
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
		 
		 List<InvoiceDTO> invoiceDTOList = new ArrayList<>();
		 invoiceDTOList.add(invoiceDTO);
		 item.setInvoiceDTOList(invoiceDTOList);
		 invoiceEngineMigrationProcessor.startInvoiceEngineMigrationProcessor(item);
	}

}
