package com.tm.invoice.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.struts.mock.MockHttpServletRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tm.employee.dto.EmployeeDTO;
import com.tm.invoice.domain.PurchaseOrder;
import com.tm.invoice.domain.Task;
import com.tm.invoice.dto.BillingQueueDTO;
import com.tm.invoice.dto.EmployeeProfileDTO;
import com.tm.invoice.dto.TaskDTO;
import com.tm.invoice.enums.ActiveFlag;
import com.tm.invoice.mongo.domain.BillingQueue;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.mongo.repository.BillingQueueRepository;
import com.tm.invoice.repository.PurchaseOrderRepository;
import com.tm.invoice.service.impl.PendingBillingQueueServiceImpl;
import com.tm.invoice.service.mapper.PendingBillingQueueMapper;
import com.tm.invoice.service.resttemplate.EmployeeRestTemplate;

public class PendingBillingQueueServiceTest {
	
	
	  @InjectMocks
	  PendingBillingQueueServiceImpl pendingBillingQueueServiceImpl;
	
	  @Mock
	  private BillingQueueRepository pendingBillingQueueRepository;
	  
	  @Mock
	  private RestTemplate restTemplate;
	  
	  @Mock
	  private EmployeeRestTemplate employeeRestTemplate;
	  
	  @Mock
	  private DiscoveryClient discoveryClient;
	  
	  @Mock
	  private PurchaseOrderRepository purchaseOrderRepository;
	  
	  @BeforeTest
	  public void setUpPendingBillingQueueServiceTest()
	  {
		   this.pendingBillingQueueRepository = mock(BillingQueueRepository.class);
	       this.restTemplate = mock(RestTemplate.class);
	       this.discoveryClient = mock(DiscoveryClient.class);
	       this.purchaseOrderRepository=mock(PurchaseOrderRepository.class);
	       pendingBillingQueueServiceImpl=new PendingBillingQueueServiceImpl(pendingBillingQueueRepository,purchaseOrderRepository, restTemplate, discoveryClient);	       
	  }
	  	  
    @Test(dataProviderClass = PendingBillingQueueServiceTestDataProvider.class,
            dataProvider = "getPendingBillingQueueList", description = "")
    public void getPendingBillingQueueList(Pageable pageRequestNull, Pageable pageRequestNonNull,
            Page<BillingQueue> pendingbillingQueueList) {
        UUID engagementId = UUID.randomUUID();
        when(pendingBillingQueueRepository.getPendingBillingQueueList(pageRequestNonNull,
                engagementId, "status")).thenReturn(pendingbillingQueueList);
        pendingBillingQueueServiceImpl.getPendingBillingQueueList(engagementId, "status",
                pageRequestNull);
        pendingBillingQueueServiceImpl.getPendingBillingQueueList(engagementId, "status",
                pageRequestNonNull);
        AssertJUnit.assertNotNull(pendingBillingQueueServiceImpl
                .getPendingBillingQueueList(engagementId, "status", pageRequestNonNull));

    }
	  
	  @Test(dataProviderClass = PendingBillingQueueServiceTestDataProvider.class, dataProvider = "getPendingBillingQueueDetail", description = "")
	  public void getPendingBillingQueueDetail(UUID pendingBillingQueueId,BillingQueue billingQueue)
	  {		
		  when( pendingBillingQueueRepository.findOne(pendingBillingQueueId)).thenReturn(billingQueue);
		  pendingBillingQueueServiceImpl.getPendingBillingQueueDetail(pendingBillingQueueId);
	  }
	  
	  @Test(dataProviderClass = PendingBillingQueueServiceTestDataProvider.class, dataProvider = "activeInactiveBillingDetails", description = "")
	  public void activeInactiveBillingDetails(BillingQueueDTO pendingBillDetailsDTOActive,BillingQueue billingQueue)
	  {
		  when( pendingBillingQueueRepository.findOne(pendingBillDetailsDTOActive.getBillingQueueId())).thenReturn(billingQueue);		  
		  when(pendingBillingQueueRepository.save(billingQueue)).thenReturn(billingQueue);
		  pendingBillingQueueServiceImpl.activeInactiveBillingDetails(pendingBillDetailsDTOActive);
	  }
	  
	  @Test(dataProviderClass = PendingBillingQueueServiceTestDataProvider.class, dataProvider = "activeInactiveBillingDetailsInActive", description = "")
	  public void activeInactiveBillingDetailsInactive(BillingQueueDTO pendingBillDetailsDTOInActive,BillingQueue billingQueueInactive)
	  {		
		  when( pendingBillingQueueRepository.findOne(pendingBillDetailsDTOInActive.getBillingQueueId())).thenReturn(billingQueueInactive);		  
		  when(pendingBillingQueueRepository.save(billingQueueInactive)).thenReturn(billingQueueInactive);
		  pendingBillingQueueServiceImpl.activeInactiveBillingDetails(pendingBillDetailsDTOInActive);
	  }
	  
	 @Test(dataProvider = "submitBillingDetails")
	  public void submitBillingDetails(ParameterizedTypeReference<EmployeeDTO> responseType,
	          ResponseEntity<EmployeeDTO> responseEntity, HttpHeaders httpHeaders, String url,
	          BillingQueueDTO pendingBillDetailsDTO,List<BillingQueue>  pendingbillingQueueList,EmployeeDTO employeeDTO,BillingQueue billingQueue)
	  {  
		  MockHttpServletRequest request = new MockHttpServletRequest();
		  RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		  String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		  request.setAttribute("token", requestedToken);
		 		
		  BillingQueueDTO pendingBillDetailsDTONull=new BillingQueueDTO();
		  pendingBillDetailsDTONull.setStatus("Active");
		  pendingBillDetailsDTONull.setEngagementId("engagementId");
		  pendingBillDetailsDTONull.setEmployeeId(10L);
		  pendingBillDetailsDTONull.setEffectiveStartDate("06/10/2017");
		  pendingBillDetailsDTONull.setEffectiveEndDate("06/13/2017");
		  pendingBillDetailsDTONull.setProfileActiveDate("06/10/2017");
		  pendingBillDetailsDTONull.setProfileEndDate("06/15/2017");
		  pendingBillDetailsDTONull.setPtoAllottedHours(new BigDecimal("10364055.81"));
		  
		  PurchaseOrder po=new PurchaseOrder();
		  List<PurchaseOrder> polist=new ArrayList<PurchaseOrder>();
		  po.setEngagementId(UUID.randomUUID());
		  po.setNotes("notes");;
		  polist.add(po);
		  
		  UUID id=UUID.fromString(pendingBillDetailsDTO.getEngagementId());
		  when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		  when(pendingBillingQueueRepository.findByEngagementIdAndEmployeeIdAndStatusAndIdNot(pendingBillDetailsDTO.getEngagementId(),pendingBillDetailsDTO.getEmployeeId(),"Inactive",UUID.randomUUID())).thenReturn(pendingbillingQueueList);
		  when(pendingBillingQueueRepository.save(billingQueue)).thenReturn(billingQueue);
		  when(purchaseOrderRepository.findByEngagementIdAndByActive(id, ActiveFlag.Y)).thenReturn(polist);
		  when(pendingBillingQueueRepository.findByContractorIdAndEngagementIdAndIdNot(pendingBillDetailsDTO.getContractorId(), pendingBillDetailsDTO.getEngagementId(), UUID.randomUUID())).thenReturn(pendingbillingQueueList);
		  when(pendingBillingQueueRepository.findByContractorIdAndEngagementId(pendingBillDetailsDTO.getContractorId(), pendingBillDetailsDTO.getEngagementId())).thenReturn(pendingbillingQueueList);
		  pendingBillingQueueServiceImpl.submitBillingDetails(pendingBillDetailsDTO);
		 // AssertJUnit.assertNotNull(pendingBillingQueueServiceImpl.submitBillingDetails(pendingBillDetailsDTONull));
		 
	  }
	    
	    @Test(dataProvider = "submitBillingDetails")
	    public void updateBillingDetails(ParameterizedTypeReference<EmployeeDTO> responseType,
		          ResponseEntity<EmployeeDTO> responseEntity, HttpHeaders httpHeaders, String url,
		          BillingQueueDTO pendingBillDetailsDTO,List<BillingQueue>  pendingbillingQueueList,EmployeeDTO employeeDTO,BillingQueue billingQueue)
	    {
	    	
	    	 MockHttpServletRequest request = new MockHttpServletRequest();
	    	 RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
			 String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
			 request.setAttribute("token", requestedToken);
			  
	    	when(pendingBillingQueueRepository.save(billingQueue)).thenReturn(billingQueue);
	    	pendingBillingQueueServiceImpl.updateBillingDetails(pendingBillDetailsDTO);
	    }
	    
	 
	   @Test(dataProvider = "updateTaskBillingDetails")
	    public void updateTaskBillingDetails(BillingQueue billingQueueTest,List<TaskDTO> task,List<Task> listTasks)
	    {
		   	UUID id=UUID.randomUUID();
	    	BillingQueue billingQueue=new BillingQueue();
	    	billingQueue.setId(id);
	    	billingQueue.setAccountManagerId(10L);
	    	billingQueue.setBillToClient("billToClient");
	    	billingQueue.setAccountManagerId(10L);
	    	billingQueue.setEffectiveEndDate("10/10/2017");
	    	
	    	when(pendingBillingQueueRepository.findOne(id)).thenReturn(billingQueue);
	    	when(pendingBillingQueueRepository.save(billingQueue)).thenReturn(billingQueue);
	    	pendingBillingQueueServiceImpl.updateTaskBillingDetails(id, task);
	    }
	 

	 /*  @Test(dataProvider = "submitBillingDetails")
		  public void createBillingDetails(ParameterizedTypeReference<EmployeeDTO> responseType,
		          ResponseEntity<EmployeeDTO> responseEntity, HttpHeaders httpHeaders, String url,
		          BillingQueueDTO pendingBillDetailsDTO,List<BillingQueue>  pendingbillingQueueList,EmployeeDTO employeeDTO,BillingQueue billingQueue)
		  {  
			  MockHttpServletRequest request = new MockHttpServletRequest();
			  RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
			  String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
			  request.setAttribute("token", requestedToken);
			 	
			  when(pendingBillingQueueRepository.findByEngagementIdAndEmployeeIdAndStatusAndIdNot(pendingBillDetailsDTO.getEngagementId(), pendingBillDetailsDTO.getEmployeeId(), "Active", pendingBillDetailsDTO.getBillingQueueId())).thenReturn(pendingbillingQueueList);
		  	  pendingBillingQueueServiceImpl.createBillingDetails(pendingBillDetailsDTO);
			
			
			 
		  }*/
		   
	   
	   @DataProvider(name = "updateTaskBillingDetails")
		public static Iterator<Object[]> updateTaskBillingDetails(){
			 
			Task task=new Task();
			task.setTaskId(UUID.randomUUID());
			task.setContractId(10L);
			task.setEmployeeId(10L);
			List<Task> listTasks=new ArrayList<Task>();
			listTasks.add(task);			
			BillingQueue billingQueueInactive=new BillingQueue();
			billingQueueInactive.setId(UUID.randomUUID());
			billingQueueInactive.setAccountManagerId(10L);
			billingQueueInactive.setBillToClient("billToClient");
			billingQueueInactive.setAccountManagerId(10L);
			billingQueueInactive.setEffectiveEndDate("10/05/2017");			
			TaskDTO taskDTO=new TaskDTO();
			taskDTO.setTaskId(UUID.randomUUID());
			taskDTO.setContractId(10L);
			taskDTO.setEmployeeId(10L);
			taskDTO.setTaskName("taskName");
			List<TaskDTO> listTask=new ArrayList<TaskDTO>();
			listTask.add(taskDTO);
			
			Set<Object[]> testData = new LinkedHashSet<Object[]>();
			testData.add(new Object[] {billingQueueInactive,listTask,listTasks});
			return testData.iterator();
		 }
	  	    
	    @DataProvider(name = "submitBillingDetails")
	    public static Iterator<Object[]> submitBillingDetails() throws ParseException {
	    	
	    
	    	UUID engId=UUID.randomUUID();
	    	String joinDate="06-20-2017";
	    	SimpleDateFormat formatter = new SimpleDateFormat("mm-dd-yyyy");		   
		    Date effectiveendDate=formatter.parse(joinDate);
	    	
	    	BillingQueueDTO pendingBillDetailsDTO=new BillingQueueDTO();
			pendingBillDetailsDTO.setBillingQueueId(UUID.randomUUID());
			pendingBillDetailsDTO.setStatus("Active");
			pendingBillDetailsDTO.setEngagementId(engId.toString());
			pendingBillDetailsDTO.setEmployeeId(10L);
			pendingBillDetailsDTO.setEffectiveStartDate("06/10/2017");
			pendingBillDetailsDTO.setEffectiveEndDate("06/13/2017");
			pendingBillDetailsDTO.setProfileActiveDate("06/10/2017");
			pendingBillDetailsDTO.setProfileEndDate("06/15/2017");
			pendingBillDetailsDTO.setPtoAllottedHours(new BigDecimal("10364055.81"));
			pendingBillDetailsDTO.setContractorId("10");
			pendingBillDetailsDTO.setBillingQueueId(engId);
			pendingBillDetailsDTO.setContractorMailId("contractorMailId");
		
			List<BillingQueue>  pendingbillingQueueList=new ArrayList<BillingQueue>();
			BillingQueue billingQueue=new BillingQueue();
			billingQueue.setId(UUID.randomUUID());
			billingQueue.setBillingProfile("billingProfile");
			billingQueue.setBillToClientId(10L);
			billingQueue.setBillingSpecialistId(10L);	
			billingQueue.setEffectiveStartDate("06/10/2017");
			billingQueue.setEffectiveEndDate("06/08/2017");
			pendingbillingQueueList.add(billingQueue);
			
		    String url = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/10";
	        String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
	     
	        EmployeeDTO employeeDTO=new EmployeeDTO();
	        employeeDTO.setEmployeeId(10L);
	        employeeDTO.setJoiningDate(effectiveendDate);
	        
	        ParameterizedTypeReference<EmployeeDTO> responseType = new ParameterizedTypeReference<EmployeeDTO>() {};
	        ResponseEntity<EmployeeDTO> responseEntity = new ResponseEntity<>(employeeDTO, HttpStatus.OK);
	        HttpHeaders httpHeaders = new HttpHeaders() {
				private static final long serialVersionUID = -3670115299291671935L;
				{
					set(HttpHeaders.AUTHORIZATION,
							new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
				}
			};
	        Set<Object[]> testData = new LinkedHashSet<Object[]>();
	        testData.add(new Object[] {responseType, responseEntity, httpHeaders, url,
	        		pendingBillDetailsDTO,pendingbillingQueueList,employeeDTO,billingQueue});
	        return testData.iterator();
	    }
	    
	    @DataProvider(name = "createBillingDetails")
	    public static Iterator<Object[]> createBillingDetails() throws ParseException {
	    	
	    
	    	UUID engId=UUID.randomUUID();
	    	String joinDate="06-20-2017";
	    	SimpleDateFormat formatter = new SimpleDateFormat("mm-dd-yyyy");		   
		    Date effectiveendDate=formatter.parse(joinDate);
	    	
	    	BillingQueueDTO pendingBillDetailsDTO=new BillingQueueDTO();
			pendingBillDetailsDTO.setBillingQueueId(UUID.randomUUID());
			pendingBillDetailsDTO.setStatus("Active");
			pendingBillDetailsDTO.setEngagementId(engId.toString());
			pendingBillDetailsDTO.setEmployeeId(null);
			pendingBillDetailsDTO.setEffectiveStartDate("06/10/2017");
			pendingBillDetailsDTO.setEffectiveEndDate("06/13/2017");
			pendingBillDetailsDTO.setProfileActiveDate("06/10/2017");
			pendingBillDetailsDTO.setProfileEndDate("06/15/2017");
			pendingBillDetailsDTO.setPtoAllottedHours(new BigDecimal("10364055.81"));
			pendingBillDetailsDTO.setContractorId("10");
			pendingBillDetailsDTO.setBillingQueueId(engId);
			pendingBillDetailsDTO.setContractorMailId("contractorMailId");
		
			List<BillingQueue>  pendingbillingQueueList=new ArrayList<BillingQueue>();
			BillingQueue billingQueue=new BillingQueue();
			billingQueue.setId(UUID.randomUUID());
			billingQueue.setBillingProfile("billingProfile");
			billingQueue.setBillToClientId(10L);
			billingQueue.setBillingSpecialistId(10L);	
			billingQueue.setEffectiveStartDate("06/10/2017");
			billingQueue.setEffectiveEndDate("06/08/2017");
			pendingbillingQueueList.add(billingQueue);
			
		    String url = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/";
	        String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
	     
	        EmployeeDTO employeeDTO=new EmployeeDTO();
	        employeeDTO.setEmployeeId(30L);
	        employeeDTO.setJoiningDate(effectiveendDate);
	        
	        ParameterizedTypeReference<EmployeeDTO> responseType = new ParameterizedTypeReference<EmployeeDTO>() {};
	        ResponseEntity<EmployeeDTO> responseEntity = new ResponseEntity<>(employeeDTO, HttpStatus.OK);
	        HttpHeaders httpHeaders = new HttpHeaders() {
				private static final long serialVersionUID = -3670115299291671935L;
				{
					set(HttpHeaders.AUTHORIZATION,
							new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
				}
			};
	        Set<Object[]> testData = new LinkedHashSet<Object[]>();
	        testData.add(new Object[] {responseType, responseEntity, httpHeaders, url,
	        		pendingBillDetailsDTO,pendingbillingQueueList,employeeDTO,billingQueue});
	        return testData.iterator();
	    }
	  /*  	    
	    @DataProvider(name = "updateBillingDetails")
	    public static Iterator<Object[]> updateBillingDetails() {
	    	BillingQueueDTO pendingBillDetailsDTO=new BillingQueueDTO();
			pendingBillDetailsDTO.setBillingQueueId(UUID.randomUUID());
			pendingBillDetailsDTO.setStatus("Active");
			pendingBillDetailsDTO.setEngagementId("engagementId");
			pendingBillDetailsDTO.setEmployeeId(10L);
			pendingBillDetailsDTO.setEffectiveStartDate("06/10/2017");
			pendingBillDetailsDTO.setEffectiveEndDate("06/13/2017");
			pendingBillDetailsDTO.setProfileActiveDate("06/10/2017");
			pendingBillDetailsDTO.setProfileEndDate("06/15/2017");
			pendingBillDetailsDTO.setPtoAllottedHours(new BigDecimal("10364055.81"));
		
			List<BillingQueue>  pendingbillingQueueList=new ArrayList<BillingQueue>();
			BillingQueue billingQueue=new BillingQueue();
			billingQueue.setId(UUID.randomUUID());
			billingQueue.setBillingProfile("billingProfile");
			billingQueue.setBillToClientId(10L);
			billingQueue.setBillingSpecialistId(10L);	
			billingQueue.setEffectiveStartDate("06/10/2017");
			billingQueue.setEffectiveEndDate("06/08/2017");
			pendingbillingQueueList.add(billingQueue);
			
		    String url = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/10";
	        String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
	     
	        EmployeeDTO employeeDTO=new EmployeeDTO();
	        employeeDTO.setEmployeeId(100L);
	        employeeDTO.setJoiningDate(new Date());
	        ParameterizedTypeReference<EmployeeDTO> responseType = new ParameterizedTypeReference<EmployeeDTO>() {};
	        ResponseEntity<EmployeeDTO> responseEntity = new ResponseEntity<>(employeeDTO, HttpStatus.OK);
	        HttpHeaders httpHeaders = new HttpHeaders() {
				private static final long serialVersionUID = -3670115299291671935L;
				{
					set(HttpHeaders.AUTHORIZATION,
							new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
				}
			};
	        Set<Object[]> testData = new LinkedHashSet<Object[]>();
	        testData.add(new Object[] {responseType, responseEntity, httpHeaders, url,
	        		pendingBillDetailsDTO,pendingbillingQueueList,employeeDTO,billingQueue});
	        return testData.iterator();
	    }*/
	    
	  /*  @DataProvider(name = "createBillingDetailsTest")
	    public static Iterator<Object[]> createBillingDetailsTest() {
	    	BillingQueueDTO pendingBillDetailsDTO=new BillingQueueDTO();
			pendingBillDetailsDTO.setBillingQueueId(UUID.randomUUID());
			pendingBillDetailsDTO.setStatus("Active");
			pendingBillDetailsDTO.setEngagementId("engagementId");
			pendingBillDetailsDTO.setEmployeeId(30L);
			pendingBillDetailsDTO.setEffectiveStartDate("06/10/2017");
			pendingBillDetailsDTO.setEffectiveEndDate("06/13/2017");
			pendingBillDetailsDTO.setProfileActiveDate("06/10/2017");
			pendingBillDetailsDTO.setProfileEndDate("06/15/2017");
			pendingBillDetailsDTO.setPtoAllottedHours(new BigDecimal("10364055.81"));
		
			List<BillingQueue>  pendingbillingQueueList=new ArrayList<BillingQueue>();
			BillingQueue billingQueue=new BillingQueue();
			billingQueue.setId(UUID.randomUUID());
			billingQueue.setBillingProfile("billingProfile");
			billingQueue.setBillToClientId(10L);
			billingQueue.setBillingSpecialistId(10L);	
			billingQueue.setEffectiveStartDate("06/10/2017");
			billingQueue.setEffectiveEndDate("06/08/2017");
			pendingbillingQueueList.add(billingQueue);
			
		    String url = "http://COMMONSERVICEMANAGEMENT/common/employee-profile/30";
	        String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
	     
	        EmployeeDTO employeeDTO=new EmployeeDTO();
	        employeeDTO.setEmployeeId(100L);
	         employeeDTO.setJoiningDate(new Date());
	        ParameterizedTypeReference<EmployeeDTO> responseType = new ParameterizedTypeReference<EmployeeDTO>() {};
	        ResponseEntity<EmployeeDTO> responseEntity = new ResponseEntity<>(employeeDTO, HttpStatus.OK);
	        HttpHeaders httpHeaders = new HttpHeaders() {
				private static final long serialVersionUID = -3670115299291671935L;
				{
					set(HttpHeaders.AUTHORIZATION,
							new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
				}
			};
	        Set<Object[]> testData = new LinkedHashSet<Object[]>();
	        testData.add(new Object[] {responseType, responseEntity, httpHeaders, url,
	        		pendingBillDetailsDTO,pendingbillingQueueList,employeeDTO,billingQueue});
	        return testData.iterator();
	    }*/
	    
	  
	    

}
