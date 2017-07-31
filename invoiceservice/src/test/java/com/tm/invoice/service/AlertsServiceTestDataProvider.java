package com.tm.invoice.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.testng.annotations.DataProvider;
import com.tm.invoice.mongo.domain.InvoiceAlertDetails;

public class AlertsServiceTestDataProvider {
	
	@DataProvider(name = "getInvoiceAlerts")
	public static Iterator<Object[]> getActivityLog() throws ParseException {

		List<InvoiceAlertDetails> invoiceAlertsDetailsList = new ArrayList<>();
		InvoiceAlertDetails invoiceAlertDetails = new InvoiceAlertDetails();
		invoiceAlertDetails.setAlertDate(new Date());
		invoiceAlertDetails.setAlertsType("Low Expense Amount");
		invoiceAlertDetails.setAlertTypeId(UUID.randomUUID());
		invoiceAlertsDetailsList.add(invoiceAlertDetails);
		Page<InvoiceAlertDetails> invoiceAlertDetailsPage = new PageImpl<InvoiceAlertDetails>(invoiceAlertsDetailsList);
		String alertName = "Low PO Funds";
		Pageable pageRequest = new PageRequest(0, 100, Sort.Direction.ASC, "alertDate");		
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { alertName,pageRequest,invoiceAlertDetailsPage});
		return testData.iterator();
	}

}
