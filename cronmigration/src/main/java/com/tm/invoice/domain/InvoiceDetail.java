package com.tm.invoice.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.GeneratedValue;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "invoiceQueueDetails")
public class InvoiceDetail implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 3107413530512317996L;
	
    @Type(type = "uuid-char")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID _id;   
    @Type(type = "uuid-char")
    private UUID invoiceQueueId;
	@Type(type = "uuid-char")
	private UUID invoiceSetupId;	
	private Long contractorId;
	private String contractorName;
	@Type(type = "uuid-char")
	private UUID poId;	
	private Double salesTaxRate;	
	private Double salesTaxAmount;	
	private Double cityTaxRate;	
	private Double cityTaxAmount;	
	private Double countryTaxRate;	
	private Double countryTaxAmount;	
	private Double stateTaxRate;	
	private Double stateTaxAmount;	
	private Double federalTaxRate;	
	private Double federalTaxAmount;	
	private Double totalTaxAmount;	
	private Double stHours;	
	private Double stAmount;	
	private Double otHours;	
	private Double otAmount;	
	private Double dtHours;	
	private Double dtAmount;	
	private Double workHours;	
	private Double totalAmount;	
	
	private Double unitAmount;
	private Date startDate;	
	private Date endDate;
	private Integer totalUnits;
	 
	private BigDecimal billToClientST;
	 
	private BigDecimal billToClientOT;
	 
	private BigDecimal billToClientDT;
 
	private BigDecimal billClientrate;
	
	
	Double ptoHours = 0d;
	Double leaveHours = 0d;
	
	@Transient
	int serialNumber = 0;
	@Transient
	private String stotdtRate;
	@Transient
	private String stotdtDoubleRate;	
    public UUID get_id() {
        return _id;
    }
    public void set_id(UUID _id) {
        this._id = _id;
    }
    public UUID getInvoiceQueueId() {
        return invoiceQueueId;
    }
    public void setInvoiceQueueId(UUID invoiceQueueId) {
        this.invoiceQueueId = invoiceQueueId;
    }
    public UUID getInvoiceSetupId() {
        return invoiceSetupId;
    }
    public void setInvoiceSetupId(UUID invoiceSetupId) {
        this.invoiceSetupId = invoiceSetupId;
    }
    public Long getContractorId() {
        return contractorId;
    }
    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }
    public String getContractorName() {
        return contractorName;
    }
    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }
    public UUID getPoId() {
        return poId;
    }
    public void setPoId(UUID poId) {
        this.poId = poId;
    }
    public Double getSalesTaxRate() {
        return salesTaxRate;
    }
    public void setSalesTaxRate(Double salesTaxRate) {
        this.salesTaxRate = salesTaxRate;
    }
    public Double getSalesTaxAmount() {
        return salesTaxAmount;
    }
    public void setSalesTaxAmount(Double salesTaxAmount) {
        this.salesTaxAmount = salesTaxAmount;
    }
    public Double getCityTaxRate() {
        return cityTaxRate;
    }
    public void setCityTaxRate(Double cityTaxRate) {
        this.cityTaxRate = cityTaxRate;
    }
    public Double getCityTaxAmount() {
        return cityTaxAmount;
    }
    public void setCityTaxAmount(Double cityTaxAmount) {
        this.cityTaxAmount = cityTaxAmount;
    }
    public Double getCountryTaxRate() {
        return countryTaxRate;
    }
    public void setCountryTaxRate(Double countryTaxRate) {
        this.countryTaxRate = countryTaxRate;
    }
    public Double getCountryTaxAmount() {
        return countryTaxAmount;
    }
    public void setCountryTaxAmount(Double countryTaxAmount) {
        this.countryTaxAmount = countryTaxAmount;
    }
    public Double getStateTaxRate() {
        return stateTaxRate;
    }
    public void setStateTaxRate(Double stateTaxRate) {
        this.stateTaxRate = stateTaxRate;
    }
    public Double getStateTaxAmount() {
        return stateTaxAmount;
    }
    public void setStateTaxAmount(Double stateTaxAmount) {
        this.stateTaxAmount = stateTaxAmount;
    }
    public Double getFederalTaxRate() {
        return federalTaxRate;
    }
    public void setFederalTaxRate(Double federalTaxRate) {
        this.federalTaxRate = federalTaxRate;
    }
    public Double getFederalTaxAmount() {
        return federalTaxAmount;
    }
    public void setFederalTaxAmount(Double federalTaxAmount) {
        this.federalTaxAmount = federalTaxAmount;
    }
    public Double getTotalTaxAmount() {
        return totalTaxAmount;
    }
    public void setTotalTaxAmount(Double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }
    public Double getStHours() {
        return stHours;
    }
    public void setStHours(Double stHours) {
        this.stHours = stHours;
    }
    public Double getStAmount() {
        return stAmount;
    }
    public void setStAmount(Double stAmount) {
        this.stAmount = stAmount;
    }
    public Double getOtHours() {
        return otHours;
    }
    public void setOtHours(Double otHours) {
        this.otHours = otHours;
    }
    public Double getOtAmount() {
        return otAmount;
    }
    public void setOtAmount(Double otAmount) {
        this.otAmount = otAmount;
    }
    public Double getDtHours() {
        return dtHours;
    }
    public void setDtHours(Double dtHours) {
        this.dtHours = dtHours;
    }
    public Double getDtAmount() {
        return dtAmount;
    }
    public void setDtAmount(Double dtAmount) {
        this.dtAmount = dtAmount;
    }
   
    public Double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
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
	public BigDecimal getBillToClientST() {
		return billToClientST;
	}
	public void setBillToClientST(BigDecimal billToClientST) {
		this.billToClientST = billToClientST;
	}
	public BigDecimal getBillToClientOT() {
		return billToClientOT;
	}
	public void setBillToClientOT(BigDecimal billToClientOT) {
		this.billToClientOT = billToClientOT;
	}
	public BigDecimal getBillToClientDT() {
		return billToClientDT;
	}
	public void setBillToClientDT(BigDecimal billToClientDT) {
		this.billToClientDT = billToClientDT;
	}
	public BigDecimal getBillClientrate() {
		return billClientrate;
	}
	public void setBillClientrate(BigDecimal billClientrate) {
		this.billClientrate = billClientrate;
	}
	public String getStotdtRate() {
		return stotdtRate;
	}
	public void setStotdtRate(String stotdtRate) {
		this.stotdtRate = stotdtRate;
	}
	public String getStotdtDoubleRate() {
		return stotdtDoubleRate;
	}
	public void setStotdtDoubleRate(String stotdtDoubleRate) {
		this.stotdtDoubleRate = stotdtDoubleRate;
	}
	public Double getPtoHours() {
		return ptoHours;
	}
	public void setPtoHours(Double ptoHours) {
		this.ptoHours = ptoHours;
	}
	public Double getLeaveHours() {
		return leaveHours;
	}
	public void setLeaveHours(Double leaveHours) {
		this.leaveHours = leaveHours;
	}
	public int getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}
	public Double getWorkHours() {
		return workHours;
	}
	public void setWorkHours(Double workHours) {
		this.workHours = workHours;
	}
	public Double getUnitAmount() {
		return unitAmount;
	}
	public void setUnitAmount(Double unitAmount) {
		this.unitAmount = unitAmount;
	}
	public Integer getTotalUnits() {
		return totalUnits;
	}
	public void setTotalUnits(Integer totalUnits) {
		this.totalUnits = totalUnits;
	}
	
}