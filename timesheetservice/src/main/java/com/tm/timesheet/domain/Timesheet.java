/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.domain.Timehsheet.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "timesheet")
public class Timesheet implements Serializable {

	private static final long serialVersionUID = 1147429355103276902L;

	@Id
	private UUID id;
	private Employee employee;
	private Engagement engagement;
	private Date startDate;
	private Date endDate;
	private String status;
	private Double totalHours;
	private Double ptoHours;
	private Double workHours;
	private Double leaveHours;
	private LookUpType lookupType;
	private AuditFields created;
	private AuditFields updated;
	private AuditFields submitted;
	private AuditFields approved;
	private Double stHours;
	private Double otHours;
	private Double dtHours;
	private Integer units;
	private String source;
	private Date billStartDate;
	private Date billEndDate;
	private String designation;

	public List<StatusDetails> getStatusDetails() {
		return statusDetails;
	}

	public void setStatusDetails(List<StatusDetails> statusDetails) {
		this.statusDetails = statusDetails;
	}

	private UUID configGroupId;
	private UUID timeRuleId;

	private SearchField searchField;
	private List<StatusDetails> statusDetails;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Engagement getEngagement() {
		return engagement;
	}

	public void setEngagement(Engagement engagement) {
		this.engagement = engagement;
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

	public Double getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(Double totalHours) {
		this.totalHours = totalHours;
	}

	public Double getPtoHours() {
		return ptoHours;
	}

	public void setPtoHours(Double ptoHours) {
		this.ptoHours = ptoHours;
	}

	public Double getWorkHours() {
		return workHours;
	}

	public void setWorkHours(Double workHours) {
		this.workHours = workHours;
	}

	public Double getLeaveHours() {
		return leaveHours;
	}

	public void setLeaveHours(Double leaveHours) {
		this.leaveHours = leaveHours;
	}

	public AuditFields getUpdated() {
		return updated;
	}

	public void setUpdated(AuditFields updated) {
		this.updated = updated;
	}

	public AuditFields getSubmitted() {
		return submitted;
	}

	public void setSubmitted(AuditFields submitted) {
		this.submitted = submitted;
	}

	public AuditFields getApproved() {
		return approved;
	}

	public void setApproved(AuditFields approved) {
		this.approved = approved;
	}

	public AuditFields getCreated() {
		return created;
	}

	public void setCreated(AuditFields created) {
		this.created = created;
	}

	public Double getStHours() {
		return stHours;
	}

	public void setStHours(Double stHours) {
		this.stHours = stHours;
	}

	public Double getOtHours() {
		return otHours;
	}

	public void setOtHours(Double otHours) {
		this.otHours = otHours;
	}

	public Double getDtHours() {
		return dtHours;
	}

	public void setDtHours(Double dtHours) {
		this.dtHours = dtHours;
	}

	public UUID getConfigGroupId() {
		return configGroupId;
	}

	public void setConfigGroupId(UUID configGroupId) {
		this.configGroupId = configGroupId;
	}

	public UUID getTimeRuleId() {
		return timeRuleId;
	}

	public void setTimeRuleId(UUID timeRuleId) {
		this.timeRuleId = timeRuleId;
	}

	public LookUpType getLookupType() {
		return lookupType;
	}

	public void setLookupType(LookUpType lookupType) {
		this.lookupType = lookupType;
	}

	public SearchField getSearchField() {
		return searchField;
	}

	public void setSearchField(SearchField searchField) {
		this.searchField = searchField;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getUnits() {
		return units;
	}

	public void setUnits(Integer units) {
		this.units = units;
	}

	public Date getBillStartDate() {
		return billStartDate;
	}

	public void setBillStartDate(Date billStartDate) {
		this.billStartDate = billStartDate;
	}

	public Date getBillEndDate() {
		return billEndDate;
	}

	public void setBillEndDate(Date billEndDate) {
		this.billEndDate = billEndDate;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

}