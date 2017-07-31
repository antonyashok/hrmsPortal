package com.tm.util;

import java.math.BigDecimal;

public class TimesheetConstants {

    public static final String ATLIS_COMMON_TRANSACTION_MANAGER = "atlisTransactionManager";
    public static final String ATLIS_TS_TRANSACTION_MANAGER = "atlisTsTransactionManager";
    public static final String ATLIS_ENGAGEMENT_TRANSACTION_MANAGER = "atlisEngagementTransactionManager";
    public static final String USER_PROFILE_DEV = "SELECT e.employeeId as userId, e.firstName as firstName, e.lastName as lastName, "
            + " e.middleInitials as middleInitials, 1 as roleId, 'roleName' as roleName,"
            + " 'roleDescription' as roleDescription, e.employeeType as employeeType from Employee e "
            + " where e.employeeId=:userId and e.activeStatus='Y' and e.employeeType='E' ";
    public static final String USER_PROFILE_LOCAL = "SELECT e.employeeId as userId, e.firstName as firstName, e.lastName as lastName, "
            + " e.middleInitials as middleInitials, e.roleId as roleId, er.roleName as roleName,"
            + " er.roleDescription as roleDescription, e.employeeType as employeeType from Employee e, "
            + " EmployeeRole er where er.roleId=e.roleId and e.employeeId=:userId and e.activeStatus='Y' and e.employeeType='E' ";
    public static final String USER_ID = "userId";
    public static final String USER_EMAIL_ID = "userEmailId";
    public static final String EMAIL = "email";
    public static final String EMPLOYEE_ID = "employeeId";
    public static final String EMPLOYEE_IDS = "employeeIds";
    public static final String MANAGER_EMPLOYEE_ID = "managerEmployeeId";
    public static final String ACTIVE_FLAG = "activeFlag";
    public static final String TIME_RULE_LOOKUP_MONGO_COLLECTION = "timeRuleLookup";
    public static final String PROFILE_IMAGE_MONGO_COLLECTION = "profileImage";
    public static final String TIME_SHEET_DETAIL_ID = "timesheetDetailId";
    public static final String TIME_LIST_NOTE_ID = "timeListNoteId";
    public static final String TIME_SHEET_NOTES_ID = "timesheetNotesId";
    public static final String TIME_SHEET_NOTE_COUNT = "timesheetNoteCount";
    public static final String TIME_SHEET_CONSOLIDATED_DATA = "timesheetConsolidatedData";
    public static final String TIME_SHEET_DATA = "timesheetData";
    public static final String PTOREQUEST_DATA = "ptoRequestData";
    public static final String PTOTYPE_DATA = "ptoTypeData";
    public static final String PTOTYPE_ID = "ptoTypeId";
    public static final String PTO_COMMENT_COUNT = "ptoCommentCount";
    public static final String SPECIFIC_TIME_SHEET_SUMMARY = "specificTimesheetSummary";
    public static final String RCTIMESHEETCONFIGID = "rcTimeSheetConfigId";
    public static final String RETIMESHEETCONFIGDTO = "rcTimeSheetConfigDto";
    public static final String LEGACY_ID = "legacyId";

    public static final String YYYYMMDD_WITH_HYPEN = "yyyy-MM-dd";
    public static final String YYYYMMDD_WITH_DOT = "yyyy.MM.dd";
    public static final String MM_DD_YYYY_HH_MM_SS_A = "MM/dd/yyyy h:mm:ss a";
    public static final String MMM_DD_YYYY_HH_MM_SS_A = "MMM dd, yyyy - HH:mm:ss";
    public static final String EEE_MMM_DD_YYYY = "EEE MMM dd, yyyy";
    public static final String MM_DD_YYYY = "MM/dd/yyyy";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd hh:mm:ss";
    public static final String MM_DD_YYY_HH_MM_SS_A = "MM/dd/yyy h:mm:ss a";
    public static final String MM_DD_YYYY_HH_MM_SS = "MM/dd/yyyy hh:mm:ss";
    public static final String SUN = "SUN";
    public static final String MON = "MON";
    public static final String TUE = "TUE";
    public static final String WED = "WED";
    public static final String THU = "THU";
    public static final String FRI = "FRI";
    public static final String SAT = "SAT";

    public static final String CC_SUN = "Sun";
    public static final String CC_MON = "Mon";
    public static final String CC_TUE = "Tue";
    public static final String CC_WED = "Wed";
    public static final String CC_THU = "Thu";
    public static final String CC_FRI = "Fri";
    public static final String CC_SAT = "Sat";

    public static final String SUNDAY = "sunday";
    public static final String MONDAY = "monday";
    public static final String TUESDAY = "tuesday";
    public static final String WEDNESDAY = "wednesday";
    public static final String THURSDAY = "thursday";
    public static final String FRIDAY = "friday";
    public static final String SATURDAY = "saturday";

    public static final String TIME = "Time";

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String YES = "Yes";
    public static final String NO = "No";

    public static final String VIEW_TS_SUMMARY = "viewTimesheetSummary";
    public static final String VIEW_TS_SUMMARY_FOR_MOBILE = "viewTimesheetSummary";

    public static final String TIME_OFF_TYPE = "timeOffType";

    public static final String WEB = "web";
    public static final String MOBILE = "mobile";

    public static final String TOTAL = "total";
    public static final String CC_TOTAL = "Total";
    public static final String MM_DD = "MM/dd";
    public static final String DAY = "day";
    public static final String WEEKHOLIDAY = "WeekHoliday";
    public static final String CC_DAY = "Day";
    public static final String DATE = "date";
    public static final String CC_DATE = "Date";
    public static final String CC_NOTE_ID = "NoteId";
    public static final String CC_TS_DATE = "TimesheetDate";
    public static final String CC_TIME_DETAILS_ID = "TimeDetailsId";
    public static final String CC_PTO_REQUEST_ID = "PtoRequestId";
    public static final String CC_PTO_REQUEST_DATE = "PtoRequestDate";
    public static final String CC_COMMENT = "Comment";
    public static final String HOURS = "hours";
    public static final String GROUP = "group";
    /**
     * PTO_TYPE List START
     */
    public static final String HOLIDAY = "Holiday";
    public static final String VACATION = "Vacation";
    public static final String BEREAVEMENT = "Bereavement";
    public static final String JURY_DUTY = "Jury Duty";
    public static final String SICK = "Sick";
    public static final String PTO = "Pto";
    public static final String OTHER = "Other";
    public static final String OFFICE_CLOSURE = "Office Closure";
    public static final String NA = "NA";
    public static final String OFF = "Off";

    /**
     * PTO_TYPE List END
     */

    public static final String DAY1 = "day1";
    public static final String DAY2 = "day2";
    public static final String DAY3 = "day3";
    public static final String DAY4 = "day4";
    public static final String DAY5 = "day5";
    public static final String DAY6 = "day6";
    public static final String DAY7 = "day7";

    public static final String TEMP_FILE1_NAME = "default-profile.jpg";
    public static final String TEMP_FILE2_NAME = "default-profile.jpg";
    public static final String TEMP_FILE_DIR_NAME = "/images/";
    public static final String PROFILE_IMG_1 = "profileImage-1.jpg";
    public static final String PROFILE_IMG_2 = "profileImage-2.jpg";
    public static final String PROFILE_IMG_3 = "profileImage-3.jpg";

    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";

    public static final String APPROVED = "Approved";
	public static final String VERIFIED = "Verified";
	public static final String UNVERIFIED = "Unverified";
    
    public static final String OVERDUE = "Overdue";
    public static final String REJECTED = "Rejected";
    public static final String AWAITING_APPROVAL = "AwaitingApproval";
    public static final String NOT_SUBMITTED = "NotSubmitted";
    public static final String AWAITING_APPROVAL_WITH_SPACE = "Awaiting approval";
    public static final String AWAITING_APPROVAL_WITH_FIRST_CAPS= "Awaiting Approval";
    public static final String NOT_SUBMITTED_WITH_SPACE = "Not Submitted";
    public static final String CC_APPROVED = "approved";
    public static final String CC_AWAITING_APPROVAL = "awaitingApproval";

    public static final String TS_DETAILS_ID = "tsDetailsId";
    public static final String TIMESHEET_DETAILS_ID = "timesheetId";
    public static final String TIMESHEET_DETAIL_IDS = "tsDetailIds";
    public static final String TIMESHEET_TIME_DETAILS_ID = "tstDetailsIds";
    public static final String TS_TYPE_DETAIL_ID = "timesheetTypeDetailId";
    public static final String TS_TIME_DETAIL_ID = "timesheetTypeDetailId";
    public static final String TIMESHEET_NOTE_ERROR = "Already entered the timesheet details";
    public static final String ALERT_MAX_CAP_HOURS_EXCEEDS = "ALERT: Exceeding maximum week hours";
    public static final String ALERT = "alert";
    public static final String PTO_HOUR_INSUFFICIENT = "Insufficient PTO hours";
    public static final String SEARCH_TERM = "searchTerm";
    public static final String STATUS = "status";
    public static final String OK = "ok";
    public static final String ERROR = "error";
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String NO_RECORDS_FOUND = "No records found";
    public static final String START_INDEX = "startIndex";
    public static final String NO_OF_ROWS = "results";
    public static final String SORT = "sort";
    public static final String DIR = "dir";
    public static final String SEARCH_TXT = "searchTxt";
    public static final String PAGE = "page";
    public static final String SIZE = "size";
    public static final String ORDER = "order";
    public static final int PAGE_SIZE = 25;
    public static final String TIMESHEET_ID = "timesheetId";
    public static final String TIMESHEET_TYPE = "timesheetType";

    public static final String TIMESHEET_STATUS = "timesheetStatus";
    public static final String PTO_REQUEST_ID = "ptoRequestId";
    public static final String PTO_COMMENT = "comment";
    public static final String PTO_HOUR_REQUEST = "hoursRequested";
    public static final String PTO_REQUEST_DATE = "ptoRequestDate";

    public static final String PTO_TYPE = "ptoType";
    public static final String PTO_TYPE_ID = "ptoTypeId";
    public static final String PTO_TYPE_HOUR = "ptoTypeHour";
    public static final String PTO_TYPE_NOTE = "ptoTypeNote";

    public static final String TIMESHEET_LIST_HOURS = "timeListHour";
    public static final String TIMESHEET_LIST_NOTE = "timeListNote";
    public static final String TIME_DETAILS_ID = "timeDetailsId";

    public static final String PTO_DATE = "ptoDate";
    public static final String PTO_SUBMITED = "Submited";
    public static final String ALL = "all";
    public static final String CC_ALL = "All";

    public static final String TIMESHEET_NOTE = "timesheetNote";
    public static final String TS_NOTE_ID = "timesheetNoteId";
    public static final String TS_HOUR = "timesheetHour";

    public static final String TIMESHEET_MOBILE_COMMENT = "timesheetMoblieComment";

    public static final String LAST_UPDATE_DATE = "auditDetails.lastUpdateDate";
    public static final String TIMESHEET_IDS = "timesheetIds";
    public static final String MESSAGE = "message";
    public static final String TIMESHEET_IMAGE_MONGO_COLLECTION = "timesheetAttachmentImage";
    public static final String TIMESHEET_ATTACHEMENT_IDS = "timesheetAttachmentId";
    public static final String FILENAME = "fileName";
    public static final String EEE_MMM_DD_YYYY_HH_MM = "MMM dd, yyyy hh:mm:ss a";
    public static final String ALREADY_UPLOAD = "Already Uploaded";
    public static final String NO_SCREEN_ATTACHED = "No Screenshots Attached ";

    public static final String TOTAL_HOURS = "totalHours";
    public static final String PTO_HOURS = "ptoHours";
    public static final String WORK_HOURS = "workHours";
    public static final String NAME = "JOHN";
    public static final String TIMESHEET_SUBMITTED = "Timesheet has been submitted";
    public static final String TIMESHEET_APPROVED = "Timesheet has been approved";
    public static final String TIMESHEET_REJECTED = "Timesheet has been rejected";
    public static final String TIMESHEET_STATUS_UPDATED = "Timesheet has been updated";
    
    public static final String TIMEOFF_WITH_HYPEN = "Time Off - ";
    public static final String HAVE_BEEN_ADDED = " has been added";
    public static final String HAVE_BEEN_UPDATED = " has been updated";
    public static final String HAVE_BEEN_DELETED = " has been deleted";

    public static final String OT = "OT";
    public static final String DT = "DT";

    public static final String TS_OT_LIST = "tsOtList";
    public static final String TS_DT_LIST = "tsDtList";

    public static final String LOC = "loc";
    public static final String GRP = "grp";

    public static final String OFFICE_ID = "officeId";
    public static final String OFFICE_IDS = "officeIds";
    public static final String CONFIG_IDS = "configIds";

    public static final String RECRUITER_TS_CONFIG = "recruiterTsConfigVO";
    public static final String RECRUITER_TS_HOUR = "recruiterTsHourVO";
    public static final String RECRUITER_TS_LOCATION = "recruiterTsLocationVO";
    public static final String RECRUITER_HOLIDAY_LOCATION_GROUP = "rectrHldLocGrpVO";
    public static final String CONFIG_ID = "configId";

    public static final String EMPLOYEE_TYPE_RECRUITER = "R";
    public static final String EMPLOYEE_TYPE_EMPLOYEE = "E";
    public static final String APPROVED_COUNT = "approvedCount";
    public static final String AWAITING_APPROVAL_COUNT = "awaitingApprovalCount";
    public static final String ALL_COUNT = "allCount";

    public static final String ROLE_NAME = "roleName";
    public static final String RECRUITER = "Recruiter";
    public static final String SALES_MANAGER = "Sales Manager";
    public static final String HR_MANAGER = "HR Manager";
    public static final String ROLE_ID = "roleId";

    public static final String RECRUITER_NOTIFY_CONFIG = "rectrrNotifyConfigVO";

    public static final String TIMESTAMP = "timestamp";

    public static final String RECRUITER_ID = "recruiterId";
    public static final String LOCATION = "location";
    public static final String RC_TS_CONFIG_ID = "rcTimesheetConfigId";
    public static final String OFFICE_LOCATIONS = "officeLocations";

    public static final String CHECK_DATA = "Check Input Data";
    /**
     * REGEX EXPRESSIONS
     */
    public static final String REGEX_NUMBERS = "^[0-9]";

    public static final String OFFICE_NAME = "officeName";
    public static final String HOLIDAYYEAR = "holidayYear";
    public static final String HOLIDAY_DESCRIPTION = "holidayDescription";

    public static final String UUID_DEFAULT = "00000000-0000-0000-0000-000000000000";
    public static final String SAVE = "save";
    public static final String UPDATE = "update";
    public static final String ADDED = "Added";
    public static final String UPDATED = "Updated";
    public static final String WEEK_START_LESS_THAN_WEEK_END = "Week start less than week end";
    public static final String SUN_HOUR_NOT_EXISTS = "Sunday Hour not exists 24";
    public static final String MON_HOUR_NOT_EXISTS = "Monday Hour not exists 24";
    public static final String TUE_HOUR_NOT_EXISTS = "Tuesday Hour not exists 24";
    public static final String WED_HOUR_NOT_EXISTS = "Wednesday Hour not exists 24";
    public static final String THU_HOUR_NOT_EXISTS = "Thusday Hour not exists 24";
    public static final String FRI_HOUR_NOT_EXISTS = "Friday Hour not exists 24";
    public static final String SAT_HOUR_NOT_EXISTS = "Saturday Hour not exists 24";
    public static final String SUN_START_TIME_NOT_EXISTS =
            "Sunday start time, limit not exists break start time";
    public static final String SUN_END_TIME_NOT_EXISTS =
            "Sunday end time not exists 24 and limit exists break end time";
    public static final String MON_START_TIME_NOT_EXISTS =
            "Monday start time, limit not exists break start time";
    public static final String MON_END_TIME_NOT_EXISTS =
            "Monday end time not exists 24 and limit exists break end time";
    public static final String TUE_START_TIME_NOT_EXISTS =
            "Tuesday start time, limit not exists break start time";

    public static final String TUE_END_TIME_NOT_EXISTS =
            "Tuesday end time not exists 24 and limit exists break end time";
    public static final String WED_START_TIME_NOT_EXISTS =
            "Wednesday start time, limit not exists break start time";
    public static final String WED_END_TIME_NOT_EXISTS =
            "Wednesday end time not exists 24 and limit exists break end time";
    public static final String THU_START_TIME_NOT_EXISTS =
            "Thursday start time, limit not exists break start time";
    public static final String THU_END_TIME_NOT_EXISTS =
            "Thusday end time not exists 24 and limit exists break end time";
    public static final String FRI_START_TIME_NOT_EXISTS =
            "Friday start time, limit not exists break start time";
    public static final String FRI_END_TIME_NOT_EXISTS =
            "Friday end time not exists 24 and limit exists break end time";
    public static final String SAT_START_TIME_NOT_EXISTS =
            "Saturday start time, limit not exists break start time";
    public static final String SAT_END_TIME_NOT_EXISTS =
            "Saturday end time not exists 24 and limit exists break end time";

    public static final String STRAIGHT_TIME_MAXIMUM_NOT_EXISTS =
            "Straight time maximum not exists 24";
    public static final String OVER_TIME_MAXIMUM_NOT_EXISTS = "Over time maximum not  exists 24";
    public static final String DOUBLE_TIME_MAXIMUM_NOT_EXISTS = "Double time maximum not exists 24";
    public static final String STRAIGHT_TIME_NOT_EXISTS = "Straight time not exists 168";
    public static final String OVER_TIME_NOT_EXISTS = "Over time not exists 168";
    public static final String DOUBLE_TIME_NOT_EXISTS = "Double time not exists 168";
    public static final String OVER_TIME_MINIMUM_NOT_EXISTS = "Over time minimum not exists  ";
    public static final String DOUBLE_TIME_MINIMUM_NOT_EXISTS = "Double time minimum not exists  ";


    public static final String SUNDAY_HOURS = "sundayHours";
    public static final String MON_HOURS = "monHours";
    public static final String TUE_HOURS = "tueHours";
    public static final String WED_HOURS = "wedHours";
    public static final String THU_HOURS = "thuHours";
    public static final String FRI_HOURS = "friHours";
    public static final String SAT_HOURS = "satHours";
    public static final String BREAK_START_TIME = "breakStartTime";
    public static final String BREAK_END_TIME = "breakEndTime";
    public static final String SUN_START_TIME = "sunStartTime";
    public static final String SUN_END_TIME = "sunEndTime";
    public static final String MON_START_TIME = "monStartTime";
    public static final String MON_END_TIME = "monEndTime";
    public static final String TUE_START_TIME = "tueStartTime";
    public static final String TUE_END_TIME = "tueEndTime";
    public static final String WED_START_TIME = "wedStartTime";

    public static final String WED_END_TIME = "wedEndTime";
    public static final String THU_START_TIME = "thuStartTime";
    public static final String THU_END_TIME = "thuEndTime";
    public static final String FRI_START_TIME = "friStartTime";
    public static final String FRI_END_TIME = "friEndTime";
    public static final String SAT_START_TIME = "satStartTime";
    public static final String SAT_END_TIME = "satEndTime";
    public static final BigDecimal BIGDECIMAL_VALUE_ZERO = BigDecimal.ZERO;
    public static final String DATE_FORMAT_ERROR = "Invalid date format";
    public static final String EMAIL_ERROR = "Invalid Email";
    public static final String EMAIL_MAX_ERROR = "Maximum email Id limit is 10";
    public static final Integer EMAIL_MAX_COUNT = 10;

    public static final String DAILY = "D";
    public static final String HOUR = "H";
    public static final String TIMESTAMP_TYPE = "T";

    public static final BigDecimal DAY_HOUR = new BigDecimal(24);
    public static final BigDecimal WEEK_HOUR = new BigDecimal(168);
    public static final String SET_EMPTY = "0.00";
    public static final String NO_RECORDS = "time.norecords.found";
    public static final String RC_DELETE_MESSAGE = "time.recruiterTsHoliday.records.delete.success";

    public static final String SENDER_NAME = "senderName";

    public static final Long ZERO_LONG = 0L;
    public static final String HOLIDAY_ERROR = "Holiday is required";

    public static final String ENTER_MAXIMUM_OT = "Enter the maximum over time";
    public static final String ENTER_MAXIMUM_DT = "Enter the maximum Double time";
    public static final String TIMESHEET_NO = "N";
    public static final String TIMESHEET_YES = "Y";
    public static final String EMPLOYEE_TYPE = "E";
    public static final String EMP_TYPE = "empType";
    public static final String CONTRACTOR_TYPE = "C";
    public static final String FLAG = "flag";
    public static final String SUCESS_MESSAGE = "Successfully created";
    public static final String GIVEN_HOUR_NOT_EXISTS_MIN_MAX = "Week hours total should lie between";
    public static final String AND = "and";
    public static final String MAI_HOURS = "min Hours";
    public static final String MAX_HOURS = "max Hours.";
    public static final String TIMESHEET_SERVICE = "urn:igi:timetrak:hr-recruiter-timesheet:approve";
    
    /**
     * AUTHORITIES
     */
    public static final String DELETE_EXPENSE_REPORT = "urn:igi:expense-management:expense-report:delete";
    public static final String UPLOAD_RECEIPT = "urn:igi:expense-management:receipt:import";
    public static final String DELETE_RECEIPT = "urn:igi:expense-management:receipt:delete";
    
    public static final String APPROVE_EXPENSE_APPROVER = "urn:igi:expense-management:expense-report:approve";
    public static final String APPROVE_EXPENSE_AUDIT = "urn:igi:expense-management:compliance-expense-report:approve";
    public static final String EMAIL_FOR_APPROVAL_AUDIT = "urn:igi:expense-management:compliance-expense-send-email-for-approval:create";
    public static final String RECEIPT_VERIFIED_AUDIT = "urn:igi:expense-management:compliance-expense-verify-receipt:approve"; 
    public static final String APPORVAL_NEEDED_AUDIT = "urn:igi:expense-management:compliance-expense-approval-needed:approve";
    public static final String APPROVAL_RECEIVED_AUDIT = "urn:igi:expense-management:compliance-expense-approval-received:approve"; 
    public static final String APPROVE_EXPENSE_PAYROLL = "urn:igi:expense-management:payroll-expense-report:approve";   
    public static final String APPROVE_EXPENSE_INVOICE = "urn:igi:expense-management:verification-expense-report:approve";
    public static final String ADMIN_SETTINGS = "urn:igi:admin-expense-management:admin-settings:grant";    
    public static final String USER_PROFILE_VIEW = "urn:igi:authorization-management:userprofile:view";
    public static final String CREATE_NOTES = "urn:igi:expense-management:notes:create";
    public static final String ADMIN_SETTINGS_WITHOUT_SCOPE = "urn:igi:admin-expense-management:admin-settings";    
    public static final String YES_KEY = "Yes";
    public static final String NO_KEY = "No";    
    public static final String AUTHORITY_NAME = "authorityName";
    public static final String PARENTID = "parentId";
    public static final String INTERNAL_AUDIT_SPECIALIST = "Internal Audit Specialist";
    public static final String PAYROLL_MANAGER = "Payroll Manager";
    public static final String INVOICING_SPECIALIST = "Invoicing Specialist";  
    public static final String SUBMITTER = "Submitter";
    public static final String APPROVER = "Approver";
    public static final String ADMIN = "Admin";
    public static final String VERIFICATION_TEAM = "Verification Team";
    public static final String STAFFING = "Staffing";
    public static final String IG_MANAGEMENT = "Ig Management";
    public static final String COMPLIANCE_TEAM = "Compliance Team";
    public static final String PAYROLL_TEAM = "Payroll Team";
    public static final String CREATE_EXPENSE_REPORT = "urn:igi:expense-management:expense-report:create";    
    public static final String SIDEMENU = "SIDEMENU";
    public static final String PARENTCAMID = "parentCamId";
    public static final String PARENT_UUID = "parentUUID";
    public static final String EMAILADDRESS = "emailAddress";
    public static final String PRINCIPAL = "principal";
    
    public static final String ADMIN_TIMETRACK_SETTINGS = "urn:igi:timetrak:admin-settings:grant";    
    public static final String HR_RECRUITER = "urn:igi:timetrak:hr-recruiter-timesheet:approve";    
    public static final String SALES_RECRUITER = "urn:igi:timetrak:sales-recruiter-timesheet:approve";    
    
    public static final String HR_TEAM = "Hr Team";
    public static final String SALES_TEAM = "Sales Team";
    public static final String SERVICE_NOT_AVAILABLE = "Service Not Available";
    public static final String STAFFING_VIEW = "urn:igi:restrak:candidate:view";   
    public static final String EEEE_MMM_DD_YYYY = "EEEE - MMM dd, yyyy"; 
    public static final String CC_TS_DATE_Format = "TimesheetDateFormat";
    public static final String AUDIT_LAST_UPDATEDATE="lastUpdateDate";

    
    public static final String APPROVAL_ERROR = "No approvals";
    public static final String TS_ACTIVITY_SUMMARY = "tsActivitySummary";
    public static final String TS_APPROVAL_SUMMARY = "tsApprovalsSummary";
    public static final String NOTES_ERROR = "No notes entered for timesheet";
    public static final String MANAGER_COMMENTS_SUMMARY = "managerCommentsSummary";
    public static final String TS_NOTES_ADDED="Note added";
    public static final String TS_NOTES_HAS_BEEN_ADDED="Notes has been added";

    /**
     * The Sales and HR Manager Leave Type Constant values
     * 
     */
    
    public static final Integer LEAVE_WORKING = 1;
    public static final Integer LEAVE_PTO = 3;
    public static final Integer LEAVE_SICK_TIME_OFF = 4;
    public static final Integer LEAVE_HOLIDAY = 5;
    public static final Integer LEAVE_JURY_DUTY = 6;
    public static final Integer LEAVE_BEREAVEMENT = 7;
    public static final Integer LEAVE_OFFICE_CLOSURE = 8;
    public static final int WORK_TYPE = 1;
	public static final String EFFECTIVE_DATE = "effectiveDate";
    public static final String KAFKA_TIMESHEET_TYPE= "InternalTimesheet";
    
    public static final String TIMESHEET_CREATED = "TimesheetCreated";
    public static final String UNITS = "Units";
    public static final String COMPLETED = "Completed";

}
