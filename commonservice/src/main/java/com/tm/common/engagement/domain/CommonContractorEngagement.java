package com.tm.common.engagement.domain;

import java.io.Serializable;

import javax.persistence.Column;

public class CommonContractorEngagement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6547768326560944045L;

	@Column(name = "emp_full_name")
	private String name;

	@Column(name = "am_empl_id")
	private String accountManagerMailId;

	/*
	 * @Column(name="") private String recruiterName;
	 * 
	 * @Column(name="") private String recruiterMailId;
	 * 
	 * @Column(name="") private Long clientManagerId;
	 * 
	 * @Column(name="") private String clientManagerName;
	 * 
	 * @Column(name="") private String clientManagerMailId;
	 */

	@Column(name = "file_no")
	private Integer fileNumber;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountManagerMailId() {
		return accountManagerMailId;
	}

	public void setAccountManagerMailId(String accountManagerMailId) {
		this.accountManagerMailId = accountManagerMailId;
	}

	public Integer getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(Integer fileNumber) {
		this.fileNumber = fileNumber;
	}
}
