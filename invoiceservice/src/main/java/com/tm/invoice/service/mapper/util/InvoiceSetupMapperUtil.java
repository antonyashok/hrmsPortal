package com.tm.invoice.service.mapper.util;

import java.util.Date;

import com.tm.invoice.util.InvoiceCommonUtils;

public class InvoiceSetupMapperUtil {

	public static Date validateDateFields(String date) {
		if (null != date) {
			return InvoiceCommonUtils.convertStringToDate(date);
		} else {
			return null;
		}
	}

	public static Date validateNotesDateFields(String date) {
		if (null != date) {
			return InvoiceCommonUtils.convertStringToDate(date);
		} else {
			return null;
		}
	}

	public static String validateNotesStringFields(Date date) {
		if (null == date) {
			return null;
		}
		return InvoiceCommonUtils.getFormattedDate(date);
	}

	public static String validateStringFields(Date date) {
		if (null == date) {
			return null;
		}
		return InvoiceCommonUtils.getFormattedDate(date);
	}

	private InvoiceSetupMapperUtil() {

	}
}
