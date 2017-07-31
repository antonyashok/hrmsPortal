package com.tm.common.service.impl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tm.common.domain.HolidayCalendar;
import com.tm.common.domain.HolidayCalendarDetail;
import com.tm.common.domain.HolidaySettingsView;
import com.tm.common.domain.HolidayStateProvince;
import com.tm.common.domain.StateProvince;
import com.tm.common.repository.HolidayCalendarDetailRepository;
import com.tm.common.repository.HolidayCalendarRepository;
import com.tm.common.repository.HolidaySettingsViewRepository;
import com.tm.common.repository.StateProvinceRepository;
import com.tm.common.service.HolidayCalenderService;
import com.tm.common.service.dto.HolidayCalendarDTO;
import com.tm.common.service.dto.HolidayCalendarDetailDTO;
import com.tm.common.service.dto.HolidaySettingsViewDTO;
import com.tm.common.service.dto.HolidayStateProvinceDTO;
import com.tm.common.service.mapper.HolidaySettingsMapper;

@Service
@Transactional
public class HolidayCalenderServiceImpl implements HolidayCalenderService {

	private HolidayCalendarRepository holidayCalendarRepository;

	private HolidayCalendarDetailRepository holidayCalanderDetailRepository;

	private HolidaySettingsViewRepository holidaySettingsViewRepository;
	
	private StateProvinceRepository provinceRepository;

	private DateTimeFormatter formatter = DateTimeFormatter
			.ofPattern("MM/dd/yyyy");
	private SimpleDateFormat simpleDateFormatter = new SimpleDateFormat(
			"MM/dd/yyyy");

	@Inject
	public HolidayCalenderServiceImpl(
			@NotNull HolidayCalendarRepository holidayCalendarRepository,
			@NotNull HolidayCalendarDetailRepository holidayCalanderDetailRepository,
			@NotNull HolidaySettingsViewRepository holidaySettingsViewRepository, 
			@NotNull StateProvinceRepository provinceRepository) {
		this.holidayCalendarRepository = holidayCalendarRepository;
		this.holidayCalanderDetailRepository = holidayCalanderDetailRepository;
		this.holidaySettingsViewRepository = holidaySettingsViewRepository;
		this.provinceRepository = provinceRepository;
	}

	@Transactional(readOnly = false)
	@Override
	public HolidayCalendarDTO saveHolidayCalender(
			HolidayCalendarDTO holidayCalendarDTO) {
		holidayCalendarDTO.setCreatedBy(1L);
		HolidayCalendar holidayCalendar = HolidaySettingsMapper.INSTANCE
				.holidayCalendarDTOToHolidayCalendar(holidayCalendarDTO);
		holidayCalendar.setSettingName(holidayCalendar.getSettingName().trim());
		prepareHolidayCalendarDetailDTO(holidayCalendar,
				holidayCalendarDTO.getHolidayCalendarDetailDTO());
		prepareHolidayStateProvince(holidayCalendar,
				holidayCalendarDTO.getHolidayStateProvinceDTO());
		holidayCalendar = holidayCalendarRepository.save(holidayCalendar);
		HolidayCalendarDTO calendarDTO = HolidaySettingsMapper.INSTANCE
				.holidayCalendarToHolidayCalendarDTO(holidayCalendar);
		List<HolidayCalendarDetailDTO> calendarDetailDTOs = getHolidayCalendarDetail(holidayCalendar
				.getHolidayCalendarDetail());
		calendarDTO.setHolidayCalendarDetailDTO(calendarDetailDTOs);
		calendarDTO.setHolidayStateProvinceDTO(HolidaySettingsMapper.INSTANCE
				.holidayStateProvinceToHolidayStateProvinceDTO(holidayCalendar
						.getHolidayStateProvince()));
		return calendarDTO;
	}

	private void prepareHolidayCalendarDetailDTO(
			HolidayCalendar holidayCalendar,
			List<HolidayCalendarDetailDTO> calendarDetailDTOs) {
		List<HolidayCalendarDetail> holidayCalendarDetails = new ArrayList<>();
		if (Objects.nonNull(holidayCalendar.getHolidayCalendarId())) {
			deleteHolidayCalendarDetailByHolidayCalendarId(holidayCalendar
					.getHolidayCalendarId());
		}
		calendarDetailDTOs
				.forEach(holidayCalendarDetailDTO -> {
					holidayCalendarDetailDTO.setHolidayCalendarDetailId(null);
					for (LocalDate date = LocalDate.parse(
							holidayCalendarDetailDTO.getHolidayDate(),
							formatter); !date.isAfter(LocalDate.parse(
							holidayCalendarDetailDTO.getHolidayEndDate(),
							formatter)); date = date.plusDays(1)) {
						holidayCalendarDetailDTO.setHolidayDate(formatter
								.format(date));
						holidayCalendarDetailDTO.setCreatedBy(1L);
						HolidayCalendarDetail holidayCalendarDetail = HolidaySettingsMapper.INSTANCE
								.holidayCalendarDetailDTOToHolidayCalendarDetail(holidayCalendarDetailDTO);
						holidayCalendarDetail.setHolidayname(holidayCalendarDetail.getHolidayname().trim());
						holidayCalendarDetail
								.setHolidayCalendar(holidayCalendar);
						holidayCalendarDetails.add(holidayCalendarDetail);
					}
				});
		holidayCalendar.setHolidayCalendarDetail(holidayCalendarDetails);
	}

	private void prepareHolidayStateProvince(HolidayCalendar holidayCalendar,
			List<HolidayStateProvinceDTO> holidayStateProvinceDTOs) {
		List<HolidayStateProvince> provinceDTOs = new ArrayList<>();
		holidayStateProvinceDTOs
				.forEach(holidayStateProvinceDTO -> {
					if (holidayStateProvinceDTO.getStateProvinceId() != 0) {
						HolidayStateProvince holidayStateProvince = HolidaySettingsMapper.INSTANCE
								.holidayStateProvinceDTOToHolidayStateProvince(holidayStateProvinceDTO);
						holidayStateProvince
								.setHolidayCalendar(holidayCalendar);
						provinceDTOs.add(holidayStateProvince);
					} else if(holidayStateProvinceDTO.getStateProvinceId() == 0){
						List<StateProvince> stateProvinces = provinceRepository.findAllStateProvince(holidayCalendar.getCountryId());
						stateProvinces.forEach(stateProvince -> {
							HolidayStateProvinceDTO stateProvinceDTO = new HolidayStateProvinceDTO();
							stateProvinceDTO.setStateProvinceId(stateProvince.getStateProvinceId());
							HolidayStateProvince holidayStateProvince = HolidaySettingsMapper.INSTANCE
									.holidayStateProvinceDTOToHolidayStateProvince(stateProvinceDTO);
							holidayStateProvince
									.setHolidayCalendar(holidayCalendar);
							provinceDTOs.add(holidayStateProvince);
						});
					}
				});
		holidayCalendar.setHolidayStateProvince(provinceDTOs);
	}

	@Override
	public HolidayCalendarDTO getHolidayCalendarDTO(UUID holidayCalendarId) {
		HolidayCalendar holidayCalendar = holidayCalendarRepository
				.findOne(holidayCalendarId);
		HolidayCalendarDTO calendarDTO = HolidaySettingsMapper.INSTANCE
				.holidayCalendarToHolidayCalendarDTO(holidayCalendar);
		List<HolidayCalendarDetailDTO> calendarDetailDTOs = getHolidayCalendarDetail(holidayCalendar
				.getHolidayCalendarDetail());
		calendarDTO.setHolidayCalendarDetailDTO(calendarDetailDTOs);
		calendarDTO.setHolidayStateProvinceDTO(HolidaySettingsMapper.INSTANCE
				.holidayStateProvinceToHolidayStateProvinceDTO(holidayCalendar
						.getHolidayStateProvince()));
		return calendarDTO;
	}

	private List<HolidayCalendarDetailDTO> getHolidayCalendarDetail(
			List<HolidayCalendarDetail> calendarDetails) {
		Map<String, List<HolidayCalendarDetail>> calendarDetailMap = calendarDetails
				.stream()
				.collect(
						Collectors
								.groupingBy(HolidayCalendarDetail::getHolidayname));
		List<HolidayCalendarDetailDTO> holidayCalendarDetailsDTO = new ArrayList<>();
		calendarDetailMap
				.forEach((key, values) -> {
					Date maxDate = values.stream()
							.map(HolidayCalendarDetail::getHolidayDate)
							.max(Date::compareTo).get();
					Date minDate = values
							.stream()
							.map(HolidayCalendarDetail::getHolidayDate)
							.min(Date::compareTo).get();
					List<HolidayCalendarDetailDTO> tmpHolidayCalDetailsDTO = new ArrayList<>();
					tmpHolidayCalDetailsDTO = HolidaySettingsMapper.INSTANCE
							.holidayCalendarDetailToHolidayCalendarDetailDTO(values
									.stream()
									.filter(calendarDetail -> calendarDetail
											.getHolidayDate()
											.equals(values
													.stream()
													.map(HolidayCalendarDetail::getHolidayDate)
													.min(Date::compareTo).get()))
									.collect(Collectors.toList()));
					tmpHolidayCalDetailsDTO.get(0).setHolidayEndDate(
							simpleDateFormatter.format(maxDate));
					
					if(minDate.after(new Date())) {
						tmpHolidayCalDetailsDTO.get(0).setActiveFlag(true);
					}
					List<UUID> holidayDetailIds = values
							.stream()
							.map(HolidayCalendarDetail::getHolidayCalendarDetailId)
							.collect(Collectors.toList());
					tmpHolidayCalDetailsDTO.get(0).setHolidayDetailIds(
							holidayDetailIds);
					holidayCalendarDetailsDTO.addAll(tmpHolidayCalDetailsDTO);
				});
		return holidayCalendarDetailsDTO;
	}

	@Override
	public void deleteHolidayCalenderDetail(UUID holidayCalendarDetailId) {
		holidayCalanderDetailRepository.delete(holidayCalendarDetailId);
	}

	@Override
	public void deleteHolidayCalenderDetail(List<UUID> holidayCalendarDetailIds) {
		holidayCalanderDetailRepository
				.deleteHolidayCalendarDetailsByIds(holidayCalendarDetailIds);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<HolidaySettingsViewDTO> getAllHolidaySettings(Pageable pageable) {
		Pageable pageableRequest = pageable;

		if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
			pageableRequest = new PageRequest(pageable.getPageNumber(),
					pageable.getPageSize(), Sort.Direction.DESC,
					"lastModifiedDate");
		}
		Page<HolidaySettingsView> holidaySettingsView = holidaySettingsViewRepository
				.findAll(pageableRequest);
		List<HolidaySettingsViewDTO> result = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(holidaySettingsView.getContent())) {
			for (HolidaySettingsView holidaySettings : holidaySettingsView
					.getContent()) {
				HolidaySettingsViewDTO holidaySettingsViewDTO = HolidaySettingsMapper.INSTANCE
						.holidaySettingsViewToHolidaySettingsViewDTO(holidaySettings);
				result.add(holidaySettingsViewDTO);
			}
		}
		return new PageImpl<>(result, pageable,
				holidaySettingsView.getTotalElements());
	}

	private void deleteHolidayCalendarDetailByHolidayCalendarId(
			UUID calendarDetailId) {
		holidayCalanderDetailRepository
				.deleteHolidayCalendarDetailByHolidayCalendarId(calendarDetailId);
	}
}
