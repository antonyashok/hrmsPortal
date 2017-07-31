package com.tm.common.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "hldy_cldr_dtl")
public class HolidayCalendarDetail implements Serializable {

	private static final long serialVersionUID = 6631488205959711749L;

	@Id
	@Column(name = "hldy_cldr_dtl_id")
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID holidayCalendarDetailId;

	@ManyToOne
	@JoinColumn(name = "hldy_cldr_id", nullable = false)
	private HolidayCalendar holidayCalendar;

	@Column(name = "hldy_nm", nullable = false, length = 100)
	private String holidayname;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "hldy_dt")
	private Date holidayDate;

	@CreatedBy
	@Column(name = "created_by", nullable = false, length = 50, updatable = false)
	@JsonIgnore
	private Long createdBy;

	@CreatedDate
	@Column(name = "create_dt", nullable = false)
	@JsonIgnore
	private ZonedDateTime createdDate = ZonedDateTime.now();

	@LastModifiedBy
	@Column(name = "updated_by", length = 50)
	@JsonIgnore
	private Long lastModifiedBy;

	@JsonIgnore
	@Column(name = "last_updt_dt")
	private ZonedDateTime lastModifiedDate = ZonedDateTime.now();

	public HolidayCalendar getHolidayCalendar() {
		return holidayCalendar;
	}

	public void setHolidayCalendar(HolidayCalendar holidayCalendar) {
		this.holidayCalendar = holidayCalendar;
	}

	public UUID getHolidayCalendarDetailId() {
		return holidayCalendarDetailId;
	}

	public void setHolidayCalendarDetailId(UUID holidayCalendarDetailId) {
		this.holidayCalendarDetailId = holidayCalendarDetailId;
	}

	public String getHolidayname() {
		return holidayname;
	}

	public void setHolidayname(String holidayname) {
		this.holidayname = holidayname;
	}

	public Date getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public ZonedDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(ZonedDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public Long getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public ZonedDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

}