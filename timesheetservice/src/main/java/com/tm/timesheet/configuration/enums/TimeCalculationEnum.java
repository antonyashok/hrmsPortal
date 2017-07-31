package com.tm.timesheet.configuration.enums;

public enum TimeCalculationEnum {
    W("WEEKLY"), D("DAILY");
    
    private String timeCalutaion;
    
    TimeCalculationEnum(String timeCalutaion){
        this.timeCalutaion = timeCalutaion;
    }
    
    public String timeCalutaion() {
        return timeCalutaion;
    }
    
    public static TimeCalculationEnum fromString(String timeCalutaion) {
        if (timeCalutaion != null) {
          for (TimeCalculationEnum b : TimeCalculationEnum.values()) {
            if (timeCalutaion.equalsIgnoreCase(b.timeCalutaion)) {
              return b;
            }
          }
        }
        return null;
      }
    
}
