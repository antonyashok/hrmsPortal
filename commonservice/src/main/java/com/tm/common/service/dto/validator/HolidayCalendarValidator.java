package com.tm.common.service.dto.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;

import com.tm.common.exception.CommonLookupException;
import com.tm.common.repository.HolidayCalendarRepository;
import com.tm.common.service.dto.HolidayCalendarDTO;
import com.tm.common.service.dto.HolidayCalendarDetailDTO;

public class HolidayCalendarValidator implements
		ConstraintValidator<HolidayCalendar, HolidayCalendarDTO> {

	private HolidayCalendarRepository holidayCalendarRepository;
	private SimpleDateFormat simpleDate = new SimpleDateFormat("MM/dd/yyyy");
	private DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern("MM/dd/yyyy");
	private MessageSourceAccessor accessor;
	private Logger logger = LoggerFactory
			.getLogger(HolidayCalendarValidator.class);

	public HolidayCalendarValidator(
			HolidayCalendarRepository holidayCalendarRepository,
			MessageSource messageSource) {
		this.holidayCalendarRepository = holidayCalendarRepository;
		this.accessor = new MessageSourceAccessor(messageSource);
	}

	@Override
	public void initialize(HolidayCalendar constraintAnnotation) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isValid(HolidayCalendarDTO holidayCalendarDTO,
			ConstraintValidatorContext context) {
		List<com.tm.common.domain.HolidayCalendar> list = null;
		if (Objects.nonNull(holidayCalendarDTO.getHolidayCalendarId())) {
			list = holidayCalendarRepository.findBySettingNameWithId(
					holidayCalendarDTO.getSettingName(),
					holidayCalendarDTO.getHolidayCalendarId());
		} else {
			list = holidayCalendarRepository
					.findBySettingName(holidayCalendarDTO.getSettingName());
		}
		if (CollectionUtils.isNotEmpty(list)) {
			throw new CommonLookupException(
					accessor.getMessage("holiday.settings.name.exists"));
		}
		holidayCalendarDTO
				.getHolidayCalendarDetailDTO()
				.forEach(
						holidayCalendarDetail -> {
							if (null == holidayCalendarDetail
									.getHolidayCalendarDetailId()
									&& stringToDate(
											holidayCalendarDetail
													.getHolidayDate())
                                    .before(stringToDate(simpleDate.format(new Date())))) {
                                throw new CommonLookupException(accessor.getMessage(
                                                "holiday.settings.calendar.details.date.future"));
                            }
							HolidayCalendarDetailDTO holidayCalendarDetailDTO = new HolidayCalendarDetailDTO();
							BeanUtils.copyProperties(holidayCalendarDetail,
									holidayCalendarDetailDTO);
							holidayCalendarDTO
									.getHolidayStateProvinceDTO()
									.forEach(
											holidatStateProvince -> {
												List<UUID> stateProvineIds = null;
												if (Objects
														.nonNull(holidayCalendarDTO
																.getHolidayCalendarId())) {
													stateProvineIds = holidayCalendarRepository
															.stateProvinceBasedHolidayDateWithIdCheck(
																	holidatStateProvince
																			.getStateProvinceId(),
																	stringToDate(holidayCalendarDetailDTO
																			.getHolidayDate()),
																	stringToDate(holidayCalendarDetailDTO
																			.getHolidayEndDate()),
																	holidayCalendarDTO
																			.getHolidayCalendarId());
												} else {
													stateProvineIds = holidayCalendarRepository
															.stateProvinceBasedHolidayDateCheck(
																	holidatStateProvince
																			.getStateProvinceId(),
																	stringToDate(holidayCalendarDetailDTO
																			.getHolidayDate()),
																	stringToDate(holidayCalendarDetailDTO
																			.getHolidayEndDate()));
												}
												if (CollectionUtils
														.isNotEmpty(stateProvineIds)) {
													throw new CommonLookupException(
															String.format(
																	"%s %s",
																	holidayCalendarDetail
																			.getHolidayname(),
																	accessor.getMessage("holiday.settings.holidayDate.mapped")));
												}
											});
						});
		// Cal Details Check
		// Check the Name is duplicate in input holiday calendar details
		List<HolidayCalendarDetailDTO> calendarDetailDTOs = holidayCalendarDTO
				.getHolidayCalendarDetailDTO();
		List<HolidayCalendarDetailDTO> tmpCalendarDetailDTO = new ArrayList<>();
		tmpCalendarDetailDTO.addAll(calendarDetailDTOs);
		calendarDetailDTOs
				.forEach(calendarDetailDTO -> {
					tmpCalendarDetailDTO.remove(calendarDetailDTO);
					boolean exceptionCheck = false;
					if (tmpCalendarDetailDTO.stream()
							.anyMatch(
									calDetailDTO -> calDetailDTO
											.getHolidayname().equals(
													calendarDetailDTO
															.getHolidayname()))) {
						exceptionCheck = true;
					}
					if (exceptionCheck) {
						throw new CommonLookupException(
								accessor.getMessage("holiday.settings.calendar.hoilday.name.exists"));
					}
				});
		Set<LocalDate> hoildayDates = new HashSet<>();
		calendarDetailDTOs
				.forEach(calDetail -> {
					for (LocalDate date = LocalDate.parse(
							calDetail.getHolidayDate(), formatter); !date
							.isAfter(LocalDate.parse(
									calDetail.getHolidayEndDate(), formatter)); date = date
							.plusDays(1)) {
						LocalDate localDate = date;
						if (hoildayDates.stream().anyMatch(
								hoildayDate -> hoildayDate.equals(localDate))) {
							throw new CommonLookupException(
									accessor.getMessage("holiday.settings.calendar.hoilday.date.exists"));
						} else {
							hoildayDates.add(localDate);
						}
					}
				});
		return true;
	}

	private Date stringToDate(String date) {
		try {
			return simpleDate.parse(date);
		} catch (ParseException e) {
			logger.error("String to date Conversion...".concat(e.getMessage()));
			return null;
		}
	}

}
