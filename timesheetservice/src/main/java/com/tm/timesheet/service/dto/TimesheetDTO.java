package com.tm.timesheet.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.timesheet.domain.LookUpType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "Timesheet", collectionRelation = "Timesheets")
public class TimesheetDTO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = -2267206880645715813L;

    private UUID timesheetId;
    private EmployeeDTO employee;
    private EngagementDTO engagement;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String startDate;
    private String endDate;
    private String status;
    private String totalHours;
    private Double totalUnits;
    private Boolean paidStatus = Boolean.FALSE;
    private String ptoHours;
    private Double nonBillablePTO;
    private String workHours;
    private String leaveHours;
    private String timeOffHours;
    private String stHours;
    private String otHours;
    private String dtHours;
    private UUID configGroupId;
    private Double holidayHours;
	private List<TimesheetDaysDTO> days;
	private List<TimesheetTaskDTO> taskDetails;
	private LookUpType lookupType;
    private AuditFieldsDTO created;
    private AuditFieldsDTO updated;
    private AuditFieldsDTO approved;
    private AuditFieldsDTO submitted;
    private String source;
    private String period;
    private String dateRange;
    private String formattedStartDate;
    private String formattedEndDate;
    private boolean previousTimeSheetFlag;
    private UUID previousTimesheetId;
    private boolean nextTimeSheetFlag;
    private UUID nextTimesheetId;  
    private String previousTimesheetLookUpValue;
    private String nextTimesheetLookUpValue;   
    private String previousStartDate;
    private String previousEndDate;
    private String nextStartDate;
    private String nextEndDate;
    private String previousEngagementId;
    private String nextEngagementId;
       

    public String getPreviousEngagementId() {
        return previousEngagementId;
    }

    public void setPreviousEngagementId(String previousEngagementId) {
        this.previousEngagementId = previousEngagementId;
    }

    public String getNextEngagementId() {
        return nextEngagementId;
    }

    public void setNextEngagementId(String nextEngagementId) {
        this.nextEngagementId = nextEngagementId;
    }

    public String getPreviousStartDate() {
        return previousStartDate;
    }

    public void setPreviousStartDate(String previousStartDate) {
        this.previousStartDate = previousStartDate;
    }

    public String getPreviousEndDate() {
        return previousEndDate;
    }

    public void setPreviousEndDate(String previousEndDate) {
        this.previousEndDate = previousEndDate;
    }

    public String getNextStartDate() {
        return nextStartDate;
    }

    public void setNextStartDate(String nextStartDate) {
        this.nextStartDate = nextStartDate;
    }

    public String getNextEndDate() {
        return nextEndDate;
    }

    public void setNextEndDate(String nextEndDate) {
        this.nextEndDate = nextEndDate;
    }

    public String getPreviousTimesheetLookUpValue() {
        return previousTimesheetLookUpValue;
    }

    public void setPreviousTimesheetLookUpValue(String previousTimesheetLookUpValue) {
        this.previousTimesheetLookUpValue = previousTimesheetLookUpValue;
    }

    public String getNextTimesheetLookUpValue() {
        return nextTimesheetLookUpValue;
    }

    public void setNextTimesheetLookUpValue(String nextTimesheetLookUpValue) {
        this.nextTimesheetLookUpValue = nextTimesheetLookUpValue;
    }

    public boolean isPreviousTimeSheetFlag() {
        return previousTimeSheetFlag;
    }

    public void setPreviousTimeSheetFlag(boolean previousTimeSheetFlag) {
        this.previousTimeSheetFlag = previousTimeSheetFlag;
    }

    public boolean isNextTimeSheetFlag() {
        return nextTimeSheetFlag;
    }

    public void setNextTimeSheetFlag(boolean nextTimeSheetFlag) {
        this.nextTimeSheetFlag = nextTimeSheetFlag;
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

    public Double getNonBillablePTO() {
        return nonBillablePTO;
    }

    public void setNonBillablePTO(Double nonBillablePTO) {
        this.nonBillablePTO = nonBillablePTO;
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

	public LookUpType getLookupType() {
		return lookupType;
	}

	public void setLookupType(LookUpType lookupType) {
		this.lookupType = lookupType;
	}

	public String getStHours() {
		return stHours;
	}

	public void setStHours(String stHours) {
		this.stHours = CommonUtils.roundOfValue(stHours);
	}

	public String getOtHours() {
		return otHours;
	}

	public void setOtHours(String otHours) {
		this.otHours = CommonUtils.roundOfValue(otHours);
	}

	public String getDtHours() {
		return dtHours;
	}

	public void setDtHours(String dtHours) {
		this.dtHours = CommonUtils.roundOfValue(dtHours);
	}

	public String getFormattedStartDate() {
		return formattedStartDate;
	}

	public void setFormattedStartDate(String formattedStartDate) {
		this.formattedStartDate = formattedStartDate;
	}

	public String getFormattedEndDate() {
		return formattedEndDate;
	}

	public void setFormattedEndDate(String formattedEndDate) {
		this.formattedEndDate = formattedEndDate;
	}

	public UUID getTimesheetId() {
		return timesheetId;
	}

	public void setTimesheetId(UUID timesheetId) {
		this.timesheetId = timesheetId;
	}

	public UUID getPreviousTimesheetId() {
		return previousTimesheetId;
	}

	public void setPreviousTimesheetId(UUID previousTimesheetId) {
		this.previousTimesheetId = previousTimesheetId;
	}

	public UUID getNextTimesheetId() {
		return nextTimesheetId;
	}

	public void setNextTimesheetId(UUID nextTimesheetId) {
		this.nextTimesheetId = nextTimesheetId;
	}

	public UUID getConfigGroupId() {
		return configGroupId;
	}

	public void setConfigGroupId(UUID configGroupId) {
		this.configGroupId = configGroupId;
	}

	public String getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(String totalHours) {
		this.totalHours = CommonUtils.roundOfValue(totalHours);
	}

	public String getWorkHours() {
		return workHours;
	}

	public void setWorkHours(String workHours) {
		this.workHours = CommonUtils.roundOfValue(workHours);
	}

	public String getLeaveHours() {
		return leaveHours;
	}

	public void setLeaveHours(String leaveHours) {
		this.leaveHours = CommonUtils.roundOfValue(leaveHours);
	}

	public String getPtoHours() {
		return ptoHours;
	}

	public void setPtoHours(String ptoHours) {
		this.ptoHours = CommonUtils.roundOfValue(ptoHours);
	}

	public String getTimeOffHours() {
		return timeOffHours;
	}

	public void setTimeOffHours(String timeOffHours) {
		this.timeOffHours = timeOffHours;
	}
	
	
	
}
