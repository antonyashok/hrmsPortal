package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.tm.invoice.dto.UserPreferenceDTO;

@Entity
@Table(name = "po_invoice_setup_details_view")
public class PoInvoiceSetupDetailsView implements Serializable {

	private static final long serialVersionUID = 1543767924611329123L;

	@Id
	@Column(name = "po_id")
	@Type(type = "uuid-char")
	private UUID purchaseOrderId;

	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "inv_actv_flg")
	private String invoiceActiveFlag;

	@Column(name = "inv_end_date")
	private Date invoiceEndDate;

	@Column(name = "inv_setup_id")
	@Type(type = "uuid-char")
	private UUID invoiceSetupId;

	@Column(name = "inv_setup_nm")
	private String invoiceSetupName;

//	@Column(name = "inv_specialist_empl_id")
//	private Long invoiceSpecialistEmployeeId;

	@Column(name = "inv_start_date")
	private Date invoiceStartDate;

	@Column(name = "inv_status")
	private String invoiceStatus;

	@Column(name = "inv_template_id")
	private Long invoiceTemplateId;

	@Column(name = "inv_typ_nm")
	private String invoiceTypeName;

//	@Column(name = "invoice_setup_source")
//	private String invoiceSetupSource;

	@Column(name = "po_end_date")
	private Date poEndDate;

	@Column(name = "po_number")
	private String poNumber;

	@Column(name = "po_start_date")
	private Date poStartDate;

//	@Column(name = "po_status")
//	private String poStatus;

	@Column(name = "engmt_strt_dt")
	private Date engagementStartDate;

	@Column(name = "engmt_end_dt")
	private Date engagementEndDate;

	@Column(name = "currency_name")
	private String currencyName;

	@Column(name = "bill_to_mgr_name")
	private String billToMgrName;

	@Column(name = "bill_to_mgr_email")
	private String billToManagerEmailId;

	@Column(name = "bill_address")
	private String billAddress;

	@Column(name = "countryName")
	private String countryName;

	@Column(name = "state_name")
	private String stateName;

	@Column(name = "city_name")
	private String cityName;

	@Column(name = "postal_code")
	private String postalCode;

	@Column(name = "finance_mgr_id")
	private Long financeMgrId;

	@Column(name = "finance_mgr_name")
	private String financeMgrName;

	@Column(name = "inv_delivery_mode")
	private String invoiceDeliveryMode;

	@Column(name = "hiring_mgr_name")
	private String hiringMgrName;

	@Column(name = "hiring_mgr_email")
	private String hiringMgrEmail;

	@Column(name = "finance_rep_id")
	private Long financeRepresentId;

	@Column(name = "finance_rep_name")
	private String financeRepresentName;

//	@Column(name = "payment_terms")
//	private String paymentTerms;

	@Column(name = "engagement_name")
	private String projectName;

	@Column(name = "engmt_id")
	@Type(type = "uuid-char")
	private UUID projectId;

	@Column
	private String prefix;

	@Column(name = "starting_number")
	private Integer startingNumber;

	@Column(name = "suffix_type")
	private String suffixType;

	@Column(name = "suffix_value")
	private String suffixValue;

	@Column(name = "separator_value")
	private String separatorValue;

	@Column(name = "inv_setup_notes")
	private String invoiceSetupNotes;
	
	@Column(name = "bill_cycle_day")
	private String billCycleDay;
	
	@Column(name = "bill_cyl_nm")
	private String billCycleName;
	
	@Column(name = "bill_cycle_strt_end_det")
	private String billCycleStartEndDetail;
	
	
	@Transient
	@Type(type = "uuid-char")
	private List<UUID> invoiceSetupIds;

	@Transient
	private List<BillCycle> billCycles;

	@Transient
	private InvoiceSetupBillCycle invoiceSetupBillCycle;

	@Transient
	private UserPreferenceDTO userPreferenceDTO;

	public UUID getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(UUID purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getInvoiceActiveFlag() {
		return invoiceActiveFlag;
	}

	public void setInvoiceActiveFlag(String invoiceActiveFlag) {
		this.invoiceActiveFlag = invoiceActiveFlag;
	}

	public Date getInvoiceEndDate() {
		return invoiceEndDate;
	}

	public void setInvoiceEndDate(Date invoiceEndDate) {
		this.invoiceEndDate = invoiceEndDate;
	}

	public UUID getInvoiceSetupId() {
		return invoiceSetupId;
	}

	public void setInvoiceSetupId(UUID invoiceSetupId) {
		this.invoiceSetupId = invoiceSetupId;
	}

	public String getInvoiceSetupName() {
		return invoiceSetupName;
	}

	public void setInvoiceSetupName(String invoiceSetupName) {
		this.invoiceSetupName = invoiceSetupName;
	}
	
	public Date getInvoiceStartDate() {
		return invoiceStartDate;
	}

	public void setInvoiceStartDate(Date invoiceStartDate) {
		this.invoiceStartDate = invoiceStartDate;
	}

	public String getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public Long getInvoiceTemplateId() {
		return invoiceTemplateId;
	}

	public void setInvoiceTemplateId(Long invoiceTemplateId) {
		this.invoiceTemplateId = invoiceTemplateId;
	}

	public String getInvoiceTypeName() {
		return invoiceTypeName;
	}

	public void setInvoiceTypeName(String invoiceTypeName) {
		this.invoiceTypeName = invoiceTypeName;
	}

	public Date getPoEndDate() {
		return poEndDate;
	}

	public void setPoEndDate(Date poEndDate) {
		this.poEndDate = poEndDate;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public Date getPoStartDate() {
		return poStartDate;
	}

	public void setPoStartDate(Date poStartDate) {
		this.poStartDate = poStartDate;
	}

	public Date getEngagementStartDate() {
		return engagementStartDate;
	}

	public void setEngagementStartDate(Date engagementStartDate) {
		this.engagementStartDate = engagementStartDate;
	}

	public Date getEngagementEndDate() {
		return engagementEndDate;
	}

	public void setEngagementEndDate(Date engagementEndDate) {
		this.engagementEndDate = engagementEndDate;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getBillToMgrName() {
		return billToMgrName;
	}

	public void setBillToMgrName(String billToMgrName) {
		this.billToMgrName = billToMgrName;
	}

	public String getBillToManagerEmailId() {
		return billToManagerEmailId;
	}

	public void setBillToManagerEmailId(String billToManagerEmailId) {
		this.billToManagerEmailId = billToManagerEmailId;
	}

	public String getBillAddress() {
		return billAddress;
	}

	public void setBillAddress(String billAddress) {
		this.billAddress = billAddress;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Long getFinanceMgrId() {
		return financeMgrId;
	}

	public void setFinanceMgrId(Long financeMgrId) {
		this.financeMgrId = financeMgrId;
	}

	public String getFinanceMgrName() {
		return financeMgrName;
	}

	public void setFinanceMgrName(String financeMgrName) {
		this.financeMgrName = financeMgrName;
	}

	public String getInvoiceDeliveryMode() {
		return invoiceDeliveryMode;
	}

	public void setInvoiceDeliveryMode(String invoiceDeliveryMode) {
		this.invoiceDeliveryMode = invoiceDeliveryMode;
	}

	public List<UUID> getInvoiceSetupIds() {
		return invoiceSetupIds;
	}

	public void setInvoiceSetupIds(List<UUID> invoiceSetupIds) {
		this.invoiceSetupIds = invoiceSetupIds;
	}

	public List<BillCycle> getBillCycles() {
		return billCycles;
	}

	public void setBillCycles(List<BillCycle> billCycles) {
		this.billCycles = billCycles;
	}

	public InvoiceSetupBillCycle getInvoiceSetupBillCycle() {
		return invoiceSetupBillCycle;
	}

	public void setInvoiceSetupBillCycle(InvoiceSetupBillCycle invoiceSetupBillCycle) {
		this.invoiceSetupBillCycle = invoiceSetupBillCycle;
	}

	public UserPreferenceDTO getUserPreferenceDTO() {
		return userPreferenceDTO;
	}

	public void setUserPreferenceDTO(UserPreferenceDTO userPreferenceDTO) {
		this.userPreferenceDTO = userPreferenceDTO;
	}

	public String getHiringMgrName() {
		return hiringMgrName;
	}

	public void setHiringMgrName(String hiringMgrName) {
		this.hiringMgrName = hiringMgrName;
	}

	public String getHiringMgrEmail() {
		return hiringMgrEmail;
	}

	public void setHiringMgrEmail(String hiringMgrEmail) {
		this.hiringMgrEmail = hiringMgrEmail;
	}

	public Long getFinanceRepresentId() {
		return financeRepresentId;
	}

	public void setFinanceRepresentId(Long financeRepresentId) {
		this.financeRepresentId = financeRepresentId;
	}

	public String getFinanceRepresentName() {
		return financeRepresentName;
	}

	public void setFinanceRepresentName(String financeRepresentName) {
		this.financeRepresentName = financeRepresentName;
	}

//	public String getPaymentTerms() {
//		return paymentTerms;
//	}
//
//	public void setPaymentTerms(String paymentTerms) {
//		this.paymentTerms = paymentTerms;
//	}
//	
//	public String getPoStatus() {
//		return poStatus;
//	}
//
//	public void setPoStatus(String poStatus) {
//		this.poStatus = poStatus;
//	}
//	
//	public String getInvoiceSetupSource() {
//		return invoiceSetupSource;
//	}
//
//	public void setInvoiceSetupSource(String invoiceSetupSource) {
//		this.invoiceSetupSource = invoiceSetupSource;
//	}
//	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}	
//
//	public Long getInvoiceSpecialistEmployeeId() {
//		return invoiceSpecialistEmployeeId;
//	}
//
//	public void setInvoiceSpecialistEmployeeId(Long invoiceSpecialistEmployeeId) {
//		this.invoiceSpecialistEmployeeId = invoiceSpecialistEmployeeId;
//	}
	

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public UUID getProjectId() {
		return projectId;
	}

	public void setProjectId(UUID projectId) {
		this.projectId = projectId;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public Integer getStartingNumber() {
		return startingNumber;
	}

	public void setStartingNumber(Integer startingNumber) {
		this.startingNumber = startingNumber;
	}

	public String getSuffixType() {
		return suffixType;
	}

	public void setSuffixType(String suffixType) {
		this.suffixType = suffixType;
	}

	public String getSuffixValue() {
		return suffixValue;
	}

	public void setSuffixValue(String suffixValue) {
		this.suffixValue = suffixValue;
	}

	public String getSeparatorValue() {
		return separatorValue;
	}

	public void setSeparatorValue(String separatorValue) {
		this.separatorValue = separatorValue;
	}

	public String getInvoiceSetupNotes() {
		return invoiceSetupNotes;
	}

	public void setInvoiceSetupNotes(String invoiceSetupNotes) {
		this.invoiceSetupNotes = invoiceSetupNotes;
	}

	public String getBillCycleDay() {
		return billCycleDay;
	}

	public void setBillCycleDay(String billCycleDay) {
		this.billCycleDay = billCycleDay;
	}

	public String getBillCycleName() {
		return billCycleName;
	}

	public void setBillCycleName(String billCycleName) {
		this.billCycleName = billCycleName;
	}

	public String getBillCycleStartEndDetail() {
		return billCycleStartEndDetail;
	}

	public void setBillCycleStartEndDetail(String billCycleStartEndDetail) {
		this.billCycleStartEndDetail = billCycleStartEndDetail;
	}

 

}
