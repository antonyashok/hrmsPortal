package com.tm.invoice.enums;

import org.apache.commons.lang3.StringUtils;

public enum RollOverType {
	Auto("Auto"), Manual("Manual");
    
    private String rollOverType;

    RollOverType(String rollOverType) {
        this.rollOverType = rollOverType;
    }

    public String status() {
        return rollOverType;
    }
    
    public static RollOverType fromString(String rollOverType) {
        if (StringUtils.isNotBlank(rollOverType)) {
          for (RollOverType b : RollOverType.values()) {
            if (rollOverType.equalsIgnoreCase(b.rollOverType)) {
              return b;
            }
          }
        }
        return null;
      }
}
