package com.tm.common.service;

import java.util.List;
import java.util.UUID;

import com.tm.common.domain.Country;
import com.tm.common.domain.StateProvince;

public interface CommonLookupService {

	List<StateProvince> getAllStateProvince(long countryId,
			UUID holidayCalendarId);

	List<Country> holidaySettingsBasedCountries(UUID holidayCalendarId);
}
