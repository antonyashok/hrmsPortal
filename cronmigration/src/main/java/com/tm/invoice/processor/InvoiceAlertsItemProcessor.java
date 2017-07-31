/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.invoiceengine.processor.InvoiceAlertsSetupProcessor.java
 * Author        : Annamalai L
 * Date Created  : May 3rd, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.invoice.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.tm.common.domain.EmployeeProfile;
import com.tm.common.domain.LookupView;
import com.tm.invoice.domain.InvoiceAlertsConfig;
import com.tm.invoice.domain.InvoiceSetup;
import com.tm.invoice.domain.PurchaseOrder;
import com.tm.invoice.domain.PurchaseOrderDetailsView;
import com.tm.invoice.dto.InvoiceAlertDetailsDTO;
import com.tm.invoice.dto.InvoiceAlertsBatchDTO;
import com.tm.invoice.mongo.domain.InvoiceAlertDetails;
import com.tm.invoice.mongo.domain.InvoiceReturn;
import com.tm.invoice.mongo.domain.ManualInvoice;
import com.tm.invoice.repository.BillCycleRepository;
import com.tm.invoice.repository.InvoiceAlertsConfigRepository;
import com.tm.invoice.repository.PurchaseOrderRepository;
import com.tm.util.InvoiceAlertsUtils;

public class InvoiceAlertsItemProcessor implements
		ItemProcessor<InvoiceAlertsBatchDTO, InvoiceAlertsBatchDTO> {

	private static final Logger log = LoggerFactory
			.getLogger(InvoiceAlertsItemProcessor.class);

	public static final String NORMAL_ALERT = "NORMAL_ALERT";
	public static final String RED_ALERT = "RED_ALERT";

	public static final String DISCOUNT_MILESTONE_APPROVAL_NEEDED = "Discount Milestone Approval Needed";
	public static final String INVOICE_CONFIGURATION_APPROVAL_NEEDED = "Invoice Configuration Approval Needed";
	public static final String INVOICE_MILESTONE_REACHED = "Invoice Milestone Reached";
	public static final String IRREGULAR_CYCLE_ENDING = "Irregular Cycle Ending";
	public static final String LOW_PO_FUNDS = "Low PO Funds";
	public static final String LOW_EXPENSE_AMOUNT = "Low Expense Amount";
	public static final String MANUAL_INVOICE_APPROVAL_NEEDED = "Manual Invoice Approval Needed";
	public static final String PREBILL_APPROVAL_NEEDED = "PreBill Approval Needed";
	public static final String PREBILL_EXHAUSTION = "PreBill Exhaustion";
	public static final String RETURN_APPROVAL_NEEDED = "Return Approval Needed";
	public static final String BILL_WATCH = "Bill watch";

	public static final String MILESTONE_ACCRUE = "MILESTONE ACCRUE";
	public static final String MILESTONE_FIXED = "MILESTONE FIXED";
	public static final String IRREGULAR = "IRREGULAR";
	public static final String REGULAR = "REGULAR";
	public static final String PREBILL = "PREBILL";

	@Autowired
	BillCycleRepository billCycleRepository;
	
	@Autowired
	PurchaseOrderRepository purcheaseOrderRepository;

	@Autowired
	InvoiceAlertsConfigRepository invoiceAlertsConfigRepository;

	@Override
	public InvoiceAlertsBatchDTO process(
			InvoiceAlertsBatchDTO invoiceAlertsBatchDTO) throws Exception {
		List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs = new ArrayList<>();
		List<InvoiceAlertDetails> removeInvoiceAlertDetailsDTOs = new ArrayList<>();
		log.info("InvoiceSetupProcess is calling");
		List<LookupView> lookupList = invoiceAlertsBatchDTO.getLookupViews();

		List<InvoiceAlertsConfig> invoiceAlertsConfigs = invoiceAlertsConfigRepository
				.findAll();
		Map<UUID, List<InvoiceAlertsConfig>> invoiceAlertsConfigMap = new HashMap<>();

		if(CollectionUtils.isNotEmpty(invoiceAlertsBatchDTO.getInvoiceAlertDetails())){
			removeManualInvoice(invoiceAlertsBatchDTO.getInvoiceAlertDetails(),removeInvoiceAlertDetailsDTOs,invoiceAlertsBatchDTO.getApprovedManualInvoice());
			removeInvoiceReturn(invoiceAlertsBatchDTO.getInvoiceAlertDetails(),removeInvoiceAlertDetailsDTOs,invoiceAlertsBatchDTO.getApprovedInvoiceReturn());
			//removeLowPOAmount(invoiceAlertsBatchDTO.getInvoiceAlertDetails(),removeInvoiceAlertDetailsDTOs,invoiceAlertsConfigs);
			//removeLowPOExpenseAmount(invoiceAlertsBatchDTO.getInvoiceAlertDetails(),removeInvoiceAlertDetailsDTOs,invoiceAlertsConfigs);
		}
		
		lookupList
				.forEach(lookupView -> {
					List<InvoiceAlertsConfig> invoiceAlertsConfigList = new ArrayList<>();
					invoiceAlertsConfigs.forEach(invoiceAlertsConfig -> {
						if (lookupView.getEntityAttributeMapId().equals(
								invoiceAlertsConfig.getAlertId())) {
							invoiceAlertsConfigList.add(invoiceAlertsConfig);
						}
					});
					invoiceAlertsConfigMap.put(
							lookupView.getEntityAttributeMapId(),
							invoiceAlertsConfigList);
					checkLookupValueAndCallRespectiveFunctions(
							invoiceAlertsBatchDTO, invoiceAlertDetailsDTOs,
							lookupView, invoiceAlertsConfigMap);
				});
		
		// lookupList
		// .forEach(lookupView -> checkLookupValueAndCallRespectiveFunctions(
		// invoiceAlertsBatchDTO, invoiceAlertDetailsDTOs,
		// lookupView));
		invoiceAlertsBatchDTO.setInvoiceAlertDetailsDTO(invoiceAlertDetailsDTOs);
		invoiceAlertsBatchDTO.setRemoveInvoiceAlertDetails(removeInvoiceAlertDetailsDTOs);

		return invoiceAlertsBatchDTO;
	}

	private void checkLookupValueAndCallRespectiveFunctions(
			InvoiceAlertsBatchDTO invoiceAlertsBatchDTO,
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs,
			LookupView lookupView,
			Map<UUID, List<InvoiceAlertsConfig>> invoiceAlertsConfigMap) {

		/* Milestone Type based Alerts */
		/*if (lookupView.getAttributeValue().equalsIgnoreCase(
				INVOICE_MILESTONE_REACHED)) {
			invoiceMilestoneAlerts(invoiceAlertsBatchDTO,
					invoiceAlertDetailsDTOs, lookupView, invoiceAlertsConfigMap);
		}*/

		/* Approval Needed Alerts */
		if (lookupView.getAttributeValue().equalsIgnoreCase(
				INVOICE_CONFIGURATION_APPROVAL_NEEDED)
				|| lookupView.getAttributeValue().equalsIgnoreCase(
						MANUAL_INVOICE_APPROVAL_NEEDED)
				/*|| lookupView.getAttributeValue().equalsIgnoreCase(
						PREBILL_APPROVAL_NEEDED)*/
				|| lookupView.getAttributeValue().equalsIgnoreCase(
						RETURN_APPROVAL_NEEDED)) {
			invoiceApprovalAlerts(invoiceAlertsBatchDTO,
					invoiceAlertDetailsDTOs, lookupView, invoiceAlertsConfigMap);
		}

		/* Irregular Bill Cycle Alerts */
		/*if (lookupView.getAttributeValue().equalsIgnoreCase(
				IRREGULAR_CYCLE_ENDING)) {
			invoiceIrregularBillCycleAlerts(invoiceAlertsBatchDTO,
					invoiceAlertDetailsDTOs, lookupView, invoiceAlertsConfigMap);
		}*/

		/* LOW FUND ALERTS */
		/*if (lookupView.getAttributeValue().equalsIgnoreCase(LOW_PO_FUNDS)
				|| lookupView.getAttributeValue().equalsIgnoreCase(
						LOW_EXPENSE_AMOUNT)) {
			invoiceLowFundAlerts(invoiceAlertsBatchDTO,
					invoiceAlertDetailsDTOs, lookupView, invoiceAlertsConfigMap);
		}*/

		/* Pre-bill Exhaution Alert */
		/*if (lookupView.getAttributeValue().equalsIgnoreCase(PREBILL_EXHAUSTION)) {
			invoicePrebillExhautionAlert(invoiceAlertsBatchDTO,
					invoiceAlertDetailsDTOs, lookupView, invoiceAlertsConfigMap);
		}*/
	}

	/*private void invoiceMilestoneAlerts(
			InvoiceAlertsBatchDTO invoiceAlertsBatchDTO,
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs,
			LookupView lookupView,
			Map<UUID, List<InvoiceAlertsConfig>> invoiceAlertsConfigMap) {
		List<PoInvoiceSetupDetailsView> purchaseOrderList = invoiceAlertsBatchDTO
				.getPoInvoiceSetupDetailsView();
		Map<UUID, PurchaseOrderDetailsView> purchaseOrderDetailsViewMap = invoiceAlertsBatchDTO
				.getPurchaseOrderDetailsViewMap();
		Map<Long, EmployeeProfile> employeeMap = invoiceAlertsBatchDTO
				.getEmployeesMap();
		purchaseOrderList
				.forEach(purchaseOrder -> {
					PurchaseOrderDetailsView purchaseOrderDetailsView = purchaseOrderDetailsViewMap
							.get(purchaseOrder.getPurchaseOrderId());
					if (null != purchaseOrder) {
						EmployeeProfile billingSpecialistEmployee = employeeMap
								.get(purchaseOrder
										.getInvoiceSpecialistEmployeeId());

						if (purchaseOrder.getEngagementEndDate().after(
								new Date())) {
							populateAlertsForMilestoneFixed(
									invoiceAlertDetailsDTOs, lookupView,
									purchaseOrder, purchaseOrderDetailsView,
									billingSpecialistEmployee);

							populateAlertsForMilestoneAccure(
									invoiceAlertDetailsDTOs, lookupView,
									purchaseOrder, purchaseOrderDetailsView,
									billingSpecialistEmployee);
						}
					}
				});
	}

	private void populateAlertsForMilestoneAccure(
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs,
			LookupView lookupView, PoInvoiceSetupDetailsView purchaseOrder,
			PurchaseOrderDetailsView purchaseOrderDetailsView,
			EmployeeProfile billingSpecialistEmployee) {
		InvoiceAlertDetailsDTO invoiceAlertDetailsDTO = null;
		if (purchaseOrder.getInvoiceTypeName().equalsIgnoreCase(
				MILESTONE_ACCRUE)
				&& checkMilestoneAmount(purchaseOrder.getInvoiceSetupId())) {
			invoiceAlertDetailsDTO = populateInvoiceAlertDetailsDTO(
					purchaseOrder, purchaseOrderDetailsView,
					billingSpecialistEmployee, lookupView, NORMAL_ALERT);
			if (Objects.nonNull(invoiceAlertDetailsDTO)) {
				invoiceAlertDetailsDTOs.add(invoiceAlertDetailsDTO);
			}
		}
	}

	private void populateAlertsForMilestoneFixed(
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs,
			LookupView lookupView, PoInvoiceSetupDetailsView purchaseOrder,
			PurchaseOrderDetailsView purchaseOrderDetailsView,
			EmployeeProfile billingSpecialistEmployee) {
		InvoiceAlertDetailsDTO invoiceAlertDetailsDTO = null;
		if (purchaseOrder.getInvoiceTypeName()
				.equalsIgnoreCase(MILESTONE_FIXED)) {
			Date mileStoneFixedMaxDate = getMileStoneFixedMaxDate(purchaseOrder
					.getInvoiceSetupId());
			if (InvoiceAlertsUtils.checkDateEquals(mileStoneFixedMaxDate,
					new Date())) {
				invoiceAlertDetailsDTO = populateInvoiceAlertDetailsDTO(
						purchaseOrder, purchaseOrderDetailsView,
						billingSpecialistEmployee, lookupView, NORMAL_ALERT);
				if (Objects.nonNull(invoiceAlertDetailsDTO)) {
					invoiceAlertDetailsDTOs.add(invoiceAlertDetailsDTO);
				}
			}
		}
	}*/

	private void invoiceApprovalAlerts(
			InvoiceAlertsBatchDTO invoiceAlertsBatchDTO,
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs,
			LookupView lookupView,
			Map<UUID, List<InvoiceAlertsConfig>> invoiceAlertsConfigMap) {
		if (lookupView.getAttributeValue().equalsIgnoreCase(
				MANUAL_INVOICE_APPROVAL_NEEDED)) {
			manualInvoiceAlerts(invoiceAlertsBatchDTO, invoiceAlertDetailsDTOs,
					lookupView, invoiceAlertsConfigMap);
		} /*else if (lookupView.getAttributeValue().equalsIgnoreCase(
				PREBILL_APPROVAL_NEEDED)) {
			invoiceMilestoneAlertsForPreBill(invoiceAlertsBatchDTO,
					invoiceAlertDetailsDTOs, lookupView, invoiceAlertsConfigMap);
		}*/ else if (lookupView.getAttributeValue().equalsIgnoreCase(
				RETURN_APPROVAL_NEEDED)) {
			invoiceReturnAlerts(invoiceAlertsBatchDTO, invoiceAlertDetailsDTOs,
					lookupView, invoiceAlertsConfigMap);
		}
	}

	/*private void invoiceMilestoneAlertsForPreBill(
			InvoiceAlertsBatchDTO invoiceAlertsBatchDTO,
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs,
			LookupView lookupView, Map<UUID, List<InvoiceAlertsConfig>> invoiceAlertsConfigMap) {
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		PurchaseOrderDetailsView purchaseOrderDetailsView = new PurchaseOrderDetailsView();
		List<InvoiceSetup> invoiceSetupList = invoiceAlertsBatchDTO
				.getPenddingInvoiceSetUp();
		Map<Long, EmployeeProfile> employeeMap = invoiceAlertsBatchDTO
				.getEmployeesMap();
		invoiceSetupList.forEach(invoiceSetup -> {
			InvoiceAlertDetailsDTO invoiceAlertDetailsDTO = null;

			if (null != invoiceSetup) {
				EmployeeProfile billingSpecialistEmployee = employeeMap
						.get(invoiceSetup.getInvoiceSpecialistEmployeeId());

				if (InvoiceAlertsUtils.checkDate(
						invoiceSetup.getSubmittedDate(), new Date())) {
					invoiceAlertDetailsDTO = populateInvoiceAlertDetailsDTO(
							purchaseOrder, purchaseOrderDetailsView,
							invoiceSetup, billingSpecialistEmployee,
							lookupView, NORMAL_ALERT);
					if (Objects.nonNull(invoiceAlertDetailsDTO)) {
						invoiceAlertDetailsDTOs.add(invoiceAlertDetailsDTO);
					}
				}
			}
		});
	}*/

	private void manualInvoiceAlerts(
			InvoiceAlertsBatchDTO invoiceAlertsBatchDTO,
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs,
			LookupView lookupView,
			Map<UUID, List<InvoiceAlertsConfig>> invoiceAlertsConfigMap) {
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		PurchaseOrderDetailsView purchaseOrderDetailsView = new PurchaseOrderDetailsView();
		List<ManualInvoice> mannualInvoiceList = invoiceAlertsBatchDTO
				.getPenddingManualInvoice();
		List<InvoiceAlertsConfig> invoiceAlertsConfigs = invoiceAlertsConfigMap
				.get(lookupView.getEntityAttributeMapId());
		Integer dayDiff = Integer.parseInt(invoiceAlertsConfigs.get(0)
				.getAlertColumnValue());
		mannualInvoiceList
				.forEach(manualInvoice -> {
					InvoiceAlertDetailsDTO invoiceAlertDetailsDTO = null;

					if (null != manualInvoice) {
						EmployeeProfile billingSpecialistEmployee = new EmployeeProfile();

						if (InvoiceAlertsUtils.checkDate(
								manualInvoice.getCreatedDate(), new Date(),
								dayDiff)) {
							invoiceAlertDetailsDTO = populateInvoiceAlertDetailsDTO(
									purchaseOrder, purchaseOrderDetailsView,
									manualInvoice, billingSpecialistEmployee,
									lookupView, NORMAL_ALERT);
							if (Objects.nonNull(invoiceAlertDetailsDTO)) {
								invoiceAlertDetailsDTOs
										.add(invoiceAlertDetailsDTO);
							}
						}
					}
				});
	}/*
	 * 
	 * private List<InvoiceAlertsConfig> getInvoiceAlertsConfig(LookupView
	 * lookupView) { return invoiceAlertsConfigRepository
	 * .findInvoiceAlertsConfigsByAlertId(lookupView
	 * .getEntityAttributeMapId()); }
	 */

	private void invoiceReturnAlerts(
			InvoiceAlertsBatchDTO invoiceAlertsBatchDTO,
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs,
			LookupView lookupView, Map<UUID, List<InvoiceAlertsConfig>> invoiceAlertsConfigMap) {
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		PurchaseOrderDetailsView purchaseOrderDetailsView = new PurchaseOrderDetailsView();
		List<InvoiceReturn> invoiceSetupList = invoiceAlertsBatchDTO
				.getPenddingInvoiceReturn();
		Map<Long, EmployeeProfile> employeeMap = invoiceAlertsBatchDTO
				.getEmployeesMap();
		List<InvoiceAlertsConfig> invoiceAlertsConfigs = invoiceAlertsConfigMap
				.get(lookupView.getEntityAttributeMapId());
		invoiceSetupList.forEach(invoiceReturn -> {
			InvoiceAlertDetailsDTO invoiceAlertDetailsDTO = null;

			if (null != invoiceReturn) {
				EmployeeProfile billingSpecialistEmployee = employeeMap
						.get(invoiceReturn.getBillingSpecialistId());

				if (InvoiceAlertsUtils.checkDate(invoiceReturn.getUpdated()
						.getOn(), new Date(), Integer.parseInt(invoiceAlertsConfigs.get(0).getAlertColumnValue()))) {
					invoiceAlertDetailsDTO = populateInvoiceAlertDetailsDTO(
							purchaseOrder, purchaseOrderDetailsView,
							invoiceReturn, billingSpecialistEmployee,
							lookupView, NORMAL_ALERT);
					if (Objects.nonNull(invoiceAlertDetailsDTO)) {
						invoiceAlertDetailsDTOs.add(invoiceAlertDetailsDTO);
					}
				}
			}
		});
	}

	/*private void invoiceIrregularBillCycleAlerts(
			InvoiceAlertsBatchDTO invoiceAlertsBatchDTO,
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs,
			LookupView lookupView,
			Map<UUID, List<InvoiceAlertsConfig>> invoiceAlertsConfigMap) {
		List<PurchaseOrder> purchaseOrderList = invoiceAlertsBatchDTO
				.getPurchaseOrders();
		Map<Long, EmployeeProfile> employeeMap = invoiceAlertsBatchDTO
				.getEmployeesMap();

		purchaseOrderList
				.forEach(purchaseOrder -> {
					PurchaseOrderDetailsView purchaseOrderDetailsView = invoiceAlertsBatchDTO
							.getPurchaseOrderDetailsViewMap().get(
									purchaseOrder.getPurchaseOrderId());
					InvoiceSetup invoiceSetup = invoiceAlertsBatchDTO
							.getInvoiceSetupMap().get(
									purchaseOrder.getInvoiceSetupId());
					if (null != invoiceSetup) {
						populateAlertForIrregularBillCycle(
								invoiceAlertDetailsDTOs, lookupView,
								employeeMap, purchaseOrder,
								purchaseOrderDetailsView, invoiceSetup,
								invoiceAlertsConfigMap);
					}
				});
	}*/

	/*private void populateAlertForIrregularBillCycle(
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs,
			LookupView lookupView, Map<Long, EmployeeProfile> employeeMap,
			PurchaseOrder purchaseOrder,
			PurchaseOrderDetailsView purchaseOrderDetailsView,
			InvoiceSetup invoiceSetup,
			Map<UUID, List<InvoiceAlertsConfig>> invoiceAlertsConfigMap) {
		InvoiceAlertDetailsDTO invoiceAlertDetailsDTO;
		EmployeeProfile billingSpecialistEmployee = employeeMap
				.get(invoiceSetup.getInvoiceSpecialistEmployeeId());

		List<InvoiceAlertsConfig> invoiceAlertsConfigs = invoiceAlertsConfigMap
				.get(lookupView.getEntityAttributeMapId());// getInvoiceAlertsConfig(lookupView);
		Integer dayDiffForNormalAlert = 0;
		Integer dayDiffForRedAlert = 0;

		if (CollectionUtils.isNotEmpty(invoiceAlertsConfigs)) {
			dayDiffForNormalAlert = Integer.parseInt(invoiceAlertsConfigs
					.get(0).getAlertColumnValue());
			dayDiffForRedAlert = Integer.parseInt(invoiceAlertsConfigs.get(1)
					.getAlertColumnValue());
		}

		Date irregularMaxDate = getIrregularMaxDate(invoiceSetup
				.getInvoiceSetupId());
		if (invoiceSetup.getInvoiceTypeName().equalsIgnoreCase(IRREGULAR)) {
			if (InvoiceAlertsUtils.checkDate(irregularMaxDate, new Date(),
					dayDiffForNormalAlert)) {
				invoiceAlertDetailsDTO = populateInvoiceAlertDetailsDTO(
						purchaseOrder, purchaseOrderDetailsView, invoiceSetup,
						billingSpecialistEmployee, lookupView, NORMAL_ALERT);
				if (Objects.nonNull(invoiceAlertDetailsDTO)) {
					invoiceAlertDetailsDTOs.add(invoiceAlertDetailsDTO);
				}
			} else if (InvoiceAlertsUtils.checkDate(irregularMaxDate,
					new Date(), dayDiffForRedAlert)) {
				invoiceAlertDetailsDTO = populateInvoiceAlertDetailsDTO(
						purchaseOrder, purchaseOrderDetailsView, invoiceSetup,
						billingSpecialistEmployee, lookupView, RED_ALERT);
				if (Objects.nonNull(invoiceAlertDetailsDTO)) {
					invoiceAlertDetailsDTOs.add(invoiceAlertDetailsDTO);
				}
			}
		}
	}*/

	private void invoiceLowFundAlerts(
			InvoiceAlertsBatchDTO invoiceAlertsBatchDTO,
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs,
			LookupView lookupView,
			Map<UUID, List<InvoiceAlertsConfig>> invoiceAlertsConfigMap) {
		List<PurchaseOrder> purchaseOrderList = invoiceAlertsBatchDTO
				.getPurchaseOrders();
		Map<Long, EmployeeProfile> employeeMap = invoiceAlertsBatchDTO
				.getEmployeesMap();
		purchaseOrderList
				.forEach(purchaseOrder -> {
					Double amount = purchaseOrder.getRevenueAmount();
					Double expenseAmount = purchaseOrder.getExpenseAmount();
					Double balanceAmount = purchaseOrder
							.getBalanceRevenueAmount();
					Double balanceExpenseAmount = purchaseOrder
							.getBalanceExpenseAmount();
					PurchaseOrderDetailsView purchaseOrderDetailsView = invoiceAlertsBatchDTO
							.getPurchaseOrderDetailsViewMap().get(
									purchaseOrder.getPurchaseOrderId());
					InvoiceSetup invoiceSetup = invoiceAlertsBatchDTO
							.getInvoiceSetupMap().get(
									purchaseOrder.getInvoiceSetupId());
					InvoiceAlertDetailsDTO invoiceAlertDetailsDTO;
					if (null != invoiceSetup) {
						EmployeeProfile billingSpecialistEmployee = null;//employeeMap.get(invoiceSetup.getInvoiceSpecialistEmployeeId());
						if (billingSpecialistEmployee != null && amount != null
								&& lookupView.getAttributeValue()
										.equalsIgnoreCase(LOW_PO_FUNDS)) {
							invoiceAlertDetailsDTO = invoiceLowPOFundAlerts(
									purchaseOrder, purchaseOrderDetailsView,
									invoiceSetup, billingSpecialistEmployee,
									lookupView, amount, balanceAmount,
									invoiceAlertsConfigMap);
							if (Objects.nonNull(invoiceAlertDetailsDTO)) {
								invoiceAlertDetailsDTOs.add(invoiceAlertDetailsDTO);
							}
						}
						if (expenseAmount != null && billingSpecialistEmployee != null
								&& lookupView.getAttributeValue()
										.equalsIgnoreCase(LOW_EXPENSE_AMOUNT)) {
							invoiceAlertDetailsDTO = invoiceLowExpenseFundAlerts(
											purchaseOrder,
											purchaseOrderDetailsView,
											invoiceSetup,
											billingSpecialistEmployee,
											lookupView, expenseAmount,
											balanceExpenseAmount,
											invoiceAlertsConfigMap);
							if (Objects.nonNull(invoiceAlertDetailsDTO)) {
								invoiceAlertDetailsDTOs.add(invoiceAlertDetailsDTO);
							}
						}
					}
				});
	}

	private InvoiceAlertDetailsDTO invoiceLowPOFundAlerts(
			PurchaseOrder purchaseOrder,
			PurchaseOrderDetailsView purchaseOrderDetailsView,
			InvoiceSetup invoiceSetup,
			EmployeeProfile billingSpecialistEmployee, LookupView lookupView,
			Double amount, Double balanceAmount,
			Map<UUID, List<InvoiceAlertsConfig>> invoiceAlertsConfigMap) {

		InvoiceAlertDetailsDTO invoiceAlertDetailsDTO = null;
		List<InvoiceAlertsConfig> invoiceAlertsConfigs = invoiceAlertsConfigMap
				.get(lookupView.getEntityAttributeMapId());// getInvoiceAlertsConfig(lookupView);

		Float normalAlertPercentage = 10f;
		Float redAlertPercentage = 5f;
		for (InvoiceAlertsConfig invoiceAlertsConfig : invoiceAlertsConfigs) {
			if (invoiceAlertsConfig.getAlertIndicator().equalsIgnoreCase(
					NORMAL_ALERT)) {
				normalAlertPercentage = Float.parseFloat(invoiceAlertsConfig
						.getAlertColumnValue());
			} else if (invoiceAlertsConfig.getAlertIndicator()
					.equalsIgnoreCase(RED_ALERT)) {
				redAlertPercentage = Float.parseFloat(invoiceAlertsConfig
						.getAlertColumnValue());
			}
		}

		Double balanceCheckTen = InvoiceAlertsUtils.calculateBalanceAmount(
				amount, normalAlertPercentage);
		Double balanceCheckFive = InvoiceAlertsUtils.calculateBalanceAmount(
				amount, redAlertPercentage);

		if (balanceCheckTen.compareTo(balanceAmount) > 0) {
			invoiceAlertDetailsDTO = populateInvoiceAlertDetailsDTO(
					purchaseOrder, purchaseOrderDetailsView, invoiceSetup,
					billingSpecialistEmployee, lookupView, NORMAL_ALERT);
		}else if (balanceCheckFive.compareTo(balanceAmount) > 0) {
			invoiceAlertDetailsDTO = populateInvoiceAlertDetailsDTO(
					purchaseOrder, purchaseOrderDetailsView, invoiceSetup,
					billingSpecialistEmployee, lookupView, RED_ALERT);
		}
		return invoiceAlertDetailsDTO;
	}

	private InvoiceAlertDetailsDTO invoiceLowExpenseFundAlerts(
			PurchaseOrder purchaseOrder,
			PurchaseOrderDetailsView purchaseOrderDetailsView,
			InvoiceSetup invoiceSetup,
			EmployeeProfile billingSpecialistEmployee, LookupView lookupView,
			Double expenseAmount, Double balanceExpenseAmount,
			Map<UUID, List<InvoiceAlertsConfig>> invoiceAlertsConfigMap) {

		InvoiceAlertDetailsDTO invoiceAlertDetailsDTO = null;
		List<InvoiceAlertsConfig> invoiceAlertsConfigs = invoiceAlertsConfigMap
				.get(lookupView.getEntityAttributeMapId());// getInvoiceAlertsConfig(lookupView);

		Float normalAlertPercentage = 10f;
		Float redAlertPercentage = 5f;
		for (InvoiceAlertsConfig invoiceAlertsConfig : invoiceAlertsConfigs) {
			if (invoiceAlertsConfig.getAlertIndicator().equalsIgnoreCase(
					NORMAL_ALERT)) {
				normalAlertPercentage = Float.parseFloat(invoiceAlertsConfig
						.getAlertColumnValue());
			} else if (invoiceAlertsConfig.getAlertIndicator()
					.equalsIgnoreCase(RED_ALERT)) {
				redAlertPercentage = Float.parseFloat(invoiceAlertsConfig
						.getAlertColumnValue());
			}
		}

		Double balanceCheckTen = InvoiceAlertsUtils.calculateBalanceAmount(
				expenseAmount, normalAlertPercentage);
		Double balanceCheckFive = InvoiceAlertsUtils.calculateBalanceAmount(
				expenseAmount, redAlertPercentage);
		if (balanceCheckTen.compareTo(balanceExpenseAmount) > 0) {
			invoiceAlertDetailsDTO = populateInvoiceAlertDetailsDTO(
					purchaseOrder, purchaseOrderDetailsView, invoiceSetup,
					billingSpecialistEmployee, lookupView, NORMAL_ALERT);
		}else if (balanceCheckFive.compareTo(balanceExpenseAmount) > 0) {
			invoiceAlertDetailsDTO = populateInvoiceAlertDetailsDTO(
					purchaseOrder, purchaseOrderDetailsView, invoiceSetup,
					billingSpecialistEmployee, lookupView, RED_ALERT);
		}
		return invoiceAlertDetailsDTO;
	}

	/*private void invoicePrebillExhautionAlert(
			InvoiceAlertsBatchDTO invoiceAlertsBatchDTO,
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs,
			LookupView lookupView,
			Map<UUID, List<InvoiceAlertsConfig>> invoiceAlertsConfigMap) {
		List<PoInvoiceSetupDetailsView> purchaseOrderList = invoiceAlertsBatchDTO
				.getPoInvoiceSetupDetailsView();
		Map<UUID, PurchaseOrderDetailsView> purchaseOrderDetailsViewMap = invoiceAlertsBatchDTO
				.getPurchaseOrderDetailsViewMap();
		Map<UUID, PreBillView> preBillViewMap = invoiceAlertsBatchDTO
				.getPreBillViewMap();
		Map<UUID, List<InvoiceQueue>> invoiceQueueMap = invoiceAlertsBatchDTO
				.getInvoiceQueueMap();
		Map<Long, EmployeeProfile> employeeMap = invoiceAlertsBatchDTO
				.getEmployeesMap();
		purchaseOrderList
				.forEach(purchaseOrder -> {
					PurchaseOrderDetailsView purchaseOrderDetailsView = purchaseOrderDetailsViewMap
							.get(purchaseOrder.getPurchaseOrderId());
					PreBillView preBillView = preBillViewMap.get(purchaseOrder
							.getInvoiceSetupId());
					EmployeeProfile billingSpecialistEmployee = employeeMap
							.get(purchaseOrder.getInvoiceSpecialistEmployeeId());
					if (purchaseOrder.getInvoiceTypeName().equalsIgnoreCase(
							PREBILL)) {
						populateAlertForPrebillExhaution(
								invoiceAlertDetailsDTOs, lookupView,
								invoiceQueueMap, purchaseOrder,
								purchaseOrderDetailsView, preBillView,
								billingSpecialistEmployee);
					}
				});
	}*/

	/*private void populateAlertForPrebillExhaution(
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTOs,
			LookupView lookupView,
			Map<UUID, List<InvoiceQueue>> invoiceQueueMap,
			PoInvoiceSetupDetailsView purchaseOrder,
			PurchaseOrderDetailsView purchaseOrderDetailsView,
			PreBillView preBillView, EmployeeProfile billingSpecialistEmployee) {
		InvoiceAlertDetailsDTO invoiceAlertDetailsDTO;
		List<InvoiceQueue> invoiceQueueList = invoiceQueueMap.get(purchaseOrder
				.getInvoiceSetupId());
		if (!invoiceQueueList.isEmpty()) {
			Double totalInvoiceAmount = 0.00;
			for (InvoiceQueue invoiceQueue : invoiceQueueList) {
				totalInvoiceAmount = totalInvoiceAmount
						+ invoiceQueue.getAmount();
			}
			BigDecimal totalAmountTenPercent = (preBillView.getAmount()
					.multiply(BigDecimal.valueOf(10))).divide(BigDecimal
					.valueOf(100));
			BigDecimal remainingAmount = preBillView.getAmount().subtract(
					BigDecimal.valueOf(totalInvoiceAmount));
			if (totalAmountTenPercent.compareTo(remainingAmount) < 0) {
				invoiceAlertDetailsDTO = populateInvoiceAlertDetailsDTO(
						purchaseOrder, purchaseOrderDetailsView,
						billingSpecialistEmployee, lookupView, NORMAL_ALERT);
				if (Objects.nonNull(invoiceAlertDetailsDTO)) {
					invoiceAlertDetailsDTOs.add(invoiceAlertDetailsDTO);
				}
			}
		}
	}*/

	private InvoiceAlertDetailsDTO populateInvoiceAlertDetailsDTO(
			PurchaseOrder purchaseOrder,
			PurchaseOrderDetailsView purchaseOrderDetailsView,
			InvoiceSetup invoiceSetup,
			EmployeeProfile billingSpecialistEmployee, LookupView lookupView,
			String alertsFor) {

		InvoiceAlertDetailsDTO invoiceAlertDetailsDTO = new InvoiceAlertDetailsDTO();
		/* Populating the lookupView values in invoiceAlertDetailsDTO */
		invoiceAlertDetailsDTO.setEntityId(lookupView.getEntityId());
		invoiceAlertDetailsDTO.setEntityName(lookupView.getEntityName());
		invoiceAlertDetailsDTO.setEntityAttributeMapId(lookupView
				.getEntityAttributeMapId());
		invoiceAlertDetailsDTO.setEntityAttributeMapValue(lookupView
				.getEntityAttributeMapValue());
		invoiceAlertDetailsDTO.setAttributeId(lookupView.getAttributeId());
		invoiceAlertDetailsDTO.setAttributeName(lookupView.getAttributeName());
		invoiceAlertDetailsDTO
				.setAttributeValue(lookupView.getAttributeValue());

		/* Populating invoiceAlerts meta-data */
		if (null != purchaseOrderDetailsView) {
			invoiceAlertDetailsDTO.setEngagementId(purchaseOrderDetailsView
					.getEngagementId());
			invoiceAlertDetailsDTO.setEngagementName(purchaseOrderDetailsView
					.getEngagementName());
		}
		invoiceAlertDetailsDTO.setPurchaseOrderId(purchaseOrder
				.getPurchaseOrderId());
		invoiceAlertDetailsDTO.setPurchaseOrderNumber(purchaseOrder
				.getPoNumber());
		invoiceAlertDetailsDTO.setCustomerId(purchaseOrder.getCustomerId());
		invoiceAlertDetailsDTO.setIndicator(alertsFor);

		/* Alerts related data */
		invoiceAlertDetailsDTO.setAlertDate(new Date());
		invoiceAlertDetailsDTO.setBillToClient(purchaseOrderDetailsView.getCustomerName());

		if (null != billingSpecialistEmployee) {
			invoiceAlertDetailsDTO
					.setBillingSpecialist(billingSpecialistEmployee
							.getFullName());
		}
		return invoiceAlertDetailsDTO;
	}

	private InvoiceAlertDetailsDTO populateInvoiceAlertDetailsDTO(
			PurchaseOrder purchaseOrder,
			PurchaseOrderDetailsView purchaseOrderDetailsView,
			ManualInvoice manualInvoice,
			EmployeeProfile billingSpecialistEmployee, LookupView lookupView,
			String alertsFor) {

		InvoiceAlertDetailsDTO invoiceAlertDetailsDTO = new InvoiceAlertDetailsDTO();
		/* Populating the lookupView values in invoiceAlertDetailsDTO */
		invoiceAlertDetailsDTO.setEntityId(lookupView.getEntityId());
		invoiceAlertDetailsDTO.setEntityName(lookupView.getEntityName());
		invoiceAlertDetailsDTO.setEntityAttributeMapId(lookupView
				.getEntityAttributeMapId());
		invoiceAlertDetailsDTO.setEntityAttributeMapValue(lookupView
				.getEntityAttributeMapValue());
		invoiceAlertDetailsDTO.setAttributeId(lookupView.getAttributeId());
		invoiceAlertDetailsDTO.setAttributeName(lookupView.getAttributeName());
		invoiceAlertDetailsDTO
				.setAttributeValue(lookupView.getAttributeValue());

		/* Populating invoiceAlerts meta-data */
		if (null != purchaseOrderDetailsView) {
			invoiceAlertDetailsDTO.setEngagementId(purchaseOrderDetailsView
					.getEngagementId());
			invoiceAlertDetailsDTO.setEngagementName(purchaseOrderDetailsView
					.getEngagementName());
		}

		invoiceAlertDetailsDTO.setPurchaseOrderId(purchaseOrder
				.getPurchaseOrderId());
		invoiceAlertDetailsDTO.setPurchaseOrderNumber(manualInvoice
				.getPoNumber());
		invoiceAlertDetailsDTO.setCustomerId(purchaseOrder.getCustomerId());
		invoiceAlertDetailsDTO.setInvoiceSetupId(purchaseOrder
				.getInvoiceSetupId());
		invoiceAlertDetailsDTO.setIndicator(alertsFor);

		/* Alerts related data */
		invoiceAlertDetailsDTO.setAlertDate(new Date());
		invoiceAlertDetailsDTO.setBillToClient(manualInvoice.getBillToClient());
		invoiceAlertDetailsDTO.setInvoiceId(manualInvoice.getInvoiceId());
		if (null != billingSpecialistEmployee) {
			invoiceAlertDetailsDTO
					.setBillingSpecialist(billingSpecialistEmployee
							.getFullName());
		}
		return invoiceAlertDetailsDTO;
	}

	private InvoiceAlertDetailsDTO populateInvoiceAlertDetailsDTO(
			PurchaseOrder purchaseOrder,
			PurchaseOrderDetailsView purchaseOrderDetailsView,
			InvoiceReturn invoiceReturn,
			EmployeeProfile billingSpecialistEmployee, LookupView lookupView,
			String alertsFor) {

		InvoiceAlertDetailsDTO invoiceAlertDetailsDTO = new InvoiceAlertDetailsDTO();
		/* Populating the lookupView values in invoiceAlertDetailsDTO */
		invoiceAlertDetailsDTO.setEntityId(lookupView.getEntityId());
		invoiceAlertDetailsDTO.setEntityName(lookupView.getEntityName());
		invoiceAlertDetailsDTO.setEntityAttributeMapId(lookupView
				.getEntityAttributeMapId());
		invoiceAlertDetailsDTO.setEntityAttributeMapValue(lookupView
				.getEntityAttributeMapValue());
		invoiceAlertDetailsDTO.setAttributeId(lookupView.getAttributeId());
		invoiceAlertDetailsDTO.setAttributeName(lookupView.getAttributeName());
		invoiceAlertDetailsDTO
				.setAttributeValue(lookupView.getAttributeValue());

		/* Populating invoiceAlerts meta-data */
		if (null != purchaseOrderDetailsView) {
			invoiceAlertDetailsDTO.setEngagementId(purchaseOrderDetailsView
					.getEngagementId());
			invoiceAlertDetailsDTO.setEngagementName(purchaseOrderDetailsView
					.getEngagementName());
		}

		invoiceAlertDetailsDTO.setPurchaseOrderId(purchaseOrder
				.getPurchaseOrderId());
		invoiceAlertDetailsDTO.setPurchaseOrderNumber(purchaseOrder
				.getPoNumber());
		invoiceAlertDetailsDTO.setCustomerId(purchaseOrder.getCustomerId());
		invoiceAlertDetailsDTO.setInvoiceSetupId(purchaseOrder
				.getInvoiceSetupId());
		invoiceAlertDetailsDTO.setIndicator(alertsFor);

		/* Alerts related data */
		invoiceAlertDetailsDTO.setAlertDate(new Date());
		invoiceAlertDetailsDTO.setBillToClient(invoiceReturn
				.getBillToClientName());
		invoiceAlertDetailsDTO.setInvoiceId(invoiceReturn.getId());

		if (null != billingSpecialistEmployee) {
			invoiceAlertDetailsDTO
					.setBillingSpecialist(billingSpecialistEmployee
							.getFullName());
		}
		return invoiceAlertDetailsDTO;
	}

	/*
	 * private InvoiceAlertDetailsDTO populateInvoiceAlertDetailsDTO(
	 * PoInvoiceSetupDetailsView purchaseOrder, PurchaseOrderDetailsView
	 * purchaseOrderDetailsView, InvoiceSetup invoiceSetup, EmployeeProfile
	 * billingSpecialistEmployee, LookupView lookupView, String alertsFor) {
	 * 
	 * InvoiceAlertDetailsDTO invoiceAlertDetailsDTO = new
	 * InvoiceAlertDetailsDTO(); Populating the lookupView values in
	 * invoiceAlertDetailsDTO
	 * invoiceAlertDetailsDTO.setEntityId(lookupView.getEntityId());
	 * invoiceAlertDetailsDTO.setEntityName(lookupView.getEntityName());
	 * invoiceAlertDetailsDTO.setEntityAttributeMapId(lookupView
	 * .getEntityAttributeMapId());
	 * invoiceAlertDetailsDTO.setEntityAttributeMapValue(lookupView
	 * .getEntityAttributeMapValue());
	 * invoiceAlertDetailsDTO.setAttributeId(lookupView.getAttributeId());
	 * invoiceAlertDetailsDTO.setAttributeName(lookupView.getAttributeName());
	 * invoiceAlertDetailsDTO
	 * .setAttributeValue(lookupView.getAttributeValue());
	 * 
	 * Populating invoiceAlerts meta-data if(null != purchaseOrderDetailsView) {
	 * invoiceAlertDetailsDTO.setEngagementId(purchaseOrderDetailsView
	 * .getEngagementId());
	 * invoiceAlertDetailsDTO.setEngagementName(purchaseOrderDetailsView
	 * .getEngagementName()); }
	 * 
	 * invoiceAlertDetailsDTO.setPurchaseOrderId(purchaseOrder
	 * .getPurchaseOrderId());
	 * invoiceAlertDetailsDTO.setPurchaseOrderNumber(purchaseOrder
	 * .getPoNumber());
	 * invoiceAlertDetailsDTO.setCustomerId(purchaseOrder.getCustomerId());
	 * invoiceAlertDetailsDTO
	 * .setInvoiceSetupId(purchaseOrder.getInvoiceSetupId());
	 * invoiceAlertDetailsDTO.setIndicator(alertsFor);
	 * 
	 * Alerts related data invoiceAlertDetailsDTO.setAlertDate(new Date());
	 * invoiceAlertDetailsDTO.setBillToClient(invoiceSetup
	 * .getBillToOrganizationName());
	 * 
	 * if(null != billingSpecialistEmployee) {
	 * invoiceAlertDetailsDTO.setBillingSpecialist
	 * (billingSpecialistEmployee.getFullName()); } return
	 * invoiceAlertDetailsDTO; }
	 */

	/*private InvoiceAlertDetailsDTO populateInvoiceAlertDetailsDTO(
			PoInvoiceSetupDetailsView purchaseOrder,
			PurchaseOrderDetailsView purchaseOrderDetailsView,
			EmployeeProfile billingSpecialistEmployee, LookupView lookupView,
			String alertsFor) {

		InvoiceAlertDetailsDTO invoiceAlertDetailsDTO = new InvoiceAlertDetailsDTO();
		 Populating the lookupView values in invoiceAlertDetailsDTO 
		invoiceAlertDetailsDTO.setEntityId(lookupView.getEntityId());
		invoiceAlertDetailsDTO.setEntityName(lookupView.getEntityName());
		invoiceAlertDetailsDTO.setEntityAttributeMapId(lookupView
				.getEntityAttributeMapId());
		invoiceAlertDetailsDTO.setEntityAttributeMapValue(lookupView
				.getEntityAttributeMapValue());
		invoiceAlertDetailsDTO.setAttributeId(lookupView.getAttributeId());
		invoiceAlertDetailsDTO.setAttributeName(lookupView.getAttributeName());
		invoiceAlertDetailsDTO
				.setAttributeValue(lookupView.getAttributeValue());

		 Populating invoiceAlerts meta-data 
		if (null != purchaseOrderDetailsView) {
			invoiceAlertDetailsDTO.setEngagementId(purchaseOrderDetailsView
					.getEngagementId());
			invoiceAlertDetailsDTO.setEngagementName(purchaseOrderDetailsView
					.getEngagementName());
		}

		invoiceAlertDetailsDTO.setPurchaseOrderId(purchaseOrder
				.getPurchaseOrderId());
		invoiceAlertDetailsDTO.setPurchaseOrderNumber(purchaseOrder
				.getPoNumber());
		invoiceAlertDetailsDTO.setCustomerId(purchaseOrder.getCustomerId());
		invoiceAlertDetailsDTO.setIndicator(alertsFor);
		invoiceAlertDetailsDTO.setInvoiceSetupId(purchaseOrder
				.getInvoiceSetupId());

		 Alerts related data 
		invoiceAlertDetailsDTO.setAlertDate(new Date());
		invoiceAlertDetailsDTO.setBillToClient(null);

		if (null != billingSpecialistEmployee) {
			invoiceAlertDetailsDTO
					.setBillingSpecialist(billingSpecialistEmployee
							.getFullName());
		}
		return invoiceAlertDetailsDTO;
	}

	private Date getMileStoneFixedMaxDate(UUID invoiceSetupId) {
		return billCycleRepository
				.findMaxMilestoneDateByInvoiceSetupId(invoiceSetupId);
	}

	private Date getIrregularMaxDate(UUID invoiceSetupId) {
		return billCycleRepository
				.findMaxMatureDateByInvoiceSetupId(invoiceSetupId);
	}

	private Boolean checkMilestoneAmount(UUID invoiceSetupId) {
		List<BillCycle> billCycleList = billCycleRepository
				.checkMilestoneAmount(invoiceSetupId, AccuringFlag.Y);
		return (CollectionUtils.isEmpty(billCycleList)) ? true : false;
	}*/
    
	private void removeManualInvoice(List<InvoiceAlertDetails> invoiceAlertDetails,
			List<InvoiceAlertDetails> removeInvoiceAlertDetailsDTOs, List<String> mannualInvoiceList) {
		for(InvoiceAlertDetails invoiceAlertDetail:invoiceAlertDetails){
			if(mannualInvoiceList.contains(invoiceAlertDetail.getInvoiceId())){
				removeInvoiceAlertDetailsDTOs.add(invoiceAlertDetail);
			}
		}
	}
	
	private void removeInvoiceReturn(List<InvoiceAlertDetails> invoiceAlertDetails,
			List<InvoiceAlertDetails> removeInvoiceAlertDetailsDTOs, List<String> invoiceReturn) {
		for(InvoiceAlertDetails invoiceAlertDetail:invoiceAlertDetails){
			if(invoiceReturn.contains(invoiceAlertDetail.getInvoiceId())){
				removeInvoiceAlertDetailsDTOs.add(invoiceAlertDetail);
			}
		}
	}
	
	private void removeLowPOAmount(List<InvoiceAlertDetails> invoiceAlertDetails,
			List<InvoiceAlertDetails> removeInvoiceAlertDetailsDTOs, List<InvoiceAlertsConfig> invoiceAlertsConfigs) {
		for (InvoiceAlertDetails invoiceAlertDetail : invoiceAlertDetails) {
			Float normalAlertPercentage = 10f;
			Float redAlertPercentage = 5f;
			PurchaseOrder purchaseOrder = purcheaseOrderRepository
					.findByPoNumber(invoiceAlertDetail.getPurchaseOrderNumber());
			/*for (InvoiceAlertsConfig invoiceAlertsConfig : invoiceAlertsConfigs) {
				if (invoiceAlertsConfig.getAlertIndicator().equalsIgnoreCase(NORMAL_ALERT)) {
					normalAlertPercentage = Float.parseFloat(invoiceAlertsConfig.getAlertColumnValue());
				} else if (invoiceAlertsConfig.getAlertIndicator().equalsIgnoreCase(RED_ALERT)) {
					redAlertPercentage = Float.parseFloat(invoiceAlertsConfig.getAlertColumnValue());
				}
			}*/

			Double balanceCheckTen = InvoiceAlertsUtils.calculateBalanceAmount(purchaseOrder.getRevenueAmount(),
					normalAlertPercentage);
			Double balanceCheckFive = InvoiceAlertsUtils.calculateBalanceAmount(purchaseOrder.getRevenueAmount(),
					redAlertPercentage);

			if ((balanceCheckTen.compareTo(purchaseOrder.getBalanceRevenueAmount()) < 0) && invoiceAlertDetail.getAlertsType().equals(LOW_PO_FUNDS)) {
				removeInvoiceAlertDetailsDTOs.add(invoiceAlertDetail);
			} else if (balanceCheckFive.compareTo(purchaseOrder.getBalanceRevenueAmount()) < 0 && invoiceAlertDetail.getAlertsType().equals(LOW_PO_FUNDS)) {
				removeInvoiceAlertDetailsDTOs.add(invoiceAlertDetail);
			}
		}
	}
	
	private void removeLowPOExpenseAmount(List<InvoiceAlertDetails> invoiceAlertDetails,
			List<InvoiceAlertDetails> removeInvoiceAlertDetailsDTOs, List<InvoiceAlertsConfig> invoiceAlertsConfigs) {
		for (InvoiceAlertDetails invoiceAlertDetail : invoiceAlertDetails) {
			Float normalAlertPercentage = 10f;
			Float redAlertPercentage = 5f;
			PurchaseOrder purchaseOrder = purcheaseOrderRepository
					.findByPoNumber(invoiceAlertDetail.getPurchaseOrderNumber());
			/*for (InvoiceAlertsConfig invoiceAlertsConfig : invoiceAlertsConfigs) {
				if (invoiceAlertsConfig.getAlertIndicator().equalsIgnoreCase(NORMAL_ALERT)) {
					normalAlertPercentage = Float.parseFloat(invoiceAlertsConfig.getAlertColumnValue());
				} else if (invoiceAlertsConfig.getAlertIndicator().equalsIgnoreCase(RED_ALERT)) {
					redAlertPercentage = Float.parseFloat(invoiceAlertsConfig.getAlertColumnValue());
				}
			}*/

			Double balanceCheckTen = InvoiceAlertsUtils.calculateBalanceAmount(purchaseOrder.getExpenseAmount(),
					normalAlertPercentage);
			Double balanceCheckFive = InvoiceAlertsUtils.calculateBalanceAmount(purchaseOrder.getExpenseAmount(),
					redAlertPercentage);

			if (balanceCheckTen.compareTo(purchaseOrder.getBalanceExpenseAmount()) < 0 && invoiceAlertDetail.getAlertsType().equals(LOW_EXPENSE_AMOUNT)) {
				removeInvoiceAlertDetailsDTOs.add(invoiceAlertDetail);
			} else if (balanceCheckFive.compareTo(purchaseOrder.getBalanceExpenseAmount()) < 0 && invoiceAlertDetail.getAlertsType().equals(LOW_EXPENSE_AMOUNT)) {
				removeInvoiceAlertDetailsDTOs.add(invoiceAlertDetail);
			}
		}
	}

}

