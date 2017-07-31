package com.tm.timesheet.configuration.enums;

import org.apache.commons.lang3.StringUtils;

public enum WorkInputEnum {
    H("HOURS"), T("TIME_STAMP");


    private String workInput;

    WorkInputEnum(String workInput) {
        this.workInput = workInput;
    }

    public String workInput() {
        return workInput;
    }
    
    public static WorkInputEnum fromString(String workInput) {
        if (StringUtils.isNotBlank(workInput)) {
          for (WorkInputEnum b : WorkInputEnum.values()) {
            if (workInput.equalsIgnoreCase(b.workInput)) {
              return b;
            }
          }
        }
        return null;
      }

}
