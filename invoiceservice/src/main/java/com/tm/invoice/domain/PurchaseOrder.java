package com.tm.invoice.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.commonapi.core.audit.mysql.BaseAuditableEntity;
import com.tm.invoice.enums.ActiveFlag;

@Entity
@Table(name = "purchase_order")
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties({ "auditDetails" })
@Inheritance(strategy = InheritanceType.JOINED)
public class PurchaseOrder extends BaseAuditableEntity implements Serializable{

    private static final long serialVersionUID = -2879849747860430777L;

    public enum Alert {
        Y, N
    }

    public enum Potype {
        REVENUE, EXPENSE
    }

    @Id
    @Column(name = "po_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID purchaseOrderId;

    @Column(name = "actv_flg")
    @Enumerated(EnumType.STRING)
    private ActiveFlag active;

    @Enumerated(EnumType.STRING)
    private Alert alert;

    private String description;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "engmt_id")
    @Type(type = "uuid-char")
    private UUID engagementId;

    @Column(name = "invoice_generated_date")
    private Date invoiceGeneratedDate;

    private String notes;

    @Column(name = "parent_po_id")
    @Type(type = "uuid-char")
    private UUID parentPurchaseOrderId;

    @Column(name = "po_amount")
    private BigDecimal purchaseOrderAmount;

    @Column(name = "po_number")
    private String poNumber;

    @Column(name = "po_type")
    @Enumerated(EnumType.STRING)
    private Potype purchaseOrderType;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "unbilled_po_ref")
    private String unbilledPoReference;
    
    public UUID getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(UUID purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public ActiveFlag getActive() {
        return active;
    }

    public void setActive(ActiveFlag active) {
        this.active = active;
    }

    public Alert getAlert() {
        return alert;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public UUID getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(UUID engagementId) {
        this.engagementId = engagementId;
    }

    public Date getInvoiceGeneratedDate() {
        return invoiceGeneratedDate;
    }

    public void setInvoiceGeneratedDate(Date invoiceGeneratedDate) {
        this.invoiceGeneratedDate = invoiceGeneratedDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public UUID getParentPurchaseOrderId() {
        return parentPurchaseOrderId;
    }

    public void setParentPurchaseOrderId(UUID parentPurchaseOrderId) {
        this.parentPurchaseOrderId = parentPurchaseOrderId;
    }

    public BigDecimal getPurchaseOrderAmount() {
        return purchaseOrderAmount;
    }

    public void setPurchaseOrderAmount(BigDecimal purchaseOrderAmount) {
        this.purchaseOrderAmount = purchaseOrderAmount;
    }

    public Potype getPurchaseOrderType() {
        return purchaseOrderType;
    }

    public void setPurchaseOrderType(Potype purchaseOrderType) {
        this.purchaseOrderType = purchaseOrderType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getUnbilledPoReference() {
        return unbilledPoReference;
    }

    public void setUnbilledPoReference(String unbilledPoReference) {
        this.unbilledPoReference = unbilledPoReference;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

}