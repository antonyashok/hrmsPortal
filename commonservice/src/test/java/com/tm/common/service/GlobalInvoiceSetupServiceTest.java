package com.tm.common.service;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.tm.common.domain.GlobalInvoiceSetupEntityAttribute;
import com.tm.common.repository.GlobalInvoiceSetupEntityAttributeRepository;
import com.tm.common.service.impl.GlobalInvoiceSetupServiceImpl;

public class GlobalInvoiceSetupServiceTest {

	@InjectMocks
	GlobalInvoiceSetupServiceImpl globalInvoiceSetupServiceImpl;
	
	@Mock
	private GlobalInvoiceSetupEntityAttributeRepository globalInvoiceSetupEntityAttributeRepository;
	  
	@BeforeTest
	public void setUp() {

		globalInvoiceSetupEntityAttributeRepository = mock(GlobalInvoiceSetupEntityAttributeRepository.class);
		globalInvoiceSetupServiceImpl = new GlobalInvoiceSetupServiceImpl(globalInvoiceSetupEntityAttributeRepository); 
	}
	
	@Test
	public void testFindAllAttributes() {

		List<GlobalInvoiceSetupEntityAttribute> globalInvoiceSetupEntityAttributes = new ArrayList<>(); 
		GlobalInvoiceSetupEntityAttribute attribute = new GlobalInvoiceSetupEntityAttribute();
		attribute.setAttributeName("OPTIONS");
		globalInvoiceSetupEntityAttributes.add(attribute);
		when(globalInvoiceSetupEntityAttributeRepository.findAll()).thenReturn(globalInvoiceSetupEntityAttributes);
		AssertJUnit.assertNotNull(globalInvoiceSetupServiceImpl.findAllAttributes());

		attribute.setAttributeName("DELIVERY");
		AssertJUnit.assertNotNull(globalInvoiceSetupServiceImpl.findAllAttributes());
		
		attribute.setAttributeName("TERMS");
		AssertJUnit.assertNotNull(globalInvoiceSetupServiceImpl.findAllAttributes());
		
		attribute.setAttributeName("PAY_CURRENCY");
		AssertJUnit.assertNotNull(globalInvoiceSetupServiceImpl.findAllAttributes());
		
		attribute.setAttributeName("LINE_OF_BUSINESS");
		AssertJUnit.assertNotNull(globalInvoiceSetupServiceImpl.findAllAttributes());
	}
}
