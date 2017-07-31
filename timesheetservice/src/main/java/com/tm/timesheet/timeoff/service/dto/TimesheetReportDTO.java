package com.tm.timesheet.timeoff.service.dto;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Transient;

import org.springframework.hateoas.core.Relation;

@Relation(value = "reports", collectionRelation = "reports")
public class TimesheetReportDTO implements Serializable {

    private static final long serialVersionUID = -5826546795866296748L;

    private String reportFor;
    private String activeStatus;
    private UUID engmtId;
    private Long emplId;
    private String reportName;
    private String reportStatus;
    private String month;
    private String year;

    private String name;
    private String designation;
    private String st;
    private String ot;
    private String dt;
    private String total;
    private String pto;
    private String leave;
    private int serialNumber;
    private String monthYear;

    @Transient
    private String SUBREPORT_DIR;

    public String getReportFor() {
        return reportFor;
    }

    public void setReportFor(String reportFor) {
        this.reportFor = reportFor;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public UUID getEngmtId() {
        return engmtId;
    }

    public void setEngmtId(UUID engmtId) {
        this.engmtId = engmtId;
    }

    public Long getEmplId() {
        return emplId;
    }

    public void setEmplId(Long emplId) {
        this.emplId = emplId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public String getOt() {
        return ot;
    }

    public void setOt(String ot) {
        this.ot = ot;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPto() {
        return pto;
    }

    public void setPto(String pto) {
        this.pto = pto;
    }

    public String getLeave() {
        return leave;
    }

    public void setLeave(String leave) {
        this.leave = leave;
    }

    public String getSUBREPORT_DIR() {
        return SUBREPORT_DIR;
    }

    public void setSUBREPORT_DIR(String sUBREPORT_DIR) {
        SUBREPORT_DIR = sUBREPORT_DIR;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }



}
