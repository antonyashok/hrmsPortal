package com.tm.invoice.domain;

import javax.persistence.Column;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tm.invoice.enums.CurrencyEnum;

@Document(collection = "InvoiceSetupBillToClient")
public class InvoiceSetupBillToClient {

    public enum LineOfBusinessEnumValue {
        Telecom, Finance, HealthCare, Banking,
    }

    @Column
    private ObjectId _id;

    @Column
    private Long billToClientId;

    @Column
    private Long endClientId;

    @Column
    private String billToClient;

    @Column
    private String endClient;

    @Column
    private String costCenter;

    @Column
    @NotBlank(message = "Line Of Business Not empty")
    private LineOfBusinessEnumValue lineOfBusiness;

    @Column
    private Long contractorId;

    @Column
    private CurrencyEnum payCurrency;

    @Column
    private String paymentTerms;

    @Column
    private String terms;

    @Column
    private String deptNumber;

    @Column
    private Long fein;

    @Column
    private Long engagementId;

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Long getBillToClientId() {
        return billToClientId;
    }

    public void setBillToClientId(Long billToClientId) {
        this.billToClientId = billToClientId;
    }

    public Long getEndClientId() {
        return endClientId;
    }

    public void setEndClientId(Long endClientId) {
        this.endClientId = endClientId;
    }

    public String getBillToClient() {
        return billToClient;
    }

    public void setBillToClient(String billToClient) {
        this.billToClient = billToClient;
    }

    public String getEndClient() {
        return endClient;
    }

    public void setEndClient(String endClient) {
        this.endClient = endClient;
    }

    public String getCostCenter() {
        return costCenter;
    }

    public void setCostCenter(String costCenter) {
        this.costCenter = costCenter;
    }

    public LineOfBusinessEnumValue getLineOfBusiness() {
        return lineOfBusiness;
    }

    public void setLineOfBusiness(LineOfBusinessEnumValue lineOfBusiness) {
        this.lineOfBusiness = lineOfBusiness;
    }

    public Long getContractorId() {
        return contractorId;
    }

    public void setContractorId(Long contractorId) {
        this.contractorId = contractorId;
    }

    public CurrencyEnum getPayCurrency() {
        return payCurrency;
    }

    public void setPayCurrency(CurrencyEnum payCurrency) {
        this.payCurrency = payCurrency;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getDeptNumber() {
        return deptNumber;
    }

    public void setDeptNumber(String deptNumber) {
        this.deptNumber = deptNumber;
    }

    public Long getFein() {
        return fein;
    }

    public void setFein(Long fein) {
        this.fein = fein;
    }

    public Long getEngagementId() {
        return engagementId;
    }

    public void setEngagementId(Long engagementId) {
        this.engagementId = engagementId;
    }

   /* @Override
    public ObjectId getId() {

        return null;
    }

    @Override
    public void setId(ObjectId id) {

    }*/

}
