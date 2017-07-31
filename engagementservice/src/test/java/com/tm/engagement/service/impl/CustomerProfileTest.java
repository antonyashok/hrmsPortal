package com.tm.engagement.service.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.tm.engagement.constants.EngagementConstants;
import com.tm.engagement.domain.CustomerProfile;
import com.tm.engagement.domain.CustomerProfile.ActiveFlagEnum;
import com.tm.engagement.domain.CustomerProfileView;
import com.tm.engagement.domain.EngagementOffice;
import com.tm.engagement.exception.CustomerProfileException;
import com.tm.engagement.repository.CustomerLocationsRepository;
import com.tm.engagement.repository.CustomerProfileRepository;
import com.tm.engagement.repository.CustomerProfileViewRepository;
import com.tm.engagement.repository.EngagementOfficeRepository;
import com.tm.engagement.service.dto.CompanyLocationDTO;
import com.tm.engagement.service.dto.CompanyProfileDTO;
import com.tm.engagement.service.dto.CustomerProfileDTO;
import com.tm.engagement.service.dto.EmployeeProfileDTO;
import com.tm.engagement.service.mapper.CustomerProfileMapper;

public class CustomerProfileTest {

	@InjectMocks
	CustomerProfileServiceImpl customerProfileServiceImpl;

	@Mock
	private CustomerProfileRepository customerRepository;

	@Mock
	private CustomerProfileViewRepository customerViewRepository;
	
	@Mock
	CustomerLocationsRepository customerLocationsRepository;

	@Mock
	private CustomerProfileMapper customerProfileMapper;
	
	@Mock
	private EngagementOfficeRepository engagementOfficeRepository;

	private RestTemplate restTemplate;

	private DiscoveryClient discoveryClient;

	@BeforeTest
	public void setUp() throws Exception {
		
		this.restTemplate = Mockito.mock(RestTemplate.class);
		this.customerRepository = Mockito.mock(CustomerProfileRepository.class);
		this.customerLocationsRepository = Mockito.mock(CustomerLocationsRepository.class);
		this.customerViewRepository = Mockito.mock(CustomerProfileViewRepository.class);
		this.customerProfileMapper = Mockito.mock(CustomerProfileMapper.class);
		this.engagementOfficeRepository = Mockito.mock(EngagementOfficeRepository.class);
		customerProfileServiceImpl = new CustomerProfileServiceImpl(customerRepository, customerViewRepository, customerLocationsRepository,
				engagementOfficeRepository, restTemplate, discoveryClient);
	}

	@Test
	public void testCustomerDetails() {
		
		Long customerId = 1l;
		CustomerProfile customerProfile = new CustomerProfile();
		customerProfile.setCustomerId(customerId);
		customerProfile.setCustomerNumber("CUST_01");
		//customerProfile.setStateName("California");
		//customerProfile.setCityName("USA");
		customerProfile.setEffectiveStartDate(new Date("01/01/2017"));
		customerProfile.setEffectiveEndDate(new Date("12/31/2017"));
		CustomerProfileDTO customerProfileDTO = CustomerProfileMapper.INSTANCE.customerToCustomerEditDTO(customerProfile);
		when(customerRepository.findOne(customerId)).thenReturn(customerProfile);
		AssertJUnit.assertEquals(customerProfileDTO, customerProfileServiceImpl.getCustomerDetails(customerId));
	}

	@Test
	public void testCustomerDetailsEmpty() throws Exception {
		
		Long customerId = RandomUtils.nextLong();
		when(customerRepository.findOne(customerId)).thenReturn(null);
		try{
		customerProfileServiceImpl.getCustomerDetails(customerId);
		}catch(Exception e){}
		when(customerRepository.findOne(customerId)).thenReturn(getCustomerProfile());
		List<Long> officeIds = new ArrayList<>();
		officeIds.add(RandomUtils.nextLong());
		CompanyProfileDTO companyProfileDTO =new CompanyProfileDTO();
		companyProfileDTO.setOfficeIds(officeIds);
		when(customerLocationsRepository.getOfficeLocationsByCustomerId(customerId)).thenReturn(officeIds);
		List<EngagementOffice> engagementOfficeList =new ArrayList<>(Arrays.asList(getEngagementOffice())); 
				when(engagementOfficeRepository
				.getEngagementOfficeByOfficeId(companyProfileDTO
						.getOfficeIds())).thenReturn(engagementOfficeList);
		customerProfileServiceImpl.getCustomerDetails(customerId);
	}
	
	@Test
	public void testGetActiveCustomersList() {
		
		List<CustomerProfile> customerProfiles = Mockito.mock(List.class);
		when(customerRepository.getActiveCustomerProfile()).thenReturn(customerProfiles);
		AssertJUnit.assertEquals(customerProfiles, customerProfileServiceImpl.getActiveCustomersList());
	}
	
	@Test
	public void testGetCustomerProfileList() {
		
		Pageable pageable = Mockito.mock(Pageable.class);
		Page<CustomerProfileView> customerProfileList = Mockito.mock(Page.class);
		CustomerProfileView customerProfileView = Mockito.mock(CustomerProfileView.class);
		List<CustomerProfileView> customerProfileViews = Arrays.asList(customerProfileView); 
		CustomerProfile customerProfile = new CustomerProfile();
		customerProfile.setCustomerId(1l);
		customerProfile.setCustomerNumber("CUST_01");
		//customerProfile.setStateName("California");
		//customerProfile.setCityName("USA");
		customerProfile.setEffectiveStartDate(new Date("01/01/2017"));
		customerProfile.setEffectiveEndDate(new Date("12/31/2017"));
		customerProfile.setActiveFlag(ActiveFlagEnum.Y);
		when(customerViewRepository.getAllCustomerDetailByActive(Mockito.anyObject(), Mockito.anyString(), Mockito.anyObject())).thenReturn(customerProfileList);
		when(customerProfileList.getContent()).thenReturn(customerProfileViews);
		when(pageable.getPageSize()).thenReturn(10);
		when(pageable.getPageNumber()).thenReturn(1);
		AssertJUnit.assertNotNull(customerProfileServiceImpl.getCustomerProfileList(Mockito.anyObject(), Mockito.anyString(), Mockito.anyString()));
		
		when(customerViewRepository.findAll(pageable)).thenReturn(null);
		AssertJUnit.assertNull(customerProfileServiceImpl.getCustomerProfileList(pageable, "searchString", null));
		
		when(customerProfileList.getContent()).thenReturn(new ArrayList<CustomerProfileView>());
		when(customerViewRepository.findAll((Pageable) Mockito.anyObject())).thenReturn(customerProfileList);
		AssertJUnit.assertNotNull(customerProfileServiceImpl.getCustomerProfileList(pageable, "searchString", null));
	}

	@Test(dataProvider = "employeeRestTemplateDataProvider", expectedExceptions = {CustomerProfileException.class})
	public void testCreateCustomerCheckNumberByCustomerId(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) {
		
		CustomerProfile customerProfile = new CustomerProfile();
		customerProfile.setCustomerId(1l);
		customerProfile.setCustomerNumber("CUST_01");
		//customerProfile.setStateName("California");
		//customerProfile.setCityName("USA");
		customerProfile.setEffectiveStartDate(new Date("01/01/2017"));
		customerProfile.setEffectiveEndDate(new Date("12/31/2017"));
		customerProfile.setCustomerName("TestCustomerName");
		CustomerProfileDTO customerProfileDTO = CustomerProfileMapper.INSTANCE.customerToCustomerEditDTO(customerProfile);
		when(customerRepository.checkExistByCustomerNumber(customerProfile.getCustomerNumber())).thenReturn(customerProfile);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		when(customerRepository.checkExistByCustomerId(customerProfile.getCustomerId())).thenReturn(Arrays.asList("CUST_01"));
		customerProfileServiceImpl.createCustomer(customerProfileDTO);
	}

	@Test(dataProvider = "employeeRestTemplateDataProvider", expectedExceptions = {CustomerProfileException.class})
	public void testCreateCustomerCheckNumberByCustomerName(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) {
		
		CustomerProfile customerProfile = new CustomerProfile();
		customerProfile.setCustomerId(null);
		customerProfile.setCustomerNumber("CUST_01");
		//customerProfile.setStateName("California");
		//customerProfile.setCityName("USA");
		customerProfile.setEffectiveStartDate(new Date("01/01/2017"));
		customerProfile.setEffectiveEndDate(new Date("12/31/2017"));
		customerProfile.setCustomerName("TestCustomerName");
		CustomerProfileDTO customerProfileDTO = CustomerProfileMapper.INSTANCE.customerToCustomerEditDTO(customerProfile);
		when(customerRepository.checkExistByCustomerNumber(customerProfile.getCustomerNumber())).thenReturn(customerProfile);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		when(customerRepository.checkExistByCustomerNumber(customerProfile.getCustomerNumber())).thenReturn(customerProfile);
		customerProfileServiceImpl.createCustomer(customerProfileDTO);
	}

	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testCreateCustomer(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) {
		
		CustomerProfileDTO customerProfileDTO = new CustomerProfileDTO();
		customerProfileDTO.setCustomerId(1l);
		customerProfileDTO.setCustomerNumber("CUST_01");
		//customerProfileDTO.setStateName("California");
		//customerProfileDTO.setCityName("USA");
		customerProfileDTO.setEffectiveStartDate("01/01/2017");
		customerProfileDTO.setEffectiveEndDate("12/31/2017");
		customerProfileDTO.setCustomerName("TestCustomerName");
		CustomerProfile customerProfile = CustomerProfileMapper.INSTANCE.customerProfileDTOToCustomer(customerProfileDTO);
		when(customerRepository.checkExistByCustomerNumber(customerProfile.getCustomerNumber())).thenReturn(customerProfile);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		when(customerRepository.checkExistByCustomerId(customerProfile.getCustomerId())).thenReturn(Arrays.asList());
		when(customerRepository.saveAndFlush((CustomerProfile) Mockito.anyObject())).thenReturn(customerProfile);
		AssertJUnit.assertNull(customerProfileServiceImpl.createCustomer(customerProfileDTO));
	}
	
	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testCreateCustomerWithCustomerIdNull(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) {
		
		CustomerProfileDTO customerProfileDTO = new CustomerProfileDTO();
		customerProfileDTO.setCustomerId(null);
		customerProfileDTO.setCustomerNumber("CUST_01");
		//customerProfileDTO.setStateName("California");
		//customerProfileDTO.setCityName("USA");
		customerProfileDTO.setEffectiveStartDate("01/01/2017");
		customerProfileDTO.setEffectiveEndDate("12/31/2017");
		customerProfileDTO.setCustomerName("TestCustomerName");
		CustomerProfile customerProfile = CustomerProfileMapper.INSTANCE.customerProfileDTOToCustomer(customerProfileDTO);
		when(customerRepository.checkExistByCustomerNumber(customerProfile.getCustomerNumber())).thenReturn(customerProfile);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		when(customerRepository.checkExistByCustomerNumber(customerProfile.getCustomerNumber())).thenReturn(null);
		when(customerRepository.saveAndFlush((CustomerProfile) Mockito.anyObject())).thenReturn(customerProfile);
		AssertJUnit.assertNull(customerProfileServiceImpl.createCustomer(customerProfileDTO));
	}
	
	@DataProvider(name = "employeeRestTemplateDataProvider")
    public static Iterator<Object[]> employeeRestTemplate() {
    	
        String url = "http://COMMONSERVICEMANAGEMENT/common/employee-profile?emailId=allinall@techmango.net";
        String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
        EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();        
        employeeProfileDTO.setEmployeeId(100L);
        ParameterizedTypeReference<EmployeeProfileDTO> responseType = new ParameterizedTypeReference<EmployeeProfileDTO>() {};
        ResponseEntity<EmployeeProfileDTO> responseEntity = new ResponseEntity<>(employeeProfileDTO, HttpStatus.OK);
        HttpHeaders httpHeaders = new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION,
						new StringBuilder(EngagementConstants.BEARER).append(" ").append(requestedToken).toString());
			}
		};
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {responseType, responseEntity, httpHeaders, url});
        return testData.iterator();
    }
    
    private CustomerProfile getCustomerProfile(){
    	CustomerProfile customerProfile =new CustomerProfile();
    	customerProfile.setActiveFlag(ActiveFlagEnum.Y);
    	customerProfile.setCityId(RandomUtils.nextLong());
    	CompanyLocationDTO companyLocationDTO =Mockito.mock(CompanyLocationDTO.class);
    	List<CompanyLocationDTO> companyLocationDTOList = new ArrayList<>(Arrays.asList(companyLocationDTO));
    	customerProfile.setCompanyLocationDTO(companyLocationDTOList);
    	customerProfile.setCountryId(RandomUtils.nextLong());
    	return customerProfile;
    }
    
    private EngagementOffice getEngagementOffice(){
    	EngagementOffice engagementOffice =new EngagementOffice();
    	engagementOffice.setActiveFlag(EngagementOffice.ActiveFlagEnum.Y);
    	engagementOffice.setCreatedDate(new Date());
    	engagementOffice.setCustomerLocationId(UUID.randomUUID());
    	engagementOffice.setEngagementId(UUID.randomUUID());
    	engagementOffice.setEngmtOfficeId(RandomUtils.nextLong());
    	engagementOffice.setLinkedLocations(RandomStringUtils.random(10));
    	engagementOffice.setOfficeId(RandomUtils.nextLong());
    	engagementOffice.setUpdatedDate(new Date());
    	return engagementOffice;
    }
}