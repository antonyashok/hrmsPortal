package com.tm.common.employee.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.common.employee.domain.PasswordHintUserQuestion;
import com.tm.common.employee.service.dto.PasswordHintUserQuestionDTO;

@Mapper
public interface PasswordHintUserQuestionMapper {

	PasswordHintUserQuestionMapper INSTANCE = Mappers.getMapper(PasswordHintUserQuestionMapper.class);
	
	PasswordHintUserQuestion passwordHintUserQuestionDTOToPasswordHintUserQuestion(PasswordHintUserQuestionDTO passwordHintUserQuestionDTO);
}
