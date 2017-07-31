package com.tm.invoice.enums;

public enum CurrencyEnum {


    USD("USD"), CAD("CAD");


    private String value;

    /**
     * @param value
     */
    private CurrencyEnum(String value) {
        this.value = value;
    }

    /**
     * @return value
     */
    public String value() {
        return this.value;
    }



}
