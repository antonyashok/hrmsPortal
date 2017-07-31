package com.tm.common.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "hldy_st_prv")
public class HolidayStateProvince implements Serializable {

	private static final long serialVersionUID = 6941496516821268296L;

	@Id
	@Column(name = "hldy_st_prv_id")
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID holidayStateProvinceId;

	@ManyToOne
	@JoinColumn(name = "hldy_cldr_id", nullable = false)
	private HolidayCalendar holidayCalendar;

	@Column(name = "st_prv_id")
	private Long stateProvinceId;

	public UUID getHolidayStateProvinceId() {
		return holidayStateProvinceId;
	}

	public void setHolidayStateProvinceId(UUID holidayStateProvinceId) {
		this.holidayStateProvinceId = holidayStateProvinceId;
	}

	public Long getStateProvinceId() {
		return stateProvinceId;
	}

	public void setStateProvinceId(Long stateProvinceId) {
		this.stateProvinceId = stateProvinceId;
	}

	public HolidayCalendar getHolidayCalendar() {
		return holidayCalendar;
	}

	public void setHolidayCalendar(HolidayCalendar holidayCalendar) {
		this.holidayCalendar = holidayCalendar;
	}

}