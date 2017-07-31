package com.tm.invoice.mongo.dto;


import java.math.BigDecimal;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "manualInvoiceContractorDetail")
public class ManualInvoiceContractorDetailDTO {

    private static final String CONTRACTOR_ID_REQUIRED = "Contractor Id is Required";
    private static final String CONTRACTOR_NAME_REQUIRED = "Contractor Name is Required";
    private static final String AMOUNT_REQUIRED = "Amount is Required";
    private static final String DESCRIPTION_REQUIRED = "Description is Required";

    private ObjectId id;
    @NotBlank(message = CONTRACTOR_ID_REQUIRED)
    private Long contractorId;
    @NotBlank(message = CONTRACTOR_NAME_REQUIRED)
    private String contractorName;
    @NotBlank(message = AMOUNT_REQUIRED)
    private BigDecimal amount;
    @NotBlank(message = DESCRIPTION_REQUIRED)
    private String description;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
