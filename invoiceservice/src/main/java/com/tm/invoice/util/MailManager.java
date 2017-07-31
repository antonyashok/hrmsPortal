/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.web.util.MailManager.java
 * Author        : Annamalai
 * Date Created  : Apr 18th, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.invoice.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.invoice.domain.EmailConfiguration;
import com.tm.invoice.domain.EmailConfiguration.EmailFilter;
import com.tm.invoice.domain.EmailSettings;
import com.tm.invoice.dto.EmailTaskLog;
import com.tm.invoice.dto.EmailTaskLog.EmailStatusEnum;
import com.tm.invoice.mongo.domain.InvoiceQueue;
import com.tm.invoice.repository.EmailConfigurationRepository;
import com.tm.invoice.repository.EmailSettingsRepository;

import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;

@Service
@Component
public class MailManager {	

	private static final String INVOICE = "invoice";

	private static final String RECEIPIENT_NAME = "$RECEIPIENT_NAME$";
	
	private static final String IG_IMG = "$igImg$";
	
	private static final Logger log = LoggerFactory.getLogger(MailManager.class);
	
	SimpleDateFormat formater = new SimpleDateFormat("MM/dd/yyyy");
	
	SimpleDateFormat formater1 = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final String EXCEPTION_WHILE_SENDING_MAIL = "Exception while sending mail";
	
    private static final String INV_EMAIL_DELIVERY = "InvoiceGeneration";

    private static final String INVOICE_DATE = "$INVOICE_DATE$";

    private static final String BILL_TYPE = "$BILL_CYCLE_TYPE$";	   
	
	@Autowired
	EmailSettingsRepository emailSettingsRepository;
	
	@Autowired
	EmailConfigurationRepository emailConfigurationRepository;	
	
	@Autowired
    MongoTemplate mongoTemplate;		  
    
    @Inject
    public MailManager(EmailSettingsRepository emailSettingsRepository,
            EmailConfigurationRepository emailConfigurationRepository, MongoTemplate mongoTemplate
            ) {      
        this.emailSettingsRepository = emailSettingsRepository;
        this.emailConfigurationRepository = emailConfigurationRepository;
        this.mongoTemplate = mongoTemplate;
      
    }
	
	public EmailConfiguration getEmailConfigurationByConfigName(String role) {
		EmailConfiguration emailConfiguration = new EmailConfiguration();
		if (!role.isEmpty()) {
			emailConfiguration = emailConfigurationRepository
					.findEmailConfigurationByEmailConfigName(role);
		}
		return emailConfiguration;
	}
	

    public EmailSettings getEmailSettingsBySenderName() {
		List<EmailSettings> emailSettings = null;
		EmailSettings emailSetting = null;
		try {
			emailSettings = emailSettingsRepository.findAll();
			if (CollectionUtils.isNotEmpty(emailSettings)) {
				emailSetting = emailSettings.get(0);
			}
		} catch (Exception e) {
			log.error("Emailsettings is not configured"+e);
		}
		return emailSetting;
	}	
	
	private EmailTaskLog saveToEmailTaskLog(String emailId, UUID timesheetId, String mailSubject,
			EmailSettings emailSettingsVO,
			String metaTemplateWithHTMLData, String moduleName) {
		try {
			return saveEmailTaskLog(emailId, timesheetId, mailSubject, metaTemplateWithHTMLData, emailSettingsVO, moduleName);
		} catch (RuntimeException re) {
			log.error(EXCEPTION_WHILE_SENDING_MAIL, re);
		} catch (Exception ex) {
			log.error(EXCEPTION_WHILE_SENDING_MAIL, ex);
		}
		return null;
	}

	private EmailTaskLog saveEmailTaskLog(String emailId, UUID referenceId, String mailSubject, String metaTemplateWithHTMLData,
			EmailSettings emailSettings, String moduleName) {
		EmailTaskLog emailTaskLog = new EmailTaskLog();
		emailTaskLog.setEmailTo(emailId);
		emailTaskLog.setEmailStatus(EmailStatusEnum.NEW);
		java.sql.Date currentDate = InvoiceCommonUtils.getCurrentDate();
		emailTaskLog.setEmailSentDate(currentDate);
		emailTaskLog.setEmailFrom(emailSettings.getSenderEmail());
		emailTaskLog.setEmailTo(emailId);
		emailTaskLog.setEmailSubject(mailSubject);
		emailTaskLog.setEmailContent(metaTemplateWithHTMLData);
		emailTaskLog.setRetryCount(0);
		emailTaskLog.setInvoiceQueueId(referenceId);
		CommonUtils.populateAuditDetailsForSave(emailTaskLog);		
		return emailTaskLog;
	}	

	public EmailConfiguration getEmailConfigurationByRole(String role) {
		EmailConfiguration emailConfiguration = new EmailConfiguration();
		if (!role.isEmpty()) {
			emailConfiguration = emailConfigurationRepository.findEmailConfigurationByEmailConfigName(role);
		}
		return emailConfiguration;
	}
	
	
    public EmailTaskLog sendEmailForInvoice(InvoiceQueue invoice) {
        Map<String, Object> metaTsLastDate = new HashMap<>();
        Map<String, Object> metaEmailContent = new HashMap<>();
        String configName = INV_EMAIL_DELIVERY;
        String mailSubject;
        String billToManagerEmail = null;
        if(invoice.getManualInvoiceId() != null) {
            billToManagerEmail = invoice.getRecipientsEmailIds();
        } else {
            billToManagerEmail = invoice.getBillToManagerEmailId();
        }         
        if(billToManagerEmail != null) {
            String billToManagerName = invoice.getBillToManagerName();
            metaTsLastDate.put(INVOICE_DATE, formater1.format(new Date()));
            EmailConfiguration emailConfig = getEmailConfigurationByConfigName(configName);
            if (null != emailConfig) {
                mailSubject =
                        CommonUtils.mergeTemplateWithData(emailConfig.getSubject(), metaTsLastDate);
                return populateMailContentAndSaveEmailTaskLogForInvoice(billToManagerEmail, billToManagerName, invoice,
                        metaEmailContent, mailSubject, emailConfig);
            } else {
                log.error("Email template is not configured");
            }
        }
        return null;
    }

    public EmailTaskLog populateMailContentAndSaveEmailTaskLogForInvoice(String billToManagerEmail,
            String billToManagerName, InvoiceQueue invoice, Map<String, Object> metaEmailContent,
            String mailSubject, EmailConfiguration emailConfig) {
        EmailSettings emailSettingsVO = getEmailSettingsBySenderName();
        if (null != emailSettingsVO) {
            String msg;
            msg = emailConfig.getEmailContent();
            metaEmailContent.put(RECEIPIENT_NAME, billToManagerName != null ? billToManagerName: StringUtils.EMPTY);
            metaEmailContent.put(IG_IMG, CommonUtils.formLogoImageURL());
       /*     if(invoice.getManualInvoiceId() != null) {
                metaEmailContent.put(BILL_TYPE, invoice.getInvoiceType() != null ? invoice.getInvoiceType() : StringUtils.EMPTY);
            } else {
                metaEmailContent.put(BILL_TYPE, invoice.getBillCycle() != null ? invoice.getBillCycle() : StringUtils.EMPTY);
            }           */ 
            //TODO:Need to add Bill cycle if Auto generated invoice.
            metaEmailContent.put(BILL_TYPE, invoice.getInvoiceType() != null ? invoice.getInvoiceType() : StringUtils.EMPTY);

            metaEmailContent.put(INVOICE_DATE, formater1.format(new Date()));            
            String emailWithHtmlContent = CommonUtils.mergeTemplateWithData(msg, metaEmailContent);
            if (emailConfig.getEmailFilter() == EmailFilter.Y) {
                try {
                   return saveToEmailTaskLog(emailConfig.getDefaultRecipient(), invoice.getId(),
                            mailSubject, emailSettingsVO, emailWithHtmlContent, MailManager.INVOICE);
                } catch (Exception msgException) {
                    log.error(EXCEPTION_WHILE_SENDING_MAIL, msgException);
                }
            } else if (emailConfig.getEmailFilter() == EmailFilter.N) {
                try {
                   return saveToEmailTaskLog(billToManagerEmail, invoice.getId(), mailSubject,
                            emailSettingsVO, emailWithHtmlContent, MailManager.INVOICE);
                } catch (Exception msgException) {
                    log.error(EXCEPTION_WHILE_SENDING_MAIL, msgException);
                }
            }
        } else {
            log.error("Email settings is not configured");
        }
        return null;
    }

	
}