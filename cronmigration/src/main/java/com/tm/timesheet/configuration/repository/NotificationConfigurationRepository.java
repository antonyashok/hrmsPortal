package com.tm.timesheet.configuration.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tm.timesheet.configuration.domain.ConfigurationGroup;
import com.tm.timesheet.configuration.domain.NotificationConfiguration;

public interface NotificationConfigurationRepository extends
		CrudRepository<NotificationConfiguration, UUID> {

	List<NotificationConfiguration> findByConfigurationGroup(
			ConfigurationGroup configurationGroup);

	@Query("SELECT nc FROM NotificationConfiguration AS nc WHERE nc.configurationGroup.configurationGroupId=:configId")
	NotificationConfiguration getRecruiterNotificationById(
			@Param("configId") UUID configId);

}
