package com.tm.timesheet.configuration.web.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.timesheet.configuration.domain.EmailTaskLog;
import com.tm.timesheet.configuration.repository.EmailTaskLogRepository;
import com.tm.timesheet.configuration.service.dto.StatusDTO;

import io.swagger.annotations.Api;

@RestController(value="/emailTaskLogs")
@Api(value = "emailTaskLogs", produces = APPLICATION_JSON_VALUE ,
        consumes = APPLICATION_JSON_VALUE)
public class EmailTaskLogResource {
    
    private EmailTaskLogRepository emailTaskLogRepository;
    
    @Inject
    public EmailTaskLogResource(EmailTaskLogRepository emailTaskLogRepository) {
        this.emailTaskLogRepository = emailTaskLogRepository;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    @RequiredAuthority({InvoiceConstants.FINANCE_REPRESENTATIVE })
    public StatusDTO saveEmailTask(@RequestBody EmailTaskLog emailTaskLog) {
        emailTaskLogRepository.save(emailTaskLog);
        return new StatusDTO("ok");
    }

}
