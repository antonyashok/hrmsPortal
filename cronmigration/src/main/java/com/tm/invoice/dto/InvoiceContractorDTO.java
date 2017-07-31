package com.tm.invoice.dto;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

public class InvoiceContractorDTO extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 7798348751734603495L;

	private int serialNumber;
	private String contractorName;
	private Double workHours;
	private Double leaveHours;
	private Double ptoHours;
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
	public Double getLeaveHours() {
		return leaveHours;
	}
	public void setLeaveHours(Double leaveHours) {
		this.leaveHours = leaveHours;
	}
	public Double getPtoHours() {
		return ptoHours;
	}
	public void setPtoHours(Double ptoHours) {
		this.ptoHours = ptoHours;
	}
	
	public int getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	

	

}
