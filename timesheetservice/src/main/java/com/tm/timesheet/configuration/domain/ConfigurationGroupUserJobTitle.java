/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.domain.ConfigurationGroupUserJobTitle.java
 * Author        : Annamalai L
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "timesheet_config_grp_jobtitle")
public class ConfigurationGroupUserJobTitle implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4785657303577369876L;

    @Id
    @Column(name = "ts_config_grp_jobtitle_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID configurationGroupUserJobTitleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ts_config_grp_id", nullable = false)
    private ConfigurationGroup configurationGroup;

    @Type(type = "uuid-char")
    @Column(name = "job_title_id")
    private UUID userGroupId;

    public UUID getConfigurationGroupUserJobTitleId() {
        return configurationGroupUserJobTitleId;
    }

    public void setConfigurationGroupUserJobTitleId(UUID configurationGroupUserJobTitleId) {
        this.configurationGroupUserJobTitleId = configurationGroupUserJobTitleId;
    }

    public ConfigurationGroup getConfigurationGroup() {
        return configurationGroup;
    }

    public void setConfigurationGroup(ConfigurationGroup configurationGroup) {
        this.configurationGroup = configurationGroup;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }
    
}
