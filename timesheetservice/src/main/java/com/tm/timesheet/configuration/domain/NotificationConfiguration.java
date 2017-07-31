/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheeet.configuration.domain.NotificationConfiguration.java
 * Author        : Annamalai L
 * Date Created  : Jan 10, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "timesheet_notify_config")
public class NotificationConfiguration implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5478251991131975631L;

    @Id
    @Column(name = "ts_notify_config_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID timesheetNotificationConfigId;

    @OneToOne
    @JoinColumn(name = "ts_config_grp_id", nullable = false)
    private ConfigurationGroup configurationGroup;

    @Type(type = "uuid-char")
    @Column(name = "ts_notify_id", nullable = false)
    private UUID notificationAttributeId;

    @Column(name = "ts_notify_value", nullable = false)
    private String notificationAttributeValue;

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false)
    @JsonIgnore
    private Long createdBy;

    @CreatedDate
    @Column(name = "create_dt", nullable = false)
    @JsonIgnore
    private ZonedDateTime createdDate = ZonedDateTime.now();
    
	public UUID getTimesheetNotificationConfigId() {
		return timesheetNotificationConfigId;
	}

	public void setTimesheetNotificationConfigId(
	    UUID timesheetNotificationConfigId) {
		this.timesheetNotificationConfigId = timesheetNotificationConfigId;
	}

	public ConfigurationGroup getConfigurationGroup() {
		return configurationGroup;
	}

	public void setConfigurationGroup(ConfigurationGroup configurationGroup) {
		this.configurationGroup = configurationGroup;
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
}
