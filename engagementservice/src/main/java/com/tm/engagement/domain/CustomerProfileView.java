package com.tm.engagement.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tm.engagement.domain.CustomerProfile.ActiveFlagEnum;

@Entity
@Table(name = "customer_profile_view")
public class CustomerProfileView implements Serializable {

	private static final long serialVersionUID = -2329534334517387162L;

	@Id
	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "effective_start_date")
	private String effectiveStartDate;

	@Column(name = "effective_end_date")
	private String effectiveEndDate;

	@Column(name = "updated_date")
	private String updatedDate;

	@Column(name = "country_name")
	private String countryName;

	@Enumerated(EnumType.STRING)
	@Column(name = "active_flg")
	private ActiveFlagEnum activeFlag;

	@Column(name = "effective_startdate")
	private String startDate;

	@Column(name = "effective_enddate")
	private String endDate;

	@Column(name = "updateddate")
	private String sortUpdatedDate;
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(String effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public String getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(String effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public ActiveFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
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

	public String getSortUpdatedDate() {
		return sortUpdatedDate;
	}

	public void setSortUpdatedDate(String sortUpdatedDate) {
		this.sortUpdatedDate = sortUpdatedDate;
	}
	
}