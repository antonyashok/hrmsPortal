package com.tm.common.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gis_entity_attributes_view")
public class GlobalInvoiceSetupEntityAttribute implements Serializable {

  private static final long serialVersionUID = 1474410204160281812L;

  @Id
  @Column(name = "entity_attrib_map_id")
  private String entityAttributeMapId;

  @Column(name = "attrib_id")
  private String attributeId;

  @Column(name = "attrib_name")
  private String attributeName;

  @Column(name = "attrib_val")
  private String attributeValue;

  public String getEntityAttributeMapId() {
    return entityAttributeMapId;
  }

  public void setEntityAttributeMapId(String entityAttributeMapId) {
    this.entityAttributeMapId = entityAttributeMapId;
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

}
