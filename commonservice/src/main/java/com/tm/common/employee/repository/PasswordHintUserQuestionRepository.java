package com.tm.common.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.common.employee.domain.PasswordHintUserQuestion;

public interface PasswordHintUserQuestionRepository extends JpaRepository<PasswordHintUserQuestion, Long>{

	@Query("select p from PasswordHintUserQuestion p where p.keyCloakUserId=:keyCloakUserId")
	PasswordHintUserQuestion getPasswordHintUserQuestionByKeyCloakUserId(@Param("keyCloakUserId") String keyCloakUserId);
}
