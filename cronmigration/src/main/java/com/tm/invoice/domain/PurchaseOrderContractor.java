package com.tm.invoice.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.tm.commonapi.core.audit.mysql.BaseAuditableEntity;
import com.tm.commonapi.web.core.data.IEntity;

@Table(name = "purchase_order_contractor")
@Entity
public class PurchaseOrderContractor extends BaseAuditableEntity implements IEntity<UUID> {

	private static final long serialVersionUID = 7119291738578638872L;

	@Id
    @Column(name = "po_cntctr_id")
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID poContractorId;

    @Column(name = "po_id")
    @Type(type = "uuid-char")
    private UUID poId;
    
    @Column(name = "empl_id")
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

	@Override
	public UUID getId() {
		return null;
	}

	@Override
	public void setId(UUID id) {
		
	}

}
