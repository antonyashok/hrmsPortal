package com.tm.commonapi.core.audit.mysql;

import javax.persistence.MappedSuperclass;

import com.tm.commonapi.core.audit.AuditDetails;
import com.tm.commonapi.core.audit.Auditable;
import com.tm.commonapi.web.core.data.BaseEntity;

@MappedSuperclass
public class BaseAuditableEntity extends BaseEntity implements Auditable {

    protected MySQLAuditDetails auditDetails;

    /**
     * @param auditDetails the auditDetails to set
     */
    public void setAuditDetails(AuditDetails auditDetails) {
        this.auditDetails = (MySQLAuditDetails) auditDetails;
    }

    @Override
    public AuditDetails getAuditDetils() {
        return auditDetails;
    }

}
