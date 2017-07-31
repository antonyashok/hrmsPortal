package com.tm.commonapi.core.audit;

public interface Auditable {

    AuditDetails getAuditDetils();

    void setAuditDetails(AuditDetails auditDetails);

}
