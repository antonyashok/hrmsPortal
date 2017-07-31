package com.tm.timesheetgeneration.reader;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.tm.timesheet.configuration.domain.EmailTaskLog;
import com.tm.timesheet.configuration.domain.EmailTaskLog.EmailStatusEnum;
import com.tm.timesheet.configuration.repository.EmailTaskLogRepository;
import com.tm.timesheetgeneration.service.dto.EmailDTO;

public class EmailSenderReader implements ItemReader<EmailDTO> {

	private static final Logger log = LoggerFactory.getLogger(EmailSenderReader.class);

	@Autowired
	private EmailTaskLogRepository emailTaskLogRepository;

	private static boolean collected = false;

	@StepScope
	@Value("#{stepExecutionContext[from]}")
	public int fromId;

	@StepScope
	@Value("#{stepExecutionContext[to]}")
	public int toId;
	
	@Override
	@StepScope
	public EmailDTO read() {
		
		EmailDTO emailDTO = null;
		if (!collected) {
			try {
				collected = true;
				List<EmailTaskLog> emailTskList = emailTaskLogRepository
						.getEmailTaskLogsByEmailStatusNotInSUCCESS(EmailStatusEnum.SUCCESS);
				if (CollectionUtils.isNotEmpty(emailTskList)) {
					emailDTO = new EmailDTO();
					emailDTO.setEmailTaskLogs(emailTskList);
				}
			} catch (Exception e) {
				log.error("Error in EmailSenderReader ", e);
			}
		} else {
			collected = false;
			return null;
		}
		return emailDTO;
	}
}
