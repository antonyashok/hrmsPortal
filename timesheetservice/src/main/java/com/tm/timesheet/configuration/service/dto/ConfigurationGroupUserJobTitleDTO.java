package com.tm.timesheet.configuration.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "ConfigurationGroupUserJobTitle", collectionRelation = "ConfigurationGroupUserJobTitles")
public class ConfigurationGroupUserJobTitleDTO extends ResourceSupport implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1783786383250205569L;

	private UUID configurationGroupUserJobTitleId;
    private UUID userGroupId;
    private String userGroupName;

    public UUID getConfigurationGroupUserJobTitleId() {
        return configurationGroupUserJobTitleId;
    }

    public void setConfigurationGroupUserJobTitleId(UUID configurationGroupUserJobTitleId) {
        this.configurationGroupUserJobTitleId = configurationGroupUserJobTitleId;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }
}