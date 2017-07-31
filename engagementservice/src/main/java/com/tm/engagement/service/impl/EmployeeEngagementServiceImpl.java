package com.tm.engagement.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.tm.engagement.constants.EngagementConstants;
import com.tm.engagement.domain.EmployeeBillingProfile;
import com.tm.engagement.domain.EmployeeEngagement;
import com.tm.engagement.domain.EmployeeEngagementTaskMap;
import com.tm.engagement.domain.EmployeeTaskDetailView;
import com.tm.engagement.domain.Engagement;
import com.tm.engagement.domain.Engagement.ActiveFlagEnum;
import com.tm.engagement.domain.EngagementContractors;
import com.tm.engagement.domain.EngagementTask;
import com.tm.engagement.domain.Task;
import com.tm.engagement.domain.TaskDefinition;
import com.tm.engagement.domain.WeekPlan;
import com.tm.engagement.exception.EngagementException;
import com.tm.engagement.exception.TaskExistException;
import com.tm.engagement.exception.TaskGroupException;
import com.tm.engagement.repository.EmployeeBillingProfileRepository;
import com.tm.engagement.repository.EmployeeEngagementRepository;
import com.tm.engagement.repository.EmployeeEngagementTaskMapRepository;
import com.tm.engagement.repository.EmployeeTaskDetailViewRepository;
import com.tm.engagement.repository.EngagementContractorsRepository;
import com.tm.engagement.repository.EngagementRepository;
import com.tm.engagement.repository.EngagementTaskRepository;
import com.tm.engagement.repository.TaskDefinitionRepository;
import com.tm.engagement.repository.TaskRepository;
import com.tm.engagement.repository.WeekPlanRepository;
import com.tm.engagement.service.EmployeeEngagementService;
import com.tm.engagement.service.dto.BillingQueueDTO;
import com.tm.engagement.service.dto.EmployeeEngagementTaskDTO;
import com.tm.engagement.service.dto.EngagementContractorsDTO;
import com.tm.engagement.service.dto.EngagementTaskDTO;
import com.tm.engagement.service.dto.TaskDetailDTO;
import com.tm.engagement.service.mapper.EngagementMapper;
import com.tm.engagement.web.rest.util.DateUtil;

@Service
public class EmployeeEngagementServiceImpl implements EmployeeEngagementService {
	
	private static final String ERR_IN_SAVE_TASK = "save task has been failed.";
    
    private static final String ERR_TASK_EXIST = "Task name already exist '";

	private EmployeeEngagementRepository employeeEngagementRepository;

	private EngagementRepository engagementRepository;

	private WeekPlanRepository weekPlanRepository;

	private EmployeeBillingProfileRepository employeeBillingProfileRepository;

	private EngagementTaskRepository engagementTaskRepository;

	private TaskDefinitionRepository taskDefinitionRepository;

	private EmployeeEngagementTaskMapRepository employeeEngagementTaskMapRepository;
	
	private TaskRepository taskRepository;
	
	private EngagementContractorsRepository engagementContractorsRepository;
	
	private EmployeeTaskDetailViewRepository employeeTaskDetailViewRepository;

	@Inject
	public EmployeeEngagementServiceImpl(EmployeeEngagementRepository employeeEngagementRepository,
			EngagementRepository engagementRepository, WeekPlanRepository weekPlanRepository,
			EmployeeBillingProfileRepository employeeBillingProfileRepository,
			EngagementTaskRepository engagementTaskRepository, TaskDefinitionRepository taskDefinitionRepository,
			EmployeeEngagementTaskMapRepository employeeEngagementTaskMapRepository,
			TaskRepository taskRepository,EngagementContractorsRepository engagementContractorsRepository,
			EmployeeTaskDetailViewRepository employeeTaskDetailViewRepository) {
		this.employeeEngagementRepository = employeeEngagementRepository;
		this.engagementRepository = engagementRepository;
		this.weekPlanRepository = weekPlanRepository;
		this.employeeBillingProfileRepository = employeeBillingProfileRepository;
		this.engagementTaskRepository = engagementTaskRepository;
		this.taskDefinitionRepository = taskDefinitionRepository;
		this.employeeEngagementTaskMapRepository = employeeEngagementTaskMapRepository;
		this.taskRepository = taskRepository;
		this.engagementContractorsRepository = engagementContractorsRepository;
		this.employeeTaskDetailViewRepository = employeeTaskDetailViewRepository;
	}

	@Override
	public BillingQueueDTO createEmployeeEngagement(BillingQueueDTO billingQueueDTO) {
		return saveTaskDefenition(billingQueueDTO);
	}

	@Override
	public List<WeekPlan> getWeekPlan() {
		return weekPlanRepository.findAll();
	}

	@Override
	public List<TaskDefinition> getTaskDetails(String taskName) {
		return taskDefinitionRepository.findByTaskNameLike(taskName + "%");
	}

	private BillingQueueDTO saveTaskDefenition(BillingQueueDTO billingQueueDTO) {
		Engagement engagement = engagementRepository.findOne(UUID.fromString(billingQueueDTO.getEngagementId()));
		EmployeeEngagement employeeEngagementDetails = new EmployeeEngagement();
		if (null != billingQueueDTO.getEmployeeEngagementId()
				&& StringUtils.isNotEmpty(billingQueueDTO.getEmployeeEngagementId())) {
			employeeEngagementDetails = employeeEngagementRepository
					.findByEmployeeEngagamentIdAndActiveFlag(UUID.fromString(billingQueueDTO.getEmployeeEngagementId()),"Y");
		}
		employeeEngagementDetails = checkIfEngmtNull(billingQueueDTO, engagement, employeeEngagementDetails);
		EmployeeBillingProfile employeeBillingProfileDetails = employeeBillingProfileRepository
				.findByEmployeeEngagementEmployeeEngagamentId(employeeEngagementDetails.getEmployeeEngagamentId());
		if (null == employeeBillingProfileDetails) {
			employeeBillingProfileDetails = new EmployeeBillingProfile();
		}
		employeeBillingProfileDetails.setActiveFlag("Y");
		employeeBillingProfileDetails.setApContactId(billingQueueDTO.getAccountManagerId());
		employeeBillingProfileDetails.setBillingSpecialistEmployeeId(engagement.getFinanceRepId());
		employeeBillingProfileDetails.setBillToClientId(billingQueueDTO.getBillToClientId());
		employeeBillingProfileDetails.setBillTypeLookupId(billingQueueDTO.getBillTypeLookupId());
		chkMaxCappedForDTHours(billingQueueDTO, employeeBillingProfileDetails);
		chkMaxCappedForOTHours(billingQueueDTO, employeeBillingProfileDetails);
		chkMaxCappedSTHours(billingQueueDTO, employeeBillingProfileDetails);
		employeeBillingProfileDetails.setClientManagerId(0L);
		employeeBillingProfileDetails.setEmplId(billingQueueDTO.getEmployeeId());
		employeeBillingProfileDetails.setEmployeeEngagement(employeeEngagementDetails);
		employeeBillingProfileDetails.setProcessFeeRatePct(billingQueueDTO.getProcessingFeeRate());
		employeeBillingProfileDetails.setProcessReferralFee(billingQueueDTO.getReferalsFees());
		employeeBillingProfileDetails
				.setProfileActivateDate(DateUtil.convertStringToDate(billingQueueDTO.getProfileActiveDate()));
		employeeBillingProfileDetails.setTimeRuleId(billingQueueDTO.getTimesheetRuleLookUpId());
		employeeBillingProfileDetails.setRateTypeLookupId(billingQueueDTO.getTimesheetTypeLookUpId());
		employeeBillingProfileDetails.setTimesheetEntryLookupId(billingQueueDTO.getTimesheetTypeLookUpId());
		employeeBillingProfileDetails.setTimesheetMethodLookupId(billingQueueDTO.getTimesheetMethodLookUpId());
		employeeBillingProfileDetails.setPoExistFlag("Y");
		employeeBillingProfileDetails.setPurchaseOrderId(billingQueueDTO.getPurchaseOrderId());
		employeeBillingProfileDetails.setEndClientId(0L);
		employeeBillingProfileDetails.setCreatedBy(0L);
		employeeBillingProfileDetails.setLastModifiedBy(0L);
		employeeBillingProfileDetails
				.setContractStartDate(DateUtil.convertStringToDate(billingQueueDTO.getEffectiveStartDate()));
		employeeBillingProfileDetails
				.setContractEndDate(DateUtil.convertStringToDate(billingQueueDTO.getEffectiveEndDate()));
		employeeBillingProfileRepository.save(employeeBillingProfileDetails);
		billingQueueDTO.setEmployeeEngagementId(employeeEngagementDetails.getEmployeeEngagamentId().toString());
		return billingQueueDTO;
	}

	private void chkMaxCappedSTHours(BillingQueueDTO billingQueueDTO, EmployeeBillingProfile employeeBillingProfile) {
		if (null != billingQueueDTO.getCappedMaxDTHours()) {
			employeeBillingProfile.setCappedMaxSTHrs(billingQueueDTO.getCappedMaxSTHours());
		} else {
			employeeBillingProfile.setCappedMaxSTHrs(0);
		}
	}

	private void chkMaxCappedForOTHours(BillingQueueDTO billingQueueDTO,
			EmployeeBillingProfile employeeBillingProfile) {
		if (null != billingQueueDTO.getCappedMaxDTHours()) {
			employeeBillingProfile.setCappedMaxOTHrs(billingQueueDTO.getCappedMaxOTHours());
		} else {
			employeeBillingProfile.setCappedMaxOTHrs(0);
		}
	}

	private void chkMaxCappedForDTHours(BillingQueueDTO billingQueueDTO, EmployeeBillingProfile employeeBillingProfile) {
		if (null != billingQueueDTO.getCappedMaxDTHours()) {
			employeeBillingProfile.setCappedMaxDTHrs(billingQueueDTO.getCappedMaxDTHours());
		} else {
			employeeBillingProfile.setCappedMaxDTHrs(0);
		}
	}

	private EmployeeEngagement checkIfEngmtNull(BillingQueueDTO billingQueueDTO, Engagement engagement,
			EmployeeEngagement employeeEngagementDetails) {
		if (Objects.isNull(employeeEngagementDetails)) {
			employeeEngagementDetails = new EmployeeEngagement();
		}
		employeeEngagementDetails.setEmployeeId(billingQueueDTO.getEmployeeId());
		employeeEngagementDetails.setEngagement(engagement);
		employeeEngagementDetails.setAccountManagerEmployeeId(billingQueueDTO.getAccountManagerId());
		employeeEngagementDetails.setActiveFlag("Y");
		employeeEngagementDetails
				.setEffectiveStartDate(DateUtil.convertStringToDate(billingQueueDTO.getEffectiveStartDate()));
		employeeEngagementDetails
				.setEffectiveEndDate(DateUtil.convertStringToDate(billingQueueDTO.getEffectiveEndDate()));
		WeekPlan weekPlan = weekPlanRepository.findOne(UUID.fromString(billingQueueDTO.getWeekPlanId()));
		employeeEngagementDetails.setWeekPlan(weekPlan);
		employeeEngagementDetails.setCustManagerEmployeeId(0L);
		employeeEngagementDetails.setRecruiterEmployeeId(0L);
		employeeEngagementDetails.setCreatedBy(0L);
		employeeEngagementDetails.setLastModifiedBy(0L);
		employeeEngagementDetails.setContractorEmailId(billingQueueDTO.getContractorMailId());
		employeeEngagementDetails.setContractorEmployeeId(billingQueueDTO.getContractorId());
		employeeEngagementDetails.setOfficeId(billingQueueDTO.getOfficeId());
		employeeEngagementDetails.setTimesheetGeneratedFlag("N");
		employeeEngagementDetails = employeeEngagementRepository.save(employeeEngagementDetails);
		return employeeEngagementDetails;
	}

	@Override
	public String updateBillingProfile(String employeeEngagementId, String status) {

		EmployeeEngagement employeeEngagementDetails = employeeEngagementRepository
				.findOne(UUID.fromString(employeeEngagementId));
		if (null == employeeEngagementDetails) {
			throw new EngagementException("Not Exist");
		}

		EmployeeBillingProfile employeeBillingProfileDetails = employeeBillingProfileRepository
				.findByEmployeeEngagementEmployeeEngagamentId(UUID.fromString(employeeEngagementId));
		EmployeeEngagementTaskMap employeeEngagementTaskMapDetails = employeeEngagementTaskMapRepository
				.findByEmployeeEngagementEmployeeEngagamentId(UUID.fromString(employeeEngagementId));

		if (status.equals(EngagementConstants.ACTIVE)) {
			employeeEngagementDetails.setActiveFlag("Y");
			employeeBillingProfileDetails.setActiveFlag("Y");
			if (null != employeeEngagementTaskMapDetails) {
				employeeEngagementTaskMapDetails.setActiveFlag(ActiveFlagEnum.Y);
			}
		} else {
			employeeEngagementDetails.setActiveFlag("N");
			employeeBillingProfileDetails.setActiveFlag("N");
			if (employeeBillingProfileDetails.getContractEndDate().after(new Date())) {
				employeeEngagementDetails.setEffectiveEndDate(new Date());
				employeeBillingProfileDetails.setContractEndDate(new Date());
			}
			if (null != employeeEngagementTaskMapDetails) {
				employeeEngagementTaskMapDetails.setActiveFlag(ActiveFlagEnum.N);
			}
		}

		employeeEngagementRepository.save(employeeEngagementDetails);
		employeeBillingProfileRepository.save(employeeBillingProfileDetails);
		employeeEngagementTaskMapRepository.save(employeeEngagementTaskMapDetails);
		return "OK";
	}
	
	@Override
	public String updateBillingProfile(String employeeEngagementId) {

		EmployeeEngagement employeeEngagementDetails = employeeEngagementRepository
				.findByEmployeeEngagamentIdAndActiveFlag(UUID.fromString(employeeEngagementId),"Y");
		if (null == employeeEngagementDetails) {
			throw new EngagementException("Not Exist");
		}

		EmployeeBillingProfile employeeBillingProfileDetails = employeeBillingProfileRepository
				.findByEmployeeEngagementEmployeeEngagamentIdAndActiveFlag(UUID.fromString(employeeEngagementId),"Y");
		

		employeeEngagementDetails.setActiveFlag("N");
		employeeBillingProfileDetails.setActiveFlag("N");
		if (employeeBillingProfileDetails.getContractEndDate().after(new Date())) {
			employeeEngagementDetails.setEffectiveEndDate(new Date());
			employeeBillingProfileDetails.setContractEndDate(new Date());
		}

		employeeEngagementRepository.save(employeeEngagementDetails);
		employeeBillingProfileRepository.save(employeeBillingProfileDetails);
		return "OK";
	}
	
	 public void saveEmployeeEngagementTask(EmployeeEngagementTaskDTO employeeEngagementTaskDTO) {
	        Task taskDetails = new Task();
	        if (null == employeeEngagementTaskDTO.getTaskId()) {
	            taskDetails.setTaskName(employeeEngagementTaskDTO.getTaskName());
	            taskDetails.setTaskDescription(employeeEngagementTaskDTO.getTaskDescription());
	            taskDetails = taskRepository.save(taskDetails);
	            employeeEngagementTaskDTO.setTaskId(taskDetails.getTaskId());
	        } else {
	            taskDetails = taskRepository.findOne(employeeEngagementTaskDTO.getTaskId());
	            taskDetails.setTaskName(employeeEngagementTaskDTO.getTaskName());
	            taskDetails.setTaskDescription(employeeEngagementTaskDTO.getTaskDescription());
	            taskDetails.setTaskId(employeeEngagementTaskDTO.getTaskId());
	            taskRepository.save(taskDetails);
	        }
	        List<EngagementTask> engagementTaskList =engagementTaskRepository.findByEngagementIdAndTaskId(employeeEngagementTaskDTO.getEngagementId(),taskDetails.getTaskId());              
	        if(CollectionUtils.isEmpty(engagementTaskList)){ 
	        	EngagementTaskDTO engagementTaskDTO = new EngagementTaskDTO();
	        	engagementTaskDTO.setEngagementId(employeeEngagementTaskDTO.getEngagementId());
	        	engagementTaskDTO.setTaskId(taskDetails.getTaskId());
	        	engagementTaskDTO.setTaskName(taskDetails.getTaskName());
	            createEngagementTask(engagementTaskDTO);      
	        }
	        List<EmployeeEngagementTaskMap> employeeEngagementTaskMapList = new ArrayList<>();
	        List<EmployeeEngagementTaskMap> removeEmployeeEngagementTaskMapList = new ArrayList<>();
	        for (EngagementContractorsDTO contractors : employeeEngagementTaskDTO.getContractorList()) {
	            if (contractors.getCheckFlag()) {
	                EmployeeEngagementTaskMap employeeEngagementTaskMap = new EmployeeEngagementTaskMap();
	                EmployeeEngagement employeeEngagement = employeeEngagementRepository
	                        .findByEmployeeIdAndEngagementEngagementIdAndActiveFlag(contractors.getEmployeeId(),
	                                contractors.getEngagementId(), "Y");
	                employeeEngagementTaskMap.setActiveFlag(ActiveFlagEnum.Y);
	                employeeEngagementTaskMap.setTask(taskDetails);
	                employeeEngagementTaskMap.setEmployeeEngagement(employeeEngagement);
	                employeeEngagementTaskMap.setCreatedBy(0L);
	                employeeEngagementTaskMap.setLastModifiedBy(0L);
	                employeeEngagementTaskMap.setTimesheetGeneratedFlag("N");
	                List<EmployeeEngagementTaskMap> checkEmployeeEngagementTaskMapList = employeeEngagementTaskMapRepository
	                        .findByEmployeeEngagementEmployeeEngagamentIdAndTaskTaskId(
	                                contractors.getEmployeeengagementId(), employeeEngagementTaskDTO.getTaskId());
	                if (CollectionUtils.isEmpty(checkEmployeeEngagementTaskMapList)) {
	                    employeeEngagementTaskMapList.add(employeeEngagementTaskMap);
	                }
	            } else {
	                List<EmployeeEngagementTaskMap> checkEmployeeEngagementTaskMapList = employeeEngagementTaskMapRepository
	                        .findByEmployeeEngagementEmployeeEngagamentIdAndTaskTaskId(
	                                contractors.getEmployeeengagementId(), employeeEngagementTaskDTO.getTaskId());
	                if (CollectionUtils.isNotEmpty(checkEmployeeEngagementTaskMapList)) {
	                    removeEmployeeEngagementTaskMapList.addAll(checkEmployeeEngagementTaskMapList);
	                }
	            }
	        }
	        employeeEngagementTaskMapRepository.save(employeeEngagementTaskMapList);
	        employeeEngagementTaskMapRepository.delete(removeEmployeeEngagementTaskMapList);
	    }
	    
	    public void deleteEmployeeEngagementTask(UUID engagementId, UUID taskId) {
	        if (null != taskId) {
	            List<EmployeeTaskDetailView> employeeTaskDetailViewList = employeeTaskDetailViewRepository
	                    .findByEngagementIdAndTaskId(engagementId, taskId);
	            List<EmployeeEngagementTaskMap> employeeEngagementTaskList = new ArrayList<>();
	            if (CollectionUtils.isNotEmpty(employeeTaskDetailViewList) && StringUtils.isNotEmpty(employeeTaskDetailViewList.get(0).getEmployeeId())) {
	                String[] employeeArr = employeeTaskDetailViewList.get(0).getEmployeeId().split(",");
	                List<String> employeeList = new ArrayList<>(Arrays.asList(employeeArr));
	                List<EmployeeEngagementTaskMap> employeeEngagementTaskMapList = new ArrayList<>();
	                for (String employee : employeeList) {
	                    EmployeeEngagement employeeEngagement = employeeEngagementRepository
	                            .findByEmployeeIdAndEngagementEngagementIdAndActiveFlag(Long.valueOf(employee),
	                                    engagementId, "Y");
	                    employeeEngagementTaskMapList = employeeEngagementTaskMapRepository
	                            .findByEmployeeEngagementEmployeeEngagamentIdAndTaskTaskId(
	                                    employeeEngagement.getEmployeeEngagamentId(), taskId);
	                    employeeEngagementTaskList.addAll(employeeEngagementTaskMapList);
	                }
	            }
	            employeeEngagementTaskMapRepository.delete(employeeEngagementTaskList);
	            List<EngagementTask> engagementTaskList = engagementTaskRepository.findByEngagementIdAndTaskId(engagementId,taskId);
	            engagementTaskRepository.delete(engagementTaskList);
	        }
	        
	    }

	    @Override
	    public List<EngagementContractorsDTO> checkMappedEmployee(UUID engagementId, UUID taskId) {
	        List<EngagementContractors> engagementContractorsList = engagementContractorsRepository.findByEngagementId(engagementId);
	        List<EngagementContractorsDTO> engagementContractorsDTOList = new ArrayList<>();
	        if(CollectionUtils.isNotEmpty(engagementContractorsList)){
	            engagementContractorsDTOList = EngagementMapper.INSTANCE.engagementContractorsListToengagementContractorsDTOList(engagementContractorsList);
	            engagementContractorsDTOList.forEach(engagementContractorsDetails ->{
	            List<EmployeeEngagementTaskMap> employeeEngagementTaskMapList = employeeEngagementTaskMapRepository.findByEmployeeEngagementEmployeeEngagamentIdAndTaskTaskId(engagementContractorsDetails.getEmployeeengagementId(), taskId);
	            if(CollectionUtils.isNotEmpty(employeeEngagementTaskMapList)){
	                engagementContractorsDetails.setCheckFlag(true);
	            }else{
	                engagementContractorsDetails.setCheckFlag(false);
	            }
	        });
	        }
	        return engagementContractorsDTOList;
	    }
	    

	    @Override
	    public List<TaskDetailDTO> getAllEngagementTaskDetails(UUID engagementId) {
	        List<TaskDetailDTO> engagementMappedTaskList = new ArrayList<>();
	        List<EngagementTask> engagementTaskDetailsList =
	                engagementTaskRepository.findByEngagementId(engagementId);
	        if (CollectionUtils.isNotEmpty(engagementTaskDetailsList)) {
	            for (EngagementTask engagementTask : engagementTaskDetailsList) {
	                List<EmployeeTaskDetailView> employeeTaskDetailViewList = employeeTaskDetailViewRepository
	                        .findByEngagementIdAndTaskId(engagementId, engagementTask.getTaskId());
	                TaskDetailDTO taskDTO = new TaskDetailDTO();
	                if (CollectionUtils.isNotEmpty(employeeTaskDetailViewList)) {
	                	 taskDTO.setTaskId(employeeTaskDetailViewList.get(0).getTaskId());
		                 taskDTO.setTaskName(employeeTaskDetailViewList.get(0).getTaskName());
		                 taskDTO.setTaskDescription(employeeTaskDetailViewList.get(0).getTaskDesc());
		                 taskDTO.setEmployeeId(employeeTaskDetailViewList.get(0).getEmployeeId());
		                 taskDTO.setEmployeeName(employeeTaskDetailViewList.get(0).getEmployeeName());
		                 engagementMappedTaskList.add(taskDTO);
	                } else {
	                	Task task =  taskRepository.findOne(engagementTask.getTaskId());
	                    taskDTO.setTaskId(task.getTaskId());
	                    taskDTO.setTaskName(task.getTaskName());
	                    taskDTO.setTaskDescription(task.getTaskDescription());
	                    taskDTO.setEmployeeId("");
	                    taskDTO.setEmployeeName("");
	                    engagementMappedTaskList.add(taskDTO);
	                }
	            }
	        }
	        return engagementMappedTaskList;
	    }
	    
	    @Override
		public EngagementTask createEngagementTask(EngagementTaskDTO engagementTaskDTO) {
			Engagement engagement = engagementRepository.findOne(engagementTaskDTO.getEngagementId());
			Task task = taskRepository.findByTaskId(engagementTaskDTO.getTaskId());
			List<EngagementTask> engagementTaskList = engagementTaskRepository
					.findByEngagementIdAndTaskName(engagementTaskDTO.getEngagementId(), engagementTaskDTO.getTaskName());
			if (CollectionUtils.isNotEmpty(engagementTaskList)) {
				throw new TaskExistException(ERR_TASK_EXIST + engagementTaskDTO.getTaskName() + "'");
			}
			if (Objects.nonNull(engagement) && Objects.nonNull(task)) {
				EngagementTask engagementTask = new EngagementTask();
				engagementTask.setEngagementId(engagementTaskDTO.getEngagementId());
				engagementTask.setTaskId(engagementTaskDTO.getTaskId());
				engagementTask.setTaskName(engagementTaskDTO.getTaskName());
				engagementTask.setCreatedBy(0L);
				return engagementTaskRepository.save(engagementTask);
			} else {
				throw new TaskGroupException(ERR_IN_SAVE_TASK);
			}

		}
}
