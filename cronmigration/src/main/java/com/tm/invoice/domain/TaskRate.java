package com.tm.invoice.domain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.commonapi.core.audit.mysql.BaseAuditableEntity;

@Entity
@Table(name = "task_rate")
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties({"auditDetails"})
public class TaskRate extends BaseAuditableEntity {

    @Id
    @Column(name = "task_rate_id")
    @Type(type = "uuid-char")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID taskRateId;

    @Column(name = "task_id")
    @Type(type = "uuid-char")
    private UUID taskId;

    @Column(name = "effective_date")
    @NotNull(message = "{effective_date.required.error.message}")
    private Date effectiveDate;

    @Column(name = "bill_Rate_DT")
    private BigDecimal bdt;

    @Column(name = "bill_Rate_OT")
    private BigDecimal bot;

    @Column(name = "bill_Rate_ST")
    private BigDecimal bst;

    @Column(name = "end_Rate_DT")
    private BigDecimal edt;

    @Column(name = "end_Rate_OT")
    private BigDecimal eot;

    @Column(name = "end_Rate_ST")
    private BigDecimal est;

    @Column(name = "bill_to_client_rate")
    private BigDecimal billClientrate;

    @Column(name = "End_client_rate")
    private BigDecimal endClientrate;

    public UUID getTaskRateId() {
        return taskRateId;
    }

    public void setTaskRateId(UUID taskRateId) {
        this.taskRateId = taskRateId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public BigDecimal getBdt() {
        return bdt;
    }

    public void setBdt(BigDecimal bdt) {
        this.bdt = bdt;
    }

    public BigDecimal getBot() {
        return bot;
    }

    public void setBot(BigDecimal bot) {
        this.bot = bot;
    }

    public BigDecimal getBst() {
        return bst;
    }

    public void setBst(BigDecimal bst) {
        this.bst = bst;
    }

    public BigDecimal getEdt() {
        return edt;
    }

    public void setEdt(BigDecimal edt) {
        this.edt = edt;
    }

    public BigDecimal getEot() {
        return eot;
    }

    public void setEot(BigDecimal eot) {
        this.eot = eot;
    }

    public BigDecimal getEst() {
        return est;
    }

    public void setEst(BigDecimal est) {
        this.est = est;
    }

    public BigDecimal getBillClientrate() {
        return billClientrate;
    }

    public void setBillClientrate(BigDecimal billClientrate) {
        this.billClientrate = billClientrate;
    }

    public BigDecimal getEndClientrate() {
        return endClientrate;
    }

    public void setEndClientrate(BigDecimal endClientrate) {
        this.endClientrate = endClientrate;
    }

    public String toFormattedDateRangeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return "[ " + sdf.format(getEffectiveDate()) + " to " + sdf.format(getEffectiveDate())
                + " ]";
    }

}
