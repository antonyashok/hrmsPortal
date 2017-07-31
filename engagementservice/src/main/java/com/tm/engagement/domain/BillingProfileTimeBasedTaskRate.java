package com.tm.engagement.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "billprof_time_task_rate_dtl")
public class BillingProfileTimeBasedTaskRate extends AbstractAuditingEntity
        implements Serializable {

    private static final long serialVersionUID = -8665163438487962462L;

    @Id
    @Column(name = "bp_time_task_rate_dtl_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID billingProfileTimeTaskDetailId;

    @Column(name = "bill_to_client_dt_rate")
    private BigDecimal billToClientDTRate;

    @Column(name = "bill_to_client_ot_rate")
    private BigDecimal billToClientOTRate;

    @Column(name = "bill_to_client_st_rate")
    private BigDecimal billToClientSTRate;

    @ManyToOne
    @JoinColumn(name = "empl_bill_profile_id")
    private EmployeeBillingProfile employeeBillingProfile;

    public UUID getBillingProfileTimeTaskDetailId() {
        return billingProfileTimeTaskDetailId;
    }

    public void setBillingProfileTimeTaskDetailId(UUID billingProfileTimeTaskDetailId) {
        this.billingProfileTimeTaskDetailId = billingProfileTimeTaskDetailId;
    }

    public BigDecimal getBillToClientDTRate() {
        return billToClientDTRate;
    }

    public void setBillToClientDTRate(BigDecimal billToClientDTRate) {
        this.billToClientDTRate = billToClientDTRate;
    }

    public BigDecimal getBillToClientOTRate() {
        return billToClientOTRate;
    }

    public void setBillToClientOTRate(BigDecimal billToClientOTRate) {
        this.billToClientOTRate = billToClientOTRate;
    }

    public BigDecimal getBillToClientSTRate() {
        return billToClientSTRate;
    }

    public void setBillToClientSTRate(BigDecimal billToClientSTRate) {
        this.billToClientSTRate = billToClientSTRate;
    }

    public EmployeeBillingProfile getEmployeeBillingProfile() {
        return employeeBillingProfile;
    }

    public void setEmployeeBillingProfile(EmployeeBillingProfile employeeBillingProfile) {
        this.employeeBillingProfile = employeeBillingProfile;
    }

}
