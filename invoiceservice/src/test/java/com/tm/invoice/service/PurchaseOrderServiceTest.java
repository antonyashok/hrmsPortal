package com.tm.invoice.service;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.ibm.icu.text.SimpleDateFormat;
import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.exception.RecordNotFoundException;
import com.tm.invoice.domain.PoContractorsView;
import com.tm.invoice.domain.PurchaseOrder;
import com.tm.invoice.domain.PurchaseOrder.Potype;
import com.tm.invoice.dto.EmployeeProfileDTO;
import com.tm.invoice.dto.InvoiceAttachmentsDTO;
import com.tm.invoice.dto.PurchaseOrderDTO;
import com.tm.invoice.engagement.dto.EngagementDTO;
import com.tm.invoice.enums.ActiveFlag;
import com.tm.invoice.exception.InvoiceEngagementRequestException;
import com.tm.invoice.exception.PoNotExistException;
import com.tm.invoice.mongo.domain.InvoiceAttachments;
import com.tm.invoice.repository.PoContractorsViewRepository;
import com.tm.invoice.repository.PurchaseOrderRepository;
import com.tm.invoice.service.impl.PurchaseOrderServiceImpl;

public class PurchaseOrderServiceTest {

	@InjectMocks
	PurchaseOrderServiceImpl purchaseOrderServiceImpl;

	@Mock
	PurchaseOrderRepository purchaseOrderRepository;
	@Mock
	InvoiceAttachmentService invoiceAttachmentsService;
	@Mock
	DiscoveryClient discoveryClient;
	@Mock
	RestTemplate restTemplate;
	@Mock
	PoContractorsViewRepository poContractorsViewRepository;
	public static UUID id = UUID.randomUUID();

	@BeforeTest
	public void setUp() {

		this.purchaseOrderRepository = Mockito.mock(PurchaseOrderRepository.class);
		this.invoiceAttachmentsService = Mockito.mock(InvoiceAttachmentService.class);
		this.discoveryClient = Mockito.mock(DiscoveryClient.class);
		this.restTemplate = Mockito.mock(RestTemplate.class);
		this.poContractorsViewRepository = Mockito.mock(PoContractorsViewRepository.class);
		purchaseOrderServiceImpl = new PurchaseOrderServiceImpl(purchaseOrderRepository, restTemplate, discoveryClient,
				invoiceAttachmentsService, poContractorsViewRepository);

	}
	
	@Test(dataProvider = "customerProfileRestTemplateDataProvider", expectedExceptions = { InvoiceEngagementRequestException.class })
	public void createPO(ParameterizedTypeReference<EngagementDTO> responseType,
            ResponseEntity<EngagementDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException{
		
		PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO();
		purchaseOrderDTO.setPurchaseOrderId(id);
		purchaseOrderDTO.setEngagementId(id);
		purchaseOrderDTO.setStartDate("01/05/2017");
		purchaseOrderDTO.setEndDate("01/05/2017");
		purchaseOrderDTO.setLastUpdatedOn("01/05/2017");
		purchaseOrderDTO.setPoNumber("ponumber");
		purchaseOrderDTO.setPurchaseOrderType(Potype.REVENUE);
		purchaseOrderDTO.setPurchaseOrderAmount(new BigDecimal(100));
		
		PurchaseOrderDTO purchaseOrderDTOExpense = new PurchaseOrderDTO();
		purchaseOrderDTOExpense.setPurchaseOrderId(id);
		purchaseOrderDTOExpense.setEngagementId(id);
		purchaseOrderDTOExpense.setStartDate("01/05/2017");
		purchaseOrderDTOExpense.setEndDate("01/05/2017");
		purchaseOrderDTOExpense.setLastUpdatedOn("01/05/2017");
		purchaseOrderDTOExpense.setPoNumber("ponumber");
		purchaseOrderDTOExpense.setPurchaseOrderType(Potype.EXPENSE);
		purchaseOrderDTOExpense.setPurchaseOrderAmount(new BigDecimal(100));
		
		InvoiceAttachmentsDTO invoiceAttachments = new InvoiceAttachmentsDTO();
		List<InvoiceAttachmentsDTO> InvoiceAttachmentsDTO =  Arrays.asList(invoiceAttachments);
		purchaseOrderDTO.setPurcheaseOrderAttachements(InvoiceAttachmentsDTO);
		
		String DateStr = "01/04/2017";
		Date POEndDate = new SimpleDateFormat("MM/dd/yyyy").parse(DateStr);
		
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setEndDate(POEndDate);
		purchaseOrder.setPurchaseOrderId(id);
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		when(purchaseOrderRepository.findByPoNumber("ponumber",id)).thenReturn(null);
		when(purchaseOrderRepository.findOne(id)).thenReturn(purchaseOrder);
		when(purchaseOrderRepository.save((PurchaseOrder)Mockito.anyObject())).thenReturn(purchaseOrder);
		
		purchaseOrderServiceImpl.createPO(purchaseOrderDTO);
		purchaseOrderServiceImpl.createPO(purchaseOrderDTOExpense);
		
	}
	
	
	@DataProvider(name = "customerProfileRestTemplateDataProvider")
    public static Iterator<Object[]> customerProfileRestTemplate() {
//    	UUID id = UUID.randomUUID();
        String url = "http://ENGAGEMENTMANAGEMENT/engagements/engagementEdit/"+ id ;
        String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
        
        EngagementDTO engagementDTO = new EngagementDTO();
        engagementDTO.setEngagementId(id);
        engagementDTO.setEngmtStartDate("01/05/2017");
        engagementDTO.setRevenuePurchaseOrderId(id);
        engagementDTO.setEngmtEndDate("01/05/2017");
        engagementDTO.setTotalRevenueAmount(new BigDecimal(100));
        engagementDTO.setBalanceRevenueAmount(new BigDecimal(100));
        engagementDTO.setInitialExpenseAmount(new BigDecimal(100));
        engagementDTO.setInitialRevenueAmount(new BigDecimal(100));
        
        
        ParameterizedTypeReference<EngagementDTO> responseType = new ParameterizedTypeReference<EngagementDTO>() {};
        ResponseEntity<EngagementDTO> responseEntity = new ResponseEntity<>(engagementDTO, HttpStatus.OK);
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
    
    
//    @DataProvider(name = "updateEngagementDetailsRestTemplateDataProvider")
//    public static Iterator<Object[]> updateEngagementDetails() {
////    	UUID id = UUID.randomUUID();
//        String url = "http://ENGAGEMENTMANAGEMENT/engagements/updateEngagementDetails?engagementId=4d43f200-c373-4b6d-bd20-1f77f48227b2&type=Revenue&poId=4d43f200-c373-4b6d-bd20-1f77f48227b2&poNumber=ponumber&initialAmount=null&totalAmount=300&balanceAmount=300&engmtDate=01/05/2017" ;
//        String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
//        
//        int status = 200;
//        
//        
//        ParameterizedTypeReference<Integer> responseType = new ParameterizedTypeReference<Integer>() {};
//        ResponseEntity<Integer> responseEntity = new ResponseEntity<>(status, HttpStatus.OK);
//        HttpHeaders httpHeaders = new HttpHeaders() {
//			private static final long serialVersionUID = -3670115299291671935L;
//			{
//				set(HttpHeaders.AUTHORIZATION,
//						new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
//			}
//		};
//        Set<Object[]> testData = new LinkedHashSet<Object[]>();
//        testData.add(new Object[] {responseType, responseEntity, httpHeaders, url});
//        return testData.iterator();
//    }
    

    
    @Test
    public void getAllPODetails(){
    	PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO();
		purchaseOrderDTO.setPurchaseOrderId(id);
		purchaseOrderDTO.setEngagementId(id);
		purchaseOrderDTO.setStartDate("01/05/2017");
		purchaseOrderDTO.setEndDate("01/05/2017");
		purchaseOrderDTO.setLastUpdatedOn("01/05/2017");
		purchaseOrderDTO.setPoNumber("ponumber");
		purchaseOrderDTO.setPurchaseOrderType(Potype.REVENUE);
		purchaseOrderDTO.setPurchaseOrderAmount(new BigDecimal(100));
		
		List<PurchaseOrderDTO> purchaseOrderDetailsDTOList = Arrays.asList(purchaseOrderDTO);
		
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		List<PurchaseOrder> purchaseOrders = Arrays.asList(purchaseOrder);
		
		when(purchaseOrderRepository.getPODetailsByCriteria(id,"search",ActiveFlag.Y, new PageRequest(5, 5),2 )).thenReturn(purchaseOrders);
				
		purchaseOrderServiceImpl.getAllPODetails(id,"search",InvoiceConstants.N_STR, new PageRequest(5, 5));
    	
    }
    
    @Test
    public void getAllContractorDetailsByEngagementId(){
    	
    	PoContractorsView poContractorsView = new PoContractorsView();
    	poContractorsView.setEngagementId(id);
    	Page<PoContractorsView> poContractorsViewPage = new PageImpl<PoContractorsView>(Arrays.asList(poContractorsView));
//    	Page<Timesheet> timesheetList = new PageImpl<Timesheet>(Arrays.asList(timesheet));
    	
    	when(poContractorsViewRepository.findByEngagementId((UUID)anyObject(),(Pageable)anyObject())).thenReturn(poContractorsViewPage);
    	purchaseOrderServiceImpl.getAllContractorDetailsByEngagementId(id,new PageRequest(5, 5));
    }
    
    @Test
    public void getAllContractorDetailsByContractorName(){
    	
    	PoContractorsView poContractorsView = new PoContractorsView();
    	poContractorsView.setEngagementId(id);
    	Page<PoContractorsView> poContractorsViewPage = new PageImpl<PoContractorsView>(Arrays.asList(poContractorsView));
    	
    	when(poContractorsViewRepository.findByEngagementId((UUID)anyObject(),(Pageable)anyObject())).thenReturn(poContractorsViewPage);
    	purchaseOrderServiceImpl.getAllContractorDetailsByContractorName(id,"",new PageRequest(5, 5));
    }
    
    @Test(expectedExceptions = { PoNotExistException.class })
    public void getPurchaseOrder(){
    	
    	PurchaseOrder PurchaseOrder = Mockito.mock(PurchaseOrder.class);
    	when(purchaseOrderRepository.findOne((UUID)anyObject())).thenReturn(PurchaseOrder);
    	purchaseOrderServiceImpl.getPurchaseOrder(id);	
    	
    	when(purchaseOrderRepository.findOne((UUID)anyObject())).thenReturn(null);
    	purchaseOrderServiceImpl.getPurchaseOrder(id);	
    }
    
    
  /*  @Test(dataProvider = "customerProfileRestTemplateDataProvider")
    public void updatePO(ParameterizedTypeReference<EngagementDTO> responseType,
            ResponseEntity<EngagementDTO> responseEntity, HttpHeaders httpHeaders, String url) throws ParseException{
    	
    	PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO();
		purchaseOrderDTO.setPurchaseOrderId(id);
		purchaseOrderDTO.setEngagementId(id);
		purchaseOrderDTO.setStartDate("01/05/2017");
		purchaseOrderDTO.setEndDate("01/05/2017");
		purchaseOrderDTO.setLastUpdatedOn("01/05/2017");
		purchaseOrderDTO.setPoNumber("ponumber");
		purchaseOrderDTO.setPurchaseOrderType(Potype.REVENUE);
		purchaseOrderDTO.setPurchaseOrderAmount(new BigDecimal(100));
		
		InvoiceAttachmentsDTO invoiceAttachments = new InvoiceAttachmentsDTO();
		List<InvoiceAttachmentsDTO> InvoiceAttachmentsDTO =  Arrays.asList(invoiceAttachments);
		purchaseOrderDTO.setPurcheaseOrderAttachements(InvoiceAttachmentsDTO);
		
		String DateStr = "01/04/2017";
		Date POEndDate = new SimpleDateFormat("MM/dd/yyyy").parse(DateStr);
		
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setEndDate(POEndDate);
		purchaseOrder.setPurchaseOrderId(id);
		purchaseOrder.setPurchaseOrderAmount(new BigDecimal(100));
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
		when(restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseType)).thenReturn(responseEntity);
		
		when(purchaseOrderRepository.findByPurchaseOrderId(id)).thenReturn(purchaseOrder);
		when(purchaseOrderRepository.save((PurchaseOrder)Mockito.anyObject())).thenReturn(purchaseOrder);
		
		 String updateurl = "http://ENGAGEMENTMANAGEMENT/engagements/updateEngagementDetails?engagementId="+id+"&type=Revenue&poId="+id+"&poNumber=ponumber&initialAmount=100&totalAmount=200&balanceAmount=200&engmtDate=01/05/2017" ;
	     int status = 200;
	     ParameterizedTypeReference<Integer> updateresponseType = new ParameterizedTypeReference<Integer>() {};
	     ResponseEntity<Integer> updateresponseEntity = new ResponseEntity<>(status, HttpStatus.OK);
	     
	     
//	     MockHttpServletRequest updaterequest = new MockHttpServletRequest();
//			RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(updaterequest));
//			String requestedToken1 = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
//			request.setAttribute("token", requestedToken1);
			when(restTemplate.exchange(updateurl, HttpMethod.PUT, new HttpEntity<>(httpHeaders), updateresponseType)).thenReturn(updateresponseEntity);
	     

		
    	purchaseOrderServiceImpl.updatePO(purchaseOrderDTO);
    }*/
}
