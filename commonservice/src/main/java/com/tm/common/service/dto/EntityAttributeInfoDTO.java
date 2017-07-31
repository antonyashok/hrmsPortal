package com.tm.common.service.dto;

import java.io.Serializable;

public class EntityAttributeInfoDTO implements Serializable {

  private static final long serialVersionUID = 1255902164406280618L;

  private String id;
  private String attribute;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getAttribute() {
    return attribute;
  }

  public void setAttribute(String attribute) {
    this.attribute = attribute;
  }


}
