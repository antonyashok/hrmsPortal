package com.tm.engagement.service;

import java.util.List;
import java.util.UUID;

import com.tm.engagement.domain.EngagementTask;
import com.tm.engagement.domain.TaskDefinition;
import com.tm.engagement.domain.WeekPlan;
import com.tm.engagement.service.dto.BillingQueueDTO;
import com.tm.engagement.service.dto.EmployeeEngagementTaskDTO;
import com.tm.engagement.service.dto.EngagementContractorsDTO;
import com.tm.engagement.service.dto.EngagementTaskDTO;
import com.tm.engagement.service.dto.TaskDetailDTO;

public interface EmployeeEngagementService {

	BillingQueueDTO createEmployeeEngagement(BillingQueueDTO billingQueueDTO);
    
    String updateBillingProfile(String employeeEngagementId,String status);
    
    String updateBillingProfile(String employeeEngagementId);
    
    List<WeekPlan> getWeekPlan();
    
    List<TaskDefinition> getTaskDetails(String taskName);
    
    void saveEmployeeEngagementTask(EmployeeEngagementTaskDTO employeeEngagementTaskDTO); 
    
    void deleteEmployeeEngagementTask(UUID engagementId,UUID taskId);

    List<EngagementContractorsDTO> checkMappedEmployee(UUID engagementId,UUID taskId);
    
    List<TaskDetailDTO> getAllEngagementTaskDetails(UUID engagementId);
    
    EngagementTask createEngagementTask(EngagementTaskDTO engagementTaskDTO);

}
