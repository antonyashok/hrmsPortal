/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.service.dto.OfficeLocationDTO.java
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

/**
 * A DTO for the OfficeLocation entity.
 */
public class OfficeLocationDTO implements Serializable {

    private static final long serialVersionUID = 2756667699778872521L;

    private Long officeId;
    private String officeName;
    private String activeFlag;
    private String isConfigured = "N";
    private String isActive = "N";
    public OfficeLocationDTO() {

    }

    public OfficeLocationDTO(Long officeId, String officeName, String isActive,
            String isConfigured) {
        this(officeId, officeName, isActive, isConfigured, null);
    }

    public OfficeLocationDTO(Long officeId, String officeName, String isActive,
            String isConfigured, String activeFlag) {
        this.officeId = officeId;
        this.officeName = officeName;
        this.isActive = isActive;
        this.isConfigured = isConfigured;
        this.activeFlag = activeFlag;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getIsConfigured() {
        return isConfigured;
    }

    public void setIsConfigured(String isConfigured) {
        this.isConfigured = isConfigured;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

}
