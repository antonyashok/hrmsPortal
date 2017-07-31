package com.tm.invoice.domain;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tm.commonapi.web.core.data.IEntity;
import com.tm.invoice.enums.DeliveryEnum;

@JsonIgnoreProperties({"auditDetails"})
@Document(collection = "invoiceSetupDetails")
public class InvoiceSetupDetails implements IEntity<ObjectId>{

    private static final long serialVersionUID = 5157854896200804845L;

    public enum BillCycleEnum {
        Monthly, MonthlyByday, SemiMonthly, BiWeeklyEven, Weekly
    }

    public enum DaysEnum {
        Sun, Mon, Tue, Wed, Thu, Fri, Sat
    }

    public enum ConsolidateInvoiceEnum {
        Y, N
    }

    public enum PrebillKeepAccruingEnum {
        Y, N
    }

    public enum HaveSalesTaxEnum {
        Y, N
    }

    public enum DoNotUsePrebillOnceTimeframeExceedEnum {
        Y, N
    }

    public enum GenerateInvoiceEnum {
        Y, N
    }

    public enum PrebillWithoutContractorsEnum {
        Y, N
    }

    public enum IncludeExpensesEnum {
        Y, N
    }

    public enum IncludeTimesheetEnum {
        Y, N
    }

    public enum MultipleContractorEnum {
        Y, N
    }

    public enum ShowHiringMgrEnum {
        Y, N
    }

    public enum SubProjectDifferentInvoiceEnum {
        Y, N
    }

    public enum SubProjectDifferentItemEnum {
        Y, N
    }

    public enum SubProjectSingleItemEnum {
        Y, N
    }

    public enum DefaultToBillProfileEnum {
        Y, N
    }

    public enum GenerateSeparateInvoiceEnum {
        Y, N
    }

    public enum IncludeExpensesDocEnum {
        Y, N
    }

    public enum InvoiceTypeEnum {
        Regular, Irregular, MilestoneAccrue, MilestoneFixed, Prebill
    }

    public enum DefaultToSubjectEnum {
        Y, N
    }

    public enum AutoGenerateInvoiceEnum {
        Y, N
    }

    public enum ExcludeContractorName {
        Y, N
    }

    @Id
    @GeneratedValue(generator = "id")
    @Column
    private ObjectId _id;

    @Column
    private Long invoiceSetupId;

    /*
     * @Column private String billToClient;
     * 
     * @Column private String endClient;
     */

    @Column
    private String projectName;

    /*
     * @Column private LineOfBusinessEnum lineOfBusiness;
     */

    @Column
    private Long billToClientId;

    @Column
    private String status;

    @Column
    private Long contractorId;

    /*
     * @Column private Long employeeId;
     * 
     * @Column private Long engagementId;
     * 
     * @Column private CurrencyEnum payCurrency;
     * 
     * @Column private String paymentTerms;
     * 
     * @Column private String terms;
     * 
     * @Column private String deptNumber;
     * 
     * @Column private Long fein;
     * 
     * @Column private String costCenter;
     */

    @Column
    private String invoiceSetupName;

    @Column
    private String description;

    @Column
    private List<InvoiceType> invoiceTypeFields;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String irrStartDate;

    private String billCyclePeriod;

    @Enumerated(value = EnumType.STRING)
    private DaysEnum days;

    @Enumerated(value = EnumType.STRING)
    private BillCycleEnum billCycle;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String prebillStartDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String prebillEndDate;

    private int prebillNoOfHrs;

    private BigDecimal prebillAmount;

    private BigDecimal initialPrebillAmount;

    @Enumerated(value = EnumType.STRING)
    private PrebillKeepAccruingEnum prebillKeepAccruing = PrebillKeepAccruingEnum.N;

    @Enumerated(value = EnumType.STRING)
    private HaveSalesTaxEnum haveSalesTax = HaveSalesTaxEnum.N;

    @Enumerated(value = EnumType.STRING)
    private DoNotUsePrebillOnceTimeframeExceedEnum doNotUsePrebillOnceTimeframeExceedEnum =
            DoNotUsePrebillOnceTimeframeExceedEnum.N;

    @Enumerated(value = EnumType.STRING)
    private PrebillWithoutContractorsEnum prebillWithoutContractors =
            PrebillWithoutContractorsEnum.N;

    @Enumerated(value = EnumType.STRING)
    private GenerateInvoiceEnum generateInvoice = GenerateInvoiceEnum.N;

    private String prebillNotes;

    @Enumerated(value = EnumType.STRING)
    private InvoiceTypeEnum invoiceTypeName;

    @Column
    private Long templateId;

    @Column
    private String templateName;

    @Column
    private String billingSpecialist;

    @Column
    private String arSpecialist;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String effectiveFromDate;

    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private String effectiveToDate;

    @Column
    private DeliveryEnum delivery;

    @Column
    private String recipientEmail;

    @Column
    private String emailSubject;

    @Column
    @Enumerated(value = EnumType.STRING)
    private DefaultToSubjectEnum defaultToSubject = DefaultToSubjectEnum.N;

    @Column
    @Enumerated(value = EnumType.STRING)
    private DefaultToBillProfileEnum defaultToBillProfile = DefaultToBillProfileEnum.N;

    @Column
    @Enumerated(value = EnumType.STRING)
    private IncludeTimesheetEnum includeTimesheet = IncludeTimesheetEnum.N;

    @Column
    @Enumerated(value = EnumType.STRING)
    private ExcludeContractorName excludeContractorName = ExcludeContractorName.N;

    @Column
    @Enumerated(value = EnumType.STRING)
    private AutoGenerateInvoiceEnum autoGenerateInvoice = AutoGenerateInvoiceEnum.N;

    @Column
    @Enumerated(value = EnumType.STRING)
    private GenerateSeparateInvoiceEnum generateSeparateInvoice = GenerateSeparateInvoiceEnum.N;

    @Column
    @Enumerated(value = EnumType.STRING)
    private MultipleContractorEnum multipleContractor = MultipleContractorEnum.N;

    @Column
    @Enumerated(value = EnumType.STRING)
    private ShowHiringMgrEnum showHiringMgr = ShowHiringMgrEnum.N;

    @Column
    @Enumerated(value = EnumType.STRING)
    private SubProjectSingleItemEnum subProjectSingleItem = SubProjectSingleItemEnum.N;

    @Column
    @Enumerated(value = EnumType.STRING)
    private SubProjectDifferentItemEnum subProjectDifferentItem = SubProjectDifferentItemEnum.N;

    @Column
    @Enumerated(value = EnumType.STRING)
    private SubProjectDifferentInvoiceEnum subProjectDifferentInvoice =
            SubProjectDifferentInvoiceEnum.N;

    @Column
    @Enumerated(value = EnumType.STRING)
    private IncludeExpensesEnum includeExpenses = IncludeExpensesEnum.N;

    @Column
    @Enumerated(value = EnumType.STRING)
    private IncludeExpensesDocEnum includeExpensesDoc = IncludeExpensesDocEnum.N;

    @Column
    private String fileNumber;

    @Column
    private Long billCycleId;

    @Column
    private String billCycleStartEndId;

    @Column
    private Long billCycleDayId;

    @Column
    private Boolean consolidatedInvocie;

    @Column
    private String notes;

    @Column
    private String invoiceCreatedDate;

    @Column
    private String poNumber;

    @Column
    private List<PurchaseOrder> poDetailsList;

    @Column
    private List<InvoicSetupAssignBill> billToManager;

    @Column
    private InvoiceSetupBillToClient billToclientData;

    @Column
    private List<String> activityLog;

    @Column
    private List<InvoiceSetupNotes> notesList;

    @Transient
    private String saveStatus;

    @Transient
    private Boolean subProjectDifferentItemBoolean;

    @Transient
    private Boolean defaultToSubjectBoolean;

    @Transient
    private Boolean includeTimesheetBoolean;

    @Transient
    private Boolean prebillKeepAccruingBoolean;

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
    private int bmTotalRecords;

    @Transient
    private List<Long> invoiseSetupIds;

    @Transient
    private Boolean subProjectDifferentInvoiceBoolean;

    @Transient
    private Boolean generateSeparateInvoiceBoolean;

    @Transient
    private Boolean includeExpensesBoolean;

    @Transient
    private Boolean includeExpensesDocBoolean;

    @Transient
    private Boolean showHiringMgrBoolean;

    @Transient
    private Boolean multipleContractorBoolean;

    @Transient
    private Boolean autoGenerateInvoiceBoolean;

    @Transient
    private Boolean excludeContractorNameBoolean;

    @Transient
    private Boolean defaultToBillProfileBoolean;

    @Transient
    private Boolean generateInvoiceBoolean;

    @Transient
    private Boolean prebillWithoutContractorsBoolean;

    @Transient
    private Boolean doNotUsePrebillOnceExceedBoolean;

    @Transient
    private Boolean haveSalesTaxBoolean;

    @Transient
    private Boolean consolidateInvoiceBoolean;

    @Transient
    private Boolean subProjectSingleItemBoolean;

    public ObjectId get_id() {
        return _id;
    }

    public Long getInvoiceSetupId() {
        return invoiceSetupId;
    }

    public String getProjectName() {
        return projectName;
    }

    public Long getBillToClientId() {
        return billToClientId;
    }

    public String getStatus() {
        return status;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public String getInvoiceSetupName() {
        return invoiceSetupName;
    }

    public String getDescription() {
        return description;
    }

    public List<InvoiceType> getInvoiceTypeFields() {
        return invoiceTypeFields;
    }

    public String getIrrStartDate() {
        return irrStartDate;
    }

    public String getBillCyclePeriod() {
        return billCyclePeriod;
    }

    public DaysEnum getDays() {
        return days;
    }

    public BillCycleEnum getBillCycle() {
        return billCycle;
    }

    public String getPrebillStartDate() {
        return prebillStartDate;
    }

    public String getPrebillEndDate() {
        return prebillEndDate;
    }

    public int getPrebillNoOfHrs() {
        return prebillNoOfHrs;
    }

    public BigDecimal getPrebillAmount() {
        return prebillAmount;
    }

    public BigDecimal getInitialPrebillAmount() {
        return initialPrebillAmount;
    }

    public PrebillKeepAccruingEnum getPrebillKeepAccruing() {
        return prebillKeepAccruing;
    }

    public HaveSalesTaxEnum getHaveSalesTax() {
        return haveSalesTax;
    }

    public DoNotUsePrebillOnceTimeframeExceedEnum getDoNotUsePrebillOnceTimeframeExceedEnum() {
        return doNotUsePrebillOnceTimeframeExceedEnum;
    }

    public PrebillWithoutContractorsEnum getPrebillWithoutContractors() {
        return prebillWithoutContractors;
    }

    public GenerateInvoiceEnum getGenerateInvoice() {
        return generateInvoice;
    }

    public String getPrebillNotes() {
        return prebillNotes;
    }

    public InvoiceTypeEnum getInvoiceTypeName() {
        return invoiceTypeName;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getBillingSpecialist() {
        return billingSpecialist;
    }

    public String getArSpecialist() {
        return arSpecialist;
    }

    public String getEffectiveFromDate() {
        return effectiveFromDate;
    }

    public String getEffectiveToDate() {
        return effectiveToDate;
    }

    public DeliveryEnum getDelivery() {
        return delivery;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public DefaultToSubjectEnum getDefaultToSubject() {
        return defaultToSubject;
    }

    public DefaultToBillProfileEnum getDefaultToBillProfile() {
        return defaultToBillProfile;
    }

    public IncludeTimesheetEnum getIncludeTimesheet() {
        return includeTimesheet;
    }

    public ExcludeContractorName getExcludeContractorName() {
        return excludeContractorName;
    }

    public AutoGenerateInvoiceEnum getAutoGenerateInvoice() {
        return autoGenerateInvoice;
    }

    public GenerateSeparateInvoiceEnum getGenerateSeparateInvoice() {
        return generateSeparateInvoice;
    }

    public MultipleContractorEnum getMultipleContractor() {
        return multipleContractor;
    }

    public ShowHiringMgrEnum getShowHiringMgr() {
        return showHiringMgr;
    }

    public SubProjectSingleItemEnum getSubProjectSingleItem() {
        return subProjectSingleItem;
    }

    public SubProjectDifferentItemEnum getSubProjectDifferentItem() {
        return subProjectDifferentItem;
    }

    public SubProjectDifferentInvoiceEnum getSubProjectDifferentInvoice() {
        return subProjectDifferentInvoice;
    }

    public IncludeExpensesEnum getIncludeExpenses() {
        return includeExpenses;
    }

    public IncludeExpensesDocEnum getIncludeExpensesDoc() {
        return includeExpensesDoc;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public Long getBillCycleId() {
        return billCycleId;
    }

    public String getBillCycleStartEndId() {
        return billCycleStartEndId;
    }

    public Long getBillCycleDayId() {
        return billCycleDayId;
    }

    public Boolean getConsolidatedInvocie() {
        return consolidatedInvocie;
    }

    public String getNotes() {
        return notes;
    }

    public String getInvoiceCreatedDate() {
        return invoiceCreatedDate;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public List<PurchaseOrder> getPoDetailsList() {
        return poDetailsList;
    }

    public List<InvoicSetupAssignBill> getBillToManager() {
        return billToManager;
    }

    public InvoiceSetupBillToClient getBillToclientData() {
        return billToclientData;
    }

    public List<String> getActivityLog() {
        return activityLog;
    }

    public List<InvoiceSetupNotes> getNotesList() {
        return notesList;
    }

    public String getSaveStatus() {
        return saveStatus;
    }

    public Boolean getSubProjectDifferentItemBoolean() {
        return subProjectDifferentItemBoolean;
    }

    public Boolean getDefaultToSubjectBoolean() {
        return defaultToSubjectBoolean;
    }

    public Boolean getIncludeTimesheetBoolean() {
        return includeTimesheetBoolean;
    }

    public Boolean getPrebillKeepAccruingBoolean() {
        return prebillKeepAccruingBoolean;
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

    public int getBmTotalRecords() {
        return bmTotalRecords;
    }

    public List<Long> getInvoiseSetupIds() {
        return invoiseSetupIds;
    }

    public Boolean getSubProjectDifferentInvoiceBoolean() {
        return subProjectDifferentInvoiceBoolean;
    }

    public Boolean getGenerateSeparateInvoiceBoolean() {
        return generateSeparateInvoiceBoolean;
    }

    public Boolean getIncludeExpensesBoolean() {
        return includeExpensesBoolean;
    }

    public Boolean getIncludeExpensesDocBoolean() {
        return includeExpensesDocBoolean;
    }

    public Boolean getShowHiringMgrBoolean() {
        return showHiringMgrBoolean;
    }

    public Boolean getMultipleContractorBoolean() {
        return multipleContractorBoolean;
    }

    public Boolean getAutoGenerateInvoiceBoolean() {
        return autoGenerateInvoiceBoolean;
    }

    public Boolean getExcludeContractorNameBoolean() {
        return excludeContractorNameBoolean;
    }

    public Boolean getDefaultToBillProfileBoolean() {
        return defaultToBillProfileBoolean;
    }

    public Boolean getGenerateInvoiceBoolean() {
        return generateInvoiceBoolean;
    }

    public Boolean getPrebillWithoutContractorsBoolean() {
        return prebillWithoutContractorsBoolean;
    }

    public Boolean getDoNotUsePrebillOnceExceedBoolean() {
        return doNotUsePrebillOnceExceedBoolean;
    }

    public Boolean getHaveSalesTaxBoolean() {
        return haveSalesTaxBoolean;
    }

    public Boolean getConsolidateInvoiceBoolean() {
        return consolidateInvoiceBoolean;
    }

    public Boolean getSubProjectSingleItemBoolean() {
        return subProjectSingleItemBoolean;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public void setInvoiceSetupId(Long invoiceSetupId) {
        this.invoiceSetupId = invoiceSetupId;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setBillToClientId(Long billToClientId) {
        this.billToClientId = billToClientId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    public void setInvoiceSetupName(String invoiceSetupName) {
        this.invoiceSetupName = invoiceSetupName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInvoiceTypeFields(List<InvoiceType> invoiceTypeFields) {
        this.invoiceTypeFields = invoiceTypeFields;
    }

    public void setIrrStartDate(String irrStartDate) {
        this.irrStartDate = irrStartDate;
    }

    public void setBillCyclePeriod(String billCyclePeriod) {
        this.billCyclePeriod = billCyclePeriod;
    }

    public void setDays(DaysEnum days) {
        this.days = days;
    }

    public void setBillCycle(BillCycleEnum billCycle) {
        this.billCycle = billCycle;
    }

    public void setPrebillStartDate(String prebillStartDate) {
        this.prebillStartDate = prebillStartDate;
    }

    public void setPrebillEndDate(String prebillEndDate) {
        this.prebillEndDate = prebillEndDate;
    }

    public void setPrebillNoOfHrs(int prebillNoOfHrs) {
        this.prebillNoOfHrs = prebillNoOfHrs;
    }

    public void setPrebillAmount(BigDecimal prebillAmount) {
        this.prebillAmount = prebillAmount;
    }

    public void setInitialPrebillAmount(BigDecimal initialPrebillAmount) {
        this.initialPrebillAmount = initialPrebillAmount;
    }

    public void setPrebillKeepAccruing(PrebillKeepAccruingEnum prebillKeepAccruing) {
        this.prebillKeepAccruing = prebillKeepAccruing;
    }

    public void setHaveSalesTax(HaveSalesTaxEnum haveSalesTax) {
        this.haveSalesTax = haveSalesTax;
    }

    public void setDoNotUsePrebillOnceTimeframeExceedEnum(
            DoNotUsePrebillOnceTimeframeExceedEnum doNotUsePrebillOnceTimeframeExceedEnum) {
        this.doNotUsePrebillOnceTimeframeExceedEnum = doNotUsePrebillOnceTimeframeExceedEnum;
    }

    public void setPrebillWithoutContractors(
            PrebillWithoutContractorsEnum prebillWithoutContractors) {
        this.prebillWithoutContractors = prebillWithoutContractors;
    }

    public void setGenerateInvoice(GenerateInvoiceEnum generateInvoice) {
        this.generateInvoice = generateInvoice;
    }

    public void setPrebillNotes(String prebillNotes) {
        this.prebillNotes = prebillNotes;
    }

    public void setInvoiceTypeName(InvoiceTypeEnum invoiceTypeName) {
        this.invoiceTypeName = invoiceTypeName;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setBillingSpecialist(String billingSpecialist) {
        this.billingSpecialist = billingSpecialist;
    }

    public void setArSpecialist(String arSpecialist) {
        this.arSpecialist = arSpecialist;
    }

    public void setEffectiveFromDate(String effectiveFromDate) {
        this.effectiveFromDate = effectiveFromDate;
    }

    public void setEffectiveToDate(String effectiveToDate) {
        this.effectiveToDate = effectiveToDate;
    }

    public void setDelivery(DeliveryEnum delivery) {
        this.delivery = delivery;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public void setDefaultToSubject(DefaultToSubjectEnum defaultToSubject) {
        this.defaultToSubject = defaultToSubject;
    }

    public void setDefaultToBillProfile(DefaultToBillProfileEnum defaultToBillProfile) {
        this.defaultToBillProfile = defaultToBillProfile;
    }

    public void setIncludeTimesheet(IncludeTimesheetEnum includeTimesheet) {
        this.includeTimesheet = includeTimesheet;
    }

    public void setExcludeContractorName(ExcludeContractorName excludeContractorName) {
        this.excludeContractorName = excludeContractorName;
    }

    public void setAutoGenerateInvoice(AutoGenerateInvoiceEnum autoGenerateInvoice) {
        this.autoGenerateInvoice = autoGenerateInvoice;
    }

    public void setGenerateSeparateInvoice(GenerateSeparateInvoiceEnum generateSeparateInvoice) {
        this.generateSeparateInvoice = generateSeparateInvoice;
    }

    public void setMultipleContractor(MultipleContractorEnum multipleContractor) {
        this.multipleContractor = multipleContractor;
    }

    public void setShowHiringMgr(ShowHiringMgrEnum showHiringMgr) {
        this.showHiringMgr = showHiringMgr;
    }

    public void setSubProjectSingleItem(SubProjectSingleItemEnum subProjectSingleItem) {
        this.subProjectSingleItem = subProjectSingleItem;
    }

    public void setSubProjectDifferentItem(SubProjectDifferentItemEnum subProjectDifferentItem) {
        this.subProjectDifferentItem = subProjectDifferentItem;
    }

    public void setSubProjectDifferentInvoice(
            SubProjectDifferentInvoiceEnum subProjectDifferentInvoice) {
        this.subProjectDifferentInvoice = subProjectDifferentInvoice;
    }

    public void setIncludeExpenses(IncludeExpensesEnum includeExpenses) {
        this.includeExpenses = includeExpenses;
    }

    public void setIncludeExpensesDoc(IncludeExpensesDocEnum includeExpensesDoc) {
        this.includeExpensesDoc = includeExpensesDoc;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public void setBillCycleId(Long billCycleId) {
        this.billCycleId = billCycleId;
    }

    public void setBillCycleStartEndId(String billCycleStartEndId) {
        this.billCycleStartEndId = billCycleStartEndId;
    }

    public void setBillCycleDayId(Long billCycleDayId) {
        this.billCycleDayId = billCycleDayId;
    }

    public void setConsolidatedInvocie(Boolean consolidatedInvocie) {
        this.consolidatedInvocie = consolidatedInvocie;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setInvoiceCreatedDate(String invoiceCreatedDate) {
        this.invoiceCreatedDate = invoiceCreatedDate;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public void setPoDetailsList(List<PurchaseOrder> poDetailsList) {
        this.poDetailsList = poDetailsList;
    }

    public void setBillToManager(List<InvoicSetupAssignBill> billToManager) {
        this.billToManager = billToManager;
    }

    public void setBillToclientData(InvoiceSetupBillToClient billToclientData) {
        this.billToclientData = billToclientData;
    }

    public void setActivityLog(List<String> activityLog) {
        this.activityLog = activityLog;
    }

    public void setNotesList(List<InvoiceSetupNotes> notesList) {
        this.notesList = notesList;
    }

    public void setSaveStatus(String saveStatus) {
        this.saveStatus = saveStatus;
    }

    public void setSubProjectDifferentItemBoolean(Boolean subProjectDifferentItemBoolean) {
        this.subProjectDifferentItemBoolean = subProjectDifferentItemBoolean;
    }

    public void setDefaultToSubjectBoolean(Boolean defaultToSubjectBoolean) {
        this.defaultToSubjectBoolean = defaultToSubjectBoolean;
    }

    public void setIncludeTimesheetBoolean(Boolean includeTimesheetBoolean) {
        this.includeTimesheetBoolean = includeTimesheetBoolean;
    }

    public void setPrebillKeepAccruingBoolean(Boolean prebillKeepAccruingBoolean) {
        this.prebillKeepAccruingBoolean = prebillKeepAccruingBoolean;
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

    public void setBmTotalRecords(int bmTotalRecords) {
        this.bmTotalRecords = bmTotalRecords;
    }

    public void setInvoiseSetupIds(List<Long> invoiseSetupIds) {
        this.invoiseSetupIds = invoiseSetupIds;
    }

    public void setSubProjectDifferentInvoiceBoolean(Boolean subProjectDifferentInvoiceBoolean) {
        this.subProjectDifferentInvoiceBoolean = subProjectDifferentInvoiceBoolean;
    }

    public void setGenerateSeparateInvoiceBoolean(Boolean generateSeparateInvoiceBoolean) {
        this.generateSeparateInvoiceBoolean = generateSeparateInvoiceBoolean;
    }

    public void setIncludeExpensesBoolean(Boolean includeExpensesBoolean) {
        this.includeExpensesBoolean = includeExpensesBoolean;
    }

    public void setIncludeExpensesDocBoolean(Boolean includeExpensesDocBoolean) {
        this.includeExpensesDocBoolean = includeExpensesDocBoolean;
    }

    public void setShowHiringMgrBoolean(Boolean showHiringMgrBoolean) {
        this.showHiringMgrBoolean = showHiringMgrBoolean;
    }

    public void setMultipleContractorBoolean(Boolean multipleContractorBoolean) {
        this.multipleContractorBoolean = multipleContractorBoolean;
    }

    public void setAutoGenerateInvoiceBoolean(Boolean autoGenerateInvoiceBoolean) {
        this.autoGenerateInvoiceBoolean = autoGenerateInvoiceBoolean;
    }

    public void setExcludeContractorNameBoolean(Boolean excludeContractorNameBoolean) {
        this.excludeContractorNameBoolean = excludeContractorNameBoolean;
    }

    public void setDefaultToBillProfileBoolean(Boolean defaultToBillProfileBoolean) {
        this.defaultToBillProfileBoolean = defaultToBillProfileBoolean;
    }

    public void setGenerateInvoiceBoolean(Boolean generateInvoiceBoolean) {
        this.generateInvoiceBoolean = generateInvoiceBoolean;
    }

    public void setPrebillWithoutContractorsBoolean(Boolean prebillWithoutContractorsBoolean) {
        this.prebillWithoutContractorsBoolean = prebillWithoutContractorsBoolean;
    }

    public void setDoNotUsePrebillOnceExceedBoolean(Boolean doNotUsePrebillOnceExceedBoolean) {
        this.doNotUsePrebillOnceExceedBoolean = doNotUsePrebillOnceExceedBoolean;
    }

    public void setHaveSalesTaxBoolean(Boolean haveSalesTaxBoolean) {
        this.haveSalesTaxBoolean = haveSalesTaxBoolean;
    }

    public void setConsolidateInvoiceBoolean(Boolean consolidateInvoiceBoolean) {
        this.consolidateInvoiceBoolean = consolidateInvoiceBoolean;
    }

    public void setSubProjectSingleItemBoolean(Boolean subProjectSingleItemBoolean) {
        this.subProjectSingleItemBoolean = subProjectSingleItemBoolean;
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
