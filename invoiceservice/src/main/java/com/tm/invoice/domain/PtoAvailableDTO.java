package com.tm.invoice.domain;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "PtoAvaliable", collectionRelation = "ptoAvaliable")
public class PtoAvailableDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -3481700213887059631L;

	private String ptoAvaliableId;
	private String employeeName;
	private Long employeeId;
	private String allotedHours="0.0";
	private String availedHours="0.0";
	private String requestedHours="0.0";
	private String approvedHours="0.0";
	private String draftHours="0.0";
	private String balanceHours="0.0";
	private String currentDate;
	private String startDate;
	private String endDate;
	private String reportingManagerName;
	private String engagementId;

	public String getPtoAvaliableId() {
		return ptoAvaliableId;
	}

	public void setPtoAvaliableId(String ptoAvaliableId) {
		this.ptoAvaliableId = ptoAvaliableId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getAllotedHours() {
		return allotedHours;
	}

	public void setAllotedHours(String allotedHours) {
		this.allotedHours = allotedHours;
	}

	public String getAvailedHours() {
		return availedHours;
	}

	public void setAvailedHours(String availedHours) {
		this.availedHours = availedHours;
	}

	public String getRequestedHours() {
		return requestedHours;
	}

	public void setRequestedHours(String requestedHours) {
		this.requestedHours = requestedHours;
	}

	public String getApprovedHours() {
		return approvedHours;
	}

	public void setApprovedHours(String approvedHours) {
		this.approvedHours = approvedHours;
	}

	public String getDraftHours() {
		return draftHours;
	}

	public void setDraftHours(String draftHours) {
		this.draftHours = draftHours;
	}

	public String getBalanceHours() {
		return balanceHours;
	}

	public void setBalanceHours(String balanceHours) {
		this.balanceHours = balanceHours;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getReportingManagerName() {
		return reportingManagerName;
	}

	public void setReportingManagerName(String reportingManagerName) {
		this.reportingManagerName = reportingManagerName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(String engagementId) {
		this.engagementId = engagementId;
	}

}
