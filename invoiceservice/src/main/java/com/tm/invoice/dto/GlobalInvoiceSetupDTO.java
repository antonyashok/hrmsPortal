package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.hateoas.ResourceSupport;

import com.tm.invoice.enums.GlobalInvoiceFlag;
import com.tm.invoice.enums.GlobalInvoiceStatus;

public class GlobalInvoiceSetupDTO extends ResourceSupport implements Serializable {

  private static final long serialVersionUID = 4867158790232498523L;

  public enum ActiveFlagEnum {
    Y, N
  }

  public enum InvoiceStatusEnum {
    Active, Inactive, Hold, Draft
  }

  private UUID invoiceSetupId;

  private String invoiceSetupName;

  private String invoiceDescription;

  private String invoiceType;

  private String billCycleFrequency;

  private String billCycleFrequencyType;

  private String billCycleFrequencyDay;

  private String billCycleFrequencyDate;

  private String delivery;

  private String effectiveFromDate;

  private String effectiveToDate;

  private String notesToDisplay;

  private String invoiceSpecialistNotes;

  private Long invTemplateId;

  @Enumerated(EnumType.STRING)
  private GlobalInvoiceFlag activeFlag;

  @Enumerated(EnumType.STRING)
  private GlobalInvoiceStatus invoiceStatus;

  private List<GlobalInvoiceSetupOptionDTO> globalInvoiceSetupOption;

  private String prefix;

  private Integer startingNumber;

  private String suffixType;

  private String suffixValue;

  private String separator;

  public UUID getInvoiceSetupId() {
    return invoiceSetupId;
  }

  public void setInvoiceSetupId(UUID invoiceSetupId) {
    this.invoiceSetupId = invoiceSetupId;
  }

  public String getInvoiceSetupName() {
    return invoiceSetupName;
  }

  public void setInvoiceSetupName(String invoiceSetupName) {
    this.invoiceSetupName = invoiceSetupName;
  }

  public String getInvoiceDescription() {
    return invoiceDescription;
  }

  public void setInvoiceDescription(String invoiceDescription) {
    this.invoiceDescription = invoiceDescription;
  }

  public String getInvoiceType() {
    return invoiceType;
  }

  public void setInvoiceType(String invoiceType) {
    this.invoiceType = invoiceType;
  }

  public String getBillCycleFrequency() {
    return billCycleFrequency;
  }

  public void setBillCycleFrequency(String billCycleFrequency) {
    this.billCycleFrequency = billCycleFrequency;
  }

  public String getBillCycleFrequencyType() {
    return billCycleFrequencyType;
  }

  public void setBillCycleFrequencyType(String billCycleFrequencyType) {
    this.billCycleFrequencyType = billCycleFrequencyType;
  }

  public String getBillCycleFrequencyDay() {
    return billCycleFrequencyDay;
  }

  public void setBillCycleFrequencyDay(String billCycleFrequencyDay) {
    this.billCycleFrequencyDay = billCycleFrequencyDay;
  }

  public String getBillCycleFrequencyDate() {
    return billCycleFrequencyDate;
  }

  public void setBillCycleFrequencyDate(String billCycleFrequencyDate) {
    this.billCycleFrequencyDate = billCycleFrequencyDate;
  }

  public String getDelivery() {
    return delivery;
  }

  public void setDelivery(String delivery) {
    this.delivery = delivery;
  }

  public String getNotesToDisplay() {
    return notesToDisplay;
  }

  public void setNotesToDisplay(String notesToDisplay) {
    this.notesToDisplay = notesToDisplay;
  }

  public String getInvoiceSpecialistNotes() {
    return invoiceSpecialistNotes;
  }

  public void setInvoiceSpecialistNotes(String invoiceSpecialistNotes) {
    this.invoiceSpecialistNotes = invoiceSpecialistNotes;
  }

  /*
   * public String getInvoiceStatus() { return invoiceStatus; }
   * 
   * public void setInvoiceStatus(String invoiceStatus) { this.invoiceStatus = invoiceStatus; }
   */

  public String getEffectiveFromDate() {
    return effectiveFromDate;
  }

  public void setEffectiveFromDate(String effectiveFromDate) {
    this.effectiveFromDate = effectiveFromDate;
  }

  public String getEffectiveToDate() {
    return effectiveToDate;
  }

  public void setEffectiveToDate(String effectiveToDate) {
    this.effectiveToDate = effectiveToDate;
  }

  public List<GlobalInvoiceSetupOptionDTO> getGlobalInvoiceSetupOption() {
    return globalInvoiceSetupOption;
  }

  public void setGlobalInvoiceSetupOption(
      List<GlobalInvoiceSetupOptionDTO> globalInvoiceSetupOption) {
    this.globalInvoiceSetupOption = globalInvoiceSetupOption;
  }

  public Long getInvTemplateId() {
    return invTemplateId;
  }

  public void setInvTemplateId(Long invTemplateId) {
    this.invTemplateId = invTemplateId;
  }

  public GlobalInvoiceFlag getActiveFlag() {
    return activeFlag;
  }

  public void setActiveFlag(GlobalInvoiceFlag activeFlag) {
    this.activeFlag = activeFlag;
  }

  public GlobalInvoiceStatus getInvoiceStatus() {
    return invoiceStatus;
  }

  public void setInvoiceStatus(GlobalInvoiceStatus invoiceStatus) {
    this.invoiceStatus = invoiceStatus;
  }

  public String getPrefix() {
    return prefix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public Integer getStartingNumber() {
    return startingNumber;
  }

  public void setStartingNumber(Integer startingNumber) {
    this.startingNumber = startingNumber;
  }

  public String getSuffixType() {
    return suffixType;
  }

  public void setSuffixType(String suffixType) {
    this.suffixType = suffixType;
  }

  public String getSuffixValue() {
    return suffixValue;
  }

  public void setSuffixValue(String suffixValue) {
    this.suffixValue = suffixValue;
  }

  public String getSeparator() {
    return separator;
  }

  public void setSeparator(String separator) {
    this.separator = separator;
  }
  
}
