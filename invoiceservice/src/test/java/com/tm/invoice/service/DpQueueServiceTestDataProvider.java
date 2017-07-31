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
import org.testng.annotations.DataProvider;

import com.tm.invoice.dto.BillingDetailsDTO;
import com.tm.invoice.dto.DPQueueListDTO;
import com.tm.invoice.dto.DPQueueMinDTO;
import com.tm.invoice.dto.DpQueueDTO;
import com.tm.invoice.mongo.domain.AuditFields;
import com.tm.invoice.mongo.domain.DpQueue;

public class DpQueueServiceTestDataProvider {

	@DataProvider(name = "getDpQueues")
	public static Iterator<Object[]> getDpQueues() throws ParseException {

		DpQueue dpQueue = new DpQueue();
		dpQueue.setId(UUID.randomUUID());
		dpQueue.setBillToClientId(10L);
		dpQueue.setContractorId(10L);
		dpQueue.setLocationId(10L);
		List<DpQueue> listDpQueue = new ArrayList<>();
		listDpQueue.add(dpQueue);
		Page<DpQueue> pageDpQueue = new PageImpl<DpQueue>(listDpQueue);

		Long longBillToClientId = 10L;
		String stringStatus = "status";

		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { longBillToClientId, stringStatus, pageDpQueue });
		return testData.iterator();
	}

	@DataProvider(name = "getDpQueueDetails")
	public static Iterator<Object[]> getDpQueueDetails() throws ParseException {

		DpQueue dpQueue = new DpQueue();
		dpQueue.setId(UUID.randomUUID());
		dpQueue.setLocationId(10L);
		dpQueue.setStatus("status");
		dpQueue.setBillToClientId(10L);
		dpQueue.setContractorId(10L);
		dpQueue.setPoId(UUID.randomUUID());

		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { dpQueue });
		return testData.iterator();
	}

	@DataProvider(name = "saveDirectPlacement")
	public static Iterator<Object[]> saveDirectPlacement() throws ParseException {

		AuditFields auditFields = new AuditFields();
		auditFields.setBy(0L);
		auditFields.setOn(new Date());

		DpQueueDTO dpQueueDTO = new DpQueueDTO();
		dpQueueDTO.setId(UUID.randomUUID());
		dpQueueDTO.setLocationId(10L);
		dpQueueDTO.setStatus("Not Generated");
		dpQueueDTO.setBillToClientId(10L);
		dpQueueDTO.setContractorId(10L);
		dpQueueDTO.setPoId("0");
		dpQueueDTO.setCreated(auditFields);
		dpQueueDTO.setUpdated(auditFields);

		DpQueue dpQueue = new DpQueue();
		dpQueue.setId(UUID.fromString("017b050c-469d-11e7-a919-92ebcb67fe33"));
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { dpQueueDTO, dpQueue });
		return testData.iterator();
	}

	@DataProvider(name = "updateDirectPlacement")
	public static Iterator<Object[]> updateDirectPlacement() throws ParseException {

		AuditFields auditFields = new AuditFields();
		auditFields.setBy(0L);
		auditFields.setOn(new Date());

		DpQueueDTO dpQueueDTO = new DpQueueDTO();
		dpQueueDTO.setId(UUID.randomUUID());
		dpQueueDTO.setLocationId(10L);
		dpQueueDTO.setStatus("Not Generated");
		dpQueueDTO.setBillToClientId(10L);
		dpQueueDTO.setContractorId(10L);
		dpQueueDTO.setPoId("0");
		dpQueueDTO.setCreated(auditFields);
		dpQueueDTO.setUpdated(auditFields);

		DpQueue dpQueue = new DpQueue();
		dpQueue.setId(UUID.fromString("017b050c-469d-11e7-a919-92ebcb67fe33"));
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { dpQueueDTO, dpQueue });
		return testData.iterator();
	}

	@DataProvider(name = "generateInvoice")
	public static Iterator<Object[]> generateInvoice() throws ParseException {

		UUID oneDpQueueIds = UUID.fromString("017b050c-469d-11e7-a919-92ebcb67fe33");
		UUID secondDpQueueIds = UUID.fromString("017b050c-469d-11e7-a919-92ebcb67fe44");
		List<UUID> dpQueueIds = new ArrayList<UUID>();
		dpQueueIds.add(oneDpQueueIds);
		dpQueueIds.add(secondDpQueueIds);

		DpQueue objDpQueue = new DpQueue();
		objDpQueue.setId(UUID.randomUUID());
		objDpQueue.setLocationId(10L);
		objDpQueue.setStatus("status");
		objDpQueue.setBillToClientId(10L);
		objDpQueue.setContractorId(10L);

		List<DpQueue> dpQueues = new ArrayList<DpQueue>();
		dpQueues.add(objDpQueue);

		BillingDetailsDTO billingDetailsDTO = new BillingDetailsDTO();
		billingDetailsDTO.setDpQueueIds(dpQueueIds);

		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { dpQueueIds, dpQueues, billingDetailsDTO });
		return testData.iterator();
	}

	@DataProvider(name = "getNotGeneratedDpQueues")
	public static Iterator<Object[]> getNotGeneratedDpQueues() throws ParseException {

		List<DpQueue> dpQueues = new ArrayList<DpQueue>();
		DpQueue dpQueue = new DpQueue();
		dpQueue.setId(UUID.randomUUID());
		dpQueue.setLocationId(10L);
		dpQueue.setStatus("status");
		dpQueue.setBillToClientId(10L);
		dpQueue.setContractorId(10L);
		dpQueue.setPoId(UUID.randomUUID());
		dpQueues.add(dpQueue);

		DPQueueMinDTO dPQueueMinDTO = new DPQueueMinDTO();
		dPQueueMinDTO.setDpQueueId(UUID.randomUUID());
		dPQueueMinDTO.setEmployeeName(null);

		List<DPQueueMinDTO> listdPQueueMinDTO = new ArrayList<DPQueueMinDTO>();
		listdPQueueMinDTO.add(dPQueueMinDTO);

		DPQueueListDTO dPQueueListDTO = new DPQueueListDTO();
		dPQueueListDTO.setDpQueues(listdPQueueMinDTO);

		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { dpQueues, dPQueueListDTO });
		return testData.iterator();
	}

}
