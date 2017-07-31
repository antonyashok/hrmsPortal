package com.tm.invoice.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.tm.invoice.enums.GlobalInvoiceFlag;
import com.tm.invoice.enums.GlobalInvoiceStatus;

@Entity
@Table(name = "global_invoice_setup")
public class GlobalInvoiceSetup implements Serializable {

	private static final long serialVersionUID = -3035665727415008854L;

	@Id
	@Column(name = "global_inv_setup_id")
	@Type(type = "uuid-char")
	/*
	 * @GeneratedValue(generator = "uuid")
	 * 
	 * @GenericGenerator(name = "uuid", strategy = "uuid2")
	 */
	private UUID invoiceSetupId;

	@Column(name = "inv_setup_name")
	private String invoiceSetupName;

	@Column(name = "setup_descr")
	private String invoiceDescription;

	@Column(name = "inv_type")
	private String invoiceType;

	@Column(name = "inv_freq")
	private String billCycleFrequency;

	@Column(name = "inv_freq_type")
	private String billCycleFrequencyType;

	@Column(name = "in_freq_day")
	private String billCycleFrequencyDay;

	@Column(name = "inv_freq_date")
	private Date billCycleFrequencyDate;

	@Column(name = "delivery_mode")
	private String delivery;

	@Column(name = "inv_start_dt")
	private Date effectiveFromDate;

	@Column(name = "inv_end_dt")
	private Date effectiveToDate;

	@Column(name = "notes_to_display")
	private String notesToDisplay;

	@Column(name = "inv_notes")
	private String invoiceSpecialistNotes;

	@Column(name = "payment_terms")
	private String paymentTerms;

	@Column(name = "lob_name")
	private String lineOfBusiness;

	@Column(name = "inv_terms")
	private String terms;

	@Column(name = "currency")
	private String payCurrency;

	@Enumerated(EnumType.STRING)
	@Column(name = "actv_flg")
	private GlobalInvoiceFlag activeFlag;

	@Enumerated(EnumType.STRING)
	@Column(name = "inv_status")
	private GlobalInvoiceStatus invoiceStatus;

	@Column(name = "inv_template_id")
	private Long invTemplateId;

	/*
	 * @OneToOne
	 * 
	 * @JoinColumn(name = "inv_template_id") private InvoiceTemplate
	 * invoiceTemplate;
	 */

	@Column(name = "bill_specialist_id")
	private Long billSpecialistId;

	// @JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "globalInvoiceSetup")
	private List<GlobalInvoiceSetupOption> globalInvoiceSetupOption;

	@Column(name = "created_by", nullable = false, length = 50, updatable = false)
	private Long createdBy;

	@Column(name = "created_on", nullable = false)
	private Timestamp createdDate = new Timestamp(System.currentTimeMillis());

	@Column(name = "updated_by", length = 50)
	private Long lastModifiedBy;

	@Column(name = "updated_on")
	private Timestamp lastModifiedDate = new Timestamp(System.currentTimeMillis());

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

	public String getInvoiceDescription() {
		return invoiceDescription;
	}

	public void setInvoiceDescription(String invoiceDescription) {
		this.invoiceDescription = invoiceDescription;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getBillCycleFrequency() {
		return billCycleFrequency;
	}

	public void setBillCycleFrequency(String billCycleFrequency) {
		this.billCycleFrequency = billCycleFrequency;
	}

	public String getBillCycleFrequencyType() {
		return billCycleFrequencyType;
	}

	public void setBillCycleFrequencyType(String billCycleFrequencyType) {
		this.billCycleFrequencyType = billCycleFrequencyType;
	}

	public String getBillCycleFrequencyDay() {
		return billCycleFrequencyDay;
	}

	public void setBillCycleFrequencyDay(String billCycleFrequencyDay) {
		this.billCycleFrequencyDay = billCycleFrequencyDay;
	}

	public Date getBillCycleFrequencyDate() {
		return billCycleFrequencyDate;
	}

	public void setBillCycleFrequencyDate(Date billCycleFrequencyDate) {
		this.billCycleFrequencyDate = billCycleFrequencyDate;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public Date getEffectiveFromDate() {
		return effectiveFromDate;
	}

	public void setEffectiveFromDate(Date effectiveFromDate) {
		this.effectiveFromDate = effectiveFromDate;
	}

	public Date getEffectiveToDate() {
		return effectiveToDate;
	}

	public void setEffectiveToDate(Date effectiveToDate) {
		this.effectiveToDate = effectiveToDate;
	}

	public String getNotesToDisplay() {
		return notesToDisplay;
	}

	public void setNotesToDisplay(String notesToDisplay) {
		this.notesToDisplay = notesToDisplay;
	}

	public String getInvoiceSpecialistNotes() {
		return invoiceSpecialistNotes;
	}

	public void setInvoiceSpecialistNotes(String invoiceSpecialistNotes) {
		this.invoiceSpecialistNotes = invoiceSpecialistNotes;
	}

	public String getPaymentTerms() {
		return paymentTerms;
	}

	public void setPaymentTerms(String paymentTerms) {
		this.paymentTerms = paymentTerms;
	}

	public String getLineOfBusiness() {
		return lineOfBusiness;
	}

	public void setLineOfBusiness(String lineOfBusiness) {
		this.lineOfBusiness = lineOfBusiness;
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public String getPayCurrency() {
		return payCurrency;
	}

	public void setPayCurrency(String payCurrency) {
		this.payCurrency = payCurrency;
	}

	public GlobalInvoiceFlag getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(GlobalInvoiceFlag activeFlag) {
		this.activeFlag = activeFlag;
	}

	public GlobalInvoiceStatus getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(GlobalInvoiceStatus invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public Long getInvTemplateId() {
		return invTemplateId;
	}

	public void setInvTemplateId(Long invTemplateId) {
		this.invTemplateId = invTemplateId;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	public Long getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public List<GlobalInvoiceSetupOption> getGlobalInvoiceSetupOption() {
		return globalInvoiceSetupOption;
	}

	public void setGlobalInvoiceSetupOption(List<GlobalInvoiceSetupOption> globalInvoiceSetupOption) {
		this.globalInvoiceSetupOption = globalInvoiceSetupOption;
	}

	public Long getBillSpecialistId() {
		return billSpecialistId;
	}

	public void setBillSpecialistId(Long billSpecialistId) {
		this.billSpecialistId = billSpecialistId;
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

	/*
	 * public InvoiceTemplate getInvoiceTemplate() { return invoiceTemplate; }
	 * 
	 * public void setInvoiceTemplate(InvoiceTemplate invoiceTemplate) {
	 * this.invoiceTemplate = invoiceTemplate; }
	 */

}