package com.tm.common.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class used for mapping for all lookup entity and attribute columns.
 *
 */
@Entity
@Table(name = "entity_attrib_map")
public class EntityAttribute implements Serializable {

	public enum ActiveFlagEnum {
		Y, N
	}

	private static final long serialVersionUID = 7880595209450884695L;

	@Id
	@Column(name = "entity_attrib_map_id", nullable = false)
	private String entityAttributeId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "entity_id", nullable = false)
	private EntityList entityList;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "attrib_id", nullable = false)
	private AttributeList attributeList;

	@Column(name = "attrib_val", nullable = false)
	private String attributeValue;

	@Enumerated(EnumType.STRING)
	@Column(name = "actv_flg")
	private ActiveFlagEnum activeFlag;

	@Column(name = "seq_no", nullable = false)
	private Integer sequenceNumber;

	public String getEntityAttributeId() {
		return entityAttributeId;
	}

	public void setEntityAttributeId(String entityAttributeId) {
		this.entityAttributeId = entityAttributeId;
	}

	public EntityList getEntityList() {
		return entityList;
	}

	public void setEntityList(EntityList entityList) {
		this.entityList = entityList;
	}

	public AttributeList getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(AttributeList attributeList) {
		this.attributeList = attributeList;
	}

	public String getAttributeValue() {
		return attributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}

	public ActiveFlagEnum getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlagEnum activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

}