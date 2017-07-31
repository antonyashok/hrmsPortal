package com.tm.engagement.web.rest;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.security.AuthoritiesConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.engagement.domain.EngagementTask;
import com.tm.engagement.domain.Status;
import com.tm.engagement.domain.TaskDefinition;
import com.tm.engagement.domain.WeekPlan;
import com.tm.engagement.service.EmployeeEngagementService;
import com.tm.engagement.service.dto.BillingQueueDTO;
import com.tm.engagement.service.dto.EmployeeEngagementTaskDTO;
import com.tm.engagement.service.dto.EngagementContractorsDTO;
import com.tm.engagement.service.dto.EngagementTaskDTO;
import com.tm.engagement.service.dto.TaskDetailDTO;

@RestController
public class EmployeeEngagementResource {

	private EmployeeEngagementService employeeEngagementService;

	@Inject
	public EmployeeEngagementResource(EmployeeEngagementService employeeEngagementService) {
		this.employeeEngagementService = employeeEngagementService;
	}

	@RequestMapping(value = "/employeeEngagement", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.FINANCE_MANAGER,
			AuthoritiesConstants.FINANCE_REPRESENTATIVE })
	public BillingQueueDTO createEmployeeEngagement(@RequestBody BillingQueueDTO billingQueueDTO) {
		return employeeEngagementService.createEmployeeEngagement(billingQueueDTO);
	}

	@RequestMapping(value = "/employeeEngagement/{status}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.FINANCE_MANAGER,
			AuthoritiesConstants.FINANCE_REPRESENTATIVE,AuthoritiesConstants.PROFILE_VIEW })
	public Status createEmployeeEngagement(@PathVariable String status, @RequestBody BillingQueueDTO billingQueueDTO) {
		String value = employeeEngagementService.updateBillingProfile(billingQueueDTO.getEmployeeEngagementId(),
				status);
		return new Status(value);
	}
	
	@RequestMapping(value = "/employeeEngagement/{employeeEngagementId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.FINANCE_MANAGER,
			AuthoritiesConstants.FINANCE_REPRESENTATIVE,AuthoritiesConstants.PROFILE_VIEW })
	public Status createEmployeeEngagement(@PathVariable String employeeEngagementId) {
		String value = employeeEngagementService.updateBillingProfile(employeeEngagementId);
		return new Status(value);
	}

	@RequestMapping(value = "/weekPlan", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.FINANCE_MANAGER,
			AuthoritiesConstants.FINANCE_REPRESENTATIVE })
	public List<WeekPlan> getWeekPlan() {
		return employeeEngagementService.getWeekPlan();
	}

	@RequestMapping(value = "/taskDetails/{taskName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.FINANCE_MANAGER,
			AuthoritiesConstants.FINANCE_REPRESENTATIVE })
	public List<TaskDefinition> getTaskDetails(@PathVariable String taskName) {
		return employeeEngagementService.getTaskDetails(taskName);
	}
	
	
	@RequestMapping(value = "/employeeEngagement/task", method = RequestMethod.PUT)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<EmployeeEngagementTaskDTO> saveEngagementTask(@RequestBody EmployeeEngagementTaskDTO employeeEngagementTaskDTO) {
		employeeEngagementService.saveEmployeeEngagementTask(employeeEngagementTaskDTO);
		EmployeeEngagementTaskDTO employeeEngagementTaskDetailsDTO =new EmployeeEngagementTaskDTO();
		employeeEngagementTaskDetailsDTO.setTaskName("Success");
		return new ResponseEntity<>(employeeEngagementTaskDetailsDTO, HttpStatus.OK);
	}
	
	
	@RequestMapping(value = "/employeeEngagement/task/{engagementId}", method = RequestMethod.DELETE)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public ResponseEntity<EmployeeEngagementTaskDTO> deleteEngagementTask(@PathVariable UUID engagementId,@RequestParam UUID taskId) {
		employeeEngagementService.deleteEmployeeEngagementTask(engagementId,taskId);
		EmployeeEngagementTaskDTO employeeEngagementTaskDetailsDTO =new EmployeeEngagementTaskDTO();
		employeeEngagementTaskDetailsDTO.setTaskName("Success");
		return new ResponseEntity<>(employeeEngagementTaskDetailsDTO, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/engagementContractors/{engagementId}", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public List<EngagementContractorsDTO> getTaskDetails(@PathVariable UUID engagementId,@RequestParam UUID taskId) {
		return employeeEngagementService.checkMappedEmployee(engagementId,taskId);
	}
	
	@RequestMapping(value = "/engagement/task/{engagementId}", method = RequestMethod.GET)
	@RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
	public List<TaskDetailDTO> getAllEngagementTaskDetails(@PathVariable UUID engagementId) {
		List<TaskDetailDTO> result = employeeEngagementService.getAllEngagementTaskDetails(engagementId);
		return result;
	}
	
    @RequestMapping(value = "/engagement/task", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiredAuthority({AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.FINANCE_MANAGER,
            AuthoritiesConstants.FINANCE_REPRESENTATIVE})
    public EngagementTask createEngagementTask(@RequestBody EngagementTaskDTO engagementTaskDTO) {
        return employeeEngagementService.createEngagementTask(engagementTaskDTO);
    }
		

}
