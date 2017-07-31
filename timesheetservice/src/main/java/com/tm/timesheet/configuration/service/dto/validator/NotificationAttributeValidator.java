package com.tm.timesheet.configuration.service.dto.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tm.timesheet.configuration.domain.NotificationAttribute.ValidationTypeEnum;
import com.tm.timesheet.configuration.exception.NotificationEmailException;
import com.tm.timesheet.configuration.exception.NotificationEmailLimitExceedException;
import com.tm.timesheet.configuration.service.dto.NotificationAttributeDTO;

public class NotificationAttributeValidator
        implements ConstraintValidator<NotificationAttributeValidation, NotificationAttributeDTO> {

    private static final Logger logger =
            LoggerFactory.getLogger(NotificationAttributeValidator.class);

    @Override
    public void initialize(NotificationAttributeValidation arg0) {

    }

    @Override
    public boolean isValid(NotificationAttributeDTO notificationAttributeDTO,
            ConstraintValidatorContext arg1) {
        logger.info("Email Attribute validation");
        boolean valid = true;
        String REGEX =
                "(([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4}))(((,| , | ,){1}"
                        + "([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4}))*)";

        if (notificationAttributeDTO.getDependentNotificationAttribute() != null
                && notificationAttributeDTO.getDependentNotificationAttribute().getValidationType()
                        .toString().equals(ValidationTypeEnum.EMAIL.toString())) {
            Pattern pattern = Pattern.compile(REGEX);
            if (StringUtils.isNotBlank(notificationAttributeDTO.getNotificationAttributeValue())
                    && notificationAttributeDTO.getNotificationAttributeValue()
                            .equalsIgnoreCase("checked")
                    && StringUtils.isNotBlank(notificationAttributeDTO
                            .getDependentNotificationAttribute().getNotificationAttributeValue())) {
                String dependentAttributeValue = notificationAttributeDTO.getDependentNotificationAttribute()
                        .getNotificationAttributeValue().trim();
                String[] emailIds = dependentAttributeValue.split(",");
            
                if (emailIds.length > 10) {
                    throw new NotificationEmailLimitExceedException(notificationAttributeDTO
                            .getDependentNotificationAttribute().getNotificationAttributeValue());
                }
                Matcher matcher = pattern.matcher(dependentAttributeValue);
                if (!matcher.matches()) {
                	 throw new NotificationEmailException(notificationAttributeDTO
                             .getDependentNotificationAttribute().getNotificationAttributeValue());
                }
            }
        }
        return valid;
    }

}
