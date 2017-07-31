package com.tm.invoice.enums;

import org.apache.commons.lang3.StringUtils;

public enum Status {
	Active("Active"), InActive("InActive");
    
    private String status;

    Status(String status) {
        this.status = status;
    }

    public String status() {
        return status;
    }
    
    public static Status fromString(String status) {
        if (StringUtils.isNotBlank(status)) {
          for (Status b : Status.values()) {
            if (status.equalsIgnoreCase(b.status)) {
              return b;
            }
          }
        }
        return null;
      }
}
