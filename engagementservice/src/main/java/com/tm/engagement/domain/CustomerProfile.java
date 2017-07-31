package com.tm.engagement.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tm.engagement.service.dto.CompanyLocationDTO;

@Entity
@Table(name = "customer_profile")
public class CustomerProfile extends AbstractAuditingEntity implements Serializable {

	private static final long serialVersionUID = -8481625513487346933L;

	public enum ActiveFlagEnum {
		Y, N
	}

	@Id
	@GeneratedValue
	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "customer_number")
	private String customerNumber;

	@Column(name = "customer_name")
	private String customerName;

	@Column(name = "customer_email")
	private String customerEmail;

	@Column(name = "customer_phone")
	private String customerPhone;

	@Column(name = "customer_address")
	private String customerAddress;

	@Column(name = "country_id")
	private Long countryId;

	@Column(name = "effective_start_date")
	private Date effectiveStartDate;

	@Column(name = "effective_end_date")
	private Date effectiveEndDate;

	@Column(name = "customer_postal_code")
	private String postalCode;

	@Enumerated(EnumType.STRING)
	@Column(name = "active_flg")
	private ActiveFlagEnum activeFlag;

	@Transient
	private String countryName;

	@Transient
	private String lastUpdatedDt;
	
	@Column(name = "state_id")
	private Long stateId;
	
	@Column(name = "city_id")
	private Long cityId;

	@Transient
	private List<CompanyLocationDTO> companyLocationDTO;
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	
	public Date getEffectiveStartDate() {
		return effectiveStartDate;
	}

	public void setEffectiveStartDate(Date effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}

	public Date getEffectiveEndDate() {
		return effectiveEndDate;
	}

	public void setEffectiveEndDate(Date effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public ActiveFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getLastUpdatedDt() {
		return lastUpdatedDt;
	}

	public void setLastUpdatedDt(String lastUpdatedDt) {
		this.lastUpdatedDt = lastUpdatedDt;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public List<CompanyLocationDTO> getCompanyLocationDTO() {
		return companyLocationDTO;
	}

	public void setCompanyLocationDTO(List<CompanyLocationDTO> companyLocationDTO) {
		this.companyLocationDTO = companyLocationDTO;
	}	
}