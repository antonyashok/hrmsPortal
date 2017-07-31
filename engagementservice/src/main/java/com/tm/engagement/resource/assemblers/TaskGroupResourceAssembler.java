package com.tm.engagement.resource.assemblers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.inject.Inject;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import com.tm.engagement.service.dto.TaskGroupDTO;
import com.tm.engagement.web.rest.TaskGroupResource;

@Service
public class TaskGroupResourceAssembler
        extends ResourceAssemblerSupport<TaskGroupDTO, TaskGroupDTO> {

    protected final RelProvider relProvider;
    protected final EntityLinks entityLinks;

    @Inject
    public TaskGroupResourceAssembler(final EntityLinks entityLinks,
            final RelProvider relProvider) {
        super(TaskGroupResource.class, TaskGroupDTO.class);
        this.entityLinks = entityLinks;
        this.relProvider = relProvider;
    }

    @Override
    public TaskGroupDTO toResource(TaskGroupDTO resource) {
        // self link to view configuration settings
        resource.add(linkTo(methodOn(TaskGroupResource.class).getAllTaskGroupDetails(null,null)).withSelfRel());
        return resource;
    }

}
