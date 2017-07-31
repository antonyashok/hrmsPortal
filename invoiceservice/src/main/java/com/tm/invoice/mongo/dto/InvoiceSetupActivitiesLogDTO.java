package com.tm.invoice.mongo.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "invoiceSetupActivityLog")
@Relation(value = "InvoiceSetupActivitiesLog", collectionRelation = "InvoiceSetupActivitiesLog")
public class InvoiceSetupActivitiesLogDTO extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = 1203854186411668525L;

    private UUID sourceReferenceId;
    private Long employeeId;
    private String employeeName;
    private Long employeeRoleId;
    private String employeeRoleName;
    private String comment;
    private String sourceReferenceType;
    private String dateTime;
    private String updatedOn;

    public UUID getSourceReferenceId() {
        return sourceReferenceId;
    }

    public void setSourceReferenceId(UUID sourceReferenceId) {
        this.sourceReferenceId = sourceReferenceId;
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

    public Long getEmployeeRoleId() {
        return employeeRoleId;
    }

    public void setEmployeeRoleId(Long employeeRoleId) {
        this.employeeRoleId = employeeRoleId;
    }

    public String getEmployeeRoleName() {
        return employeeRoleName;
    }

    public void setEmployeeRoleName(String employeeRoleName) {
        this.employeeRoleName = employeeRoleName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSourceReferenceType() {
        return sourceReferenceType;
    }

    public void setSourceReferenceType(String sourceReferenceType) {
        this.sourceReferenceType = sourceReferenceType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

}
