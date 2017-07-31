package com.tm.invoice.dto;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Relation(value = "GlobalInvoiceContractorsSetupGrid", collectionRelation = "GlobalInvoiceContractorsSetupGridViews")
public class GlobalInvoiceContractorsSetupGridDTO implements Serializable {

    private static final long serialVersionUID = 8772519401904272178L;

    private UUID globalInvoiceSetupContractorId;
    private UUID globalInvoiceSetupId;
    private long employeeId;
    private String contractorName;
    private String fileNumber;
    private String poNumber;
    private String clientId;
    private String clientName;   

    public UUID getGlobalInvoiceSetupContractorId() {
      return globalInvoiceSetupContractorId;
    }

    public void setGlobalInvoiceSetupContractorId(UUID globalInvoiceSetupContractorId) {
      this.globalInvoiceSetupContractorId = globalInvoiceSetupContractorId;
    }

    public UUID getGlobalInvoiceSetupId() {
        return globalInvoiceSetupId;
    }

    public void setGlobalInvoiceSetupId(UUID globalInvoiceSetupId) {
        this.globalInvoiceSetupId = globalInvoiceSetupId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getContractorName() {
        return contractorName;
    }

    public void setContractorName(String contractorName) {
        this.contractorName = contractorName;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
}
