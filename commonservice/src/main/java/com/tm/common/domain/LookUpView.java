package com.tm.common.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "lkp_view")
public class LookUpView implements Serializable {

    private static final long serialVersionUID = 2935088317529840309L;

    @Id
    @Column(name = "entity_attrib_map_id")
    @Type(type = "uuid-char")
    private UUID entityAttributeMapId;

    @Column(name = "attrib_id")
    @Type(type = "uuid-char")
    private UUID attributeId;

    @Column(name = "attrib_name")
    private String attributeName;

    @Column(name = "attrib_val")
    private String attributeValue;

    @Column(name = "entity_id")
    @Type(type = "uuid-char")
    private UUID entityId;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "sequnce_number")
    private int sequnceNumber;
    
    @Column(name = "entity_attrib_map_val")
    private String entityAttributeMapValue;

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

    public String getEntityAttributeMapValue() {
        return entityAttributeMapValue;
    }

    public void setEntityAttributeMapValue(String entityAttributeMapValue) {
        this.entityAttributeMapValue = entityAttributeMapValue;
    }

}
