package com.tm.invoice.mapper.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tm.commonapi.constants.InvoiceConstants;

public final class InvoiceSetupMapperUtil {
	
	private InvoiceSetupMapperUtil(){
		
	}
	
	public static String validateStringFields(Date date) {
        if (null == date) {
            return null;
        }
        return getFormattedDate(date);
    }
	 
	public static String getFormattedDate(Date startDate) {
		SimpleDateFormat fmt = new SimpleDateFormat(InvoiceConstants.DATE_FORMAT);
		return fmt.format(startDate);
	}
	
	public static Date validateDateFields(String date) {
        if (null != date) {
            return convertStringToDate(date);
        } else {
            return null;
        }
	}
	
	public static Date convertStringToDate(String date) {
		DateFormat format = new SimpleDateFormat(
				InvoiceConstants.DATE_FORMAT_OF_MMDDYYY);
		try {
			return format.parse(date);
		} catch (ParseException e) {

		}
		return null;
	}
 
}
