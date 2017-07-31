package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@Relation(value = "dpQueue", collectionRelation = "dpQueues")
public class DPQueueMinDTO implements Serializable {

	private static final long serialVersionUID = -7584129057619591970L;

	private UUID dpQueueId;
	private String employeeName;

	public UUID getDpQueueId() {
		return dpQueueId;
	}

	public void setDpQueueId(UUID dpQueueId) {
		this.dpQueueId = dpQueueId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

}
