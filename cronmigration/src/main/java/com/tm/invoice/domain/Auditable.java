package com.tm.invoice.domain;

public interface Auditable {

    AuditDetails getAuditDetails();

    void setAuditDetails(AuditDetails auditDetails);

}
