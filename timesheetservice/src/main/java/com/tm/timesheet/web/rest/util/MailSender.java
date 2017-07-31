package com.tm.timesheet.web.rest.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailSender extends JavaMailSenderImpl {
	@Value("${email.support.username}")
	public static String EMAIL_SUPPORT_USERNAME;
	
	@Value("${email.support.password}")
	public static String EMAIL_SUPPORT_PASSWORD;
	
    public MailSender() {
        super.setUsername(EMAIL_SUPPORT_USERNAME);
        super.setPassword(EMAIL_SUPPORT_PASSWORD);
    }
}