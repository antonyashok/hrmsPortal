package com.tm.invoice.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.tm.invoice.domain.ActivityLog;
import com.tm.invoice.dto.ActivityLogDTO;
import com.tm.invoice.mapper.ActivityLogMapper;
import com.tm.invoice.mongo.repository.ActivityLogRepository;
import com.tm.invoice.service.ActivityLogService;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {

	private ActivityLogRepository activityLogRepository;

	@Inject
	public ActivityLogServiceImpl(ActivityLogRepository activityLogRepository) {
		this.activityLogRepository = activityLogRepository;
	}

	@Override
	public List<ActivityLogDTO> getActivityLog(UUID id) {
		List<ActivityLog> activityLogs = activityLogRepository
				.findBySourceReferenceIdOrderByUpdatedOnDesc(id);
		if (CollectionUtils.isNotEmpty(activityLogs)) {
			return ActivityLogMapper.INSTANCE
					.activityLogsToActivityLogDTOs(activityLogs);
		}
		return Collections.<ActivityLogDTO> emptyList();
	}
}
