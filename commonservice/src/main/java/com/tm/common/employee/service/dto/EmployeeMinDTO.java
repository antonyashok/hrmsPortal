package com.tm.common.employee.service.dto;

import java.io.Serializable;

public class EmployeeMinDTO implements Serializable {

  private static final long serialVersionUID = 321897675808770348L;

  private Long id;
  private String firstName;
  private String lastName;
  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }  

}
