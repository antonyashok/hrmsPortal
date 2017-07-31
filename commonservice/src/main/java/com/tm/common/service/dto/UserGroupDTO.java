package com.tm.common.service.dto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "userGroups", collectionRelation = "userGroups")
public class UserGroupDTO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = 7135830361609838198L;

    public enum groupTypeEnum {
        RCTR, EMPL, CNCTR
    }

    private String groupId;

    private String groupName;

    private String groupDescription;

    private String groupType;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
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

}
