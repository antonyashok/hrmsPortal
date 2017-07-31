package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "invoice_template")
public class InvoiceTemplate implements Serializable {

    private static final long serialVersionUID = 7342074500649215951L;

    public enum Active {
        Y, N
    }

    public enum Templateflag {
        Y, N
    }

    @Id
    @Column(name = "inv_template_id")
    private Long invoiceTemplateId;

    @Column(name = "actv_flg")
    @Enumerated(EnumType.STRING)
    private Active activeStatus;

    @Column(name = "inv_template_desc")
    private String invoiceTemplatedesc;

    @Column(name = "inv_template_nm")
    private String invoiceTemplateName;

    private String logofilename;

    @Column(name = "std_template_flg")
    @Enumerated(EnumType.STRING)
    private Templateflag stdTemplateFlag;

    private byte[] template;
    
    @Column(name="created_by")
    private Long createdBy;

    @Column(name="created_on")
    private Date createdOn;
    
    @Column(name="updated_by")
    private Long updatedBy;

    @Column(name="updated_on")
    private Date updatedOn;

    public Long getInvoiceTemplateId() {
        return invoiceTemplateId;
    }

    public void setInvoiceTemplateId(Long invoiceTemplateId) {
        this.invoiceTemplateId = invoiceTemplateId;
    }

    public Active getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Active activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getInvoiceTemplatedesc() {
        return invoiceTemplatedesc;
    }

    public void setInvoiceTemplatedesc(String invoiceTemplatedesc) {
        this.invoiceTemplatedesc = invoiceTemplatedesc;
    }

    public String getInvoiceTemplateName() {
        return invoiceTemplateName;
    }

    public void setInvoiceTemplateName(String invoiceTemplateName) {
        this.invoiceTemplateName = invoiceTemplateName;
    }

    public String getLogofilename() {
        return logofilename;
    }

    public void setLogofilename(String logofilename) {
        this.logofilename = logofilename;
    }

    public Templateflag getStdTemplateFlag() {
        return stdTemplateFlag;
    }

    public void setStdTemplateFlag(Templateflag stdTemplateFlag) {
        this.stdTemplateFlag = stdTemplateFlag;
    }

    public byte[] getTemplate() {
        return template;
    }

    public void setTemplate(byte[] template) {
        this.template = template;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }
    
    
}
