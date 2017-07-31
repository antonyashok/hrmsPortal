package com.tm.invoice.dto;

import java.io.Serializable;

public class DeliveryDTO implements Serializable {

	private static final long serialVersionUID = -7574110871513813807L;

	private String deliveryType;

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

}
