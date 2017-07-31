package com.tm.timesheet.configuration.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "NotificationConfiguration", collectionRelation = "NotificationConfigurations")
public class NotificationConfigurationDTO extends ResourceSupport implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2878427410874809414L;
	
    private UUID timesheetNotificationConfigId;
    private UUID notificationAttributeId;
    private String notificationAttributeValue;

	public UUID getTimesheetNotificationConfigId() {
		return timesheetNotificationConfigId;
	}

	public void setTimesheetNotificationConfigId(
	    UUID timesheetNotificationConfigId) {
		this.timesheetNotificationConfigId = timesheetNotificationConfigId;
	}

	public UUID getNotificationAttributeId() {
		return notificationAttributeId;
	}

	public void setNotificationAttributeId(UUID notificationAttributeId) {
		this.notificationAttributeId = notificationAttributeId;
	}

	public String getNotificationAttributeValue() {
		return notificationAttributeValue;
	}

	public void setNotificationAttributeValue(String notificationAttributeValue) {
		this.notificationAttributeValue = notificationAttributeValue;
	}

}
