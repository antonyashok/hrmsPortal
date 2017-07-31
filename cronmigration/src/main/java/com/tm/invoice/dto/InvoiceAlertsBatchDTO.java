package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tm.common.domain.Employee;
import com.tm.common.domain.EmployeeProfile;
import com.tm.common.domain.LookupView;
import com.tm.invoice.domain.BillCycle;
import com.tm.invoice.domain.InvoiceSetup;
import com.tm.invoice.domain.InvoiceSetupBillCycle;
import com.tm.invoice.domain.PoInvoiceSetupDetailsView;
import com.tm.invoice.domain.PreBillView;
import com.tm.invoice.domain.PurchaseOrder;
import com.tm.invoice.domain.PurchaseOrderDetailsView;
import com.tm.invoice.domain.InvoiceQueue;
import com.tm.invoice.mongo.domain.InvoiceAlertDetails;
import com.tm.invoice.mongo.domain.InvoiceReturn;
import com.tm.invoice.mongo.domain.ManualInvoice;

public class InvoiceAlertsBatchDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 599975814808568578L;

	private List<LookupView> lookupViews;
	private List<PurchaseOrder> purchaseOrders;
	private Map<UUID, PurchaseOrderDetailsView> purchaseOrderDetailsViewMap;
	private Map<UUID, InvoiceSetup> invoiceSetupMap;
	private Employee billingSpecialist;
	private Map<Long, EmployeeProfile> employeeProfileMap;
	private Map<UUID, InvoiceSetupBillCycle> invoiceSetupBillCycleMap;
	private Map<UUID, BillCycle> billCycles;
	private List<InvoiceSetup> penddingInvoiceSetUp;
	private List<ManualInvoice> penddingManualInvoice;
	private List<String> approvedInvoiceReturn;
	private List<String> approvedManualInvoice;
	private List<InvoiceReturn> penddingInvoiceReturn;
	private List<InvoiceAlertDetails> invoiceAlertDetails;
	private List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTO;
	private List<InvoiceAlertDetails> removeInvoiceAlertDetails;
	private List<PoInvoiceSetupDetailsView> poInvoiceSetupDetailsView; 
	private Map<UUID,PreBillView> preBillViewMap;
	private Map<UUID,List<InvoiceQueue>> invoiceQueueMap;

	public List<LookupView> getLookupViews() {
		return lookupViews;
	}

	public void setLookupViews(List<LookupView> lookupViews) {
		this.lookupViews = lookupViews;
	}

	public List<PurchaseOrder> getPurchaseOrders() {
		return purchaseOrders;
	}

	public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) {
		this.purchaseOrders = purchaseOrders;
	}
	
	public Map<UUID, PurchaseOrderDetailsView> getPurchaseOrderDetailsViewMap() {
		return purchaseOrderDetailsViewMap;
	}

	public void setPurchaseOrderDetailsViewMap(
			Map<UUID, PurchaseOrderDetailsView> purchaseOrderDetailsViewMap) {
		this.purchaseOrderDetailsViewMap = purchaseOrderDetailsViewMap;
	}

	public Map<UUID, InvoiceSetup> getInvoiceSetupMap() {
		return invoiceSetupMap;
	}

	public void setInvoiceSetupMap(Map<UUID, InvoiceSetup> invoiceSetupMap) {
		this.invoiceSetupMap = invoiceSetupMap;
	}
	
	public Employee getBillingSpecialist() {
		return billingSpecialist;
	}

	public void setBillingSpecialist(Employee billingSpecialist) {
		this.billingSpecialist = billingSpecialist;
	}
	
	public Map<Long, EmployeeProfile> getEmployeesMap() {
		return employeeProfileMap;
	}

	public void setEmployeesMap(Map<Long, EmployeeProfile> employeesMap) {
		this.employeeProfileMap = employeesMap;
	}
	
	public Map<Long, EmployeeProfile> getEmployeeProfileMap() {
		return employeeProfileMap;
	}

	public void setEmployeeProfileMap(Map<Long, EmployeeProfile> employeeProfileMap) {
		this.employeeProfileMap = employeeProfileMap;
	}

	public Map<UUID, InvoiceSetupBillCycle> getInvoiceSetupBillCycleMap() {
		return invoiceSetupBillCycleMap;
	}

	public void setInvoiceSetupBillCycleMap(
			Map<UUID, InvoiceSetupBillCycle> invoiceSetupBillCycleMap) {
		this.invoiceSetupBillCycleMap = invoiceSetupBillCycleMap;
	}

	public Map<UUID, BillCycle> getBillCycles() {
		return billCycles;
	}

	public void setBillCycles(Map<UUID, BillCycle> billCycles) {
		this.billCycles = billCycles;
	}

	public List<InvoiceSetup> getPenddingInvoiceSetUp() {
		return penddingInvoiceSetUp;
	}

	public void setPenddingInvoiceSetUp(List<InvoiceSetup> penddingInvoiceSetUp) {
		this.penddingInvoiceSetUp = penddingInvoiceSetUp;
	}
	
	public List<ManualInvoice> getPenddingManualInvoice() {
		return penddingManualInvoice;
	}

	public void setPenddingManualInvoice(List<ManualInvoice> penddingManualInvoice) {
		this.penddingManualInvoice = penddingManualInvoice;
	}

	public List<InvoiceReturn> getPenddingInvoiceReturn() {
		return penddingInvoiceReturn;
	}

	public void setPenddingInvoiceReturn(List<InvoiceReturn> penddingInvoiceReturn) {
		this.penddingInvoiceReturn = penddingInvoiceReturn;
	}
	
	public List<InvoiceAlertDetails> getInvoiceAlertDetails() {
		return invoiceAlertDetails;
	}

	public void setInvoiceAlertDetails(List<InvoiceAlertDetails> invoiceAlertDetails) {
		this.invoiceAlertDetails = invoiceAlertDetails;
	}

	public List<InvoiceAlertDetailsDTO> getInvoiceAlertDetailsDTO() {
		return invoiceAlertDetailsDTO;
	}

	public void setInvoiceAlertDetailsDTO(
			List<InvoiceAlertDetailsDTO> invoiceAlertDetailsDTO) {
		this.invoiceAlertDetailsDTO = invoiceAlertDetailsDTO;
	}

	public List<PoInvoiceSetupDetailsView> getPoInvoiceSetupDetailsView() {
		return poInvoiceSetupDetailsView;
	}

	public void setPoInvoiceSetupDetailsView(List<PoInvoiceSetupDetailsView> poInvoiceSetupDetailsView) {
		this.poInvoiceSetupDetailsView = poInvoiceSetupDetailsView;
	}

	public Map<UUID, PreBillView> getPreBillViewMap() {
		return preBillViewMap;
	}

	public void setPreBillViewMap(Map<UUID, PreBillView> preBillViewMap) {
		this.preBillViewMap = preBillViewMap;
	}

	public Map<UUID, List<InvoiceQueue>> getInvoiceQueueMap() {
		return invoiceQueueMap;
	}

	public void setInvoiceQueueMap(Map<UUID, List<InvoiceQueue>> invoiceQueueMap) {
		this.invoiceQueueMap = invoiceQueueMap;
	}

	public List<String> getApprovedInvoiceReturn() {
		return approvedInvoiceReturn;
	}

	public void setApprovedInvoiceReturn(List<String> approvedInvoiceReturn) {
		this.approvedInvoiceReturn = approvedInvoiceReturn;
	}

	public List<String> getApprovedManualInvoice() {
		return approvedManualInvoice;
	}

	public void setApprovedManualInvoice(List<String> approvedManualInvoice) {
		this.approvedManualInvoice = approvedManualInvoice;
	}

	public List<InvoiceAlertDetails> getRemoveInvoiceAlertDetails() {
		return removeInvoiceAlertDetails;
	}

	public void setRemoveInvoiceAlertDetails(List<InvoiceAlertDetails> removeInvoiceAlertDetails) {
		this.removeInvoiceAlertDetails = removeInvoiceAlertDetails;
	}

}