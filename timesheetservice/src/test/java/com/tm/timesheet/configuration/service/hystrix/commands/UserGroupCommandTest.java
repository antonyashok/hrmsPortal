package com.tm.timesheet.configuration.service.hystrix.commands;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.mockito.Mockito;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.netflix.config.ConfigurationManager;
import com.tm.timesheet.configuration.service.dto.UserGroupDTO;


public class UserGroupCommandTest {

    RestTemplate restTemplate;
    
    @BeforeMethod
    public void setUpUserGroupCommandTest() throws Exception {
        restTemplate = Mockito.mock(RestTemplate.class);
        new UserGroupCommand(restTemplate, "http://COMMONSERVICEMANAGEMENT", null);
        ConfigurationManager.getConfigInstance()
                .setProperty("hystrix.command.default.circuitBreaker.forceOpen", "true");
    }

    @AfterMethod
    public void setUpConfigInstance() throws Exception {
        ConfigurationManager.getConfigInstance()
                .setProperty("hystrix.command.default.circuitBreaker.forceOpen", "false");
    }

    /**
     * Test case : userGroupCommandFallback
     * @param responseType
     * @param responseEntity
     * @param group
     * @param url
     * @throws Exception 
     */
    @Test(dataProviderClass = UserGroupCommandTestDataProvider.class,
            dataProvider = "userGroupCommandFallback",
            description = "Fallback check for failure case")
    public void userGroupCommandCommandFallback(
            ParameterizedTypeReference<List<UserGroupDTO>> responseType,
            ResponseEntity<List<UserGroupDTO>> responseEntity, String group, String url) throws Exception {
        when(restTemplate.exchange(url, HttpMethod.GET, null, responseType))
                .thenReturn(responseEntity);
        /*List<UserGroupDTO> persistedUserGroupDTOList =
                new UserGroupCommand(restTemplate, group, null).getAllJobTitle();
        // --Mock call verification for not invoked the run method
        verify(restTemplate, times(0)).exchange(url, HttpMethod.GET, null, responseType);
        // --Service call assertion for fallBack return the empty list
        assertThat(CollectionUtils.isEmpty(persistedUserGroupDTOList)).isTrue();*/
    }
}
