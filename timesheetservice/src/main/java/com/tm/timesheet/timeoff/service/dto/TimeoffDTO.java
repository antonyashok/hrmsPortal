package com.tm.timesheet.timeoff.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.timesheet.domain.AuditFields;

@JsonInclude(Include.NON_NULL)
public class TimeoffDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = -3996522176189454337L;

	private static final String PTO_TYPE_ID_IS_REQUIRED = "PTO Type Id is Required";
	private static final String TYPE_IS_REQUIRED = "Type is Required";
	private static final String FROM_DATE_IS_REQUIRED = "From Date is Required";
	private static final String TO_DATE_IS_REQUIRED = "To Date is Required";
	private static final String COMMENTS_ALLOWED_300_CHARACTERS = "Comments allowed 300 characters only";
	private static final String TIMEOFF_REQUEST_DETAILS_IS_REQUIRED = "Time off request details is required";
	private static final String UI_REQUEST_DATE_FORMAT = "MM/dd/yyyy";

	private UUID timeoffId;
	private String employeeId;
	private String employeeName;
	private String primaryEmailId;
	private String reportingManagerId;
	private String reportingManagerName;
	private String reportingManagerEmailId;
	@NotBlank(message = PTO_TYPE_ID_IS_REQUIRED)
	private String ptoTypeId;
	@NotBlank(message = TYPE_IS_REQUIRED)
	private String ptoTypeName;
	@NotBlank(message = FROM_DATE_IS_REQUIRED)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = UI_REQUEST_DATE_FORMAT)
	private String startDate;
	@NotBlank(message = TO_DATE_IS_REQUIRED)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = UI_REQUEST_DATE_FORMAT)
	private String endDate;
	private String status;
	private String totalHours;
	@Size(max = 300, message = COMMENTS_ALLOWED_300_CHARACTERS)
	private String comments;
	private String lastUpdated;
	private AuditFields created;
	private AuditFields updated;
	@Valid
	@NotNull(message = TIMEOFF_REQUEST_DETAILS_IS_REQUIRED)
	private List<TimeoffRequestDetailDTO> ptoRequestDetailDTO;
	private List<TimeoffActivityLogDTO> timeoffActivityLogDTO;
	private List<String> timeoffIds;
	private String timesheetId;
	private UUID engagementId;
	private String engagementName;
	//private Double maxHours;

	public List<String> getTimeoffIds() {
		return timeoffIds;
	}

	public void setTimeoffIds(List<String> timeoffIds) {
		this.timeoffIds = timeoffIds;
	}

	public String getPtoTypeName() {
		return ptoTypeName;
	}

	public void setPtoTypeName(String ptoTypeName) {
		this.ptoTypeName = ptoTypeName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(String totalHours) {
		this.totalHours = totalHours;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getReportingManagerId() {
		return reportingManagerId;
	}

	public void setReportingManagerId(String reportingManagerId) {
		this.reportingManagerId = reportingManagerId;
	}

	public String getReportingManagerName() {
		return reportingManagerName;
	}

	public void setReportingManagerName(String reportingManagerName) {
		this.reportingManagerName = reportingManagerName;
	}

	public String getPtoTypeId() {
		return ptoTypeId;
	}

	public void setPtoTypeId(String ptoTypeId) {
		this.ptoTypeId = ptoTypeId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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

	public List<TimeoffRequestDetailDTO> getPtoRequestDetailDTO() {
		return ptoRequestDetailDTO;
	}

	public void setPtoRequestDetailDTO(List<TimeoffRequestDetailDTO> ptoRequestDetailDTO) {
		this.ptoRequestDetailDTO = ptoRequestDetailDTO;
	}

	public String getPrimaryEmailId() {
		return primaryEmailId;
	}

	public void setPrimaryEmailId(String primaryEmailId) {
		this.primaryEmailId = primaryEmailId;
	}

	public String getReportingManagerEmailId() {
		return reportingManagerEmailId;
	}

	public void setReportingManagerEmailId(String reportingManagerEmailId) {
		this.reportingManagerEmailId = reportingManagerEmailId;
	}

	public UUID getTimeoffId() {
		return timeoffId;
	}

	public void setTimeoffId(UUID timeoffId) {
		this.timeoffId = timeoffId;
	}

	public List<TimeoffActivityLogDTO> getTimeoffActivityLogDTO() {
		return timeoffActivityLogDTO;
	}

	public void setTimeoffActivityLogDTO(List<TimeoffActivityLogDTO> timeoffActivityLogDTO) {
		this.timeoffActivityLogDTO = timeoffActivityLogDTO;
	}

	public String getTimesheetId() {
		return timesheetId;
	}

	public void setTimesheetId(String timesheetId) {
		this.timesheetId = timesheetId;
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

	/*public Double getMaxHours() {
		return maxHours;
	}

	public void setMaxHours(Double maxHours) {
		this.maxHours = maxHours;
	}*/

	
}
