/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.domain.LookupView.java
 * Author        : Annamalai L
 * Date Created  : Apr 7th, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.common.domain;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "lkp_view")
public class LookupView implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 786290169097134092L;

    @Id
    @Column(name = "entity_attrib_map_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID entityAttributeMapId;

    @Column(name = "entity_id")
    @Type(type = "uuid-char")
    private UUID entityId;

    @Column(name = "entity_name")
    private String entityName;

    @Column(name = "attrib_id")
    @Type(type = "uuid-char")
    private UUID attributeId;

    @Column(name = "attrib_name")
    private String attributeName;

    @Column(name = "attrib_val")
    private String attributeValue;

    @Column(name = "sequnce_number")
    private Integer sequnceNumber;

    @Column(name = "entity_attrib_map_val")
    private String entityAttributeMapValue;
    
    @Transient
    private List<UUID> entityAttributeMapIds;

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

    public Integer getSequnceNumber() {
        return sequnceNumber;
    }

    public void setSequnceNumber(Integer sequnceNumber) {
        this.sequnceNumber = sequnceNumber;
    }

    public String getEntityAttributeMapValue() {
        return entityAttributeMapValue;
    }

    public void setEntityAttributeMapValue(String entityAttributeMapValue) {
        this.entityAttributeMapValue = entityAttributeMapValue;
    }

    public List<UUID> getEntityAttributeMapIds() {
        return entityAttributeMapIds;
    }

    public void setEntityAttributeMapIds(List<UUID> entityAttributeMapIds) {
        this.entityAttributeMapIds = entityAttributeMapIds;
    }

}
