package com.tm.invoice.dto;

import java.io.Serializable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.invoice.domain.InvoiceTemplate.Active;
import com.tm.invoice.domain.InvoiceTemplate.Templateflag;

@JsonInclude(value = Include.NON_NULL)
public class InvoiceTemplateDTO implements Serializable{

    private static final long serialVersionUID = 240371172922049383L;

    private Long invoiceTemplateId;

    @Enumerated(EnumType.STRING)
    private Active activeStatus;

    private String invoiceTemplatedesc;

    private String invoiceTemplateName;

    private String logofilename;

    @Enumerated(EnumType.STRING)
    private Templateflag stdTemplateFlag;

    private byte[] template;
    
    private String contentType="image/png";

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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
  }
