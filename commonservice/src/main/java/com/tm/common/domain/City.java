package com.tm.common.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "cities")
@JsonIgnoreProperties({ "createdDate", "lastUpdatedDate" })
public class City implements Serializable {

	private static final long serialVersionUID = -4370054435898395062L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "city_id", nullable = false)
	private long cityId;
	@Column(name = "st_prv_id", nullable = false)
	private long stateProvinceId;
	@Column(name = "city_name", nullable = false)
	private String cityName;
	/*@Column(name = "city_desc")
	private String cityDesc;*/
	/*@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false)
	private Date createdDate;
	@Column(name = "last_updated_date")
	private Date lastUpdatedDate;*/

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public long getStateProvinceId() {
		return stateProvinceId;
	}

	public void setStateProvinceId(long stateProvinceId) {
		this.stateProvinceId = stateProvinceId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/*public String getCityDesc() {
		return cityDesc;
	}

	public void setCityDesc(String cityDesc) {
		this.cityDesc = cityDesc;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
*/
}