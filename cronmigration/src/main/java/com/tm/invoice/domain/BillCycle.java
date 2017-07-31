package com.tm.invoice.domain;

import java.io.Serializable;
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

@Entity
@Table(name = "bill_cycle")
public class BillCycle implements Serializable {

	public enum AccuringFlag {
		Y, N
	}

	private static final long serialVersionUID = -2881877898927822581L;

	@Id
	@Column(name = "bill_cyl_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long billCycleId;

	@Column(name = "bill_cycle_nm")
	private String billCycleName;

	@Column(name = "mature_day")
	private String matureDay;

	@Column(name = "mature_dt")
	private Date matureDate;

	@Column(name = "milestone_amt")
	private BigDecimal milestoneAmount;

	@Column(name = "milestone_dt")
	private Date milestoneDate;

	@Column(name = "inv_setup_id")
	@Type(type = "uuid-char")
	private UUID invoiceSetupId;

	@Column(name = "accruing_flg")

	@Enumerated(EnumType.STRING)
	private AccuringFlag accuringFlg;

	@Transient
	private List<UUID> invoiceSetupIds;

	@Transient
	private List<Long> validCycleIds;

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

	public Date getMatureDate() {
		return matureDate;
	}

	public void setMatureDate(Date matureDate) {
		this.matureDate = matureDate;
	}

	public BigDecimal getMilestoneAmount() {
		return milestoneAmount;
	}

	public void setMilestoneAmount(BigDecimal milestoneAmount) {
		this.milestoneAmount = milestoneAmount;
	}

	public Date getMilestoneDate() {
		return milestoneDate;
	}

	public void setMilestoneDate(Date milestoneDate) {
		this.milestoneDate = milestoneDate;
	}

	public UUID getInvoiceSetupId() {
		return invoiceSetupId;
	}

	public void setInvoiceSetupId(UUID invoiceSetupId) {
		this.invoiceSetupId = invoiceSetupId;
	}

	public List<UUID> getInvoiceSetupIds() {
		return invoiceSetupIds;
	}

	public void setInvoiceSetupIds(List<UUID> invoiceSetupIds) {
		this.invoiceSetupIds = invoiceSetupIds;
	}

	public AccuringFlag getAccuringFlg() {
		return accuringFlg;
	}

	public void setAccuringFlg(AccuringFlag accuringFlg) {
		this.accuringFlg = accuringFlg;
	}

	public List<Long> getValidCycleIds() {
		return validCycleIds;
	}

	public void setValidCycleIds(List<Long> validCycleIds) {
		this.validCycleIds = validCycleIds;
	}

}