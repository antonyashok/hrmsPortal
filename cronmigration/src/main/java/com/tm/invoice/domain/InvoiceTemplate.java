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


    private static final long serialVersionUID = 6304176049600497001L;

    public enum active {
        Y, N
    }

    public enum templateflag {
        Y, N
    }

    @Id
    @Column(name = "inv_template_id")
    private Long invoiceTemplateId;

    @Column(name = "inv_template_nm")
    private String invoiceTemplateName;

    @Column(name = "inv_template_desc")
    private String invoiceTemplatedesc;

    @Column(name = "logofilename")
    private String logofileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "std_template_flg")
    private templateflag templateflag;

    @Enumerated(EnumType.STRING)
    @Column(name = "actv_flg")
    private active activeStatus;
    
    private String template;

    @Column(name = "created_by")
    private Long createdby;

    @Column(name = "created_on")
    private Date createdDate;

    @Column(name = "updated_by")
    private Long updatedby;

    @Column(name = "updated_on")
    private Date updatedDate;

    public Long getInvoiceTemplateId() {
        return invoiceTemplateId;
    }

    public void setInvoiceTemplateId(Long invoiceTemplateId) {
        this.invoiceTemplateId = invoiceTemplateId;
    }

    public String getInvoiceTemplateName() {
        return invoiceTemplateName;
    }

    public void setInvoiceTemplateName(String invoiceTemplateName) {
        this.invoiceTemplateName = invoiceTemplateName;
    }

    public String getInvoiceTemplatedesc() {
        return invoiceTemplatedesc;
    }

    public void setInvoiceTemplatedesc(String invoiceTemplatedesc) {
        this.invoiceTemplatedesc = invoiceTemplatedesc;
    }

    public String getLogofileName() {
        return logofileName;
    }

    public void setLogofileName(String logofileName) {
        this.logofileName = logofileName;
    }



    public templateflag getTemplateflag() {
        return templateflag;
    }

    public void setTemplateflag(templateflag templateflag) {
        this.templateflag = templateflag;
    }

    public active getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(active activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Long getCreatedby() {
        return createdby;
    }

    public void setCreatedby(Long createdby) {
        this.createdby = createdby;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(Long updatedby) {
        this.updatedby = updatedby;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
}
