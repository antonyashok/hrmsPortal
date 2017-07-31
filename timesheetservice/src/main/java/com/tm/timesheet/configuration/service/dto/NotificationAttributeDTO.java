/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.service.dto.NotificationAttributeDTO.java
 * Author        : Annamalai L
 * Date Created  : Jan 18, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.tm.timesheet.configuration.domain.ConfigurationGroup.ActiveFlagEnum;
import com.tm.timesheet.configuration.domain.ConfigurationGroup.UserGroupCategoryEnum;
import com.tm.timesheet.configuration.domain.NotificationAttribute.NotificationAttributeDisableEnum;
import com.tm.timesheet.configuration.domain.NotificationAttribute.ValidationTypeEnum;
import com.tm.timesheet.configuration.service.dto.validator.NotificationAttributeValidation;

@Relation(value = "notificationAttribute", collectionRelation = "notificationAttribute")
@NotificationAttributeValidation(message="Please enter a valid email id.")
public class NotificationAttributeDTO extends ResourceSupport implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5726211517585311268L;

    private UUID notificationAttributeId;
    private UserGroupCategoryEnum userGroupCategory;
    private String notificationAttributeName;
    private String notificationAttributeInputType;
    private String notificationAttributeDefaultValue;
    private Integer notificationAttributeFieldSequence;
    private UUID dependentNotificationAttributeId;
    private NotificationAttributeDisableEnum notificationAttributeDisableFlag;
    private ValidationTypeEnum validationType;
    private ActiveFlagEnum activeFlag;
    private String defaultValue;
    private String notificationAttributeValue;
    public NotificationAttributeDTO dependentNotificationAttribute;

    public UUID getNotificationAttributeId() {
        return notificationAttributeId;
    }

    public void setNotificationAttributeId(UUID notificationAttributeId) {
        this.notificationAttributeId = notificationAttributeId;
    }

    public UserGroupCategoryEnum getUserGroupCategory() {
        return userGroupCategory;
    }

    public void setUserGroupCategory(UserGroupCategoryEnum userGroupCategory) {
        this.userGroupCategory = userGroupCategory;
    }

    public String getNotificationAttributeName() {
        return notificationAttributeName;
    }

    public void setNotificationAttributeName(String notificationAttributeName) {
        this.notificationAttributeName = notificationAttributeName;
    }

    public String getNotificationAttributeInputType() {
        return notificationAttributeInputType;
    }

    public void setNotificationAttributeInputType(String notificationAttributeInputType) {
        this.notificationAttributeInputType = notificationAttributeInputType;
    }

    public String getNotificationAttributeDefaultValue() {
        return notificationAttributeDefaultValue;
    }

    public void setNotificationAttributeDefaultValue(String notificationAttributeDefaultValue) {
        this.notificationAttributeDefaultValue = notificationAttributeDefaultValue;
    }

    public Integer getNotificationAttributeFieldSequence() {
        return notificationAttributeFieldSequence;
    }

    public void setNotificationAttributeFieldSequence(Integer notificationAttributeFieldSequence) {
        this.notificationAttributeFieldSequence = notificationAttributeFieldSequence;
    }

    public UUID getDependentNotificationAttributeId() {
        return dependentNotificationAttributeId;
    }

    public void setDependentNotificationAttributeId(UUID dependentNotificationAttributeId) {
        this.dependentNotificationAttributeId = dependentNotificationAttributeId;
    }

    public NotificationAttributeDisableEnum getNotificationAttributeDisableFlag() {
        return notificationAttributeDisableFlag;
    }

    public void setNotificationAttributeDisableFlag(
            NotificationAttributeDisableEnum notificationAttributeDisableFlag) {
        this.notificationAttributeDisableFlag = notificationAttributeDisableFlag;
    }

    public ValidationTypeEnum getValidationType() {
        return validationType;
    }

    public void setValidationType(ValidationTypeEnum validationType) {
        this.validationType = validationType;
    }

    public ActiveFlagEnum getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(ActiveFlagEnum activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getNotificationAttributeValue() {
        return notificationAttributeValue;
    }

    public void setNotificationAttributeValue(String notificationAttributeValue) {
        this.notificationAttributeValue = notificationAttributeValue;
    }

    public NotificationAttributeDTO getDependentNotificationAttribute() {
        return dependentNotificationAttribute;
    }

    public void setDependentNotificationAttribute(
            NotificationAttributeDTO dependentNotificationAttribute) {
        this.dependentNotificationAttribute = dependentNotificationAttribute;
    }
    
}