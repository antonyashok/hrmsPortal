/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.service.dto.CommonTimesheetDTO.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tm.timesheet.timeoff.service.dto.TimeoffDTO;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonTimesheetDTO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = -2267206880645715813L;

    private UUID timesheetId;
    private EmployeeDTO employee;
    private EngagementDTO engagement;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String startDate;
    private String endDate;
    private String status;
    private Double totalHours;
    private Double totalUnits;
    private Boolean paidStatus = Boolean.FALSE;
    private Double ptoHours;
    private Double nonBillablePTO;
    private Double workHours;
    private Double leaveHours;
    private Double stHours;
    private Double otHours;
    private Double dtHours;
    private String configGroupId;
    private String timeRuleId;
    private Double holidayHours;
    private AuditFieldsDTO created;
    private AuditFieldsDTO updated;
    private AuditFieldsDTO approved;
    private AuditFieldsDTO submitted;
    private String source;
    private String period;
    private String dateRange;
	private LookUpTypeDTO lookupType;
	private List<StatusDetailDTO> statusDetail;
	private List<NoteDTO> notesDetail;
	private List<TimesheetDaysDTO> days;
	private List<TimesheetTaskDTO> taskDetails;
	private List<TimeoffDTO> timeoffDTOList;
	private List<TimesheetDTO> timesheetList;
	private String timesheetType ="";

    public UUID getTimesheetId() {
        return timesheetId;
    }

    public void setTimesheetId(UUID timesheetId) {
        this.timesheetId = timesheetId;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public EngagementDTO getEngagement() {
        return engagement;
    }

    public void setEngagement(EngagementDTO engagement) {
        this.engagement = engagement;
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

    public Double getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(Double totalUnits) {
        this.totalUnits = totalUnits;
    }

    public Boolean getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(Boolean paidStatus) {
        this.paidStatus = paidStatus;
    }

    public Double getPtoHours() {
        return ptoHours;
    }

    public void setPtoHours(Double ptoHours) {
        this.ptoHours = ptoHours;
    }

    public Double getNonBillablePTO() {
        return nonBillablePTO;
    }

    public void setNonBillablePTO(Double nonBillablePTO) {
        this.nonBillablePTO = nonBillablePTO;
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

    public String getConfigGroupId() {
        return configGroupId;
    }

    public void setConfigGroupId(String configGroupId) {
        this.configGroupId = configGroupId;
    }
    
    public String getTimeRuleId() {
		return timeRuleId;
	}

	public void setTimeRuleId(String timeRuleId) {
		this.timeRuleId = timeRuleId;
	}

	public Double getHolidayHours() {
        return holidayHours;
    }

    public void setHolidayHours(Double holidayHours) {
        this.holidayHours = holidayHours;
    }

    public AuditFieldsDTO getCreated() {
        return created;
    }

    public void setCreated(AuditFieldsDTO created) {
        this.created = created;
    }

    public AuditFieldsDTO getUpdated() {
        return updated;
    }

    public void setUpdated(AuditFieldsDTO updated) {
        this.updated = updated;
    }

    public AuditFieldsDTO getApproved() {
        return approved;
    }

    public void setApproved(AuditFieldsDTO approved) {
        this.approved = approved;
    }

    public AuditFieldsDTO getSubmitted() {
        return submitted;
    }

    public void setSubmitted(AuditFieldsDTO submitted) {
        this.submitted = submitted;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

	public LookUpTypeDTO getLookupType() {
		return lookupType;
	}

	public void setLookupType(LookUpTypeDTO lookupType) {
		this.lookupType = lookupType;
	}

	public List<StatusDetailDTO> getStatusDetail() {
		return statusDetail;
	}

	public void setStatusDetail(List<StatusDetailDTO> statusDetail) {
		this.statusDetail = statusDetail;
	}

	public List<NoteDTO> getNotesDetail() {
		return notesDetail;
	}

	public void setNotesDetail(List<NoteDTO> notesDetail) {
		this.notesDetail = notesDetail;
	}

	public List<TimesheetDaysDTO> getDays() {
		return days;
	}

	public void setDays(List<TimesheetDaysDTO> days) {
		this.days = days;
	}

	public List<TimesheetTaskDTO> getTaskDetails() {
		return taskDetails;
	}

	public void setTaskDetails(List<TimesheetTaskDTO> taskDetails) {
		this.taskDetails = taskDetails;
	}

	public List<TimeoffDTO> getTimeoffDTOList() {
		return timeoffDTOList;
	}

	public void setTimeoffDTOList(List<TimeoffDTO> timeoffDTOList) {
		this.timeoffDTOList = timeoffDTOList;
	}

	public List<TimesheetDTO> getTimesheetList() {
		return timesheetList;
	}

	public void setTimesheetList(List<TimesheetDTO> timesheetList) {
		this.timesheetList = timesheetList;
	}

	public String getTimesheetType() {
		return timesheetType;
	}

	public void setTimesheetType(String timesheetType) {
		this.timesheetType = timesheetType;
	}

	
	
}
