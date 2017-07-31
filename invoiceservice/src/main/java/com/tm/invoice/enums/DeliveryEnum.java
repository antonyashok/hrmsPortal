package com.tm.invoice.enums;

public enum DeliveryEnum {


    Email("Email"), AUTO_Delivery("Auto Delivery"), USPS("USPS"), CSV("csv"), EMAIL_PLUS_CSV(
            "email+csv"), EMAIL_AND_ONLINE("email&online");


    private String value;

    /**
     * @param value
     */
    private DeliveryEnum(String value) {
        this.value = value;
    }

    /**
     * @return value
     */
    public String value() {
        return this.value;
    }

}
