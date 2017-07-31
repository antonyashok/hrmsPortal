package com.tm.invoice.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.tm.expense.domain.ExpenseView;
import com.tm.invoice.domain.InvoiceDetail;
import com.tm.timesheetgeneration.domain.Timesheet;

public class InvoiceDTO implements Serializable {

	private static final long serialVersionUID = 425556417088502369L;

	private ExpenseDTO expenseDetails;
	private UserPreferenceDTO userPreference;
	private ClientInfoDTO clientInfo;
	private BillToManagerDTO billToManager;
	private PDFAttachmentDTO invoicePDF;
	private PDFAttachmentDTO timesheetPDF;
	private PDFAttachmentDTO expensePDF;
	private List<BillingProfileDTO> billingProfiles;
	private BigDecimal billingAmount;
	private DeliveryDTO deliveryMethod;
	private List<Timesheet> timesheetList; 
	private PurchaseOrderDTO purchaseOrder;
	private List<InvoiceDetail> invoiceDetails;
	private String exceptionReason;
	private UUID id;
	private String invoiceNumber;
	private Long billToClientId;
	private String billToClientName;
	private Long endClientId;
	private String endClientName;
	private Long financeRepresentId;
	private String financeRepresentName;
	private String timesheetType;
	private String location;
	private String country;
	private String delivery;
	private String currencyType;
	private BigDecimal amount;
	private String status;
	private String timesheetAttachment;
	private String billableExpensesAttachment;
	private Date startDate;
	private Date endDate;
	private String comments;
	private String exceptionSource;
	private String attentionManagerName;
	private List<InvoiceExceptionDetailsDTO> invoiceExceptionDetail;
	private InvoiceSetupDTO invoiceSetup;
	private String poNumber;
	private Date billDate;
	private UUID invoiceQueueId;
	private Date invoiceDate;
	private String billCycle;
	private LocalDate runCronDate;	
	private String projectName;
	private UUID projectId;
	private List<ExpenseView> expensesList;
	private List<ExpenseView> expensesReceiptList;
	private String billToManagerName;
	private String billToManagerEmailId; 
	private BigDecimal expenseAmount;
	private Integer invoiceSetupNumber;
	private String invoiceSetupName;
	private String existingInvoiceNumber;
	private String invoiceSetupNotes;

	public ExpenseDTO getExpenseDetails() {
		return expenseDetails;
	}

	public void setExpenseDetails(ExpenseDTO expenseDetails) {
		this.expenseDetails = expenseDetails;
	}

	public UserPreferenceDTO getUserPreference() {
		return userPreference;
	}

	public void setUserPreference(UserPreferenceDTO userPreference) {
		this.userPreference = userPreference;
	}

	public ClientInfoDTO getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(ClientInfoDTO clientInfo) {
		this.clientInfo = clientInfo;
	}

	public BillToManagerDTO getBillToManager() {
		return billToManager;
	}

	public void setBillToManager(BillToManagerDTO billToManager) {
		this.billToManager = billToManager;
	}

	public PDFAttachmentDTO getTimesheetPDF() {
		return timesheetPDF;
	}

	public void setTimesheetPDF(PDFAttachmentDTO timesheetPDF) {
		this.timesheetPDF = timesheetPDF;
	}

	public PDFAttachmentDTO getExpensePDF() {
		return expensePDF;
	}

	public void setExpensePDF(PDFAttachmentDTO expensePDF) {
		this.expensePDF = expensePDF;
	}

	public List<BillingProfileDTO> getBillingProfiles() {
		return billingProfiles;
	}

	public void setBillingProfiles(List<BillingProfileDTO> billingProfiles) {
		this.billingProfiles = billingProfiles;
	}

	public BigDecimal getBillingAmount() {
		return billingAmount;
	}

	public void setBillingAmount(BigDecimal billingAmount) {
		this.billingAmount = billingAmount;
	}

	public PurchaseOrderDTO getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrderDTO purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public String getExceptionReason() {
		return exceptionReason;
	}

	public void setExceptionReason(String exceptionReason) {
		this.exceptionReason = exceptionReason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DeliveryDTO getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(DeliveryDTO deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public Long getBillToClientId() {
		return billToClientId;
	}

	public void setBillToClientId(Long billToClientId) {
		this.billToClientId = billToClientId;
	}

	public String getBillToClientName() {
		return billToClientName;
	}

	public void setBillToClientName(String billToClientName) {
		this.billToClientName = billToClientName;
	}

	public Long getEndClientId() {
		return endClientId;
	}

	public void setEndClientId(Long endClientId) {
		this.endClientId = endClientId;
	}

	public String getEndClientName() {
		return endClientName;
	}

	public void setEndClientName(String endClientName) {
		this.endClientName = endClientName;
	}

	public Long getFinanceRepresentId() {
		return financeRepresentId;
	}

	public void setFinanceRepresentId(Long financeRepresentId) {
		this.financeRepresentId = financeRepresentId;
	}

	public String getFinanceRepresentName() {
		return financeRepresentName;
	}

	public void setFinanceRepresentName(String financeRepresentName) {
		this.financeRepresentName = financeRepresentName;
	}

	public String getTimesheetType() {
		return timesheetType;
	}

	public void setTimesheetType(String timesheetType) {
		this.timesheetType = timesheetType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getTimesheetAttachment() {
		return timesheetAttachment;
	}

	public void setTimesheetAttachment(String timesheetAttachment) {
		this.timesheetAttachment = timesheetAttachment;
	}

	public String getBillableExpensesAttachment() {
		return billableExpensesAttachment;
	}

	public void setBillableExpensesAttachment(String billableExpensesAttachment) {
		this.billableExpensesAttachment = billableExpensesAttachment;
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

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getExceptionSource() {
		return exceptionSource;
	}

	public void setExceptionSource(String exceptionSource) {
		this.exceptionSource = exceptionSource;
	}

	public String getAttentionManagerName() {
		return attentionManagerName;
	}

	public void setAttentionManagerName(String attentionManagerName) {
		this.attentionManagerName = attentionManagerName;
	}

	public List<InvoiceExceptionDetailsDTO> getInvoiceExceptionDetail() {
		return invoiceExceptionDetail;
	}

	public void setInvoiceExceptionDetail(List<InvoiceExceptionDetailsDTO> invoiceExceptionDetail) {
		this.invoiceExceptionDetail = invoiceExceptionDetail;
	}

	public List<InvoiceDetail> getInvoiceDetails() {
		return invoiceDetails;
	}

	public void setInvoiceDetails(List<InvoiceDetail> invoiceDetails) {
		this.invoiceDetails = invoiceDetails;
	}

	public InvoiceSetupDTO getInvoiceSetup() {
		return invoiceSetup;
	}

	public void setInvoiceSetup(InvoiceSetupDTO invoiceSetup) {
		this.invoiceSetup = invoiceSetup;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public PDFAttachmentDTO getInvoicePDF() {
		return invoicePDF;
	}

	public void setInvoicePDF(PDFAttachmentDTO invoicePDF) {
		this.invoicePDF = invoicePDF;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public UUID getInvoiceQueueId() {
		return invoiceQueueId;
	}

	public void setInvoiceQueueId(UUID invoiceQueueId) {
		this.invoiceQueueId = invoiceQueueId;
	}

	public String getBillCycle() {
		return billCycle;
	}

	public void setBillCycle(String billCycle) {
		this.billCycle = billCycle;
	}

	public List<Timesheet> getTimesheetList() {
		return timesheetList;
	}

	public void setTimesheetList(List<Timesheet> timesheetList) {
		this.timesheetList = timesheetList;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public UUID getProjectId() {
		return projectId;
	}

	public void setProjectId(UUID projectId) {
		this.projectId = projectId;
	}

	public List<ExpenseView> getExpensesList() {
		return expensesList;
	}

	public void setExpensesList(List<ExpenseView> expensesList) {
		this.expensesList = expensesList;
	}

	public List<ExpenseView> getExpensesReceiptList() {
		return expensesReceiptList;
	}

	public void setExpensesReceiptList(List<ExpenseView> expensesReceiptList) {
		this.expensesReceiptList = expensesReceiptList;
	}

    public LocalDate getRunCronDate() {
        return runCronDate;
    }

    public void setRunCronDate(LocalDate runCronDate) {
        this.runCronDate = runCronDate;
    }

    public String getBillToManagerName() {
        return billToManagerName;
    }

    public void setBillToManagerName(String billToManagerName) {
        this.billToManagerName = billToManagerName;
    }

    public String getBillToManagerEmailId() {
        return billToManagerEmailId;
    }

    public void setBillToManagerEmailId(String billToManagerEmailId) {
        this.billToManagerEmailId = billToManagerEmailId;
    }
	public BigDecimal getExpenseAmount() {
		return expenseAmount;
	}

	public void setExpenseAmount(BigDecimal expenseAmount) {
		this.expenseAmount = expenseAmount;
	}

	public Integer getInvoiceSetupNumber() {
		return invoiceSetupNumber;
	}

	public void setInvoiceSetupNumber(Integer invoiceSetupNumber) {
		this.invoiceSetupNumber = invoiceSetupNumber;
	}

	public String getInvoiceSetupName() {
		return invoiceSetupName;
	}

	public void setInvoiceSetupName(String invoiceSetupName) {
		this.invoiceSetupName = invoiceSetupName;
	}

	public String getExistingInvoiceNumber() {
		return existingInvoiceNumber;
	}

	public void setExistingInvoiceNumber(String existingInvoiceNumber) {
		this.existingInvoiceNumber = existingInvoiceNumber;
	}

	public String getInvoiceSetupNotes() {
		return invoiceSetupNotes;
	}

	public void setInvoiceSetupNotes(String invoiceSetupNotes) {
		this.invoiceSetupNotes = invoiceSetupNotes;
	}	
	
}
