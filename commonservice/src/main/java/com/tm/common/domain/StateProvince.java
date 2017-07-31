package com.tm.common.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "state_province")
@JsonIgnoreProperties({ "createDate", "lastUpdateDate" })
public class StateProvince implements Serializable {

	private static final long serialVersionUID = 690141412920503618L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "st_prv_id", nullable = false)
	private long stateProvinceId;
	@Column(name = "st_prv_nm", nullable = false)
	private String stateProvinceName;
	@Column(name = "st_desc")
	private String stateDescription;
	@Column(name = "cntry_id", nullable = false)
	private long countryId;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_dt", nullable = false)
	private Date createDate;
	@Column(name = "last_updt_dt")
	private Date lastUpdateDate;
	@Transient
	private String isActiveFlg = "N";

	public long getStateProvinceId() {
		return stateProvinceId;
	}

	public void setStateProvinceId(long stateProvinceId) {
		this.stateProvinceId = stateProvinceId;
	}

	public String getStateProvinceName() {
		return stateProvinceName;
	}

	public void setStateProvinceName(String stateProvinceName) {
		this.stateProvinceName = stateProvinceName;
	}

	public String getStateDescription() {
		return stateDescription;
	}

	public void setStateDescription(String stateDescription) {
		this.stateDescription = stateDescription;
	}

	public long getCountryId() {
		return countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	public String getIsActiveFlg() {
		return isActiveFlg;
	}

	public void setIsActiveFlg(String isActiveFlg) {
		this.isActiveFlg = isActiveFlg;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

}
