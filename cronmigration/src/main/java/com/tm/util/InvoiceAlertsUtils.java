package com.tm.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvoiceAlertsUtils {
	
	private static final String ERROR_WHILE_PARSING_THE_DATE = "Error while parsing the date";

	private static final Logger log = LoggerFactory.getLogger(InvoiceAlertsUtils.class);
	
	private static final DateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	private InvoiceAlertsUtils() {
		
	}
	
	public static Boolean checkDate(Date frmDate, Date toDate, int diff) {
		Long checkDateval = 0l;
		Date formattedFromDate = null;
		Date formattedCurrentDate = null;
		try {
			formattedFromDate = simpleDateFormat.parse(simpleDateFormat.format(frmDate));
			formattedCurrentDate = simpleDateFormat.parse(simpleDateFormat.format(toDate));
			checkDateval = getDaysBetweenDates(formattedFromDate, formattedCurrentDate);
		} catch (Exception e) {
			log.error(InvoiceAlertsUtils.ERROR_WHILE_PARSING_THE_DATE+e);
			return false;
		}
		return (checkDateval >= diff) ? true : false;
	}
	
	public static Boolean checkDate(Date frmDate, Date toDate) {
		Long checkDateCompareval = 0l;
		Date formattedFromDate = null;
		Date formattedCurrentDate = null;
		try {
			formattedFromDate = simpleDateFormat.parse(simpleDateFormat.format(frmDate));
			formattedCurrentDate = simpleDateFormat.parse(simpleDateFormat.format(toDate));
			checkDateCompareval = getDaysBetweenDates(formattedFromDate, formattedCurrentDate);
		} catch (Exception e) {
			log.error(InvoiceAlertsUtils.ERROR_WHILE_PARSING_THE_DATE+e);
			return false;
		}
		return (checkDateCompareval <= 0) ? true : false;	
	}
	
	public static Double calculateBalanceAmount(Double amt, Float percentage) {
		return ((percentage / 100) * amt);
	}
	
	public static Boolean checkDateEquals(Date frmDate, Date toDate){
		log.info("checkDateEqualsval");
		//Long checkDateEqualsval = getDaysBetweenDates(frmDate, toDate);
		Date invoiceStartDate = null;
		Date currentDate = null;
		try {
			invoiceStartDate = simpleDateFormat.parse(simpleDateFormat.format(frmDate));
			currentDate = simpleDateFormat.parse(simpleDateFormat.format(toDate));
		} catch (Exception e) {
			log.error(InvoiceAlertsUtils.ERROR_WHILE_PARSING_THE_DATE+e);
			return false;
		}
		return invoiceStartDate.equals(currentDate) ? true : false;	
	}
	
	public static Long getDaysBetweenDates(Date frmDate, Date toDate) {
		return TimeUnit.MILLISECONDS.toDays(toDate.getTime() - frmDate.getTime());
	}
	
	/*public static void main(String hai[]) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		DataInputStream in = new DataInputStream(System.in);
		try {
			log.info("Enter the From date");
			String frmDate = in.readLine();
			log.info("Enter the To date");
			String toDate = in.readLine();
			log.info("Enter the Date comparsion units");
			int diff = Integer.parseInt(in.readLine());

			log.info("Enter the Amount");
			Double amt = Double.parseDouble(in.readLine());
			log.info("Enter the percentage");
			Float percentage = Float.parseFloat(in.readLine());

			InvoiceAlertsUtils obj = new InvoiceAlertsUtils();
			try {
				log.info("Method 1 O/P =" + obj.checkDate(sdf.parse(frmDate), sdf.parse(toDate), diff));
				log.info("Method 2 O/P =" + obj.checkDateCompare(sdf.parse(frmDate), sdf.parse(toDate)));
				log.info("Method 3 O/P =" + obj.calculateBalanceAmount(amt, percentage));
				log.info("Method 4 O/P =" + obj.checkDateEquals(sdf.parse(frmDate),sdf.parse(toDate)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}*/

}
