package com.tm.invoice.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.tm.employee.dto.EmployeeDTO;
import com.tm.invoice.domain.Task;
import com.tm.invoice.dto.BillingQueueDTO;
import com.tm.invoice.dto.EmployeeProfileDTO;
import com.tm.invoice.dto.TaskDTO;
import com.tm.invoice.mongo.domain.BillingQueue;

public class PendingBillingQueueServiceTestDataProvider {

	@DataProvider(name = "getPendingBillingQueueList")
	public static Iterator<Object[]> getPendingBillingQueueList() throws ParseException {
		
		Pageable pageRequestNull = new PageRequest(0, 100, null);
		//Pageable pageRequestNull = new PageRequest(0, 100, null, "lastUpdatedOn");
		Pageable pageRequestNonNull = new PageRequest(0, 100, Sort.Direction.ASC, "lastUpdatedOn");
		
		BillingQueue billingQueue=new BillingQueue();
		billingQueue.setId(UUID.randomUUID());
		billingQueue.setBillingSpecialistId(10L);
		billingQueue.setBillToClient("billToClient");
		billingQueue.setBillToClientId(10L);
		List<BillingQueue>billingQueueList=new ArrayList<BillingQueue>();
		billingQueueList.add(billingQueue);
		Page<BillingQueue> pendingbillingQueueList=new PageImpl<BillingQueue>(billingQueueList);
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {pageRequestNull,pageRequestNonNull,pendingbillingQueueList});
		return testData.iterator();
	}
	
	@DataProvider(name = "getPendingBillingQueueDetail")
	public static Iterator<Object[]> getPendingBillingQueueDetail() throws ParseException {
		
		UUID pendingBillingQueueId=UUID.randomUUID();	
		BillingQueue billingQueue=new BillingQueue();
		billingQueue.setId(UUID.randomUUID());
		billingQueue.setAccountManagerId(10L);
		billingQueue.setBillToClient("billToClient");
		billingQueue.setAccountManagerId(10L);
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {pendingBillingQueueId,billingQueue});
		return testData.iterator();
	}
	
	
	@DataProvider(name = "activeInactiveBillingDetails")
	public static Iterator<Object[]> activeInactiveBillingDetails() throws ParseException {
		
			
		BillingQueue billingQueue=new BillingQueue();
		billingQueue.setId(UUID.randomUUID());
		billingQueue.setAccountManagerId(10L);
		billingQueue.setBillToClient("billToClient");
		billingQueue.setAccountManagerId(10L);
		
		BillingQueueDTO pendingBillDetailsDTOActive=new BillingQueueDTO();
		pendingBillDetailsDTOActive.setBillingQueueId(UUID.randomUUID());
		pendingBillDetailsDTOActive.setStatus("Active");
	
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {pendingBillDetailsDTOActive,billingQueue});
		return testData.iterator();
	}
	
	
	@DataProvider(name = "activeInactiveBillingDetailsInActive")
	public static Iterator<Object[]> activeInactiveBillingDetailsInActive() throws ParseException {
		
			
		BillingQueue billingQueueInactive=new BillingQueue();
		billingQueueInactive.setId(UUID.randomUUID());
		billingQueueInactive.setAccountManagerId(10L);
		billingQueueInactive.setBillToClient("billToClient");
		billingQueueInactive.setAccountManagerId(10L);
		billingQueueInactive.setEffectiveEndDate("10/05/2017");
		BillingQueueDTO pendingBillDetailsDTOInActive=new BillingQueueDTO();
		pendingBillDetailsDTOInActive.setBillingQueueId(UUID.randomUUID());
		pendingBillDetailsDTOInActive.setStatus("Inactive");
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {pendingBillDetailsDTOInActive,billingQueueInactive});
		return testData.iterator();
	}
	
	
	@DataProvider(name = "submitBillingDetails")
	public static Iterator<Object[]> submitBillingDetails() throws ParseException {
		
		BillingQueueDTO pendingBillDetailsDTO=new BillingQueueDTO();
		pendingBillDetailsDTO.setBillingQueueId(UUID.randomUUID());
		pendingBillDetailsDTO.setStatus("Active");
		pendingBillDetailsDTO.setEngagementId("engagementId");
		pendingBillDetailsDTO.setEmployeeId(10L);
		pendingBillDetailsDTO.setEffectiveStartDate("06/10/2017");
		pendingBillDetailsDTO.setEffectiveEndDate("06/13/2017");
		pendingBillDetailsDTO.setProfileActiveDate("06/10/2017");
		pendingBillDetailsDTO.setProfileEndDate("06/15/2017");
	
		List<BillingQueue>  pendingbillingQueueList=new ArrayList<BillingQueue>();
		BillingQueue billingQueue=new BillingQueue();
		billingQueue.setId(UUID.randomUUID());
		billingQueue.setBillingProfile("billingProfile");
		billingQueue.setBillToClientId(10L);
		billingQueue.setBillingSpecialistId(10L);	
		billingQueue.setEffectiveStartDate("06/10/2017");
		billingQueue.setEffectiveEndDate("06/08/2017");
		pendingbillingQueueList.add(billingQueue);
		
		EmployeeDTO employeeDTO=new EmployeeDTO();
		employeeDTO.setEmployeeId(10L);
		employeeDTO.setJoiningDate(new Date());
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {pendingBillDetailsDTO,pendingbillingQueueList,employeeDTO});
		return testData.iterator();
	}

	@DataProvider(name = "updateTaskBillingDetailsTest")
	public static Iterator<Object[]> updateTaskBillingDetailsTest() throws ParseException{
		 
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
		
		//billingQueueInactive.setSubTasksDetails(listTasks);
		
		TaskDTO taskDTO=new TaskDTO();
		taskDTO.setTaskId(UUID.randomUUID());
		taskDTO.setContractId(10L);
		taskDTO.setEmployeeId(10L);
		taskDTO.setTaskName("taskName");
		List<TaskDTO> listTask=new ArrayList<TaskDTO>();
		listTask.add(taskDTO);
		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {billingQueueInactive,listTask});
		return testData.iterator();
	 }
	
	
	
	@DataProvider(name = "employeeRestTemplateDataProviderTest")
    public static Iterator<Object[]> employeeRestTemplateTest() {
    	
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
