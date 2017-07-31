package com.tm.common.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.tm.common.domain.LookUpView;
import com.tm.common.repository.LookUpViewRepository;
import com.tm.common.service.impl.LookUpViewServiceImpl;

public class LookUpViewServiceTest {
	
	@InjectMocks
	LookUpViewServiceImpl lookUpViewServiceImpl;

	@Mock
	private LookUpViewRepository lookUpViewRepository;
	
	@BeforeTest
	public void setUp() throws Exception {
		
		this.lookUpViewRepository = mock(LookUpViewRepository.class);
		lookUpViewServiceImpl = new LookUpViewServiceImpl(lookUpViewRepository);
	}
	
	@Test
	public void testFindAllInvoiceSetupAttributes() {

		UUID attributeId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		UUID entityAttributeMapId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff11");
		UUID entityId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff22");
		
		LookUpView lookUpView = mock(LookUpView.class);
		List<LookUpView> results = Arrays.asList(lookUpView);
		when(lookUpView.getAttributeId()).thenReturn(attributeId);
		when(lookUpView.getAttributeName()).thenReturn("TestAttributeName");
		when(lookUpView.getAttributeValue()).thenReturn("TestAttributeValue");
		when(lookUpView.getEntityAttributeMapId()).thenReturn(entityAttributeMapId);
		when(lookUpView.getEntityAttributeMapValue()).thenReturn("TestEntityAttributeMapValue");
		when(lookUpView.getEntityId()).thenReturn(entityId);
		when(lookUpView.getEntityName()).thenReturn("TestEntityName");
		
		when(lookUpViewRepository.findByEntityName(anyString())).thenReturn(results);
		AssertJUnit.assertNotNull(lookUpViewServiceImpl.findAllInvoiceSetupAttributes("TestEntityName"));
		
		when(lookUpViewRepository.findByEntityName(anyString())).thenReturn(null);
		AssertJUnit.assertNotNull(lookUpViewServiceImpl.findAllInvoiceSetupAttributes("TestEntityName"));
	}
}
