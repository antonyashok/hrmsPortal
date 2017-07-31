package com.tm.engagement.service.impl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.tm.engagement.domain.EmployeeEngagementTaskMap;
import com.tm.engagement.domain.EngagementTask;
import com.tm.engagement.domain.Task;
import com.tm.engagement.domain.TaskGroup;
import com.tm.engagement.exception.TaskGroupException;
import com.tm.engagement.repository.EmployeeEngagementTaskMapRepository;
import com.tm.engagement.repository.EngagementTaskRepository;
import com.tm.engagement.repository.TaskGroupRepository;
import com.tm.engagement.repository.TaskRepository;
import com.tm.engagement.service.dto.TaskDetailDTO;
import com.tm.engagement.service.dto.TaskGroupDTO;

public class TaskGroupTest {

	@InjectMocks
	TaskGroupServiceImpl taskGroupServiceImpl;

	@Mock
	private TaskGroupRepository taskGroupRepository;

	@Mock
	private TaskRepository taskRepository;
	
	@Mock
	private EmployeeEngagementTaskMapRepository employeeEngagementTaskMapRepository;

	@Mock
	private EngagementTaskRepository engagementTaskRepository;

	@BeforeTest
	public void setUp() throws Exception {
		this.taskGroupRepository = Mockito.mock(TaskGroupRepository.class);
		this.taskRepository = Mockito.mock(TaskRepository.class);
		this.employeeEngagementTaskMapRepository = Mockito.mock(EmployeeEngagementTaskMapRepository.class);
		this.engagementTaskRepository = Mockito.mock(EngagementTaskRepository.class);
		taskGroupServiceImpl = new TaskGroupServiceImpl(taskGroupRepository, taskRepository, employeeEngagementTaskMapRepository,
				engagementTaskRepository);
	}

	@Test
	public void getAllTaskGroupDetails(){
		Pageable pageRequest = new PageRequest(0, 100, Sort.Direction.DESC, "auditFields.on");
		TaskGroup taskGroup = Mockito.mock(TaskGroup.class);
		List<TaskGroup> taskGroupDetails=new ArrayList<>();
		taskGroupDetails.add(taskGroup);
		Page<TaskGroup> taskGroups = new PageImpl<TaskGroup>(taskGroupDetails);
		when(taskGroupRepository.findAll()).thenReturn(taskGroupDetails);
		when(taskGroupRepository.findAll(pageRequest)).thenReturn(taskGroups);
		taskGroupServiceImpl.getAllTaskGroupDetails(pageRequest);
	}

	@Test
	public void getAllTaskDetails(){
		
		TaskGroup taskGroup = new TaskGroup();
		UUID uuid=UUID.randomUUID();
		List<Task> taskList = new ArrayList<Task>();
	    Task task = new Task();
		task.setTaskId(uuid);
		task.setTaskName("Task Name");
		task.setTaskDescription("taskDescription");
		taskList.add(task);
		taskGroup.setTaskGroupId(uuid);
		taskGroup.setTaskGroupName("Task Group Name");
		taskGroup.setTaskGroupDescription("Task Group Description");
		
		taskGroup.setTaskDetails(taskList);
		Pageable pageRequest = new PageRequest(0, 100, Sort.Direction.DESC, "auditFields.on");
		try{
			taskGroupServiceImpl.getAllTaskDetails(uuid, pageRequest);
		}catch(Exception e){}
		when(taskGroupRepository.findByTaskGroupId(uuid)).thenReturn(taskGroup);
		try{
			taskGroupServiceImpl.getAllTaskDetails(uuid, pageRequest);
		}catch(Exception e){}
		List<Task> listTask=new ArrayList<>();
		Task task1 = Mockito.mock(Task.class);
		listTask.add(task1);
		when(taskRepository.findByTaskGroup(taskGroup)).thenReturn(listTask);
		Page<Task> tasks =  new PageImpl<Task>(listTask);
		when(taskRepository.findByTaskGroup(taskGroup,pageRequest)).thenReturn(tasks);
		List<EmployeeEngagementTaskMap> mappedTasks= new ArrayList<EmployeeEngagementTaskMap>();
		EmployeeEngagementTaskMap employeeEngagementTaskMap = Mockito.mock(EmployeeEngagementTaskMap.class);
		mappedTasks.add(employeeEngagementTaskMap);
		List<UUID> taskIds=new ArrayList<UUID>();
		taskIds.add(uuid);
		when(employeeEngagementTaskMapRepository.findByEngagementTaskEngagementTaskId(taskIds)).thenReturn(mappedTasks);
		taskGroupServiceImpl.getAllTaskDetails(uuid, pageRequest);
	}
	
	@Test
	public void deleteTask() {
		 Task task = Mockito.mock(Task.class);
		 UUID taskId=UUID.randomUUID();
		 doNothing().when(taskRepository).delete(taskId);
		 when(taskRepository.findOne(taskId)).thenReturn(task);
		 taskGroupServiceImpl.deleteTask(taskId);
		 
	 }
	
	@Test
    public void saveTask(){
        UUID uuid = UUID.randomUUID();
        TaskGroupDTO taskGroupDTO = new TaskGroupDTO();
        TaskDetailDTO taskDetailDTO = new TaskDetailDTO();
        taskDetailDTO.setDeletable(false);
        taskDetailDTO.setTaskId(uuid);
        List<TaskDetailDTO> listTaskDetailDTO = new ArrayList<TaskDetailDTO>();
        listTaskDetailDTO.add(taskDetailDTO);
        taskGroupDTO.setTaskDetails(listTaskDetailDTO);
        taskGroupDTO.setTaskGroupName("Task Group Name");
        try{
           taskGroupServiceImpl.saveTask(taskGroupDTO);}
        catch(Exception e){}
        taskDetailDTO.setTaskName("Enter the name of the details");
        
         try{
             taskGroupServiceImpl.saveTask(taskGroupDTO);
         }catch(Exception e){}
         TaskGroup taskGroup = new TaskGroup();
         taskGroup.setTaskGroupId(uuid);
         try{
             when(taskGroupRepository.findByTaskGroupName(taskGroupDTO.getTaskGroupName())).thenReturn(taskGroup);
             taskGroupServiceImpl.saveTask(taskGroupDTO);}
         catch(Exception e){}
         //List<String> listString = new ArrayList<String>(Arrays.asList(taskDetailDTO.getTaskName()));
         List<String> listString = new ArrayList<String>();
         listString.add(taskDetailDTO.getTaskName());
         
         Task task = new Task();
         task.setTaskId(uuid);
         task.setTaskDescription(RandomStringUtils.random(10));
         task.setTaskNames(listString);
         task.setTaskName(RandomStringUtils.random(10));
         //existingTasks.add(task);
         List<Task> existingTasks=new ArrayList<>();
         existingTasks.add(task);
         taskGroupDTO.setTaskGroupId(taskGroup.getTaskGroupId());
         taskDetailDTO.setTaskId(null);
         List<Task> existingTasks1=new ArrayList<>(Arrays.asList(task));
         
         taskGroupDTO.setTaskGroupId(taskGroup.getTaskGroupId());
         taskDetailDTO.setTaskId(null);
         taskGroup.setTaskDetails(existingTasks1);
         taskGroup.setTaskGroupDescription(RandomStringUtils.random(10));
         taskGroup.setTaskGroupName(RandomStringUtils.random(10));
         try{
             when(taskRepository.findTasksByTaskName(listString, taskGroup)).thenReturn(existingTasks);
             taskGroupServiceImpl.saveTask(taskGroupDTO);}
         catch(Exception e){}
         try{
             taskDetailDTO.setTaskId(uuid);
             taskGroupDTO.setTaskGroupId(uuid);
             when(taskRepository.findTasksByTaskName(listString, taskGroup)).thenReturn(existingTasks);
             taskGroupServiceImpl.saveTask(taskGroupDTO);}
         catch(Exception e){}
    }
		
    @Test
    public void saveTaskWithDeletableTasks() {
        UUID uuid = UUID.randomUUID();
        TaskGroupDTO taskGroupDTO = new TaskGroupDTO();
        TaskDetailDTO taskDetailDTO = new TaskDetailDTO();
        taskDetailDTO.setDeletable(true);
        taskDetailDTO.setTaskId(uuid);
        List<TaskDetailDTO> listTaskDetailDTO = new ArrayList<TaskDetailDTO>();
        listTaskDetailDTO.add(taskDetailDTO);
        taskGroupDTO.setTaskDetails(listTaskDetailDTO);
        taskGroupDTO.setTaskGroupName(RandomStringUtils.random(10));
        taskGroupServiceImpl.saveTask(taskGroupDTO);
    }
       
	@Test(expectedExceptions = { TaskGroupException.class })
	    public void saveTaskWithEmptyTaskName(){
	        UUID uuid = UUID.randomUUID();
	        TaskGroupDTO taskGroupDTO = new TaskGroupDTO();
	        TaskDetailDTO taskDetailDTO = new TaskDetailDTO();
	        TaskGroup taskGroup=new TaskGroup();
	        taskDetailDTO.setDeletable(false);
	        taskDetailDTO.setTaskId(uuid);
	        List<TaskDetailDTO> listTaskDetailDTO = new ArrayList<TaskDetailDTO>();
	        listTaskDetailDTO.add(taskDetailDTO);
	        taskGroupDTO.setTaskDetails(listTaskDetailDTO);
	        taskDetailDTO.setTaskName(StringUtils.EMPTY);
	        taskGroupDTO.setTaskGroupName("Task Group Name");
	        when(taskGroupRepository.findByTaskGroupName(taskGroupDTO.getTaskGroupName())).thenReturn(taskGroup);
            taskGroupServiceImpl.saveTask(taskGroupDTO);
	           taskGroupServiceImpl.saveTask(taskGroupDTO);
	    }
	
    @Test
    public void getTaskGroupDetailsList() {
        TaskGroup taskGroup = Mockito.mock(TaskGroup.class);
        List<TaskGroup> taskGroupDetails = new ArrayList<TaskGroup>();
        taskGroupDetails.add(taskGroup);
        when(taskGroupRepository.findAll()).thenReturn(taskGroupDetails);
        taskGroupServiceImpl.getTaskGroupDetailsList();
    }
	
	@Test
	public void testGetTaskGroupDetailsList(){
		String id=UUID.randomUUID().toString();
		Task task = new Task();
		task.setTaskId(UUID.randomUUID());
		task.setTaskDescription("Task Description");
		task.setTaskName("Task Name");
		List<Task> taskList = new ArrayList<Task>();
		taskList.add(task);
		when(taskRepository.findByTaskGroupTaskGroupId(UUID.fromString(id))).thenReturn(taskList);
		
		EngagementTask engagementTask = Mockito.mock(EngagementTask.class);
		List<EngagementTask> engagementTaskList = new ArrayList<>(Arrays.asList(engagementTask));
        
		when(engagementTaskRepository.findByEngagementIdAndTaskName(UUID.randomUUID(), "Task Name")).thenReturn(engagementTaskList);
		taskGroupServiceImpl.getTaskGroupDetailsList(id,UUID.randomUUID());
	}
	
	
}