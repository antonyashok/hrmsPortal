package com.tm.timesheet.timeoff.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "pto_hours_view")
public class TimeoffPto implements Serializable {

	private static final long serialVersionUID = -3996522176189454337L;
	
	

	@Id
	@Column(name = "time_rule_id")
	@Type(type = "uuid-char")
	private UUID timeRuleId;
	
	@Column(name = "empl_id")
	private Long employeeId;
	
	@Column(name = "engmt_id")
	@Type(type = "uuid-char")
	private UUID engagementId;
	
	@Column(name = "max_pto_hrs")
	private Double max_pto_hrs;

	public UUID getTimeRuleId() {
		return timeRuleId;
	}

	public void setTimeRuleId(UUID timeRuleId) {
		this.timeRuleId = timeRuleId;
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

	public Double getMax_pto_hrs() {
		return max_pto_hrs;
	}

	public void setMax_pto_hrs(Double max_pto_hrs) {
		this.max_pto_hrs = max_pto_hrs;
	}
	

	
	
}
