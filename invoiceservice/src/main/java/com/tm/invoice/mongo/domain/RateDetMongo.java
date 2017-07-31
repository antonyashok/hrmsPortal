package com.tm.invoice.mongo.domain;

import java.math.BigDecimal;

import org.springframework.format.annotation.DateTimeFormat;

import com.tm.commonapi.web.core.data.BaseDTO;

public class RateDetMongo extends BaseDTO {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String effectiveDate;

    private BigDecimal billClientrate;
    private BigDecimal endClientrate;
    private BigDecimal bst;
    private BigDecimal bot;
    private BigDecimal bdt;
    private BigDecimal est;
    private BigDecimal eot;
    private BigDecimal edt;

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public BigDecimal getBillClientrate() {
        return billClientrate;
    }

    public BigDecimal getEndClientrate() {
        return endClientrate;
    }

    public BigDecimal getBst() {
        return bst;
    }

    public BigDecimal getBot() {
        return bot;
    }

    public BigDecimal getBdt() {
        return bdt;
    }

    public BigDecimal getEst() {
        return est;
    }

    public BigDecimal getEot() {
        return eot;
    }

    public BigDecimal getEdt() {
        return edt;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setBillClientrate(BigDecimal billClientrate) {
        this.billClientrate = billClientrate;
    }

    public void setEndClientrate(BigDecimal endClientrate) {
        this.endClientrate = endClientrate;
    }

    public void setBst(BigDecimal bst) {
        this.bst = bst;
    }

    public void setBot(BigDecimal bot) {
        this.bot = bot;
    }

    public void setBdt(BigDecimal bdt) {
        this.bdt = bdt;
    }

    public void setEst(BigDecimal est) {
        this.est = est;
    }

    public void setEot(BigDecimal eot) {
        this.eot = eot;
    }

    public void setEdt(BigDecimal edt) {
        this.edt = edt;
    }

}
