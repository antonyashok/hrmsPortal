package com.tm.engagement.service.impl;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.data.mongodb.core.MongoTemplate;
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
import com.tm.engagement.domain.Engagement;
import com.tm.engagement.domain.EngagementHolidays;
import com.tm.engagement.domain.EngagementHolidaysView;
import com.tm.engagement.domain.EngagementOffice;
import com.tm.engagement.domain.EngagementView;
import com.tm.engagement.exception.EngagementException;
import com.tm.engagement.repository.CustomerLocationsRepository;
//import com.tm.engagement.repository.ContractorUsersViewRepository;
import com.tm.engagement.repository.CustomerProfileRepository;
import com.tm.engagement.repository.EngagementContractorsRepository;
import com.tm.engagement.repository.EngagementHolidayRepository;
import com.tm.engagement.repository.EngagementOfficeRepository;
import com.tm.engagement.repository.EngagementRepository;
import com.tm.engagement.repository.EngagementViewRepository;
import com.tm.engagement.repository.ReportingMgrEngmtViewRepository;
import com.tm.engagement.service.dto.EmployeeProfileDTO;
import com.tm.engagement.service.dto.EngagementDTO;
import com.tm.engagement.service.dto.OfficeLocationDTO;
import com.tm.engagement.service.mapper.EngagementMapper;
import com.tm.engagement.service.resources.HolidayResourceDTO;

public class EngagementTest {

	@InjectMocks
	EngagementServiceImpl engagementServiceImpl;

	@Mock
	private EngagementRepository engagementRepository;
	
	@Mock
	private EngagementOfficeRepository engagementOfficeRepository;
	
	@Mock
	private EngagementViewRepository engagementViewRepository;
	
	@Mock
	private CustomerProfileRepository customerRepository;
	
	@Mock
	EngagementHolidayRepository engagementHolidayRepository;
	
	@Mock
	ReportingMgrEngmtViewRepository contractorRepository;
	
	@Mock
	EngagementContractorsRepository engagementContractorsRepository;
	
	@Mock
	CustomerLocationsRepository customerLocationsRepository;

	MongoTemplate mongoTemplate;

	private RestTemplate restTemplate;

	private DiscoveryClient discoveryClient;

	@BeforeTest
	public void setUp() throws Exception {
		
		this.restTemplate = mock(RestTemplate.class);
		this.engagementRepository = mock(EngagementRepository.class);
		this.engagementOfficeRepository = mock(EngagementOfficeRepository.class);
		this.engagementViewRepository = mock(EngagementViewRepository.class);
		this.customerRepository = mock(CustomerProfileRepository.class);
		this.engagementHolidayRepository = mock(EngagementHolidayRepository.class);
		this.customerLocationsRepository = mock(CustomerLocationsRepository.class);
		engagementServiceImpl = new EngagementServiceImpl(engagementRepository, restTemplate, discoveryClient, engagementViewRepository, 
				engagementOfficeRepository, customerRepository, engagementHolidayRepository, contractorRepository,customerLocationsRepository, engagementContractorsRepository,mongoTemplate);
	}

	@Test
	public void testGetEngagementList() {
		
		Pageable pageableRequest = mock(Pageable.class);
		Page<EngagementView> engagementList = mock(Page.class);
		List<EngagementView> engagementViews = mock(List.class); 
		when(engagementViewRepository.findAll(pageableRequest)).thenReturn(engagementList);
		when(engagementList.getContent()).thenReturn(engagementViews);
		AssertJUnit.assertNotNull(engagementServiceImpl.getEngagementList(pageableRequest));
		
		when(engagementList.getContent()).thenReturn(null);
		AssertJUnit.assertNotNull(engagementServiceImpl.getEngagementList(pageableRequest));
	}

	@Test
	public void testGetEngagementDetails() {
		
		UUID engagementId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		Engagement engagement = mock(Engagement.class);
		when(engagementRepository.findOne(engagementId)).thenReturn(engagement);
		AssertJUnit.assertNotNull(engagementServiceImpl.getEngagementDetails(engagementId));
	}
	
	@Test (expectedExceptions = {EngagementException.class})
	public void testGetEngagementDetailsNotFound() {
		
		UUID engagementId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		when(engagementRepository.findOne(engagementId)).thenReturn(null);
		AssertJUnit.assertNotNull(engagementServiceImpl.getEngagementDetails(engagementId));
	}
	
	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testgetEngagements(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		long userId = 100L;
		Engagement engagement = new Engagement();
		List<Engagement> engagements = Arrays.asList(engagement);
		when(engagementRepository.findByEmployeeId(userId)).thenReturn(engagements);
		AssertJUnit.assertNotNull(engagementServiceImpl.getEngagements());
	}
	
	@Test (dataProvider = "officeLocationCommandDataProvider")
	public void testGetConfiguredOfficeLocations(ParameterizedTypeReference<OfficeLocationDTO> responseType,
            ResponseEntity<OfficeLocationDTO> responseEntity, HttpHeaders httpHeaders, String url) {
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		AssertJUnit.assertNotNull(engagementServiceImpl.getConfiguredOfficeLocations(null));
		
		UUID engagementId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		Engagement engagement = mock(Engagement.class);
		when(engagementRepository.findOne(engagementId)).thenReturn(engagement);
		List<Long> ids = mock(List.class);
		when(engagementRepository.findConfiguredOfficeIds(engagementId)).thenReturn(ids);
		EngagementOffice engagementOffice = Mockito.mock(EngagementOffice.class);
		when(engagementOffice.getOfficeId()).thenReturn(123L);
		List<EngagementOffice> configuredOfficeLocations = Arrays.asList(engagementOffice);
		when(engagementRepository.findOfficeIds(engagementId)).thenReturn(configuredOfficeLocations);
		when(ids.remove(anyObject())).thenReturn(true);
		AssertJUnit.assertNotNull(engagementServiceImpl.getConfiguredOfficeLocations(engagementId));
	}
	
	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void testCreateEngagement(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

		EngagementDTO engagementDTO = getEngagementDTO();		
		Engagement engagement = EngagementMapper.INSTANCE.engagementDTOToEngagement(engagementDTO);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		CustomerProfile customerProfile = mock(CustomerProfile.class);
		when(engagementRepository.checkExistByEngagementName(engagementDTO.getEngagementName(), engagementDTO.getCustomerOrganizationId())).thenReturn(null);
		when(customerRepository.findOne(engagementDTO.getCustomerOrganizationId())).thenReturn(customerProfile);
		when(customerRepository.getCustomerEffectiveDates(anyLong(), anyObject(), anyObject())).thenReturn(Arrays.asList(new CustomerProfile()));
		when(engagementRepository.save(engagement)).thenReturn(engagement);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		AssertJUnit.assertNull(engagementServiceImpl.createEngagement(engagementDTO));
		
		engagementDTO.setEngagementId(UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00"));
		when(engagementRepository.checkExistByEngagementId(engagement.getEngagementId(),engagement.getCustomerOrganizationId())).thenReturn(Arrays.asList("Engagement1", "Engagement2"));
		AssertJUnit.assertNull(engagementServiceImpl.createEngagement(engagementDTO));
	}
	
	@Test(dataProvider = "employeeRestTemplateDataProvider", expectedExceptions = {EngagementException.class})
	public void testCreateEngagementWithEmptyCustomerProfile(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

		EngagementDTO engagementDTO = getEngagementDTO();		
		Engagement engagement = EngagementMapper.INSTANCE.engagementDTOToEngagement(engagementDTO);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		CustomerProfile customerProfile = mock(CustomerProfile.class);
		when(engagementRepository.checkExistByEngagementName(engagementDTO.getEngagementName(), engagementDTO.getCustomerOrganizationId())).thenReturn(null);
		when(customerRepository.findOne(engagementDTO.getCustomerOrganizationId())).thenReturn(customerProfile);
		when(customerRepository.getCustomerEffectiveDates(anyLong(), anyObject(), anyObject())).thenReturn(Arrays.asList());
		when(engagementRepository.save(engagement)).thenReturn(engagement);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		when(customerProfile.getEffectiveStartDate()).thenReturn(new Date("13/05/2015"));
		when(customerProfile.getEffectiveEndDate()).thenReturn(new Date("13/05/2018"));
		engagementServiceImpl.createEngagement(engagementDTO);
	}
	
	@Test(dataProvider = "employeeRestTemplateDataProvider", expectedExceptions = {EngagementException.class})
	public void testCreateEngagementWithExistingEngagement(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

		EngagementDTO engagementDTO = getEngagementDTO();		
		Engagement engagement = EngagementMapper.INSTANCE.engagementDTOToEngagement(engagementDTO);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		CustomerProfile customerProfile = mock(CustomerProfile.class);
		when(engagementRepository.checkExistByEngagementName(engagementDTO.getEngagementName(), engagementDTO.getCustomerOrganizationId())).thenReturn(null);
		when(customerRepository.findOne(engagementDTO.getCustomerOrganizationId())).thenReturn(customerProfile);
		when(customerRepository.getCustomerEffectiveDates(anyLong(), anyObject(), anyObject())).thenReturn(Arrays.asList());
		when(engagementRepository.save(engagement)).thenReturn(engagement);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		when(customerProfile.getEffectiveStartDate()).thenReturn(new Date("13/05/2015"));
		when(customerProfile.getEffectiveEndDate()).thenReturn(new Date("13/05/2018"));
		when(engagementRepository.checkExistByEngagementName(engagementDTO.getEngagementName(), engagementDTO.getCustomerOrganizationId())).thenReturn(engagement);
		engagementServiceImpl.createEngagement(engagementDTO);
	}

	@Test(dataProvider = "employeeRestTemplateDataProvider", expectedExceptions = {EngagementException.class})
	public void testCreateEngagementWithExistingEngagementId(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

		EngagementDTO engagementDTO = getEngagementDTO();		
		Engagement engagement = EngagementMapper.INSTANCE.engagementDTOToEngagement(engagementDTO);
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		CustomerProfile customerProfile = mock(CustomerProfile.class);
		when(engagementRepository.checkExistByEngagementName(engagementDTO.getEngagementName(), engagementDTO.getCustomerOrganizationId())).thenReturn(null);
		when(customerRepository.findOne(engagementDTO.getCustomerOrganizationId())).thenReturn(customerProfile);
		when(customerRepository.getCustomerEffectiveDates(anyLong(), anyObject(), anyObject())).thenReturn(Arrays.asList());
		when(engagementRepository.save(engagement)).thenReturn(engagement);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		when(customerProfile.getEffectiveStartDate()).thenReturn(new Date("13/05/2015"));
		when(customerProfile.getEffectiveEndDate()).thenReturn(new Date("13/05/2018"));
		when(engagementRepository.checkExistByEngagementName(engagementDTO.getEngagementName(), engagementDTO.getCustomerOrganizationId())).thenReturn(engagement);
		
		engagementDTO.setEngagementId(UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00"));
		when(engagementRepository.checkExistByEngagementId(engagementDTO.getEngagementId(),engagementDTO.getCustomerOrganizationId())).thenReturn(Arrays.asList("TestProject", "Engagement2"));
		engagementServiceImpl.createEngagement(engagementDTO);
	}
	
	@Test
	public void testGetEngagementHolidayList() {

		Pageable pageable = mock(Pageable.class);
		Page<EngagementHolidaysView> engagementList = mock(Page.class);
		when(engagementHolidayRepository.getEngagementHolidays(pageable)).thenReturn(engagementList);
		AssertJUnit.assertNotNull(engagementServiceImpl.getEngagementHolidayList(pageable));
	}
	
	@Test (expectedExceptions = {EngagementException.class})
	public void testGetEngagementHolidayDetails() {
	
		UUID engagementHolidayId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		EngagementHolidaysView engagement = mock(EngagementHolidaysView.class);
		when(engagementHolidayRepository.getEngagementHoliday(engagementHolidayId)).thenReturn(engagement);
		AssertJUnit.assertNotNull(engagementServiceImpl.getEngagementHolidayDetails(engagementHolidayId));
		
		when(engagementHolidayRepository.getEngagementHoliday(engagementHolidayId)).thenReturn(null);
		engagementServiceImpl.getEngagementHolidayDetails(engagementHolidayId);
	}

	@Test (dataProvider = "employeeRestTemplateDataProvider", expectedExceptions = {EngagementException.class})
	public void testCreateEngagementHoliday(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		HolidayResourceDTO engagementHolidays = mock(HolidayResourceDTO.class);
		EngagementHolidaysView engagementHolidaysView = mock(EngagementHolidaysView.class);
		EngagementHolidays engagementData = mock(EngagementHolidays.class);
		when(engagementHolidays.getEngagementId()).thenReturn("067e6162-3b6f-4ae2-a171-2470b63dff00");
		when(engagementHolidays.getHolidayDate()).thenReturn("05/12/2017");
		when(engagementHolidays.getHolidayDescription()).thenReturn("TestHolidayDescription");
		when(engagementHolidayRepository.checkEngagementHoliday(anyString(), anyObject())).thenReturn(null);
		Engagement engagement = Mockito.mock(Engagement.class);
		when(engagementRepository.findOne(UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00"))).thenReturn(engagement);
		when(engagement.getEngmtStartDate()).thenReturn(new Date("05/01/2017"));
		when(engagement.getEngmtEndDate()).thenReturn(new Date("05/01/2018"));
		when(engagementHolidayRepository.save(engagementData)).thenReturn(engagementData);
		AssertJUnit.assertNull(engagementServiceImpl.createEngagementHoliday(engagementHolidays));

		when(engagementHolidays.getHolidayDate()).thenReturn("05/12/2019");
		try {
			engagementServiceImpl.createEngagementHoliday(engagementHolidays);
		} catch (EngagementException e) {}
		
		when(engagementHolidays.getHolidayDate()).thenReturn("05/12/2016");
		try {
			engagementServiceImpl.createEngagementHoliday(engagementHolidays);
		} catch (EngagementException e) {}
		
		when(engagementHolidayRepository.checkEngagementHoliday(anyString(), anyObject())).thenReturn(engagementHolidaysView);
		engagementServiceImpl.createEngagementHoliday(engagementHolidays);
	}
	
	@Test (dataProvider = "employeeRestTemplateDataProvider", expectedExceptions = {EngagementException.class})
	public void testCreateEngagementHolidayNull(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException {

		UUID engagementHolidayId = UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00");
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		HolidayResourceDTO engagementHolidays = mock(HolidayResourceDTO.class);
		EngagementHolidaysView engagementHolidaysView = mock(EngagementHolidaysView.class);
		EngagementHolidays engagementData = mock(EngagementHolidays.class);
		when(engagementHolidays.getEngagementId()).thenReturn("067e6162-3b6f-4ae2-a171-2470b63dff00");
		when(engagementHolidays.getHolidayDate()).thenReturn("05/12/2017");
		when(engagementHolidays.getHolidayDescription()).thenReturn("TestHolidayDescription");
		when(engagementHolidayRepository.checkEngagementHoliday(anyString(), anyObject())).thenReturn(null);
		when(engagementHolidayRepository.save(engagementData)).thenReturn(engagementData);
		when(engagementHolidayRepository.checkEngagementHoliday(anyString(), anyObject())).thenReturn(engagementHolidaysView);
		when(engagementHolidays.getEngagementHolidayId()).thenReturn(engagementHolidayId);
		
		when(engagementHolidayRepository.checkUpdateEngagementHoliday(anyString(), anyObject(), anyObject())).thenReturn(null);
		AssertJUnit.assertNull(engagementServiceImpl.createEngagementHoliday(engagementHolidays));
		
		when(engagementHolidayRepository.checkUpdateEngagementHoliday(anyString(), anyObject(), anyObject())).thenReturn(engagementHolidaysView);
		engagementServiceImpl.createEngagementHoliday(engagementHolidays);
	}

	private EngagementDTO getEngagementDTO() {
		
		EngagementDTO engagementDTO = new EngagementDTO();
		engagementDTO.setEngagementName("TestProject");
		engagementDTO.setBillToMgrName("TestManager");
		engagementDTO.setProjectMgrName("TestProjectManager");
		engagementDTO.setHiringMgrName("TestHiringManager");
		engagementDTO.setBillAddress("TestAddress");
		engagementDTO.setPostalCode("625014");
		engagementDTO.setEngmtStartDate("01/13/2015");
		engagementDTO.setEngmtEndDate("01/13/2018");
		engagementDTO.setCustomerOrganizationId(1000L);
		
		List<OfficeLocationDTO> officeList = new ArrayList<OfficeLocationDTO>();
		OfficeLocationDTO officeLocationDTO = new OfficeLocationDTO();
		officeLocationDTO.setOfficeId(1L);
		officeLocationDTO.setOfficeName("Madurai");
		officeLocationDTO.setActiveFlag(EngagementOffice.ActiveFlagEnum.Y.toString());
		officeList.add(officeLocationDTO);
		engagementDTO.setOfficeList(officeList);
		
		return engagementDTO;
	}

	@DataProvider(name = "officeLocationCommandDataProvider")
    public static Iterator<Object[]> officeLocationCommand() {
   	
        String url = "http://COMMONSERVICEMANAGEMENT/common/officelocations";
        String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
        List<OfficeLocationDTO> officeLocationDTOList = new ArrayList<>();
        OfficeLocationDTO officeLocationDTO = new OfficeLocationDTO();
        officeLocationDTO.setActiveFlag("y");
        officeLocationDTO.setOfficeId(123L);
        officeLocationDTO.setOfficeName("NewYork");
        officeLocationDTOList.add(officeLocationDTO);
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        ParameterizedTypeReference<List<OfficeLocationDTO>> responseType = new ParameterizedTypeReference<List<OfficeLocationDTO>>() {};
        ResponseEntity<List<OfficeLocationDTO>> responseEntity = new ResponseEntity<>(officeLocationDTOList, HttpStatus.OK);
        HttpHeaders httpHeaders = new HttpHeaders() {
 			private static final long serialVersionUID = -3670115299291671935L;
 			{
 				set(HttpHeaders.AUTHORIZATION,
 						new StringBuilder(EngagementConstants.BEARER).append(" ").append(requestedToken).toString());
 			}
 		};
        testData.add(new Object[] {responseType, responseEntity, httpHeaders, url});
        return testData.iterator();
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
    
    public Engagement getEngagement(){
    	Engagement engagement = new Engagement();
    	engagement.setEngagementId(UUID.randomUUID());
    	engagement.setCustomerOrganizationId(RandomUtils.nextLong());
    	return engagement;
    }
	
    @Test
    public void updateEngagement() {
    	
    	BigDecimal initialAmount=BigDecimal.TEN, totalAmount=initialAmount, balanceAmount=totalAmount;
    	 UUID engagementId=UUID.randomUUID(),poId=UUID.randomUUID();
    	 String poNumber= RandomStringUtils.random(10),type= "Revenue";
    	 Date engagementEndDate = new Date(),issueDate=new Date();
    	 DateFormat format = new SimpleDateFormat();
    	 String engmntDate = format.format(engagementEndDate);
    	 String poIssueDate = format.format(issueDate);
    	 int updateValue=RandomUtils.nextInt(10);
    	 
    	 Engagement engagement = getEngagement();
    	 engagementRepository.updateEngagementForRevenuePO(initialAmount,
                     totalAmount, balanceAmount, engagementId, poId, poNumber, engagementEndDate,issueDate);
    	 when(engagementRepository.findOne(engagementId)).thenReturn(engagement);
    	 when(customerRepository.updateCustomerDate(engagementEndDate,
         engagement.getCustomerOrganizationId())).thenReturn(updateValue);
    	 try{
    		  engagementServiceImpl.updateEngagement(initialAmount, totalAmount, balanceAmount, engagementId, poId, poNumber, type, engmntDate, poIssueDate);
    	   }catch(Exception e){}
    	 try{type="Expense";
			  engagementServiceImpl.updateEngagement(initialAmount, totalAmount, balanceAmount, engagementId, poId, poNumber, type, engmntDate, poIssueDate);
	       }catch(Exception e){}
	}
    
}
