package com.tm.timesheet.service.dto;

import java.util.List;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

@JsonInclude(value = Include.NON_EMPTY)
public class TimesheetResourceDTO extends ResourceSupport{

	private UUID timesheetId;
	private EmployeeDTO employee;
	private String startDate;
	private String endDate;
	private String status;
	private String stHours;
	private String otHours;
	private String dtHours;
	private String totalHours;
	private String ptoHours;
	private String timeOffHours;
	private LookUpTypeDTO lookupType;
	private UUID configGroupId;
	private String source;
	private List<StatusDetailDTO> statusDetail;
	private List<TimesheetDaysDTO> days;
	private List<TimesheetTaskDTO> timesheetsheetDetials;
	private EngagementDTO engagement;
	private String period;
	private String dateRange;
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

    private String nextEngagementId;

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

	@JsonUnwrapped
	private Resources<EmbeddedWrapper> embeddedResource;
	
    public EmployeeDTO getEmployee() {
		return employee;
	}
	public void setEmployee(EmployeeDTO employee) {
		this.employee = employee;
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
	public LookUpTypeDTO getLookupType() {
		return lookupType;
	}
	public void setLookupType(LookUpTypeDTO lookupType) {
		this.lookupType = lookupType;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public List<StatusDetailDTO> getStatusDetail() {
		return statusDetail;
	}
	public void setStatusDetail(List<StatusDetailDTO> statusDetail) {
		this.statusDetail = statusDetail;
	}
	public List<TimesheetDaysDTO> getDays() {
		return days;
	}
	public void setDays(List<TimesheetDaysDTO> days) {
		this.days = days;
	}
	public List<TimesheetTaskDTO> getTimesheetsheetDetials() {
		return timesheetsheetDetials;
	}
	public void setTimesheetsheetDetials(List<TimesheetTaskDTO> timesheetsheetDetials) {
		this.timesheetsheetDetials = timesheetsheetDetials;
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
	public Resources<EmbeddedWrapper> getEmbeddedResource() {
		return embeddedResource;
	}
	public void setEmbeddedResource(Resources<EmbeddedWrapper> embeddedResource) {
		this.embeddedResource = embeddedResource;
	}
	public EngagementDTO getEngagement() {
		return engagement;
	}
	public void setEngagement(EngagementDTO engagement) {
		this.engagement = engagement;
	}
	public String getStHours() {
		return stHours;
	}
	public void setStHours(String stHours) {
		this.stHours = stHours;
	}
	public String getOtHours() {
		return otHours;
	}
	public void setOtHours(String otHours) {
		this.otHours = otHours;
	}
	public String getDtHours() {
		return dtHours;
	}
	public void setDtHours(String dtHours) {
		this.dtHours = dtHours;
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
		this.totalHours = totalHours;
	}

	public String getPtoHours() {
		return ptoHours;
	}

	public void setPtoHours(String ptoHours) {
		this.ptoHours = ptoHours;
	}

	public String getTimeOffHours() {
		return timeOffHours;
	}

	public void setTimeOffHours(String timeOffHours) {
		this.timeOffHours = timeOffHours;
	}


}
