package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;


@Entity
@Table(name="po_contractors_view")
public class PoContractorsView implements Serializable {

    private static final long serialVersionUID = -2938062805539690704L;

    @Column(name="contract_end_dt")
	private Date contractEndDate;

	@Column(name="contract_strt_dt")
	private Date contractStartDate;

	@Id
	@Column(name="emp_id")
	private Long employeeId;

	@Column(name="empl_nm")
	private String employeeName;

	@Column(name="inv_setup_id")
	@Type(type = "uuid-char")
	private UUID invoiceSetupId;

	@Type(type = "uuid-char")
	@Column(name="po_id")
	private UUID purchaseOrderId;
	
	@Column(name="po_number")
	private String poNumber;
	
	@Column(name="engmt_id")
	@Type(type = "uuid-char")
	private UUID engagementId;

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public Date getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public UUID getInvoiceSetupId() {
        return invoiceSetupId;
    }

    public void setInvoiceSetupId(UUID invoiceSetupId) {
        this.invoiceSetupId = invoiceSetupId;
    }

    public UUID getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(UUID purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public UUID getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(UUID engagementId) {
        this.engagementId = engagementId;
    }
    
    

}