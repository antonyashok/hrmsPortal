package com.tm.common.service.dto.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HolidayCalendarValidator.class)
public @interface HolidayCalendar {

	String message() default "{Holiday calender is not valid}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
