/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.domain.ConfigurationGroupLocation.java
 * Author        : Annamalai L
 * Date Created  : Mar 20, 2017
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
@Table(name = "timesheet_config_grp_locn")
public class ConfigurationGroupLocation implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -505387895788164075L;

    @Id
    @Column(name = "ts_config_grp_locn_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID groupLocationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ts_config_grp_id", nullable = false)
    private ConfigurationGroup configurationGroup;

    @Column(name = "ofc_id", length = 15)
    private Long officeId;

    public UUID getGroupLocationId() {
        return groupLocationId;
    }

    public void setGroupLocationId(UUID groupLocationId) {
        this.groupLocationId = groupLocationId;
    }

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }

    public ConfigurationGroup getConfigurationGroup() {
        return configurationGroup;
    }

    public void setConfigurationGroup(ConfigurationGroup configurationGroup) {
        this.configurationGroup = configurationGroup;
    }
}
