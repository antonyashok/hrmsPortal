package com.tm.engagement.service.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.tm.engagement.domain.EmployeeBillingProfile;
import com.tm.engagement.domain.EmployeeEngagement;
import com.tm.engagement.domain.EmployeeEngagementTaskMap;
import com.tm.engagement.domain.EmployeeTaskDetailView;
import com.tm.engagement.domain.Engagement;
import com.tm.engagement.domain.Engagement.ActiveFlagEnum;
import com.tm.engagement.domain.EngagementTask;
import com.tm.engagement.domain.Task;
import com.tm.engagement.domain.TaskDefinition;
import com.tm.engagement.domain.TaskGroup;
import com.tm.engagement.domain.WeekPlan;
import com.tm.engagement.exception.EngagementException;
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
import com.tm.engagement.service.dto.BillingQueueDTO;
import com.tm.engagement.service.dto.EmployeeEngagementTaskDTO;
import com.tm.engagement.service.dto.EngagementContractorsDTO;
import com.tm.engagement.service.dto.EngagementTaskDTO;
import com.tm.engagement.service.dto.TaskDTO;


public class EmployeeEngagementTest {

	@InjectMocks
	EmployeeEngagementServiceImpl employeeEngagementServiceImpl;

	@Mock
	private EmployeeEngagementRepository employeeEngagementRepository;

	@Mock
	private EngagementRepository engagementRepository;

	@Mock
	private WeekPlanRepository weekPlanRepository;

	@Mock
	private EmployeeBillingProfileRepository employeeBillingProfileRepository;

	@Mock
	private EngagementTaskRepository engagementTaskRepository;

	@Mock
	private TaskDefinitionRepository taskDefinitionRepository;
	
	@Mock
	private TaskRepository taskRepository;

	@Mock
	private EmployeeEngagementTaskMapRepository employeeEngagementTaskMapRepository;
	
	@Mock
	private EngagementContractorsRepository engagementContractorsRepository;
	
	@Mock
	private EmployeeTaskDetailViewRepository employeeTaskDetailViewRepository;

	@BeforeMethod
	@BeforeTest
	public void setUp() throws Exception {
		this.employeeEngagementRepository = Mockito.mock(EmployeeEngagementRepository.class);
		this.engagementRepository = Mockito.mock(EngagementRepository.class);
		this.weekPlanRepository = Mockito.mock(WeekPlanRepository.class);
		this.employeeBillingProfileRepository = Mockito.mock(EmployeeBillingProfileRepository.class);
		this.engagementTaskRepository = Mockito.mock(EngagementTaskRepository.class);
		this.taskDefinitionRepository = Mockito.mock(TaskDefinitionRepository.class);
		this.employeeEngagementTaskMapRepository = Mockito.mock(EmployeeEngagementTaskMapRepository.class);
		this.taskRepository=Mockito.mock(TaskRepository.class);
		this.employeeTaskDetailViewRepository = Mockito.mock(EmployeeTaskDetailViewRepository.class);
		employeeEngagementServiceImpl = new EmployeeEngagementServiceImpl(employeeEngagementRepository,
				engagementRepository, weekPlanRepository, employeeBillingProfileRepository, engagementTaskRepository,
				taskDefinitionRepository, employeeEngagementTaskMapRepository,taskRepository,engagementContractorsRepository,employeeTaskDetailViewRepository);
	}

	// TODO : PowerMockito is not supported with code coverage. https://github.com/powermock/powermock/issues/422
	/*@ObjectFactory
	public IObjectFactory getObjectFactory() {
	  return new org.powermock.modules.testng.PowerMockObjectFactory();
	}*/

	@Test
	public void getWeekPlan() {
		List<WeekPlan> weekPlan = new ArrayList<>();
		when(weekPlanRepository.findAll()).thenReturn(weekPlan);
		AssertJUnit.assertEquals(weekPlan, employeeEngagementServiceImpl.getWeekPlan());
	}

	@Test
	public void getTaskDetails() {
		List<TaskDefinition> taskDefinition = new ArrayList<>();
		String taskName="task";
		when(taskDefinitionRepository.findByTaskNameLike(taskName + "%")).thenReturn(taskDefinition);
		AssertJUnit.assertEquals(taskDefinition, employeeEngagementServiceImpl.getTaskDetails(Mockito.anyString()));
	}
	
	@Test (expectedExceptions = {EngagementException.class})
	public void testupdateBillingProfileforNullEmployeeEngagementDetails() {
	
		String employeeEngagementId = "067e6162-3b6f-4ae2-a171-2470b63dff00";
		String status = "Active";
		EmployeeEngagement employeeEngagementDetails = null;
		when(employeeEngagementRepository.findOne(UUID.fromString(employeeEngagementId))).thenReturn(employeeEngagementDetails);
		employeeEngagementServiceImpl.updateBillingProfile(employeeEngagementId, status);
	}
	
	@Test
	public void testupdateBillingProfileForActiveStatus() {

		String employeeEngagementId = "067e6162-3b6f-4ae2-a171-2470b63dff00";
		String status = "Active";
		EmployeeEngagement employeeEngagementDetails = new EmployeeEngagement();
		EmployeeBillingProfile employeeBillingProfileDetails = new EmployeeBillingProfile();
		EmployeeEngagementTaskMap employeeEngagementTaskMapDetails = new EmployeeEngagementTaskMap();
		when(employeeEngagementRepository.findOne(UUID.fromString(employeeEngagementId))).thenReturn(employeeEngagementDetails);
		when(employeeBillingProfileRepository.findByEmployeeEngagementEmployeeEngagamentId(Mockito.anyObject())).thenReturn(employeeBillingProfileDetails);
		when(employeeEngagementTaskMapRepository.findByEmployeeEngagementEmployeeEngagamentId(Mockito.anyObject())).thenReturn(employeeEngagementTaskMapDetails);
		when(employeeEngagementRepository.save(employeeEngagementDetails)).thenReturn(employeeEngagementDetails);
		when(employeeBillingProfileRepository.save(employeeBillingProfileDetails)).thenReturn(employeeBillingProfileDetails);
		when(employeeEngagementTaskMapRepository.save(employeeEngagementTaskMapDetails)).thenReturn(employeeEngagementTaskMapDetails);
		AssertJUnit.assertEquals("OK", employeeEngagementServiceImpl.updateBillingProfile(employeeEngagementId, status));
	}

	@Test
	public void testupdateBillingProfileForInActiveStatus() {
	
		String employeeEngagementId = "067e6162-3b6f-4ae2-a171-2470b63dff00";
		String status = "Inactive";
		EmployeeEngagement employeeEngagementDetails = new EmployeeEngagement();
		EmployeeBillingProfile employeeBillingProfileDetails = new EmployeeBillingProfile();
		employeeBillingProfileDetails.setContractEndDate(new Date("01/05/2017"));
		EmployeeEngagementTaskMap employeeEngagementTaskMapDetails = new EmployeeEngagementTaskMap();
		when(employeeEngagementRepository.findOne(UUID.fromString(employeeEngagementId))).thenReturn(employeeEngagementDetails);
		when(employeeBillingProfileRepository.findByEmployeeEngagementEmployeeEngagamentId(Mockito.anyObject())).thenReturn(employeeBillingProfileDetails);
		when(employeeEngagementTaskMapRepository.findByEmployeeEngagementEmployeeEngagamentId(Mockito.anyObject())).thenReturn(employeeEngagementTaskMapDetails);
		when(employeeEngagementRepository.save(employeeEngagementDetails)).thenReturn(employeeEngagementDetails);
		when(employeeBillingProfileRepository.save(employeeBillingProfileDetails)).thenReturn(employeeBillingProfileDetails);
		when(employeeEngagementTaskMapRepository.save(employeeEngagementTaskMapDetails)).thenReturn(employeeEngagementTaskMapDetails);
		AssertJUnit.assertEquals("OK", employeeEngagementServiceImpl.updateBillingProfile(employeeEngagementId, status));
	}
	
	@Test
	public void testupdateBillingProfileForActiveStatusWithNullTaskMapDetails() {

		String employeeEngagementId = "067e6162-3b6f-4ae2-a171-2470b63dff00";
		String status = "Active";
		EmployeeEngagement employeeEngagementDetails = new EmployeeEngagement();
		EmployeeBillingProfile employeeBillingProfileDetails = new EmployeeBillingProfile();
		employeeBillingProfileDetails.setContractEndDate(new Date("01/05/2017"));
		EmployeeEngagementTaskMap employeeEngagementTaskMapDetails = null;
		when(employeeEngagementRepository.findOne(UUID.fromString(employeeEngagementId))).thenReturn(employeeEngagementDetails);
		when(employeeBillingProfileRepository.findByEmployeeEngagementEmployeeEngagamentId(Mockito.anyObject())).thenReturn(employeeBillingProfileDetails);
		when(employeeEngagementTaskMapRepository.findByEmployeeEngagementEmployeeEngagamentId(Mockito.anyObject())).thenReturn(employeeEngagementTaskMapDetails);
		when(employeeEngagementRepository.save(employeeEngagementDetails)).thenReturn(employeeEngagementDetails);
		when(employeeBillingProfileRepository.save(employeeBillingProfileDetails)).thenReturn(employeeBillingProfileDetails);
		when(employeeEngagementTaskMapRepository.save(employeeEngagementTaskMapDetails)).thenReturn(employeeEngagementTaskMapDetails);
		AssertJUnit.assertEquals("OK", employeeEngagementServiceImpl.updateBillingProfile(employeeEngagementId, status));
	}

	@Test
	public void testupdateBillingProfileForInActiveStatusWithNullTaskMapDetails() {

		String employeeEngagementId = "067e6162-3b6f-4ae2-a171-2470b63dff00";
		String status = "Inactive";
		EmployeeEngagement employeeEngagementDetails = new EmployeeEngagement();
		EmployeeBillingProfile employeeBillingProfileDetails = new EmployeeBillingProfile();
		employeeBillingProfileDetails.setContractEndDate(new Date("01/05/2017"));
		EmployeeEngagementTaskMap employeeEngagementTaskMapDetails = null;
		when(employeeEngagementRepository.findOne(UUID.fromString(employeeEngagementId))).thenReturn(employeeEngagementDetails);
		when(employeeBillingProfileRepository.findByEmployeeEngagementEmployeeEngagamentId(Mockito.anyObject())).thenReturn(employeeBillingProfileDetails);
		when(employeeEngagementTaskMapRepository.findByEmployeeEngagementEmployeeEngagamentId(Mockito.anyObject())).thenReturn(employeeEngagementTaskMapDetails);
		when(employeeEngagementRepository.save(employeeEngagementDetails)).thenReturn(employeeEngagementDetails);
		when(employeeBillingProfileRepository.save(employeeBillingProfileDetails)).thenReturn(employeeBillingProfileDetails);
		when(employeeEngagementTaskMapRepository.save(employeeEngagementTaskMapDetails)).thenReturn(employeeEngagementTaskMapDetails);
		AssertJUnit.assertEquals("OK", employeeEngagementServiceImpl.updateBillingProfile(employeeEngagementId, status));
	}
	
	@Test
	public void createEmployeeEngagement() throws Exception {
	
		BillingQueueDTO billingQueueDTO = Mockito.mock(BillingQueueDTO.class);
		EmployeeEngagement employeeEngagementDetails = Mockito.mock(EmployeeEngagement.class);
		EmployeeBillingProfile employeeBillingProfileDetails = Mockito.mock(EmployeeBillingProfile.class);
		Engagement engagement = Mockito.mock(Engagement.class);				
		List<TaskDTO> taskDTOs = new ArrayList<TaskDTO>();
		when(billingQueueDTO.getAccountManagerId()).thenReturn(10L);
		when(billingQueueDTO.getEngagementId()).thenReturn("067e6162-3b6f-4ae2-a171-2470b63dff00");
		when(billingQueueDTO.getEmployeeId()).thenReturn(600L);
		when(billingQueueDTO.getAccountManagerId()).thenReturn(100L);
		when(billingQueueDTO.getBillingSpecialistId()).thenReturn(10L);
		when(billingQueueDTO.getBillToClientId()).thenReturn(10L);
		when(billingQueueDTO.getBillTypeLookupId()).thenReturn("1000");		
		when(billingQueueDTO.getProcessingFeeRate()).thenReturn(new BigDecimal(1000));
		when(billingQueueDTO.getReferalsFees()).thenReturn(new BigDecimal(1000));
		when(billingQueueDTO.getProfileActiveDate()).thenReturn("05/12/2015");
		when(billingQueueDTO.getTimesheetRuleLookUpId()).thenReturn("1000");
		when(billingQueueDTO.getTimesheetTypeLookUpId()).thenReturn("1000");
		when(billingQueueDTO.getTimesheetMethodLookUpId()).thenReturn("1000");
		when(billingQueueDTO.getEffectiveEndDate()).thenReturn("05/13/2017");
		when(billingQueueDTO.getEffectiveStartDate()).thenReturn("05/13/2015");
		when(billingQueueDTO.getWeekPlanId()).thenReturn("067e6162-3b6f-4ae2-a171-2470b63dff11");
		when(billingQueueDTO.getPurchaseOrderId()).thenReturn(UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff11"));
		when(billingQueueDTO.getSubTasksDetails()).thenReturn(taskDTOs);
		when(billingQueueDTO.getEmployeeEngagementId()).thenReturn("067e6162-3b6f-4ae2-a171-2470b63dff00");
		when(engagementRepository.findOne(UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff00"))).thenReturn(engagement);
		when(employeeEngagementRepository.findByEmployeeIdAndEngagementEngagementIdAndActiveFlag(billingQueueDTO.getEmployeeId(),
						UUID.fromString(billingQueueDTO.getEngagementId()), "Y")).thenReturn(employeeEngagementDetails);
		when(employeeEngagementRepository.findByEmployeeEngagamentIdAndActiveFlag(UUID.fromString(billingQueueDTO.getEmployeeEngagementId()), "Y")).thenReturn(employeeEngagementDetails);
		when(employeeEngagementDetails.getEmployeeEngagamentId()).thenReturn(UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff11"));
		when(employeeEngagementRepository.save((EmployeeEngagement) Mockito.anyObject())).thenReturn(employeeEngagementDetails);
		AssertJUnit.assertEquals(billingQueueDTO, employeeEngagementServiceImpl.createEmployeeEngagement(billingQueueDTO));
		
		when(billingQueueDTO.getCappedMaxDTHours()).thenReturn(null);
		AssertJUnit.assertEquals(billingQueueDTO, employeeEngagementServiceImpl.createEmployeeEngagement(billingQueueDTO));
		
		when(employeeEngagementRepository.findByEmployeeIdAndEngagementEngagementIdAndActiveFlag(billingQueueDTO.getEmployeeId(),
				UUID.fromString(billingQueueDTO.getEngagementId()), "Y")).thenReturn(null);
		when(employeeEngagementRepository.save((EmployeeEngagement) Mockito.anyObject())).thenReturn(employeeEngagementDetails);
		AssertJUnit.assertEquals(billingQueueDTO, employeeEngagementServiceImpl.createEmployeeEngagement(billingQueueDTO));
		
		// TODO : PowerMockito is not supported with code coverage. https://github.com/powermock/powermock/issues/422
		when(employeeEngagementRepository.findByEmployeeIdAndEngagementEngagementIdAndActiveFlag(billingQueueDTO.getEmployeeId(),
				UUID.fromString(billingQueueDTO.getEngagementId()), "Y")).thenReturn(null);
		WeekPlan weekPlan = Mockito.mock(WeekPlan.class);
		when(weekPlanRepository.findOne(UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff11"))).thenReturn(weekPlan);
		/*EmployeeEngagement employeeEngagement = PowerMockito.mock(EmployeeEngagement.class);
		PowerMockito.whenNew(EmployeeEngagement.class).withNoArguments().thenReturn(employeeEngagement);
		PowerMockito.when(employeeEngagementRepository.save(employeeEngagement)).thenReturn(employeeEngagement);
		PowerMockito.when(employeeEngagement.getEmployeeEngagamentId()).thenReturn(UUID.fromString("067e6162-3b6f-4ae2-a171-2470b63dff11"));
		assertEquals(employeeEngagementServiceImpl.createEmployeeEngagement(billingQueueDTO), billingQueueDTO);*/
		
		TaskDTO mockTaskDTO = Mockito.mock(TaskDTO.class);
		when(mockTaskDTO.getTaskName()).thenReturn("TestTask");
		taskDTOs.add(mockTaskDTO);
		TaskDefinition taskDefinition = Mockito.mock(TaskDefinition.class);
		EngagementTask engagementTaskDetails = Mockito.mock(EngagementTask.class);
		EmployeeEngagementTaskMap employeeEngagementTaskMapDetails = Mockito.mock(EmployeeEngagementTaskMap.class);
		when(billingQueueDTO.getSubTasksDetails()).thenReturn(taskDTOs);
		when(taskDefinitionRepository.findByTaskName(Mockito.anyString())).thenReturn(taskDefinition);
		//when(engagementTaskRepository.findByTaskDefinitionTaskIdAndEngagementEngagementId(Mockito.anyObject(), Mockito.anyObject())).thenReturn(engagementTaskDetails);
		when(taskDefinitionRepository.save(taskDefinition)).thenReturn(taskDefinition);
		when(engagementTaskRepository.save(engagementTaskDetails)).thenReturn(engagementTaskDetails);
		//when(employeeEngagementTaskMapRepository.findByEmployeeEngagementEmployeeEngagamentIdAndEngagementTaskEngagementTaskId(Mockito.anyObject(), Mockito.anyObject())).thenReturn(employeeEngagementTaskMapDetails);
		when(employeeEngagementTaskMapRepository.save(employeeEngagementTaskMapDetails)).thenReturn(employeeEngagementTaskMapDetails);
		when(employeeBillingProfileRepository.findByEmplIdAndEmployeeEngagementEmployeeEngagamentId(Mockito.anyLong(), Mockito.anyObject())).thenReturn(employeeBillingProfileDetails);
		AssertJUnit.assertEquals(billingQueueDTO, employeeEngagementServiceImpl.createEmployeeEngagement(billingQueueDTO));
		
		when(taskDefinitionRepository.findByTaskName(Mockito.anyString())).thenReturn(null);
		when(taskDefinitionRepository.save((TaskDefinition)Mockito.anyObject())).thenReturn(taskDefinition);
		AssertJUnit.assertEquals(billingQueueDTO, employeeEngagementServiceImpl.createEmployeeEngagement(billingQueueDTO));
		
		//when(engagementTaskRepository.findByTaskDefinitionTaskIdAndEngagementEngagementId(Mockito.anyObject(), Mockito.anyObject())).thenReturn(null);
		when(engagementTaskRepository.save((EngagementTask)Mockito.anyObject())).thenReturn(engagementTaskDetails);
		AssertJUnit.assertEquals(billingQueueDTO, employeeEngagementServiceImpl.createEmployeeEngagement(billingQueueDTO));
		
		when(employeeBillingProfileRepository.findByEmplIdAndEmployeeEngagementEmployeeEngagamentId(Mockito.anyLong(), Mockito.anyObject())).thenReturn(null);
		AssertJUnit.assertEquals(billingQueueDTO, employeeEngagementServiceImpl.createEmployeeEngagement(billingQueueDTO));
		
		//when(employeeEngagementTaskMapRepository.findByEmployeeEngagementEmployeeEngagamentIdAndEngagementTaskEngagementTaskId(Mockito.anyObject(), Mockito.anyObject())).thenReturn(null);
		AssertJUnit.assertEquals(billingQueueDTO, employeeEngagementServiceImpl.createEmployeeEngagement(billingQueueDTO));
	}
	
	private EmployeeEngagementTaskDTO getEmployeeEngagementDetails(){
	    EmployeeEngagementTaskDTO employeeEngagementTaskDTO=new EmployeeEngagementTaskDTO();
        employeeEngagementTaskDTO.setEngagementId(UUID.randomUUID());
        employeeEngagementTaskDTO.setTaskDescription(RandomStringUtils.random(10));
        employeeEngagementTaskDTO.setTaskId(UUID.randomUUID());
        employeeEngagementTaskDTO.setTaskName(RandomStringUtils.random(10));
        List<EngagementContractorsDTO> contractorList=new ArrayList<>();
        EngagementContractorsDTO engagementContractorsDTO=new EngagementContractorsDTO();
        engagementContractorsDTO.setCheckFlag(true);
        engagementContractorsDTO.setEmployeeDesignationName(RandomStringUtils.random(10));
        engagementContractorsDTO.setEmployeeengagementId(UUID.randomUUID());
        engagementContractorsDTO.setEmployeeName(RandomStringUtils.random(10));
        engagementContractorsDTO.setEngagementId(employeeEngagementTaskDTO.getEngagementId());
        contractorList.add(engagementContractorsDTO);
        employeeEngagementTaskDTO.setContractorList(contractorList);
        return employeeEngagementTaskDTO;
	}
	
    @Test
    public void saveEmployeeEngagementTaskWithSuccess() {
        EmployeeEngagementTaskDTO employeeEngagementTaskDTO = getEmployeeEngagementDetails();
        Task taskDetails = new Task();
        Task task = new Task();
        task.setTaskId(UUID.randomUUID());
        task.setTaskName(RandomStringUtils.random(10));
        taskDetails.setTaskName(employeeEngagementTaskDTO.getTaskName());
        taskDetails.setTaskDescription(employeeEngagementTaskDTO.getTaskDescription());
        List<EngagementTask> engagementTaskList = new ArrayList<>();
        EngagementTask engagementTask = getEngagementTask();
        engagementTaskList.add(engagementTask);
        Engagement engagement = getEngagement();
        EngagementTaskDTO engagementTaskDTO = getEngagementTaskDTO(engagement);
        EmployeeEngagement employeeEngagement = getEmployeeEngagement();
        List<EmployeeEngagementTaskMap> checkEmployeeEngagementTaskMapList = new ArrayList<>();
        EmployeeEngagementTaskMap employeeEngagementTaskMap =
                getEmployeeEngagementTaskMap(employeeEngagement, taskDetails);
        checkEmployeeEngagementTaskMapList.add(employeeEngagementTaskMap);
        when(taskRepository.save(taskDetails)).thenReturn(taskDetails);
        when(taskRepository.findOne(employeeEngagementTaskDTO.getTaskId())).thenReturn(taskDetails);
        when(engagementTaskRepository.findByEngagementIdAndTaskId(
                employeeEngagementTaskDTO.getEngagementId(), taskDetails.getTaskId()))
                        .thenReturn(engagementTaskList);
        when(employeeEngagementRepository.findByEmployeeIdAndEngagementEngagementIdAndActiveFlag(
                employeeEngagementTaskDTO.getContractorList().get(0).getEmployeeId(),
                employeeEngagementTaskDTO.getContractorList().get(0).getEngagementId(), "Y"))
                        .thenReturn(employeeEngagement);
        when(employeeEngagementTaskMapRepository
                .findByEmployeeEngagementEmployeeEngagamentIdAndTaskTaskId(employeeEngagementTaskDTO
                        .getContractorList().get(0).getEmployeeengagementId(),
                        employeeEngagementTaskDTO.getTaskId()))
                                .thenReturn(checkEmployeeEngagementTaskMapList);
        when(employeeEngagementTaskMapRepository.save(checkEmployeeEngagementTaskMapList))
                .thenReturn(checkEmployeeEngagementTaskMapList);
        when(engagementRepository.findOne(engagementTaskDTO.getEngagementId()))
                .thenReturn(engagement);
        when(taskRepository.findByTaskId(engagementTaskDTO.getTaskId())).thenReturn(task);
        when(engagementTaskRepository.findByEngagementIdAndTaskName(
                engagementTaskDTO.getEngagementId(), engagementTaskDTO.getTaskName()))
                        .thenReturn(engagementTaskList);
        when(engagementTaskRepository.save(engagementTask)).thenReturn(engagementTask);
        try{
        employeeEngagementServiceImpl.saveEmployeeEngagementTask(employeeEngagementTaskDTO);
        }catch(Exception e){}
    }

	
    private EngagementTaskDTO getEngagementTaskDTO(Engagement engagement) {
        EngagementTaskDTO engagementTaskDTO=new  EngagementTaskDTO ();
        engagementTaskDTO.setEngagementId(engagement.getEngagementId());
        engagementTaskDTO.setEngagementTaskId(UUID.randomUUID());
        engagementTaskDTO.setTaskId(UUID.randomUUID());
        engagementTaskDTO.setTaskName(RandomStringUtils.random(05));
        return engagementTaskDTO;
    }

    @Test(expectedExceptions={TaskGroupException.class})
    public void saveEmployeeEngagementTaskWithFailiure () {
        EmployeeEngagementTaskDTO employeeEngagementTaskDTO = getEmployeeEngagementDetails();
        Task taskDetails = new Task();
        taskDetails.setTaskName(employeeEngagementTaskDTO.getTaskName());
        taskDetails.setTaskDescription(employeeEngagementTaskDTO.getTaskDescription());
        List<EngagementTask> engagementTaskList = new ArrayList<>();
        EngagementTask engagementTask = getEngagementTask();
        engagementTaskList.add(engagementTask);
        EmployeeEngagement employeeEngagement = getEmployeeEngagement();
        List<EmployeeEngagementTaskMap> checkEmployeeEngagementTaskMapList = new ArrayList<>();
        EmployeeEngagementTaskMap employeeEngagementTaskMap =
                getEmployeeEngagementTaskMap(employeeEngagement, taskDetails);
        Engagement engagement = getEngagement();
        checkEmployeeEngagementTaskMapList.add(employeeEngagementTaskMap);
        when(taskRepository.save(taskDetails)).thenReturn(taskDetails);
        when(taskRepository.findOne(employeeEngagementTaskDTO.getTaskId())).thenReturn(taskDetails);
        when(engagementTaskRepository.findByEngagementIdAndTaskId(
                employeeEngagementTaskDTO.getEngagementId(), taskDetails.getTaskId()))
                        .thenReturn(engagementTaskList);
        when(employeeEngagementRepository.findByEmployeeIdAndEngagementEngagementIdAndActiveFlag(
                employeeEngagementTaskDTO.getContractorList().get(0).getEmployeeId(),
                employeeEngagementTaskDTO.getContractorList().get(0).getEngagementId(), "Y"))
                        .thenReturn(employeeEngagement);
        when(employeeEngagementTaskMapRepository
                .findByEmployeeEngagementEmployeeEngagamentIdAndTaskTaskId(employeeEngagementTaskDTO
                        .getContractorList().get(0).getEmployeeengagementId(),
                        employeeEngagementTaskDTO.getTaskId()))
                                .thenReturn(checkEmployeeEngagementTaskMapList);
        when(employeeEngagementTaskMapRepository.save(checkEmployeeEngagementTaskMapList))
                .thenReturn(checkEmployeeEngagementTaskMapList);
        when(engagementRepository.findOne(UUID.randomUUID())).thenReturn(engagement);
        when(taskRepository.findByTaskId(UUID.randomUUID())).thenReturn(taskDetails);
        when(engagementTaskRepository.findByEngagementIdAndTaskName(UUID.randomUUID(),
                RandomStringUtils.random(10))).thenReturn(engagementTaskList);
        when(engagementTaskRepository.save(engagementTask)).thenReturn(engagementTask);
        employeeEngagementServiceImpl.saveEmployeeEngagementTask(employeeEngagementTaskDTO);
    }

    private Engagement getEngagement() {
        Engagement engagement = new Engagement();
        engagement.setActiveFlag(ActiveFlagEnum.Y);
        engagement.setEngagementId(UUID.randomUUID());
        engagement.setEngagementName(RandomStringUtils.random(10));
        engagement.setCountryId(RandomUtils.nextLong());
        return engagement;
    }

    private EmployeeEngagementTaskMap getEmployeeEngagementTaskMap(
            EmployeeEngagement employeeEngagement, Task task) {
        EmployeeEngagementTaskMap employeeEngagementTaskMap = new EmployeeEngagementTaskMap();
        employeeEngagementTaskMap.setActiveFlag(ActiveFlagEnum.Y);
        employeeEngagementTaskMap.setEmployeeEngagement(employeeEngagement);
        employeeEngagementTaskMap.setLastModifiedBy(RandomUtils.nextLong());
        employeeEngagementTaskMap.setLastModifiedDate(new Date());
        employeeEngagementTaskMap.setTask(task);
        return employeeEngagementTaskMap;
    }

    private EmployeeEngagement getEmployeeEngagement() {
        EmployeeEngagement employeeEngagement = new EmployeeEngagement();
        employeeEngagement.setAccountManagerEmployeeId(RandomUtils.nextLong());
        employeeEngagement.setActiveFlag("Y");
        employeeEngagement.setContractorEmailId(RandomStringUtils.random(10));
        employeeEngagement.setContractorEmployeeId(RandomStringUtils.random(10));
        employeeEngagement.setCustManagerEmployeeId(RandomUtils.nextLong());
        employeeEngagement.setEffectiveEndDate(new Date());
        employeeEngagement.setEffectiveStartDate(new Date());
        employeeEngagement.setEmployeeEngagamentId(UUID.randomUUID());
        return employeeEngagement;
    }

    private EngagementTask getEngagementTask() {
        EngagementTask engagementTask = new EngagementTask();
        engagementTask.setCreatedBy(RandomUtils.nextLong());
        engagementTask.setCreatedDate(new Date());
        engagementTask.setEngagementId(UUID.randomUUID());
        engagementTask.setEngagementTaskId(UUID.randomUUID());
        engagementTask.setTaskId(UUID.randomUUID());
        engagementTask.setTaskName(RandomStringUtils.random(10));
        return engagementTask;
    }
    
    @Test
    public void deleteEmployeeEngagementTask() {
    	UUID engagementId = UUID.randomUUID();
    	UUID taskId = UUID.randomUUID();
    	
    	List<EmployeeTaskDetailView> employeeTaskDetailViewList =new ArrayList<EmployeeTaskDetailView>
    	                           (Arrays.asList(getEmployeeTaskDetailView()));
    	when(employeeTaskDetailViewRepository
                 .findByEngagementIdAndTaskId(engagementId, taskId)).thenReturn(employeeTaskDetailViewList);
    	when(employeeEngagementRepository
                .findByEmployeeIdAndEngagementEngagementIdAndActiveFlag
                (Long.valueOf(employeeTaskDetailViewList.get(0).getEmployeeId()),
                        engagementId, "Y")).thenReturn(getEmployeeEngagement());
    	List<EmployeeEngagementTaskMap> employeeEngagementTaskMapList = new ArrayList<>
    	                 (Arrays.asList(getEmployeeEngagementTaskMap(getEmployeeEngagement(),getTask()))); 
        		when(employeeEngagementTaskMapRepository
                .findByEmployeeEngagementEmployeeEngagamentIdAndTaskTaskId(
                		getEmployeeEngagement().getEmployeeEngagamentId(), taskId)).thenReturn(employeeEngagementTaskMapList);
        doNothing().when(employeeEngagementTaskMapRepository).delete(employeeEngagementTaskMapList);
        List<EngagementTask> engagementTaskList =new ArrayList<EngagementTask> (Arrays.asList(getEngagementTask())); 
        when(engagementTaskRepository.findByEngagementIdAndTaskId(engagementId,taskId)).thenReturn(engagementTaskList);
        doNothing().when(engagementTaskRepository).delete(engagementTaskList);
        employeeEngagementServiceImpl.deleteEmployeeEngagementTask(engagementId,taskId);
    }
    
    private EmployeeTaskDetailView getEmployeeTaskDetailView(){
    	EmployeeTaskDetailView employeeTaskDetailView =new EmployeeTaskDetailView ();
    	employeeTaskDetailView.setEmployeeengagementId(UUID.randomUUID());
    	employeeTaskDetailView.setEmployeeId(String.valueOf(RandomUtils.nextLong()));
    	employeeTaskDetailView.setEmployeeName(RandomStringUtils.random(10));
    	employeeTaskDetailView.setEngagementId(UUID.randomUUID());
    	employeeTaskDetailView.setTaskDesc(RandomStringUtils.random(10));
    	employeeTaskDetailView.setTaskId(UUID.randomUUID());
    	employeeTaskDetailView.setTaskName(RandomStringUtils.random(10));
    	return employeeTaskDetailView ;
    }
    
    private Task getTask(){
    	Task task = new Task();
    	task.setCreatedBy(RandomUtils.nextLong());
    	task.setCreatedDate(new Date());
    	task.setLastModifiedBy(RandomUtils.nextLong());
    	task.setLastModifiedDate(new Date());
    	task.setTaskDescription(RandomStringUtils.random(10));
        TaskGroup taskGroup=Mockito.mock(TaskGroup.class); 
    	task.setTaskGroup(taskGroup);
    	task.setTaskId(UUID.randomUUID());
    	task.setTaskName(RandomStringUtils.random(10));
    	return task;
    }
   
    private EmployeeBillingProfile getEmployeeBillingProfile(){
    	EmployeeBillingProfile employeeBillingProfile = new EmployeeBillingProfile();
    	employeeBillingProfile.setEmployeeBillingProfileId(UUID.randomUUID());
    	employeeBillingProfile.setContractEndDate(new Date());
    	return employeeBillingProfile;
    }
    @Test
    public void getAllEngagementTaskDetails() {
    	UUID engagementId = UUID.randomUUID();
    	EngagementTask engagementTask=getEngagementTask();
    	List<EngagementTask> engagementTaskDetailsList = new ArrayList<>(Arrays.asList(engagementTask));
    	when(engagementTaskRepository.findByEngagementId(engagementId)).thenReturn(engagementTaskDetailsList);
    	List<EmployeeTaskDetailView> employeeTaskDetailViewList = new ArrayList<>(Arrays.asList(getEmployeeTaskDetailView())); 
    			 when(employeeTaskDetailViewRepository
                 .findByEngagementIdAndTaskId(engagementId, engagementTask.getTaskId())).thenReturn(employeeTaskDetailViewList);
    	employeeEngagementServiceImpl.getAllEngagementTaskDetails(engagementId);
    	List<EmployeeTaskDetailView> employeeTaskDetailViewList2 = new ArrayList<>(); 
		 when(employeeTaskDetailViewRepository
        .findByEngagementIdAndTaskId(engagementId, engagementTask.getTaskId())).thenReturn(employeeTaskDetailViewList2);
    	when(taskRepository.findOne(engagementTask.getTaskId())).thenReturn(getTask());
    	employeeEngagementServiceImpl.getAllEngagementTaskDetails(engagementId);
    }
    
    @Test
    public void updateBillingProfile(){
    	String employeeEngagementId=UUID.randomUUID().toString();
    	try{
    	employeeEngagementServiceImpl.updateBillingProfile(employeeEngagementId);
    	}catch(EngagementException e){}
    	EmployeeEngagement employeeEngagementDetails =getEmployeeEngagement(); 
    	
    	when(employeeEngagementRepository
				.findByEmployeeEngagamentIdAndActiveFlag(UUID.fromString(employeeEngagementId),"Y")).thenReturn(employeeEngagementDetails);
    	
    	EmployeeBillingProfile employeeBillingProfileDetails = getEmployeeBillingProfile();
    	Date date = new Date();
    	Calendar c = Calendar.getInstance(); 
    	c.setTime(date); 
    	c.add(Calendar.DATE, 1);
    	date = c.getTime();
    	employeeBillingProfileDetails.setContractEndDate(date);
    			when(employeeBillingProfileRepository
				.findByEmployeeEngagementEmployeeEngagamentIdAndActiveFlag(UUID.fromString(employeeEngagementId),"Y")).thenReturn(employeeBillingProfileDetails);
		employeeEngagementServiceImpl.updateBillingProfile(employeeEngagementId);
		
    }
    
}
