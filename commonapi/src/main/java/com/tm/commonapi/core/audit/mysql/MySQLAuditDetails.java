package com.tm.commonapi.core.audit.mysql;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.tm.commonapi.core.audit.AuditDetails;

@Embeddable
public class MySQLAuditDetails implements AuditDetails {

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "create_dt", nullable = false)
    private Date createDate;

    @Column(name = "updated_by", nullable = false)
    private Long updatedBy;

    @Column(name = "last_updt_dt", nullable = false)
    private Date lastUpdateDate;

    /**
     * @return the createdBy
     */
    public Long getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the updatedBy
     */
    public Long getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @param updatedBy the updatedBy to set
     */
    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * @return the lastUpdateDate
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * @param lastUpdateDate the lastUpdateDate to set
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

}
