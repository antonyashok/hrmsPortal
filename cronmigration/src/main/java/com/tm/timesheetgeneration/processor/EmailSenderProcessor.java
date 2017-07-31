package com.tm.timesheetgeneration.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.tm.timesheetgeneration.service.dto.EmailDTO;

public class EmailSenderProcessor implements ItemProcessor<EmailDTO, EmailDTO> {

	private static final Logger log = LoggerFactory.getLogger(EmailSenderProcessor.class);

	@Override
	public EmailDTO process(EmailDTO item) throws Exception {
		log.debug("EmailSenderProcessor Object count::>>> " + item.getEmailTaskLogs().size());
		return item;
	}
}