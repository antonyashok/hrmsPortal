package com.tm.invoice.enums;

import org.apache.commons.lang3.StringUtils;

public enum GlobalInvoiceStatus {
	Active("Active"), Inactive("Inactive"),
	Hold("Hold"), Draft("Draft");
    
    private String status;

    GlobalInvoiceStatus(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }
    
    public static GlobalInvoiceStatus fromString(String status) {
        if (StringUtils.isNotBlank(status)) {
          for (GlobalInvoiceStatus b : GlobalInvoiceStatus.values()) {
            if (status.equalsIgnoreCase(b.status)) {
              return b;
            }
          }
        }
        return null;
      }
}
