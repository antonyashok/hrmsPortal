package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "invoice_setup")
public class InvoiceSetup extends AbstractAuditingEntity implements Serializable {

  private static final long serialVersionUID = -6607008790426384076L;

  public enum ActiveFlag {
    Y, N
  }

  @Id
  @Type(type = "uuid-char")
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "uuid2")
  @Column(name = "inv_setup_id", nullable = false)
  private UUID invoiceSetupId;

  @Column(name = "actv_flg")
  @Enumerated(EnumType.STRING)
  private ActiveFlag activeFlag;

  @Column(name = "bill_to_org_id")
  private Long billToOrganizationId;

  @Column(name = "delivery_mode_lkp_nm")
  private String deliveryModeLookUpName;

  @Column(name = "end_dt")
  private Date endDate;

  @Column(name="engmt_id")
  @Type(type = "uuid-char")
  private UUID engagementId;
  
  @Column(name = "inv_desc")
  private String invoiceDescription;

  @Column(name = "inv_setup_nm")
  private String invoiceSetupName;

  @Column(name = "inv_template_id")
  private Long invoiceTemplateId;

  @Column(name = "inv_typ_nm")
  private String invoiceTypeName;

  @Column
  private String notes;

  @Column(name = "receipient_email")
  private String receipientEmail;

  @Column
  private String status;

  @Column(name = "strt_dt")
  private Date startDate;


  @OneToMany(mappedBy = "invoiceSetup")
  private List<InvoiceSetupNote> invoiceSetupNotes;

  @OneToMany(mappedBy = "invoiceSetup")
  private List<InvoiceSetupOption> invoiceSetupOptions;

  @Transient
  private List<UUID> invoiceSetupIds;

  @Column
  private String prefix;

  @Column(name = "starting_number")
  private Integer startingNumber;

  @Column(name = "suffix_type")
  private String suffixType;

  @Column(name = "suffix_value")
  private String suffixValue;

  @Column(name="separator_value")
  private String separator;
  
  @Column(name = "bill_cycle_day")
  private String billCycleDay;

  @Column(name = "bill_cyl_nm")
  private String billCycleName;
  
  @Column(name = "bill_cycle_strt_end_det")
  private String billCycleStartEndDetail;

  public UUID getInvoiceSetupId() {
    return invoiceSetupId;
  }

  public void setInvoiceSetupId(UUID invoiceSetupId) {
    this.invoiceSetupId = invoiceSetupId;
  }

  public ActiveFlag getActiveFlag() {
    return activeFlag;
  }

  public void setActiveFlag(ActiveFlag activeFlag) {
    this.activeFlag = activeFlag;
  }

  public Long getBillToOrganizationId() {
    return billToOrganizationId;
  }

  public void setBillToOrganizationId(Long billToOrganizationId) {
    this.billToOrganizationId = billToOrganizationId;
  }

  public String getDeliveryModeLookUpName() {
    return deliveryModeLookUpName;
  }

  public void setDeliveryModeLookUpName(String deliveryModeLookUpName) {
    this.deliveryModeLookUpName = deliveryModeLookUpName;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getInvoiceDescription() {
    return invoiceDescription;
  }

  public void setInvoiceDescription(String invoiceDescription) {
    this.invoiceDescription = invoiceDescription;
  }

  public String getInvoiceSetupName() {
    return invoiceSetupName;
  }

  public void setInvoiceSetupName(String invoiceSetupName) {
    this.invoiceSetupName = invoiceSetupName;
  }

  public Long getInvoiceTemplateId() {
    return invoiceTemplateId;
  }

  public void setInvoiceTemplateId(Long invoiceTemplateId) {
    this.invoiceTemplateId = invoiceTemplateId;
  }

  public String getInvoiceTypeName() {
    return invoiceTypeName;
  }

  public void setInvoiceTypeName(String invoiceTypeName) {
    this.invoiceTypeName = invoiceTypeName;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getReceipientEmail() {
    return receipientEmail;
  }

  public void setReceipientEmail(String receipientEmail) {
    this.receipientEmail = receipientEmail;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public List<InvoiceSetupNote> getInvoiceSetupNotes() {
    return invoiceSetupNotes;
  }

  public void setInvoiceSetupNotes(List<InvoiceSetupNote> invoiceSetupNotes) {
    this.invoiceSetupNotes = invoiceSetupNotes;
  }

  public List<InvoiceSetupOption> getInvoiceSetupOptions() {
    return invoiceSetupOptions;
  }

  public void setInvoiceSetupOptions(List<InvoiceSetupOption> invoiceSetupOptions) {
    this.invoiceSetupOptions = invoiceSetupOptions;
  }

  public List<UUID> getInvoiceSetupIds() {
    return invoiceSetupIds;
  }

  public void setInvoiceSetupIds(List<UUID> invoiceSetupIds) {
    this.invoiceSetupIds = invoiceSetupIds;
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

public UUID getEngagementId() {
    return engagementId;
}

public void setEngagementId(UUID engagementId) {
    this.engagementId = engagementId;
}

public String getBillCycleDay() {
    return billCycleDay;
}

public void setBillCycleDay(String billCycleDay) {
    this.billCycleDay = billCycleDay;
}

public String getBillCycleName() {
    return billCycleName;
}

public void setBillCycleName(String billCycleName) {
    this.billCycleName = billCycleName;
}

public String getBillCycleStartEndDetail() {
    return billCycleStartEndDetail;
}

public void setBillCycleStartEndDetail(String billCycleStartEndDetail) {
    this.billCycleStartEndDetail = billCycleStartEndDetail;
}


}
