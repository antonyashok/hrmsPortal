/**
 * 
 */
package com.tm.engagement.service.resources;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.hateoas.ResourceSupport;

/**
 * @author hemanth
 *
 */
//@Relation(value = "CntrHolidays", collectionRelation = "CntrHolidays")
public class HolidayResourceDTO extends ResourceSupport implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7972522617425269701L;
	
	private static final String ENGAGEMENT_HOLIDAY_IS_REQUIRED = "Holiday Description is Required";

	//private String engagementHolidayId;
	
	private UUID engagementHolidayId;

	private String engagementId;

	//private Date holidayDate;
	private String holidayDate;

	@NotBlank(message = ENGAGEMENT_HOLIDAY_IS_REQUIRED)
	private String holidayDescription;
	
	private String engagementName;
	
	private Long customerId;
	
	private String customerName;

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

	public String getHolidayDate() {
		return holidayDate;
	}

	public void setHolidayDate(String holidayDate) {
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
	
	
}
