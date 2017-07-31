package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.List;

public class DPQueueListDTO implements Serializable {
	
	private static final long serialVersionUID = -4969859948236296917L;
	
	private List<DPQueueMinDTO> dpQueues;

	public List<DPQueueMinDTO> getDpQueues() {
		return dpQueues;
	}

	public void setDpQueues(List<DPQueueMinDTO> dpQueues) {
		this.dpQueues = dpQueues;
	}
	
}
