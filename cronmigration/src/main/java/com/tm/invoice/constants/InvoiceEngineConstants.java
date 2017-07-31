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
package com.tm.invoice.constants;

import com.tm.invoice.domain.InvoiceSetupOption.InvoiceFlag;
import com.tm.invoice.enums.GlobalInvoiceFlag;

public class InvoiceEngineConstants {

    public static final String PRIVATE = "Private";

    public static final String IS_TS_INCLUDE = "INCLUDE_TS_INV";

    public static final String IS_EXP_INCLUDE = "INCLUDE_EXP";

    public static final String IS_EXP_DOC_INCLUDE = "INCLUDE_EXP_DOC";

    public static final String IS_HIRING_MANAGER_SHOW = "SHOW_HIR_MANAGER";

    public static final String IS_SINGLE_LINE_ITEM_SHOW = "INV_SUB_PROJ_AS_SINGLE_LINE";

    public static final String IS_DIFF_LINE_ITEM_SHOW = "INV_SUB_PROJ_AS_DIFF_LINE";

    public static final String IS_SEP_OT_INV = "GENERATE_SEP_OT_INV";

    public static final String IS_CONTRACTOR_EXCLUDE = "EXCLUDE_CONTRACTOR";

    public static final InvoiceFlag PRIVATE_Y = InvoiceFlag.Y;
    
    public static final GlobalInvoiceFlag GLOB_STR = GlobalInvoiceFlag.Y;
    
    public static final String AUTO_DELIVERY = "Auto Delivery";

    public static final String EMAIL_DELIVERY = "Email";

    public static final String POSTAL_DELIVERY = "USPS";
    
    public static final String HOURS = "Hours";
    
    public static final String UNITS = "Units";
    
    /* Attachments constants */
    public static final String TYPE_PDF = "application/pdf";
    
    /*Template Constants*/
    public static final String LOGO = "logo";

    public static final String ADDRESS = "customerAddress";

    public static final String BILL_TO_MANAGER = "billToManager";

    public static final String BILL_TO_MANAGER_ADDR = "billToManagerAddress";

    public static final String INV_NUMBER = "invoiceNumber";

    public static final String INV_DATE = "invoiceDate";

    public static final String TOTAL = "total";

    public static final String ADDITIONAL_COMMENTS = "%%additionalComments%%";

    public static final String PAGE = "%%page%%";

    public static final String PO_NUMBER = "poNumber";

    public static final String INV_CONTENT_LIST = "invoiceContentList";

    public static final String PAYMENT_TERMS = "paymentTerms";

    public static final String CONSULTANT_NAME = "consultantName";

    public static final String SUB_REPORT_DIR = "SUBREPORT_DIR";

    public static final int DEFAULT_INVOICE_PAGE_SIZE = 10;

    public static final String PDF = "PDF";

	public static final String INVOICE_SETUP_NOTES = "invoiceSetupNotes";

    private InvoiceEngineConstants(){
        
    }

}
