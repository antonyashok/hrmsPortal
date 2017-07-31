package com.tm.common.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.tm.common.domain.Country;
import com.tm.common.domain.StateProvince;
import com.tm.common.repository.CountryRepository;
import com.tm.common.repository.HolidayCalendarRepository;
import com.tm.common.repository.HolidayStateProvinceRepository;
import com.tm.common.repository.StateProvinceRepository;
import com.tm.common.service.CommonLookupService;

@Service
public class CommonLookupServiceImpl implements CommonLookupService {

  private StateProvinceRepository stateProvinceRepository;
  private CountryRepository countryRepository;
  private HolidayCalendarRepository holidayCalendarRepository;
  private HolidayStateProvinceRepository holidayStateProvinceRepository;
  
  @Inject
  public CommonLookupServiceImpl(StateProvinceRepository stateProvinceRepository, 
      CountryRepository countryRepository, HolidayCalendarRepository holidayCalendarRepository, 
      HolidayStateProvinceRepository holidayStateProvinceRepository) {
    this.stateProvinceRepository = stateProvinceRepository;
    this.countryRepository = countryRepository;
    this.holidayCalendarRepository = holidayCalendarRepository;
    this.holidayStateProvinceRepository = holidayStateProvinceRepository;
  }
  
  public List<StateProvince> getAllStateProvince(long countryId, UUID holidayCalendarId) {
    List<StateProvince> stateProvinces = stateProvinceRepository.findAllStateProvince(countryId);
    if(CollectionUtils.isNotEmpty(stateProvinces)) {
      if(Objects.nonNull(holidayCalendarId)) {
        List<Long> stateProvinceIds = holidayStateProvinceRepository.stateProvinceIds(holidayCalendarId);
        stateProvinces.forEach(stateProvince -> {
          if (stateProvinceIds.stream().anyMatch(stateProvinceId -> stateProvince.getStateProvinceId() == stateProvinceId)) {
            stateProvince.setIsActiveFlg("Y");
          }
        });
      }
      StateProvince stateProvinceAll = populateAll();
      if (stateProvinces.stream().anyMatch(stateProvince -> stateProvince.getIsActiveFlg().equals("N"))) {
        stateProvinceAll.setIsActiveFlg("N");
      } else {
        stateProvinceAll.setIsActiveFlg("Y");
      }
      stateProvinces.add(0, stateProvinceAll);
    }
    return stateProvinces;
  }
  
  public List<Country> holidaySettingsBasedCountries(UUID holidayCalendarId) {
    List<Country> countries = countryRepository.findAll();
    if(CollectionUtils.isNotEmpty(countries) && Objects.nonNull(holidayCalendarId)) {
      Integer countryId = holidayCalendarRepository.getCountryId(holidayCalendarId);
      countries.forEach(countrie -> {
        if(countryId == countrie.getCountryId()) {
          countrie.setIsActiveFlg("Y");
        }
      });
    }
    return countries;
  }
  
  private StateProvince populateAll() {
    StateProvince stateProvince = new StateProvince();
    stateProvince.setStateProvinceName("ALL");
    stateProvince.setStateProvinceId(0);
    stateProvince.setStateDescription("ALL");
    return stateProvince;
  }
  
}
