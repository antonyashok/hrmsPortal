package com.tm.engagement.service.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties({"auditDetails"})
@Inheritance(strategy = InheritanceType.JOINED)
public class TaskDTO  {

    @Id
    private UUID taskId;
    private Long employeeId;
    private Long contractId;
    private Long engagementId;
    private String taskName;
    private String billType;
    private List<TaskRateDTO> TaskRateDetails = new ArrayList<>();

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Long getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(Long engagementId) {
        this.engagementId = engagementId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public List<TaskRateDTO> getTaskRateDetails() {
        return TaskRateDetails;
    }

    public void setTaskRateDetails(List<TaskRateDTO> taskRateDetails) {
        TaskRateDetails = taskRateDetails;
    }

}
