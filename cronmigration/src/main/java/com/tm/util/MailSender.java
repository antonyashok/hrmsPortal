/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.web.util.MailSender.java
 * Author        : Annamalai
 * Date Created  : Apr 18th, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.util;

import org.springframework.mail.javamail.JavaMailSenderImpl;

public class MailSender extends JavaMailSenderImpl {
    public MailSender() {
        super.setUsername(TimesheetMailConstants.EMAIL_SUPPORT_USERNAME);
        super.setPassword(TimesheetMailConstants.EMAIL_SUPPORT_PASSWORD);
    }
}