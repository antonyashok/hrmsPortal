package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.tm.invoice.enums.GlobalInvoiceFlag;

public class GlobalInvoiceSetupOptionDTO implements Serializable {

  private static final long serialVersionUID = -2242249888250820907L;

  private UUID invoiceSetupOptionId;

  @Enumerated(EnumType.STRING)
  private GlobalInvoiceFlag value;
  
  private UUID id;

 
  public UUID getInvoiceSetupOptionId() {
    return invoiceSetupOptionId;
  }

  public void setInvoiceSetupOptionId(UUID invoiceSetupOptionId) {
    this.invoiceSetupOptionId = invoiceSetupOptionId;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

public GlobalInvoiceFlag getValue() {
	return value;
}

public void setValue(GlobalInvoiceFlag value) {
	this.value = value;
}


}
