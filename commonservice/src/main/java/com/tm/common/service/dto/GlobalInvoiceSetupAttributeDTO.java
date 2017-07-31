package com.tm.common.service.dto;

import java.io.Serializable;
import java.util.List;

public class GlobalInvoiceSetupAttributeDTO implements Serializable {

  private static final long serialVersionUID = -6626074464147108515L;

  private List<EntityAttributeInfoDTO> lineOfBusinesses;
  private List<EntityAttributeInfoDTO> payCurrencies;
  private List<EntityAttributeInfoDTO> terms;
  private List<EntityAttributeInfoDTO> deliveries;
  private List<EntityAttributeInfoDTO> options;

  public List<EntityAttributeInfoDTO> getLineOfBusinesses() {
    return lineOfBusinesses;
  }

  public void setLineOfBusinesses(List<EntityAttributeInfoDTO> lineOfBusinesses) {
    this.lineOfBusinesses = lineOfBusinesses;
  }

  public List<EntityAttributeInfoDTO> getPayCurrencies() {
    return payCurrencies;
  }

  public void setPayCurrencies(List<EntityAttributeInfoDTO> payCurrencies) {
    this.payCurrencies = payCurrencies;
  }

  public List<EntityAttributeInfoDTO> getTerms() {
    return terms;
  }

  public void setTerms(List<EntityAttributeInfoDTO> terms) {
    this.terms = terms;
  }

  public List<EntityAttributeInfoDTO> getDeliveries() {
    return deliveries;
  }

  public void setDeliveries(List<EntityAttributeInfoDTO> deliveries) {
    this.deliveries = deliveries;
  }

  public List<EntityAttributeInfoDTO> getOptions() {
    return options;
  }

  public void setOptions(List<EntityAttributeInfoDTO> options) {
    this.options = options;
  }
  
}
