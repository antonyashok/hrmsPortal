package com.tm.timesheet.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "engagement")
public class EngagementDetail implements Serializable {

    private static final long serialVersionUID = 1551506403244860161L;

    @Id
    @Column(name = "engmt_id")
    private String id;
    @Column(name = "engmt_nm")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
