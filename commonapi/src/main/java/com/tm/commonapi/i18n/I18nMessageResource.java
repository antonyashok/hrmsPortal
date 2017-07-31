package com.tm.commonapi.i18n;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class I18nMessageResource {

    @Autowired
    private MessageSource messageSource;

    /**
     * 
     * @param id
     * @return String message
     */
    public String getMessage(String id) {
        Locale locale = LocaleContextHolder.getLocale();
        String localeMessage = messageSource.getMessage(id, null, locale);
        return localeMessage;
    }

    /**
     * 
     * @param id
     * @param args
     * @return String message
     */
    public String getMessage(String id, Object[] args) {
        Locale locale = LocaleContextHolder.getLocale();
        String localeMessage = messageSource.getMessage(id, args, locale);
        return localeMessage;
    }
}
