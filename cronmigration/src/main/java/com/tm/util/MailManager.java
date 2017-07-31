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
package com.tm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import com.mongodb.BasicDBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.tm.commonapi.exception.ApplicationException;
import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.invoice.dto.InvoiceDTO;
import com.tm.invoice.mongo.repository.InvoiceAttachementsRepository;
import com.tm.timesheet.configuration.domain.EmailConfiguration;
import com.tm.timesheet.configuration.domain.EmailConfiguration.EmailFilter;
import com.tm.timesheet.configuration.domain.EmailSettings;
import com.tm.timesheet.configuration.domain.EmailTaskLog;
import com.tm.timesheet.configuration.domain.EmailTaskLog.EmailStatusEnum;
import com.tm.timesheet.configuration.repository.EmailConfigurationRepository;
import com.tm.timesheet.configuration.repository.EmailSettingsRepository;
import com.tm.timesheet.configuration.repository.EmailTaskLogRepository;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.exception.RetryEmailOnExceptionStrategy;
import com.tm.timesheetgeneration.repository.TimesheetDetailsRepository;
import com.tm.timesheetgeneration.repository.TimesheetRepository;

@Component
public class MailManager {
	
	private static final String TIMESHEET = "timesheet";

	private static final String INVOICE = "invoice";

	private static final String RECEIPIENT_NAME = "$RECEIPIENT_NAME$";
	
	private static final String IG_IMG = "$igImg$";
	
	private static final String LAST_WEEK_START_DATE = "$LAST_WEEK_START_DATE$";
	
	private static final String LAST_WEEK_END_DATE = "$LAST_WEEK_END_DATE$";
	
	private static final Logger log = LoggerFactory.getLogger(MailManager.class);
	
	SimpleDateFormat formater = new SimpleDateFormat("MM/dd/yyyy");
	
	SimpleDateFormat formater1 = new SimpleDateFormat("yyyy-MM-dd");
	
	private static final String EXCEPTION_WHILE_SENDING_MAIL = "Exception while sending mail";
	
    private static final String INV_EMAIL_DELIVERY = "InvoiceGeneration";

    private static final String INVOICE_DATE = "$INVOICE_DATE$";

    private static final String BILL_TYPE = "$BILL_CYCLE_TYPE$";
	    
	
	@Autowired
	EmailTaskLogRepository emailTaskLogRepository;
	
	@Autowired
	EmailSettingsRepository emailSettingsRepository;
	
	@Autowired
	EmailConfigurationRepository emailConfigurationRepository;
	
	@Autowired
	TimesheetRepository timesheetRepository;
	
	@Autowired
	TimesheetDetailsRepository timesheetDetailsRepository;
	
	@Autowired
	InvoiceAttachementsRepository invoiceAttachementsRepository;
		
	@Autowired
    MongoTemplate mongoTemplate;
	
	public void sendTimesheetNotificationMail(String emailId, Timesheet timesheet, String configName) {
		Map<String, Object> metaTsLastDate = new HashMap<>();
		Map<String, Object> metaEmailContent = new HashMap<>();

		String mailSubject;
		if (null != timesheet && null != timesheet.getEndDate() && null != timesheet.getEmployee().getName()) {			
			String formattedEndDate = formater.format(timesheet.getEndDate());
			metaTsLastDate.put(MailManager.LAST_WEEK_END_DATE, formattedEndDate);

			EmailConfiguration emailConfig = getEmailConfigurationByConfigName(configName);
			if (null != emailConfig) {
				mailSubject = CommonUtils.mergeTemplateWithData(
					emailConfig.getSubject(), metaTsLastDate);				
				populateMailContentAndSaveEmailTaskLog(emailId, timesheet, metaEmailContent, mailSubject,
					emailConfig);				
			}
		}
	}
	
	public EmailConfiguration getEmailConfigurationByConfigName(String role) {
		EmailConfiguration emailConfiguration = new EmailConfiguration();
		if (!role.isEmpty()) {
			emailConfiguration = emailConfigurationRepository
					.findEmailConfigurationByEmailConfigName(role);
		}
		return emailConfiguration;
	}
	
	private void populateMailContentAndSaveEmailTaskLog(String emailId, Timesheet timesheet, Map<String, Object> metaEmailContent,
			String mailSubject, EmailConfiguration emailConfig) {
		
		EmailSettings emailSettingsVO = getEmailSettingsBySenderName();
		if (null != emailSettingsVO) {
			String msg;
			msg = emailConfig.getEmailContent();
			metaEmailContent.put(RECEIPIENT_NAME,
					CommonUtils.getRecipientName(timesheet.getEmployee().getName()));
			metaEmailContent.put(IG_IMG, CommonUtils.formLogoImageURL());
			metaEmailContent.put(MailManager.LAST_WEEK_START_DATE, formater.format(timesheet.getStartDate()));
			metaEmailContent.put(MailManager.LAST_WEEK_END_DATE, formater.format(timesheet.getEndDate()));
			
			String emailWithHtmlContent = CommonUtils
					.mergeTemplateWithData(msg, metaEmailContent);
			if (emailConfig.getEmailFilter() == EmailFilter.Y) {
				try {
					saveToEmailTaskLog(emailConfig.getDefaultRecipient(), timesheet.getId(),
							mailSubject, emailSettingsVO,
							emailWithHtmlContent, MailManager.TIMESHEET);
				} catch (Exception msgException) {
					log.error(EXCEPTION_WHILE_SENDING_MAIL,
							msgException);
				}
			} else if (emailConfig.getEmailFilter() == EmailFilter.N) {
				try {
					saveToEmailTaskLog(emailId, timesheet.getId(), mailSubject, 
							emailSettingsVO, emailWithHtmlContent, MailManager.TIMESHEET);
				} catch (Exception msgException) {
					log.error(EXCEPTION_WHILE_SENDING_MAIL,
							msgException);
				}
			}
		}
	}
	
	public void sendEmailByEmailTskLogId(List<UUID> emailTaskLogIdList) {
		emailTaskLogIdList.forEach(emailTaskLogId -> {
			EmailTaskLog emailTskLog = emailTaskLogRepository.findOne(emailTaskLogId);
			if (emailTskLog != null && emailTskLog.getRetryCount() <= RetryEmailOnExceptionStrategy.DEFAULT_RETRIES) {
				try {
					
					if(null != emailTskLog.getTimesheetId()) {
						new MailSupport().sendHtmlMail(emailTskLog.getEmailTo(), emailTskLog.getEmailSubject(),
								emailTskLog.getEmailContent(), getEmailSettingsBySenderName());
					}
					if(null != emailTskLog.getInvoiceQueueId()) {						
						new MailSupport().sendMailWithAttachement(emailTskLog.getEmailTo(), emailTskLog.getEmailSubject(),
								emailTskLog.getEmailContent(), getAttachementInputStream(emailTskLog), getEmailSettingsBySenderName());
					} 
					emailTskLog.setEmailStatus(EmailStatusEnum.SUCCESS);
					CommonUtils.populateAuditDetailsForUpdate(emailTskLog);
					emailTaskLogRepository.save(emailTskLog);
				} catch (Exception e) {
					log.error("Error while send the mail for this  EmailTskLogId {}", emailTaskLogId, e);
					emailTskLog.setEmailStatus(EmailStatusEnum.FAILURE);
					emailTskLog.setRetryCount(emailTskLog.getRetryCount() + 1);
					CommonUtils.populateAuditDetailsForUpdate(emailTskLog);
					emailTaskLogRepository.save(emailTskLog);
				}
			}
		});
	}

	private File getAttachementInputStream(EmailTaskLog emailTskLog) {
		
		BasicDBObject query = new BasicDBObject();
		query.put("sourceReferenceId", emailTskLog.getInvoiceQueueId().toString());
		query.put("subType", "Invoice");
		
		GridFS gridFS = new GridFS(mongoTemplate.getDb(),
				"invoice");
		GridFSDBFile gridFSDBfile = gridFS.findOne(query);
		InputStream inputStream = null;
		try {
			inputStream = gridFSDBfile.getInputStream();
		} catch (Exception e) {
		    throw new ApplicationException("Exception occured while getting attachements :: " + e);
		} finally {
			if(inputStream != null) {
				CommonUtils.inputStreamSafeClose(inputStream);
			}
		}
		
		File attachement = null; 
		try {
			attachement = stream2file(inputStream, gridFSDBfile.getFilename(), gridFSDBfile.getContentType().split("/")[1]);
		} catch (IOException e) {
			log.error("Error while getAttachementInputStream() :: "+e);
		}
		return attachement;
	}
	
	private static File stream2file (InputStream in, String PREFIX, String SUFFIX) throws IOException {
        File tempFile = File.createTempFile(PREFIX, SUFFIX);
        tempFile.deleteOnExit();
        String tDir = System.getProperty("java.io.tmpdir");        
        tempFile = new File(tDir, PREFIX +"." + SUFFIX);
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        } catch(IOException exp) {
        	log.error("InvoiceEngine :: Attachements -- Error while converting the inputstream to file :: "+exp);
        }
        return tempFile;
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
	
	public Long sendMailtoRecruiter(Long employeeId, String emailId, UUID timesheetId, String recipientName) {
        String[] splitRecipientName;
        String recipientNameSecondname = "";
        String recipientNameFirstname = "";
		
		if (null != employeeId && null != timesheetId && null != emailId && StringUtils.isNotBlank(recipientName)) {
			Map<String, Object> metaTsLastDate = new HashMap<>();
			EmailConfiguration emailConfiguration = getEmailConfigurationByRole(
					TimesheetMailConstants.RECRUITER_TIMESHEET_CREATED);

			if (null == emailConfiguration) {
				return 0l;
			}

			Timesheet timesheet = getTimesheetByEmplId(timesheetId);
			metaTsLastDate.put(LAST_WEEK_START_DATE, formater.format(timesheet.getStartDate()));
			metaTsLastDate.put(LAST_WEEK_END_DATE, formater.format(timesheet.getEndDate()));

			String mailSubject = CommonUtils.mergeTemplateWithData(emailConfiguration.getSubject(), metaTsLastDate);

			splitRecipientName = recipientName.split(",");
			
			if(splitRecipientName.length > 0) {
				recipientNameSecondname = splitRecipientName[0];
	            recipientNameFirstname = splitRecipientName[1];
			}
            
			Map<String, Object> metaTimesheetData = new HashMap<>();
			metaTimesheetData.put(RECEIPIENT_NAME, recipientNameFirstname + " " + recipientNameSecondname);
			
			EmailSettings emailSettingsVO = getEmailSettingsBySenderName();
			if (null == emailSettingsVO) {
				return 0l;
			}

			String metaDataWithTemplate = emailConfiguration.getEmailContent();			
			String metaTemplateWithHTMLData = CommonUtils.mergeTemplateWithData(metaDataWithTemplate, metaTimesheetData);

			if (emailConfiguration.getEmailFilter() == EmailFilter.Y) {
				saveToEmailTaskLog(emailConfiguration.getDefaultRecipient(), timesheetId, mailSubject, 
						emailSettingsVO, metaTemplateWithHTMLData, MailManager.TIMESHEET);
			} else if (emailConfiguration.getEmailFilter() == EmailFilter.N) {
				saveToEmailTaskLog(emailId, timesheetId, mailSubject, emailSettingsVO,
						metaTemplateWithHTMLData, MailManager.TIMESHEET);
			}
		}
		return employeeId;
	}
	
	private void saveToEmailTaskLog(String emailId, UUID timesheetId, String mailSubject,
			EmailSettings emailSettingsVO,
			String metaTemplateWithHTMLData, String moduleName) {
		try {
			saveEmailTaskLog(emailId, timesheetId, mailSubject, metaTemplateWithHTMLData, emailSettingsVO, moduleName);
		} catch (RuntimeException re) {
			log.error(EXCEPTION_WHILE_SENDING_MAIL, re);
		} catch (Exception ex) {
			log.error(EXCEPTION_WHILE_SENDING_MAIL, ex);
		}
	}

	private void saveEmailTaskLog(String emailId, UUID referenceId, String mailSubject, String metaTemplateWithHTMLData,
			EmailSettings emailSettings, String moduleName) {
		EmailTaskLog emailTaskLog = new EmailTaskLog();
		emailTaskLog.setEmailStatus(EmailStatusEnum.NEW);
		java.sql.Date currentDate = TimeSheetCommonUtils.getCurrentDate();
		emailTaskLog.setEmailSentDate(currentDate);
		emailTaskLog.setEmailFrom(emailSettings.getSenderEmail());
		emailTaskLog.setEmailTo(emailId);
		emailTaskLog.setEmailSubject(mailSubject);
		emailTaskLog.setEmailContent(metaTemplateWithHTMLData);
		emailTaskLog.setRetryCount(0);
		if(moduleName.equals(TIMESHEET)) {
			emailTaskLog.setTimesheetId(referenceId);
		} else if(moduleName.equals(INVOICE)) {
			emailTaskLog.setInvoiceQueueId(referenceId);
		}
		CommonUtils.populateAuditDetailsForSave(emailTaskLog);
		emailTaskLogRepository.saveAndFlush(emailTaskLog);
	}

	public Timesheet getTimesheetByEmplId(UUID timesheetId) {
		return timesheetRepository.findOne(timesheetId);
	}	

	public EmailConfiguration getEmailConfigurationByRole(String role) {
		EmailConfiguration emailConfiguration = new EmailConfiguration();
		if (!role.isEmpty()) {
			emailConfiguration = emailConfigurationRepository.findEmailConfigurationByEmailConfigName(role);
		}
		return emailConfiguration;
	}
	
	
    public void sendEmailForInvoice(InvoiceDTO invoice) {
        Map<String, Object> metaTsLastDate = new HashMap<>();
        Map<String, Object> metaEmailContent = new HashMap<>();
        String configName = INV_EMAIL_DELIVERY;
        String mailSubject;
        String billToManagerName = null;

        if ( null != invoice.getRunCronDate()) {
            billToManagerName = invoice.getBillToManager().getBillToMgrName();
            metaTsLastDate.put(INVOICE_DATE, formater1.format(new Date()));
        }

        EmailConfiguration emailConfig = getEmailConfigurationByConfigName(configName);
        if (null != emailConfig) {
            mailSubject =
                    CommonUtils.mergeTemplateWithData(emailConfig.getSubject(), metaTsLastDate);
            populateMailContentAndSaveEmailTaskLogForInvoice(invoice.getBillToManagerEmailId(), billToManagerName, invoice,
                    metaEmailContent, mailSubject, emailConfig);
        } else {
            log.error("Email template is not configured");
        }
    }

    public void populateMailContentAndSaveEmailTaskLogForInvoice(String billToManagerEmail,
            String billToManagerName, InvoiceDTO invoice, Map<String, Object> metaEmailContent,
            String mailSubject, EmailConfiguration emailConfig) {
        EmailSettings emailSettingsVO = getEmailSettingsBySenderName();
        LocalDate invoiceDate;
        String billCycleType;
        if (null != emailSettingsVO) {
            String msg;
            msg = emailConfig.getEmailContent();
            metaEmailContent.put(RECEIPIENT_NAME, billToManagerName);
            metaEmailContent.put(IG_IMG, CommonUtils.formLogoImageURL());
            if (null != invoice.getInvoiceSetup().getInvoiceType()) {
                billCycleType = invoice.getInvoiceSetup().getInvoiceType();
                metaEmailContent.put(BILL_TYPE, billCycleType);
            }
            if (null != invoice.getRunCronDate()) {
                invoiceDate = invoice.getRunCronDate();
                metaEmailContent.put(INVOICE_DATE, invoiceDate);
            }

            String emailWithHtmlContent = CommonUtils.mergeTemplateWithData(msg, metaEmailContent);
            if (emailConfig.getEmailFilter() == EmailFilter.Y) {
                try {
                    saveToEmailTaskLog(emailConfig.getDefaultRecipient(), invoice.getInvoiceQueueId(),
                            mailSubject, emailSettingsVO, emailWithHtmlContent, MailManager.INVOICE);
                } catch (Exception msgException) {
                    log.error(EXCEPTION_WHILE_SENDING_MAIL, msgException);
                }
            } else if (emailConfig.getEmailFilter() == EmailFilter.N) {
                try {
                    saveToEmailTaskLog(billToManagerEmail, invoice.getInvoiceQueueId(), mailSubject,
                            emailSettingsVO, emailWithHtmlContent, MailManager.INVOICE);
                } catch (Exception msgException) {
                    log.error(EXCEPTION_WHILE_SENDING_MAIL, msgException);
                }
            }
        } else {
            log.error("Email settings is not configured");
        }
    }

    @SuppressWarnings("unused")
	private String populateCompanyLogo(EmailTaskLog emailTskLog) {
    	Map<String, Object> metaEmailContent = new HashMap<>();
    	BasicDBObject query = new BasicDBObject();
		query.put("sourceReferenceId", emailTskLog.getCompanyLogoId().toString());
		
		GridFS gridFS = new GridFS(mongoTemplate.getDb(),
				"timesheet");
		GridFSDBFile gridFSDBfile = gridFS.findOne(query);
		InputStream inputStream = null;
		try {
			inputStream = gridFSDBfile.getInputStream();
			
			byte[] bytes = IOUtils.toByteArray(inputStream);
			
			byte[] encodeBase64 = Base64.encodeBase64(bytes);
			String base64DataString = new String(encodeBase64 , "UTF-8");
			
			metaEmailContent.put(IG_IMG, "data:image/jpeg;base64, "+base64DataString);
			emailTskLog.setEmailContent(CommonUtils.mergeTemplateWithData(emailTskLog.getEmailContent(), metaEmailContent));			
		} catch (Exception e) {
		    throw new ApplicationException("Exception occured while getting attachements :: " + e);
		} finally {
			if(inputStream != null) {
				CommonUtils.inputStreamSafeClose(inputStream);
			}
		}
		return emailTskLog.getEmailContent();
    }
}