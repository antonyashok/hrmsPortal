package com.tm.invoice.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.tm.invoice.domain.ActivityLog;
import com.tm.invoice.dto.ActivityLogDTO;

@Mapper
public interface ActivityLogMapper {

  ActivityLogMapper INSTANCE = Mappers.getMapper(ActivityLogMapper.class);

  ActivityLogDTO activityLogToActivityLogDTO(ActivityLog activityLog);

  List<ActivityLogDTO> activityLogsToActivityLogDTOs(List<ActivityLog> activityLogs);
}
