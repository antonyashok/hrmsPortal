package com.tm.common.service.dto;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class LookUpViewDTO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = -3885940260462437356L;

    @Type(type = "uuid-char")
    private UUID attributeId;

    private String attributeName;

    private String attributeValue;

    @Type(type = "uuid-char")
    private UUID entityAttributeMapId;

    @Type(type = "uuid-char")
    private UUID entityId;

    private String entityName;

    private int sequnceNumber;

    public UUID getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(UUID attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public UUID getEntityAttributeMapId() {
        return entityAttributeMapId;
    }

    public void setEntityAttributeMapId(UUID entityAttributeMapId) {
        this.entityAttributeMapId = entityAttributeMapId;
    }

    public UUID getEntityId() {
        return entityId;
    }

    public void setEntityId(UUID entityId) {
        this.entityId = entityId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public int getSequnceNumber() {
        return sequnceNumber;
    }

    public void setSequnceNumber(int sequnceNumber) {
        this.sequnceNumber = sequnceNumber;
    }

}
