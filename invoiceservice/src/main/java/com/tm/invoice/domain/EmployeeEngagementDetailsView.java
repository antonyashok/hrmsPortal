package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "employee_engagement_detail_view")
@JsonInclude(value = Include.NON_NULL)
public class EmployeeEngagementDetailsView implements Serializable  {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2824887347758090717L;

	@Column(name="empl_engmt_id")
    @Id
    private UUID employeeEngagementId; 
    
    @Column(name="engmt_id")
    @Type(type = "uuid-char")
    private UUID engagementId;
    
    @Column(name="empl_id")
    private Long employeeId;
    
    @Column(name="employee_name")
    private String employeeName;
    
    @Column(name="file_no")
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
