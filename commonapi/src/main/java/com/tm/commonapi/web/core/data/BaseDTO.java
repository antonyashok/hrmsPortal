package com.tm.commonapi.web.core.data;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import com.tm.commonapi.exception.ApplicationException;
import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.commonapi.web.rest.util.ReflectionUtil;

public abstract class BaseDTO {

    public <T extends BaseEntity> T toDomain(Class<T> type) {
        BaseEntity baseEntity = null;
        try {
            baseEntity = CommonUtils.objectMapper
                    .readValue(CommonUtils.objectMapper.writeValueAsString(this), type);
        } catch (IOException ex) {
            throw new ApplicationException(
                    "An exception occurred while instantiating entity object.", ex);
        }
        if (type.isInstance(baseEntity)) {
            return type.cast(baseEntity);
        }
        if (baseEntity == null) {
            throw new ApplicationException(
                    "An exception occurred while instantiating entity object.");
        }
        return null;
    }


    public <T extends BaseDTO> Resource<T> toResourceWithSelfLink(Class<T> type,
            Class<?> restController) {
        if (!type.isInstance(this)) {
            throw new ApplicationException("An exception occurred while casting dto object.");
        }
        Resource<T> resource = new Resource<>(type.cast(this));
        resource.add(linkTo(restController)
                .slash(ReflectionUtil.invokeMethodByName(type, "getId", type.cast(this)))
                .withSelfRel());
        return resource;
    }


    public static <T extends BaseEntity> List<T> toDomainList(List<? extends BaseDTO> inputDTOList,
            Class<T> type) {
        List<T> baseEntityList = new ArrayList<>();
        for (BaseDTO baseDTO : inputDTOList) {
            baseEntityList.add(baseDTO.toDomain(type));
        }
        return baseEntityList;
    }

    public static <T extends BaseDTO> List<Resource<T>> toResourceListWithSelfLink(
            List<? extends BaseDTO> inputDTOList, Class<T> type, Class<?> restController) {
        List<Resource<T>> resourceList = new ArrayList<>();
        for (BaseDTO baseDTO : inputDTOList) {
            resourceList.add(baseDTO.toResourceWithSelfLink(type, restController));
        }
        return resourceList;
    }

    public <T extends BaseDTO> Resource<T> toResourceWithCustomLinks(Class<T> type,
            Class<?> restController, Boolean includeSelfLink, Link... customLinks) {
        if (!type.isInstance(this)) {
            throw new ApplicationException("An exception occurred while casting dto object.");
        }
        Resource<T> resource = new Resource<>(type.cast(this));
        if (includeSelfLink) {
            resource.add(linkTo(restController)
                    .slash(ReflectionUtil.invokeMethodByName(type, "getId", type.cast(this)))
                    .withSelfRel());
        }
        resource.add(customLinks);
        return resource;
    }

    public static <T extends BaseDTO> List<Resource<T>> toResourceListWithCustomLink(
            List<? extends BaseDTO> inputDTOList, Class<T> type, Class<?> restController,
            Boolean includeSelfLink, Link... customLinks) {
        List<Resource<T>> resourceList = new ArrayList<>();
        for (BaseDTO baseDTO : inputDTOList) {
            resourceList.add(baseDTO.toResourceWithCustomLinks(type, restController,
                    includeSelfLink, customLinks));
        }
        return resourceList;
    }


}
