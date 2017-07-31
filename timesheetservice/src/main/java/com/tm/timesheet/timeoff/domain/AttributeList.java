package com.tm.timesheet.timeoff.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "attribute_lst")
public class AttributeList implements Serializable {

	private static final long serialVersionUID = -8481625513487346933L;

	@Id
	@Column(name = "attrib_id", nullable = false)
	private String attributeId;

	@Column(name = "attrib_name", nullable = false)
	private String attributeName;

	@Column(name = "data_type", nullable = false)
	private String dataType;

	@Column(name = "dependent_attrib_id", nullable = false)
	private String dependentAttributeId;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "attributeList", cascade = CascadeType.ALL)
	private List<EntityAttribute> entityAttribute;

	public List<EntityAttribute> getEntityAttributeMap() {
		return entityAttribute;
	}

	public void setEntityAttributeMap(List<EntityAttribute> entityAttribute) {
		this.entityAttribute = entityAttribute;
	}

	@Override
	public String toString() {
		return "AttributeList [attributeId=" + attributeId + ", attributeName=" + attributeName + ", dataType="
				+ dataType + ", dependentAttributeId=" + dependentAttributeId + "]";
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

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDependentAttributeId() {
		return dependentAttributeId;
	}

	public void setDependentAttributeId(String dependentAttributeId) {
		this.dependentAttributeId = dependentAttributeId;
	}

}
