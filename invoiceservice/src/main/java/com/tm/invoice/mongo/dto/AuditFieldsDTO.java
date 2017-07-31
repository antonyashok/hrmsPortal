package com.tm.invoice.mongo.dto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "AuditFields", collectionRelation = "AuditFields")
public class AuditFieldsDTO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = -235732184076320691L;

    private Long by;
    @JsonFormat(pattern = "MM/dd/yyyy")
    private String on;

    public Long getBy() {
        return by;
    }

    public void setBy(Long by) {
        this.by = by;
    }

    public String getOn() {
        return on;
    }

    public void setOn(String on) {
        this.on = on;
    }
}
