package com.tm.invoice.dto;

import java.io.Serializable;

public class UserPreferenceDTO implements Serializable {

  private static final long serialVersionUID = -3871910797698379759L;

  private boolean isTimesheetInclude;
  private boolean isExpenseInclude;
  private boolean isExpenseDocumentationInclude;
  private boolean isHiringManagerShow;
  private boolean isSingleLineItemShow;
  private boolean isDifferentLineItemShow;
  private boolean isSeparateOverTimeInvoiceGenerate;
  private boolean isContractorNameExclude;
  private boolean isAutoDelivery;
  private boolean isEmailDelivery;
  private boolean isPostalDelivery;

  public boolean isTimesheetInclude() {
    return isTimesheetInclude;
  }

  public void setTimesheetInclude(boolean isTimesheetInclude) {
    this.isTimesheetInclude = isTimesheetInclude;
  }

  public boolean isExpenseInclude() {
    return isExpenseInclude;
  }

  public void setExpenseInclude(boolean isExpenseInclude) {
    this.isExpenseInclude = isExpenseInclude;
  }

  public boolean isExpenseDocumentationInclude() {
    return isExpenseDocumentationInclude;
  }

  public void setExpenseDocumentationInclude(boolean isExpenseDocumentationInclude) {
    this.isExpenseDocumentationInclude = isExpenseDocumentationInclude;
  }

  public boolean isHiringManagerShow() {
    return isHiringManagerShow;
  }

  public void setHiringManagerShow(boolean isHiringManagerShow) {
    this.isHiringManagerShow = isHiringManagerShow;
  }

  public boolean isSingleLineItemShow() {
    return isSingleLineItemShow;
  }

  public void setSingleLineItemShow(boolean isSingleLineItemShow) {
    this.isSingleLineItemShow = isSingleLineItemShow;
  }

  public boolean isDifferentLineItemShow() {
    return isDifferentLineItemShow;
  }

  public void setDifferentLineItemShow(boolean isDifferentLineItemShow) {
    this.isDifferentLineItemShow = isDifferentLineItemShow;
  }

  public boolean isSeparateOverTimeInvoiceGenerate() {
    return isSeparateOverTimeInvoiceGenerate;
  }

  public void setSeparateOverTimeInvoiceGenerate(boolean isSeparateOverTimeInvoiceGenerate) {
    this.isSeparateOverTimeInvoiceGenerate = isSeparateOverTimeInvoiceGenerate;
  }

  public boolean isContractorNameExclude() {
    return isContractorNameExclude;
  }

  public void setContractorNameExclude(boolean isContractorNameExclude) {
    this.isContractorNameExclude = isContractorNameExclude;
  }

  public boolean isAutoDelivery() {
    return isAutoDelivery;
  }

  public void setAutoDelivery(boolean isAutoDelivery) {
    this.isAutoDelivery = isAutoDelivery;
  }

  public boolean isEmailDelivery() {
    return isEmailDelivery;
  }

  public void setEmailDelivery(boolean isEmailDelivery) {
    this.isEmailDelivery = isEmailDelivery;
  }

  public boolean isPostalDelivery() {
    return isPostalDelivery;
  }

  public void setPostalDelivery(boolean isPostalDelivery) {
    this.isPostalDelivery = isPostalDelivery;
  }

}
