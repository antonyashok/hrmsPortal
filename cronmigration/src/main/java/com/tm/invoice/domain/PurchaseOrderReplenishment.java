package com.tm.invoice.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties({ "auditDetails" })
@Table(name = "purchase_order_replenishments")
public class PurchaseOrderReplenishment {

	public enum ReversalFlag {
		Y, N
	}

	@Id
	@Column(name = "po_repl_id")
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID poReplId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "po_id", nullable = false)
	private PurchaseOrder purchaseOrder;

	@Column(name = "rev_amt")
	private Double replenishmentRevenueAmount;

	@Column(name = "exp_amt")
	private Double replenishmentExpenseAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "reversal_flg")
	private ReversalFlag reversalFlg;

	@Column(name = "notes")
	private String notes;

	@Column(name = "reversal_notes")
	private String reversalNotes;

	public UUID getPoReplId() {
		return poReplId;
	}

	public void setPoReplId(UUID poReplId) {
		this.poReplId = poReplId;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public Double getReplenishmentRevenueAmount() {
		return replenishmentRevenueAmount;
	}

	public void setReplenishmentRevenueAmount(Double replenishmentRevenueAmount) {
		this.replenishmentRevenueAmount = replenishmentRevenueAmount;
	}

	public Double getReplenishmentExpenseAmount() {
		return replenishmentExpenseAmount;
	}

	public void setReplenishmentExpenseAmount(Double replenishmentExpenseAmount) {
		this.replenishmentExpenseAmount = replenishmentExpenseAmount;
	}

	public ReversalFlag getReversalFlg() {
		return reversalFlg;
	}

	public void setReversalFlg(ReversalFlag reversalFlg) {
		this.reversalFlg = reversalFlg;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getReversalNotes() {
		return reversalNotes;
	}

	public void setReversalNotes(String reversalNotes) {
		this.reversalNotes = reversalNotes;
	}

}