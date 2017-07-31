package com.tm.timesheetgeneration.service.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.hateoas.core.Relation;

import com.tm.common.domain.ContractorEmployeeEngagementView;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.domain.TimesheetDetails;
import com.tm.util.Week;

@Relation(value = "contractorEngagement", collectionRelation = "contractorEngagements")
public class ContractorEngagementBatchDTO implements Serializable {

	private static final long serialVersionUID = 6130943000945729079L;

	private Date weekStartDate;

	private Date weekEndDate;

	private List<Timesheet> timesheetList;

	private List<ContractorEmployeeEngagementView> contractorEngagementList;

	private List<TimesheetDetails> timesheetDetailList;

	private LinkedHashMap<Integer, Week> allWeekMapList;

	private List<Week> allWeekList;

	private Week week;
	
	private Week todayWeek;
	
	private ContractorEmployeeEngagementView contractorEmployeeEngagementView;
	
	private Long employeeId;

	public Date getWeekStartDate() {
		return weekStartDate;
	}

	public void setWeekStartDate(Date weekStartDate) {
		this.weekStartDate = weekStartDate;
	}

	public Date getWeekEndDate() {
		return weekEndDate;
	}

	public void setWeekEndDate(Date weekEndDate) {
		this.weekEndDate = weekEndDate;
	}

	public List<Timesheet> getTimesheetList() {
		return timesheetList;
	}

	public void setTimesheetList(List<Timesheet> timesheetList) {
		this.timesheetList = timesheetList;
	}

	public List<ContractorEmployeeEngagementView> getContractorEngagementList() {
		return contractorEngagementList;
	}

	public void setContractorEngagementList(List<ContractorEmployeeEngagementView> contractorEngagementList) {
		this.contractorEngagementList = contractorEngagementList;
	}

	public List<TimesheetDetails> getTimesheetDetailList() {
		return timesheetDetailList;
	}

	public void setTimesheetDetailList(List<TimesheetDetails> timesheetDetailList) {
		this.timesheetDetailList = timesheetDetailList;
	}

	public LinkedHashMap<Integer, Week> getAllWeekMapList() {
		return allWeekMapList;
	}

	public void setAllWeekMapList(LinkedHashMap<Integer, Week> allWeekMapList) {
		this.allWeekMapList = allWeekMapList;
	}

	public List<Week> getAllWeekList() {
		return allWeekList;
	}

	public void setAllWeekList(List<Week> allWeekList) {
		this.allWeekList = allWeekList;
	}

	public Week getWeek() {
		return week;
	}

	public void setWeek(Week week) {
		this.week = week;
	}

	public ContractorEmployeeEngagementView getContractorEmployeeEngagementView() {
		return contractorEmployeeEngagementView;
	}

	public void setContractorEmployeeEngagementView(ContractorEmployeeEngagementView contractorEmployeeEngagementView) {
		this.contractorEmployeeEngagementView = contractorEmployeeEngagementView;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Week getTodayWeek() {
		return todayWeek;
	}

	public void setTodayWeek(Week todayWeek) {
		this.todayWeek = todayWeek;
	}
	
	

}
