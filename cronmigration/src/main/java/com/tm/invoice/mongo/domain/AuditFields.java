package com.tm.invoice.mongo.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditFields implements Serializable {

    private static final long serialVersionUID = 6585961231976205878L;

    private Long by;
    private Date on;  

    public Long getBy() {
        return by;
    }

    public void setBy(Long by) {
        this.by = by;
    }

    public Date getOn() {
        return on;
    }

    public void setOn(Date on) {
        this.on = on;
    }

}
