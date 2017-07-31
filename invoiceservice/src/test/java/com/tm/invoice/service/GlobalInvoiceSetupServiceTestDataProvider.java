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
import org.testng.annotations.DataProvider;

import com.tm.invoice.domain.GlobalInvoiceSetup;
import com.tm.invoice.domain.GlobalInvoiceSetupGrid;
import com.tm.invoice.domain.InvoiceTemplate;
import com.tm.invoice.dto.GlobalInvoiceSetupDTO;

public class GlobalInvoiceSetupServiceTestDataProvider {
	
	
	@DataProvider(name = "getGlobalInvoiceSetups")
	public static Iterator<Object[]> getGlobalInvoiceSetups() throws ParseException {

		Pageable pageRequest = new PageRequest(0, 100, null);
	//	Pageable pageRequest = new PageRequest(0, 100, Sort.Direction.ASC, "createdDate");
		String Status ="Approved";			
		GlobalInvoiceSetupGrid globalInvoiceSetupGrid=new GlobalInvoiceSetupGrid();
		globalInvoiceSetupGrid.setGlobalInvoiceSetupId(UUID.randomUUID());
		globalInvoiceSetupGrid.setInvoiceSetupName(null);
		globalInvoiceSetupGrid.setCreatedDate(new Date());
		globalInvoiceSetupGrid.setInvoiceStartDate(new Date());
		globalInvoiceSetupGrid.setInvoiceEndDate(new Date());		
		List<GlobalInvoiceSetupGrid> listGlobalInvoiceSetupGrid=new ArrayList<GlobalInvoiceSetupGrid>();
		listGlobalInvoiceSetupGrid.add(globalInvoiceSetupGrid);		
		Page<GlobalInvoiceSetupGrid> pageGlobalInvoiceSetupGrid=new PageImpl<GlobalInvoiceSetupGrid>(listGlobalInvoiceSetupGrid);		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {Status,pageRequest,pageGlobalInvoiceSetupGrid});
		return testData.iterator();
	}
	
	/*
	@DataProvider(name = "getGlobalInvoiceContractorsSetups")
	public static Iterator<Object[]> getGlobalInvoiceContractorsSetups() throws ParseException {

		Pageable pageRequestNull = new PageRequest(0, 100, null);
		//Pageable pageRequest = new PageRequest(0, 100, null);
		Pageable pageRequest=new PageRequest(0, 100, Sort.Direction.ASC, "contractorName");
		String Status ="Approved";		
		
		UUID globalInvoiceSetupId=UUID.randomUUID();
		
		GlobalInvoiceContractorsSetupGrid globalInvoiceContractorsSetupGrid=new GlobalInvoiceContractorsSetupGrid();
		globalInvoiceContractorsSetupGrid.setClientId("");
		globalInvoiceContractorsSetupGrid.setClientName("");
		globalInvoiceContractorsSetupGrid.setContractorName("");
		globalInvoiceContractorsSetupGrid.setEmployeeId(10L);
		globalInvoiceContractorsSetupGrid.setGlobalInvoiceSetupContractorId(UUID.randomUUID());		
		
		List<GlobalInvoiceContractorsSetupGrid> listGlobalInvoiceContractorsSetupGrid=new ArrayList<GlobalInvoiceContractorsSetupGrid>();		
		listGlobalInvoiceContractorsSetupGrid.add(globalInvoiceContractorsSetupGrid);
		
		Page<GlobalInvoiceContractorsSetupGrid> pageGlobalInvoiceSetupGrid=new 
						PageImpl<GlobalInvoiceContractorsSetupGrid>(listGlobalInvoiceContractorsSetupGrid);		
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {Status,pageRequestNull,pageRequest,pageGlobalInvoiceSetupGrid,globalInvoiceSetupId});
		return testData.iterator();
	}*/
	
	
	@DataProvider(name = "getTemplateDetails")
	public static Iterator<Object[]> getTemplateDetails() throws ParseException {
		List<InvoiceTemplate> listInvoicetemplate =new ArrayList<InvoiceTemplate>();
		
		InvoiceTemplate invoicetemplate=new InvoiceTemplate();
		invoicetemplate.setInvoiceTemplateId(10L);
		invoicetemplate.setCreatedBy(10L);
		invoicetemplate.setCreatedOn(new Date());
		invoicetemplate.setInvoiceTemplateName("");
		invoicetemplate.setUpdatedBy(10L);
		invoicetemplate.setUpdatedOn(new Date());		
		listInvoicetemplate.add(invoicetemplate);
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {listInvoicetemplate});
		return testData.iterator();
	}
	
	@DataProvider(name = "saveGlobalInvoiceSetup")
	public static Iterator<Object[]> saveGlobalInvoiceSetup() throws ParseException {
		
		GlobalInvoiceSetupDTO globalInvoiceSetupDTO=new GlobalInvoiceSetupDTO();
		
		globalInvoiceSetupDTO.setInvoiceSetupId(UUID.randomUUID());
		globalInvoiceSetupDTO.setInvTemplateId(10L);
		globalInvoiceSetupDTO.setPrefix("status");
		globalInvoiceSetupDTO.setSuffixType("suffixType");
		globalInvoiceSetupDTO.setSeparator("separator");		
		globalInvoiceSetupDTO.setStartingNumber(100);		
		List<GlobalInvoiceSetup> listGlobalInvoiceSetup=new ArrayList<GlobalInvoiceSetup>();
		GlobalInvoiceSetup objGlobalInvoiceSetup=new GlobalInvoiceSetup();
		objGlobalInvoiceSetup.setCreatedBy(10L);
		objGlobalInvoiceSetup.setInvoiceSetupId(UUID.randomUUID());
		objGlobalInvoiceSetup.setEffectiveFromDate(new Date());
		objGlobalInvoiceSetup.setEffectiveToDate(new Date());
		listGlobalInvoiceSetup.add(objGlobalInvoiceSetup);
			
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {globalInvoiceSetupDTO,objGlobalInvoiceSetup,listGlobalInvoiceSetup});
		return testData.iterator();
	}
	
	
	@DataProvider(name = "getGlobalInvoiceSetup")
	public static Iterator<Object[]> getGlobalInvoiceSetup() throws ParseException {
		
		UUID invoiceSetupId=UUID.randomUUID();
		
		GlobalInvoiceSetup globalInvoiceSetup=new GlobalInvoiceSetup();		
		globalInvoiceSetup.setInvoiceSetupId(invoiceSetupId);
		globalInvoiceSetup.setBillCycleFrequencyDate(new Date());
		globalInvoiceSetup.setCreatedBy(10L);
		globalInvoiceSetup.setDelivery("Delivery");
		globalInvoiceSetup.setEffectiveFromDate(new Date());		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {invoiceSetupId,globalInvoiceSetup});
		return testData.iterator();
	}
	
	
	
	@DataProvider(name = "updateGlobalInvoiceSetupStatus")
	public static Iterator<Object[]> updateGlobalInvoiceSetupStatus() throws ParseException {
		  		  
		UUID invoiceSetupId=UUID.randomUUID();		
		GlobalInvoiceSetupDTO globalInvoiceSetupDTO=new GlobalInvoiceSetupDTO();
		globalInvoiceSetupDTO.setInvoiceSetupId(invoiceSetupId);
		globalInvoiceSetupDTO.setInvoiceStatus(null);
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {invoiceSetupId,globalInvoiceSetupDTO});
		return testData.iterator();
	}
	
	
	@DataProvider(name = "getGlobalInvoiceSetupList")
	public static Iterator<Object[]> getGlobalInvoiceSetupList() throws ParseException {
		  		  
		List<GlobalInvoiceSetup> glopalSetupList=new ArrayList<GlobalInvoiceSetup>();
		GlobalInvoiceSetup globalInvoiceSetup=new GlobalInvoiceSetup();
		globalInvoiceSetup.setInvoiceSetupId(UUID.randomUUID());
		globalInvoiceSetup.setCreatedBy(10L);
		globalInvoiceSetup.setEffectiveFromDate(new Date());
		globalInvoiceSetup.setDelivery("Delivery");		
		glopalSetupList.add(globalInvoiceSetup);
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {glopalSetupList});
		return testData.iterator();
	}

	
	
}
