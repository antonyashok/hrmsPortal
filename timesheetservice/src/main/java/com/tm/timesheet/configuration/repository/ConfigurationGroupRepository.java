package com.tm.timesheet.configuration.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.timesheet.configuration.domain.ConfigurationGroup;

public interface ConfigurationGroupRepository extends JpaRepository<ConfigurationGroup, UUID> {

    @Query("SELECT DISTINCT location.officeId FROM ConfigurationGroup config inner join"
            + " config.configurationGroupLocation location inner join"
            + " config.configurationGroupUserJobTitle userGroup WHERE"
            + " userGroup.configurationGroup.configurationGroupId=location.configurationGroup.configurationGroupId "
            + " AND userGroup.userGroupId IN (:userGroupIds)"
            + " AND config.configurationGroupId=:configurationGroupId"
            + " AND config.activeFlag='Y' AND config.effectiveFlag = 'Y'")
    List<Long> findConfiguredOfficeIdsByGroupIdAndUserGroupIds(
            @Param("configurationGroupId") UUID configurationGroupId,
            @Param("userGroupIds") List<UUID> userGroupIds);
    
    @Query("SELECT DISTINCT location.officeId FROM ConfigurationGroup config inner join"
            + " config.configurationGroupLocation location inner join"
            + " config.configurationGroupUserJobTitle userGroup WHERE"
            + " userGroup.configurationGroup.configurationGroupId=location.configurationGroup.configurationGroupId "
            + " AND userGroup.userGroupId IN (:userGroupIds)"
            + " AND config.activeFlag='Y' AND config.effectiveFlag = 'Y'")
    List<Long> findConfiguredOfficeIdsByUserGroupIds(
            @Param("userGroupIds") List<UUID> userGroupIds);
}
