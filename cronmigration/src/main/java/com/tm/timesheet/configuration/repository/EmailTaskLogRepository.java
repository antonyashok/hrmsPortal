package com.tm.timesheet.configuration.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.timesheet.configuration.domain.EmailTaskLog;
import com.tm.timesheet.configuration.domain.EmailTaskLog.EmailStatusEnum;

public interface EmailTaskLogRepository extends
		JpaRepository<EmailTaskLog, UUID> {

	@Query("SELECT emailTasks FROM EmailTaskLog AS emailTasks WHERE emailTasks.emailStatus !=:emailStatus")
	List<EmailTaskLog> getEmailTaskLogsByEmailStatusNotInSUCCESS(
			@Param("emailStatus") EmailStatusEnum emailStatus);

}