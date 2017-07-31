package com.tm.invoice.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tm.commonapi.exception.BusinessException;

public class ValidationUtil {

	private static final String ERR_MAILID_COUNT = "Only 10 email ids are allowed";
	private static final String INVALID_MAIL_ID = "Invalid email Id";

	private ValidationUtil() {

	}

	public static boolean isValid(String mailId) {
		boolean valid = true;
		String REGEX = "(([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4}))(((,| , | ,){1}"
				+ "([A-Za-z0-9_\\-\\.])+\\@([A-Za-z0-9_\\-\\.])+\\.([A-Za-z]{2,4}))*)";

		Pattern pattern = Pattern.compile(REGEX);

		String mailIds = mailId.trim();
		String[] emailIds = mailIds.split(",");

		if (emailIds.length > 10) {
			throw new BusinessException(ERR_MAILID_COUNT);
		}
		Matcher matcher = pattern.matcher(mailIds);
		if (!matcher.matches()) {
			throw new BusinessException(INVALID_MAIL_ID);
		}

		return valid;
	}

}
