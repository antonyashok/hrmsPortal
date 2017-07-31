/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.cron.dto.RecruiterTimeDTO.java
 * Author        : Annamalai L
 * Date Created  : Apr 13th, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheetgeneration.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.timesheet.configuration.enums.TimeCalculationEnum;
import com.tm.timesheetgeneration.domain.TimesheetDetails;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecruiterTimeDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7571226234727520750L;

	public enum EmailFlag {
		Y, N
	}

	public enum DefaultTimeCalcFlag {
		D, W
	}

	public enum TimesheetTypeEnum {
		H, T
	}

	@Type(type = "uuid-char")
	private UUID configurationId;

	private String weekStartDay;

	private String weekEndDay;

	private Double sunHours = 0d;

	private Double monHours = 0d;

	private Double tueHours = 0d;

	private Double wedHours = 0d;

	private Double thuHours = 0d;

	private Double friHours = 0d;

	private Double satHours = 0d;

	private String breakStartTime = null;

	private String breakEndTime = null;

	private String sunStartTime = null;

	private String sunEndTime = null;

	private String monStartTime = null;

	private String monEndTime = null;

	private String tueStartTime = null;

	private String tueEndTime = null;

	private String wedStartTime = null;

	private String wedEndTime = null;

	private String thuStartTime = null;

	private String thuEndTime = null;

	private String friStartTime = null;

	private String friEndTime = null;

	private String satStartTime = null;

	private String satEndTime = null;

	private Long employeeId;

	private String rcrtrCntctInfo;

	private Long officeId;
	
	private Long roleId;

	private String roleName;

	private String officeName;

	private Long accountManagerId;

	private Long salesManagerId;

	@Column(name = "rcrtCntctInfo")
	private String rcrtCntctInfo;

	@Column(name = "accntMngrCntctInfo")
	private String accntMngrCntctInfo;

	@Column(name = "hourly_flg")
	private String hourlyFlg;

	@Column(name = "actv_flg")
	private String activeFlag;

	private UUID ptoTypeId;

	private String employeeName;

	private Double minHours = 0d;

	private Double maxHours = 0d;

	private Double startMinHours = 0d;

	private Double startMaxHours = 0d;

	private Double otMinHours = 0d;

	private Double otMaxHours = 0d;

	private Double dtMinHours = 0d;

	private Double dtMaxHours = 0d;

	@Enumerated(EnumType.STRING)
	private EmailFlag offLdrRmdrEmailFlg = EmailFlag.N;

	@Enumerated(EnumType.STRING)
	private EmailFlag aprvConfEmailFlg = EmailFlag.N;

	@Enumerated(EnumType.STRING)
	private EmailFlag hrMgrRmdrEmailFlg = EmailFlag.N;

	@Enumerated(EnumType.STRING)
	private EmailFlag pyrlMgrRmdrEmailFlg = EmailFlag.N;

	@Enumerated(EnumType.STRING)
	private EmailFlag rctrAutoEmailFlg = EmailFlag.Y;

	private String hrMgrEmail = null;

	private String pyrlMgrEmail = null;

	@Enumerated(EnumType.STRING)
	private TimeCalculationEnum timeCalculation = TimeCalculationEnum.D;

	@Enumerated(EnumType.STRING)
	private TimesheetTypeEnum timesheetTypeEnum = TimesheetTypeEnum.H;
	
	private List<TimesheetDetails> timesheetDetailList;
	
	private Date joiningDate;
	
	private String employeeEmailId;

	public UUID getConfigurationId() {
		return configurationId;
	}

	public void setConfigurationId(UUID configurationId) {
		this.configurationId = configurationId;
	}

	public String getWeekStartDay() {
		return weekStartDay;
	}

	public void setWeekStartDay(String weekStartDay) {
		this.weekStartDay = weekStartDay;
	}

	public String getWeekEndDay() {
		return weekEndDay;
	}

	public void setWeekEndDay(String weekEndDay) {
		this.weekEndDay = weekEndDay;
	}

	public Double getSunHours() {
		return sunHours;
	}

	public void setSunHours(Double sunHours) {
		this.sunHours = sunHours;
	}

	public Double getMonHours() {
		return monHours;
	}

	public void setMonHours(Double monHours) {
		this.monHours = monHours;
	}

	public Double getTueHours() {
		return tueHours;
	}

	public void setTueHours(Double tueHours) {
		this.tueHours = tueHours;
	}

	public Double getWedHours() {
		return wedHours;
	}

	public void setWedHours(Double wedHours) {
		this.wedHours = wedHours;
	}

	public Double getThuHours() {
		return thuHours;
	}

	public void setThuHours(Double thuHours) {
		this.thuHours = thuHours;
	}

	public Double getFriHours() {
		return friHours;
	}

	public void setFriHours(Double friHours) {
		this.friHours = friHours;
	}

	public Double getSatHours() {
		return satHours;
	}

	public void setSatHours(Double satHours) {
		this.satHours = satHours;
	}

	public String getBreakStartTime() {
		return breakStartTime;
	}

	public void setBreakStartTime(String breakStartTime) {
		this.breakStartTime = breakStartTime;
	}

	public String getBreakEndTime() {
		return breakEndTime;
	}

	public void setBreakEndTime(String breakEndTime) {
		this.breakEndTime = breakEndTime;
	}

	public String getSunStartTime() {
		return sunStartTime;
	}

	public void setSunStartTime(String sunStartTime) {
		this.sunStartTime = sunStartTime;
	}

	public String getSunEndTime() {
		return sunEndTime;
	}

	public void setSunEndTime(String sunEndTime) {
		this.sunEndTime = sunEndTime;
	}

	public String getMonStartTime() {
		return monStartTime;
	}

	public void setMonStartTime(String monStartTime) {
		this.monStartTime = monStartTime;
	}

	public String getMonEndTime() {
		return monEndTime;
	}

	public void setMonEndTime(String monEndTime) {
		this.monEndTime = monEndTime;
	}

	public String getTueStartTime() {
		return tueStartTime;
	}

	public void setTueStartTime(String tueStartTime) {
		this.tueStartTime = tueStartTime;
	}

	public String getTueEndTime() {
		return tueEndTime;
	}

	public void setTueEndTime(String tueEndTime) {
		this.tueEndTime = tueEndTime;
	}

	public String getWedStartTime() {
		return wedStartTime;
	}

	public void setWedStartTime(String wedStartTime) {
		this.wedStartTime = wedStartTime;
	}

	public String getWedEndTime() {
		return wedEndTime;
	}

	public void setWedEndTime(String wedEndTime) {
		this.wedEndTime = wedEndTime;
	}

	public String getThuStartTime() {
		return thuStartTime;
	}

	public void setThuStartTime(String thuStartTime) {
		this.thuStartTime = thuStartTime;
	}

	public String getThuEndTime() {
		return thuEndTime;
	}

	public void setThuEndTime(String thuEndTime) {
		this.thuEndTime = thuEndTime;
	}

	public String getFriStartTime() {
		return friStartTime;
	}

	public void setFriStartTime(String friStartTime) {
		this.friStartTime = friStartTime;
	}

	public String getFriEndTime() {
		return friEndTime;
	}

	public void setFriEndTime(String friEndTime) {
		this.friEndTime = friEndTime;
	}

	public String getSatStartTime() {
		return satStartTime;
	}

	public void setSatStartTime(String satStartTime) {
		this.satStartTime = satStartTime;
	}

	public String getSatEndTime() {
		return satEndTime;
	}

	public void setSatEndTime(String satEndTime) {
		this.satEndTime = satEndTime;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getRcrtrCntctInfo() {
		return rcrtrCntctInfo;
	}

	public void setRcrtrCntctInfo(String rcrtrCntctInfo) {
		this.rcrtrCntctInfo = rcrtrCntctInfo;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public Long getAccountManagerId() {
		return accountManagerId;
	}

	public void setAccountManagerId(Long accountManagerId) {
		this.accountManagerId = accountManagerId;
	}

	public Long getSalesManagerId() {
		return salesManagerId;
	}

	public void setSalesManagerId(Long salesManagerId) {
		this.salesManagerId = salesManagerId;
	}

	public String getRcrtCntctInfo() {
		return rcrtCntctInfo;
	}

	public void setRcrtCntctInfo(String rcrtCntctInfo) {
		this.rcrtCntctInfo = rcrtCntctInfo;
	}

	public String getAccntMngrCntctInfo() {
		return accntMngrCntctInfo;
	}

	public void setAccntMngrCntctInfo(String accntMngrCntctInfo) {
		this.accntMngrCntctInfo = accntMngrCntctInfo;
	}

	public String getHourlyFlg() {
		return hourlyFlg;
	}

	public void setHourlyFlg(String hourlyFlg) {
		this.hourlyFlg = hourlyFlg;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public UUID getPtoTypeId() {
		return ptoTypeId;
	}

	public void setPtoTypeId(UUID ptoTypeId) {
		this.ptoTypeId = ptoTypeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Double getMinHours() {
		return minHours;
	}

	public void setMinHours(Double minHours) {
		this.minHours = minHours;
	}

	public Double getMaxHours() {
		return maxHours;
	}

	public void setMaxHours(Double maxHours) {
		this.maxHours = maxHours;
	}

	public Double getStartMinHours() {
		return startMinHours;
	}

	public void setStartMinHours(Double startMinHours) {
		this.startMinHours = startMinHours;
	}

	public Double getStartMaxHours() {
		return startMaxHours;
	}

	public void setStartMaxHours(Double startMaxHours) {
		this.startMaxHours = startMaxHours;
	}

	public Double getOtMinHours() {
		return otMinHours;
	}

	public void setOtMinHours(Double otMinHours) {
		this.otMinHours = otMinHours;
	}

	public Double getOtMaxHours() {
		return otMaxHours;
	}

	public void setOtMaxHours(Double otMaxHours) {
		this.otMaxHours = otMaxHours;
	}

	public Double getDtMinHours() {
		return dtMinHours;
	}

	public void setDtMinHours(Double dtMinHours) {
		this.dtMinHours = dtMinHours;
	}

	public Double getDtMaxHours() {
		return dtMaxHours;
	}

	public void setDtMaxHours(Double dtMaxHours) {
		this.dtMaxHours = dtMaxHours;
	}

	public EmailFlag getOffLdrRmdrEmailFlg() {
		return offLdrRmdrEmailFlg;
	}

	public void setOffLdrRmdrEmailFlg(EmailFlag offLdrRmdrEmailFlg) {
		this.offLdrRmdrEmailFlg = offLdrRmdrEmailFlg;
	}

	public Date getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	public EmailFlag getAprvConfEmailFlg() {
		return aprvConfEmailFlg;
	}

	public void setAprvConfEmailFlg(EmailFlag aprvConfEmailFlg) {
		this.aprvConfEmailFlg = aprvConfEmailFlg;
	}

	public EmailFlag getHrMgrRmdrEmailFlg() {
		return hrMgrRmdrEmailFlg;
	}

	public void setHrMgrRmdrEmailFlg(EmailFlag hrMgrRmdrEmailFlg) {
		this.hrMgrRmdrEmailFlg = hrMgrRmdrEmailFlg;
	}

	public EmailFlag getPyrlMgrRmdrEmailFlg() {
		return pyrlMgrRmdrEmailFlg;
	}

	public void setPyrlMgrRmdrEmailFlg(EmailFlag pyrlMgrRmdrEmailFlg) {
		this.pyrlMgrRmdrEmailFlg = pyrlMgrRmdrEmailFlg;
	}

	public EmailFlag getRctrAutoEmailFlg() {
		return rctrAutoEmailFlg;
	}

	public void setRctrAutoEmailFlg(EmailFlag rctrAutoEmailFlg) {
		this.rctrAutoEmailFlg = rctrAutoEmailFlg;
	}

	public String getHrMgrEmail() {
		return hrMgrEmail;
	}

	public void setHrMgrEmail(String hrMgrEmail) {
		this.hrMgrEmail = hrMgrEmail;
	}

	public String getPyrlMgrEmail() {
		return pyrlMgrEmail;
	}

	public void setPyrlMgrEmail(String pyrlMgrEmail) {
		this.pyrlMgrEmail = pyrlMgrEmail;
	}
	
	public TimeCalculationEnum getTimeCalculation() {
		return timeCalculation;
	}

	public void setTimeCalculation(TimeCalculationEnum timeCalculation) {
		this.timeCalculation = timeCalculation;
	}

	public TimesheetTypeEnum getTimesheetTypeEnum() {
		return timesheetTypeEnum;
	}

	public void setTimesheetTypeEnum(TimesheetTypeEnum timesheetTypeEnum) {
		this.timesheetTypeEnum = timesheetTypeEnum;
	}

	public List<TimesheetDetails> getTimesheetDetailList() {
		return timesheetDetailList;
	}

	public void setTimesheetDetailList(List<TimesheetDetails> timesheetDetailList) {
		this.timesheetDetailList = timesheetDetailList;
	}

	public String getEmployeeEmailId() {
		return employeeEmailId;
	}

	public void setEmployeeEmailId(String employeeEmailId) {
		this.employeeEmailId = employeeEmailId;
	}	
	
}
