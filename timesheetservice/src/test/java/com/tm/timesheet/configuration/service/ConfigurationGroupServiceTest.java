package com.tm.timesheet.configuration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.mockito.Mockito;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tm.timesheet.configuration.domain.ConfigurationGroup;
import com.tm.timesheet.configuration.domain.ConfigurationGroupView;
import com.tm.timesheet.configuration.domain.HolidayConfiguration;
import com.tm.timesheet.configuration.domain.NotificationAttribute;
import com.tm.timesheet.configuration.domain.NotificationConfiguration;
import com.tm.timesheet.configuration.enums.TimeCalculationEnum;
import com.tm.timesheet.configuration.exception.ConfigurationException;
import com.tm.timesheet.configuration.exception.ConfigurationNotFoundException;
import com.tm.timesheet.configuration.exception.HolidayConfigurationException;
import com.tm.timesheet.configuration.exception.HolidayDateConfigurationException;
import com.tm.timesheet.configuration.repository.ConfigurationGroupRepository;
import com.tm.timesheet.configuration.repository.ConfigurationGroupViewRepository;
import com.tm.timesheet.configuration.repository.HolidayConfigurationRepository;
import com.tm.timesheet.configuration.repository.NotificationAttributeRepository;
import com.tm.timesheet.configuration.repository.NotificationConfigurationRepository;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupDTO;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupViewDTO;
import com.tm.timesheet.configuration.service.dto.HolidayConfigurationDTO;
import com.tm.timesheet.configuration.service.dto.OfficeLocationDTO;
import com.tm.timesheet.configuration.service.dto.UserGroupDTO;
import com.tm.timesheet.configuration.service.impl.ConfigurationGroupServiceImpl;
import com.tm.timesheet.configuration.service.mapper.ConfigurationGroupMapper;

public class ConfigurationGroupServiceTest {

    private static final String CONFIG_UUID_STRING = "461b98be-db2e-441d-903d-d75f7296763a";

	ConfigurationGroupServiceImpl configurationGroupServiceImpl;

    ConfigurationGroupRepository configurationGroupRepository;

    HolidayConfigurationRepository holidayConfigurationRepository;

    NotificationConfigurationRepository notificationConfigurationRepository;

    NotificationAttributeRepository notificationAttributeRepository;
    
    ConfigurationGroupViewRepository configurationGroupViewRepository;

    DiscoveryClient discoveryClient;

    RestTemplate restTemplate;
    
    MessageSource messageSource;
    
    MessageSourceAccessor accessor;

    @BeforeMethod
    public void setUpConfigurationSettingServiceTest() throws Exception {
        configurationGroupRepository = Mockito.mock(ConfigurationGroupRepository.class);
        holidayConfigurationRepository = Mockito.mock(HolidayConfigurationRepository.class);
        notificationConfigurationRepository =
                Mockito.mock(NotificationConfigurationRepository.class);
        notificationAttributeRepository = Mockito.mock(NotificationAttributeRepository.class);
        configurationGroupViewRepository = Mockito.mock(ConfigurationGroupViewRepository.class);
        discoveryClient = Mockito.mock(DiscoveryClient.class);
        restTemplate = Mockito.mock(RestTemplate.class);
        messageSource = Mockito.mock(MessageSource.class);
        accessor = Mockito.mock(MessageSourceAccessor.class);
		configurationGroupServiceImpl = new ConfigurationGroupServiceImpl(
				configurationGroupRepository, holidayConfigurationRepository,
				notificationConfigurationRepository,
				notificationAttributeRepository,
				configurationGroupViewRepository, restTemplate,
				discoveryClient, messageSource);
    }

    /**
     * Test case : saveTimesheetConfigurationForTimeStampInput
     * 
     * @param configurationGroupDTO
     * @param configurationGroup
     * @param persistedConfigurationGroup
     * @throws Exception
     */
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "saveTimesheetConfigurationForTimeStampInput",
            description = "To be save the given timeSheet configuration of Time_Stamp based input data and result should be a successful one.")
    public void saveTimesheetConfigurationForTimeStampInput(
            ConfigurationGroupDTO configurationGroupDTO, ConfigurationGroup configurationGroup,
            ConfigurationGroup persistedConfigurationGroup) throws Exception {
        when(configurationGroupRepository.save(any(ConfigurationGroup.class)))
                .thenReturn(persistedConfigurationGroup);
        ConfigurationGroupDTO persistedConfigurationGroupDTO =
                configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
        // --Mock call verification
        verify(configurationGroupRepository, times(1)).save(any(ConfigurationGroup.class));
        // --Service call assertion
        assertThat(null != persistedConfigurationGroupDTO.getConfigurationGroupId()).isTrue();
        assertThat(StringUtils.equals("NewYork City Timesheet Setup",
                persistedConfigurationGroupDTO.getConfigurationGroupName())).isTrue();
        assertThat(persistedConfigurationGroupDTO.getConfigurationGroupLocation() != null).isTrue();
        assertThat(
                CollectionUtils.isNotEmpty(configurationGroupDTO.getConfigurationGroupLocation()))
                        .isTrue();
        assertThat(persistedConfigurationGroupDTO.getHolidayConfiguration() != null).isTrue();
        assertThat(StringUtils.equals(persistedConfigurationGroupDTO.getHolidayConfiguration()
                .iterator().next().getHolidayDescription(), "Thanks giving day")).isTrue();
        
        configurationGroupDTO.setStMaxHours(0.5D);
        try {
        	configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
        } catch (Exception e) {}
        
        configurationGroupDTO.setStMaxHours(199D);
        try {
        	configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
        } catch (Exception e) {}
        
        configurationGroupDTO.setStMaxHours(15D);
        configurationGroupDTO.setOtMaxHours(199D);
        try {
        	configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
        } catch (Exception e) {}
        
        configurationGroupDTO.setOtMaxHours(15D);
        configurationGroupDTO.setDtMaxHours(199D);
        try {
        	configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
        } catch (Exception e) {}
        
        configurationGroupDTO.setDtMaxHours(15D);
        try {
        	configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
        } catch (Exception e) {}
        
        configurationGroupDTO.setStMaxHours(168D);
        try {
        	configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
        } catch (Exception e) {}
        
        configurationGroupDTO.setTimeCalculation(TimeCalculationEnum.valueOf("D").timeCalutaion());
        configurationGroupDTO.setStMaxHours(24D);
        configurationGroupDTO.setOtMaxHours(15D);
        configurationGroupDTO.setDtMaxHours(0D);
        try {
        	configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
        } catch (Exception e) {}
        
        configurationGroupDTO.setTimeCalculation(TimeCalculationEnum.valueOf("D").timeCalutaion());
        configurationGroupDTO.setStMaxHours(9D);
        configurationGroupDTO.setDtMaxHours(20D);
        configurationGroupDTO.setDefaultInput("TEST");
        configurationGroupDTO.setUserGroupCategory("RCTR");
        configurationGroupDTO.setMaxHours(140D);
        configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
    }

    /**
     * Test case : saveTimesheetConfigurationForHourInput
     * 
     * @param configurationGroupDTO
     * @param configurationGroup
     * @param persistedConfigurationGroup
     * @throws Exception
     */
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "saveTimesheetConfigurationForHourInput",
            description = "To be save the given timeSheet configuration of HOUR based input data and result should be a successful one.")
    public void saveTimesheetConfigurationForHourInput(ConfigurationGroupDTO configurationGroupDTO,
            ConfigurationGroup configurationGroup, ConfigurationGroup persistedConfigurationGroup)
            throws Exception {
        when(configurationGroupRepository.save(any(ConfigurationGroup.class)))
                .thenReturn(persistedConfigurationGroup);
        ConfigurationGroupDTO persistedConfigurationGroupDTO =
                configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
        // --Mock call verification
        verify(configurationGroupRepository, times(1)).save(any(ConfigurationGroup.class));
        // --Service call assertion
        assertThat(null != persistedConfigurationGroupDTO.getConfigurationGroupId()).isTrue();
        assertThat(StringUtils.equals("NewYork City Timesheet Setup",
                persistedConfigurationGroupDTO.getConfigurationGroupName())).isTrue();
        assertThat(persistedConfigurationGroupDTO.getConfigurationGroupLocation() != null).isTrue();
        assertThat(persistedConfigurationGroupDTO.getHolidayConfiguration() != null).isTrue();
        assertThat(StringUtils.equals(persistedConfigurationGroupDTO.getHolidayConfiguration()
                .iterator().next().getHolidayDescription(), "Thanks giving day")).isTrue();

    }

    /**
     * Test case : saveTimesheetConfigurationWrongEnumValue
     * 
     * @param configurationGroupDTO
     * @throws Exception
     */
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "saveTimesheetConfigurationWrongEnumValue",
            expectedExceptions = Exception.class,
            description = "Should not be save in this request, Since Enum value not matched with request.")
    public void saveTimesheetConfigurationWrongEnumValue(
            ConfigurationGroupDTO configurationGroupDTO) throws Exception {
        configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
    }


    /**
     * Test case : saveTimesheetConfigurationReturnNull
     * 
     * @param configurationGroupDTO
     * @param configurationGroup
     * @throws Exception
     */
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "saveTimesheetConfigurationReturnNull",
            description = "Repository got fails to given response, User get null value of return.")
    public void saveTimesheetConfigurationReturnNull(ConfigurationGroupDTO configurationGroupDTO,
            ConfigurationGroup configurationGroup) throws Exception {
        ConfigurationGroupDTO persistedConfigurationGroupDTO =
                configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
        // --Service call assertion
        assertThat(null == persistedConfigurationGroupDTO).isTrue();
    }

    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "saveTimesheetConfigurationReturnNull",
            description = "Repository got fails to given response, User get null value of return.")
    public void saveTimesheetConfigurationStHoursIsNull(ConfigurationGroupDTO configurationGroupDTO,
            ConfigurationGroup configurationGroup) throws Exception {
    	//configurationGroupDTO.setStMaxHours(1d);
        ConfigurationGroupDTO persistedConfigurationGroupDTO =
                configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
        // --Service call assertion
        assertThat(null == persistedConfigurationGroupDTO).isTrue();
    }

    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "saveTimesheetConfigurationReturnNull",
            description = "Repository got fails to given response, User get null value of return.")
    public void saveTimesheetConfigurationOtHoursIsNull(ConfigurationGroupDTO configurationGroupDTO,
            ConfigurationGroup configurationGroup) throws Exception {
    	//configurationGroupDTO.setOtMaxHours(1d);
        ConfigurationGroupDTO persistedConfigurationGroupDTO =
                configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
        // --Service call assertion
        assertThat(null == persistedConfigurationGroupDTO).isTrue();
    }
    
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "saveTimesheetConfigurationReturnNull",
            description = "Repository got fails to given response, User get null value of return.")
    public void saveTimesheetConfigurationDtHoursIsNull(ConfigurationGroupDTO configurationGroupDTO,
            ConfigurationGroup configurationGroup) throws Exception {
    	//configurationGroupDTO.setDtMaxHours(1d);
        ConfigurationGroupDTO persistedConfigurationGroupDTO =
                configurationGroupServiceImpl.createConfigurationGroup(configurationGroupDTO);
        // --Service call assertion
        assertThat(null == persistedConfigurationGroupDTO).isTrue();
    }

    /**
     * Test case : updateTimesheetConfigurationForHourInput
     * 
     * @param configurationGroupDTO
     * @param configurationGroup
     * @param persistedConfigurationGroup
     * @param oldPersistedConfigurationGroup
     * @param updateOldPersistedConfigurationGroup
     * @throws Exception
     */
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "updateTimesheetConfigurationForHourInput",
            description = "To be update the given configuration based on HOUR input type value only.")
    public void updateTimesheetConfigurationForHourInput(
            ConfigurationGroupDTO configurationGroupDTO, ConfigurationGroup configurationGroup,
            ConfigurationGroup persistedConfigurationGroup,
            ConfigurationGroup oldPersistedConfigurationGroup,
            ConfigurationGroup updateOldPersistedConfigurationGroup) throws Exception {
        when(configurationGroupRepository.save(any(ConfigurationGroup.class)))
                .thenReturn(persistedConfigurationGroup);
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING)))
                .thenReturn(oldPersistedConfigurationGroup);
        when(configurationGroupRepository.saveAndFlush(any(ConfigurationGroup.class)))
                .thenReturn(updateOldPersistedConfigurationGroup);
        ConfigurationGroupDTO persistedConfigurationGroupDTO =
                configurationGroupServiceImpl.updateConfigurationGroup(configurationGroupDTO);

        // --Mock call verification
        verify(configurationGroupRepository, times(1)).save(any(ConfigurationGroup.class));
        verify(configurationGroupRepository, times(1)).saveAndFlush(any(ConfigurationGroup.class));
        verify(configurationGroupRepository, times(1)).findOne(oldPersistedConfigurationGroup.getConfigurationGroupId());
        // --Service call assertion
        assertThat(null != persistedConfigurationGroupDTO.getConfigurationGroupId()).isTrue();
        assertThat(StringUtils.equals("NewYork City & California Timesheet Setup",
                persistedConfigurationGroupDTO.getConfigurationGroupName())).isTrue();
        assertThat(persistedConfigurationGroupDTO.getConfigurationGroupLocation() != null).isTrue();
        assertThat(persistedConfigurationGroupDTO.getHolidayConfiguration() != null).isTrue();
        assertThat(StringUtils.equals(persistedConfigurationGroupDTO.getHolidayConfiguration()
                .iterator().next().getHolidayDescription(), "Thanks giving day")).isTrue();
        assertThat(configurationGroupDTO.getConfigurationGroupId() != persistedConfigurationGroupDTO
                .getConfigurationGroupId()).isTrue();
    }

    /**
     * Test case : updateTimesheetConfigurationForTimeInput
     * 
     * @param configurationGroupDTO
     * @param configurationGroup
     * @param persistedConfigurationGroup
     * @param oldPersistedConfigurationGroup
     * @param updateOldPersistedConfigurationGroup
     * @throws Exception
     */
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "updateTimesheetConfigurationForTimeInput",
            description = "To be update the given configuration based on TIME_STAMP input type value only.")
    public void updateTimesheetConfigurationForTimeInput(
            ConfigurationGroupDTO configurationGroupDTO, ConfigurationGroup configurationGroup,
            ConfigurationGroup persistedConfigurationGroup,
            ConfigurationGroup oldPersistedConfigurationGroup,
            ConfigurationGroup updateOldPersistedConfigurationGroup) throws Exception {
        when(configurationGroupRepository.save(any(ConfigurationGroup.class)))
                .thenReturn(persistedConfigurationGroup);
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING)))
                .thenReturn(oldPersistedConfigurationGroup);
        when(configurationGroupRepository.saveAndFlush(any(ConfigurationGroup.class)))
                .thenReturn(updateOldPersistedConfigurationGroup);
        ConfigurationGroupDTO persistedConfigurationGroupDTO =
                configurationGroupServiceImpl.updateConfigurationGroup(configurationGroupDTO);

        // --Mock call verification
        verify(configurationGroupRepository, times(1)).save(any(ConfigurationGroup.class));
        verify(configurationGroupRepository, times(1)).saveAndFlush(any(ConfigurationGroup.class));
        verify(configurationGroupRepository, times(1)).findOne(oldPersistedConfigurationGroup.getConfigurationGroupId());
        // --Service call assertion
        assertThat(null != persistedConfigurationGroupDTO.getConfigurationGroupId()).isTrue();
        assertThat(StringUtils.equals("NewYork City & California Timesheet Setup",
                persistedConfigurationGroupDTO.getConfigurationGroupName())).isTrue();
        assertThat(persistedConfigurationGroupDTO.getConfigurationGroupLocation() != null).isTrue();
        assertThat(persistedConfigurationGroupDTO.getHolidayConfiguration() != null).isTrue();
        assertThat(StringUtils.equals(persistedConfigurationGroupDTO.getHolidayConfiguration()
                .iterator().next().getHolidayDescription(), "Thanks giving day")).isTrue();
        assertThat(configurationGroupDTO.getConfigurationGroupId() != persistedConfigurationGroupDTO
                .getConfigurationGroupId()).isTrue();
    }

    /**
     * Test case : updateTimesheetConfigurationRecordNotExist
     * 
     * @param configurationGroupDTO
     * @throws Exception
     */
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "updateTimesheetConfigurationRecordNotExist",
            expectedExceptions = ConfigurationNotFoundException.class,
            description = "Should be error due to the record does not exist given configurationId.")
    public void updateTimesheetConfigurationRecordNotExist(
            ConfigurationGroupDTO configurationGroupDTO) throws Exception {
        configurationGroupServiceImpl.updateConfigurationGroup(configurationGroupDTO);
    }

    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "updateTimesheetConfigurationSetOtDtAsNull",
            expectedExceptions = ConfigurationNotFoundException.class,
            description = "Should be error due to the record does not exist given configurationId.")
    public void updateTimesheetConfigurationSetOtDtAsNull(
            ConfigurationGroupDTO configurationGroupDTO) throws Exception {
        configurationGroupServiceImpl.updateConfigurationGroup(configurationGroupDTO);
    }
    
    /**
     * Test case : getTimesheetConfiguration
     * 
     * @param configurationGroupId
     * @param persistedConfigurationGroup
     * @throws Exception
     */
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "getTimesheetConfiguration",
            description = "Should be return an configuration related details .")
    public void getTimesheetConfiguration(UUID configurationGroupId,
            ConfigurationGroup persistedConfigurationGroup,
            List<HolidayConfiguration> holidayConfigurations) throws Exception {
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING)))
                .thenReturn(persistedConfigurationGroup);
        when(holidayConfigurationRepository.findByConfigurationGroup(any(ConfigurationGroup.class)))
                .thenReturn(holidayConfigurations);
        ConfigurationGroupDTO persistedConfigurationGroupDTO =
                configurationGroupServiceImpl.getConfigurationGroup(configurationGroupId);
        ConfigurationGroupMapper.INSTANCE.configurationGroupDTOToconfigurationGroupResourceMap(
                persistedConfigurationGroupDTO);
        // --Mock call verification
        verify(configurationGroupRepository, times(1)).findOne(configurationGroupId);
        // --Service call assertion
        assertThat(null != persistedConfigurationGroupDTO).isTrue();
        assertThat(configurationGroupId == persistedConfigurationGroupDTO.getConfigurationGroupId())
                .isTrue();
        assertThat(null != persistedConfigurationGroupDTO.getConfigurationGroupId()).isTrue();
        assertThat(StringUtils.equals("NewYork City Timesheet Setup",
                persistedConfigurationGroupDTO.getConfigurationGroupName())).isTrue();
        assertThat(persistedConfigurationGroupDTO.getConfigurationGroupLocation() != null).isTrue();
        assertThat(persistedConfigurationGroupDTO.getHolidayConfiguration() != null).isTrue();
    }

    /**
     * Test case : getTimesheetConfigurationRepositoryNotRespond
     * 
     * @param configurationGroupId
     * @throws Exception
     */
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "getTimesheetConfigurationRepositoryNotRespond",
            expectedExceptions = Exception.class,
            description = "Should be throw the Exception, Since repository has to throw the exception.")
    public void getTimesheetConfigurationRepositoryNotRespond(UUID configurationGroupId)
            throws Exception {
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING))).thenThrow(new Exception());
        configurationGroupServiceImpl.getConfigurationGroup(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING));
        // --Mock call verification
        verify(configurationGroupRepository, times(1)).findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING));
    }


    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "getHolidayConfiguration",
            description = "Should be return Holiday Configuration.")
    public void getHolidayConfiguration(UUID configurationGroupId,
            List<HolidayConfiguration> holidayConfiguratoins) throws Exception {
        when(holidayConfigurationRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING)))
                .thenReturn(holidayConfiguratoins.get(0));
        configurationGroupServiceImpl.getHolidayConfiguration(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING));

    }


    /**
     * Test case : getTimesheetConfigurationFailureCase
     * 
     * @param configurationGroupId
     * @throws Exception
     */
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "getTimesheetConfigurationFailureCase",
            expectedExceptions = ConfigurationNotFoundException.class,
            description = "Data does not exist in database should be throw exception.")
    public void getTimesheetConfigurationFailureCase(UUID configurationGroupId) throws Exception {
        configurationGroupServiceImpl.getConfigurationGroup(configurationGroupId);
        // --Mock call verification
        verify(configurationGroupRepository, times(0)).findOne(UUID.randomUUID());
    }

    /**
     * Test case : deleteTimesheetConfiguration
     * 
     * @param configurationGroupId
     * @throws Exception
     */
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "deleteTimesheetConfiguration",
            expectedExceptions = ConfigurationNotFoundException.class,
            description = "Should be delete the configuration for given id.")
    public void deleteTimesheetConfiguration(UUID configurationGroupId,
            ConfigurationGroup configurationGroup) throws Exception {
        configurationGroupRepository.findOne(configurationGroupId);
        configurationGroupServiceImpl.deleteConfigurationGroup(configurationGroupId);
        verify(configurationGroupRepository, times(0)).findOne(configurationGroupId);

    }

    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "deleteTimesheetConfigurationWithData",
            description = "Should be replace with Active Flag as N for the given id.")
    public void deleteTimesheetConfigurationWithData(UUID configurationGroupId,
            ConfigurationGroup configurationGroup) throws Exception {
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING)))
                .thenReturn(configurationGroup);
        configurationGroupRepository.saveAndFlush(configurationGroup);
        configurationGroupServiceImpl.deleteConfigurationGroup(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING));

    }


    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "saveHolidayConfiguration",
            description = "Should be save the Holiday configuration.")
    public void saveHolidayConfiguration(ConfigurationGroup configurationGroup,
            ConfigurationGroupDTO configurationGroupDTO, UUID configurationGroupId)
            throws Exception {
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING)))
                .thenReturn(configurationGroup);
        when(holidayConfigurationRepository.save(configurationGroup.getHolidayConfiguration()))
                .thenReturn(configurationGroup.getHolidayConfiguration());
        configurationGroupServiceImpl.createHolidayConfiguration(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING),
                configurationGroupDTO.getHolidayConfiguration());


    }


    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "saveHolidayConfiguration",
            description = "Should be update the Holiday configuration")
    public void updateHolidayConfiguration(ConfigurationGroup configurationGroup,
            ConfigurationGroupDTO configurationGroupDTO, UUID configurationGroupId)
            throws Exception {
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING)))
                .thenReturn(configurationGroup);
        when(holidayConfigurationRepository.save(configurationGroup.getHolidayConfiguration()))
                .thenReturn(configurationGroup.getHolidayConfiguration());
        configurationGroupServiceImpl.createHolidayConfiguration(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING),
                configurationGroupDTO.getHolidayConfiguration());
        configurationGroupServiceImpl.updateHolidayConfiguration(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING),
                configurationGroupDTO.getHolidayConfiguration());

    }

    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "saveHolidayConfiguration",
            description = "Should be return the Holiday configuration.")
    public void getConfiguredHolidayConfigurations(ConfigurationGroup configurationGroup,
            ConfigurationGroupDTO configurationGroupDTO, UUID configurationGroupId)
            throws Exception {
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING)))
                .thenReturn(configurationGroup);
        when(holidayConfigurationRepository.save(configurationGroup.getHolidayConfiguration()))
                .thenReturn(configurationGroup.getHolidayConfiguration());
        when(holidayConfigurationRepository.findByConfigurationGroup(configurationGroup))
                .thenReturn(configurationGroup.getHolidayConfiguration());
        configurationGroupServiceImpl.getConfiguredHolidayConfigurations(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING));

    }

    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "saveHolidayConfiguration",
            expectedExceptions = ConfigurationException.class,
            description = "Should be return the Holiday configuration.")
    public void getConfiguredHolidayConfigurationsWithException(
            ConfigurationGroup configurationGroup, ConfigurationGroupDTO configurationGroupDTO,
            UUID configurationGroupId) throws Exception {
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING))).thenReturn(null);
        configurationGroupServiceImpl.getConfiguredHolidayConfigurations(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING));

    }

    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "getNotificationConfigurations",
            description = "Should be return the Notification configuration")
    public void getConfiguredNotificationConfigurations(ConfigurationGroup configurationGroup,
            UUID configurationGroupId) throws Exception {
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING)))
                .thenReturn(configurationGroup);
        configurationGroupServiceImpl.getConfiguredNotificationConfigurations(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING));

    }

    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "getNotificationConfigurations",
            expectedExceptions = ConfigurationException.class,
            description = "Should be return the Notification configuration")
    public void getConfiguredNotificationConfigurationsWithException(
            ConfigurationGroup configurationGroup, UUID configurationGroupId) throws Exception {
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING))).thenReturn(null);
        configurationGroupServiceImpl.getConfiguredNotificationConfigurations(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING));

    }

    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "createNotification",
            description = "Should be create the Notification configuration")
    public void createNotification(ConfigurationGroup configurationGroup,
            ConfigurationGroupDTO configurationGroupDTO,
            List<NotificationConfiguration> notificationConfigurations, UUID configurationGroupId,
            List<NotificationAttribute> notificationAttributes) throws Exception {
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING)))
                .thenReturn(configurationGroup);
        when(notificationConfigurationRepository.save(notificationConfigurations))
                .thenReturn(notificationConfigurations);
        when(notificationAttributeRepository.findByUserGroupCategory(
                configurationGroup.getUserGroupCategory())).thenReturn(notificationAttributes);
        configurationGroupServiceImpl.createNotification(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING),
                configurationGroupDTO.getNotificationAttribute());
    }

    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "createNotification", expectedExceptions = ConfigurationException.class,
            description = "Should be throw Exception")
    public void createNotificationWithException(ConfigurationGroup configurationGroup,
            ConfigurationGroupDTO configurationGroupDTO,
            List<NotificationConfiguration> notificationConfigurations, UUID configurationGroupId,
            List<NotificationAttribute> notificationAttributes) throws Exception {
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING))).thenReturn(null);
        configurationGroupServiceImpl.createNotification(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING),
                configurationGroupDTO.getNotificationAttribute());
    }

    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "updateNotification",
            description = "Should be update the Notification Attributes")
    public void updateNotification(ConfigurationGroup configurationGroup,
            ConfigurationGroupDTO configurationGroupDTO,
            List<NotificationConfiguration> notificationConfigurations, UUID configurationGroupId,
            List<NotificationAttribute> notificationAttributes) throws Exception {
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING)))
                .thenReturn(configurationGroup);
        when(notificationConfigurationRepository.save(notificationConfigurations))
                .thenReturn(notificationConfigurations);
        when(notificationAttributeRepository.findByUserGroupCategory(
                configurationGroup.getUserGroupCategory())).thenReturn(notificationAttributes);
        configurationGroupServiceImpl.updateNotification(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING),
                configurationGroupDTO.getNotificationAttribute());
    }


    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "updateNotification", expectedExceptions = ConfigurationException.class,
            description = "Should be throw Exception")
    public void updateNotificationWithException(ConfigurationGroup configurationGroup,
            ConfigurationGroupDTO configurationGroupDTO,
            List<NotificationConfiguration> notificationConfigurations, UUID configurationGroupId,
            List<NotificationAttribute> notificationAttributes) throws Exception {
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING))).thenReturn(null);
        configurationGroupServiceImpl.updateNotification(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING),
                configurationGroupDTO.getNotificationAttribute());
    }

    /**
     * Test case : deleteTimesheetHolidayConfiguration Description : Should be delete the
     * HolidayConfiguration for given id.
     * 
     * @param timeSheetHolidayId
     * @throws Exception
     */
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "deleteTimesheetHolidayConfiguration",
            description = "Should be delete the HolidayConfiguration for given id.")
    public void deleteTimesheetHolidayConfiguration(UUID timeSheetHolidayId) throws Exception {
        configurationGroupServiceImpl.deleteHolidayConfiguration(timeSheetHolidayId);
        // --Mock call verification
//        verify(holidayConfigurationRepository, times(1)).delete(UUID.randomUUID());
    }

    /**
     * Test case : deleteTimesheetNotificationConfiguration
     * 
     * @param timeSheetHolidayId
     * @throws Exception
     */
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "deleteTimesheetNotificationConfiguration",
            description = "Should be delete the NotificationConfiguration for given id.")
    public void deleteTimesheetNotificationConfiguration(UUID timesheetNotificationId)
            throws Exception {
        configurationGroupServiceImpl.deleteNotificationConfiguration(timesheetNotificationId);
        // --Mock call verification
//        verify(notificationConfigurationRepository, times(1)).delete(UUID.randomUUID());
    }
    
    /**
     * Test case : getAllTimesheetConfiguration
     * 
     * @param serviceInstanceList
     * @param responseType
     * @param responseEntity
     * @param pageTimesheetConfigGroup
     * @param pageable
     * @param url
     * @param group
     * @throws Exception
     */
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "getAllTimesheetConfigurationView",
            description = "Should be successfully fetch all the configuration details along with officeId mapped with office name using the rest call of OfficeCommend of HystrixCommand.")
    public void getAllTimesheetConfigurationFromView(
            Page<ConfigurationGroupView> pageTimesheetConfigGroup, Pageable pageable)
            throws Exception {
        when(configurationGroupViewRepository.findAll(pageable))
                .thenReturn(pageTimesheetConfigGroup);
        Page<ConfigurationGroupViewDTO> persistedConfigurationGroupDTOs =
                configurationGroupServiceImpl.getAllConfigurationGroupsView(pageable);
        // --Mock call verification
        verify(configurationGroupViewRepository, times(1)).findAll(any(Pageable.class));
        // --Service call assertion
        assertThat(null != persistedConfigurationGroupDTOs).isTrue();

    }
    
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "getAllTimesheetConfigurationFromViewForNullPageableObject",
            description = "Should be successfully fetch all the configuration details along with officeId mapped with office name using the rest call of OfficeCommend of HystrixCommand.")
    public void getAllTimesheetConfigurationFromViewForNullPageableObject(
            Page<ConfigurationGroupView> pageTimesheetConfigGroup, Pageable pageable)
            throws Exception {
        when(configurationGroupViewRepository.findAll(pageable))
                .thenReturn(pageTimesheetConfigGroup);
        if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
        	pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.Direction.DESC, "createdDate");
        }
        Page<ConfigurationGroupViewDTO> persistedConfigurationGroupDTOs =
                configurationGroupServiceImpl.getAllConfigurationGroupsView(pageable);
        // --Mock call verification
        verify(configurationGroupViewRepository, times(1)).findAll(any(Pageable.class));
        // --Service call assertion
        assertThat(null != persistedConfigurationGroupDTOs).isTrue();
    }
    
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "getTimesheetConfigurationWithOutNotificationData",
            description = "Should be return an configuration related details .")
    public void getTimesheetConfigurationWithOutNotificationData(UUID configurationGroupId,
            ConfigurationGroup persistedConfigurationGroup) throws Exception {
    	
        when(configurationGroupRepository.findOne(UUID.fromString(ConfigurationGroupServiceTest.CONFIG_UUID_STRING))).thenReturn(persistedConfigurationGroup);
        ConfigurationGroupDTO persistedConfigurationGroupDTO = configurationGroupServiceImpl.getConfigurationGroup(UUID.fromString(CONFIG_UUID_STRING));
        // --Mock call verification
        verify(configurationGroupRepository, times(1)).findOne(UUID.fromString(CONFIG_UUID_STRING));
        // --Service call assertion
        assertThat(null != persistedConfigurationGroupDTO).isTrue();
    }


    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class, dataProvider = "getConfiguredOfficeLocationsByJobTitle")
    public void getConfiguredOfficeLocationsByJobTitle(List<ServiceInstance> serviceInstanceList, 
    		ResponseEntity<List<OfficeLocationDTO>> responseEntity, List<String> jobTitleIds, 
    		List<Long> configuredOfficeLocations, UUID configId,ConfigurationGroup configurationGroup) throws Exception {
    	
        when(discoveryClient.getInstances(any(String.class))).thenReturn(serviceInstanceList);
        MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
        String commandUrl = "http://COMMONSERVICEMANAGEMENT/common/officelocations";
		ParameterizedTypeReference<List<OfficeLocationDTO>> resType = new ParameterizedTypeReference<List<OfficeLocationDTO>>() {
		};
		OfficeLocationDTO officeLocationDTO = mock(OfficeLocationDTO.class);
		List<OfficeLocationDTO> locationDTOs = Arrays.asList(officeLocationDTO);
        ResponseEntity<List<OfficeLocationDTO>> res = new ResponseEntity<>(locationDTOs, HttpStatus.OK);
        HttpHeaders httpHeaders = new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION,
						new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
			}
		};
		when(restTemplate.exchange(commandUrl, HttpMethod.GET, new org.springframework.http.HttpEntity(httpHeaders), resType)).thenReturn(res);
        when(configurationGroupRepository.findConfiguredOfficeIdsByUserGroupIds(any(List.class))).thenReturn(configuredOfficeLocations);
        when(configurationGroupRepository.findOne(UUID.fromString("461b98be-db2e-441d-903d-d75f7296763a"))).thenReturn(configurationGroup);
        configurationGroupServiceImpl.getConfiguredOfficeIdsByConfigIdOrUserGroupIds(configId, String.join(",", jobTitleIds));
    }

    @SuppressWarnings("unchecked")
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "getOfficeLocationsByConfigurationGroup")
    public void getOfficeLocationsByConfigurationGroup(List<ServiceInstance> serviceInstanceList,
            UUID configId, ResponseEntity<List<OfficeLocationDTO>> responseEntity,
            ConfigurationGroup configurationGroup) throws Exception {
        when(discoveryClient.getInstances(any(String.class))).thenReturn(serviceInstanceList);
        MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
        String commandUrl = "http://COMMONSERVICEMANAGEMENT/common/officelocations";
		ParameterizedTypeReference<List<OfficeLocationDTO>> resType = new ParameterizedTypeReference<List<OfficeLocationDTO>>() {};
		OfficeLocationDTO officeLocationDTO = mock(OfficeLocationDTO.class);
		List<OfficeLocationDTO> locationDTOs = Arrays.asList(officeLocationDTO);
        ResponseEntity<List<OfficeLocationDTO>> res = new ResponseEntity<>(locationDTOs, HttpStatus.OK);
        HttpHeaders httpHeaders = new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION,
						new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
			}
		};
		when(restTemplate.exchange(commandUrl, HttpMethod.GET, new org.springframework.http.HttpEntity(httpHeaders), resType)).thenReturn(res);
        when(configurationGroupRepository.findOne(UUID.fromString("461b98be-db2e-441d-903d-d75f7296763a"))).thenReturn(configurationGroup);
        configurationGroupServiceImpl.getOfficeLocationsByConfigurationGroup(UUID.fromString("461b98be-db2e-441d-903d-d75f7296763a"));
    }

    @SuppressWarnings("unchecked")
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class, dataProvider = "getConfiguredOfficeLocationsByJobTitle")
    public void getConfiguredOfficeLocationsByJobTitleGroupIdBlank(
            List<ServiceInstance> serviceInstanceList,
            ResponseEntity<List<OfficeLocationDTO>> responseEntity, List<String> jobTitleIds,
            List<Long> configuredOfficeLocations, UUID configId, ConfigurationGroup configurationGroup) throws Exception {
    	
        when(discoveryClient.getInstances(any(String.class))).thenReturn(serviceInstanceList);
        MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
        String commandUrl = "http://COMMONSERVICEMANAGEMENT/common/officelocations";
		ParameterizedTypeReference<List<OfficeLocationDTO>> resType = new ParameterizedTypeReference<List<OfficeLocationDTO>>() {
		};
		OfficeLocationDTO officeLocationDTO = mock(OfficeLocationDTO.class);
		List<OfficeLocationDTO> locationDTOs = Arrays.asList(officeLocationDTO);
        ResponseEntity<List<OfficeLocationDTO>> res = new ResponseEntity<>(locationDTOs, HttpStatus.OK);
        HttpHeaders httpHeaders = new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION,
						new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
			}
		};
		when(restTemplate.exchange(commandUrl, HttpMethod.GET, new org.springframework.http.HttpEntity(httpHeaders), resType)).thenReturn(res);
        when(configurationGroupRepository.findConfiguredOfficeIdsByUserGroupIds(any(List.class))).thenReturn(configuredOfficeLocations);
        when(configurationGroupRepository.findOne(UUID.fromString("461b98be-db2e-441d-903d-d75f7296763a"))).thenReturn(configurationGroup);
        configurationGroupServiceImpl.getConfiguredOfficeIdsByConfigIdOrUserGroupIds(null, String.join(",", jobTitleIds));
    }
    
    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "getJobTitleByConfigurationGroup")
    public void getJobTitleByConfigurationGroup(List<ServiceInstance> serviceInstanceList,
            UUID configId, ResponseEntity<List<UserGroupDTO>> responseEntity,
            ConfigurationGroup configurationGroup) throws Exception {
    	
        when(discoveryClient.getInstances(any(String.class))).thenReturn(serviceInstanceList);
        MockHttpServletRequest request = new MockHttpServletRequest();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		String requestedToken = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJieVNYRHcybGVLSi1ZMnYta1pVVlpjMVd1ekpsNm1udXZrbDMxLVdFbUNzIn0.eyJqdGkiOiIwMWQzYmVkZS04NmEyLTQzZTQtYmFkZC05MjkxODdiM2NhOGQiLCJleHAiOjE0OTUwMDIzNTYsIm5iZiI6MCwiaWF0IjoxNDk0OTk4NzU2LCJpc3MiOiJodHRwOi8vMTkyLjE2OC42LjM1L2F1dGgvcmVhbG1zL3RpbWVzaGVldCIsImF1ZCI6ImFkbWluLWNsaSIsInN1YiI6IjZlODJhYmI4LWU2OWYtNDJiNS05MTEwLTNjMGVjZWQ5ODhjNSIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkbWluLWNsaSIsIm5vbmNlIjoiNGIxNDM4YWEtMGEwNC00YzU1LTg2NjQtY2I3MWI4NGU0YTZmIiwiYXV0aF90aW1lIjoxNDk0OTk4MzM1LCJzZXNzaW9uX3N0YXRlIjoiMzI5NGNmMTYtYjllOC00MmM5LTk4MGQtNjUxMjY2MzM3OWQwIiwiYWNyIjoiMCIsImNsaWVudF9zZXNzaW9uIjoiYWRjMGI0ZDYtYWVmOC00NDUyLWE0NjAtMzVjN2I5NTdhM2FkIiwiYWxsb3dlZC1vcmlnaW5zIjpbXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbInVtYV9hdXRob3JpemF0aW9uIl19LCJyZXNvdXJjZV9hY2Nlc3MiOnt9LCJuYW1lIjoiQWxsICBJbiBBbGwiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhbGxpbmFsbEB0ZWNobWFuZ28ubmV0IiwiZ2l2ZW5fbmFtZSI6IkFsbCAiLCJmYW1pbHlfbmFtZSI6IkluIEFsbCIsImVtYWlsIjoiYWxsaW5hbGxAdGVjaG1hbmdvLm5ldCJ9.HbEwOMvsKJFbNC-WvNy5hYS730Q7TB0jmDEkAlB-Af4bRcrcnZyaauDfxL2jUyghk0ZVa8SKiTqyGi-QyCF05J0xtCaSHp8gqDwMFI2n2T4n6UzEB46l1KnHkxXgSyRuWtK7gM8dWrRo1stY-Ffmic9Ykz2_yZsUuZ6e1hob7GBu9AadW2SxEXWBv2mTw0NpBYAD02AlNYBu7UvSozyuUde3Qlca0jCpWpDTQ9QIao7dFcnAIt2fqAxJe3bUVDnJ3kCAlbBj_3DoXKpfSnyGyYP8_S7rll15tZkXm35z8FDewang3fDXWeqk1T307lILvgIDCSauheuqRJYlBVm5Yw";
		request.setAttribute("token", requestedToken);
        String commandUrl = "http://COMMONSERVICEMANAGEMENT/common/usergroups";
		ParameterizedTypeReference<List<UserGroupDTO>> resType = new ParameterizedTypeReference<List<UserGroupDTO>>() {
		};
		UserGroupDTO userGroupDTO = mock(UserGroupDTO.class);
		List<UserGroupDTO> userGroupDTOs = Arrays.asList(userGroupDTO);
        ResponseEntity<List<UserGroupDTO>> res = new ResponseEntity<>(userGroupDTOs, HttpStatus.OK);
        HttpHeaders httpHeaders = new HttpHeaders() {
			private static final long serialVersionUID = -3670115299291671935L;
			{
				set(HttpHeaders.AUTHORIZATION,
						new StringBuilder("Bearer").append(" ").append(requestedToken).toString());
			}
		};
		when(restTemplate.exchange(commandUrl, HttpMethod.GET, new org.springframework.http.HttpEntity(httpHeaders), resType)).thenReturn(res);
        when(configurationGroupRepository.findOne(UUID.fromString("461b98be-db2e-441d-903d-d75f7296763a"))).thenReturn(configurationGroup);
        configurationGroupServiceImpl.getUserGroupByConfigurationGroup(UUID.fromString("461b98be-db2e-441d-903d-d75f7296763a"));
    }

    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "createHolidayConfigurationValidation",
            description = "Should be validate the Holiday date success.")
    public void createHolidayConfigurationValidation(
            List<HolidayConfigurationDTO> holidayConfigurationDTOs) throws Exception {
        configurationGroupServiceImpl.createHolidayConfigurationValidation(holidayConfigurationDTOs);
    }

    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "createHolidayConfigurationValidationException",
            expectedExceptions = HolidayConfigurationException.class,
            description = "Should be validate the Holiday date AlreadyExists.")
    public void createHolidayConfigurationValidationException(
            List<HolidayConfigurationDTO> holidayConfigurationDTOs) throws Exception {
        configurationGroupServiceImpl.createHolidayConfigurationValidation(holidayConfigurationDTOs);
    }

    @Test(dataProviderClass = ConfigurationGroupTestDataProvider.class,
            dataProvider = "createHolidayConfigurationDateValidationException",
            expectedExceptions = HolidayDateConfigurationException.class,
            description = "Should be validate the Holiday date AlreadyExists.")
    public void createHolidayConfigurationDateValidationException(
            List<HolidayConfigurationDTO> holidayConfigurationDTOs) throws Exception {
        configurationGroupServiceImpl.createHolidayConfigurationValidation(holidayConfigurationDTOs);
    }
}
