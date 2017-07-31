package com.tm.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tm.common.engagement.domain.EmailConfiguration;
import com.tm.common.engagement.domain.EmailConfiguration.EmailFilter;
import com.tm.common.engagement.domain.EmailSettings;
import com.tm.common.engagement.repository.EmailConfigurationRepository;
import com.tm.common.engagement.repository.EmailSettingsRepository;
import com.tm.commonapi.exception.BusinessException;
import com.tm.commonapi.web.rest.util.CommonUtils;


@Component
public class MailManagerUtil {

	private static final Logger log = LoggerFactory
			.getLogger(MailManagerUtil.class);
	
	private static final String EXCEPTION_WHILE_SENDING_MAIL = "Exception while sending mail";	
	//private static final String RECEIPIENT_NAME = "$RECEIPIENT_NAME$";	
	//private static final String IG_IMG = "$igImg$";	
	//private static final String LAST_WEEK_START_DATE = "$LAST_WEEK_START_DATE$";	
	//private static final String LAST_WEEK_END_DATE = "$LAST_WEEK_END_DATE$";	
	private static final String FIRSTNAME_LASTNAME = "$FIRSTNAME_LASTNAME$";
	private static final String TEMP_PASSWORD = "$TEMP_PASSWORD$";
	private static final String USERNAME = "$USERNAME$";
	private static final String CREATE_PASS_LINK = "$CREATEPASSURL$";

	SimpleDateFormat formater = new SimpleDateFormat("MM/dd/yyyy");
	
	public static final String TEMPLATE_NAME = "templateName";
	public static final String TIMESHEET = "timesheet";

	public static final String RECRUITER_TIMESHEET = "RecruiterAlert";
	public static final String HR_MANAGER_NOTIFICATION = "HrManagerAlert";
	public static final String RECRUITER_TS_APPROVED_ALERT = "RecruiterTsApprovedAlert";
	public static final String RECRUITER_TS_APPROVAL_REMINDER = "ReTimeSheetReminder";
	public static final String PAYROLL_AND_HR_MANAGER_ALERT = "Payroll&HRManagerAlert";
	public static final String REGIONAL_MANAGER_ALERT = "RegionalManagerAlert";
	
	/*EMAIL CONFIURATION NAMES*/
	public static final String TIMESHEET_CREATED = "TimesheetCreated";
	public static final String TIMESHEET_SUBMITTED = "TimesheetSubmitted";
	public static final String TIMESHEET_APPROVED = "TimesheetApproved";	
	public static final String TIMESHEET_REJECTED = "TimesheetRejected";
	public static final String TIMESHEET_REOPENNED = "TimesheetReopenned";	
	public static final String TIMESHEET_OVERDUE = "TimesheetOverdue";
	public static final String TIMESHEET_PAYROLL = "TimesheetPayRoll";
	public static final String TIMESHEET_PROXY = "TimesheetProxy";
	public static final String TIMESHEET_VERIFICATION = "TimesheetVerification";
	public static final String RECRUITER_TIMESHEET_SUBMITTED = "RecruiterTimesheetSubmitted";
	public static final String RECRUITER_TIMESHEET_APPROVAL = "RecruiterTimesheetApproval";
	
	public static final String CREATE_EMPLOYEE_MAIL = "CreateEmployee";
	public static final String FORGOT_PSWORD_MAIL = "ForgotPassword";
	public static final String FORGOT_PASSWORD_NOTIFICATION = "ForgotPasswordNotification";
	public static final String RESET_PASSWORD_NOTIFICATION = "ResetPasswordNotification";
	public static final String CHANGE_PASSWORD_NOTIFICATION = "ChangePasswordNotification";

	public static final String MAIL_TRANSFER_PROTOCOL = "mail.transport.protocol";
	public static final String MAIL_SMTP_AUTHENTICATION = "mail.smtp.auth";
	public static final String MAIL_START_ENABLE = "mail.smtp.starttls.enable";
	public static final String MAIL_SMTP_HOST = "mail.smtp.host";
	public static final String MAIL_SMTP_PORT = "mail.smtp.port";

	EmailSettingsRepository emailSettingsRepository;

	EmailConfigurationRepository emailConfigurationRepository;

//	EmailTaskLogRepository emailTaskLogRepository;
	
	@Value("${spring.mail-uri.domain-url}")
	public String DOMAIN_URL;
	
	@Value("${spring.mail-uri.context-path}")
	public String MAIL_CONTEXT_PATH;
	
	@Value("${spring.mail-uri.app-module}")
	public String APP_MODULE;

	@Inject
	public MailManagerUtil(EmailSettingsRepository emailSettingsRepository,
			EmailConfigurationRepository emailConfigurationRepository
			) {
		this.emailSettingsRepository = emailSettingsRepository;
		this.emailConfigurationRepository = emailConfigurationRepository;

	}
	
	/*public void sendTimesheetNotificationMail(String emailId, Timesheet timesheet, String configName) {
		Map<String, Object> metaTsLastDate = new HashMap<>();
		Map<String, Object> metaEmailContent = new HashMap<>();

		String mailSubject;
		if (null != emailId && null != timesheet && null != timesheet.getEndDate() && null != timesheet.getEmployee().getName()) {			
			String formattedEndDate = formater.format(timesheet.getEndDate());
			metaTsLastDate.put(LAST_WEEK_END_DATE, formattedEndDate);

			EmailConfiguration emailConfig = getEmailConfigurationByConfigName(configName);
			if (null != emailConfig) {
				mailSubject = CommonUtils.mergeTemplateWithData(
					emailConfig.getSubject(), metaTsLastDate);				
				populateMailContentAndSaveEmailTaskLog(emailId, timesheet, metaEmailContent, mailSubject,
					emailConfig);				
			} else {
				log.error("Email template is not configured");
			}
		}
	}*/
	
	/*public void sendTimesheetNotificationMail(Employee employee, String emailId, Timesheet timesheet, String configName) {
		Map<String, Object> metaTsLastDate = new HashMap<>();
		Map<String, Object> metaEmailContent = new HashMap<>();

		String mailSubject;
		if (null != emailId && null != timesheet && null != timesheet.getEndDate() && null != timesheet.getEmployee().getName()) {			
			String formattedEndDate = formater.format(timesheet.getEndDate());
			metaTsLastDate.put(LAST_WEEK_END_DATE, formattedEndDate);
			if(configName.equalsIgnoreCase(TIMESHEET_SUBMITTED) || 
					configName.equalsIgnoreCase(TIMESHEET_REOPENNED) || 
					configName.equalsIgnoreCase(RECRUITER_TIMESHEET_SUBMITTED)) {
				metaEmailContent.put(FIRSTNAME_LASTNAME, employee.getName());
			}
			
			EmailConfiguration emailConfig = getEmailConfigurationByConfigName(configName);
			if (null != emailConfig) {
				mailSubject = CommonUtils.mergeTemplateWithData(
					emailConfig.getSubject(), metaTsLastDate);				
				populateMailContentAndSaveEmailTaskLog(emailId, timesheet, metaEmailContent, mailSubject,
					emailConfig);				
			} else {
				log.error("Email template is not configured");
			}
		}
	}*/
	
	public void sendTimesheetNotificationMail(String emailId, String tempPassword, String configName, String userName, String keycloakId) {
		//Map<String, Object> metaTsLastDate = new HashMap<>();
		Map<String, Object> metaEmailContent = new HashMap<>();

		String mailUrl = DOMAIN_URL+"/"+MAIL_CONTEXT_PATH+"/"+APP_MODULE +"/"+keycloakId;

		String mailSubject;
		if (null != emailId && null != tempPassword) {			
				metaEmailContent.put(FIRSTNAME_LASTNAME, userName);
				metaEmailContent.put(TEMP_PASSWORD, tempPassword);
				metaEmailContent.put(USERNAME, userName);
				metaEmailContent.put(CREATE_PASS_LINK, mailUrl);
//				metaEmailContent.put(CREATE_PASS_LINK, "http://192.168.6.35/timetrackui/create-password/88e17d7f-f8b8-4e39-943e-c4afbd17c273");
			EmailConfiguration emailConfig = getEmailConfigurationByConfigName(configName);
			if (null != emailConfig) {
				mailSubject = emailConfig.getSubject();		
				populateMailContentAndSaveEmailTaskLog(emailId, metaEmailContent, mailSubject,
					emailConfig);				
			} else {
				log.error("Email template is not configured");
			}
		}
	}

	private void populateMailContentAndSaveEmailTaskLog(String emailId, Map<String, Object> metaEmailContent,
			String mailSubject, EmailConfiguration emailConfig) {
		
		EmailSettings emailSettingsVO = getEmailSettingsBySenderName();
		if (null != emailSettingsVO) {
			String msg;
			msg = emailConfig.getEmailContent();
			
			
			String emailWithHtmlContent = CommonUtils
					.mergeTemplateWithData(msg, metaEmailContent);
			if (emailConfig.getEmailFilter() == EmailFilter.Y) {
				try {
//					sendMail(emailConfig.getDefaultRecipient(), mailSubject, emailSettingsVO,
//							emailWithHtmlContent);
					sendMail(emailId, mailSubject, emailSettingsVO,
							emailWithHtmlContent);
				} catch (Exception msgException) {
					log.error(EXCEPTION_WHILE_SENDING_MAIL,
							msgException);
				}
			} else if (emailConfig.getEmailFilter() == EmailFilter.N) {
				try {
					sendMail(emailId, mailSubject, 
							emailSettingsVO, emailWithHtmlContent);
				} catch (Exception msgException) {
					log.error(EXCEPTION_WHILE_SENDING_MAIL,
							msgException);
				}
			}
		} else {
			log.error("Email settings is not configured");
		}
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
			log.error("Emailsettings is not configured" + e);
		}
		return emailSetting;
	}

	public EmailConfiguration getEmailConfigurationByConfigName(String role) {
		EmailConfiguration emailConfiguration = new EmailConfiguration();
		if (!role.isEmpty()) {
			emailConfiguration = emailConfigurationRepository
					.findEmailConfigurationByEmailConfigName(role);
		}
		return emailConfiguration;
	}

	private void sendMail(String emailId, String mailSubject,
			EmailSettings emailSettingsVO, String metaTemplateWithHTMLData) {
		try {
			new MailSupport().sendHtmlMail(emailId,mailSubject,emailSettingsVO, metaTemplateWithHTMLData);
//			new MailSupport().sendHtmlMail(emailId, "Forgot Password", tempPassword, getEmailSettingsBySenderName(), userName);
//			emailTaskLog(emailId, timesheetId, mailSubject, metaTemplateWithHTMLData,
//					emailSettingsVO);
		} catch (RuntimeException re) {
			log.error(EXCEPTION_WHILE_SENDING_MAIL, re);
		} catch (Exception ex) {
			log.error(EXCEPTION_WHILE_SENDING_MAIL, ex);
		}
	}

	/*private void emailTaskLog(String emailId, UUID timesheetId, String mailSubject,
			String metaTemplateWithHTMLData, EmailSettings emailSettings) {
		EmailTaskLog emailTaskLog = new EmailTaskLog();
		emailTaskLog.setEmailStatus(EmailStatusEnum.NEW);
		java.sql.Date currentDate = getCurrentDate();
		emailTaskLog.setEmailSentDate(currentDate);
		emailTaskLog.setEmailFrom(emailSettings.getSenderEmail());
		emailTaskLog.setEmailTo(emailId);
		emailTaskLog.setEmailSubject(mailSubject);
		emailTaskLog.setEmailContent(metaTemplateWithHTMLData);
		emailTaskLog.setRetryCount(0);
		emailTaskLog.setTimesheetId(timesheetId);
		CommonUtils.populateAuditDetailsForSave(emailTaskLog);
		emailTaskLogRepository.saveAndFlush(emailTaskLog);
	}*/

	public static java.sql.Date getCurrentDate() {
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			Date currentDate = dateFormat.parse(dateFormat.format(date));
			return new java.sql.Date(
					currentDate.getTime());
		} catch (Exception e) {
			log.info("Key", e);
			throw new BusinessException("Date Parse Exception");
		}
	}
}
