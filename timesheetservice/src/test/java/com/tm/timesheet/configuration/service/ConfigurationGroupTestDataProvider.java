package com.tm.timesheet.configuration.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.DataProvider;

import com.tm.timesheet.configuration.domain.ConfigurationGroup;
import com.tm.timesheet.configuration.domain.ConfigurationGroup.ActiveFlagEnum;
import com.tm.timesheet.configuration.domain.ConfigurationGroup.EffectiveFlagEnum;
import com.tm.timesheet.configuration.domain.ConfigurationGroup.UserGroupCategoryEnum;
import com.tm.timesheet.configuration.domain.ConfigurationGroupLocation;
import com.tm.timesheet.configuration.domain.ConfigurationGroupUserJobTitle;
import com.tm.timesheet.configuration.domain.ConfigurationGroupView;
import com.tm.timesheet.configuration.domain.HolidayConfiguration;
import com.tm.timesheet.configuration.domain.NotificationAttribute;
import com.tm.timesheet.configuration.domain.NotificationAttribute.NotificationAttributeDisableEnum;
import com.tm.timesheet.configuration.domain.NotificationAttribute.ValidationTypeEnum;
import com.tm.timesheet.configuration.domain.NotificationConfiguration;
import com.tm.timesheet.configuration.domain.TimesheetConfiguration;
import com.tm.timesheet.configuration.domain.TimesheetHourConfiguration;
import com.tm.timesheet.configuration.domain.TimesheetTimeConfiguration;
import com.tm.timesheet.configuration.enums.TimeCalculationEnum;
import com.tm.timesheet.configuration.enums.WorkInputEnum;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupDTO;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupLocationDTO;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupUserJobTitleDTO;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupViewDTO;
import com.tm.timesheet.configuration.service.dto.HolidayConfigurationDTO;
import com.tm.timesheet.configuration.service.dto.NotificationAttributeDTO;
import com.tm.timesheet.configuration.service.dto.NotificationConfigurationDTO;
import com.tm.timesheet.configuration.service.dto.OfficeLocationDTO;
import com.tm.timesheet.configuration.service.dto.UserGroupDTO;

public class ConfigurationGroupTestDataProvider {

    @DataProvider(name = "saveTimesheetConfigurationForTimeStampInput")
    public static Iterator<Object[]> saveTimesheetConfiguration() {
        return saveTimesheetConfigurationWithInputType(WorkInputEnum.valueOf("T").workInput());
    }

    @DataProvider(name = "saveTimesheetConfigurationForHourInput")
    public static Iterator<Object[]> saveTimesheetConfigurationForHourInput() {
        return saveTimesheetConfigurationWithInputType(WorkInputEnum.valueOf("H").workInput());
    }

    @DataProvider(name = "saveTimesheetConfigurationWrongEnumValue")
    public static Iterator<Object[]> saveTimesheetConfigurationWrongEnumValue() {
        ConfigurationGroupDTO timesheetConfigurationDTO =
                prepareTimesheetConfigurationData(WorkInputEnum.valueOf("H").workInput());
        timesheetConfigurationDTO.setActiveFlag("yes");
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timesheetConfigurationDTO});
        return testData.iterator();
    }

    @DataProvider(name = "saveTimesheetConfigurationWrongWorkInputEnum")
    public static Iterator<Object[]> saveTimesheetConfigurationWrongWorkInputEnum() {
        ConfigurationGroupDTO timesheetConfigurationDTO =
                prepareTimesheetConfigurationData(WorkInputEnum.valueOf("H").workInput());
        timesheetConfigurationDTO.setDefaultInput("HRS");
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timesheetConfigurationDTO});
        return testData.iterator();
    }


    @DataProvider(name = "saveTimesheetConfigurationOnlyWithMandatoryField")
    public static Iterator<Object[]> saveTimesheetConfigurationOnlyWithMandatoryField() {
        ConfigurationGroupDTO timesheetConfigurationDTO = new ConfigurationGroupDTO();
        List<ConfigurationGroupLocationDTO> configurationGroupLocationDTOList = new ArrayList<>();

        ConfigurationGroupLocationDTO tsconfigGroupLoc = null;
        configurationGroupLocationDTOList.add(tsconfigGroupLoc);
        timesheetConfigurationDTO.setConfigurationGroupLocation(configurationGroupLocationDTOList);
        List<ConfigurationGroupUserJobTitleDTO> configurationGroupUserJobTitleList =
                new ArrayList<>();
        configurationGroupLocationDTOList
                .add(prepareTimesheetConfigurationGroupLocationDTO(1256L, "Test Office", null));
        configurationGroupUserJobTitleList.add(prepareConfigurationGroupUserJobTitleDTO(
                UUID.randomUUID().toString(), UUID.randomUUID().toString(), "Recruiter"));
        timesheetConfigurationDTO
                .setConfigurationGroupUserJobTitle(configurationGroupUserJobTitleList);
        timesheetConfigurationDTO.setDefaultInput(WorkInputEnum.valueOf("H").workInput());
        ConfigurationGroup configurationGroup = new ConfigurationGroup();
        configurationGroup.setTimesheetConfiguration(new TimesheetConfiguration());
        List<ConfigurationGroupLocation> configurationGroupLocationList = new ArrayList<>();
        ConfigurationGroupLocation configurationGroupLocation = new ConfigurationGroupLocation();
        configurationGroupLocation.setGroupLocationId(UUID.randomUUID());
        configurationGroupLocationList.add(configurationGroupLocation);
        configurationGroup.setConfigurationGroupLocation(configurationGroupLocationList);
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timesheetConfigurationDTO, configurationGroup});
        return testData.iterator();
    }


    @DataProvider(name = "saveTimesheetConfigurationReturnNull")
    public static Iterator<Object[]> saveTimesheetConfigurationReturnNull() {
        // -- Prepare the test data for timesheetConfiguration with TimeStamp input
        ConfigurationGroupDTO timesheetConfigurationDTO =
                prepareTimesheetConfigurationData(WorkInputEnum.valueOf("H").workInput());

        // -- prepare the timesheetConfigurationGrop object for repository.
        ConfigurationGroup configurationGroup = prepareConfigGroupData(timesheetConfigurationDTO);

        Set<Object[]> testData = new LinkedHashSet<Object[]>();

        testData.add(new Object[] {timesheetConfigurationDTO, configurationGroup});
        return testData.iterator();
    }

    @DataProvider(name = "updateTimesheetConfigurationForHourInput")
    public static Iterator<Object[]> updateTimesheetConfigurationForHourInput() {
        return updateTimesheetConfiguration(WorkInputEnum.valueOf("H").workInput());
    }

    @DataProvider(name = "updateTimesheetConfigurationForTimeInput")
    public static Iterator<Object[]> updateTimesheetConfigurationForTimeInput() {
        return updateTimesheetConfiguration(WorkInputEnum.valueOf("T").workInput());
    }

    @DataProvider(name = "updateTimesheetConfigurationRecordNotExist")
    public static Iterator<Object[]> updateTimesheetConfigurationRecordNotExist() {
        ConfigurationGroupDTO timesheetConfigurationDTO =
                prepareTimesheetConfigurationData(WorkInputEnum.valueOf("H").workInput());
        timesheetConfigurationDTO.setConfigurationGroupId(UUID.randomUUID());
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timesheetConfigurationDTO});
        return testData.iterator();
    }

    @DataProvider(name = "updateTimesheetConfigurationSetOtDtAsNull")
    public static Iterator<Object[]> updateTimesheetConfigurationSetOtDtAsNull() {
    	ConfigurationGroupDTO timesheetConfigurationDTO =
                prepareTimesheetConfigurationData(WorkInputEnum.valueOf("H").workInput());
    	timesheetConfigurationDTO.setOtMaxHours(null);
	    timesheetConfigurationDTO.setDtMaxHours(null);
        timesheetConfigurationDTO.setConfigurationGroupId(UUID.randomUUID());
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timesheetConfigurationDTO});
        return testData.iterator();
    }
    
    @DataProvider(name = "getAllTimesheetConfigurationView")
    public static Iterator<Object[]> getAllTimesheetConfigurationView() {

        // -- Prepare the test data for TimesheetConfigurationGroup
        List<ConfigurationGroupView> configGroups = new ArrayList<>();
        IntStream.range(0, 1).forEach(range -> {
            ConfigurationGroupViewDTO configurationGroupViewDTO =
                    prepareGetAllConfigurationGroupViewDTO();
            ConfigurationGroupView configurationGroup =
                    prepareConfigurationGroupView(configurationGroupViewDTO);
            configGroups.add(configurationGroup);
        });

        Pageable pageable = null ;
        
        Page<ConfigurationGroupView> pageConfigGroup = new PageImpl<>(configGroups, pageable, 5);
       
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {pageConfigGroup, pageable});
        return testData.iterator();
    }
    
    @DataProvider(name = "getAllTimesheetConfigurationFromViewForNullPageableObject")
    public static Iterator<Object[]> getAllTimesheetConfigurationFromViewForNullPageableObject() {

        // -- Prepare the test data for TimesheetConfigurationGroup
        List<ConfigurationGroupView> configGroups = new ArrayList<>();
        IntStream.range(0, 1).forEach(range -> {
            ConfigurationGroupViewDTO configurationGroupViewDTO =
                    prepareGetAllConfigurationGroupViewDTO();
            ConfigurationGroupView configurationGroup =
                    prepareConfigurationGroupView(configurationGroupViewDTO);
            configGroups.add(configurationGroup);
        });

        Pageable pageable = null ;
        
        Page<ConfigurationGroupView> pageConfigGroup = new PageImpl<>(configGroups, pageable, 5);
       
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {pageConfigGroup, pageable});
        return testData.iterator();
    }

    @DataProvider(name = "getTimesheetConfigurationRepositoryNotRespond")
    public static Iterator<Object[]> getTimesheetConfigurationRepositoryNotRespond() {
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {UUID.randomUUID().toString()});
        return testData.iterator();
    }

    @DataProvider(name = "getTimesheetConfiguration")
    public static Iterator<Object[]> getTimesheetConfiguration() {
        // -- Prepare the test data for TimesheetConfigurationGroup
        ConfigurationGroupDTO timesheetConfigurationDTO =
                prepareTimesheetConfigurationData(WorkInputEnum.valueOf("T").workInput());
        ConfigurationGroup configurationGroup = prepareConfigGroupData(timesheetConfigurationDTO);
        configurationGroup.setEffectiveFlag(EffectiveFlagEnum.Y);
        configurationGroup.setConfigurationGroupId(UUID.fromString("461b98be-db2e-441d-903d-d75f7296763a"));
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {configurationGroup.getConfigurationGroupId(), configurationGroup,
                prepareHolidayConfiguration()});
        return testData.iterator();
    }

    @DataProvider(name = "getTimesheetConfigurationWithOutNotificationData")
    public static Iterator<Object[]> getTimesheetConfigurationWithOutNotificationData() {
        // -- Prepare the test data for TimesheetConfigurationGroup
        ConfigurationGroupDTO timesheetConfigurationDTO =
                prepareTimesheetConfigurationData(WorkInputEnum.valueOf("H").workInput());
        ConfigurationGroup configurationGroup = prepareConfigGroupData(timesheetConfigurationDTO);
        configurationGroup.setConfigurationGroupId(UUID.randomUUID());
        configurationGroup.setNotificationConfiguration(new ArrayList<>());
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(
                new Object[] {configurationGroup.getConfigurationGroupId(), configurationGroup});
        return testData.iterator();
    }

    @DataProvider(name = "getTimesheetConfigurationFailureCase")
    public static Iterator<Object[]> getTimesheetConfigurationFailureCase() {
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {UUID.fromString("461b98be-db2e-441d-903d-d75f7296763a")});
        return testData.iterator();
    }

    @DataProvider(name = "getConfiguredOfficeLocationsByJobTitle")
    public static Iterator<Object[]> getConfiguredOfficeLocationsByJobTitle() {
        List<Long> configuredLocations = new ArrayList<>();
        List<String> jobTitleIds = new ArrayList<>();
        UUID configId = null;
        ConfigurationGroupDTO timesheetConfigurationDTO =
                prepareTimesheetConfigurationData(WorkInputEnum.valueOf("T").workInput());
        ConfigurationGroup configurationGroup = prepareConfigGroupData(timesheetConfigurationDTO);
        List<ConfigurationGroupUserJobTitle> userGroupJobTitleList = new ArrayList<>();
        configuredLocations.add(10L);
        configuredLocations.add(5L);
        configuredLocations.add(9L);
        configuredLocations.add(0L);
        jobTitleIds.add(UUID.randomUUID().toString());
        jobTitleIds.add(UUID.randomUUID().toString());
        // -- prepare the officeLocation data from comTrack rest service.
        List<OfficeLocationDTO> persistedOfficeLocations = new ArrayList<>();
        prepareOfficeLocationDTO(persistedOfficeLocations, 123L);
        prepareOfficeLocationDTO(persistedOfficeLocations, 124L);

        jobTitleIds.add(UUID.randomUUID().toString());
        jobTitleIds.add(UUID.randomUUID().toString());
        configurationGroup.getConfigurationGroupUserJobTitle().forEach(userGroup ->{
            userGroup.setUserGroupId(UUID.fromString(jobTitleIds.get(0)));
            userGroupJobTitleList.add(userGroup);
           
        });
        configurationGroup.setConfigurationGroupUserJobTitle(userGroupJobTitleList);
        configId = UUID.randomUUID();
        ResponseEntity<List<OfficeLocationDTO>> responseEntity =
                new ResponseEntity<>(persistedOfficeLocations, HttpStatus.OK);
        // -- prepare the serviceInstance to fetch the rest call.
        List<ServiceInstance> serviceInstanceList = null;
        // serviceInstanceList = DiscoveryClient.getInstances("COMTRACKSERVICE");


        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {serviceInstanceList, responseEntity, jobTitleIds,
                configuredLocations, configId,configurationGroup});
        return testData.iterator();

    }

    @DataProvider(name = "deleteTimesheetConfiguration")
    public static Iterator<Object[]> deleteTimesheetConfiguration() {
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {UUID.fromString("0e7960c4-2157-4fd5-998f-1ad64645315a"), null});
        return testData.iterator();
    }

    @DataProvider(name = "deleteTimesheetConfigurationWithData")
    public static Iterator<Object[]> deleteTimesheetConfigurationWithData() {
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        ConfigurationGroup configurationGroup = new ConfigurationGroup();
        configurationGroup.setActiveFlag(ActiveFlagEnum.N);
        configurationGroup.setConfigurationGroupId(UUID.randomUUID());
        testData.add(new Object[] {UUID.randomUUID(), configurationGroup});
        return testData.iterator();
    }

    @DataProvider(name = "getHolidayConfiguration")
    public static Iterator<Object[]> getHolidayConfiguration() {
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {UUID.randomUUID(), prepareHolidayConfiguration()});
        return testData.iterator();
    }

    @DataProvider(name = "deleteTimesheetHolidayConfiguration")
    public static Iterator<Object[]> deleteTimesheetHolidayConfiguration() {
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {UUID.randomUUID()});
        return testData.iterator();
    }

    @DataProvider(name = "deleteTimesheetNotificationConfiguration")
    public static Iterator<Object[]> deleteTimesheetNotificationConfiguration() {
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {UUID.randomUUID()});
        return testData.iterator();
    }

    @DataProvider(name = "timesheetConfigurationMapperNullParamCheck")
    public static Iterator<Object[]> timesheetConfigurationMapperNullParamCheck() {
        ConfigurationGroupDTO timesheetConfigurationDTO = null;
        List<ConfigurationGroupLocationDTO> configurationGroupLocationDTOs = null;
        ConfigurationGroupLocationDTO configurationGroupLocationDTO = null;
        HolidayConfigurationDTO holidayConfigurationDTO = null;
        List<HolidayConfigurationDTO> holidayConfigurationDTOs = null;
        NotificationConfigurationDTO notificationConfigurationDTO = null;
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timesheetConfigurationDTO, configurationGroupLocationDTOs,
                configurationGroupLocationDTO, holidayConfigurationDTO, holidayConfigurationDTOs,
                notificationConfigurationDTO});
        return testData.iterator();
    }

    @DataProvider(name = "timesheetConfigurationDTOMapperNullParamCheck")
    public static Iterator<Object[]> timesheetConfigurationDTOMapperNullParamCheck() {
        List<ConfigurationGroupLocation> configurationGroupLocations = null;
        ConfigurationGroupLocation configurationGroupLocation = null;
        HolidayConfiguration holidayConfiguration = null;
        NotificationConfiguration notificationConfiguration = null;
        ConfigurationGroup configurationGroup = null;
        TimesheetConfiguration timesheetConfiguration = null;
        List<HolidayConfiguration> holidayConfigurations = null;
        TimesheetHourConfiguration timesheetHourConfiguration = null;
        TimesheetTimeConfiguration timesheetTimeConfiguration = null;
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {configurationGroupLocations, configurationGroupLocation,
                holidayConfiguration, notificationConfiguration, configurationGroup,
                timesheetConfiguration, holidayConfigurations, timesheetHourConfiguration,
                timesheetTimeConfiguration});
        return testData.iterator();
    }

    @DataProvider(name = "saveHolidayConfiguration")
    public static Iterator<Object[]> saveHolidayConfiguration() {

        ConfigurationGroup configurationGroup = new ConfigurationGroup();
        ConfigurationGroupDTO configurationGroupDTO = new ConfigurationGroupDTO();
        List<HolidayConfigurationDTO> timesheetGroupHolidays = prepareHolidayConfigurationDTO();
        configurationGroupDTO.setHolidayConfiguration(timesheetGroupHolidays);
        List<HolidayConfiguration> holidayConfigurationList = new ArrayList<>();
        prepareGroupHolidayConfiguration(configurationGroupDTO, holidayConfigurationList);
        configurationGroup.setHolidayConfiguration(holidayConfigurationList);
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {configurationGroup, configurationGroupDTO,
                UUID.randomUUID()});
        return testData.iterator();

    }


    @DataProvider(name = "getNotificationConfigurations")
    public static Iterator<Object[]> getNotificationConfigurations() {
        ConfigurationGroup configurationGroup = new ConfigurationGroup();
        ConfigurationGroupDTO configurationGroupDTO = new ConfigurationGroupDTO();
        List<NotificationConfigurationDTO> notificationConfigurationList = new ArrayList<>();
        prepareNotificationConfiguration(notificationConfigurationList);
        configurationGroupDTO.setNotificationConfiguration(notificationConfigurationList);
        List<NotificationConfiguration> notificationConfigurationlist = new ArrayList<>();
        configurationGroup.setNotificationConfiguration(notificationConfigurationlist);
        configurationGroup.setUserGroupCategory(UserGroupCategoryEnum.RCTR);
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {configurationGroup, UUID.randomUUID()});
        return testData.iterator();
    }

    @DataProvider(name = "createNotification")
    public static Iterator<Object[]> createNotification() {
        ConfigurationGroup configurationGroup = new ConfigurationGroup();
        ConfigurationGroupDTO configurationGroupDTO = new ConfigurationGroupDTO();
        List<NotificationConfigurationDTO> notificationConfigurationList = new ArrayList<>();
        configurationGroupDTO.setNotificationConfiguration(notificationConfigurationList);
        List<NotificationAttributeDTO> notificationAttributeDTOs = new ArrayList<>();
        prepareNotificationAttribute(notificationAttributeDTOs);
        notificationConfigurationList = prepareNotificationConfigurationFromAttribute(
                notificationConfigurationList, notificationAttributeDTOs);
        List<NotificationAttribute> notificationAttributes = new ArrayList<>();
        prepareNotificationAttributeEntity(notificationAttributes);
        configurationGroupDTO.setNotificationAttribute(notificationAttributeDTOs);
        List<NotificationConfiguration> notificationConfigurationlist = new ArrayList<>();
        configurationGroup.setNotificationConfiguration(notificationConfigurationlist);
        configurationGroup.setUserGroupCategory(UserGroupCategoryEnum.RCTR);
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {configurationGroup, configurationGroupDTO,
                notificationConfigurationlist, UUID.randomUUID(),
                notificationAttributes});
        return testData.iterator();
    }

    @DataProvider(name = "updateNotification")
    public static Iterator<Object[]> updateNotification() {
        ConfigurationGroup configurationGroup = new ConfigurationGroup();
        ConfigurationGroupDTO configurationGroupDTO = new ConfigurationGroupDTO();
        List<NotificationConfigurationDTO> notificationConfigurationList = new ArrayList<>();
        configurationGroupDTO.setNotificationConfiguration(notificationConfigurationList);
        List<NotificationAttributeDTO> notificationAttributeDTOs = new ArrayList<>();
        prepareNotificationAttribute(notificationAttributeDTOs);
        notificationConfigurationList = prepareNotificationConfigurationFromAttribute(
                notificationConfigurationList, notificationAttributeDTOs);
        List<NotificationAttribute> notificationAttributes = new ArrayList<>();
        prepareNotificationAttributeEntity(notificationAttributes);
        configurationGroupDTO.setNotificationAttribute(notificationAttributeDTOs);
        List<NotificationConfiguration> notificationConfigurationlist = new ArrayList<>();
        configurationGroup.setNotificationConfiguration(notificationConfigurationlist);
        configurationGroup.setUserGroupCategory(UserGroupCategoryEnum.RCTR);
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {configurationGroup, configurationGroupDTO,
                notificationConfigurationlist, UUID.randomUUID(),
                notificationAttributes});
        return testData.iterator();
    }


    @DataProvider(name = "getNotification")
    public static Iterator<Object[]> getNotification() {
        ConfigurationGroupDTO configurationGroupDTO =
                prepareTimesheetConfigurationData(WorkInputEnum.T.toString());

        // -- prepare the timesheetConfigurationGrop object for repository.
        ConfigurationGroup configurationGroup = prepareConfigGroupData(configurationGroupDTO);
        List<NotificationAttribute> notificationAttributes = new ArrayList<>();
        prepareNotificationAttributeEntity(notificationAttributes);
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {configurationGroup, configurationGroupDTO,
                configurationGroup.getNotificationConfiguration(), UUID.randomUUID(),
                notificationAttributes});
        return testData.iterator();
    }


    @DataProvider(name = "getOfficeLocationsByConfigurationGroup")
    public static Iterator<Object[]> getOfficeLocationsByConfigurationGroup() {
        // -- prepare the officeLocation data from comTrack rest service.
        List<OfficeLocationDTO> persistedOfficeLocations = new ArrayList<>();
        prepareOfficeLocationDTO(persistedOfficeLocations, 123L);
        prepareOfficeLocationDTO(persistedOfficeLocations, 124L);

        ResponseEntity<List<OfficeLocationDTO>> responseEntity =
                new ResponseEntity<>(persistedOfficeLocations, HttpStatus.OK);
        // -- prepare the serviceInstance to fetch the rest call.
        List<ServiceInstance> serviceInstanceList = null;
        // serviceInstanceList = DiscoveryClient.getInstances("COMTRACKSERVICE");

        // -- Prepare the test data for TimesheetConfigurationGroup

        ConfigurationGroupDTO timesheetConfigurationDTO =
                prepareTimesheetConfigurationData(WorkInputEnum.valueOf("T").workInput());
        ConfigurationGroup configurationGroup = prepareConfigGroupData(timesheetConfigurationDTO);
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {serviceInstanceList, UUID.randomUUID(),
                responseEntity, configurationGroup});
        return testData.iterator();
    }

    @DataProvider(name = "getJobTitleByConfigurationGroup")
    public static Iterator<Object[]> getJobTitleByConfigurationGroup() {
        // -- prepare the officeLocation data from comTrack rest service.
        List<UserGroupDTO> persistedUserGroups = new ArrayList<>();
        prepareUserGroupDTO(persistedUserGroups, "Manager");
        prepareUserGroupDTO(persistedUserGroups, "Specialist");

        ResponseEntity<List<UserGroupDTO>> responseEntity =
                new ResponseEntity<>(persistedUserGroups, HttpStatus.OK);
        // -- prepare the serviceInstance to fetch the rest call.
        List<ServiceInstance> serviceInstanceList = null;
        // serviceInstanceList = DiscoveryClient.getInstances("COMTRACKSERVICE");

        // -- Prepare the test data for TimesheetConfigurationGroup

        ConfigurationGroupDTO timesheetConfigurationDTO =
                prepareTimesheetConfigurationData(WorkInputEnum.valueOf("T").workInput());
        ConfigurationGroup configurationGroup = prepareConfigGroupData(timesheetConfigurationDTO);


        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {serviceInstanceList, UUID.randomUUID(),
                responseEntity, configurationGroup});
        return testData.iterator();
    }



    private static void prepareGroupHolidayConfiguration(
            ConfigurationGroupDTO configurationGroupDTO,
            List<HolidayConfiguration> holidayConfigurationList) {
        for (HolidayConfigurationDTO holidayConfigurationDTO : configurationGroupDTO
                .getHolidayConfiguration()) {
            HolidayConfiguration holidayConfiguration = new HolidayConfiguration();
            holidayConfiguration.setHolidayDate(holidayConfigurationDTO.getHolidayDate());
            holidayConfiguration
                    .setHolidayDescription(holidayConfigurationDTO.getHolidayDescription());
            holidayConfigurationList.add(holidayConfiguration);
        }
    }

    private static List<HolidayConfigurationDTO> prepareHolidayConfigurationDTO() {
        List<HolidayConfigurationDTO> timesheetGroupHolidays = new ArrayList<>();
        HolidayConfigurationDTO time = new HolidayConfigurationDTO();
        time.setHolidayDate(new Date(1480938734));
        time.setHoliday("02/02/2016");
        time.setHolidayDescription("Thanks Giving Day");
        timesheetGroupHolidays.add(time);
        return timesheetGroupHolidays;
    }

    private static List<HolidayConfiguration> prepareHolidayConfiguration() {
        List<HolidayConfiguration> timesheetGroupHolidays = new ArrayList<>();
        HolidayConfiguration time = new HolidayConfiguration();
        time.setHolidayDate(new Date(1480938734));
        time.setHoliday("02/02/2016");
        time.setHolidayDescription("Thanks Giving Day");
        timesheetGroupHolidays.add(time);
        return timesheetGroupHolidays;
    }

    private static ConfigurationGroupLocationDTO prepareTimesheetConfigurationGroupLocationDTO(
            Long officeId, String officeName, String activeFlag) {
        ConfigurationGroupLocationDTO tsconfigGroupLoc = new ConfigurationGroupLocationDTO();
        tsconfigGroupLoc.setOfficeId(officeId);
        tsconfigGroupLoc.setGroupLocationActiveFlag(activeFlag);
        tsconfigGroupLoc.setGroupLocationId(null);
        tsconfigGroupLoc.setOfficeName(officeName);
        return tsconfigGroupLoc;
    }

    private static ConfigurationGroupUserJobTitleDTO prepareConfigurationGroupUserJobTitleDTO(
            String configGroupUserJobTitleId, String jobTitleId, String jobTitleName) {
        ConfigurationGroupUserJobTitleDTO tsconfigGroupUserJobTitle =
                new ConfigurationGroupUserJobTitleDTO();
        tsconfigGroupUserJobTitle.setConfigurationGroupUserJobTitleId(UUID.fromString(configGroupUserJobTitleId));
        tsconfigGroupUserJobTitle.setUserGroupId(UUID.fromString(jobTitleId));
        tsconfigGroupUserJobTitle.setUserGroupName(jobTitleName);
        return tsconfigGroupUserJobTitle;
    }

    private static void prepareOfficeLocationDTO(List<OfficeLocationDTO> persistedOfficeLocations,
            Long officeId) {
        OfficeLocationDTO officeLocationDTO = new OfficeLocationDTO();
        officeLocationDTO.setActiveFlag(ActiveFlagEnum.Y.toString());
        officeLocationDTO.setOfficeId(officeId);
        officeLocationDTO.setOfficeName("IG");
        officeLocationDTO.setRegionId(456L);
        persistedOfficeLocations.add(officeLocationDTO);
    }

    private static void prepareUserGroupDTO(List<UserGroupDTO> persistedUserGroup,
            String groupName) {
        UserGroupDTO userGroupDTO = new UserGroupDTO();
        userGroupDTO.setGroupId(UUID.randomUUID());
        userGroupDTO.setGroupName(groupName);
        userGroupDTO.setGroupDescription("");
        userGroupDTO.setGroupType("RCTR");
        persistedUserGroup.add(userGroupDTO);
    }

    private static Iterator<Object[]> saveTimesheetConfigurationWithInputType(
            String workInputFlag) {
        // -- Prepare the test data for timesheetConfiguration with TimeStamp input
        ConfigurationGroupDTO timesheetConfigurationDTO =
                prepareTimesheetConfigurationData(workInputFlag);
        // -- prepare the timesheetConfigurationGrop object for repository.
        ConfigurationGroup configurationGroup = prepareConfigGroupData(timesheetConfigurationDTO);
        ConfigurationGroup persistedConfigurationGroup =
                prepareConfigGroupData(timesheetConfigurationDTO);
        persistedConfigurationGroup.setConfigurationGroupId(UUID.randomUUID());
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timesheetConfigurationDTO, configurationGroup,
                persistedConfigurationGroup});
        return testData.iterator();
    }

    private static Iterator<Object[]> updateTimesheetConfiguration(String workInputFlag) {
        // -- Prepare the test data for timesheetConfiguration
        ConfigurationGroupDTO timesheetConfigurationDTO =
                prepareTimesheetConfigurationData(workInputFlag);
        timesheetConfigurationDTO.setConfigurationGroupId(UUID.fromString("461b98be-db2e-441d-903d-d75f7296763a"));
        timesheetConfigurationDTO.setConfigurationId(UUID.randomUUID());
        if (StringUtils.equals(timesheetConfigurationDTO.getDefaultInput(),
                WorkInputEnum.H.name())) {
            timesheetConfigurationDTO.setTimesheetHourId(UUID.randomUUID());
        } else if (StringUtils.equals(timesheetConfigurationDTO.getDefaultInput(),
                WorkInputEnum.T.name())) {
            timesheetConfigurationDTO.setTimesheetTimeId(UUID.randomUUID());
        }
        // -- prepare the timesheetConfigurationGrop object for repository.
        ConfigurationGroup configurationGroup = prepareConfigGroupData(timesheetConfigurationDTO);

        // Created the new record from each update request
        ConfigurationGroup persistedConfigurationGroup =
                prepareConfigGroupData(timesheetConfigurationDTO);
        persistedConfigurationGroup.setConfigurationGroupId(UUID.randomUUID());
        persistedConfigurationGroup
                .setConfigurationGroupName("NewYork City & California Timesheet Setup");
        List<ConfigurationGroupLocation> configurationGroupLocations =
                persistedConfigurationGroup.getConfigurationGroupLocation();
        ConfigurationGroupLocation configurationGroupLocation = new ConfigurationGroupLocation();
        configurationGroupLocation
                .setGroupLocationId(persistedConfigurationGroup.getConfigurationGroupId());
        configurationGroupLocation.setOfficeId(123L);
        configurationGroupLocations.add(configurationGroupLocation);
        persistedConfigurationGroup.setConfigurationGroupLocation(configurationGroupLocations);
        // get exist record using update request congifGroupId
        ConfigurationGroup oldPersistedConfigurationGroup =
                prepareConfigGroupData(timesheetConfigurationDTO);
        oldPersistedConfigurationGroup
                .setConfigurationGroupId(timesheetConfigurationDTO.getConfigurationGroupId());

        ConfigurationGroup updateOldPersistedConfigurationGroup =
                prepareConfigGroupData(timesheetConfigurationDTO);
        updateOldPersistedConfigurationGroup
                .setConfigurationGroupId(timesheetConfigurationDTO.getConfigurationGroupId());

        // update the Old data as effectiveflg 'False' , Since it has create new record.

        Set<Object[]> testData = new LinkedHashSet<Object[]>();

        testData.add(new Object[] {timesheetConfigurationDTO, configurationGroup,
                persistedConfigurationGroup, oldPersistedConfigurationGroup,
                updateOldPersistedConfigurationGroup});

        return testData.iterator();
    }

    private static ConfigurationGroup prepareConfigGroupData(
            ConfigurationGroupDTO timesheetConfigurationDTO) {
        ConfigurationGroup confGrp = new ConfigurationGroup();
        confGrp.setActiveFlag(ActiveFlagEnum.valueOf(timesheetConfigurationDTO.getActiveFlag()));
        confGrp.setEffectiveFlag(
                EffectiveFlagEnum.valueOf(timesheetConfigurationDTO.getEffectiveFlag()));
        confGrp.setConfigurationGroupName(timesheetConfigurationDTO.getConfigurationGroupName());
        confGrp.setUserGroupCategory(UserGroupCategoryEnum.RCTR);

        List<ConfigurationGroupLocation> configurationGroupLocations = new ArrayList<>();
        for (ConfigurationGroupLocationDTO officeLocation : timesheetConfigurationDTO
                .getConfigurationGroupLocation()) {
            ConfigurationGroupLocation configurationGroupLocation =
                    new ConfigurationGroupLocation();
            configurationGroupLocation.setOfficeId(officeLocation.getOfficeId());
            configurationGroupLocation.setGroupLocationId(officeLocation.getGroupLocationId());
            configurationGroupLocations.add(configurationGroupLocation);
        }
        confGrp.setConfigurationGroupLocation(configurationGroupLocations);

        List<ConfigurationGroupUserJobTitle> configurationGroupUserJobTitles = new ArrayList<>();
        for (ConfigurationGroupUserJobTitleDTO configurationGroupUserJobTitleDTO : timesheetConfigurationDTO
                .getConfigurationGroupUserJobTitle()) {
            ConfigurationGroupUserJobTitle configurationGroupUserJobTitle =
                    new ConfigurationGroupUserJobTitle();
            configurationGroupUserJobTitle.setConfigurationGroupUserJobTitleId(
                    configurationGroupUserJobTitleDTO.getConfigurationGroupUserJobTitleId());
            configurationGroupUserJobTitle
                    .setUserGroupId(configurationGroupUserJobTitleDTO.getUserGroupId());
            configurationGroupUserJobTitles.add(configurationGroupUserJobTitle);
        }
        confGrp.setConfigurationGroupUserJobTitle(configurationGroupUserJobTitles);
        TimesheetConfiguration timesheetConfiguration = new TimesheetConfiguration();
        timesheetConfiguration.setWeekStartDay(timesheetConfigurationDTO.getWeekStartDay());
        timesheetConfiguration.setWeekEndDay(timesheetConfigurationDTO.getWeekEndDay());
        timesheetConfiguration.setMinHours(timesheetConfigurationDTO.getMinHours());
        timesheetConfiguration.setMaxHours(timesheetConfigurationDTO.getMaxHours());
        timesheetConfiguration.setTimeCalculation(TimeCalculationEnum.D);
        timesheetConfiguration.setDefaultInput(WorkInputEnum.T);
        timesheetConfiguration.setStMinHours(timesheetConfigurationDTO.getStMinHours());
        timesheetConfiguration.setStMaxHours(timesheetConfigurationDTO.getStMaxHours());


        if (StringUtils.equals(timesheetConfigurationDTO.getDefaultInput(),
                WorkInputEnum.valueOf("H").workInput())) {
            timesheetConfiguration.setTimesheetHourConfiguration(
                    mapTsConfigurationDTOToWorkHour(timesheetConfigurationDTO));
        } else if (StringUtils.equals(timesheetConfigurationDTO.getDefaultInput(),
                WorkInputEnum.valueOf("T").workInput())) {
            timesheetConfiguration.setTimesheetTimeConfiguration(
                    mapTsConfigurationDTOToWorkTimeStamp(timesheetConfigurationDTO));
        }
        confGrp.setTimesheetConfiguration(timesheetConfiguration);

        List<HolidayConfiguration> holidayConfigurations = new ArrayList<>();
        for (HolidayConfigurationDTO timesheetGroupHolidayDTO : timesheetConfigurationDTO
                .getHolidayConfiguration()) {
            HolidayConfiguration holidayConfiguration = new HolidayConfiguration();
            holidayConfiguration
                    .setTimeSheetHolidayId(timesheetGroupHolidayDTO.getTimeSheetHolidayId());
            holidayConfiguration.setHolidayDate(timesheetGroupHolidayDTO.getHolidayDate());
            holidayConfiguration
                    .setHolidayDescription(timesheetGroupHolidayDTO.getHolidayDescription());
            holidayConfiguration.setCreatedBy(1L);
            holidayConfiguration.setCreatedDate(ZonedDateTime.now());
            holidayConfigurations.add(holidayConfiguration);
        }
        confGrp.setHolidayConfiguration(holidayConfigurations);

        List<NotificationConfiguration> notificationConfigurationlist = new ArrayList<>();
        NotificationConfiguration notificationConfiguration = prepareNotificationConfigurationGroup(
                timesheetConfigurationDTO, notificationConfigurationlist);
        confGrp.setNotificationConfiguration(notificationConfigurationlist);

        List<NotificationAttribute> notificationAttributes = new ArrayList<>();
        prepareNotificationAttributeGroup(timesheetConfigurationDTO, notificationAttributes);
        confGrp.setCreatedBy(notificationConfiguration.getCreatedBy());
        confGrp.setCreatedDate(timesheetConfiguration.getCreatedDate());
        return confGrp;
    }

    private static void prepareNotificationAttributeGroup(
            ConfigurationGroupDTO timesheetConfigurationDTO,
            List<NotificationAttribute> notificationAttributes) {
        NotificationAttribute notificationAttribute = new NotificationAttribute();
        notificationAttribute.setNotificationAttributeId(timesheetConfigurationDTO
                .getNotificationAttribute().get(0).getNotificationAttributeId());
        notificationAttribute.setUserGroupCategory(
                timesheetConfigurationDTO.getNotificationAttribute().get(0).getUserGroupCategory());
        notificationAttribute.setNotificationAttributeName(timesheetConfigurationDTO
                .getNotificationAttribute().get(0).getNotificationAttributeName());
        notificationAttribute.setNotificationAttributeInputType(timesheetConfigurationDTO
                .getNotificationAttribute().get(0).getNotificationAttributeInputType());
        notificationAttribute.setNotificationAttributeDefaultValue(timesheetConfigurationDTO
                .getNotificationAttribute().get(0).getNotificationAttributeValue());
        notificationAttribute.setNotificationAttributeFieldSequence(timesheetConfigurationDTO
                .getNotificationAttribute().get(0).getNotificationAttributeFieldSequence());
        notificationAttribute.setNotificationAttributeDisableFlag(timesheetConfigurationDTO
                .getNotificationAttribute().get(0).getNotificationAttributeDisableFlag());
        notificationAttribute.setActiveFlag(
                timesheetConfigurationDTO.getNotificationAttribute().get(0).getActiveFlag());
        notificationAttribute.setDependantAttribute(notificationAttribute);
        notificationAttributes.add(notificationAttribute);
    }

    private static NotificationConfiguration prepareNotificationConfigurationGroup(
            ConfigurationGroupDTO timesheetConfigurationDTO,
            List<NotificationConfiguration> notificationConfigurationlist) {
        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration.setTimesheetNotificationConfigId(timesheetConfigurationDTO
                .getNotificationConfiguration().get(0).getTimesheetNotificationConfigId());
        notificationConfiguration.setNotificationAttributeId(timesheetConfigurationDTO
                .getNotificationAttribute().get(0).getNotificationAttributeId());
        notificationConfiguration.setNotificationAttributeValue(timesheetConfigurationDTO
                .getNotificationAttribute().get(0).getNotificationAttributeValue());
        notificationConfigurationlist.add(notificationConfiguration);
        return notificationConfiguration;
    }

    private static TimesheetTimeConfiguration mapTsConfigurationDTOToWorkTimeStamp(
            ConfigurationGroupDTO timesheetConfigurationDTO) {
        TimesheetTimeConfiguration timesheetTimeConfiguration = new TimesheetTimeConfiguration();
        timesheetTimeConfiguration.setBreakStartTime(timesheetConfigurationDTO.getBreakStartTime());
        timesheetTimeConfiguration.setBreakEndTime(timesheetConfigurationDTO.getBreakEndTime());

        timesheetTimeConfiguration
                .setSundayStartTime(timesheetConfigurationDTO.getSundayStartTime());
        timesheetTimeConfiguration.setSundayEndTime(timesheetConfigurationDTO.getSundayEndTime());

        timesheetTimeConfiguration
                .setMondayStartTime(timesheetConfigurationDTO.getMondayStartTime());
        timesheetTimeConfiguration.setMondayEndTime(timesheetConfigurationDTO.getMondayEndTime());

        timesheetTimeConfiguration
                .setTuesdayStartTime(timesheetConfigurationDTO.getTuesdayStartTime());
        timesheetTimeConfiguration.setTuesdayEndTime(timesheetConfigurationDTO.getTuesdayEndTime());

        timesheetTimeConfiguration
                .setWednesdayStartTime(timesheetConfigurationDTO.getWednesdayStartTime());
        timesheetTimeConfiguration
                .setWednesdayEndTime(timesheetConfigurationDTO.getWednesdayEndTime());

        timesheetTimeConfiguration
                .setThursdayStartTime(timesheetConfigurationDTO.getThursdayStartTime());
        timesheetTimeConfiguration
                .setThursdayEndTime(timesheetConfigurationDTO.getThursdayEndTime());

        timesheetTimeConfiguration
                .setFridayStartTime(timesheetConfigurationDTO.getFridayStartTime());
        timesheetTimeConfiguration.setFridayEndTime(timesheetConfigurationDTO.getFridayEndTime());

        timesheetTimeConfiguration
                .setSaturdayStartTime(timesheetConfigurationDTO.getSaturdayStartTime());
        timesheetTimeConfiguration
                .setSaturdayEndTime(timesheetConfigurationDTO.getSaturdayEndTime());

        return timesheetTimeConfiguration;
    }

    private static TimesheetHourConfiguration mapTsConfigurationDTOToWorkHour(
            ConfigurationGroupDTO timesheetConfigurationDTO) {
        TimesheetHourConfiguration timesheetHourConfiguration = new TimesheetHourConfiguration();
        timesheetHourConfiguration.setSundayHours(timesheetConfigurationDTO.getSundayHours());
        timesheetHourConfiguration.setMondayHours(timesheetConfigurationDTO.getMondayHours());
        timesheetHourConfiguration.setTuesdayHours(timesheetConfigurationDTO.getTuesdayHours());
        timesheetHourConfiguration.setWednesdayHours(timesheetConfigurationDTO.getWednesdayHours());
        timesheetHourConfiguration.setThursdayHours(timesheetConfigurationDTO.getThursdayHours());
        timesheetHourConfiguration.setFridayHours(timesheetConfigurationDTO.getFridayHours());
        timesheetHourConfiguration.setSaturdayHours(timesheetConfigurationDTO.getSaturdayHours());
        return timesheetHourConfiguration;
    }

    /**
     * Preparing for TimesheetConfigurationDTO with some test data
     * 
     * @param workInputFlag
     * 
     * @return
     */
    private static ConfigurationGroupDTO prepareTimesheetConfigurationData(String workInputFlag) {
        ConfigurationGroupDTO configurationGroupDTO = new ConfigurationGroupDTO();
        List<ConfigurationGroupUserJobTitleDTO> configurationGroupUserJobTitleDTOs =
                new ArrayList<>();
        List<ConfigurationGroupLocationDTO> configurationGroupLocations =
                new ArrayList<ConfigurationGroupLocationDTO>();
        ConfigurationGroupUserJobTitleDTO configurationGroupUserJobTitleDTO =
                new ConfigurationGroupUserJobTitleDTO();
        ConfigurationGroupLocationDTO tsconfigGroupLoc = new ConfigurationGroupLocationDTO();
        ConfigurationGroupLocationDTO tsconfigGroupLoc1 = new ConfigurationGroupLocationDTO();

        configurationGroupUserJobTitleDTO
                .setConfigurationGroupUserJobTitleId(UUID.randomUUID());
        configurationGroupUserJobTitleDTO.setUserGroupId(UUID.randomUUID());
        configurationGroupUserJobTitleDTO.setUserGroupName("Recruiter");
        configurationGroupUserJobTitleDTOs.add(configurationGroupUserJobTitleDTO);
        configurationGroupDTO.setConfigurationGroupUserJobTitle(configurationGroupUserJobTitleDTOs);

        tsconfigGroupLoc.setOfficeId(123L);
        tsconfigGroupLoc1.setOfficeId(0L);
        tsconfigGroupLoc.setGroupLocationActiveFlag(ActiveFlagEnum.Y.name());
        tsconfigGroupLoc.setGroupLocationId(null);
        tsconfigGroupLoc.setOfficeName("NewYork");
        configurationGroupLocations.add(tsconfigGroupLoc);
        configurationGroupLocations.add(tsconfigGroupLoc1);
        configurationGroupDTO.setConfigurationGroupLocation(configurationGroupLocations);

        configurationGroupDTO.setConfigurationGroupName("NewYork City Timesheet Setup");
        configurationGroupDTO.setWeekStartDay("Monday");
        configurationGroupDTO.setWeekEndDay("Friday");
        configurationGroupDTO.setMinHours(0d);
        configurationGroupDTO.setMaxHours(56d);
        configurationGroupDTO.setEffectiveFlag(EffectiveFlagEnum.Y.toString());
        configurationGroupDTO.setActiveFlag(ActiveFlagEnum.Y.name());
        if (StringUtils.equals(workInputFlag, WorkInputEnum.valueOf("T").workInput())) {
            configurationGroupDTO.setDefaultInput(WorkInputEnum.valueOf("T").workInput());
            prepareTimesheetConfigTimeInputData(configurationGroupDTO);
        } else if (StringUtils.equals(workInputFlag, WorkInputEnum.valueOf("H").workInput())) {
            configurationGroupDTO.setDefaultInput(WorkInputEnum.valueOf("H").workInput());
            prepareTimesheetConfigHourInputData(configurationGroupDTO);
        }
        configurationGroupDTO.setTimeCalculation(TimeCalculationEnum.valueOf("W").timeCalutaion());
        configurationGroupDTO.setStraightTime("42");
        configurationGroupDTO.setOverTime("48");
        configurationGroupDTO.setDoubleTime("56");
        configurationGroupDTO.setStMinHours(0d);
        configurationGroupDTO.setStMaxHours(42d);
        configurationGroupDTO.setOtMinHours(42d);
        configurationGroupDTO.setOtMaxHours(48d);
        configurationGroupDTO.setDtMinHours(48d);
        configurationGroupDTO.setDtMaxHours(56d);
        List<HolidayConfigurationDTO> timesheetGroupHolidays = new ArrayList<>();
        HolidayConfigurationDTO time = new HolidayConfigurationDTO();
        time.setHolidayDate(new Date(1480938734));
        time.setHoliday("02/02/2016");
        time.setHolidayDescription("Thanks giving day");
        timesheetGroupHolidays.add(time);
        configurationGroupDTO.setHolidayConfiguration(timesheetGroupHolidays);

        List<NotificationConfigurationDTO> notificationConfigurationList = new ArrayList<>();
        prepareNotificationConfiguration(notificationConfigurationList);
        configurationGroupDTO.setNotificationConfiguration(notificationConfigurationList);

        List<NotificationAttributeDTO> notificationAttributeDTOs = new ArrayList<>();
        prepareNotificationAttribute(notificationAttributeDTOs);
        configurationGroupDTO.setNotificationAttribute(notificationAttributeDTOs);
        configurationGroupDTO.setUserGroupCategory(UserGroupCategoryEnum.RCTR.toString());
        return configurationGroupDTO;
    }

    private static void prepareNotificationAttribute(
            List<NotificationAttributeDTO> notificationAttributeDTOs) {
        NotificationAttributeDTO notificationAttribute = new NotificationAttributeDTO();
        notificationAttribute.setNotificationAttributeId(UUID.randomUUID());
        notificationAttribute.setUserGroupCategory(UserGroupCategoryEnum.RCTR);
        notificationAttribute.setNotificationAttributeName("HR Manager Reminder Email");
        notificationAttribute.setNotificationAttributeInputType("checkbox");
        notificationAttribute.setNotificationAttributeDefaultValue("checked");
        notificationAttribute.setNotificationAttributeFieldSequence(3);
        notificationAttribute
                .setNotificationAttributeDisableFlag(NotificationAttributeDisableEnum.N);
        notificationAttribute.setActiveFlag(ActiveFlagEnum.N);
        notificationAttribute.setDefaultValue("checked");
        notificationAttribute.setNotificationAttributeValue("checked");

        NotificationAttributeDTO dependentNotificationAttribute = new NotificationAttributeDTO();

        dependentNotificationAttribute.setNotificationAttributeId(UUID.randomUUID());
        dependentNotificationAttribute.setUserGroupCategory(UserGroupCategoryEnum.RCTR);
        dependentNotificationAttribute.setNotificationAttributeName("HR Email Id ");
        dependentNotificationAttribute.setNotificationAttributeInputType("textbox");
        dependentNotificationAttribute.setNotificationAttributeDefaultValue("");
        dependentNotificationAttribute.setNotificationAttributeFieldSequence(4);
        dependentNotificationAttribute
                .setNotificationAttributeDisableFlag(NotificationAttributeDisableEnum.N);
        dependentNotificationAttribute.setActiveFlag(ActiveFlagEnum.N);
        dependentNotificationAttribute.setDependentNotificationAttributeId(
                notificationAttribute.getNotificationAttributeId());
        dependentNotificationAttribute.setDefaultValue("checked");
        dependentNotificationAttribute.setNotificationAttributeValue("abc@gmail.com");
        dependentNotificationAttribute.setValidationType(ValidationTypeEnum.EMAIL);
        dependentNotificationAttribute.setNotificationAttributeDefaultValue("checked");
        notificationAttribute.setDependentNotificationAttribute(dependentNotificationAttribute);
        
        
        NotificationAttributeDTO notificationAttribute1 = new NotificationAttributeDTO();
        notificationAttribute1.setNotificationAttributeId(UUID.randomUUID());
        notificationAttribute1.setUserGroupCategory(UserGroupCategoryEnum.RCTR);
        notificationAttribute1.setNotificationAttributeName("HR Manager Reminder Email");
        notificationAttribute1.setNotificationAttributeInputType("checkbox");
        notificationAttribute1.setNotificationAttributeDefaultValue("checked");
        notificationAttribute1.setNotificationAttributeFieldSequence(3);
        notificationAttribute1
                .setNotificationAttributeDisableFlag(NotificationAttributeDisableEnum.N);
        notificationAttribute1.setActiveFlag(ActiveFlagEnum.N);
        notificationAttribute1.setDefaultValue("checked");
        notificationAttribute1.setNotificationAttributeValue("unchecked");

        NotificationAttributeDTO dependentNotificationAttribute1 = new NotificationAttributeDTO();

        dependentNotificationAttribute1.setNotificationAttributeId(UUID.randomUUID());
        dependentNotificationAttribute1.setUserGroupCategory(UserGroupCategoryEnum.RCTR);
        dependentNotificationAttribute1.setNotificationAttributeName("HR Email Id ");
        dependentNotificationAttribute1.setNotificationAttributeInputType("textbox");
        dependentNotificationAttribute1.setNotificationAttributeDefaultValue("");
        dependentNotificationAttribute1.setNotificationAttributeFieldSequence(4);
        dependentNotificationAttribute1
                .setNotificationAttributeDisableFlag(NotificationAttributeDisableEnum.N);
        dependentNotificationAttribute1.setActiveFlag(ActiveFlagEnum.N);
        dependentNotificationAttribute1.setDependentNotificationAttributeId(
                notificationAttribute1.getNotificationAttributeId());
        dependentNotificationAttribute1.setDefaultValue("checked");
        dependentNotificationAttribute1.setNotificationAttributeValue("abc@gmail.com");
        dependentNotificationAttribute1.setValidationType(ValidationTypeEnum.EMAIL);
        dependentNotificationAttribute1.setNotificationAttributeDefaultValue("checked");
        notificationAttribute1.setDependentNotificationAttribute(dependentNotificationAttribute1);
        
        notificationAttributeDTOs.add(notificationAttribute);
        notificationAttributeDTOs.add(notificationAttribute1);
    }
    
    
    private static void prepareNotificationAttributeEntity(
            List<NotificationAttribute> notificationAttributes) {
        NotificationAttribute notificationAttribute = new NotificationAttribute();
        notificationAttribute.setNotificationAttributeId(UUID.randomUUID());
        notificationAttribute.setUserGroupCategory(UserGroupCategoryEnum.RCTR);
        notificationAttribute.setNotificationAttributeName("HR Manager Reminder Email");
        notificationAttribute.setNotificationAttributeInputType("checkbox");
        notificationAttribute.setNotificationAttributeDefaultValue("checked");
        notificationAttribute.setNotificationAttributeFieldSequence(3);
        notificationAttribute
                .setNotificationAttributeDisableFlag(NotificationAttributeDisableEnum.N);
        notificationAttribute.setActiveFlag(ActiveFlagEnum.N);
        NotificationAttribute dependentNotificationAttribute = new NotificationAttribute();
        dependentNotificationAttribute.setNotificationAttributeId(UUID.randomUUID());
        dependentNotificationAttribute.setUserGroupCategory(UserGroupCategoryEnum.RCTR);
        dependentNotificationAttribute.setNotificationAttributeName("HR Email Id ");
        dependentNotificationAttribute.setNotificationAttributeInputType("textbox");
        dependentNotificationAttribute.setNotificationAttributeDefaultValue("checked");
        dependentNotificationAttribute.setNotificationAttributeFieldSequence(4);
        dependentNotificationAttribute
                .setNotificationAttributeDisableFlag(NotificationAttributeDisableEnum.N);
        dependentNotificationAttribute.setActiveFlag(ActiveFlagEnum.N);
        dependentNotificationAttribute.setDefaultValue("checked");
        dependentNotificationAttribute.setValidationType(ValidationTypeEnum.EMAIL);
        notificationAttribute.setDependantAttribute(dependentNotificationAttribute);
        notificationAttributes.add(notificationAttribute);
    }

    private static void prepareNotificationConfiguration(
            List<NotificationConfigurationDTO> notificationConfigurationList) {
        NotificationConfigurationDTO notificationConfiguration = new NotificationConfigurationDTO();
        notificationConfiguration.setNotificationAttributeId(UUID.randomUUID());
        notificationConfiguration.setNotificationAttributeValue("checked");
        notificationConfiguration.setTimesheetNotificationConfigId(UUID.randomUUID());
        notificationConfigurationList.add(notificationConfiguration);
    }

    private static List<NotificationConfigurationDTO> prepareNotificationConfigurationFromAttribute(
            List<NotificationConfigurationDTO> notificationConfigurationList,
            List<NotificationAttributeDTO> notificationAttributeDTOs) {
        NotificationConfigurationDTO notificationConfiguration = new NotificationConfigurationDTO();
        notificationConfiguration.setNotificationAttributeId(
                notificationAttributeDTOs.get(0).getNotificationAttributeId());
        notificationConfiguration.setNotificationAttributeValue(
                notificationAttributeDTOs.get(0).getNotificationAttributeDefaultValue());
        notificationConfiguration.setTimesheetNotificationConfigId(UUID.randomUUID());
        notificationConfigurationList.add(notificationConfiguration);
        return notificationConfigurationList;
    }

    private static void prepareTimesheetConfigHourInputData(
            ConfigurationGroupDTO timesheetConfigSettingDTO) {
        timesheetConfigSettingDTO.setSundayHours(0d);
        timesheetConfigSettingDTO.setMondayHours(8d);
        timesheetConfigSettingDTO.setTuesdayHours(8d);
        timesheetConfigSettingDTO.setWednesdayHours(8d);
        timesheetConfigSettingDTO.setThursdayHours(8d);
        timesheetConfigSettingDTO.setFridayHours(8d);
        timesheetConfigSettingDTO.setSaturdayHours(0d);
    }

    private static void prepareTimesheetConfigTimeInputData(
            ConfigurationGroupDTO timesheetConfigSettingDTO) {
        timesheetConfigSettingDTO.setBreakStartTime("12:00 PM");
        timesheetConfigSettingDTO.setBreakEndTime("01:00 PM");
        /*timesheetConfigSettingDTO.setSundayStartTime("08:00 AM");
        timesheetConfigSettingDTO.setSundayEndTime("05:00 PM");*/

        timesheetConfigSettingDTO.setSundayStartTime("08:00 AM");
        timesheetConfigSettingDTO.setSundayEndTime("05:00 PM");

        timesheetConfigSettingDTO.setMondayStartTime("08:00 AM");
        timesheetConfigSettingDTO.setMondayEndTime("05:00 PM");

        timesheetConfigSettingDTO.setTuesdayStartTime("08:00 AM");
        timesheetConfigSettingDTO.setTuesdayEndTime("05:00 PM");

        timesheetConfigSettingDTO.setWednesdayStartTime("08:00 AM");
        timesheetConfigSettingDTO.setWednesdayEndTime("05:00 PM");

        timesheetConfigSettingDTO.setThursdayStartTime("08:00 AM");
        timesheetConfigSettingDTO.setThursdayEndTime("05:00 PM");

        timesheetConfigSettingDTO.setFridayStartTime("08:00 AM");
        timesheetConfigSettingDTO.setFridayEndTime("05:00 PM");

        timesheetConfigSettingDTO.setSaturdayStartTime("08:00 AM");
        timesheetConfigSettingDTO.setSaturdayEndTime("05:00 PM");
    }
    
    private static ConfigurationGroupViewDTO prepareGetAllConfigurationGroupViewDTO() {
        ConfigurationGroupViewDTO configurationGroupViewDTO = new ConfigurationGroupViewDTO();
        configurationGroupViewDTO.setActiveFlag("Y"); 
        configurationGroupViewDTO.setConfigurationGroupId(UUID.randomUUID());
        configurationGroupViewDTO.setDefaultInput("T");
        configurationGroupViewDTO.setJobTitle("Analyst");
        configurationGroupViewDTO.setOfficeName("Atlanta");
        configurationGroupViewDTO.setTimeCalculation("W");
        configurationGroupViewDTO.setWeekStartDay("Monday");
        configurationGroupViewDTO.setWeekEndDay("Friday");
        configurationGroupViewDTO.setStMinHours("0");
        configurationGroupViewDTO.setStMaxHours("8");
        configurationGroupViewDTO.setOtMinHours("8");
        configurationGroupViewDTO.setOtMaxHours("9");
        configurationGroupViewDTO.setDtMaxHours("9");
        configurationGroupViewDTO.setDtMinHours("10");
        configurationGroupViewDTO.setNotifyFlag("Y");
        configurationGroupViewDTO.setHolidayFlag("N");
        return configurationGroupViewDTO;
    }
    
    private static ConfigurationGroupView prepareConfigurationGroupView(
            ConfigurationGroupViewDTO configurationGroupViewDTO) {
        ConfigurationGroupView configurationGroup = new ConfigurationGroupView();
        configurationGroup.setActiveFlag(configurationGroupViewDTO.getActiveFlag()); 
        configurationGroup.setConfigurationGroupId(configurationGroupViewDTO.getConfigurationGroupId());
        configurationGroup.setDefaultInput(WorkInputEnum.T);
        configurationGroup.setJobTitle(configurationGroupViewDTO.getJobTitle());
        configurationGroup.setOfficeName(configurationGroupViewDTO.getOfficeName());
        configurationGroup.setTimeCalculation(TimeCalculationEnum.W);
        configurationGroup.setWeekStartDay(configurationGroupViewDTO.getWeekStartDay());
        configurationGroup.setWeekEndDay(configurationGroupViewDTO.getWeekEndDay());
        configurationGroup.setStMinHours(Double.parseDouble(configurationGroupViewDTO.getStMinHours()));
        configurationGroup.setStMaxHours(Double.parseDouble(configurationGroupViewDTO.getStMaxHours()));
        configurationGroup.setOtMinHours(Double.parseDouble(configurationGroupViewDTO.getOtMinHours()));
        configurationGroup.setOtMaxHours(Double.parseDouble(configurationGroupViewDTO.getOtMaxHours()));
        configurationGroup.setDtMaxHours(Double.parseDouble(configurationGroupViewDTO.getDtMinHours()));
        configurationGroup.setDtMinHours(Double.parseDouble(configurationGroupViewDTO.getDtMaxHours()));
        configurationGroup.setNotifyFlag(configurationGroupViewDTO.getNotifyFlag());
        configurationGroup.setHolidayFlag(configurationGroupViewDTO.getHolidayFlag());
        return configurationGroup;
    }
    
    @DataProvider(name = "createHolidayConfigurationValidation")
    public static Iterator<Object[]> createHolidayConfigurationValidation() {
        List<HolidayConfigurationDTO> timesheetGroupHolidays = prepareHolidayConfigurationDTO();
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timesheetGroupHolidays});
        return testData.iterator();

    }
    @DataProvider(name = "createHolidayConfigurationValidationException")
    public static Iterator<Object[]> createHolidayConfigurationValidationException() {
        List<HolidayConfigurationDTO> timesheetGroupHolidays = prepareHolidayConfigurationDTO();
        HolidayConfigurationDTO time = new HolidayConfigurationDTO();
        time.setHolidayDate(new Date(1480938734));
        time.setHoliday("02/02/2016");
        time.setHolidayDescription("Thanks Giving Day");
        timesheetGroupHolidays.add(time);
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timesheetGroupHolidays});
        return testData.iterator();

    }
    @DataProvider(name = "createHolidayConfigurationDateValidationException")
    public static Iterator<Object[]> createHolidayConfigurationDateValidationException() {
        List<HolidayConfigurationDTO> timesheetGroupHolidays = new ArrayList<>();
        HolidayConfigurationDTO time = new HolidayConfigurationDTO();
        time.setHolidayDate(null);
        time.setHolidayDescription("Thanks Giving Day");
        timesheetGroupHolidays.add(time);
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timesheetGroupHolidays});
        return testData.iterator();

    }
    
    @DataProvider(name = "createHolidayConfigurationDescriptionValidationException")
    public static Iterator<Object[]> createHolidayConfigurationDescriptionValidationException() {
        List<HolidayConfigurationDTO> timesheetGroupHolidays = new ArrayList<>();
        HolidayConfigurationDTO time = new HolidayConfigurationDTO();
        time.setHolidayDate(new Date(1480938738));
        time.setHoliday("02/02/2016");
        time.setHolidayDescription(null);
        timesheetGroupHolidays.add(time);
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timesheetGroupHolidays});
        return testData.iterator();

    }

}
