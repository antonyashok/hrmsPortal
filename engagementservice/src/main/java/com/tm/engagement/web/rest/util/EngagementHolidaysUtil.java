/**
 * 
 */
package com.tm.engagement.web.rest.util;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author hemanth
 *
 */
public class EngagementHolidaysUtil {

	public static Date getDate(String date) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		java.util.Date utilDate = dateFormat.parse(date);
		return new java.sql.Date(utilDate.getTime());
	}
}
