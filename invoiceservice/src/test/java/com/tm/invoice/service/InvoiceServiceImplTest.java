package com.tm.invoice.service;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.tm.commonapi.exception.RecordNotFoundException;
import com.tm.invoice.domain.EngagementContractors;
import com.tm.invoice.domain.InvoiceReturnView;
import com.tm.invoice.dto.EmployeeProfileDTO;
import com.tm.invoice.dto.InvoiceReturnDTO;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.domain.InvoiceReturn;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.mongo.repository.InvoiceReturnRepository;
import com.tm.invoice.mongo.repository.ManualInvoiceRepository;
import com.tm.invoice.repository.EngagementContractorsRepository;
import com.tm.invoice.repository.InvoiceReturnViewRepository;
import com.tm.invoice.service.impl.InvoiceServiceImpl;

public class InvoiceServiceImplTest {

	@InjectMocks
	private InvoiceServiceImpl invoiceServiceImpl;
	private MongoTemplate mongoTemplate;
	private InvoiceReturnViewRepository invoiceReturnDAO;
	RestTemplate restTemplate;
	DiscoveryClient discoveryClient;
	private InvoiceReturnRepository invoiceReturnRepository;
	private InvoiceAttachmentService invoiceAttachmentService;
	private HistoricalService historicalService;
    private ManualInvoiceRepository manualInvoiceRepository;
    private InvoiceQueueRepository invoiceQueueRepository;
    private EngagementContractorsRepository engagementContractorsRepository;

	@BeforeMethod
	public void setUpConfigurationSettingInvoiceServiceTest() throws Exception {

		mongoTemplate = Mockito.mock(MongoTemplate.class);
		invoiceReturnDAO = Mockito.mock(InvoiceReturnViewRepository.class);
		restTemplate = Mockito.mock(RestTemplate.class);
		discoveryClient = Mockito.mock(DiscoveryClient.class);
		invoiceReturnRepository = Mockito.mock(InvoiceReturnRepository.class);
		invoiceAttachmentService = Mockito.mock(InvoiceAttachmentService.class);
		historicalService = Mockito.mock(HistoricalService.class);
    	manualInvoiceRepository = Mockito.mock(ManualInvoiceRepository.class);
    	invoiceQueueRepository = Mockito.mock(InvoiceQueueRepository.class);
    	engagementContractorsRepository=Mockito.mock(EngagementContractorsRepository.class);
		invoiceServiceImpl = new InvoiceServiceImpl(mongoTemplate, invoiceReturnDAO, restTemplate, discoveryClient,
		invoiceReturnRepository, invoiceAttachmentService, historicalService, manualInvoiceRepository, invoiceQueueRepository);

	}

	@Test
	public void getReturnRequest() throws Exception {

		Pageable pageableRequest = null;
		InvoiceQueue invoiceQueue = Mockito.mock(InvoiceQueue.class);
		List<InvoiceQueue> invoiceQueues = Arrays.asList(invoiceQueue);
		Query query = new Query();
		query = query.addCriteria(
				Criteria.where("invoiceNumber").is(java.util.regex.Pattern.compile("")).and("status").is("Delivered"));
		when(mongoTemplate.find(query, InvoiceQueue.class)).thenReturn(invoiceQueues);
		AssertJUnit.assertNotNull(invoiceServiceImpl.getReturnRequest(new PageRequest(5, 5), "test", 1L));

	}

	@Test(expectedExceptions = { RecordNotFoundException.class })
	public void getReturnRequestById() {
		
		UUID id = UUID.randomUUID();
		InvoiceQueue invoiceQueue = Mockito.mock(InvoiceQueue.class);
		
		InvoiceReturnView invoiceReturnView = Mockito.mock(InvoiceReturnView.class);
		List<InvoiceReturnView> invoiceReturnViewList = Arrays.asList(invoiceReturnView);
		
		
		when(invoiceReturnDAO.findByInvoiceSetupId(id)).thenReturn(invoiceReturnViewList);
		
		when(mongoTemplate.findById(id, InvoiceQueue.class)).thenReturn(invoiceQueue);
		invoiceServiceImpl.getReturnRequestById(id);

		when(mongoTemplate.findById(UUID.randomUUID(), InvoiceQueue.class)).thenReturn(invoiceQueue);
		invoiceServiceImpl.getReturnRequestById(UUID.randomUUID());
	}
	
	@Test(expectedExceptions = { RecordNotFoundException.class })
	public void createReturnRequest(){
		InvoiceReturnDTO invoiceReturnDTO =  new InvoiceReturnDTO();
		invoiceReturnDTO.setInvoiceNumber("invoice123");
		EmployeeProfileDTO employeeProfileDTO = Mockito.mock(EmployeeProfileDTO.class);
		invoiceServiceImpl.createReturnRequest(invoiceReturnDTO,employeeProfileDTO);
		invoiceServiceImpl.createReturnRequest(null,employeeProfileDTO);
	}
	
	@Test
	public void getTeamReturnRequest(){
		
		InvoiceReturn InvoiceReturn = Mockito.mock(InvoiceReturn.class);
		List<InvoiceReturn> myList = Arrays.asList(InvoiceReturn);
		Query query = new Query();
		when(mongoTemplate.find(query, InvoiceReturn.class)).thenReturn(myList);
		invoiceServiceImpl.getTeamReturnRequest(new PageRequest(5, 5), 2l);
	}
	
	@Test
	public void getMyReturnRequest(){
		invoiceServiceImpl.getMyReturnRequest(new PageRequest(5, 5),2l);
	}
	
	@Test
	public void updateReturnApprovalStatus(){
		UUID id = UUID.randomUUID();
		Query query = new Query();
		InvoiceReturnDTO invoiceReturnDTO = new InvoiceReturnDTO();
		invoiceReturnDTO.setId(id);
		invoiceReturnDTO.setStatus(invoiceServiceImpl.INVOICE_APPROVED_STATUS);
		InvoiceReturn invoiceReturn = new InvoiceReturn();
		invoiceReturn.setInvoiceSetupId(id);
		invoiceReturn.setInvoiceNumber("invoice1");
		invoiceReturn.setInvoiceQueueId(UUID.randomUUID());
		InvoiceReturnView InvoiceReturnView = Mockito.mock(InvoiceReturnView.class);
		
		List<InvoiceReturnView> invoiceReturnView = Arrays.asList(InvoiceReturnView);
		when(invoiceReturnRepository.save(invoiceReturn)).thenReturn(invoiceReturn);
		when(invoiceReturnDAO.findByInvoiceSetupId(invoiceReturn.getInvoiceSetupId())).thenReturn(invoiceReturnView);		
		when(mongoTemplate.findById(id,InvoiceReturn.class)).thenReturn(invoiceReturn);
		when(invoiceReturnRepository.save(invoiceReturn)).thenReturn(invoiceReturn);
	
		InvoiceQueue invoiceQueue = new InvoiceQueue();
		invoiceQueue.setAmount(new Double(100.00));
		invoiceQueue.setBillDate(new Date());
		invoiceQueue.setId(UUID.randomUUID());		
		invoiceQueue.setExceptionSource("Invoice Return");
		invoiceQueue.setStatus("Pending Approval");
		
		List<InvoiceQueue> invoiceQueueList = new ArrayList<InvoiceQueue>();
		invoiceQueueList.add(invoiceQueue);
		
		when(mongoTemplate.find(query,InvoiceQueue.class)).thenReturn(invoiceQueueList);
		invoiceServiceImpl.updateReturnApprovalStatus(invoiceReturnDTO);
	}
	
	
	@Test(dataProvider = "employeeRestTemplateDataProvider")
	public void getLoggedInUser(ParameterizedTypeReference<EmployeeProfileDTO> responseType,
            ResponseEntity<EmployeeProfileDTO> responseEntity, HttpHeaders httpHeaders, String url){
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		invoiceServiceImpl.getLoggedInUser();
	}
	
	
	
	@Test()
	public void getInvoiceApprovalCountByUserId(){
	
		Long id=10L;
		Long resultLong=10L;
		when(invoiceQueueRepository.getInvoiceQueueCountByBillingSpecialistIdAndStatus("Pending Approval", id)).thenReturn(resultLong);
		when(manualInvoiceRepository.getManualInvoiceCountByFinanceRepIdAndStatus("Pending Approval", id)).thenReturn(resultLong);
		when(invoiceReturnRepository.getInvoiceReturnCountByReportingManagerIdAndStatus("Invoice Return", id)).thenReturn(resultLong);
		invoiceServiceImpl.getInvoiceApprovalCountByUserId(id);
	}
	
	@Test()
	public void getEngagementContracor(){
		
		UUID id=UUID.randomUUID();
		EngagementContractors engagementContractors=new EngagementContractors();
		List<EngagementContractors>listengagementContractors=new ArrayList<EngagementContractors>();
		engagementContractors.setEmployeeengagementId(UUID.randomUUID());
		engagementContractors.setEmployeeId(10L);
		engagementContractors.setEmployeeName("employeeName");
		listengagementContractors.add(engagementContractors);
		when(engagementContractorsRepository.findByEngagementId(id)).thenReturn(listengagementContractors);
		invoiceServiceImpl.getEngagementContracor(id);
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
