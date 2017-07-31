package com.tm.invoice.enums;

import org.apache.commons.lang3.StringUtils;

public enum ExpenseAlert {
    Y("Y"), N("N");
    
    private String expenseAlert;

    ExpenseAlert(String expenseAlert) {
        this.expenseAlert = expenseAlert;
    }

    public String expenseAlert() {
        return expenseAlert;
    }
    
    public static ExpenseAlert fromString(String expenseAlert) {
        if (StringUtils.isNotBlank(expenseAlert)) {
          for (ExpenseAlert b : ExpenseAlert.values()) {
            if (expenseAlert.equalsIgnoreCase(b.expenseAlert)) {
              return b;
            }
          }
        }
        return null;
      }
}
