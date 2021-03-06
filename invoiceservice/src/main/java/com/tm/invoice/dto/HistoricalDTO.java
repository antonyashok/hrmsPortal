package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@Relation(value = "historical", collectionRelation = "historicals")
public class HistoricalDTO implements Serializable {

  private static final long serialVersionUID = -8413268292072713090L;

  private UUID id;
  private UUID invoiceQueueId;
  private String invoiceNumber;
  private Long billToClientId;
  @Type(type = "uuid-char")
  private UUID invoiceSetupId;
  private String billToClientName;
  private Long endClientId;
  private String endClientName;
  private Long billingSpecialistId;
  private String billingSpecialistName;
  private String invoiceType;
  private String timesheetType;
  private String billCycle;
  private String location;
  private String country;
  private String delivery;
  private String currencyType;
  private String amountStr;
  private String amount;
  private String status;
  private String timesheetAttachment;
  private String billableExpensesAttachment;
  private String submittedDate;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getInvoiceQueueId() {
    return invoiceQueueId;
  }

  public void setInvoiceQueueId(UUID invoiceQueueId) {
    this.invoiceQueueId = invoiceQueueId;
  }

  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public Long getBillToClientId() {
    return billToClientId;
  }

  public void setBillToClientId(Long billToClientId) {
    this.billToClientId = billToClientId;
  }

  public UUID getInvoiceSetupId() {
    return invoiceSetupId;
  }

  public void setInvoiceSetupId(UUID invoiceSetupId) {
    this.invoiceSetupId = invoiceSetupId;
  }

  public String getBillToClientName() {
    return billToClientName;
  }

  public void setBillToClientName(String billToClientName) {
    this.billToClientName = billToClientName;
  }

  public Long getEndClientId() {
    return endClientId;
  }

  public void setEndClientId(Long endClientId) {
    this.endClientId = endClientId;
  }

  public String getEndClientName() {
    return endClientName;
  }

  public void setEndClientName(String endClientName) {
    this.endClientName = endClientName;
  }

  public Long getBillingSpecialistId() {
    return billingSpecialistId;
  }

  public void setBillingSpecialistId(Long billingSpecialistId) {
    this.billingSpecialistId = billingSpecialistId;
  }

  public String getBillingSpecialistName() {
    return billingSpecialistName;
  }

  public void setBillingSpecialistName(String billingSpecialistName) {
    this.billingSpecialistName = billingSpecialistName;
  }

  public String getInvoiceType() {
    return invoiceType;
  }

  public void setInvoiceType(String invoiceType) {
    this.invoiceType = invoiceType;
  }

  public String getTimesheetType() {
    return timesheetType;
  }

  public void setTimesheetType(String timesheetType) {
    this.timesheetType = timesheetType;
  }

  public String getBillCycle() {
    return billCycle;
  }

  public void setBillCycle(String billCycle) {
    this.billCycle = billCycle;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getDelivery() {
    return delivery;
  }

  public void setDelivery(String delivery) {
    this.delivery = delivery;
  }

  public String getCurrencyType() {
    return currencyType;
  }

  public void setCurrencyType(String currencyType) {
    this.currencyType = currencyType;
  }

  public String getAmountStr() {
    return amountStr;
  }

  public void setAmountStr(String amountStr) {
    this.amountStr = amountStr;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTimesheetAttachment() {
    return timesheetAttachment;
  }

  public void setTimesheetAttachment(String timesheetAttachment) {
    this.timesheetAttachment = timesheetAttachment;
  }

  public String getBillableExpensesAttachment() {
    return billableExpensesAttachment;
  }

  public void setBillableExpensesAttachment(String billableExpensesAttachment) {
    this.billableExpensesAttachment = billableExpensesAttachment;
  }

  public String getSubmittedDate() {
    return submittedDate;
  }

  public void setSubmittedDate(String submittedDate) {
    this.submittedDate = submittedDate;
  }

}
