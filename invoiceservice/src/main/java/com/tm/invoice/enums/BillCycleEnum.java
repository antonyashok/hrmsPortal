package com.tm.invoice.enums;

public enum BillCycleEnum {

    Select(""), Monthly("Monthly"), MonthlyByday("MonthlyByday"), SemiMonthly(
            "SemiMonthly"), BiWeeklyEven("BiWeeklyEven"), Weekly("Weekly");

    private String value;

    /**
     * @param value
     */
    private BillCycleEnum(String value) {
        this.value = value;
    }

    /**
     * @return value
     */
    public String value() {
        return this.value;
    }

}
