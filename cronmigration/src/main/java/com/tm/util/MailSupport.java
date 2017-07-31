/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.web.util.MailSupport.java
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

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.tm.commonapi.exception.ApplicationException;
import com.tm.timesheet.configuration.domain.EmailSettings;

public class MailSupport {
	
	private static final String MAILER_ERROR_MESSAGE_IS_INVALID = "Mailer error : Message is invalid!";

	private static final String MAILER_ERROR_TO_FIELD_IS_INVALID = "Mailer error : To field is invalid!";

	private static final String MAILER_ERROR_SUBJECT_IS_INVALID = "Mailer error : Subject is invalid!";

	private static final Logger log = LoggerFactory.getLogger(MailSupport.class);
	
    private MailSender mailSender = new MailSender();
  
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String to, String subject, String msg, EmailSettings emailSettingsVO) {
        if (emailSettingsVO != null) {
            getConfig(emailSettingsVO);

            if (StringUtils.isBlank(to)) {
                log.error(MailSupport.MAILER_ERROR_TO_FIELD_IS_INVALID);
                throw new ApplicationException(MailSupport.MAILER_ERROR_TO_FIELD_IS_INVALID);
            } else {
                SimpleMailMessage message = new SimpleMailMessage();
                String fromEmail = System.getProperty("email.support.from");
                message.setFrom(fromEmail);
                if (to.contains(",")) {
                    message.setTo(to.split(","));
                } else {
                    message.setTo(to);
                }
                message.setSubject(subject);
                message.setText(msg);
                this.mailSender.send(message);
            }
        }
    }

    public void sendHtmlMail(String to, String subject, String message, EmailSettings emailSettings)
            throws Exception {
    	log.info("Processing request to send mail ");

        /*if (StringUtils.isBlank(TimesheetMailConstants.EMAIL_SUPPORT_FROM)) {
	        log.error("Mailer error : From field is invalid!"); 
	        throw new ApplicationException("Mailer error : From field is invalid!"); 
        } 
        else */if (StringUtils.isBlank(to)) {
        	 log.error(MailSupport.MAILER_ERROR_TO_FIELD_IS_INVALID);
            throw new ApplicationException(MailSupport.MAILER_ERROR_TO_FIELD_IS_INVALID);
        } else if (StringUtils.isBlank(subject)) {
        	log.error(MailSupport.MAILER_ERROR_SUBJECT_IS_INVALID);
            throw new ApplicationException(MailSupport.MAILER_ERROR_SUBJECT_IS_INVALID);
        } else if (StringUtils.isBlank(message)) {
        	log.error(MailSupport.MAILER_ERROR_MESSAGE_IS_INVALID);
            throw new ApplicationException(MailSupport.MAILER_ERROR_MESSAGE_IS_INVALID);
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

    public void sendMailWithAttachement(String to, String subject, String message, File attachement, EmailSettings emailSettings)
            throws Exception {

        if (StringUtils.isBlank(to)) {
        	 log.error(MailSupport.MAILER_ERROR_TO_FIELD_IS_INVALID);
            throw new ApplicationException(MailSupport.MAILER_ERROR_TO_FIELD_IS_INVALID);
        } else if (StringUtils.isBlank(subject)) {
        	log.error(MailSupport.MAILER_ERROR_SUBJECT_IS_INVALID);
            throw new ApplicationException(MailSupport.MAILER_ERROR_SUBJECT_IS_INVALID);
        } else if (StringUtils.isBlank(message)) {
        	log.error(MailSupport.MAILER_ERROR_MESSAGE_IS_INVALID);
            throw new ApplicationException(MailSupport.MAILER_ERROR_MESSAGE_IS_INVALID);
        } else {
            getConfig(emailSettings);           
            
            MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            mimeMessage.setFrom(new InternetAddress(to));  
            mimeMessage.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
            mimeMessage.setSubject(subject); 
            
            BodyPart messageBodyPart1 = new MimeBodyPart();  
            messageBodyPart1.setText(message);
            messageBodyPart1.setContent(message , "text/html"); 
            
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            
            DataSource source = new FileDataSource(attachement);  
            messageBodyPart2.setDataHandler(new DataHandler (source));
            messageBodyPart2.setFileName(attachement.getName());
            
            multipart.addBodyPart(messageBodyPart1);  
            multipart.addBodyPart(messageBodyPart2);
            mimeMessage.setContent(multipart);
            
            this.mailSender.send(mimeMessage);
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
}