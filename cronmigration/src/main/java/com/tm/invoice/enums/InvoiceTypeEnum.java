package com.tm.invoice.enums;

public enum InvoiceTypeEnum {

    Select(""), Regular("Regular"), Irregular("Irregular"), MilestoneAccrue(
            "MilestoneAccrue"), MilestoneFixed("MilestoneFixed"), Prebill("Prebill");


    private String value;

    /**
     * @param value
     */
    private InvoiceTypeEnum(String value) {
        this.value = value;
    }

    /**
     * @return value
     */
    public String value() {
        return this.value;
    }

}
