package com.tm.commonapi.web.core.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tm.commonapi.exception.ApplicationException;
import com.tm.commonapi.web.rest.util.CommonUtils;

public abstract class BaseEntity {

    public <T extends BaseDTO> T toDTO(Class<T> type) {
        BaseDTO baseDTO = null;
        try {
            baseDTO = CommonUtils.objectMapper
                    .readValue(CommonUtils.objectMapper.writeValueAsString(this), type);
        } catch (IOException ex) {
            throw new ApplicationException(
                    "An exception occurred while instantiating entity object.", ex);
        }
        if (type.isInstance(baseDTO)) {
            return type.cast(baseDTO);
        }
        if (baseDTO == null) {
            throw new ApplicationException(
                    "An exception occurred while instantiating value object.");
        }
        return null;
    }

    public static <T extends BaseDTO> List<T> toDTOList(List<? extends BaseEntity> inputEntityList,
            Class<T> type) {
        List<T> baseDTOList = new ArrayList<>();
        for (BaseEntity baseEntity : inputEntityList) {
            baseDTOList.add(baseEntity.toDTO(type));
        }
        return baseDTOList;
    }

}
