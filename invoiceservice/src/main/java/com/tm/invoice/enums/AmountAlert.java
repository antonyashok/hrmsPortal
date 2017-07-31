package com.tm.invoice.enums;

import org.apache.commons.lang3.StringUtils;

public enum AmountAlert {
	Y("Y"), N("N");
    
    private String amountAlert;

    AmountAlert(String amountAlert) {
        this.amountAlert = amountAlert;
    }

    public String status() {
        return amountAlert;
    }
    
    public static AmountAlert fromString(String amountAlert) {
        if (StringUtils.isNotBlank(amountAlert)) {
          for (AmountAlert b : AmountAlert.values()) {
            if (amountAlert.equalsIgnoreCase(b.amountAlert)) {
              return b;
            }
          }
        }
        return null;
      }
}
