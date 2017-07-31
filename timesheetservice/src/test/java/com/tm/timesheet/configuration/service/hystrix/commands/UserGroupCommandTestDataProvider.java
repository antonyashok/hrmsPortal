package com.tm.timesheet.configuration.service.hystrix.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.DataProvider;

import com.tm.timesheet.configuration.service.dto.UserGroupDTO;

public class UserGroupCommandTestDataProvider {

    @DataProvider(name = "userGroupCommandFallback")
    public static Iterator<Object[]> officeLocationCommand() {
        String url = "http://COMMONSERVICEMANAGEMENT/api/usergroups/all";
        String group = "test-group";
        List<UserGroupDTO> userGroupDTOList = new ArrayList<>();
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setGroupId(UUID.fromString("29005ce7-a90a-4dd4-9de3-235b2d169bbc"));
        userGroupDTO.setGroupName("Director");
        userGroupDTO.setGroupDescription("Desc");
        userGroupDTO.setGroupType("RCTR");
        userGroupDTOList.add(userGroupDTO);
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        ParameterizedTypeReference<List<UserGroupDTO>> responseType = new ParameterizedTypeReference<List<UserGroupDTO>>() {};
        ResponseEntity<List<UserGroupDTO>> responseEntity = new ResponseEntity<>(userGroupDTOList,HttpStatus.OK);
        testData.add(new Object[] {responseType, responseEntity, group, url});
        return testData.iterator();
    }
}
