package com.tm.timesheet.timeoff.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.tm.timesheet.domain.AuditFields;

@Document(collection = "timeoffRequest")
public class Timeoff implements Serializable {

	private static final long serialVersionUID = -3996522176189454337L;

	@Id
	private UUID id;
	private Long employeeId;
	private String employeeName;
	private Long reportingManagerId;
	private String reportingManagerName;
	private String ptoTypeId;
	private String ptoTypeName;
	private Date startDate;
	private Date endDate;
	private String startDateStr;
	private String endDateStr;
	private String status;
	private Boolean billableIndFlag;
	private Double totalHours;
	private AuditFields created;
	private AuditFields updated;
	private List<TimeoffRequestDetail> ptoRequestDetail;
	private String comments;
	private String lastUpdatedDateStr;
	private UUID engagementId;
	private String engagementName;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getReportingManagerId() {
		return reportingManagerId;
	}

	public void setReportingManagerId(Long reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}

	public String getPtoTypeId() {
		return ptoTypeId;
	}

	public void setPtoTypeId(String ptoTypeId) {
		this.ptoTypeId = ptoTypeId;
	}

	public String getPtoTypeName() {
		return ptoTypeName;
	}

	public void setPtoTypeName(String ptoTypeName) {
		this.ptoTypeName = ptoTypeName;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isBillableIndFlag() {
		return billableIndFlag;
	}

	public void setBillableIndFlag(boolean billableIndFlag) {
		this.billableIndFlag = billableIndFlag;
	}

	public Double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(Double totalHours) {
		this.totalHours = totalHours;
	}

	public AuditFields getCreated() {
		return created;
	}

	public void setCreated(AuditFields created) {
		this.created = created;
	}

	public AuditFields getUpdated() {
		return updated;
	}

	public void setUpdated(AuditFields updated) {
		this.updated = updated;
	}

	public List<TimeoffRequestDetail> getPtoRequestDetail() {
		return ptoRequestDetail;
	}

	public void setPtoRequestDetail(List<TimeoffRequestDetail> ptoRequestDetail) {
		this.ptoRequestDetail = ptoRequestDetail;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getReportingManagerName() {
		return reportingManagerName;
	}

	public void setReportingManagerName(String reportingManagerName) {
		this.reportingManagerName = reportingManagerName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getLastUpdatedDateStr() {
		return lastUpdatedDateStr;
	}

	public void setLastUpdatedDateStr(String lastUpdatedDateStr) {
		this.lastUpdatedDateStr = lastUpdatedDateStr;
	}

	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}

	public String getEngagementName() {
		return engagementName;
	}

	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
	}
	
	
}
