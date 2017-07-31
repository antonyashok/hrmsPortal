package com.tm.engagement.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "customer_locations")
public class CustomerLocations implements Serializable {

	private static final long serialVersionUID = -6635267083832196180L;

	@Id
	@Column(name = "cust_loc_id")
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID customerLocationId;

	@Column(name = "ofc_id")
	private Long officeId;

	@Column(name = "cust_id")
	private Long customerId;

	public UUID getCustomerLocationId() {
		return customerLocationId;
	}

	public void setCustomerLocationId(UUID customerLocationId) {
		this.customerLocationId = customerLocationId;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	

}