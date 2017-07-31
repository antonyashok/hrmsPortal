package com.tm.common.employee.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "password_hint_master_questions")
public class PasswordHintMasterQuestion implements Serializable {

	private static final long serialVersionUID = 7703973188372658510L;

	@Id
	@Column(name = "question_id")
	private Long questionId;
	
	@Column(name = "question_type")
	private String questionType;
	
	@Column(name = "question")
	private String question;

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getQuestionType() {
		return questionType;
	}

	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}
}
