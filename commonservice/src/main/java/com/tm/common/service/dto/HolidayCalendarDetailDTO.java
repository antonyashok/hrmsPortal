package com.tm.common.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Relation(value = "holidayStateProvince", collectionRelation = "holidayStateProvinces")
@JsonIgnoreProperties({ "createdBy", "holidayCalendarId", "lastModifiedBy" })
public class HolidayCalendarDetailDTO extends ResourceSupport implements
		Serializable {

	private static final long serialVersionUID = -1257835367948401408L;

	private UUID holidayCalendarDetailId;
	private UUID holidayCalendarId;
	@NotEmpty(message = "{holiday.settings.calendar.details.name.empty}")
	private String holidayname;
	@NotEmpty(message = "{holiday.settings.calendar.details.name.empty}")
	private String holidayDate;
	@NotEmpty(message = "{holiday.settings.calendar.details.end.date.empty}")
	private String holidayEndDate;
	private Long createdBy;
	private Long lastModifiedBy;
	private List<UUID> holidayDetailIds;
	private Boolean activeFlag = false;

	public UUID getHolidayCalendarDetailId() {
		return holidayCalendarDetailId;
	}

	public void setHolidayCalendarDetailId(UUID holidayCalendarDetailId) {
		this.holidayCalendarDetailId = holidayCalendarDetailId;
	}

	public UUID getHolidayCalendarId() {
		return holidayCalendarId;
	}

	public void setHolidayCalendarId(UUID holidayCalendarId) {
		this.holidayCalendarId = holidayCalendarId;
	}

	public String getHolidayname() {
		return holidayname;
	}

	public void setHolidayname(String holidayname) {
		this.holidayname = holidayname;
	}

	public String getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(String holidayDate) {
		this.holidayDate = holidayDate;
	}

	public String getHolidayEndDate() {
		return holidayEndDate;
	}

	public void setHolidayEndDate(String holidayEndDate) {
		this.holidayEndDate = holidayEndDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public List<UUID> getHolidayDetailIds() {
		return holidayDetailIds;
	}

	public void setHolidayDetailIds(List<UUID> holidayDetailIds) {
		this.holidayDetailIds = holidayDetailIds;
	}

	public Boolean getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Boolean activeFlag) {
		this.activeFlag = activeFlag;
	}

}