package com.tm.invoice.dto;

import java.util.UUID;

import com.tm.commonapi.web.core.data.BaseDTO;

public class PurchaseOrderContractorDTO extends BaseDTO {

    private UUID poContractorId;
    private UUID poId;
    private Long employeeId;
    
	public UUID getPoContractorId() {
		return poContractorId;
	}
	public void setPoContractorId(UUID poContractorId) {
		this.poContractorId = poContractorId;
	}
	public UUID getPoId() {
		return poId;
	}
	public void setPoId(UUID poId) {
		this.poId = poId;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
    
}
