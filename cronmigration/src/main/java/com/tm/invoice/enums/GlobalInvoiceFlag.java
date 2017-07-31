package com.tm.invoice.enums;

import org.apache.commons.lang3.StringUtils;

public enum GlobalInvoiceFlag {
	Y("Y"), N("N");
    
    private String status;

    GlobalInvoiceFlag(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }
    
    public static GlobalInvoiceFlag fromString(String status) {
        if (StringUtils.isNotBlank(status)) {
          for (GlobalInvoiceFlag b : GlobalInvoiceFlag.values()) {
            if (status.equalsIgnoreCase(b.status)) {
              return b;
            }
          }
        }
        return null;
      }
}
