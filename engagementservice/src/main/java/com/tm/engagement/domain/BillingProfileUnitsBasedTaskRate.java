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
@Table(name = "billprof_unit_task_rate_dtl")
public class BillingProfileUnitsBasedTaskRate extends AbstractAuditingEntity
        implements Serializable {

    private static final long serialVersionUID = 5089484529864318034L;

    @Id
    @Column(name = "bp_unit_task_rate_dtl_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID billingProfileUnitsTaskRateDatailId;

    @Column(name = "bill_to_client_rate")
    private BigDecimal billToClientRate;

    @ManyToOne
    @JoinColumn(name = "empl_bill_profile_id")
    private EmployeeBillingProfile employeeBillingProfile;

    public UUID getBillingProfileUnitsTaskRateDatailId() {
        return billingProfileUnitsTaskRateDatailId;
    }

    public void setBillingProfileUnitsTaskRateDatailId(UUID billingProfileUnitsTaskRateDatailId) {
        this.billingProfileUnitsTaskRateDatailId = billingProfileUnitsTaskRateDatailId;
    }

    public BigDecimal getBillToClientRate() {
        return billToClientRate;
    }

    public void setBillToClientRate(BigDecimal billToClientRate) {
        this.billToClientRate = billToClientRate;
    }

    public EmployeeBillingProfile getEmployeeBillingProfile() {
        return employeeBillingProfile;
    }

    public void setEmployeeBillingProfile(EmployeeBillingProfile employeeBillingProfile) {
        this.employeeBillingProfile = employeeBillingProfile;
    }

}
