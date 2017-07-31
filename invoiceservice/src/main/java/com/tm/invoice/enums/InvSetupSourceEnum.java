package com.tm.invoice.enums;

public enum InvSetupSourceEnum {

    Private("Private"), Global("Global");

    private String value;

    /**
     * @param value
     */
    private InvSetupSourceEnum(String value) {
        this.value = value;
    }

    /**
     * @return value
     */
    public String value() {
        return this.value;
    }

}
