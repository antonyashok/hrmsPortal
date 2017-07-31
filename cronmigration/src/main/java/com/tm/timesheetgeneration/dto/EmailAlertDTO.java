/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.cronmigration.cron.dto.EmailAlertDTO.java
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

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailAlertDTO {

	private Date startDate;
    private Date endDate;
    private Long salesMngrId;
    private String salesMngrEmail;
    private String salesMngrName;
    private Long salesMngrOfcId;
    private String salesMngrOfcName;
    private String hrMngrEmail;
    private String payrollMngrEmail;
    
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getSalesMngrId() {
		return salesMngrId;
	}
	public void setSalesMngrId(Long salesMngrId) {
		this.salesMngrId = salesMngrId;
	}
	public String getSalesMngrEmail() {
		return salesMngrEmail;
	}
	public void setSalesMngrEmail(String salesMngrEmail) {
		this.salesMngrEmail = salesMngrEmail;
	}
	public String getSalesMngrName() {
		return salesMngrName;
	}
	public void setSalesMngrName(String salesMngrName) {
		this.salesMngrName = salesMngrName;
	}
	public Long getSalesMngrOfcId() {
		return salesMngrOfcId;
	}
	public void setSalesMngrOfcId(Long salesMngrOfcId) {
		this.salesMngrOfcId = salesMngrOfcId;
	}
	public String getSalesMngrOfcName() {
		return salesMngrOfcName;
	}
	public void setSalesMngrOfcName(String salesMngrOfcName) {
		this.salesMngrOfcName = salesMngrOfcName;
	}
	public String getHrMngrEmail() {
		return hrMngrEmail;
	}
	public void setHrMngrEmail(String hrMngrEmail) {
		this.hrMngrEmail = hrMngrEmail;
	}
	public String getPayrollMngrEmail() {
		return payrollMngrEmail;
	}
	public void setPayrollMngrEmail(String payrollMngrEmail) {
		this.payrollMngrEmail = payrollMngrEmail;
	}
}
