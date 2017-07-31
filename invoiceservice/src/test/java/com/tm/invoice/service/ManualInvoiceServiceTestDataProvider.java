package com.tm.invoice.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.testng.annotations.DataProvider;

import com.tm.invoice.domain.CompanyProfile;
import com.tm.invoice.domain.EmployeeEngagementDetailsView;
import com.tm.invoice.domain.InvoiceTemplate;
import com.tm.invoice.dto.EmployeeEngagementDetailsViewDTO;
import com.tm.invoice.dto.EmployeeProfileDTO;
import com.tm.invoice.mongo.domain.Historical;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.domain.ManualInvoice;
import com.tm.invoice.mongo.domain.ManualInvoiceContractorDetail;
import com.tm.invoice.mongo.dto.ManualInvoiceDTO;

public class ManualInvoiceServiceTestDataProvider {
	
	
	@DataProvider(name = "generateManualInvoice")
	public static Iterator<Object[]> generateManualInvoice() throws ParseException {

		ManualInvoiceDTO manualInvoiceDTO=new ManualInvoiceDTO();
		manualInvoiceDTO.setInvoiceId(UUID.randomUUID());
		manualInvoiceDTO.setBillToClientId(10L);
		manualInvoiceDTO.setBillToClientId(10L);
	
		
		ManualInvoice manualInvoice=new ManualInvoice();
		manualInvoice.set_id(new ObjectId());
		manualInvoice.setBillToClientId(10L);
		manualInvoice.setCountryId(10L);
	
		EmployeeProfileDTO employeeProfileDTO=new EmployeeProfileDTO();
		employeeProfileDTO.setRoleId(10L);
		employeeProfileDTO.setRoleName("Admin");
		employeeProfileDTO.setFirstName("Invoice");
		employeeProfileDTO.setLastName("Manager");
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {manualInvoiceDTO,employeeProfileDTO,manualInvoice});
		return testData.iterator();
	}
	
	@DataProvider(name = "getAllManualInvoices")
	public static Iterator<Object[]> getAllManualInvoices() throws ParseException
	{
		Pageable pageRequest = new PageRequest(0, 100, Sort.Direction.DESC, "auditFields.on");
		
		ManualInvoice manualInvoice=new ManualInvoice();
		manualInvoice.set_id(new ObjectId());
		manualInvoice.setBillToClientId(10L);
		manualInvoice.setCountryId(10L);
		manualInvoice.setBillToClientName("billtoClient");
		manualInvoice.setCountryName("India");
		
		ManualInvoiceDTO manualInvoiceDTO=new ManualInvoiceDTO();
		manualInvoiceDTO.setInvoiceId(UUID.randomUUID());
		manualInvoiceDTO.setBillToClientId(10L);
		manualInvoiceDTO.setBillToClientId(10L);
		
		
		List<ManualInvoice> manualInvoicesList=new ArrayList<ManualInvoice>();
		manualInvoicesList.add(manualInvoice);
		Page<ManualInvoice> pagemanualInvoice=new PageImpl<ManualInvoice>(manualInvoicesList);	
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {pageRequest,pagemanualInvoice,manualInvoice,manualInvoiceDTO});
		return testData.iterator();
	}

	@DataProvider(name = "updateManualInvoiceStatus")
	public static Iterator<Object[]> updateManualInvoiceStatus() throws ParseException
	{
		List<ManualInvoiceContractorDetail> manualInvoiceContractorDetails=new ArrayList<ManualInvoiceContractorDetail>();
		
		ManualInvoiceContractorDetail manualInvoiceContractorDetail=new ManualInvoiceContractorDetail();
		manualInvoiceContractorDetail.setContractorId(10L);
		manualInvoiceContractorDetail.setId(new ObjectId());
		manualInvoiceContractorDetail.setDescription("description");
	
		manualInvoiceContractorDetails.add(manualInvoiceContractorDetail);
		
		
		ManualInvoice manualInvoice=new ManualInvoice();
		manualInvoice.set_id(new ObjectId());
		manualInvoice.setBillToClientId(10L);
		manualInvoice.setCountryId(10L);
		manualInvoice.setBillToClientName("billtoClient");
		manualInvoice.setCountryName("India");
		manualInvoice.setTemplateId(10L);
		manualInvoice.setManualInvoiceContractorDetails(manualInvoiceContractorDetails);
		
		
		ManualInvoiceDTO manualInvoiceDTO=new ManualInvoiceDTO();
		manualInvoiceDTO.setInvoiceId(UUID.randomUUID());
		manualInvoiceDTO.setBillToClientId(10L);
		manualInvoiceDTO.setBillToClientId(10L);
		manualInvoiceDTO.setInvoiceId(UUID.randomUUID());
		manualInvoiceDTO.setAction("Approved");
		manualInvoiceDTO.setReviewComments("reviewComments");
		List<UUID> listManualInvoiceIds=new ArrayList<UUID>();
		listManualInvoiceIds.add(UUID.randomUUID());
		listManualInvoiceIds.add(UUID.randomUUID());
	
		manualInvoiceDTO.setManualInvoiceIds(listManualInvoiceIds);
		List<ManualInvoice> invoicesList=new ArrayList<ManualInvoice>();
		invoicesList.add(manualInvoice);
		CompanyProfile companyProfile=new CompanyProfile();
		companyProfile.setCompanyAddress("US");
		
		InvoiceTemplate invoiceTemplate=new InvoiceTemplate();
		invoiceTemplate.setInvoiceTemplateId(10L);
		invoiceTemplate.setLogofilename("logofilename");
		
		InvoiceQueue invoiceQueue=new InvoiceQueue();
		invoiceQueue.setAmount(100.00);
		invoiceQueue.setAttentionManagerName("attentionManagerName");
		invoiceQueue.setId(UUID.randomUUID());
		
		Historical historicals=new Historical();
		historicals.setAmount(100.00);
		historicals.setId(UUID.randomUUID());
		historicals.setBillingSpecialistId(10L);
		historicals.setInvoiceSetupId(UUID.randomUUID());
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {manualInvoiceDTO,invoicesList,companyProfile,manualInvoice,invoiceQueue,historicals,invoiceTemplate});
		return testData.iterator();
	}
	
	
	
	
	@DataProvider(name = "getManualInvoices")
	public static Iterator<Object[]> getManualInvoices() throws ParseException
	{
		ManualInvoice manualInvoice=new ManualInvoice();
		manualInvoice.set_id(new ObjectId());
		manualInvoice.setBillToClientId(10L);
		manualInvoice.setCountryId(10L);
		manualInvoice.setBillToClientName("billtoClient");
		manualInvoice.setCountryName("India");
		manualInvoice.setTemplateId(10L);
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {manualInvoice});
		return testData.iterator();
	}
	
	
	@DataProvider(name = "getContractorsDetailsByEngagement")
	public static Iterator<Object[]> getContractorsDetailsByEngagement() throws ParseException{
		
		Pageable pageRequest = new PageRequest(0, 100, Sort.Direction.DESC, "auditFields.on");
		
		EmployeeEngagementDetailsView employeeEngagementDetailsView=new EmployeeEngagementDetailsView();
		employeeEngagementDetailsView.setEmployeeEngagementId(UUID.randomUUID());
		employeeEngagementDetailsView.setEmployeeId(10L);
		employeeEngagementDetailsView.setEmployeeName("testName");
		employeeEngagementDetailsView.setEngagementId(UUID.randomUUID());
		
	/*	EmployeeEngagementDetailsViewDTO employeeEngagementDetailsViewDTO=new EmployeeEngagementDetailsViewDTO();
		employeeEngagementDetailsViewDTO.setEmployeeEngagementId(UUID.randomUUID());
		employeeEngagementDetailsViewDTO.setEmployeeId(10L);
		employeeEngagementDetailsViewDTO.setEmployeeName("testName");
		employeeEngagementDetailsViewDTO.setEngagementId(UUID.randomUUID());
		
		List<EmployeeEngagementDetailsViewDTO>employeeEngagementDetailsViewDTOList=new ArrayList<EmployeeEngagementDetailsViewDTO>();
		employeeEngagementDetailsViewDTOList.add(employeeEngagementDetailsViewDTO);*/
		
		
		List<EmployeeEngagementDetailsView> listemployeeEngagementDetailsView=new ArrayList<EmployeeEngagementDetailsView>();
		listemployeeEngagementDetailsView.add(employeeEngagementDetailsView);
		Page<EmployeeEngagementDetailsView> pageemployee=new PageImpl<EmployeeEngagementDetailsView>(listemployeeEngagementDetailsView);	
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {pageemployee,pageRequest});
		return testData.iterator();
		
	}
	
	
	@DataProvider(name = "getContractorsDetailsByEngagementTest")
	public static Iterator<Object[]> getContractorsDetailsByEngagementTest() throws ParseException{
		
		Pageable pageRequest = new PageRequest(0, 100, Sort.Direction.DESC, "auditFields.on");
		/*
		EmployeeEngagementDetailsView employeeEngagementDetailsView=new EmployeeEngagementDetailsView();
		employeeEngagementDetailsView.setEmployeeEngagementId(UUID.randomUUID());
		employeeEngagementDetailsView.setEmployeeId(10L);
		employeeEngagementDetailsView.setEmployeeName("testName");
		employeeEngagementDetailsView.setEngagementId(UUID.randomUUID());*/
		
		EmployeeEngagementDetailsViewDTO employeeEngagementDetailsViewDTO=new EmployeeEngagementDetailsViewDTO();
		employeeEngagementDetailsViewDTO.setEmployeeEngagementId(UUID.randomUUID());
		employeeEngagementDetailsViewDTO.setEmployeeId(10L);
		employeeEngagementDetailsViewDTO.setEmployeeName("testName");
		employeeEngagementDetailsViewDTO.setEngagementId(UUID.randomUUID());
		
		List<EmployeeEngagementDetailsViewDTO>employeeEngagementDetailsViewDTOList=new ArrayList<EmployeeEngagementDetailsViewDTO>();
		employeeEngagementDetailsViewDTOList.add(employeeEngagementDetailsViewDTO);
		
		
		List<EmployeeEngagementDetailsView> listemployeeEngagementDetailsView=new ArrayList<EmployeeEngagementDetailsView>();
		//listemployeeEngagementDetailsView.add(employeeEngagementDetailsView);
		Page<EmployeeEngagementDetailsView> pageemployee=new PageImpl<EmployeeEngagementDetailsView>(listemployeeEngagementDetailsView);	
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {pageemployee,pageRequest});
		return testData.iterator();
		
	}
	
	
	@DataProvider(name = "deleteRejectedManualInvoice")
	public static Iterator<Object[]> deleteRejectedManualInvoice() throws ParseException{
		UUID inoviceId=UUID.randomUUID();
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {inoviceId});
		return testData.iterator();
	}
	

}
