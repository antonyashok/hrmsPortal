package com.tm.common.util;

import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailSender extends JavaMailSenderImpl {
    public MailSender() {
        super.setUsername(CommonMailConstants.EMAIL_SUPPORT_USERNAME);
        super.setPassword(CommonMailConstants.EMAIL_SUPPORT_PASSWORD);
    }
}