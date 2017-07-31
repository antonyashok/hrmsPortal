package com.tm.invoice.dto;

import java.io.Serializable;

import com.tm.invoice.domain.InvoiceTemplate.templateflag;

public class InvoiceTemplateDTO implements Serializable {
  
  private static final long serialVersionUID = -842603064224232232L;
 
  private long invoiceTemplateId;

  private String invoiceTemplateName;

  private String invoiceTemplatedesc;

  private long mongoTemplaterefId;

  private String logofileName;

  private templateflag templateflag;
  
  private String template;

	public String getTemplate() {
    return template;
}

public void setTemplate(String template) {
    this.template = template;
}

    public long getInvoiceTemplateId() {
		return invoiceTemplateId;
	}

	public void setInvoiceTemplateId(long invoiceTemplateId) {
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

	public long getMongoTemplaterefId() {
		return mongoTemplaterefId;
	}

	public void setMongoTemplaterefId(long mongoTemplaterefId) {
		this.mongoTemplaterefId = mongoTemplaterefId;
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
  
  
  
  
}
