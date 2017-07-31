package com.tm.invoice.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tm.invoice.domain.InvoiceSetupBillCycle.AccuringFlag;

@Relation(value = "InvoiceSetupBillCycle", collectionRelation = "InvoiceSetupBillCycle")
public class InvoiceSetupBillCycleDTO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = -1346814246512227169L;

    private Long invoiceSetupBillCycleId;

    private BigDecimal prebillAmount;

    private BigDecimal amountRemaining;

    private BigDecimal amountUsed;

    private String billCycleDay;

    private String billCycleName;

    private String invoiceType;

    private String billCycleStartEndDetail;

    private String consInvoiceFlag;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private String prebillEndDate;

    private BigDecimal initialPrebillAmount;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private String irrStartDate;
    
    private String prebillNotes;

    private BigDecimal prebillNumberOfHours;

    private String prebillWithoutContractorFlg;

    private String prebillSalesTaxFlag;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private String prebillStartDate;

    private String prebillTimeframeExceedFlg;

    @Type(type = "uuid-char")
    private UUID invoiceSetupId;

    private AccuringFlag accuringFlg;

    public Long getInvoiceSetupBillCycleId() {
        return invoiceSetupBillCycleId;
    }

    public void setInvoiceSetupBillCycleId(Long invoiceSetupBillCycleId) {
        this.invoiceSetupBillCycleId = invoiceSetupBillCycleId;
    }

    public BigDecimal getPrebillAmount() {
        return prebillAmount;
    }

    public void setPrebillAmount(BigDecimal prebillAmount) {
        this.prebillAmount = prebillAmount;
    }

    public BigDecimal getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(BigDecimal amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    public BigDecimal getAmountUsed() {
        return amountUsed;
    }

    public void setAmountUsed(BigDecimal amountUsed) {
        this.amountUsed = amountUsed;
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

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getBillCycleStartEndDetail() {
        return billCycleStartEndDetail;
    }

    public void setBillCycleStartEndDetail(String billCycleStartEndDetail) {
        this.billCycleStartEndDetail = billCycleStartEndDetail;
    }

    public String getConsInvoiceFlag() {
        return consInvoiceFlag;
    }

    public void setConsInvoiceFlag(String consInvoiceFlag) {
        this.consInvoiceFlag = consInvoiceFlag;
    }

    public String getPrebillEndDate() {
        return prebillEndDate;
    }

    public void setPrebillEndDate(String prebillEndDate) {
        this.prebillEndDate = prebillEndDate;
    }

    public BigDecimal getInitialPrebillAmount() {
        return initialPrebillAmount;
    }

    public void setInitialPrebillAmount(BigDecimal initialPrebillAmount) {
        this.initialPrebillAmount = initialPrebillAmount;
    }

    public String getIrrStartDate() {
        return irrStartDate;
    }

    public void setIrrStartDate(String irrStartDate) {
        this.irrStartDate = irrStartDate;
    }

    public String getPrebillNotes() {
        return prebillNotes;
    }

    public void setPrebillNotes(String prebillNotes) {
        this.prebillNotes = prebillNotes;
    }

    public BigDecimal getPrebillNumberOfHours() {
        return prebillNumberOfHours;
    }

    public void setPrebillNumberOfHours(BigDecimal prebillNumberOfHours) {
        this.prebillNumberOfHours = prebillNumberOfHours;
    }

    public String getPrebillWithoutContractorFlg() {
        return prebillWithoutContractorFlg;
    }

    public void setPrebillWithoutContractorFlg(String prebillWithoutContractorFlg) {
        this.prebillWithoutContractorFlg = prebillWithoutContractorFlg;
    }

    public String getPrebillSalesTaxFlag() {
        return prebillSalesTaxFlag;
    }

    public void setPrebillSalesTaxFlag(String prebillSalesTaxFlag) {
        this.prebillSalesTaxFlag = prebillSalesTaxFlag;
    }

    public String getPrebillStartDate() {
        return prebillStartDate;
    }

    public void setPrebillStartDate(String prebillStartDate) {
        this.prebillStartDate = prebillStartDate;
    }

    public String getPrebillTimeframeExceedFlg() {
        return prebillTimeframeExceedFlg;
    }

    public void setPrebillTimeframeExceedFlg(String prebillTimeframeExceedFlg) {
        this.prebillTimeframeExceedFlg = prebillTimeframeExceedFlg;
    }

    public UUID getInvoiceSetupId() {
        return invoiceSetupId;
    }

    public void setInvoiceSetupId(UUID invoiceSetupId) {
        this.invoiceSetupId = invoiceSetupId;
    }

    public AccuringFlag getAccuringFlg() {
        return accuringFlg;
    }

    public void setAccuringFlg(AccuringFlag accuringFlg) {
        this.accuringFlg = accuringFlg;
    }

}
