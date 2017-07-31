package com.tm.invoice.mongo.repository.impl.test;

import org.testng.annotations.Test;

import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.testng.annotations.BeforeMethod;
import com.tm.invoice.mongo.domain.InvoiceAlertDetails;
import com.tm.invoice.mongo.repository.impl.InvoiceAlertDetailRepositoryImpl;

public class InvoiceAlertDetailRepositoryImplTest {

	@InjectMocks
	private InvoiceAlertDetailRepositoryImpl invoiceAlertDetailRepositoryImpl;

	@Mock
	private MongoTemplate mongoTemplate;

	@BeforeMethod
	public void setUpInvoiceAlertDetailRepositoryImplTest() throws Exception {

		mongoTemplate = Mockito.mock(MongoTemplate.class);
		invoiceAlertDetailRepositoryImpl = new InvoiceAlertDetailRepositoryImpl(mongoTemplate);
	}

	@Test
	public void getInvoiceAlertDetails() {
		String PO_ID = "poId";
		String ENGMT_ID = "engmtId";
		List<InvoiceAlertDetails> getInvoiceAlertDetails = new ArrayList<InvoiceAlertDetails>();
		InvoiceAlertDetails invoiceAlertDetails = new InvoiceAlertDetails();
		invoiceAlertDetails.setAlertDate(new Date());
		invoiceAlertDetails.setEngmtId(UUID.randomUUID());
		getInvoiceAlertDetails.add(invoiceAlertDetails);

		List<UUID> poIds = new ArrayList<UUID>();
		poIds.add(UUID.randomUUID());
		poIds.add(UUID.randomUUID());
		poIds.add(UUID.randomUUID());

		List<UUID> engagementIds = new ArrayList<UUID>();
		engagementIds.add(UUID.randomUUID());
		engagementIds.add(UUID.randomUUID());
		engagementIds.add(UUID.randomUUID());

		Query query = new Query();
		query = query.addCriteria(Criteria.where(PO_ID).in(poIds).and(ENGMT_ID).in(engagementIds));

		when(mongoTemplate.find(query, InvoiceAlertDetails.class)).thenReturn(getInvoiceAlertDetails);
		invoiceAlertDetailRepositoryImpl.getInvoiceAlertDetails(poIds, engagementIds);
	}

}
