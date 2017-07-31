package com.tm.engagement.web.rest;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.security.AuthoritiesConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.engagement.domain.Status;
import com.tm.engagement.domain.Task;
import com.tm.engagement.exception.TaskGroupException;
import com.tm.engagement.resource.assemblers.TaskGroupResourceAssembler;
import com.tm.engagement.service.TaskGroupService;
import com.tm.engagement.service.dto.TaskDetailDTO;
import com.tm.engagement.service.dto.TaskGroupDTO;

@RestController
public class TaskGroupResource {

    private static final String ERR_DELETE_TASK = "Exception occur while deleting the task ";

    private TaskGroupService taskGroupService;

    private TaskGroupResourceAssembler taskGroupResourceAssembler;

    @Inject
    public TaskGroupResource(TaskGroupService taskGroupService,
            TaskGroupResourceAssembler taskGroupResourceAssembler) {

        this.taskGroupService = taskGroupService;
        this.taskGroupResourceAssembler = taskGroupResourceAssembler;
    }


    @RequestMapping(value = "/taskGroupList", method = RequestMethod.GET)
    @RequiredAuthority({AuthoritiesConstants.PROFILE_VIEW})
    public PagedResources<TaskGroupDTO> getAllTaskGroupDetails(Pageable pageable,
            PagedResourcesAssembler<TaskGroupDTO> pagedAssembler) {
        Page<TaskGroupDTO> result = taskGroupService.getAllTaskGroupDetails(pageable);
        return pagedAssembler.toResource(result, taskGroupResourceAssembler);
    }

    @RequestMapping(value = "/taskGroup/{taskGroupId}/task/list", method = RequestMethod.GET)
    @RequiredAuthority({AuthoritiesConstants.PROFILE_VIEW})
    public TaskGroupDTO getAllTaskDetails(@PathVariable("taskGroupId") UUID taskGroupId,
            Pageable pageable, PagedResourcesAssembler<TaskGroupDTO> pagedAssembler) {
        return taskGroupService.getAllTaskDetails(taskGroupId, pageable);
    }

    @RequestMapping(value = "/taskGroup/task/{taskId}", method = RequestMethod.DELETE)
    @RequiredAuthority({AuthoritiesConstants.PROFILE_VIEW})
    public Status deleteTask(@PathVariable("taskId") UUID taskId) {
        Task response = taskGroupService.deleteTask(taskId);
        if (null == response) {
            return new Status("ok");
        } else {
            throw new TaskGroupException(ERR_DELETE_TASK);
        }
    }

    @RequestMapping(value = "/taskGroup/task", method = RequestMethod.POST)
    @RequiredAuthority({AuthoritiesConstants.PROFILE_VIEW})
    public Status saveTask(@Valid @RequestBody TaskGroupDTO taskGroupDTO) {
      return taskGroupService.saveTask(taskGroupDTO);
    }
    
    @RequestMapping(value = "/taskGroup", method = RequestMethod.GET)
    @RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
    public List<TaskGroupDTO> getAllTaskGroupDetails() {
         List<TaskGroupDTO> result = taskGroupService.getTaskGroupDetailsList();
         return result;
    }
    
    @RequestMapping(value = "/taskGroup/task/{engagementId}", method = RequestMethod.GET)
    @RequiredAuthority({ AuthoritiesConstants.PROFILE_VIEW })
    public List<TaskDetailDTO> getAllGroupTaskDetails(@RequestParam String taskGroupId,@PathVariable UUID engagementId) {
    	List<TaskDetailDTO> result = taskGroupService.getTaskGroupDetailsList(taskGroupId,engagementId);
         return result;
    }
    
}
