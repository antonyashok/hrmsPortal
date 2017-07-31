package com.tm.invoice.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Future;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import com.tm.commonapi.core.audit.mysql.BaseAuditableEntity;

public class InvoiceType extends BaseAuditableEntity {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String irrMatureDate;

    private String irrMonth;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String irrStartDate;

    private BigDecimal mileStoneAccureAmount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String mileStoneFixedAmountDate;

    private BigDecimal mileStoneFixedAmount;

    @Past
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "MM/dd/yyyy")
    private String preBillStartDate;

    @Future
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "MM/dd/yyyy")
    private String preBillEndDate;

    private String preBillHours;

    private BigDecimal preBillAmount;

    private BigDecimal initialPreBillAmount;

    public enum preBillTimeframe {
        Y, N
    }

    public enum preBillSalesTax {
        Y, N
    }

    public enum prebillOnceTimeFrameExceeded {
        Y, N
    }

    public enum preBillWithoutContractors {
        Y, N
    }

    private String preBillRateNotes;

    public String getIrrMatureDate() {
        return irrMatureDate;
    }

    public String getIrrMonth() {
        return irrMonth;
    }

    public String getIrrStartDate() {
        return irrStartDate;
    }

    public BigDecimal getMileStoneAccureAmount() {
        return mileStoneAccureAmount;
    }

    public String getMileStoneFixedAmountDate() {
        return mileStoneFixedAmountDate;
    }

    public BigDecimal getMileStoneFixedAmount() {
        return mileStoneFixedAmount;
    }

    public String getPreBillStartDate() {
        return preBillStartDate;
    }

    public String getPreBillEndDate() {
        return preBillEndDate;
    }

    public String getPreBillHours() {
        return preBillHours;
    }

    public BigDecimal getPreBillAmount() {
        return preBillAmount;
    }

    public BigDecimal getInitialPreBillAmount() {
        return initialPreBillAmount;
    }

    public String getPreBillRateNotes() {
        return preBillRateNotes;
    }

    public void setIrrMatureDate(String irrMatureDate) {
        this.irrMatureDate = irrMatureDate;
    }

    public void setIrrMonth(String irrMonth) {
        this.irrMonth = irrMonth;
    }

    public void setIrrStartDate(String irrStartDate) {
        this.irrStartDate = irrStartDate;
    }

    public void setMileStoneAccureAmount(BigDecimal mileStoneAccureAmount) {
        this.mileStoneAccureAmount = mileStoneAccureAmount;
    }

    public void setMileStoneFixedAmountDate(String mileStoneFixedAmountDate) {
        this.mileStoneFixedAmountDate = mileStoneFixedAmountDate;
    }

    public void setMileStoneFixedAmount(BigDecimal mileStoneFixedAmount) {
        this.mileStoneFixedAmount = mileStoneFixedAmount;
    }

    public void setPreBillStartDate(String preBillStartDate) {
        this.preBillStartDate = preBillStartDate;
    }

    public void setPreBillEndDate(String preBillEndDate) {
        this.preBillEndDate = preBillEndDate;
    }

    public void setPreBillHours(String preBillHours) {
        this.preBillHours = preBillHours;
    }

    public void setPreBillAmount(BigDecimal preBillAmount) {
        this.preBillAmount = preBillAmount;
    }

    public void setInitialPreBillAmount(BigDecimal initialPreBillAmount) {
        this.initialPreBillAmount = initialPreBillAmount;
    }

    public void setPreBillRateNotes(String preBillRateNotes) {
        this.preBillRateNotes = preBillRateNotes;
    }

}
