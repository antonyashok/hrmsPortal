package com.tm.timesheet.timeoff.service.dto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

@Relation(value = "entityAttribute", collectionRelation = "entityAttribute")
public class EntityAttributeDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 3878395472425571678L;

	private String entityAttributeId;

	private String entityId;

	private String attributeId;

	private String attributeName;

	private String attributeValue;

	private Integer sequenceNumber;

	public String getEntityAttributeId() {
		return entityAttributeId;
	}

	public void setEntityAttributeId(String entityAttributeId) {
		this.entityAttributeId = entityAttributeId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getAttributeId() {
		return attributeId;
	}

	public void setAttributeId(String attributeId) {
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

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

}
