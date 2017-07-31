package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "invoice_alerts_config")
@JsonIgnoreProperties({"auditDetails"})
public class InvoiceAlertsConfig implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7896696266385025641L;
		
	private enum ActiveFlag {
		Y, N
	}
	
	@Id
	@Type(type = "uuid-char")
	@Column(name = "inv_alerts_config_id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID invoiceAlertsConfigId;   
    
	@Type(type = "uuid-char")
	@Column(name = "alert_id")
	private UUID alertId;
	
	@Column(name = "alert_type")
	private String alertType;
	
	@Column(name = "alert_desc")
	private String alertDescription;
	
	@Column(name = "alert_indicator")
	private String alertIndicator;
	
	@Type(type = "uuid-char")
	@Column(name = "pre_req_alert_id")
	private UUID preRequesiteAlertId;
	
	@Column(name = "alert_col_name")
	private String alertColumnName;
	
	@Column(name = "alert_col_value")
	private String alertColumnValue;
	
	@Column(name = "alert_cond")
	private String alertCondition;
	
	@Column(name = "alert_cond_oper")
	private Integer alertConditionOperator;
		
	@Enumerated(EnumType.STRING)
	@Column(name = "active_flg")
	private ActiveFlag activeFlag;
	
	@Column(name = "created_dt")
	private Date createdDate;

	public UUID getInvoiceAlertsConfigId() {
		return invoiceAlertsConfigId;
	}

	public void setInvoiceAlertsConfigId(UUID invoiceAlertsConfigId) {
		this.invoiceAlertsConfigId = invoiceAlertsConfigId;
	}

	public UUID getAlertId() {
		return alertId;
	}

	public void setAlertId(UUID alertId) {
		this.alertId = alertId;
	}

	public String getAlertType() {
		return alertType;
	}

	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}

	public String getAlertDescription() {
		return alertDescription;
	}

	public void setAlertDescription(String alertDescription) {
		this.alertDescription = alertDescription;
	}

	public String getAlertIndicator() {
		return alertIndicator;
	}

	public void setAlertIndicator(String alertIndicator) {
		this.alertIndicator = alertIndicator;
	}

	public UUID getPreRequesiteAlertId() {
		return preRequesiteAlertId;
	}

	public void setPreRequesiteAlertId(UUID preRequesiteAlertId) {
		this.preRequesiteAlertId = preRequesiteAlertId;
	}

	public String getAlertColumnName() {
		return alertColumnName;
	}

	public void setAlertColumnName(String alertColumnName) {
		this.alertColumnName = alertColumnName;
	}

	public String getAlertColumnValue() {
		return alertColumnValue;
	}

	public void setAlertColumnValue(String alertColumnValue) {
		this.alertColumnValue = alertColumnValue;
	}

	public String getAlertCondition() {
		return alertCondition;
	}

	public void setAlertCondition(String alertCondition) {
		this.alertCondition = alertCondition;
	}

	public Integer getAlertConditionOperator() {
		return alertConditionOperator;
	}

	public void setAlertConditionOperator(Integer alertConditionOperator) {
		this.alertConditionOperator = alertConditionOperator;
	}

	public ActiveFlag getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(ActiveFlag activeFlag) {
		this.activeFlag = activeFlag;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
}