package com.tm.common.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "companyProfileAttachment")
public class CompanyProfileAttachment implements Serializable {

	private static final long serialVersionUID = -6997811371376943812L;
	@Id
	@Column(name = "_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
