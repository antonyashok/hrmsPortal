package com.tm.timesheet.web.rest.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.tm.commonapi.exception.ApplicationException;
import com.tm.commonapi.exception.BusinessException;
import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.timesheet.configuration.domain.EmailConfiguration;
import com.tm.timesheet.configuration.domain.EmailConfiguration.EmailFilter;
import com.tm.timesheet.configuration.domain.EmailSettings;
import com.tm.timesheet.configuration.domain.EmailTaskLog;
import com.tm.timesheet.configuration.domain.EmailTaskLog.EmailStatusEnum;
import com.tm.timesheet.configuration.repository.EmailConfigurationRepository;
import com.tm.timesheet.configuration.repository.EmailSettingsRepository;
import com.tm.timesheet.configuration.repository.EmailTaskLogRepository;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.Employee;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.timeoff.domain.Timeoff;
import com.tm.timesheet.configuration.service.dto.EmployeeProfileDTO;

@Component
public class MailManagerUtil {

	private static final String EMAIL_TEMPLATE_IS_NOT_CONFIGURED = "Email template is not configured";

	private static final Logger log = LoggerFactory
			.getLogger(MailManagerUtil.class);
	
	private static final String EXCEPTION_WHILE_SENDING_MAIL = "Exception while sending mail";	
	private static final String RECEIPIENT_NAME = "$RECEIPIENT_NAME$";	
	private static final String IG_IMG = "$igImg$";	
	private static final String LAST_WEEK_START_DATE = "$LAST_WEEK_START_DATE$";	
	private static final String LAST_WEEK_END_DATE = "$LAST_WEEK_END_DATE$";	
	private static final String FIRSTNAME_LASTNAME = "$FIRSTNAME_LASTNAME$";
	private static final String LEAVE_PERIOD = "$LEAVE_PERIOD$";

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
	public static final String TIMEOFF_SUBMITTED = "TimeOffSubmitted";
	public static final String TIMEOFF_APPROVED = "TimeOffApproved";
	public static final String TIMEOFF_REJECTED = "TimeOffRejected";

	public static final String MAIL_TRANSFER_PROTOCOL = "mail.transport.protocol";
	public static final String MAIL_SMTP_AUTHENTICATION = "mail.smtp.auth";
	public static final String MAIL_START_ENABLE = "mail.smtp.starttls.enable";
	public static final String MAIL_SMTP_HOST = "mail.smtp.host";
	public static final String MAIL_SMTP_PORT = "mail.smtp.port";
	
	private static final String MAILER_ERROR_MESSAGE_IS_INVALID = "Mailer error : Message is invalid!";
	private static final String MAILER_ERROR_TO_FIELD_IS_INVALID = "Mailer error : To field is invalid!";
	private static final String MAILER_ERROR_SUBJECT_IS_INVALID = "Mailer error : Subject is invalid!";

	EmailSettingsRepository emailSettingsRepository;

	EmailConfigurationRepository emailConfigurationRepository;

	EmailTaskLogRepository emailTaskLogRepository;
	
	private MailSender mailSender = new MailSender();
	
	@Inject
	public MailManagerUtil(EmailSettingsRepository emailSettingsRepository,
			EmailConfigurationRepository emailConfigurationRepository,
			EmailTaskLogRepository emailTaskLogRepository) {
		this.emailSettingsRepository = emailSettingsRepository;
		this.emailConfigurationRepository = emailConfigurationRepository;
		this.emailTaskLogRepository = emailTaskLogRepository;
	}
	
	/*public void sendTimesheetNotificationMail(Employee employee, EmployeeProfileDTO reportingEmployeeProfile, 
			Timesheet timesheet, String configName,String emailPriority) {
		Map<String, Object> metaTsLastDate = new HashMap<>();
		Map<String, Object> metaEmailContent = new HashMap<>();

		String mailSubject;
		if (null != employee.getPrimaryEmailId() && null != timesheet
				&& null != timesheet.getEndDate()
				&& null != timesheet.getEmployee().getName()) {			
			String formattedEndDate = formater.format(timesheet.getEndDate());
			metaTsLastDate.put(LAST_WEEK_END_DATE, formattedEndDate);
			metaTsLastDate.put(FIRSTNAME_LASTNAME, employee.getName());
			EmailConfiguration emailConfig = getEmailConfigurationByConfigName(configName);
			if (null != emailConfig) {
				mailSubject = CommonUtils.mergeTemplateWithData(
					emailConfig.getSubject(), metaTsLastDate);
				metaEmailContent.put(FIRSTNAME_LASTNAME, employee.getName());
				populateMailContentAndSaveEmailTaskLog(employee
	    				.getPrimaryEmailId(), timesheet, metaEmailContent, mailSubject,
					emailConfig,emailPriority);				
			} else {
				log.error(MailManagerUtil.EMAIL_TEMPLATE_IS_NOT_CONFIGURED);
			}
		}
	}*/
	
	public void sendTimesheetNotificationMail(Employee employee,
			EmployeeProfileDTO reportingEmployeeProfile, String emailId, Timesheet timesheet,
			String configName, String emailPriority) {
		Map<String, Object> metaTsLastDate = new HashMap<>();
		Map<String, Object> metaEmailContent = new HashMap<>();
		
		String mailSubject;
		if (null != reportingEmployeeProfile.getReportingManagerEmailId()
				&& null != timesheet && null != timesheet.getEndDate()
				&& null != timesheet.getEmployee().getName()) {			
			String formattedEndDate = formater.format(timesheet.getEndDate());
			metaTsLastDate.put(LAST_WEEK_END_DATE, formattedEndDate);
			metaTsLastDate.put(FIRSTNAME_LASTNAME, employee.getName());
			
			if(configName.equalsIgnoreCase(TIMESHEET_REOPENNED) || 
					configName.equalsIgnoreCase(TIMESHEET_SUBMITTED) || 
					configName.equalsIgnoreCase(RECRUITER_TIMESHEET_SUBMITTED)) {
				metaEmailContent.put(RECEIPIENT_NAME, CommonUtils
						.getRecipientName(reportingEmployeeProfile
								.getFirstName()+ " " + reportingEmployeeProfile.getLastName()));				
				metaEmailContent.put(FIRSTNAME_LASTNAME, employee.getName());
			} else if(configName.equalsIgnoreCase(TIMESHEET_APPROVED) || 
					configName.equalsIgnoreCase(TIMESHEET_REJECTED)) {
				metaEmailContent.put(RECEIPIENT_NAME, CommonUtils
						.getRecipientName(employee.getName()));
			}
			
			EmailConfiguration emailConfig = getEmailConfigurationByConfigName(configName);
			if (null != emailConfig) {
				mailSubject = CommonUtils.mergeTemplateWithData(
					emailConfig.getSubject(), metaTsLastDate);				
				populateMailContentAndSaveEmailTaskLog(emailId, timesheet, metaEmailContent, mailSubject,
					emailConfig,emailPriority);				
			} else {
				log.error(MailManagerUtil.EMAIL_TEMPLATE_IS_NOT_CONFIGURED);
			}
		}
	}

	private void populateMailContentAndSaveEmailTaskLog(String emailId, Timesheet timesheet, Map<String, Object> metaEmailContent,
			String mailSubject, EmailConfiguration emailConfig,String emailPriority) {
		
		EmailSettings emailSettingsVO = getEmailSettingsBySenderName();
		if (null != emailSettingsVO) {
			String msg;
			msg = emailConfig.getEmailContent();
			/*metaEmailContent.put(RECEIPIENT_NAME,
					CommonUtils.getRecipientName(timesheet.getEmployee().getName()));*/
			metaEmailContent.put(IG_IMG, CommonUtils.formLogoImageURL());			
			metaEmailContent.put(LAST_WEEK_START_DATE, formater.format(timesheet.getStartDate()));
			metaEmailContent.put(LAST_WEEK_END_DATE, formater.format(timesheet.getEndDate()));
			
			String emailWithHtmlContent = CommonUtils
					.mergeTemplateWithData(msg, metaEmailContent);
			if (emailConfig.getEmailFilter() == EmailFilter.Y) {
				try {
					sendMail(emailConfig.getDefaultRecipient(), timesheet.getId(),
							mailSubject, emailSettingsVO,
							emailWithHtmlContent,emailPriority);
				} catch (Exception msgException) {
					log.error(EXCEPTION_WHILE_SENDING_MAIL,
							msgException);
				}
			} else if (emailConfig.getEmailFilter() == EmailFilter.N) {
				try {
					sendMail(emailId, timesheet.getId(), mailSubject, 
							emailSettingsVO, emailWithHtmlContent,emailPriority);
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

	private void sendMail(String emailId, UUID timesheetId, String mailSubject,
			EmailSettings emailSettingsVO, String metaTemplateWithHTMLData,String maillPriority) {
		EmailTaskLog emailTaskLog = new EmailTaskLog();
		try {
			if(TimesheetConstants.MAIL_HIGH_PRIORITY.equals(maillPriority)){
				sendHtmlMail(emailId, mailSubject, emailSettingsVO, metaTemplateWithHTMLData);
				emailTaskLog.setEmailStatus(EmailStatusEnum.SUCCESS);
			}else{
				emailTaskLog.setEmailStatus(EmailStatusEnum.NEW);
			}
			emailTaskLog(emailId, timesheetId, mailSubject, metaTemplateWithHTMLData,
					emailSettingsVO, emailTaskLog);
		} catch (RuntimeException re) {
			log.error(EXCEPTION_WHILE_SENDING_MAIL, re);
		} catch (Exception ex) {
			log.error(EXCEPTION_WHILE_SENDING_MAIL, ex);
		}
	}

	private void emailTaskLog(String emailId, UUID timesheetId, String mailSubject,
			String metaTemplateWithHTMLData, EmailSettings emailSettings, EmailTaskLog emailTaskLog) {
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
	}

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
	
	@Value("${server.context-path}")
	public String CONTEXT_PATH_URL;
	
	 public void sendHtmlMail(String to, String subject, EmailSettings emailSettings, String message)
	            throws Exception {
	    	if (StringUtils.isBlank(to)) {
	        	 log.error(MAILER_ERROR_TO_FIELD_IS_INVALID);
	            throw new ApplicationException(MAILER_ERROR_TO_FIELD_IS_INVALID);
	        } else if (StringUtils.isBlank(subject)) {
	        	log.error(MAILER_ERROR_SUBJECT_IS_INVALID);
	            throw new ApplicationException(MAILER_ERROR_SUBJECT_IS_INVALID);
	        } else if (StringUtils.isBlank(message)) {
	        	log.error(MAILER_ERROR_MESSAGE_IS_INVALID);
	            throw new ApplicationException(MAILER_ERROR_MESSAGE_IS_INVALID);
	        } else {
	            getConfig(emailSettings);
	            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
	            mimeMessage.setContent(message, "text/html");
	            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	            helper.setFrom(emailSettings.getSenderEmail());
	            if (to.contains(",")) {
	                helper.setTo(to.split(","));
	            } else {
	                helper.setTo(to);
	            }

	            helper.setSubject(subject);
	            helper.setText(message, true);

	            this.mailSender.send(mimeMessage);
	            log.info("Mail has been sent with HTML content.");
	        }
	    }
	 
	 private void getConfig(EmailSettings emailSettings) {
	        mailSender.setHost(emailSettings.getHost());
	        mailSender.setPort(Integer.parseInt(emailSettings.getPort()));
	        mailSender.setUsername(emailSettings.getSenderEmail());
	        Properties javaMailProperties = new Properties();
	        String smtpgmail = "smtp.gmail.com";
	        String appHost = emailSettings.getHost();
	        javaMailProperties.put("mail.transport.protocol", "smtp");
	        if (StringUtils.isNotBlank(appHost) && StringUtils.equalsIgnoreCase(appHost, smtpgmail)) {
	            mailSender.setPassword(emailSettings.getSenderp());
	            javaMailProperties.put("mail.smtp.auth", "true");
	            javaMailProperties.put("mail.smtp.starttls.enable", "true");
	            javaMailProperties.put("mail.smtp.host", smtpgmail);
	            javaMailProperties.put("mail.smtp.port", "587");
	            javaMailProperties.put("mail.smtp.ssl.trust", smtpgmail);
	        }
	        mailSender.setJavaMailProperties(javaMailProperties);
	    }
	 
	public void sendTimeOffNotificationMail(
			EmployeeProfileDTO loggedInUserProfile,
			EmployeeProfileDTO reportingEmployeeProfile, String emailId,
			Timeoff timeOff, String configName, String emailPriority) {
			Map<String, Object> metaTsLastDate = new HashMap<>();
			Map<String, Object> metaEmailContent = new HashMap<>();

			String mailSubject;
			if (null != emailId && null != timeOff && null != timeOff.getEndDate() && null != timeOff.getEmployeeName()) {			
				String formattedEndDate = formater.format(timeOff.getEndDate());
				metaTsLastDate.put(LAST_WEEK_END_DATE, formattedEndDate);
				metaTsLastDate.put(LEAVE_PERIOD, timeOff.getStartDateStr() + " - " + timeOff.getEndDateStr());
				if(configName.equalsIgnoreCase(TIMEOFF_SUBMITTED)) {
					metaTsLastDate.put(FIRSTNAME_LASTNAME, loggedInUserProfile.getFirstName()+" "+loggedInUserProfile.getLastName());
				}
				
				
				if(configName.equalsIgnoreCase(TIMESHEET_SUBMITTED) || 
						configName.equalsIgnoreCase(TIMESHEET_REOPENNED) || 
						configName.equalsIgnoreCase(RECRUITER_TIMESHEET_SUBMITTED) ||
						configName.equalsIgnoreCase(TIMEOFF_SUBMITTED)) {
					metaEmailContent.put(RECEIPIENT_NAME, CommonUtils
							.getRecipientName(reportingEmployeeProfile
									.getFirstName()+ " " + reportingEmployeeProfile.getLastName()));
				} else if(configName.equalsIgnoreCase(TIMEOFF_APPROVED) ||
						configName.equalsIgnoreCase(TIMEOFF_REJECTED)){
					metaEmailContent.put(RECEIPIENT_NAME, CommonUtils
							.getRecipientName(loggedInUserProfile.getFirstName()+" "+loggedInUserProfile.getLastName()));
				}
				metaEmailContent.put(FIRSTNAME_LASTNAME, loggedInUserProfile.getFirstName()+" "+loggedInUserProfile.getLastName());
				
				EmailConfiguration emailConfig = getEmailConfigurationByConfigName(configName);
				if (null != emailConfig) {
					mailSubject = CommonUtils.mergeTemplateWithData(
						emailConfig.getSubject(), metaTsLastDate);				
					populateMailContentAndSaveEmailTaskLog(emailId, timeOff, metaEmailContent, mailSubject,
						emailConfig,emailPriority);				
				} else {
					log.error(MailManagerUtil.EMAIL_TEMPLATE_IS_NOT_CONFIGURED);
				}
			}
		}
	 
	 private void populateMailContentAndSaveEmailTaskLog(String emailId, Timeoff timeoff, Map<String, Object> metaEmailContent,
				String mailSubject, EmailConfiguration emailConfig,String emailPriority) {
			
			EmailSettings emailSettingsVO = getEmailSettingsBySenderName();
			if (null != emailSettingsVO) {
				String msg;
				msg = emailConfig.getEmailContent();
				/*metaEmailContent.put(RECEIPIENT_NAME,
						CommonUtils.getRecipientName(timeoff.getEmployeeName()));*/
				metaEmailContent.put(IG_IMG, CommonUtils.formLogoImageURL());			
				metaEmailContent.put(LAST_WEEK_START_DATE, formater.format(timeoff.getStartDate()));
				metaEmailContent.put(LAST_WEEK_END_DATE, formater.format(timeoff.getEndDate()));
				
				String emailWithHtmlContent = CommonUtils
						.mergeTemplateWithData(msg, metaEmailContent);
				if (emailConfig.getEmailFilter() == EmailFilter.Y) {
					try {
						sendMail(emailConfig.getDefaultRecipient(), timeoff.getId(),
								mailSubject, emailSettingsVO,
								emailWithHtmlContent,emailPriority);
					} catch (Exception msgException) {
						log.error(EXCEPTION_WHILE_SENDING_MAIL,
								msgException);
					}
				} else if (emailConfig.getEmailFilter() == EmailFilter.N) {
					try {
						sendMail(emailId, timeoff.getId(), mailSubject, 
								emailSettingsVO, emailWithHtmlContent,emailPriority);
					} catch (Exception msgException) {
						log.error(EXCEPTION_WHILE_SENDING_MAIL,
								msgException);
					}
				}
			} else {
				log.error("Email settings is not configured");
			}
		}
}
