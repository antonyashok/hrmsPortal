/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.service.dto.UserGroupDTO.java
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * A DTO for the JobTitle entity.
 */
@JsonInclude(value = Include.NON_NULL)
public class UserGroupDTO implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -6918785937384979613L;

    private UUID groupId;
    private String groupName;
    private String groupDescription;
    private String groupType;
    private String isConfigured = "N";

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getIsConfigured() {
        return isConfigured;
    }

    public void setIsConfigured(String isConfigured) {
        this.isConfigured = isConfigured;
    }

}
