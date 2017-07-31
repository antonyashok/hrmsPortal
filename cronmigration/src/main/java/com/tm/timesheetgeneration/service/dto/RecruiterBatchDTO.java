/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.service.dto.EmployeeTimesheetBatchDTO.java
 * Author        : Annamalai L
 * Date Created  : Apr 5th, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheetgeneration.service.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.hateoas.core.Relation;

import com.tm.common.domain.RecruiterProfileView;
import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.domain.TimesheetDetails;
import com.tm.timesheetgeneration.dto.RecruiterTimeDTO;
import com.tm.util.Week;

@Relation(value = "recruiterTimesheet", collectionRelation = "recruiterTimesheets")
public class RecruiterBatchDTO implements Serializable {

	private static final long serialVersionUID = -8976796477124760358L;

	private Date weekStartDate;

	private Date weekEndDate;

	private List<Timesheet> timesheetList;

	private List<RecruiterProfileView> recruiterProfileViewList;

	private List<TimesheetDetails> timesheetDetailList;

	private Map<Integer, Week> allWeekMapList;

	private List<Week> allWeekList;

	private Week week;
	
	private Week todayWeek;
	
	private RecruiterProfileView recruiterProfile;
	
	private Long employeeId;
	
	List<RecruiterTimeDTO> recruiterTimeDTOs;

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

	public List<RecruiterProfileView> getRecruiterProfileViewList() {
		return recruiterProfileViewList;
	}

	public void setRecruiterProfileViewList(List<RecruiterProfileView> recruiterProfileViewList) {
		this.recruiterProfileViewList = recruiterProfileViewList;
	}

	public List<TimesheetDetails> getTimesheetDetailList() {
		return timesheetDetailList;
	}

	public void setTimesheetDetailList(List<TimesheetDetails> timesheetDetailList) {
		this.timesheetDetailList = timesheetDetailList;
	}

	public Map<Integer, Week> getAllWeekMapList() {
		return allWeekMapList;
	}

	public void setAllWeekMapList(Map<Integer, Week> allWeekMapList) {
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

	public Week getTodayWeek() {
		return todayWeek;
	}

	public void setTodayWeek(Week todayWeek) {
		this.todayWeek = todayWeek;
	}
	
	public RecruiterProfileView getRecruiterProfile() {
		return recruiterProfile;
	}

	public void setRecruiterProfile(RecruiterProfileView recruiterProfile) {
		this.recruiterProfile = recruiterProfile;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public List<RecruiterTimeDTO> getRecruiterTimeDTOs() {
		return recruiterTimeDTOs;
	}

	public void setRecruiterTimeDTOs(List<RecruiterTimeDTO> recruiterTimeDTOs) {
		this.recruiterTimeDTOs = recruiterTimeDTOs;
	}
	
}