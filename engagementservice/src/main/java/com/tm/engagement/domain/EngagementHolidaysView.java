/**
 * 
 */
package com.tm.engagement.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author hemanth
 *
 */
@Entity
@Table(name = "engagement_holiday_view")
public class EngagementHolidaysView implements Serializable {

	private static final long serialVersionUID = 7453310039042610019L;

	/*@Id
	@Column(name = "engmt_hldy_id")
	private String engagementHolidayId;*/
	
	@Id
	@Type(type = "uuid-char")
	@Column(name = "engmt_hldy_id")
	private UUID engagementHolidayId;

	@Column(name = "engmt_id")
	private String engagementId;

	@Temporal(TemporalType.DATE)
	@Column(name = "hldy_date")
	private Date holidayDate;

	@Column(name = "hldy_desc")
	private String holidayDescription;
	
	@Column(name = "engmt_nm")
	private String engagementName;
	
	@Column(name = "customer_id")
	private Long customerId;
	
	@Column(name = "customer_name")
	private String customerName;
	
	@CreatedBy
	@Column(name = "created_by")
	@JsonIgnore
	private Long createdBy;

	@CreatedDate
	@Column(name = "create_dt")
	@JsonIgnore
	private Date createdDate;

	@LastModifiedBy
	@Column(name = "updated_by")
	@JsonIgnore
	private Long lastModifiedBy;

	@LastModifiedDate
	@Column(name = "last_updt_dt")
	@JsonIgnore
	private Date lastModifiedDate;

	public UUID getEngagementHolidayId() {
		return engagementHolidayId;
	}

	public void setEngagementHolidayId(UUID engagementHolidayId) {
		this.engagementHolidayId = engagementHolidayId;
	}

	public String getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(String engagementId) {
		this.engagementId = engagementId;
	}

	public Date getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(Date holidayDate) {
		this.holidayDate = holidayDate;
	}

	public String getHolidayDescription() {
		return holidayDescription;
	}

	public void setHolidayDescription(String holidayDescription) {
		this.holidayDescription = holidayDescription;
	}

	public String getEngagementName() {
		return engagementName;
	}

	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
	}

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

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	
	
}
