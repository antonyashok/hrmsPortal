/*******************************************************************************
 * <pre>
 *
 * File          : com.igi.timetrack.constants.TimesheetConstants.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.constants;

public class TimesheetConstants {

	private TimesheetConstants() {
		
	}
	
    public static final String EMPLOYEE = "employee";
    public static final String APPROVER = "approver";
    public static final String SUBMITTER = "submitter";
    public static final String CONTRACTOR = "contractor";
    public static final String RECRUITER = "recruiter";

    public static final String EMPLOYEE_ID = "employee._id";
    public static final String ENGAGEMENT_NAME = "engagement.name";
    public static final String SALESMANAGER_ID = "employee.salesManagerId";
    public static final String REPORTING_MANAGER_ID = "employee.reportingManagerId";
    public static final String ACCOUNT_MANAGER_ID = "engagement.accountManagerId";

    public static final String TIMESHEET_PROXY_TYPE = "Proxy";
    public static final String TIMESHEET_VERIFICATION_TYPE = "Verification";
    public static final String DISPUTE = "Dispute";
    public static final String NOT_SUBMITTED = "Not Submitted";
    public static final String NOT_VERIFIED = "Not Verified";
    public static final String VERIFIED = "Verified";
    public static final String APPROVED = "Approved";
    public static final String COMPLETED = "Completed";
    public static final String AWAITING_APPROVAL = "Awaiting Approval";
    public static final String REJECTED = "Rejected";
    public static final String OVERDUE = "Overdue";
    public static final String STATUS = "status";
    public static final String OVER_ALL_COUNT = "allCount";
    public static final String NO_RECORDS_FOUND = "No records found";

    /*public static final String TYPE_EMPLOYEE = "E";
    public static final String TYPE_CONTRACTOR = "C";
    */
    public static final String TYPE_RECRUITER = "R";    

    public static final String STATUS_ALL = "All";
    public static final String DATE_FORMAT_OF_MMDDYYY = "MM/dd/yyyy";
    public static final String COUNT = "count";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String VIEW_TIMESHEETS = "viewTimesheets";

    public static final String EMPLOYEE_TYPE = "employeeType";
    public static final String EMPLOYEEID = "employeeId";
    public static final String TIMESHEETID = "timesheetId";

    public static final String DATE_FORMAT = "MMM dd, yyyy";
    public static final String DATE_TIME_FORMAT = "MMM dd, yyyy' - 'hh:mm:ss aaa";

    public static final String ON = "on";
    public static final String UPDATED_ON = "updated.on";
    public static final String LEAVE_HOURS = "leaveHours";
    public static final String WORK_HOURS = "workHours";
    public static final String TOTAL_HOURS = "totalHours";
    public static final String PTO_HOURS = "ptoHours";

    public static final String EMPLOYEE_DOT_TYPE = "employee.type";
    public static final String LOOK_UP_TYPE = "lookupType.value";
    public static final String LOOK_UP_ID = "lookUpId";

    public static final String ID = "id";
    
    public static final String ENGAGEMENTID = "engagementId";
    public static final String TIMESHEET_DATE = "timesheetDate";
    public static final String TIMESHEET_DATE_FORMAT = "dateFormat";
    public static final String DATE_FORMAT_OF_DDMMYYY = "dd/MM/yyyy";
    public static final String DATE_FORMAT_OF_EEEEMMMDDYYYY = "EEEE -MMM dd, yyyy";
    
    public static final String HOURS = "Hours";
    public static final String UNITS = "Units";
    public static final String TIMESTAMP = "Timestamp";
    public static final String TIMER = "Timer";
    public static final String HOURS_0 = "0.00";
    
    public static final Boolean TIMESHEET_TRUE_STATUS = true;
    public static final Boolean TIMESHEET_FALSE_STATUS = false;
    public static final String HOURS_FORMAT = "hh:mm aa";
    
    public static final String SUBMITTED_SUCCESFULLY = "Submitted Succesfully";
    public static final String APPROVED_SUCCESFULLY = "Approved Succesfully";
    public static final String REJECTED_SUCCESFULLY = "Rejected Succesfully";
    
    public static final String TIMESHEET_UPDATE_ACTIVITY="Timesheet has been updated";
    public static final String TIMESHEET_COMPLETED_ACTIVITY="Timesheet has been Approved by Finance Team";
    public static final String TIMESHEET_APPROVED_ACTIVITY="Timesheet has been approved";
    public static final String TIMESHEET_SUBMIT_ACTIVITY = "Timesheet has been Submitted";
    public static final String TIMESHEET_VERIFY_ACTIVITY = "Timesheet has been Verified";
    public static final String TIMEOFF_ADD_ACTIVITY = "Time Off - ‘Time off type’ has been updated";
    public static final String TIMER_STOP_ACTIVITY = "Timer has been Stopped";
    public static final String TIMER_START_ACTIVITY = "Timer has been Started";
    public static final String TIMESHEET_REOPEN_ACTIVITY = "Timesheet has been Reopened";
    public static final String TIMESHEET_REJECTED_ACTIVITY = "Timesheet has been Rejected";
    public static final String TIMESHEET = "Timesheet";
    public static final String TIMEOFF = "Timeoff";
    public static final String TIMESHEET_TIME_EMPTY = " is empty for ";
    public static final String TIMESHEET_END_TIME_EMPTY = "End time of ";
    public static final String TIMESHEET_START_TIME_EMPTY = "Start time of ";
    public static final String TIMESHEET_DRAFT_STATUS = "Draft";
    public static final String TIMESHEET_ID_EMPTY = "Timesheet Id is empty";
    
    public static final String OK = "OK";
    public static final String OT_1_5 = "(OT) 1.5";
    public static final String TIMESHEET_PROXY_SUBMIT = "proxySubmit";
    
    public static final String TIMESHEET_UPDATE="Updated";
    public static final String TIMESHEET_SUBMIT="Submitted";
    public static final String TIMESHEET_APPROVED="Approved";
    public static final String TIMESHEET_ACTIVITY="Timesheet has been ";
    
    public static final String AUDIT_DATE_TIME_EXCEPTION ="Audit Date and Time Invalid";
    
    public static final String TIMEOFF_SUBMITTER ="TIMEOFF_SUBMITTER";
    public static final String TIMEOFF_APPROVER ="TIMEOFF_APPROVER";
    public static final String TIMESHEET_SUBMITTER ="TIMESHEET_SUBMITTER";
    public static final String TIMESHEET_APPROVER ="TIMESHEET_APPROVER";
    public static final String PROFILE_VIEW ="PROFILE_VIEW";
    public static final String TIMESHEET_VERIFIER ="TIMSHEET_VERIFIER";
    public static final String RECRUITER_AUTH ="RECRUITER";
    public static final String TIMESHEET_PAYROLL_APPROVER ="TIMESHEET_PAYROLL_APPROVER";
    
    public static final String EXP_VERIFICATION_APPROVER ="EXP_VERIFICATION_APPROVER";
    public static final String EXP_COMPLIANCE_APPROVER ="EXP_COMPLIANCE_APPROVER";
    public static final String EXP_PAYROLL_APPROVER ="EXP_PAYROLL_APPROVER";
    public static final String EXP_APPROVER ="EXP_APPROVER";
    public static final String EXP_SUBMITTER ="EXP_SUBMITTER";
    public static final String ACCOUNT_MANAGER ="ACCOUNT_MANAGER";
    
    public static final String FINANCE_REPRESENTATIVE ="FINANCE_REPRESENTATIVE";    
    public static final String FINANCE_MANAGER="FINANCE_MANAGER";
    
    public static final String SUPER_ADMIN="SUPER_ADMIN";
    
    public static final String MAIL_HIGH_PRIORITY="High";
    public static final String MAIL_LOW_PRIORITY="Low";
    public static final String TIMESHEET_PAYROLL_TYPE = "Payroll";
        
}