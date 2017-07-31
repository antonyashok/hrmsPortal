package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.tm.invoice.enums.GlobalInvoiceFlag;

@Entity
@Table(name = "global_invoice_setup_option")
public class GlobalInvoiceSetupOption implements Serializable {

  private static final long serialVersionUID = -2242249888250820907L;

 
  @Id
  @Column(name = "global_inv_setup_optn_id")
  @Type(type = "uuid-char")
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  private UUID invoiceSetupOptionId;

  @ManyToOne
  @JoinColumn(name = "global_inv_setup_id")
  private GlobalInvoiceSetup globalInvoiceSetup;

  @Column(name = "option_lookup_id")
  @Type(type = "uuid-char")
  private UUID id;

 
  @Enumerated(EnumType.STRING)
  @Column(name = "option_value")
  private GlobalInvoiceFlag value;
 
  public UUID getInvoiceSetupOptionId() {
    return invoiceSetupOptionId;
  }

  public void setInvoiceSetupOptionId(UUID invoiceSetupOptionId) {
    this.invoiceSetupOptionId = invoiceSetupOptionId;
  }

  public GlobalInvoiceSetup getGlobalInvoiceSetup() {
    return globalInvoiceSetup;
  }

  public void setGlobalInvoiceSetup(GlobalInvoiceSetup globalInvoiceSetup) {
    this.globalInvoiceSetup = globalInvoiceSetup;
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
