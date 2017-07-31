package com.tm.invoice.domain;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tm.commonapi.web.core.data.IEntity;
import com.tm.invoice.enums.ActiveFlag;
import com.tm.invoice.enums.AmountAlert;
import com.tm.invoice.enums.ExpenseAlert;
import com.tm.invoice.enums.InvSetupSourceEnum;
import com.tm.invoice.enums.RollOverType;
import com.tm.invoice.enums.Status;

@Entity
@Table(name = "purchase_order")
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties({ "auditDetails" })
@Inheritance(strategy = InheritanceType.JOINED)
public class PurchaseOrder implements IEntity<UUID>{

    private static final long serialVersionUID = 1831686249169183723L;

    @Id
	@Column(name = "po_id")
	@Type(type = "uuid-char")
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private UUID purchaseOrderId;

	@Column(name = "po_number")
	private String poNumber;

	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "actv_flg")
	private ActiveFlag activeFlag;

	@Column(name = "description")
	private String description;

	@Column(name = "amt_alert")
    @Enumerated(EnumType.STRING)
    private AmountAlert revenueAlert;

	@Column(name = "exp_alert")
	@Enumerated(EnumType.STRING)
	private ExpenseAlert expenseAlert;

	@Column(name = "rev_amt")
	private Double revenueAmount;

	@Column(name = "exp_amt")
	private Double expenseAmount;

	@Column(name = "notes")
	private String notes;

	@Column(name = "parent_po_id")
	@Type(type = "uuid-char")
	private UUID parentPoId;

	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "bal_rev_amt")
	private Double balanceRevenueAmount;

	@Column(name = "bal_exp_amt")
	private Double balanceExpenseAmount;

	@Column(name = "unbilled_rev_amt")
	private Double unbilledRevenueAmount;

	@Column(name = "unbilled_exp_amt")
	private Double unbilledExpenseAmount;

	@Type(type = "uuid-char")
	@Column(name = "unbilled_po_ref")
	private UUID unbilledPoReference;

	@Enumerated(EnumType.STRING)
	@Column(name = "rollover_type")
	private RollOverType rollOverType;
	
	@Column(name = "inv_setup_id")
	@Type(type = "uuid-char")
	private UUID invoiceSetupId;
	
	@Column(name = "engmt_id")
	@Type(type = "uuid-char")
	private UUID engagementId;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "invoice_setup_source")
	private InvSetupSourceEnum invSetupSource;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<PurchaseOrderReplenishment> purchaseOrderReplenishment;
	
	
	

    @Override
    public UUID getId() {
        return null;
    }

    @Override
    public void setId(UUID id) {}

    //@Column(name = "invoice_generated_date")
    @Transient
	private Date invoiceGeneratedDate;
    
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
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

    public ActiveFlag getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(ActiveFlag activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public AmountAlert getRevenueAlert() {
		return revenueAlert;
	}

	public void setRevenueAlert(AmountAlert revenueAlert) {
		this.revenueAlert = revenueAlert;
	}

	public ExpenseAlert getExpenseAlert() {
		return expenseAlert;
	}

    public void setExpenseAlert(ExpenseAlert expenseAlert) {
        this.expenseAlert = expenseAlert;
    }

    public Double getRevenueAmount() {
        return revenueAmount;
    }

    public void setRevenueAmount(Double revenueAmount) {
        this.revenueAmount = revenueAmount;
    }

    public Double getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(Double expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public UUID getParentPoId() {
        return parentPoId;
    }

    public void setParentPoId(UUID parentPoId) {
        this.parentPoId = parentPoId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getBalanceRevenueAmount() {
        return balanceRevenueAmount;
    }

    public void setBalanceRevenueAmount(Double balanceRevenueAmount) {
        this.balanceRevenueAmount = balanceRevenueAmount;
    }

    public Double getBalanceExpenseAmount() {
        return balanceExpenseAmount;
    }

    public void setBalanceExpenseAmount(Double balanceExpenseAmount) {
        this.balanceExpenseAmount = balanceExpenseAmount;
    }

    public Double getUnbilledRevenueAmount() {
        return unbilledRevenueAmount;
    }

    public void setUnbilledRevenueAmount(Double unbilledRevenueAmount) {
        this.unbilledRevenueAmount = unbilledRevenueAmount;
    }

    public Double getUnbilledExpenseAmount() {
        return unbilledExpenseAmount;
    }

    public void setUnbilledExpenseAmount(Double unbilledExpenseAmount) {
        this.unbilledExpenseAmount = unbilledExpenseAmount;
    }

    public UUID getUnbilledPoReference() {
        return unbilledPoReference;
    }

    public void setUnbilledPoReference(UUID unbilledPoReference) {
        this.unbilledPoReference = unbilledPoReference;
    }

    public RollOverType getRollOverType() {
        return rollOverType;
    }

    public void setRollOverType(RollOverType rollOverType) {
        this.rollOverType = rollOverType;
    }
	
	public List<PurchaseOrderReplenishment> getPurchaseOrderReplenishment() {
		return purchaseOrderReplenishment;
	}

	public void setPurchaseOrderReplenishment(List<PurchaseOrderReplenishment> purchaseOrderReplenishment) {
		this.purchaseOrderReplenishment = purchaseOrderReplenishment;
	}

	public UUID getInvoiceSetupId() {
		return invoiceSetupId;
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

	public UUID getEngagementId() {
		return engagementId;
	}

	public void setEngagementId(UUID engagementId) {
		this.engagementId = engagementId;
	}
	
	public Date getInvoiceGeneratedDate() {
		return invoiceGeneratedDate;
	}

	public void setInvoiceGeneratedDate(Date invoiceGeneratedDate) {
		this.invoiceGeneratedDate = invoiceGeneratedDate;
	}
}
