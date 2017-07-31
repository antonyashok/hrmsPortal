package com.tm.common.employee.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "password_hint_user_questions_answers")
public class PasswordHintUserQuestion implements Serializable {
	
	private static final long serialVersionUID = 7703973188372658510L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "keycloak_user_id")
	private String keyCloakUserId;
	
	@Column(name = "question_id_one")
	private Long questionIdOne;
	
	@Column(name = "answer_one")
	private String answerOne;
	
	@Column(name = "question_id_two")
	private Long questionIdTwo;
	
	@Column(name = "answer_two")
	private String answerTwo;

	@Column(name = "created_dt")
	private Date createdDate;
	
	@Column(name = "last_updt_dt")
	private Date updatedDate;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuestionIdOne() {
		return questionIdOne;
	}

	public void setQuestionIdOne(Long questionIdOne) {
		this.questionIdOne = questionIdOne;
	}

	public String getAnswerOne() {
		return answerOne;
	}

	public void setAnswerOne(String answerOne) {
		this.answerOne = answerOne;
	}

	public Long getQuestionIdTwo() {
		return questionIdTwo;
	}

	public String getKeyCloakUserId() {
		return keyCloakUserId;
	}

	public void setKeyCloakUserId(String keyCloakUserId) {
		this.keyCloakUserId = keyCloakUserId;
	}

	public void setQuestionIdTwo(Long questionIdTwo) {
		this.questionIdTwo = questionIdTwo;
	}

	public String getAnswerTwo() {
		return answerTwo;
	}

	public void setAnswerTwo(String answerTwo) {
		this.answerTwo = answerTwo;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
