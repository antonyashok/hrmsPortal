package com.tm.commonapi.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

	public static final String ADMIN = "ROLE_ADMIN";

	public static final String USER = "ROLE_USER";

	public static final String ANONYMOUS = "ROLE_ANONYMOUS";

	public static final String TIMEOFF_SUBMITTER = "TIMEOFF_SUBMITTER";
	public static final String TIMEOFF_APPROVER = "TIMEOFF_APPROVER";
	public static final String TIMESHEET_SUBMITTER = "TIMESHEET_SUBMITTER";
	public static final String TIMESHEET_APPROVER = "TIMESHEET_APPROVER";
	public static final String PROFILE_VIEW = "PROFILE_VIEW";
	public static final String TIMSHEET_VERIFIER = "TIMSHEET_VERIFIER";
	public static final String RECRUITER_AUTH = "RECRUITER";
	public static final String TIMESHEET_PAYROLL_APPROVER = "TIMESHEET_PAYROLL_APPROVER";

	public static final String EXP_VERIFICATION_APPROVER = "EXP_VERIFICATION_APPROVER";
	public static final String EXP_COMPLIANCE_APPROVER = "EXP_COMPLIANCE_APPROVER";
	public static final String EXP_PAYROLL_APPROVER = "EXP_PAYROLL_APPROVER";
	public static final String EXP_APPROVER = "EXP_APPROVER";
	public static final String EXP_SUBMITTER = "EXP_SUBMITTER";
	public static final String ACCOUNT_MANAGER = "ACCOUNT_MANAGER";
	public static final String SUPER_ADMIN = "SUPER_ADMIN";

	public static final String FINANCE_REPRESENTATIVE = "FINANCE_REPRESENTATIVE";
	public static final String FINANCE_MANAGER = "FINANCE_MANAGER";
	public static final String ALL = "ALL";

	private AuthoritiesConstants() {
	}
}
