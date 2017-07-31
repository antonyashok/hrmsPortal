package com.tm.common.employee.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bson.types.ObjectId;
import org.hibernate.annotations.Type;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@Document(collection = "EmployeeAttachments")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeAttachments implements Serializable {
	
	private static final long serialVersionUID = -8261134130485423082L;

		@Id
	    @Type(type = "uuid-char")
	    @GeneratedValue(generator = "id")
	    @Column
	    private ObjectId id;

	    @Column
	    private String sourceReferenceId;

	    @Column
	    private String sourceReferenceName;

	    public ObjectId getId() {
	        return id;
	    }

	    public void setId(ObjectId id) {
	        this.id = id;
	    }

	    public String getSourceReferenceId() {
	        return sourceReferenceId;
	    }

	    public void setSourceReferenceId(String sourceReferenceId) {
	        this.sourceReferenceId = sourceReferenceId;
	    }

	    public String getSourceReferenceName() {
	        return sourceReferenceName;
	    }

	    public void setSourceReferenceName(String sourceReferenceName) {
	        this.sourceReferenceName = sourceReferenceName;
	    }

}
