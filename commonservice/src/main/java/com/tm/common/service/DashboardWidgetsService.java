package com.tm.common.service;

import java.text.ParseException;
import java.util.Map;

public interface DashboardWidgetsService {

	Map<String, Object> getMyActiveTasks() throws ParseException;

	Map<String, Object> getBirthdayDetails() throws ParseException;
	
	Map<String, Object> getNewsLetter() throws ParseException;

	Map<String, Object> getAccolades() throws ParseException;
	
	Map<String, Object> getAnnouncement() throws ParseException;
}
