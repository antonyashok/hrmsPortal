package com.tm.timesheet.timeoff.domain;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "pto_available")
public class PtoAvailable implements Serializable {

	private static final long serialVersionUID = -3996522176189454337L;
	
	

	@Id
	@Column(name = "pto_avl_id", length = 36)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String ptoAvailableId;
	@Column(name = "empl_id")
	private Long employeeId;
	@Column(name = "engmt_id")
	@Type(type = "uuid-char")
	private UUID engagementId;
	@Column(name = "strt_dt")
	private Date startDate;
	@Column(name = "end_dt")
	private Date endDate;
	@Column(name = "alloted")
	private Double allotedHours;
	@Column(name = "draft")
	private Double draftHours;
	@Column(name = "availed")
	private Double availedHours;
	@Column(name = "requested")
	private Double requestedHours;
	@Column(name = "approved")
	private Double approvedHours;
	@Transient
	private Double balanceHours;

	public String getPtoAvailableId() {
		return ptoAvailableId;
	}

	public void setPtoAvailableId(String ptoAvailableId) {
		this.ptoAvailableId = ptoAvailableId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}


	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Double getAllotedHours() {
		return allotedHours;
	}

	public void setAllotedHours(Double allotedHours) {
		this.allotedHours = allotedHours;
	}

	public Double getDraftHours() {
		return draftHours;
	}

	public void setDraftHours(Double draftHours) {
		this.draftHours = draftHours;
	}

	public Double getAvailedHours() {
		return availedHours;
	}

	public void setAvailedHours(Double availedHours) {
		this.availedHours = availedHours;
	}

	public Double getRequestedHours() {
		return requestedHours;
	}

	public void setRequestedHours(Double requestedHours) {
		this.requestedHours = requestedHours;
	}

	public Double getApprovedHours() {
		return approvedHours;
	}

	public void setApprovedHours(Double approvedHours) {
		this.approvedHours = approvedHours;
	}

	public Double getBalanceHours() {
		Double balanceCal = getDraftHours() + getAvailedHours() + getRequestedHours() + getApprovedHours();
		DecimalFormat df = new DecimalFormat("##.##");
		Double balance = Double.valueOf(df.format(getAllotedHours() - balanceCal));
		return balance;
	}

	public void setBalanceHours(Double balanceHours) {
		this.balanceHours = balanceHours;
	}
}
