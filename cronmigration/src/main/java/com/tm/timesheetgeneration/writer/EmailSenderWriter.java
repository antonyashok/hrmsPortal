package com.tm.timesheetgeneration.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.tm.timesheet.configuration.domain.EmailTaskLog;
import com.tm.timesheetgeneration.service.dto.EmailDTO;
import com.tm.util.MailManager;

public class EmailSenderWriter implements ItemWriter<EmailDTO> {

    @Autowired
    private MailManager mailManager;

    @Override
    public void write(List<? extends EmailDTO> emailTskList) {
        if (null != emailTskList) {
        	EmailDTO emailDTO = emailTskList.get(0);      	
        	List<EmailTaskLog> emailTaskLogs = emailDTO.getEmailTaskLogs();
        	List<UUID> emailTaskLogIds = new ArrayList<>();
            emailTaskLogs.forEach(emailTaskLog -> 
            	emailTaskLogIds.add(emailTaskLog.getEmailTskLogId()));
            mailManager.sendEmailByEmailTskLogId(emailTaskLogIds);
        }

    }
}
