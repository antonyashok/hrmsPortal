package com.tm.timesheet.configuration.repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.timesheet.configuration.domain.EmployeeConfigSettingsView;
import com.tm.timesheet.configuration.domain.EmployeeConfigSettingsView.UserGroupCategoryEnum;

public interface EmployeeConfigSettingsViewRepository extends JpaRepository<EmployeeConfigSettingsView, UUID> {
	@Query("SELECT ecsv FROM EmployeeConfigSettingsView ecsv WHERE ecsv.officeId IN :officeids AND ecsv.userGroupCategory =:usergroupcategory")
	List<EmployeeConfigSettingsView> getLatestConfigSetting(@Param("officeids") List<Long> officeids,
			@Param("usergroupcategory") UserGroupCategoryEnum empl);
	
	@Query("SELECT ecsv FROM EmployeeConfigSettingsView ecsv WHERE ecsv.officeId IN :officeids AND ecsv.userGroupCategory =:usergroupcategory "
			+ "AND ecsv.effectiveEndDate =:effectiveenddate")
	List<EmployeeConfigSettingsView> getLatestConfigSettingByEndDate(@Param("officeids") List<Long> officeId,
			@Param("usergroupcategory") UserGroupCategoryEnum userGroupCategory,@Param("effectiveenddate") Date effectiveEndDate);
}
