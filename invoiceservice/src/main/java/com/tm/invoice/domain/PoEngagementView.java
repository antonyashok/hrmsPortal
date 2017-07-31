package com.tm.invoice.domain;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "po_engagement_view")
public class PoEngagementView implements Serializable {

    private static final long serialVersionUID = 1816906753859036434L;

    @Id
    @Column(name = "engmt_id")
    @Type(type = "uuid-char")
    private UUID engagementId;

    @Column(name = "engmt_nm")
    private String engagementName;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "engmt_strt_date")
	private String engmtStartDate;

	@Column(name = "engmt_end_date")
	private String engmtEndDate;

    public UUID getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(UUID engagementId) {
        this.engagementId = engagementId;
    }

    public String getEngagementName() {
        return engagementName;
    }

    public void setEngagementName(String engagementName) {
        this.engagementName = engagementName;
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

	public String getEngmtStartDate() {
		return engmtStartDate;
	}

	public void setEngmtStartDate(String engmtStartDate) {
		this.engmtStartDate = engmtStartDate;
	}

	public String getEngmtEndDate() {
		return engmtEndDate;
	}

	public void setEngmtEndDate(String engmtEndDate) {
		this.engmtEndDate = engmtEndDate;
	}
    
    

}
