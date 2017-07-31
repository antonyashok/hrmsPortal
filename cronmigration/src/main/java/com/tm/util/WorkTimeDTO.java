/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.web.rest.util.WorkTimeDTO.java
 * Author        : Annamalai L
 * Date Created  : Mar 13, 2017
 *
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.util;

import java.math.BigDecimal;

public class WorkTimeDTO {

	private BigDecimal stHour = BigDecimal.ZERO;
	private BigDecimal otHour = BigDecimal.ZERO;
	private BigDecimal dtHour = BigDecimal.ZERO;
	private BigDecimal totalHour = BigDecimal.ZERO;
	private BigDecimal calTotalHour = BigDecimal.ZERO;
	private BigDecimal calMaxHour = BigDecimal.ZERO;
	private Boolean capLimitExceed = false;
	
	public BigDecimal getStHour() {
		return stHour;
	}
	public void setStHour(BigDecimal stHour) {
		this.stHour = stHour;
	}
	public BigDecimal getOtHour() {
		return otHour;
	}
	public void setOtHour(BigDecimal otHour) {
		this.otHour = otHour;
	}
	public BigDecimal getDtHour() {
		return dtHour;
	}
	public void setDtHour(BigDecimal dtHour) {
		this.dtHour = dtHour;
	}
	public BigDecimal getTotalHour() {
		return totalHour;
	}
	public void setTotalHour(BigDecimal totalHour) {
		this.totalHour = totalHour;
	}
	public BigDecimal getCalTotalHour() {
		return calTotalHour;
	}
	public void setCalTotalHour(BigDecimal calTotalHour) {
		this.calTotalHour = calTotalHour;
	}
	public BigDecimal getCalMaxHour() {
		return calMaxHour;
	}
	public void setCalMaxHour(BigDecimal calMaxHour) {
		this.calMaxHour = calMaxHour;
	}
	public Boolean getCapLimitExceed() {
		return capLimitExceed;
	}
	public void setCapLimitExceed(Boolean capLimitExceed) {
		this.capLimitExceed = capLimitExceed;
	}
	
}