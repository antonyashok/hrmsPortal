package com.tm.common.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * This class used for mentioning application all lookup entity columns
 *
 */
@Entity
@Table(name = "entity_lst")
public class EntityList implements Serializable {

	private static final long serialVersionUID = -8481625513487346933L;

	@Id
	@Column(name = "entity_id", nullable = false)
	private String entityId;

	@Column(name = "entity_name", nullable = false)
	private String entityName;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "entityList", cascade = CascadeType.ALL)
	private List<EntityAttribute> entityAttribute;

	public List<EntityAttribute> getEntityAttributeMap() {
		return entityAttribute;
	}

	public void setEntityAttributeMap(List<EntityAttribute> entityAttribute) {
		this.entityAttribute = entityAttribute;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

}
