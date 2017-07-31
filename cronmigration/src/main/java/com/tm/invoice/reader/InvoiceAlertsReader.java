/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.invoiceengine.reader.InvoiceAlertsSetupReader.java
 * Author        : Annamalai L
 * Date Created  : May 3rd, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.invoice.reader;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.tm.common.domain.EmployeeProfile;
import com.tm.common.domain.LookupView;
import com.tm.common.repository.EmployeeRepository;
import com.tm.common.repository.LookupViewRepository;
import com.tm.invoice.domain.InvoiceQueue;
import com.tm.invoice.domain.InvoiceSetup;
import com.tm.invoice.domain.InvoiceSetupBillCycle;
import com.tm.invoice.domain.PreBillView;
import com.tm.invoice.domain.PurchaseOrder;
import com.tm.invoice.domain.PurchaseOrderDetailsView;
import com.tm.invoice.dto.InvoiceAlertsBatchDTO;
import com.tm.invoice.enums.ActiveFlag;
import com.tm.invoice.mongo.domain.InvoiceReturn;
import com.tm.invoice.mongo.domain.ManualInvoice;
import com.tm.invoice.mongo.repository.InvoiceAlertDetailRepository;
import com.tm.invoice.mongo.repository.InvoiceQueueRepository;
import com.tm.invoice.mongo.repository.InvoiceReturnRepository;
import com.tm.invoice.mongo.repository.ManualInvoiceRepository;
import com.tm.invoice.repository.InvoicePreBillRepository;
import com.tm.invoice.repository.InvoiceSetupBillCycleRepository;
import com.tm.invoice.repository.InvoiceSetupRepository;
import com.tm.invoice.repository.PoInvoiceSetupDetailsViewRepository;
import com.tm.invoice.repository.PurchaseOrderDetailsViewRepository;
import com.tm.invoice.repository.PurchaseOrderRepository;
import com.tm.util.InvoiceConstants;

public class InvoiceAlertsReader implements ItemReader<InvoiceAlertsBatchDTO> {

	private static final Logger log = LoggerFactory
			.getLogger(InvoiceAlertsReader.class);

	@StepScope
	@Value("#{stepExecutionContext[from]}")
	public int fromId;

	@StepScope
	@Value("#{stepExecutionContext[to]}")
	public int toId;

	@StepScope
	@Value("#{stepExecutionContext[applicationlivedate]}")
	public Date applicationLiveDate;

	@StepScope
	@Value("#{stepExecutionContext[weekStartDate]}")
	public LocalDate weekStartDate;

	public List<Integer> keys = new ArrayList<>();

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	PurchaseOrderRepository purchaseOrderRepository;

	@Autowired
	PurchaseOrderDetailsViewRepository purchaseOrderDetailsViewRepository;

	@Autowired
	InvoiceSetupRepository invoiceSetupRepository;
	
	@Autowired
	ManualInvoiceRepository manualInvoiceRepository;
	
	@Autowired
	InvoiceReturnRepository invoiceReturnRepository;
	
	@Autowired
	InvoicePreBillRepository invoicePreBillRepository;
	
	@Autowired
	InvoiceQueueRepository invoiceQueueRepository;
	
	@Autowired
	InvoiceSetupBillCycleRepository invoiceSetupBillCycleRepository;
	
	@Autowired
	private PoInvoiceSetupDetailsViewRepository poInvoiceSetupDetailsViewRepository;
	
	@Autowired
	InvoiceAlertDetailRepository invoiceAlertDetailRepository;
	
	@Autowired
	LookupViewRepository lookupViewRepository;

	private static final String INVOICE_ALERTS = "INVOICE_ALERTS";

	@Override
	@StepScope
	public InvoiceAlertsBatchDTO read() throws Exception,
			UnexpectedInputException, ParseException,
			NonTransientResourceException {
		log.info("************** InvoiceAlertsReader **************** ");
		if (keys.contains(fromId)) {
			return null;
		}
		keys.add(fromId);

		InvoiceAlertsBatchDTO invoiceAlertsBatchDTO = new InvoiceAlertsBatchDTO();
		//List<PurchaseOrder> purchaseOrders = purchaseOrderRepository
		//		.getPurchaseOrdersByActiveFlagAndParentPoIdIsNull(ActiveFlag.Y);
		
		//if(CollectionUtils.isNotEmpty(purchaseOrders)) {
			//invoiceAlertsBatchDTO.setPurchaseOrders(purchaseOrders);
			List<UUID> poIds = new ArrayList<>();
			List<UUID> invoiceSetupIds = new ArrayList<>();	
			List<UUID> engagementIds = new ArrayList<>();
			//populateInvoiceSetupIds(purchaseOrders, invoiceSetupIds, poIds);
			
			//List<InvoiceSetup> invoiceSetups = invoiceSetupRepository
			//		.getInvoiceSetupsByIds(invoiceSetupIds);
			//populateInvoiceSetupMap(invoiceSetups, invoiceAlertsBatchDTO);
			
			//invoiceAlertsBatchDTO.setPenddingInvoiceSetUp(invoiceSetupRepository
			//		.findByStatus(InvoiceConstants.PENDING_APPROVAL));
			
			invoiceAlertsBatchDTO.setPenddingManualInvoice(manualInvoiceRepository
					.findByStatus(InvoiceConstants.PENDING_APPROVAL));
			
			invoiceAlertsBatchDTO.setApprovedManualInvoice(populateMannualInvoiceListInvoiceId(manualInvoiceRepository
					.findByStatusNot(InvoiceConstants.PENDING_APPROVAL)));
			
			invoiceAlertsBatchDTO.setPenddingInvoiceReturn(invoiceReturnRepository
					.findByStatus(InvoiceConstants.INVOICE_RETURN));
			
			invoiceAlertsBatchDTO.setApprovedInvoiceReturn(populateInvoiceReturnInvoiceId(invoiceReturnRepository
					.findByStatusNot(InvoiceConstants.INVOICE_RETURN)));
			
			//List<PreBillView> preBillView = invoicePreBillRepository
			//		.getInvoiceSetupsByIds(invoiceSetupIds);
			//populatePreBillMap(preBillView, invoiceAlertsBatchDTO);
			
			//List<InvoiceQueue> invoiceQueue = invoiceQueueRepository
			///		.getInvoiceSetupsByInvoiceSetupId(invoiceSetupIds);
			//populateInvoiceQueueMap(invoiceQueue, invoiceAlertsBatchDTO);
			
			//invoiceAlertsBatchDTO.setPoInvoiceSetupDetailsView(poInvoiceSetupDetailsViewRepository
			//		.findAll());
			
			//List<InvoiceSetupBillCycle> invoiceSetupBillCycles = invoiceSetupBillCycleRepository
			//		.findInvoiceSetupBillCyclesByInvoiceSetupIds(invoiceSetupIds);
			//populateInvoiceSetupBillCycles(invoiceSetupBillCycles, invoiceAlertsBatchDTO);
			
			//populatePurchaseOrderDetailsViewMap(invoiceAlertsBatchDTO);
			populateAlertsLookupView(invoiceAlertsBatchDTO);
			//populateBillingSpecialistEmployeesMap(invoiceAlertsBatchDTO, invoiceSetups);
			
			//invoiceAlertsBatchDTO.getPurchaseOrderDetailsViewMap().forEach( (poId, purchaseOrderDetailsView) -> 
			//	engagementIds.add(purchaseOrderDetailsView.getEngagementId()));
			invoiceAlertsBatchDTO
					.setInvoiceAlertDetails(invoiceAlertDetailRepository.findAll());
							//.getInvoiceAlertDetails(poIds, engagementIds));
			
			return invoiceAlertsBatchDTO;
		//} else {
		//	log.info("There is no active POs");
		//	return null;
		//}
	}

	private void populateInvoiceSetupBillCycles(
			List<InvoiceSetupBillCycle> invoiceSetupBillCycles,
			InvoiceAlertsBatchDTO invoiceAlertsBatchDTO) {
		Map<UUID, InvoiceSetupBillCycle> invoiceSetupBillCycleMap = new HashMap<>();
		invoiceSetupBillCycles.forEach(invoiceSetupBillCycle -> invoiceSetupBillCycleMap.put(
				invoiceSetupBillCycle.getInvoiceSetupId(), invoiceSetupBillCycle));
		invoiceAlertsBatchDTO.setInvoiceSetupBillCycleMap(invoiceSetupBillCycleMap);
	}

	private void populateBillingSpecialistEmployeesMap(
			InvoiceAlertsBatchDTO invoiceAlertsBatchDTO,
			List<InvoiceSetup> invoiceSetups) {
		Map<Long, EmployeeProfile> employeesMap = new HashMap<>();
		invoiceSetups.forEach(invoiceSetup -> {
			//TODO : NEED to discuss with team
		/*	if(null != invoiceSetup.getInvoiceSpecialistEmployeeId()) {
				employeesMap.put(invoiceSetup.getInvoiceSpecialistEmployeeId(),
					employeeRepository.findOne(invoiceSetup
						.getInvoiceSpecialistEmployeeId()));
			}*/
		});
		invoiceAlertsBatchDTO.setEmployeesMap(employeesMap);
	}

	private void populateInvoiceSetupIds(List<PurchaseOrder> purchaseOrders,
			List<UUID> invoiceSetupIds, List<UUID> poIds) {
		purchaseOrders.forEach(purchaseOrder -> {
			if (null != purchaseOrder.getInvoiceSetupId() && !invoiceSetupIds.contains(purchaseOrder.getInvoiceSetupId())) {
				invoiceSetupIds.add(purchaseOrder.getInvoiceSetupId());
			}
			poIds.add(purchaseOrder.getPurchaseOrderId());
		});
	}

	private void populateInvoiceSetupMap(List<InvoiceSetup> invoiceSetups, InvoiceAlertsBatchDTO invoiceAlertsBatchDTO) {
		Map<UUID, InvoiceSetup> invoiceSetupsMap = new HashMap<>();
		invoiceSetups.forEach(invoiceSetup -> invoiceSetupsMap.put(
				invoiceSetup.getInvoiceSetupId(), invoiceSetup));
		invoiceAlertsBatchDTO.setInvoiceSetupMap(invoiceSetupsMap);
	}
	
	private void populatePreBillMap(List<PreBillView> preBillViews, InvoiceAlertsBatchDTO invoiceAlertsBatchDTO) {
		Map<UUID, PreBillView> preBillsMap = new HashMap<>();
		preBillViews.forEach(preBillView -> preBillsMap.put(
				preBillView.getInvoiceSetupId(), preBillView));
		invoiceAlertsBatchDTO.setPreBillViewMap(preBillsMap);
	}
	
	private void populateInvoiceQueueMap(List<InvoiceQueue> invoiceQueue, InvoiceAlertsBatchDTO invoiceAlertsBatchDTO) {
		Map<UUID, List<InvoiceQueue>> invoiceQueueMap = invoiceQueue.stream()
				.collect(Collectors.groupingBy(InvoiceQueue::getInvoiceSetupId));
		Map<UUID, List<InvoiceQueue>> invoiceQueueMapDet = new LinkedHashMap<>();
		invoiceQueueMap.forEach((invoiceSetupId, invoiceQueueList) -> 
			invoiceQueueMapDet.put(invoiceSetupId, invoiceQueueList));
		invoiceAlertsBatchDTO.setInvoiceQueueMap(invoiceQueueMap);
	}
	
	
	
	private void populatePurchaseOrderDetailsViewMap(InvoiceAlertsBatchDTO invoiceAlertsBatchDTO) {
		List<PurchaseOrderDetailsView> purchaseOrderDetailsViews = purchaseOrderDetailsViewRepository
				.findPurchaseOrderDetailsViewsByPoActiveFlagAndPoParentIdIsNull(ActiveFlag.Y);
		Map<UUID, PurchaseOrderDetailsView> purchaseOrderDetailsViewMap = new HashMap<>();
		purchaseOrderDetailsViews
				.forEach(purchaseOrderDetailsView -> purchaseOrderDetailsViewMap
						.put(purchaseOrderDetailsView.getPurchaseOrderId(),
								purchaseOrderDetailsView));
		invoiceAlertsBatchDTO
			.setPurchaseOrderDetailsViewMap(purchaseOrderDetailsViewMap);
	}

	private void populateAlertsLookupView(InvoiceAlertsBatchDTO invoiceBatchDTO) {
		List<LookupView> lookupViews = lookupViewRepository
				.getLookupViewsByEntityName(INVOICE_ALERTS);
		invoiceBatchDTO.setLookupViews(lookupViews);
	}
	
	private List<String> populateMannualInvoiceListInvoiceId(List<ManualInvoice> mannualInvoiceList) {
		List<String> invoiceIds = new ArrayList<>();
		mannualInvoiceList.forEach(invoiceReturn -> {
			invoiceIds.add(invoiceReturn.getInvoiceId().toString());
		});
		return invoiceIds;
	}
	
	private List<String> populateInvoiceReturnInvoiceId(List<InvoiceReturn> invoiceReturnList) {
		List<String> invoiceIds = new ArrayList<>();
		invoiceReturnList.forEach(invoiceReturn -> {
			invoiceIds.add(invoiceReturn.getId().toString());
		});
		return invoiceIds;
	}
}
