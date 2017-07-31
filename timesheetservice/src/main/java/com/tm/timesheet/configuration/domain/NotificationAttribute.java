/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.domain.NotificationAttribute.java
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

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.tm.timesheet.configuration.domain.ConfigurationGroup.ActiveFlagEnum;
import com.tm.timesheet.configuration.domain.ConfigurationGroup.UserGroupCategoryEnum;

@Entity
@Table(name = "timesheet_notify")
public class NotificationAttribute extends AbstractAuditingEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 5726211517585311268L;

    public enum NotificationAttributeDisableEnum {
        Y, N
    }

    public enum ValidationTypeEnum {
        EMAIL, NUMBER, URL
    }

    @Id
    @Column(name = "ts_notify_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID notificationAttributeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ts_notify_category", nullable = false)
    private UserGroupCategoryEnum userGroupCategory;

    @Column(name = "ts_notify_attrib_name", nullable = false)
    private String notificationAttributeName;

    @Column(name = "ts_notify_attrib_input_type", nullable = false)
    private String notificationAttributeInputType;

    @Column(name = "ts_notify_attrib_dflt_value")
    private String notificationAttributeDefaultValue;

    @Column(name = "ts_notify_field_seq")
    private Integer notificationAttributeFieldSequence;    

    @Enumerated(EnumType.STRING)
    @Column(name = "ts_notify_attrib_disable_flg")
    private NotificationAttributeDisableEnum notificationAttributeDisableFlag;

    @Enumerated(EnumType.STRING)
    @Column(name = "validation_type")
    private ValidationTypeEnum validationType;

    @Column(name = "dflt_val")
    private String defaultValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "actv_flg")
    private ActiveFlagEnum activeFlag;
    
    @OneToOne(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
    @Type(type = "uuid-char")
    @JoinColumn(name="ts_dep_notify_id")
    private NotificationAttribute dependantAttribute;
    
    @OneToOne(mappedBy="dependantAttribute")
    public NotificationAttribute dependentNotificationAttribute;

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

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public ActiveFlagEnum getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(ActiveFlagEnum activeFlag) {
        this.activeFlag = activeFlag;
    }

    public NotificationAttribute getDependantAttribute() {
        return dependantAttribute;
    }

    public void setDependantAttribute(NotificationAttribute dependantAttribute) {
        this.dependantAttribute = dependantAttribute;
    }

    public NotificationAttribute getDependentNotificationAttribute() {
        return dependentNotificationAttribute;
    }

    public void setDependentNotificationAttribute(NotificationAttribute dependentNotificationAttribute) {
        this.dependentNotificationAttribute = dependentNotificationAttribute;
    }
    
}
