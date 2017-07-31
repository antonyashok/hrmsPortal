package com.tm.invoice.constants;

public final class InvoiceErrorConstants {

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_ACCESS_DENIED = "error.accessDenied";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String ERR_METHOD_NOT_SUPPORTED = "error.methodNotSupported";
    public static final String ERR_INTERNAL_SERVER_ERROR = "error.internalServerError";

    public static final String ERR_USER_PROFILE = "Error in getUserprofileImage.";
    public static final String ERR_PO_NUMBER = "Po Number already exists";
    public static final String ERR_INVOICE_SETUP_NAME = "Setup Name Already Exists";

    public static final String ERR_DATE_SEQUENCE = "Date Sequence for invoice type is wrong";
    public static final String ERR_EQUAL_FROM_AND_END_DATE = "From and To date of invoice type should not be equal";
    public static final String ERR_NO_CONTRACTOR_FOR_PO = "No Contractor Found for the po Number";
    public static final String ERR_EXISTING_SETUP = "Setup Name Already Exists";
    
    public static final String ERR_FILE_UPLOAD_COUNT = "Only 10 or less than 10 files are allowed to upload";
    
    public static final String ERR_EQUAL_START_DATE_AND_MATURE_DATE = "Start and Mature date of irregular invoice type should not be equal";
    
    public static final String ERR_INVOICE_SETUP_FORMAT_ALREADY_EXISTS = "Invoice Number format already exists";
    
    public static final String ERR_GLOBAL_INVOICE_SETUP_FORMAT_ALREADY_EXISTS = "Global Invoice Number format already exists";

    private InvoiceErrorConstants() {}

}
