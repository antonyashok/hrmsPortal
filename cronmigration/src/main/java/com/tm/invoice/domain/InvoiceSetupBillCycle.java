package com.tm.invoice.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "invoice_setup_bill_cycle")
@JsonInclude(value = Include.NON_NULL)
public class InvoiceSetupBillCycle extends AbstractAuditingEntity {

    private static final long serialVersionUID = 8553956518349084317L;

    public enum TimeFrameExceedFlg {
        Y, N
    }
    public enum PrebillWithoutContractorFlg {
        Y, N
    }

    public enum SalesTaxFlag {
        Y, N
    }
    public enum ConsInvoiceFlag {
        Y, N
    }

    public enum AccuringFlag {
        Y, N
    }

    @Id
    @Column(name = "inv_setup_bill_cycle_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceSetupBillCycleId;

    @Column(name = "amount")
    private BigDecimal prebillAmount;

    @Column(name = "amount_remaining")
    private BigDecimal amountRemaining;

    @Column(name = "amount_used")
    private BigDecimal amountUsed;

    @Column(name = "bill_cycle_day")
    private String billCycleDay;

    @Column(name = "bill_cyl_nm")
    private String billCycleName;

    @Column(name = "inv_typ")
    private String invoiceType;

    @Column(name = "bill_cycle_strt_end_det")
    private String billCycleStartEndDetail;

    @Column(name = "cons_inv_flg")
    @Enumerated(EnumType.STRING)
    private ConsInvoiceFlag consInvoiceFlag;
    
    @Column(name = "accruing_flg")
    @Enumerated(EnumType.STRING)
    private AccuringFlag accuringFlg;

    @Column(name = "end_dt")
    private Date prebillEndDate;

    @Column(name = "initial_prebill_amt")
    private BigDecimal initialPrebillAmount;

    @Column(name = "irr_strt_dt")
    private Date irrStartDate;

    @Column(name = "notes")
    private String prebillNotes;

    @Column(name = "number_of_hrs")
    private BigDecimal prebillNumberOfHours;

    @Column(name = "prebill_without_contractor_flg")
    @Enumerated(EnumType.STRING)
    private PrebillWithoutContractorFlg prebillWithoutContractorFlg;

    @Column(name = "sales_tax_flg")
    @Enumerated(EnumType.STRING)
    private SalesTaxFlag prebillSalesTaxFlag;

    @Column(name = "strt_dt")
    private Date prebillStartDate;

    @Column(name = "timeframe_exceed_flg")
    @Enumerated(EnumType.STRING)
    private TimeFrameExceedFlg prebillTimeframeExceedFlg;

    @Column(name = "inv_setup_id")
    @Type(type = "uuid-char")
    private UUID invoiceSetupId;
    
    @Transient
    private List<UUID> invoiceSetupIdsForReugularAndPrebill;

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

    public ConsInvoiceFlag getConsInvoiceFlag() {
        return consInvoiceFlag;
    }

    public void setConsInvoiceFlag(ConsInvoiceFlag consInvoiceFlag) {
        this.consInvoiceFlag = consInvoiceFlag;
    }

    public Date getPrebillEndDate() {
        return prebillEndDate;
    }

    public void setPrebillEndDate(Date prebillEndDate) {
        this.prebillEndDate = prebillEndDate;
    }

    public BigDecimal getInitialPrebillAmount() {
        return initialPrebillAmount;
    }

    public void setInitialPrebillAmount(BigDecimal initialPrebillAmount) {
        this.initialPrebillAmount = initialPrebillAmount;
    }

    public Date getIrrStartDate() {
        return irrStartDate;
    }

    public void setIrrStartDate(Date irrStartDate) {
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

    public PrebillWithoutContractorFlg getPrebillWithoutContractorFlg() {
        return prebillWithoutContractorFlg;
    }

    public void setPrebillWithoutContractorFlg(
            PrebillWithoutContractorFlg prebillWithoutContractorFlg) {
        this.prebillWithoutContractorFlg = prebillWithoutContractorFlg;
    }

    public SalesTaxFlag getPrebillSalesTaxFlag() {
        return prebillSalesTaxFlag;
    }

    public void setPrebillSalesTaxFlag(SalesTaxFlag prebillSalesTaxFlag) {
        this.prebillSalesTaxFlag = prebillSalesTaxFlag;
    }

    public Date getPrebillStartDate() {
        return prebillStartDate;
    }

    public void setPrebillStartDate(Date prebillStartDate) {
        this.prebillStartDate = prebillStartDate;
    }

    public TimeFrameExceedFlg getPrebillTimeframeExceedFlg() {
        return prebillTimeframeExceedFlg;
    }

    public void setPrebillTimeframeExceedFlg(TimeFrameExceedFlg prebillTimeframeExceedFlg) {
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

    public List<UUID> getInvoiceSetupIdsForReugularAndPrebill() {
        return invoiceSetupIdsForReugularAndPrebill;
    }

    public void setInvoiceSetupIdsForReugularAndPrebill(
            List<UUID> invoiceSetupIdsForReugularAndPrebill) {
        this.invoiceSetupIdsForReugularAndPrebill = invoiceSetupIdsForReugularAndPrebill;
    }

}
