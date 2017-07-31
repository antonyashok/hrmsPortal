package com.tm.invoice.enums;

import org.apache.commons.lang3.StringUtils;

public enum ActiveFlag {
	Y("Y"), N("N");
    
    private String activeFlag;

    ActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String status() {
        return activeFlag;
    }
    
    public static ActiveFlag fromString(String activeFlag) {
        if (StringUtils.isNotBlank(activeFlag)) {
          for (ActiveFlag b : ActiveFlag.values()) {
            if (activeFlag.equalsIgnoreCase(b.activeFlag)) {
              return b;
            }
          }
        }
        return null;
      }
}
