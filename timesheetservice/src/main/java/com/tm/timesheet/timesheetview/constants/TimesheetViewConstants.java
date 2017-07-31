package com.tm.timesheet.timesheetview.constants;

public class TimesheetViewConstants {

    public static final String EMPLOYEE = "employee";
    public static final String APPROVER = "approver";
    public static final String APPROVER_TYPE = "Approver";
    public static final String SUBMITTER_TYPE = "Submitter";
    public static final String SUBMITTER = "submitter";
    public static final String CONTRACTOR = "contractor";

    public static final String EMPLOYEE_ID = "employee.id";
    public static final String ENGAGEMENT_NAME = "engagement.name";
    public static final String ENGAGEMENT_ID = "engagement.id";
    public static final String SALESMANAGER_ID = "employee.salesManagerId";
    public static final String REPORTING_MANAGER_ID = "employee.reportingManagerId";
    public static final String ACCOUNT_MANAGER_ID = "engagement.accountManagerId";
    public static final String EMPLOYEE_LOCATION_ID = "employee.locationId";

    public static final String TIMESHEET_PROXY_TYPE = "Proxy";
    public static final String TIMESHEET_VERIFICATION_TYPE = "Verification";
    public static final String TIMESHEET_RECRUTIER_TYPE = "Recruiter";
    public static final String COMPLETED = "Completed";
    public static final String DISPUTE = "Dispute";
    public static final String VERIFIED = "Verified";
    public static final String NOT_VERIFIED = "Not Verified";
    public static final String NOT_SUBMITTED = "Not Submitted";
    public static final String APPROVED = "Approved";
    public static final String AWAITING_APPROVAL = "Awaiting Approval";
    public static final String REJECTED = "Rejected";
    public static final String OVERDUE = "Overdue";
    public static final String STATUS = "status";
    public static final String OVER_ALL_COUNT = "allCount";
    public static final String NO_RECORDS_FOUND = "No records found";
    
    public static final String TYPE_EMPLOYEE = "E";
    public static final String TYPE_CONTRACTOR = "C";
    public static final String TYPE_RECURTER = "R";

    public static final String STATUS_ALL = "All";
    public static final String DATE_FORMAT_OF_MMDDYYY = "MM/dd/yyyy";
    public static final String DATE_FORMAT_OF_MMDDYY = "MM/dd/yy";
    public static final String COUNT = "count";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String VIEW_TIMESHEETS = "viewTimesheets";

    public static final String EMPLOYEE_TYPE = "employeeType";
    public static final String EMPLOYEEID = "employeeId";
    public static final String ENGAGEMENT_DTO_ID = "engagementId";
    public static final String TIMESHEETID = "timesheetId";

    public static final String DATE_FORMAT = "MMM dd, yyyy";
    public static final String DATE_TIME_FORMAT = "MMM dd, yyyy' - 'HH:mm:ss a";

    public static final String ON = "on";
    public static final String UPDATED_ON = "updated.on";
    public static final String LEAVE_HOURS = "leaveHours";
    public static final String WORK_HOURS = "workHours";
    public static final String TOTAL_HOURS = "totalHours";
    public static final String PTO_HOURS = "ptoHours";

    public static final String EMPLOYEE_DOT_TYPE = "employee.type";
    public static final String LOOK_UP_TYPE = "lookupType.value";
    public static final String LOOK_UP_ID = "lookUpId";
    public static final String CLIENT_MANAGER_NAME = "engagement.clientManagerName";
    public static final String RECRUITER_NAME = "engagement.recruiterName";
    public static final String HIRING_MANAGER_NAME = "engagement.hiringManagerName";
    public static final String REPORTING_MANAGER_NAME = "employee.reportingManagerName";
    public static final String SUBMITTED_NAME = "submitted.name";

    public static final String ID = "id";

    public static final Boolean HOLIDAY_STATUS_TRUE = true;
    public static final Boolean HOLIDAY_STATUS_FALSE = false;
    public static final Boolean JOINING_STATUS_TRUE = true;
    public static final Boolean JOINING_STATUS_FALSE = false;
    public static final String EMPLOYEE_TASK_NAME = " ";
    public static final Integer COUNT_START = 0;
    public static final Integer WEEK_START_RANGE = 1;
    public static final Integer WEEK_END_RANGE = 7;
    public static final Integer TIME_START_RANGE = 1;
    public static final Integer TIME_END_RANGE = 2;
    public static final String DEFAULT_HOUR = "0.00";
    public static final String DEFAULT_ZERO = "0";
    public static final String TIMESHEET_SOURCE = "Timetrack";
    public static final String TIMESHEET_DATE = "timesheetDate";
    public static final String TIMESHEET_DATE_FORMAT = "dateFormat";
    public static final String DATE_FORMAT_OF_MMDDYYYY = "MM/dd/yyyy";
    public static final String DATE_FORMAT_OF_EEEEMMMDDYYYY = "EEEE -MMM dd, yyyy";
    public static final String DATE_FORMAT_OF_MMDDYYYYHH = "MM-dd-yyyy hh:mm:ss";
    public static final String DATE_FORMAT_OF_MMMDDYYYY = "MMM dd,yyyy";
    public static final String DATE_FORMAT_OF_MMDD = "MM/dd";
    public static final String DATE_FORMAT_OF_E = "E";
    public static final Boolean TIMESHEET_TRUE_STATUS = true;
    public static final Boolean TIMESHEET_FALSE_STATUS = false;
    public static final String UNITS = "Units";
    public static final String TIMESTAMP = "Timestamp";
    public static final String TIMER = "Timer";
    public static final String SEQUENCE_NUMBER_START = "1";

    public static final String FORMATTED_START_DATE = "formattedStartDate";
    public static final String FORMATTED_END_DATE = "formattedEndDate";

    public static final String TIMESHEET_ATTACHMENTS = "timesheetAttachments";
    public static final String TIMESHEET_ATTACHMENT_ID = "timesheetAttachmentId";
    public static final String TIMESHEET = "test";
    public static final CharSequence OK = "ok";

    public static final Integer MAX_FILE_LENGTH = 10;
    public static final Double DEFAULT_DOUBLE_VALUE = 0.00;
    
    public static final String EMPLOYEE_NAME = "employee.name";
	public static final String ST_HOURS = "stHours";
	public static final String OT_HOURS = "otHours";
	public static final String DT_HOURS = "dtHours";
	public static final String PAYROLL_MANAGER = "payrollManager";
	public static final String ACCOUNT_MANAGER = "accountManager";
	public static final String EMPLOYEE_LOCATION_NAME ="employee.locationName";
	public static final String SUBMITTED_ON = "submitted.on";
	public static final String APPROVED_ON = "approved.on";
	public static final String APPROVER_NAME = "approved.name";
	public static final String SOURCE = "source";
    public static final String ERR_FILE_UPLOAD_COUNT = "Only 10 or less than 10 files are allowed to upload";
    public static final String SEARCH_FIELD = "searchField";
    public static final String TIMESHEETDETAILID = "timesheetDetailsId";
    
    public static final String SUBMITTED_ON_SEARCH = "searchField.lastUpdatedDateTime";
    public static final String UPDATED_ON_SEARCH = "searchField.submittedDateTime";
    public static final String APPROVED_ON_SEARCH = "searchField.approvedDateTime";
    public static final String PERIOD_SEARCH = "searchField.periodDateTime";
    
    private TimesheetViewConstants() {

    }
}
