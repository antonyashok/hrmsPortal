package com.tm.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public class CommonMailConstants {

	@Value("${email.support.username}")
	public static String EMAIL_SUPPORT_USERNAME;

	@Value("${email.support.password}")
	public static String EMAIL_SUPPORT_PASSWORD;

	@Value("${host}")
	public static String HOST;

	@Value("${timesheetui.context.path}")
	public static String TIMESHEETUI_CONTEXT_PATH;

	@Value("${innothinklogo.url}")
	public static String INNOTHINKLOGO_URL;

	@Value("${email.support.from}")
	public static String EMAIL_SUPPORT_FROM;
	
	@Value("${server.context-path}")
	public static String CONTEXT_PATH_URL;
	
	public static final String INNOTHINK_LOGO_URL="http://192.168.6.35/timetrackui/assets/img/ig-icons/inno-people.png";

	public static final String TEMPLATE_NAME = "templateName";
	public static final String TIMESHEET = "timesheet";

	public static final String RECRUITER_TIMESHEET = "RecruiterAlert";
	public static final String RECRUITER_TIMESHEET_CREATED = "RecruiterTimesheetCreated";
	public static final String HR_MANAGER_NOTIFICATION = "HrManagerAlert";
	public static final String RECRUITER_TS_APPROVED_ALERT = "RecruiterTsApprovedAlert";
	public static final String RECRUITER_TS_APPROVAL_REMINDER = "ReTimeSheetReminder";
	public static final String PAYROLL_AND_HR_MANAGER_ALERT = "Payroll&HRManagerAlert";
	public static final String REGIONAL_MANAGER_ALERT = "RegionalManagerAlert";
	
	public static final String CREATE_EMPLOYEE_MAIL = "CreateEmployee";
	public static final String FORGOT_PSWORD_MAIL = "ForgotPassword";
	
	 public static final String MAIL_TRANSFER_PROTOCOL="mail.transport.protocol";
	 public static final String MAIL_SMTP_AUTHENTICATION="mail.smtp.auth";
	 public static final String MAIL_START_ENABLE="mail.smtp.starttls.enable";
	 public static final String MAIL_SMTP_HOST="mail.smtp.host";
	 public static final String MAIL_SMTP_PORT="mail.smtp.port";
	   


	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInLocal() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}
