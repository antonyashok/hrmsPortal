package com.tm.invoice.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class BillingProfileDTO implements Serializable {

  private static final long serialVersionUID = -2297962537750870439L;

	private String contractorName;
	private Double workHours;
	private Double rate;
	private BigDecimal amount;
	 
	public String getContractorName() {
		return contractorName;
	}
	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}
	public Double getWorkHours() {
		return workHours;
	}
	public void setWorkHours(Double workHours) {
		this.workHours = workHours;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
 

}
