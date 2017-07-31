package com.tm.invoice.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.web.core.data.BaseDTO;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoiceRateDTO extends BaseDTO {

    @JsonProperty(access = Access.AUTO)
    private UUID id;

    @NotNull(message = InvoiceConstants.INVOICEID_REQUIRED)
    @NotBlank(message = InvoiceConstants.INVOICEID_BLANK_ERROR_MESSAGE)
    private String invoiceId;

    @NotNull(message = InvoiceConstants.TASKNAME_REQUIRED)
    @NotBlank(message = InvoiceConstants.TASKNAME_BLANK_ERROR_MESSAGE)
    @Size(min = 1, max = 50, message = InvoiceConstants.TASKNAME_MAX_MIN_LIMIT)
    private String taskName;

    @NotNull(message = InvoiceConstants.EFFECTIVEDATE_REQUIRED)
    @NotBlank(message = InvoiceConstants.EFFECTIVEDATE_BLANK_ERROR_MESSAGE)
    private Date effectiveDate;

    @NotNull(message = InvoiceConstants.BILLRATEST_REQUIRED)
    @NotBlank(message = InvoiceConstants.BILLRATEST_BLANK_ERROR_MESSAGE)
    @Length(min = 1, max = 4)
    private BigDecimal billRateST;

    @NotNull(message = InvoiceConstants.BILLRATEOT_REQUIRED)
    @NotBlank(message = InvoiceConstants.BILLRATEOT_BLANK_ERROR_MESSAGE)
    @Length(min = 1, max = 4)
    private BigDecimal billRateOT;

    @NotNull(message = InvoiceConstants.BILLRATEDT_REQUIRED)
    @NotBlank(message = InvoiceConstants.BILLRATEDT_BLANK_ERROR_MESSAGE)
    @Length(min = 1, max = 4)
    private BigDecimal billRateDT;

    @NotNull(message = InvoiceConstants.ENDDATE_REQUIRED)
    @NotBlank(message = InvoiceConstants.ENDDATE_BLANK_ERROR_MESSAGE)
    private Date endDate;

    @NotNull(message = InvoiceConstants.ENDRATEST_REQUIRED)
    @NotBlank(message = InvoiceConstants.ENDRATEST_BLANK_ERROR_MESSAGE)
    @Length(min = 1, max = 4)
    private BigDecimal endRateST;

    @NotNull(message = InvoiceConstants.ENDRATEOT_REQUIRED)
    @NotBlank(message = InvoiceConstants.ENDRATEOT_BLANK_ERROR_MESSAGE)
    @Length(min = 1, max = 4)
    private BigDecimal endRateOT;

    @NotNull(message = InvoiceConstants.ENDRATEDT_REQUIRED)
    @NotBlank(message = InvoiceConstants.ENDRATEDT_BLANK_ERROR_MESSAGE)
    @Length(min = 1, max = 4)
    private BigDecimal endRateDT;

}
