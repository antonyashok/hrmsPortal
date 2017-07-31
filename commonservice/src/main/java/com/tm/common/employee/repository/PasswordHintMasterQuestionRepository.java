package com.tm.common.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.common.employee.domain.PasswordHintMasterQuestion;

public interface PasswordHintMasterQuestionRepository extends JpaRepository<PasswordHintMasterQuestion, Long>{
	
}
