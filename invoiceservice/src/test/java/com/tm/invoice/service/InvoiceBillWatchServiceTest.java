package com.tm.invoice.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.tm.invoice.dto.EmployeeProfileDTO;
import com.tm.invoice.mongo.domain.AuditFields;
import com.tm.invoice.mongo.domain.InvoiceBillWatch;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.repository.InvoiceBillWatchRepository;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.mongo.repository.InvoiceReturnRepository;
import com.tm.invoice.service.impl.InvoiceBillWatchServiceImpl;
import com.tm.invoice.service.resttemplate.EmployeeRestTemplate;

public class InvoiceBillWatchServiceTest {

	@InjectMocks
	InvoiceBillWatchServiceImpl invoiceBillWatchServiceImpl;

	private InvoiceQueueRepository invoiceQueueRepository;
	private InvoiceBillWatchRepository invoiceBillWatchRepository;
	private InvoiceReturnRepository invoiceReturnRepository;
	
	@Mock
	private RestTemplate restTemplate;

	@BeforeMethod
	@BeforeTest
	public void setUp() throws Exception {
		this.invoiceQueueRepository = Mockito.mock(InvoiceQueueRepository.class);
		this.invoiceBillWatchRepository = Mockito.mock(InvoiceBillWatchRepository.class);
		this.invoiceReturnRepository = Mockito.mock(InvoiceReturnRepository.class);
		this.restTemplate = Mockito.mock(RestTemplate.class);
		this.invoiceBillWatchServiceImpl = new InvoiceBillWatchServiceImpl(invoiceQueueRepository,
				invoiceBillWatchRepository, invoiceReturnRepository,restTemplate);
	}
	
	@Test
	public void getInvoiceQueues(){
		
		Pageable pageable = null;
		Pageable pageableRequest = pageable;
		
		Pageable pageable1 = mock(Pageable.class);

		AuditFields auditFields = new AuditFields();
		auditFields.setBy(2l);
		auditFields.setOn(new Date());

		
		InvoiceQueue invoiceQueue = new InvoiceQueue();
		invoiceQueue.setAmount(100);
		invoiceQueue.setUpdated(auditFields);
		invoiceQueue.setStartDate(new Date());
		invoiceQueue.setEndDate(new Date());
		
		List<InvoiceQueue> invoiceQueueList = new ArrayList<>();
		invoiceQueueList.add(invoiceQueue);
		Page<InvoiceQueue> invoiceQueues = new PageImpl<>(invoiceQueueList, pageable, 1);
		
		
		List<InvoiceBillWatch> billWatchList = new ArrayList<InvoiceBillWatch>(); 
		
		when(invoiceQueueRepository.getInvoiceQueues(Mockito.anyLong(), Mockito.anyListOf(String.class),
				(Pageable)Mockito.anyObject(), Mockito.anyBoolean())).thenReturn(invoiceQueues);
		when(invoiceBillWatchRepository.getBillWatchByInvoiceNumber(Mockito.anyString())).thenReturn(billWatchList);
		
		invoiceBillWatchServiceImpl.getInvoiceQueues(5l,pageable1);
		
	}
	
	/*@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void createInvoiceReturn(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url){
		
		InvoiceQueue invoiceQueue = new InvoiceQueue();
		List<InvoiceQueue> invoiceQueueList = new ArrayList<InvoiceQueue>(); 
		invoiceQueueList.add(invoiceQueue);
		
		List<String> statuses = new ArrayList<>();
		statuses.add("Delivered");
		
		this.restTemplate = Mockito.mock(RestTemplate.class);
		
//		EmployeeProfileDTO employeeProfileDTO =new EmployeeProfileDTO();
//		employeeProfileDTO.setEmployeeId(2l);
//		EmployeeProfileDTO employeeProfileDTO = Mockito.mock(EmployeeProfileDTO.class);
//		when(employeeProfileDTO.getEmployeeId()).thenReturn(2l);
		when(invoiceQueueRepository.getInvoiceQueuesByInvoiceNumber("123", statuses)).thenReturn(invoiceQueueList);
//		when(invoiceBillWatchServiceImpl.getLoggedInUser()).thenReturn(employeeProfileDTO);
		
//		EmployeeRestTemplate employeeRestTemplate = Mockito.mock(EmployeeRestTemplate.class);
//		EmployeeRestTemplate employeeRestTemplate = new EmployeeRestTemplate(Mockito.anyObject(), Mockito.anyString(), Mockito.anyString());
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		
//		when(employeeRestTemplate.getEmployeeProfileDTO()).thenReturn(employeeProfileDTO);
		invoiceBillWatchServiceImpl.createInvoiceReturn("123");
	}*/
	
	@Test
	public void createExceptionReport(){
		
		InvoiceBillWatch invoiceBillWatch = Mockito.mock(InvoiceBillWatch.class);
		invoiceBillWatch.setOriginalHours(Mockito.anyDouble());
		invoiceBillWatch.setActualHours(Mockito.anyDouble());
		
		InvoiceQueue invoiceQueue = Mockito.mock(InvoiceQueue.class);
		
		List<InvoiceBillWatch> billWatchList = new ArrayList<InvoiceBillWatch>(); 
		billWatchList.add(invoiceBillWatch);
		List<InvoiceQueue> invoiceQueueList = new ArrayList<InvoiceQueue>(); 
		invoiceQueueList.add(invoiceQueue);
		
		List<String> statuses = new ArrayList<>();
		statuses.add("Pending Approval");
		
		when(invoiceBillWatchRepository.getBillWatchByInvoiceNumber(Mockito.anyString())).thenReturn(billWatchList);
		when(invoiceQueueRepository.getInvoiceQueuesByInvoiceNumber(Mockito.anyString(),Mockito.anyListOf(String.class))).thenReturn(invoiceQueueList);
		invoiceBillWatchServiceImpl.createExceptionReport(Mockito.anyString());
		
	}
	
	
	
	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void createInvoiceReturn(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url){
		
		InvoiceQueue invoiceQueue = new InvoiceQueue();
		List<InvoiceQueue> invoiceQueueList = new ArrayList<InvoiceQueue>(); 
		invoiceQueueList.add(invoiceQueue);
		
		List<String> statuses = new ArrayList<>();
		statuses.add("Delivered");
		
		
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		when(invoiceQueueRepository.getInvoiceQueuesByInvoiceNumber("123", statuses)).thenReturn(invoiceQueueList);
		
		
		invoiceBillWatchServiceImpl.createInvoiceReturn("123");
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
						new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
			}
		};
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {responseType, responseEntity, httpHeaders, url});
        return testData.iterator();
    }

}
