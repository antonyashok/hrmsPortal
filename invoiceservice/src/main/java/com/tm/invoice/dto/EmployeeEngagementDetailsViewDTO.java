package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Relation(value = "EmployeeEngagementDetailsView", collectionRelation = "EmployeeEngagementDetailsViews")
public class EmployeeEngagementDetailsViewDTO extends ResourceSupport implements Serializable {
   
    private static final long serialVersionUID = -4857154521052651507L;

    private UUID employeeEngagementId; 
    
    @Type(type = "uuid-char")
    private UUID engagementId;
   
    private Long employeeId;
    
    private String employeeName;
    
    private Integer fileNumber;
    
    @Transient
    private List<Long> contractorIds;

    public UUID getEmployeeEngagementId() {
        return employeeEngagementId;
    }

    public void setEmployeeEngagementId(UUID employeeEngagementId) {
        this.employeeEngagementId = employeeEngagementId;
    }

    public UUID getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(UUID engagementId) {
        this.engagementId = engagementId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(Integer fileNumber) {
        this.fileNumber = fileNumber;
    }

    public List<Long> getContractorIds() {
        return contractorIds;
    }

    public void setContractorIds(List<Long> contractorIds) {
        this.contractorIds = contractorIds;
    }
     
}
