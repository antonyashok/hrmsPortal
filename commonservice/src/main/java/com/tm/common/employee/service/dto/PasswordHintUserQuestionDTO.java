package com.tm.common.employee.service.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PasswordHintUserQuestionDTO implements Serializable {

	private static final long serialVersionUID = -4582064693066701975L;

	private String keyCloakUserId;
	
	private Long questionIdOne;
	
	private String answerOne;
	
	private Long questionIdTwo;
	
	private String answerTwo;
	
	private String password;
	
	private String oldPassword;
	
	private String emailId;
	
	private Boolean isFirstQuestion;

	public String getKeyCloakUserId() {
		return keyCloakUserId;
	}

	public void setKeyCloakUserId(String keyCloakUserId) {
		this.keyCloakUserId = keyCloakUserId;
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

	public void setQuestionIdTwo(Long questionIdTwo) {
		this.questionIdTwo = questionIdTwo;
	}

	public String getAnswerTwo() {
		return answerTwo;
	}

	public void setAnswerTwo(String answerTwo) {
		this.answerTwo = answerTwo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public Boolean getIsFirstQuestion() {
		return isFirstQuestion;
	}

	public void setIsFirstQuestion(Boolean isFirstQuestion) {
		this.isFirstQuestion = isFirstQuestion;
	}
}
