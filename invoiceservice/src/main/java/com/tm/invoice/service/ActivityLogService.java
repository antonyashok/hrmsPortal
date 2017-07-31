package com.tm.invoice.service;

import java.util.List;
import java.util.UUID;

import com.tm.invoice.dto.ActivityLogDTO;

public interface ActivityLogService {

	List<ActivityLogDTO> getActivityLog(UUID id);
}
