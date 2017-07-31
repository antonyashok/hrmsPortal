package com.tm.invoice.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.tm.invoice.mongo.domain.InvoiceAlertDetails;
import com.tm.invoice.mongo.repository.AlertDetailsRepository;
import com.tm.invoice.service.impl.AlertsServiceImpl;

public class AlertsServiceTest {
	
	@InjectMocks
	AlertsServiceImpl alertsServiceImpl;
	
	@Mock
	private AlertDetailsRepository alertdetailsRepository;
	
	@BeforeTest
	public void setUpAlertsServiceTest() throws Exception {
		
		this.alertdetailsRepository=mock(AlertDetailsRepository.class);
		alertsServiceImpl=new AlertsServiceImpl(alertdetailsRepository);
	}
	
	@Test(dataProviderClass = AlertsServiceTestDataProvider.class, dataProvider = "getInvoiceAlerts", description = "")
	public void getInvoiceAlerts(String typeName,Pageable pageRequest,Page<InvoiceAlertDetails> invoiceAlertDetailsPage)
	{
		when(alertdetailsRepository.findByAlertsType(Mockito.anyString(), (Pageable)Mockito.anyObject())).thenReturn(invoiceAlertDetailsPage);
		alertsServiceImpl.getInvoiceAlerts();
	}

}
