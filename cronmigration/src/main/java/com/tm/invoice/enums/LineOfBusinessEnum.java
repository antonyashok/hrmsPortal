package com.tm.invoice.enums;

public enum LineOfBusinessEnum {

    Telecom("Telecom"), Finance("Finance"), HealthCare("HealthCare"), Banking("Banking");


    private String value;

    /**
     * @param value
     */
    private LineOfBusinessEnum(String value) {
        this.value = value;
    }

    /**
     * @return value
     */
    public String value() {
        return this.value;
    }

}
