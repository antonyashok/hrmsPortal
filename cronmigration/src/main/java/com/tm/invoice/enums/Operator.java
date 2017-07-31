package com.tm.invoice.enums;

import org.apache.commons.lang3.StringUtils;

public enum Operator {
	
	ZERO("0"), ONE("1");
	
	private String operatorInput;

	Operator(String operatorInput) {
        this.operatorInput = operatorInput;
    }

    public String operatorInput() {
        return operatorInput;
    }
    
    public static Operator fromString(String operatorInput) {
        if (StringUtils.isNotBlank(operatorInput)) {
          for (Operator b : Operator.values()) {
            if (operatorInput.equalsIgnoreCase(b.operatorInput)) {
              return b;
            }
          }
        }
        return null;
      }

}