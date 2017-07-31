package com.tm.invoice.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tm.invoice.domain.BillCycle.AccuringFlag;

@Relation(value = "BillCycle", collectionRelation = "BillCycles")
public class BillCycleDTO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = 7798348751734603495L;

    private Long billCycleId;

    private String billCycleName;

    private String matureDay;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private String matureDate;

    private BigDecimal milestoneAmount;
    
    @Type(type = "uuid-char")   
    private UUID invoiceSetupId;

    @JsonFormat(pattern = "MM/dd/yyyy")
    private String milestoneDate;
    
    @Enumerated(EnumType.STRING)
    private AccuringFlag accuringFlg;

    public Long getBillCycleId() {
        return billCycleId;
    }

    public void setBillCycleId(Long billCycleId) {
        this.billCycleId = billCycleId;
    }

    public String getBillCycleName() {
        return billCycleName;
    }

    public void setBillCycleName(String billCycleName) {
        this.billCycleName = billCycleName;
    }

    public String getMatureDay() {
        return matureDay;
    }

    public void setMatureDay(String matureDay) {
        this.matureDay = matureDay;
    }

    public String getMatureDate() {
        return matureDate;
    }

    public void setMatureDate(String matureDate) {
        this.matureDate = matureDate;
    }

    public BigDecimal getMilestoneAmount() {
        return milestoneAmount;
    }

    public void setMilestoneAmount(BigDecimal milestoneAmount) {
        this.milestoneAmount = milestoneAmount;
    }

    public String getMilestoneDate() {
        return milestoneDate;
    }

    public void setMilestoneDate(String milestoneDate) {
        this.milestoneDate = milestoneDate;
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
