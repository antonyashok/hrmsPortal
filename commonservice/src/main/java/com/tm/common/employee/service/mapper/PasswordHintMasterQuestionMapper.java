package com.tm.common.employee.service.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.common.employee.domain.PasswordHintMasterQuestion;
import com.tm.common.employee.service.dto.PasswordHintMasterQuestionDTO;

@Mapper
public interface PasswordHintMasterQuestionMapper {

	PasswordHintMasterQuestionMapper INSTANCE = Mappers.getMapper(PasswordHintMasterQuestionMapper.class);
	
	PasswordHintMasterQuestionDTO passwordHintMasterQuestionToPasswordHintMasterQuestionDTO(PasswordHintMasterQuestion passwordHintMasterQuestion);

	List<PasswordHintMasterQuestionDTO> passwordHintMasterQuestionsToPasswordHintMasterQuestionDTOs(List<PasswordHintMasterQuestion> passwordHintMasterQuestions);
}
