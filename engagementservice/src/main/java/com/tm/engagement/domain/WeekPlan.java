package com.tm.engagement.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "week_plan")
public class WeekPlan extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = -7491152705529981762L;

	@Id
	@Column(name = "wk_pln_id")
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID weekPlanId;

	@Column(name = "actv_flg")
	private String activeFlag;

	@Column(name = "end_day")
	private String endDay;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time")
	private Date endTime;

	@Column(name = "no_hours")
	private Double noHours;

	@Column(name = "start_day")
	private String startDay;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time")
	private Date startTime;

	@Column(name = "wk_pln_desc")
	private String weekPlanDescription;

	@Column(name = "wk_pln_nm")
	private String weekPlanName;

	public UUID getWeekPlanId() {
		return weekPlanId;
	}

	public void setWeekPlanId(UUID weekPlanId) {
		this.weekPlanId = weekPlanId;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getEndDay() {
		return endDay;
	}

	public void setEndDay(String endDay) {
		this.endDay = endDay;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public double getNoHours() {
		return noHours;
	}

	public void setNoHours(double noHours) {
		this.noHours = noHours;
	}

	public String getStartDay() {
		return startDay;
	}

	public void setStartDay(String startDay) {
		this.startDay = startDay;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getWeekPlanDescription() {
		return weekPlanDescription;
	}

	public void setWeekPlanDescription(String weekPlanDescription) {
		this.weekPlanDescription = weekPlanDescription;
	}

	public String getWeekPlanName() {
		return weekPlanName;
	}

	public void setWeekPlanName(String weekPlanName) {
		this.weekPlanName = weekPlanName;
	}

}
