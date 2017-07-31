package com.tm.commonapi.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HourValidator {

	private static final Logger logger = LoggerFactory
			.getLogger(HourValidator.class);

	private Pattern pattern;
	private Matcher matcher;

	private static final String TIME24HOURS_PATTERN1 = "([01]?[0-9]|2[0-3])";

	public HourValidator() {
		pattern = Pattern.compile(TIME24HOURS_PATTERN1);
	}

	/**
	 * Validate time in 24 hours format with regular expression
	 * 
	 * @param time
	 *            time address for validation
	 * @return true valid time fromat, false invalid time format
	 */

	public boolean validate(final String time) {
		matcher = pattern.matcher(time);
		if (matcher.matches()) {
			return true;
		} else {
			logger.info("time==>" + time);
			matcher = pattern.matcher(time);
			logger.info("matcher==>" + matcher);
			logger.info("matchersss==>" + matcher.matches());
			return matcher.matches();
		}
	}

}
