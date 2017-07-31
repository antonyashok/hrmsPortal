
package com.tm.util;

public class InvoiceConstants {

	public static final String TM_COMMON_TRANSACTION_MANAGER = "tmTransactionManager";
	public static final String TM_IV_TRANSACTION_MANAGER = "tmIvTransactionManager";
	public static final String TM_ENGAGEMENT_TRANSACTION_MANAGER = "tmEngagementTransactionManager";
	public static final String TM_TIMESHEET_TRANSACTION_MANAGER = "tmTsTransactionManager";

	public static final String OK = "ok";
	public static final String ERROR = "error";
	public static final String ASC = "asc";
	public static final String DESC = "desc";
	public static final String ERROR_MESSAGE = "errorMessage";
	public static final String SUCCESS_MESSAGE = "successMessage";
	public static final String SERVICERESPONSE = "serviceResponse";
	public static final String STATUS = "status";
	public static final String NO_RECORDS_FOUND = "No records found";

	public static final String TRUE = "true";
	public static final String FALSE = "false";

	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";
	public static final String IS_ACTIVE = "isActive";
	public static final String TOTAL_RECORDS = "totalRecords";

	/* Report Constants */
	public static final String PDF = "pdf";
	public static final String XLS = "xls";
	public static final String XLSX = "xlsx";
	public static final String CSV = "csv";
	public static final String FILENAME = "fileName";
	public static final String ACTUAL_FILENAME = "actualFileName";
	public static final String ATTACHMENT = "attachment";
	public static final String CACHE_CONTROL = "must-revalidate, post-check=0, pre-check=0";
	public static final String APPLICATION_PDF = "application/pdf";
	public static final String APPLICATION_CSV = "text/csv";
	public static final String APPLICATION_EXCEL = "application/vnd.ms-excel";
	public static final String APPLICATION_SPREAD_SHEET = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	/* Status Constants */
	public static final String APPROVED = "Approved";
	public static final String ACTIVE = "Active";
	public static final String PENDING_APPROVAL = "PendingApproval";
	public static final String INACTIVE = "Inactive";
	public static final String DRAFT = "Draft";
	public static final String REJECT = "Reject";
	public static final String APPROVE = "Approve";
	public static final String SUBMITTED = "Submitted";
	public static final String RETURNED = "Returned";

	/* Invoice Report */
	public static final String INVOICE_REPORT_ALREADY_EXISTS = "Invoice Report Name already Exists";
	public static final String ALL_REPORT = "Invoice_Report";
	public static final String REGEX = "^[&%$##@!~_*()]";
	public static final String NO_CONTRACTOR_WAS_FOUND = "No Contractor is Found ";

	public static final String PO_ID = "poId";
	public static final String PURCHASE_ORDER_ID = "purchaseOrderId";
	public static final String PONUMBER = "poNumber";
	public static final String PURCHASE_ORDER_ATTACHMENT_ID = "poAttachmentId";
	public static final String FILE_NAME = "fileName";
	public static final String INVOICE_ATTACHMENT_MONGO_COLLECTION = "invoiceAttachmentsCollection";
	public static final String INVOICE_POATTACHMENT_MONGO_COLLECTION = "invoicePOAttachmentsCollection";
	public static final String INVOICE_ID = "invoiceId";
	public static final String FILE_UPLOAD_ERROR_MESSAGE = "File uploading Error Occur:File size may > 2MB or Number Files May > 5";
	public static final String NO_SCREEN_ATTACHED = "No Screen Attached";
	public static final String ALREADY_UPLOAD = "Already Uploaded";

	public static final String LAST_UPDATE_DATE = "auditDetails.lastUpdateDate";
	public static final String CREATED_BY = "auditDetails.createdBy";

	public static final String PROFILE_IMAGE_MONGO_COLLECTION = "profileImage";
	public static final String TEMP_FILE1_NAME = "default-profile.jpg";
	public static final String TEMP_FILE2_NAME = "default-profile.jpg";
	public static final String TEMP_FILE_DIR_NAME = "/images/";

	public static final String PROFILE_IMG_3 = "profileImage-3.jpg";
	public static final String UPLOAD_FILE_LIMIT_EXCEPTION = "Error:File Uploading Limit---1";
	public static final String SPECIAL_CHARECTER_ERROR = "Special charecter not allowed";

	public static final String TASK_IS_ALREADY_PRESENT_FOR_THIS_CONTRACTOR = "Task is already Exists";
	public static final String PENDING_BILLING_QUEUE_ATTACHMENT_ID = "attachmentId";
	public static final String INVOICE_PENDING_BILLING_QUEUEATTACHMENT_MONGO_COLLECTION = "pendingBillingQueueAttachments";
	public static final String CONTRACTOR_SAVED_SUCCESSFULLY = "Contractor Saved Successfully";

	public static final String CONTRACT_ID = "contractId";
	public static final String EMPLOYEE_ID = "employeeId";
	public static final String CONTRACTOR_NAME = "contractorName";
	public static final String BILL_TO_CLIENT = "billToClient";
	public static final String BILLTOCLIENTNAME = "billToClientName";
	public static final String END_CLIENT = "endClient";
	public static final String PROJECT = "projectName";
	public static final String RATE_TYPE = "rateType";
	public static final String OFFICE = "office";
	public static final String DIVISION = "division";
	public static final String ACCOUNT_MANAGER = "accountManager";
	public static final String RECRUITER = "recruiter";
	public static final String BILLING_SPECILIST = "billingSpecialist";
	public static final String START_DATE = "startDate";
	public static final String CONTRACTOR_START_DATE = "contractorStartDate";
	public static final String COSTCENTER = "costCenter";
	public static final String BILLING_QUEUE = "billingQueue";
	public static final String BILLING_SPECIALIST_ID = "billingSpecialistId";
	public static final String PENDING = "pending";
	public static final String BILLING_QUEUE_ID = "billingQueueId";
	public static final String CONTRACTOR_ID = "contractorId";
	public static final String PROJECT_ID = "projectId";
	public static final String NET_TERMS = "netTerms";
	public static final String BILL_CYCLE = "billCycle";
	public static final String DELIVERY = "delivery";
	public static final String INVOICE_EMAIL_SUBJECT = "invoiceEmailSubject";
	public static final String INVOICE_EMAIL = "invoiceEmail";
	public static final String BILL_TO_MANAGERS_LIST = "billToManagersList";
	public static final String PO_LIST = "poList";
	public static final String INVOICE_DETAILS_LIST = "invoiceDetailsList";

	public static final String INVOICESETUP_ID = "invoiceSetupId";
	public static final String INVOICE_SET_UP_ID = "Invoice set up id";
	public static final String INVOICE_SET_BILL_DETAILS = "Invoice set bill details";
	public static final String INVOICE_BILL_CYCLE_DETAILS = "Invoice bill cycle details";
	public static final String PURCHASE_ORDER_DETAILS = "purchaseorderDetails";

	public static final String MANAGER_NAME = "Manager name";
	public static final String NOT_EXIST = "not exist";

	public static final String INVOICE_SETUP_LIST = "invoiceSetupList";
	public static final String NO_UUID_WITH = "No UUID with";
	public static final String FOUND = "found.";
	public static final String DELETED_SUCCESSFULLY = "deleted successfully.";
	public static final String FILE_UPLOAD_ERROR = "File upload error";
	public static final String NO_DATA_WITH_ID = "No data with id ";
	public static final String UPLOAD_MULTIPLE_PO_FILE_EXCEPTION = "uploadMultiplePOFile Exception";
	public static final String GET_UPLOAD_SCREEN_EXCEPTION = "getUploadScreen Exception";
	public static final String PO_NUMBER_WITH = "PO Number with : ";
	public static final String BILLING_LIST = "billingList";

	public static final String NEW_SUB_TASK_DETAILS = "newSubTaskDetails";
	public static final String PENDING_BILLING_QUEUE_FILES_DETAILS = "pendingBillingQueueFiles Details";
	public static final String CONTRACTER_DETAIL = "contractorDetail";
	public static final String PURCHASE_ORDER_DETAIL = "purchaseorderDetails";
	public static final String INVOICE_SETUP_NAME = "Invoice set up name";
	public static final String UUID_WITH = "UUID with";

	public static final String PO_NO_DATA_FOUND = "PO Number not exist";
	public static final String INVOICE_SAVED_SUCCESSFULLY = "Invoice set up saved successfully";
	public static final String TASK_LIMIT_IS_EXCEED = "Task Limit is Exceed(>10)";
	public static final String CONTRACTOR_BILL_DETAILS = "contractorBillDetails";
	public static final String EMP_ID = "empID";

	public static final String EFFECTIVE_DATE_IS_DUPLICATE_FOR_THIS_TASK = "Effective Date Is Duplicate For This Project";

	public static final String ENDCLIENT_REQUIRED_ERROR_MESSAGE = "End client required";
	public static final String ENDCLIENT_BLANK_ERROR_MESSAGE = "End client can not be empty";
	public static final String APCONTACT_REQUIRED_ERROR_MESSAGE = "Ap contract required";
	public static final String APCONTACT_BLANK_ERROR_MESSAGE = "Ap contract can not be empty";
	public static final String HIRINGMANAGER_REQUIRED_ERROR_MESSAGE = "Hiring manager required";
	public static final String HIRINGMANAGER_BLANK_ERROR_MESSAGE = "Hiring manager can not be empty";
	public static final String TIMESHEETTYPE_REQUIRED_ERROR_MESSAGE = "Time sheet type required";
	public static final String TIMESHEETTYPE_BLANK_ERROR_MESSAGE = "Time sheet type can not be empty";

	public static final String NO_CONFIGURATION_WAS_SET = "No configuration was set for this invoice";
	public static final String INVOICE_SETUP_EDIT_FETCH_DETAILS = "invoiceSetupEditFetchDetails";

	public static final String INVOICE_SETUP_DETAILS = "invoiceSetupDetails";
	public static final String BILL_TO_CLIENT_LIST = "billToClientList";
	public static final String APPROVED_QUEUE = "ApprovedQueue";
	public static final String BILLING_PROFILE = "billingProfile";
	public static final String BILLING_SPECIALIST = "billingSpecialist";
	public static final String UPDATION_FAILED_BECAUSE_SOME_DATA_ARE_NOT_STORED = "Updation has been failed to edit InvoiceSetup because some data are Not Properly saved";
	public static final String BILL_TO_MANAGER_LIST = "BillTOManagerList";

	public static final String BILLINGPROFILE_SAVED_SUCCESSFULLY = "Billing Profile Saved Successfully";
	public static final String DP_SAVED_SUCCESSFULLY = "Direct Placement Saved Successfully";
	public static final String INVOICE_OVERVIEW_VALIDATE = "Invoice Overview Valid";
	public static final String INVOICE_OVERVIEW_INVALIDATE = "Invoice Overview In-Valid";
	public static final String INVOICE_OVERVIEW_ERROR = "Invoice Overview Error";

	/* Invoice Type */
	public static final String REGULAR = "Regular";
	public static final String IRREGULAR = "Irregular";
	public static final String MILESTONEACCURE = "MilestoneAccrue";
	public static final String MILESTONEFIXED = "MilestoneFixed";
	public static final String PREBILL = "Prebill";

	public static final String EMPLOYEENAME = "employeeName";
	public static final String PO_NUMBER = "poNumber";
	public static final String FILE_NUMBER = "fileNumber";
	public static final String EMPLOYEE_NAME = "employee_Name";
	public static final String WORKING_DATE = "workingdate";
	public static final String HOURS_WORKED = "hoursWorked";
	public static final String NOTES = "notes";
	public static final String BILLTOMANAGER = "billToManager";
	public static final String INVOICESETUP_NAME = "invoiceSetupName";
	public static final String PODETAILS = "poDetails";
	public static final String NOTESID = "notesId";
	public static final String CONTRACTORBILLDETAILS = "ContractorBillDetails";

	public static final String NO_CONTRACTORS_FOUND = "No Contractors Found";
	public static final String CONTRACTOR_QUEUE_LIST = "contractorsQueueList";

	/* Invoice Setup Button Status */
	public static final String INVOICE_SETUP_SAVED = "Invoice Setup has been saved successfully";
	public static final String INVOICE_SETUP_UPDATED = "Invoice Setup has been updated successfully";
	public static final String INVOICE_SETUP_SUBMITTED = "Invoice Setup has been submitted successfully";
	public static final String INVOICE_SETUP_APPROVED = "Invoice Setup has been approved successfully";
	public static final String INVOICE_SETUP_BULK_APPROVED = "Invoice Setup(s) has been approved successfully";
	public static final String INVOICE_SETUP_REJECTED = "Invoice Setup has been rejected successfully";
	public static final String INVOICE_SETUP_BULK_REJECTED = "Invoice Setup(s) has been rejected successfully";
	public static final String INVOICE_SETUP_DEACTIVATED = "Invoice Setup has been deactivated sucessfully";
	public static final String INVOICE_SETUP_ACTIVATED = "Invoice Setup has been activated sucessfully";
	public static final String INVOICE_SETUP_PENDINGAPPROVAL = "Invoice Setup has been moved to Pending Approval";

	public static final String INVOICEBILLTOMANAGERADDRESS_ALREADYEXISTS = "Bill to manager address already exists";
	public static final String ASSIGNBILLTOMANAGER_ADDEDSUCCESSFULLY = "Assign bill to manager added successfully";
	public static final String ASSIGNBILLTOMANAGER_UPDATEDSUCCESSFULLY = "Assign bill to manager updated successfully";
	public static final String ASSIGNBILLTOMANAGER_DELETEDSUCCESSFULLY = "Assign bill to manager deleted successfully";

	public static final String NO_PO_RELATION_IS_FOUND = "No PO Relation is Found For This Contractor";
	public static final String NO_INVOICE_SET_UP_FOUND = "No Invoice Setup is Found For This Contractor";

	public static final String CONTRACTOR_DELETE_SUCCESS_MESSAGE = "The selected Contractor Successfully removed";
	public static final String CONTRACTOR_DELETE_FAILURE_MESSAGE = "Sorry couldn't Remove the selected Contractor";
	public static final String CLONE_INVOICE_SET_UP_FAILED_MESSAGE = "The Selected InvoiceSetup clone Process has been Failed";
	public static final String CONTRACTOR_REMOVED = "Contractor Removed.";
	public static final String BILLTOCLIENTDETAILS = "BillToClientDetails";
	//public static final String NO_BILL_TO_CLIENT_INFORMATION_FOUND_FOR_THIS_ENGAGEMENT = "No BillToClient Information Found For this Engagement";
	public static final String NO_BILL_TO_CLIENT_INFORMATION_FOUND_FOR_THIS_ENGAGEMENT = "No BillToClient Information Found For this Project";
	public static final String NO_BILL_TO_CLIENT_INFORMATION_FOUND = "No BillToClient Information Found";

	/* Deactivate Invoice Setup */
	public static final String ACTIVE_CONTRACORS_LINKED = "Active Contractor(s) are associated.";
	public static final String ACTIVE_PO_LINKED = "Active PO(s) are linked.";
	public static final String INV_RETURN_REQUESTED = "Invoice Return(s) have been requested.";

	public static final String MILESTONEAMOUNT_REQUIRED_ERROR_MESSAGE = "mileStone Amount required";
	public static final String MILESTONEAMOUNT_BLANK_ERROR_MESSAGE = "mileStone Amount can not be empty";
	public static final String MILESTONEFIXEDAMOUNT_REQUIRED_ERROR_MESSAGE = "mileStone fixed Amount required";
	public static final String MILESTONEFIXEDAMOUNT_BLANK_ERROR_MESSAGE = "mileStone fixed Amount  can not be empty";
	public static final String MILESTONEFIXED_DATE_REQUIRED_ERROR_MESSAGE = "mileStone fixed date required";
	public static final String MILESTONEFIXED_DATE_BLANK_ERROR_MESSAGE = "mileStone fixed date can not be empty";
	public static final String DESCRIPTION_REQUIRED_ERROR_MESSAGE = "description required";
	public static final String DESCRIPTION_BLANK_ERROR_MESSAGE = "description can not be empty";

	/* Purchase Order */
	public static final String PO_NUMBER_REQUIRED = "poNumber Required";
	public static final String PO_NUMBER_BLANK_ERROR_MESSAGE = "poNumber can not be empty";
	public static final String PO_NUMBER_MAX_MIN_LIMIT = "poNumber cannot have more than 45 characters";
	public static final String EMPLOYEEID_REQUIRED = "employeeId Required";
	public static final String EMPLOYEEID_BLANK_ERROR_MESSAGE = "employeeId can not be empty";
	public static final String POSTARTDATE_REQUIRED = "poStartDate Required";
	public static final String POSTARTDATE_BLANK_ERROR_MESSAGE = "poStartDate can not be empty";
	public static final String POENDDATE_REQUIRED = "poEndDate Required";
	public static final String POENDDATE_BLANK_ERROR_MESSAGE = "poEndDate can not be empty";
	public static final String PODESCRIPTION_REQUIRED = "poDescription Required";
	public static final String PODESCRIPTION_BLANK_ERROR_MESSAGE = "poDescription can not be empty";
	public static final String REVENUESTARTDATE_REQUIRED = "revenueStartDate Required";
	public static final String REVENUESTARTDATE_BLANK_ERROR_MESSAGE = "revenueStartDate can not be empty";
	public static final String REVENUEENDDATE_REQUIRED = "revenueEndDate Required";
	public static final String REVENUEENDDATE_BLANK_ERROR_MESSAGE = "revenueEndDate can not be empty";
	public static final String NOTES_REQUIRED = "notes Required";
	public static final String NOTES_BLANK_ERROR_MESSAGE = "notes can not be empty";
	public static final String FILENAME_REQUIRED = "fileName Required";
	public static final String FILENAME_BLANK_ERROR_MESSAGE = "fileName can not be empty";
	public static final String FILENAME_MAX_MIN_LIMIT = "fileName cannot have more than 200 characters";
	public static final String REVENUEAMOUNT_REQUIRED = "revenueAmount Required";
	public static final String REVENUEAMOUNT_BLANK_ERROR_MESSAGE = "revenueAmount can not be empty";

	/* Invoice Rate */
	public static final String INVOICEID_REQUIRED = "invoiceId Required";
	public static final String INVOICEID_BLANK_ERROR_MESSAGE = "invoiceId can not be empty";
	public static final String TASKNAME_REQUIRED = "taskName Required";
	public static final String TASKNAME_BLANK_ERROR_MESSAGE = "taskName can not be empty";
	public static final String TASKNAME_MAX_MIN_LIMIT = "taskName cannot have more than 50 characters";
	public static final String EFFECTIVEDATE_REQUIRED = "effectiveDate Required";
	public static final String EFFECTIVEDATE_BLANK_ERROR_MESSAGE = "effectiveDate can not be empty";
	public static final String BILLRATEST_REQUIRED = "billRateST Required";
	public static final String BILLRATEST_BLANK_ERROR_MESSAGE = "billRateST can not be empty";
	public static final String BILLRATEOT_REQUIRED = "billRateOT Required";
	public static final String BILLRATEOT_BLANK_ERROR_MESSAGE = "billRateOT can not be empty";
	public static final String BILLRATEDT_REQUIRED = "billRateDT Required";
	public static final String BILLRATEDT_BLANK_ERROR_MESSAGE = "billRateDT can not be empty";
	public static final String ENDDATE_REQUIRED = "enddate Required";
	public static final String ENDDATE_BLANK_ERROR_MESSAGE = "enddate can not be empty";
	public static final String ENDRATEST_REQUIRED = "endRateST Required";
	public static final String ENDRATEST_BLANK_ERROR_MESSAGE = "endRateST can not be empty";
	public static final String ENDRATEOT_REQUIRED = "endRateOT Required";
	public static final String ENDRATEOT_BLANK_ERROR_MESSAGE = "endRateOT can not be empty";
	public static final String ENDRATEDT_REQUIRED = "endRateDT Required";
	public static final String ENDRATEDT_BLANK_ERROR_MESSAGE = "endRateDT can not be empty";

	/* Invoice Setup Assign To Bill */
	public static final String INVOICEMANAGERNAME_REQUIRED = "invoiceSetupManagerName Required";
	public static final String INVOICEMANAGERNAME_BLANK_ERROR_MESSAGE = "invoiceSetupManagerName can not be empty";
	public static final String INVOICEMANAGERADDR_REQUIRED = "invoiceSetupManagerAddress Required";
	public static final String INVOICEMANAGERADDR_BLANK_ERROR_MESSAGE = "invoiceSetupManagerAddress can not be empty";

	/* Invoice Setup */
	public static final String BILLTOCLIENT_REQUIRED = "billToClient Required";
	public static final String BILLTOCLIENT_BLANK_ERROR_MESSAGE = "billToClient can not be empty";
	public static final String ENDTOCLIENT_REQUIRED = "endToClient Required";
	public static final String ENDTOCLIENT_BLANK_ERROR_MESSAGE = "endToClient can not be empty";
	public static final String CONTRACTORID_REQUIRED = "contractorId Required";
	public static final String CONTRACTORID_BLANK_ERROR_MESSAGE = "contractorId can not be empty";
	public static final String TERMS_REQUIRED = "terms Required";
	public static final String DEPTNUMBER_REQUIRED = "deptNumber Required";
	public static final String DEPTNUMBER_BLANK_ERROR_MESSAGE = "deptNumber can not be empty";
	public static final String FEIN_REQUIRED = "FEIN Required";
	public static final String FEIN_BLANK_ERROR_MESSAGE = "deptNumber can not be empty";
	public static final String INVOICESETUPNAME_REQUIRED = "invoiceSetupName Required";
	public static final String INVOICESETUPNAME_BLANK_ERROR_MESSAGE = "invoiceSetupName can not be empty";
	public static final String EMAILSUBJECT_REQUIRED = "emailSubject Required";
	public static final String EMAILSUBJECT_BLANK_ERROR_MESSAGE = "emailSubject can not be empty";
	public static final String EMAIL_REQUIRED = "email Required";
	public static final String EMAIL_BLANK_ERROR_MESSAGE = "email can not be empty";
	public static final String ROLL_OVER_ENUM = "rollOverEnum";
	public static final String PO_PARENT_ID = "poParentId";
	public static final String INVSETUP_SOURCE = "invSetupSource";
	public static final String ENGAGEMENTNAME = "engagementName";
	public static final String ENGAGEMENTID = "engagementId";

	public static final String INVOICEBILLTOMANAGERASSIGNONEMANAGER = "Please assign one manager";
	public static final String END_CLIENT_DETAILS = "EndClientDetails";
	public static final String NO_END_CLIENT_INFORMATION_FOUND = "No End Client Information is Found";
	public static final String BILLING_SPECIALIST_DETAILS = "BillingSpecialistDetails";

	public static final String CLONED_INVOICE_SETUP_DETAILS = "ClonedInvoiceSetupDetails";
	public static final String INVOICE_SETUP_FILES_DETAILS = "InvoiceSetupFileDetails";
	public static final String INVOICING_STORE = "invoicingStore";
	public static final String INVOICE_SETUP_ATTACHMENT_ID = "invoiceSetupAttachmentId";

	public static final String CLONED_SETUP_DELETED_SUCCESSFULLY = "Cloned Setup Deleted Successfully";
	public static final int MAX_FILE_SIZE = 5;
	public static final String Y_STR = "Y";
	public static final String N_STR = "N";
	public static final String ON = "on";
	public static final String BY = "by";
	public static final int DEFAULT_TOTAL_RECORDS = 25;
	public static final int DEFAULT_START_INDEX = 0;
	public static final String DEFAULT_EMPLOYEE_NAME = "christiano ronaldo";
	public static final String INVOICE_TYPE_NAME = "invoiceTypeName";
	public static final String CLONED_SETUP = "ClonedSetup";
	public static final int MIN_FILE_LENGTH = 1;
	public static final String BILL_TO_CLIENT_ID = "billToClientId";
	public static final String ACTION_CLONED = "cloned";
	public static final String PO_STATUS = "poStatus";
	public static final int MAX_FILE_LENGTH = 5;

	public static final String CREATED_DATE_STR = "createdDate";
	public static final String DATE_FORMAT_OF_MMDDYYY = "MM/dd/yyyy";
	public static final String LAST_UPDATED_ON = "lastUpdatedOn";

	public static final String CUSTOMER_ID = "customerId";
	public static final String END_DATE = "endDate";
	public static final String PO_BALANCE = "poBalance";
	public static final String CUSTOMER_NAME = "customerName";
	public static final String PO_ACTIVE_FLAG = "poActiveFlag";

	public static final String PO_DATA_NOT_FOUND = "No Pos are Available";

	public static final String NO_GLOBALINVOICE_SET_UP_FOUND = "Global Invoice Setup is Empty";

	public static final String GLOBALINVOICE_SET_UP_ID_EMPTY = "Global Invoice Setup Id is Empty";

	public static final String GLOBALINVOICE_SET_UP_NAME_EMPTY = "Global Invoice Setup name should not be empty";

	public static final String GLOBALINVOICE_SET_UP_NAME_ALREADY_EXIST = "Global Invoice Setup name already exists";

	public static final String GLOBALINVOICE_SET_UP_NAME_EXIST = "Global Invoice Setup name already exists";

	public static final String GLOBALINVOICE_SET_UP_TERMS_EMPTY = "Global Invoice Setup terms is Empty";

	public static final String GLOBALINVOICE_SET_UP_TYPE_EMPTY = "Global Invoice Setup type is Empty";

	public static final String GLOBALINVOICE_SET_UP_BILLING_EMPTY = "Select Billing Specialist";

	public static final String GLOBALINVOICE_SET_UP_TEMPLATE_EMPTY = "Select Invoice Template";

	public static final String GLOBALINVOICE_SET_UP_DELIVERY_EMPTY = "Select Delivery Template";

	public static final String GLOBALINVOICE_SET_UP_NOTES_EMPTY = "Global Invoice Setup notes should not be empty";

	public static final String ERR_FILE_UPLOAD_COUNT = "Only 5 or less than 5 files are allowed to upload";
	public static final String SRC_REF_ID = "sourceReferenceId";
	public static final String INVOICE_ATTACHMENTS = "invoiceAttachments";
	public static final String INVOICE_ATTACHMENT_ID = "invoiceAttachmentId";
	public static final String INVOICE = "invoice";
	public static final String SRC_REF_NAME = "sourceReferenceName";
	public static final String PO = "PO";

	public static final String CONTRCTOR_NAME = "billingProfile";
	public static final String BILL_TO_CLIENT_NAME = "billToClientName";
	public static final String END_CLIENT_NAME = "endClientName";
	public static final String END_CLIENT_ID = "endClientId";
	public static final String OFFICE_ID = "officeId";
	public static final String OFFICE_NAME = "office";
	public static final String PROJECT_NAME = "projectName";
	public static final String ACCOUNT_MANAGER_ID = "accountManagerId";
	public static final String ACCOUNT_MANAGER_NAME = "accountManagerName";
	public static final String BILLING_SPECIALIST_NAME = "billingSpecialistName";
	public static final String PROFILE_ACTIVE_DATE = "profileActiveDate";
	public static final String PROFILE_END_DATE = "profileEndDate";

	public static final String PENDING_BILLING_QUEUE_ID = "id";
	public static final String SAVE = "save";

	public static final String CREATED_ON_STR = "created.on";
	public static final String UPDATED_ON_STR = "updated.on";
	public static final String ID_STR = "id";
	public static final String INVOICE_NUMBER_STR = "invoiceNumber";
	public static final String INVOICE_TYPE_STR = "invoiceType";
	public static final String TIMESHEET_TYPE_STR = "timesheetType";
	public static final String LOCATION_STR = "location";
	public static final String COUNTRY_STR = "country";
	public static final String CURRENCY_TYPE_STR = "currencyType";
	public static final String AMOUNT_STR = "amount";
	public static final String TIMESHEET_ATTACHMENT_STR = "timesheetAttachment";
	public static final String EXPENSES_ATTACHMENT_STR = "billableExpensesAttachment";
	public static final String INVOICE_QUEUE_ID_STR = "invoiceQueueId";

	public static final String STATUS_PENDING_APPROVAL_STR = "Pending Approval";
	public static final String STATUS_DELIVERED_STR = "Delivered";
	public static final String STATUS_PENDING_RETURN_STR = "Pending Return";
	public static final String STATUS_DISCARDED_STR = "Discarded";
	public static final String STATUS_NOT_GENERATED_STR = "Not Generated";
	public static final String STATUS_GENERATED_STR = "Generated";
	public static final String STATUS_INSUFFICIENT_BALANCE_STR = "Insuffecient Balance";
	public static final String LOW_PO_AMOUNT = "Low Purchase order Amount";

	public static final String SUB_TYPE_STR = "subType";
	public static final String PDF_CONTENT_TYPE_STR = "application/pdf";
	public static final String INVOICE_STR = "Invoice";
	public static final String EXPENSE_STR = "Expense";
	public static final String TIMESHEET_STR = "Timesheet";

	public static final String JPG_TYPE_STR = "image/jpeg";

	public static final String BILL_TO_ORGANIZATION_ID = "billToOrganizationId";

	public static final String EXCEPTION_SOURCE = "exceptionSource";
	public static final String INVOICE_RETURN = "Invoice Return";
	public static final String TIMESHEET_NOT_APPROVAL = "Timesheet Not Approval";
	public static final String INVOICE_SETUP_NOT_APPROVAL = "Invoice Setup Not Approval";
	public static final String INVOICE_QUEUE_REJECTED= "Invoice Queue Rejected";
	public static final String INVOICE_SETUP_NAME_FIELD = "invoiceSetupName";
	public static final String ATTENTION_MANAGER_NAME = "attentionManagerName";
	public static final String EXCEPTION_TYPE = "exceptionType";
	public static final String EXCEPTION_DETAIL_CONTRACTOR_NAME = "invoiceExceptionDetail.contractorName";
	public static final String EXCEPTION_DETAIL_WEEK_END_DATE = "invoiceExceptionDetail.weekEndDate";
	public static final String EXCEPTION_DETAIL_AMOUNT = "invoiceExceptionDetail.amount";
	public static final String EXCEPTION_DETAIL_STATUS = "invoiceExceptionDetail.status";
	public static final String EXCEPTION_DETAIL_RETURN_COMMENTS = "invoiceExceptionDetail.returnComments";
	public static final String EXCEPTION_DETAIL_RETURN_APPROVAL_COMMENTS = "invoiceExceptionDetail.returnApprovalComments";
	public static final String EXCEPTION_DETAIL_FILE_NUMBER = "invoiceExceptionDetail.fileNumber";
	public static final String EXCEPTION_DETAIL_TOTAL_HOURS = "invoiceExceptionDetail.totalHours";
	public static final String EXCEPTION_DETAIL_CURRENCY_TYPE = "invoiceExceptionDetail.currencyType";
    public static final String EFFECTIVE_DATE = "effectiveDate";
    public static final String EFFECTIVE_START_DATE = "effectiveStartDate";
    
    public static final String ERR_SEARCH_PARAM = "Search param is Required";
    public static final String ERR_OPERATOR_VALUE = "Operator value is Required";
    
    public static final String DIRECT_PLACEMENT_STR = "Direct Placement";
    
    public static final String ROWVALUESPLITTER = "@@ROW-VALUE@@";
    
    public static final String TIMESHEET = "timesheet";
    public static final String EXPENSE = "expense";
    
    public static final String STATUS_TIMESHEET_NOT_APPROVAL_STR = "Timesheet Not Approval";
    public static final String STATUS_TIMESHEET_NOT_APPROVED= "Timesheet Not Approved";
    
   
    private InvoiceConstants() {

    }
}
