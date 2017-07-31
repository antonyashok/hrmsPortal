package com.tm.timesheet.configuration.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.hateoas.core.Relation;

@Relation(value = "configurationGroupLocation", collectionRelation = "configurationGroupLocation")
public class ConfigurationGroupLocationDTO implements Serializable {

    /**
     * Auto generatedId
     */
    private static final long serialVersionUID = -5540875693466156579L;

    private UUID groupLocationId;
    private String groupLocationActiveFlag;
    private Long officeId;
    private String officeName;
    
    public UUID getGroupLocationId() {
        return groupLocationId;
    }

    public void setGroupLocationId(UUID groupLocationId) {
        this.groupLocationId = groupLocationId;
    }

    public String getGroupLocationActiveFlag() {
        return groupLocationActiveFlag;
    }

    public void setGroupLocationActiveFlag(String groupLocationActiveFlag) {
        this.groupLocationActiveFlag = groupLocationActiveFlag;
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

}
