package com.tm.invoice.dto;

import java.io.Serializable;

public class InvoiceTemplateHelperDTO implements Serializable {

    private static final long serialVersionUID = -739593698714930207L;

    private String serialNo;
    private String description;
    private String hours;
    private String rate;
    private String amount;

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
