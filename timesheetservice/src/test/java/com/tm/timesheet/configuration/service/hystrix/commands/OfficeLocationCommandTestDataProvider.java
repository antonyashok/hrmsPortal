package com.tm.timesheet.configuration.service.hystrix.commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.DataProvider;

import com.tm.timesheet.configuration.service.dto.OfficeLocationDTO;

public class OfficeLocationCommandTestDataProvider {

    @DataProvider(name = "officeLocationCommandFallback")
    public static Iterator<Object[]> officeLocationCommand() {
        String url = "http://COMMONSERVICEMANAGEMENT/api/officelocation/all";
        String group = "test-group";
        List<OfficeLocationDTO> officeLocationDTOList = new ArrayList<>();
        OfficeLocationDTO officeLocationDTO = new OfficeLocationDTO();
        officeLocationDTO.setActiveFlag("y");
        officeLocationDTO.setOfficeId(123L);
        officeLocationDTO.setOfficeName("NewYork");
        officeLocationDTO.setRegionId(456L);
        officeLocationDTOList.add(officeLocationDTO);
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        ParameterizedTypeReference<List<OfficeLocationDTO>> responseType = new ParameterizedTypeReference<List<OfficeLocationDTO>>() {};
        ResponseEntity<List<OfficeLocationDTO>> responseEntity = new ResponseEntity<>(officeLocationDTOList,HttpStatus.OK);
        testData.add(new Object[] {responseType, responseEntity, group, url});
        return testData.iterator();
    }
}
