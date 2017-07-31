package com.tm.invoice.enums;

public enum DaysEnum {

    Select(""), Sun("Sun"), Mon("Mon"), Tue("Tue"), Wed("Wed"), Thu("Thu"), Fri("Fri"), Sat("Sat");

    private String value;

    /**
     * @param value
     */
    private DaysEnum(String value) {
        this.value = value;
    }

    /**
     * @return value
     */
    public String value() {
        return this.value;
    }

}
