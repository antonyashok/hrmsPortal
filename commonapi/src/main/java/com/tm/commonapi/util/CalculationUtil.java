package com.tm.commonapi.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang3.StringUtils;

public class CalculationUtil {
	public static String priceConversion(Double price){
		
		  DecimalFormat formatter = new DecimalFormat("0.00");
		  return price != null ? formatter.format(price) : StringUtils.EMPTY;
	}	
	
	public static String priceConversion(BigDecimal price){
		
		  DecimalFormat formatter = new DecimalFormat("0.00");
		  return price != null ? formatter.format(price) : StringUtils.EMPTY;
	}	
}

