package com.tm.common.employee.service.dto;

import java.io.Serializable;
import java.util.List;

public class EmployeeMinDTOList implements Serializable {
  
  private static final long serialVersionUID = 955304473998719435L;
  
  private List<EmployeeMinDTO> employees;

  public List<EmployeeMinDTO> getEmployees() {
    return employees;
  }

  public void setEmployees(List<EmployeeMinDTO> employees) {
    this.employees = employees;
  }  
  
}
