package com.tm.invoice.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.tm.invoice.enums.ActiveFlag;
import com.tm.invoice.enums.InvSetupSourceEnum;
import com.tm.invoice.enums.Status;

@Entity
@Table(name = "purchase_order_details_view")
public class PurchaseOrderDetailsView  implements Serializable {	

    /**
	 * 
	 */
	private static final long serialVersionUID = -3986402817273507955L;

	public enum CustomerActivityFlag {
        Y, N
    }
    
    @Id
    @Column(name = "po_customer_id")
    @Type(type = "uuid-char")
    private UUID purchaseOrderCustomerId;

    @Column(name = "po_id")
    @Type(type = "uuid-char")
    private UUID purchaseOrderId;

    @Column(name = "po_number")
    private String poNumber;

    @Column(name = "parent_po_id")
    @Type(type = "uuid-char")
    private UUID poParentId;
    
    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "po_balance")
    private BigDecimal poBalance;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_actv_flg")
    @Enumerated(EnumType.STRING)
    private CustomerActivityFlag customerActiveFlag;

    @Column(name = "po_actv_flg")
    @Enumerated(EnumType.STRING)
    private ActiveFlag poActiveFlag;

    @Column(name = "last_updated_on")
    private Date lastUpdatedOn;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @Column(name = "inv_setup_id")
	@Type(type = "uuid-char")
	private UUID invoiceSetupId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "invoice_setup_source")
	private InvSetupSourceEnum invSetupSource;
	
	@Column(name = "engmt_nm")
    private String engagementName;
	
	@Column(name = "engmt_id")
	@Type(type = "uuid-char")
	private UUID engagementId;

    public UUID getPurchaseOrderCustomerId() {
        return purchaseOrderCustomerId;
    }

    public void setPurchaseOrderCustomerId(UUID purchaseOrderCustomerId) {
        this.purchaseOrderCustomerId = purchaseOrderCustomerId;
    }

    public UUID getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(UUID purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public UUID getPoParentId() {
        return poParentId;
    }

    public void setPoParentId(UUID poParentId) {
        this.poParentId = poParentId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPoBalance() {
        return poBalance;
    }

    public void setPoBalance(BigDecimal poBalance) {
        this.poBalance = poBalance;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public CustomerActivityFlag getCustomerActiveFlag() {
        return customerActiveFlag;
    }

    public void setCustomerActiveFlag(CustomerActivityFlag customerActiveFlag) {
        this.customerActiveFlag = customerActiveFlag;
    }

    public ActiveFlag getPoActiveFlag() {
        return poActiveFlag;
    }

    public void setPoActiveFlag(ActiveFlag poActiveFlag) {
        this.poActiveFlag = poActiveFlag;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UUID getInvoiceSetupId() {
		return invoiceSetupId;
	}

	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}

	public void setInvoiceSetupId(UUID invoiceSetupId) {
		this.invoiceSetupId = invoiceSetupId;
	}

	public InvSetupSourceEnum getInvSetupSource() {
		return invSetupSource;
	}

	public void setInvSetupSource(InvSetupSourceEnum invSetupSource) {
		this.invSetupSource = invSetupSource;
	}

	public String getEngagementName() {
		return engagementName;
	}

	public void setEngagementName(String engagementName) {
		this.engagementName = engagementName;
	}
}
