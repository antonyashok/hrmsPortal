package com.tm.invoice.mongo.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.core.audit.mysql.BaseAuditableEntity;
import com.tm.commonapi.web.core.data.IEntity;

@Document(collection = "ApprovedQueue")
public class PendingBillDetails extends BaseAuditableEntity implements IEntity<ObjectId> {

    private static final long serialVersionUID = 8299314219982890891L;

    private String billingProfile;
    private Long engagementID;
    private Long empID;
    private Long fileNumber;
    private String billToClient;
    private String accountManager;

    @NotNull(message = InvoiceConstants.ENDCLIENT_REQUIRED_ERROR_MESSAGE)
    @NotBlank(message = InvoiceConstants.ENDCLIENT_BLANK_ERROR_MESSAGE)
    private String endClient;

    private String poNumber;
    private String projectName;
    private Long sow;

    @NotNull(message = InvoiceConstants.APCONTACT_REQUIRED_ERROR_MESSAGE)
    @NotBlank(message = InvoiceConstants.APCONTACT_BLANK_ERROR_MESSAGE)
    private String apContact;

    private String office;
    @NotNull(message = InvoiceConstants.HIRINGMANAGER_REQUIRED_ERROR_MESSAGE)
    @NotBlank(message = InvoiceConstants.HIRINGMANAGER_BLANK_ERROR_MESSAGE)
    private String hiringManager;

    private String division;

    @NotNull(message = InvoiceConstants.TIMESHEETTYPE_REQUIRED_ERROR_MESSAGE)
    @NotBlank(message = InvoiceConstants.TIMESHEETTYPE_BLANK_ERROR_MESSAGE)
    private String timesheetType;

    private String rateType;
    private String timesheetMethod;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String startDate;

    private String timesheetRuleType;
    private String recruiter;
    private String billType;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String profileActiveDate;

    private Long contractorId;
    private String costCenter;
    private Long fein;
    private String departmentNumber;
    private BigDecimal processingFeeRate;
    private String specialFees;
    private String worksiteAddress;
    private BigDecimal county;
    private BigDecimal state;
    private String totalSalesTax;
    private String taxExempt;
    private String invoiceSetupName;
    private String invoiceSetupId;
    private String billToManager;
    private String billToManagerAddress;
    private String netTerm;
    private String billCycle;
    private String delivery;
    private String invoiceEmail;
    private String emailSubject;
    private String status;
    private String isActive;
    private List<TaskDetMongoDTO> subTasksDetails;
    private Long billingSpecilistId;

    @Column
    private ObjectId _id;

    @Column
    private Long contractId;

    @Column(name = "contractorName")
    private String contractorName;

    @Column(name = "project")
    private String project;

    @Column(name = "projectId")
    private Long projectId;

    @Column(name = "recuriter")
    private String recuriter;

    @Column(name = "billingSpecialist")
    private String billingSpecialist;

    @Transient
    private int startIndex;

    @Transient
    private int results;

    @Transient
    private int totalRecords;

    @Transient
    private String sortDirection;

    @Transient
    private String sortProperty;

    @Transient
    private List<Long> contractorIdList;

    @Transient
    private List<Integer> projectIdList;

    public String getBillingProfile() {
        return billingProfile;
    }

    public Long getEngagementID() {
        return engagementID;
    }

    public Long getEmpID() {
        return empID;
    }

    public Long getFileNumber() {
        return fileNumber;
    }

    public String getBillToClient() {
        return billToClient;
    }

    public String getAccountManager() {
        return accountManager;
    }

    public String getEndClient() {
        return endClient;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public Long getSow() {
        return sow;
    }

    public String getApContact() {
        return apContact;
    }

    public String getOffice() {
        return office;
    }

    public String getHiringManager() {
        return hiringManager;
    }

    public String getDivision() {
        return division;
    }

    public String getTimesheetType() {
        return timesheetType;
    }

    public String getRateType() {
        return rateType;
    }

    public String getTimesheetMethod() {
        return timesheetMethod;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getTimesheetRuleType() {
        return timesheetRuleType;
    }

    public String getRecruiter() {
        return recruiter;
    }

    public String getBillType() {
        return billType;
    }

    public String getProfileActiveDate() {
        return profileActiveDate;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public Long getFein() {
        return fein;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public BigDecimal getProcessingFeeRate() {
        return processingFeeRate;
    }

    public String getSpecialFees() {
        return specialFees;
    }

    public String getWorksiteAddress() {
        return worksiteAddress;
    }

    public BigDecimal getCounty() {
        return county;
    }

    public BigDecimal getState() {
        return state;
    }

    public String getTotalSalesTax() {
        return totalSalesTax;
    }

    public String getTaxExempt() {
        return taxExempt;
    }

    public String getInvoiceSetupName() {
        return invoiceSetupName;
    }

    public String getInvoiceSetupId() {
        return invoiceSetupId;
    }

    public String getBillToManager() {
        return billToManager;
    }

    public String getBillToManagerAddress() {
        return billToManagerAddress;
    }

    public String getNetTerm() {
        return netTerm;
    }

    public String getBillCycle() {
        return billCycle;
    }

    public String getDelivery() {
        return delivery;
    }

    public String getInvoiceEmail() {
        return invoiceEmail;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public String getStatus() {
        return status;
    }

    public String getIsActive() {
        return isActive;
    }

    public List<TaskDetMongoDTO> getSubTasksDetails() {
        return subTasksDetails;
    }

    public Long getBillingSpecilistId() {
        return billingSpecilistId;
    }

    public ObjectId get_id() {
        return _id;
    }

    public Long getContractId() {
        return contractId;
    }

    public String getContractorName() {
        return contractorName;
    }

    public String getProject() {
        return project;
    }

    public Long getProjectId() {
        return projectId;
    }

    public String getRecuriter() {
        return recuriter;
    }

    public String getBillingSpecialist() {
        return billingSpecialist;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getResults() {
        return results;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public String getSortProperty() {
        return sortProperty;
    }

    public List<Long> getContractorIdList() {
        return contractorIdList;
    }

    public List<Integer> getProjectIdList() {
        return projectIdList;
    }

    public void setBillingProfile(String billingProfile) {
        this.billingProfile = billingProfile;
    }

    public void setEngagementID(Long engagementID) {
        this.engagementID = engagementID;
    }

    public void setEmpID(Long empID) {
        this.empID = empID;
    }

    public void setFileNumber(Long fileNumber) {
        this.fileNumber = fileNumber;
    }

    public void setBillToClient(String billToClient) {
        this.billToClient = billToClient;
    }

    public void setAccountManager(String accountManager) {
        this.accountManager = accountManager;
    }

    public void setEndClient(String endClient) {
        this.endClient = endClient;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setSow(Long sow) {
        this.sow = sow;
    }

    public void setApContact(String apContact) {
        this.apContact = apContact;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public void setHiringManager(String hiringManager) {
        this.hiringManager = hiringManager;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public void setTimesheetType(String timesheetType) {
        this.timesheetType = timesheetType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public void setTimesheetMethod(String timesheetMethod) {
        this.timesheetMethod = timesheetMethod;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setTimesheetRuleType(String timesheetRuleType) {
        this.timesheetRuleType = timesheetRuleType;
    }

    public void setRecruiter(String recruiter) {
        this.recruiter = recruiter;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public void setProfileActiveDate(String profileActiveDate) {
        this.profileActiveDate = profileActiveDate;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public void setFein(Long fein) {
        this.fein = fein;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }

    public void setProcessingFeeRate(BigDecimal processingFeeRate) {
        this.processingFeeRate = processingFeeRate;
    }

    public void setSpecialFees(String specialFees) {
        this.specialFees = specialFees;
    }

    public void setWorksiteAddress(String worksiteAddress) {
        this.worksiteAddress = worksiteAddress;
    }

    public void setCounty(BigDecimal county) {
        this.county = county;
    }

    public void setState(BigDecimal state) {
        this.state = state;
    }

    public void setTotalSalesTax(String totalSalesTax) {
        this.totalSalesTax = totalSalesTax;
    }

    public void setTaxExempt(String taxExempt) {
        this.taxExempt = taxExempt;
    }

    public void setInvoiceSetupName(String invoiceSetupName) {
        this.invoiceSetupName = invoiceSetupName;
    }

    public void setInvoiceSetupId(String invoiceSetupId) {
        this.invoiceSetupId = invoiceSetupId;
    }

    public void setBillToManager(String billToManager) {
        this.billToManager = billToManager;
    }

    public void setBillToManagerAddress(String billToManagerAddress) {
        this.billToManagerAddress = billToManagerAddress;
    }

    public void setNetTerm(String netTerm) {
        this.netTerm = netTerm;
    }

    public void setBillCycle(String billCycle) {
        this.billCycle = billCycle;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public void setInvoiceEmail(String invoiceEmail) {
        this.invoiceEmail = invoiceEmail;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public void setSubTasksDetails(List<TaskDetMongoDTO> subTasksDetails) {
        this.subTasksDetails = subTasksDetails;
    }

    public void setBillingSpecilistId(Long billingSpecilistId) {
        this.billingSpecilistId = billingSpecilistId;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public void setRecuriter(String recuriter) {
        this.recuriter = recuriter;
    }

    public void setBillingSpecialist(String billingSpecialist) {
        this.billingSpecialist = billingSpecialist;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public void setSortProperty(String sortProperty) {
        this.sortProperty = sortProperty;
    }

    public void setContractorIdList(List<Long> contractorIdList) {
        this.contractorIdList = contractorIdList;
    }

    public void setProjectIdList(List<Integer> projectIdList) {
        this.projectIdList = projectIdList;
    }

    @Override
    public ObjectId getId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setId(ObjectId id) {
        // TODO Auto-generated method stub

    }

}
