package com.tm.engagement.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties({"auditDetails"})
public class TaskRateDTO{

    @Id
    private UUID taskRateId;

    private UUID taskId;

    @NotNull(message = "{effective_date.required.error.message}")
    private String effectiveDate;

    private BigDecimal billToClientST;

    private BigDecimal billToClientOT;

    private BigDecimal billToClientDT;

    private BigDecimal billClientrate;

	public UUID getTaskRateId() {
		return taskRateId;
	}

	public void setTaskRateId(UUID taskRateId) {
		this.taskRateId = taskRateId;
	}

	public UUID getTaskId() {
		return taskId;
	}

	public void setTaskId(UUID taskId) {
		this.taskId = taskId;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public BigDecimal getBillToClientST() {
		return billToClientST;
	}

	public void setBillToClientST(BigDecimal billToClientST) {
		this.billToClientST = billToClientST;
	}

	public BigDecimal getBillToClientOT() {
		return billToClientOT;
	}

	public void setBillToClientOT(BigDecimal billToClientOT) {
		this.billToClientOT = billToClientOT;
	}

	public BigDecimal getBillToClientDT() {
		return billToClientDT;
	}

	public void setBillToClientDT(BigDecimal billToClientDT) {
		this.billToClientDT = billToClientDT;
	}

	public BigDecimal getBillClientrate() {
		return billClientrate;
	}

	public void setBillClientrate(BigDecimal billClientrate) {
		this.billClientrate = billClientrate;
	}


   

}
