package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "invoiceQueue")
public class InvoiceQueue implements Serializable {

	private static final long serialVersionUID = -5347114775119878689L;

	@Id
	private UUID id;
	private String invoiceNumber;
	private Long billToClientId;
	@Type(type = "uuid-char")
	private UUID invoiceSetupId;
	private String billToClientName;
	private Long endClientId;
	private String endClientName;
	private Long billingSpecialistId;
	private String billingSpecialistName;
	private String invoiceType;
	private String timesheetType;
	private String billCycle;
	private String location;
	private String country;
	private String delivery;
	private String currencyType;
	private double amount;
	private String status;
	private String timesheetAttachment;
	private String billableExpensesAttachment;
	private AuditFields created;
	private AuditFields updated;
	private String invoiceSetupName;
	private Date startDate;
	private Date endDate;
	private String exceptionSource;
	private String attentionManagerName;
	private List<InvoiceExceptionDetails> invoiceExceptionDetail;
	private String comments;
	@Type(type = "uuid-char")
	private UUID dpQueueId;
	private UUID purchaseOrderId;
	private Date billDate;
	private Date invoiceDate;
	private String billToManagerName;
	private String billToManagerEmailId;
	private Integer invoiceSetupNumber;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Long getBillToClientId() {
		return billToClientId;
	}

	public void setBillToClientId(Long billToClientId) {
		this.billToClientId = billToClientId;
	}

	public String getBillToClientName() {
		return billToClientName;
	}

	public void setBillToClientName(String billToClientName) {
		this.billToClientName = billToClientName;
	}

	public Long getEndClientId() {
		return endClientId;
	}

	public void setEndClientId(Long endClientId) {
		this.endClientId = endClientId;
	}

	public String getEndClientName() {
		return endClientName;
	}

	public void setEndClientName(String endClientName) {
		this.endClientName = endClientName;
	}

	public Long getBillingSpecialistId() {
		return billingSpecialistId;
	}

	public void setBillingSpecialistId(Long billingSpecialistId) {
		this.billingSpecialistId = billingSpecialistId;
	}

	public String getBillingSpecialistName() {
		return billingSpecialistName;
	}

	public void setBillingSpecialistName(String billingSpecialistName) {
		this.billingSpecialistName = billingSpecialistName;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getTimesheetType() {
		return timesheetType;
	}

	public void setTimesheetType(String timesheetType) {
		this.timesheetType = timesheetType;
	}

	public String getBillCycle() {
		return billCycle;
	}

	public void setBillCycle(String billCycle) {
		this.billCycle = billCycle;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTimesheetAttachment() {
		return timesheetAttachment;
	}

	public void setTimesheetAttachment(String timesheetAttachment) {
		this.timesheetAttachment = timesheetAttachment;
	}

	public String getBillableExpensesAttachment() {
		return billableExpensesAttachment;
	}

	public void setBillableExpensesAttachment(String billableExpensesAttachment) {
		this.billableExpensesAttachment = billableExpensesAttachment;
	}

	public UUID getInvoiceSetupId() {
		return invoiceSetupId;
	}

	public void setInvoiceSetupId(UUID invoiceSetupId) {
		this.invoiceSetupId = invoiceSetupId;
	}

	public AuditFields getCreated() {
		return created;
	}

	public void setCreated(AuditFields created) {
		this.created = created;
	}

	public AuditFields getUpdated() {
		return updated;
	}

	public void setUpdated(AuditFields updated) {
		this.updated = updated;
	}

	public String getInvoiceSetupName() {
		return invoiceSetupName;
	}

	public void setInvoiceSetupName(String invoiceSetupName) {
		this.invoiceSetupName = invoiceSetupName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getExceptionSource() {
		return exceptionSource;
	}

	public void setExceptionSource(String exceptionSource) {
		this.exceptionSource = exceptionSource;
	}

	public String getAttentionManagerName() {
		return attentionManagerName;
	}

	public void setAttentionManagerName(String attentionManagerName) {
		this.attentionManagerName = attentionManagerName;
	}

	public List<InvoiceExceptionDetails> getInvoiceExceptionDetail() {
		return invoiceExceptionDetail;
	}

	public void setInvoiceExceptionDetail(List<InvoiceExceptionDetails> invoiceExceptionDetail) {
		this.invoiceExceptionDetail = invoiceExceptionDetail;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public UUID getDpQueueId() {
		return dpQueueId;
	}

	public void setDpQueueId(UUID dpQueueId) {
		this.dpQueueId = dpQueueId;
	}

	public UUID getPurchaseOrderId() {
		return purchaseOrderId;
	}

	public void setPurchaseOrderId(UUID purchaseOrderId) {
		this.purchaseOrderId = purchaseOrderId;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

    public String getBillToManagerName() {
        return billToManagerName;
    }

    public void setBillToManagerName(String billToManagerName) {
        this.billToManagerName = billToManagerName;
    }

    public String getBillToManagerEmailId() {
        return billToManagerEmailId;
    }

    public void setBillToManagerEmailId(String billToManagerEmailId) {
        this.billToManagerEmailId = billToManagerEmailId;
    }

	public Integer getInvoiceSetupNumber() {
		return invoiceSetupNumber;
	}

	public void setInvoiceSetupNumber(Integer invoiceSetupNumber) {
		this.invoiceSetupNumber = invoiceSetupNumber;
	}
    
}
