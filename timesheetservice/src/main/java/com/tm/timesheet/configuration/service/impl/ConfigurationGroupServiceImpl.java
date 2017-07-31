package com.tm.timesheet.configuration.service.impl;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;
import com.tm.timesheet.configuration.domain.ConfigurationGroup;
import com.tm.timesheet.configuration.domain.ConfigurationGroup.ActiveFlagEnum;
import com.tm.timesheet.configuration.domain.ConfigurationGroup.EffectiveFlagEnum;
import com.tm.timesheet.configuration.domain.ConfigurationGroup.UserGroupCategoryEnum;
import com.tm.timesheet.configuration.domain.ConfigurationGroupLocation;
import com.tm.timesheet.configuration.domain.ConfigurationGroupUserJobTitle;
import com.tm.timesheet.configuration.domain.ConfigurationGroupView;
import com.tm.timesheet.configuration.domain.HolidayConfiguration;
import com.tm.timesheet.configuration.domain.NotificationAttribute;
import com.tm.timesheet.configuration.domain.NotificationConfiguration;
import com.tm.timesheet.configuration.domain.TimesheetConfiguration;
import com.tm.timesheet.configuration.domain.TimesheetHourConfiguration;
import com.tm.timesheet.configuration.domain.TimesheetTimeConfiguration;
import com.tm.timesheet.configuration.domain.util.DateConverter;
import com.tm.timesheet.configuration.enums.TimeCalculationEnum;
import com.tm.timesheet.configuration.enums.WorkInputEnum;
import com.tm.timesheet.configuration.exception.ConfigurationException;
import com.tm.timesheet.configuration.exception.ConfigurationGroupException;
import com.tm.timesheet.configuration.exception.ConfigurationNotFoundException;
import com.tm.timesheet.configuration.exception.HolidayConfigurationException;
import com.tm.timesheet.configuration.exception.HolidayDateConfigurationException;
import com.tm.timesheet.configuration.exception.HolidayDescriptionConfigurationException;
import com.tm.timesheet.configuration.exception.NotificationAttributeNotFoundException;
import com.tm.timesheet.configuration.exception.NotificationEmailException;
import com.tm.timesheet.configuration.repository.ConfigurationGroupRepository;
import com.tm.timesheet.configuration.repository.ConfigurationGroupViewRepository;
import com.tm.timesheet.configuration.repository.HolidayConfigurationRepository;
import com.tm.timesheet.configuration.repository.NotificationAttributeRepository;
import com.tm.timesheet.configuration.repository.NotificationConfigurationRepository;
import com.tm.timesheet.configuration.service.ConfigurationGroupService;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupDTO;
import com.tm.timesheet.configuration.service.dto.ConfigurationGroupViewDTO;
import com.tm.timesheet.configuration.service.dto.HolidayConfigurationDTO;
import com.tm.timesheet.configuration.service.dto.NotificationAttributeDTO;
import com.tm.timesheet.configuration.service.dto.OfficeLocationDTO;
import com.tm.timesheet.configuration.service.dto.UserGroupDTO;
import com.tm.timesheet.configuration.service.hystrix.commands.OfficeLocationCommand;
import com.tm.timesheet.configuration.service.hystrix.commands.UserGroupCommand;
import com.tm.timesheet.configuration.service.mapper.ConfigurationGroupMapper;

/**
 * Service Implementation for managing ConfigurationGroup.
 */
@Service
@Transactional
public class ConfigurationGroupServiceImpl implements ConfigurationGroupService {

    private static final String S_S = "%s %s";

	private static final String S_S_S = "%s %s %s";

	private static final String ACTIVE_CONFIGURED = "Y";

    private static final String INACTIVE_CONFIGURED = "N";

    private static final Long ALL_LOCATION_ID = 0L;

    private static final String ALL_LOCATION_NAME = "All";

    private static final String CHECKED = "checked";

    private static final String UNCHECKED = "unchecked";

    private ConfigurationGroupRepository configurationGroupRepository;

    private HolidayConfigurationRepository holidayConfigurationRepository;

    private NotificationConfigurationRepository notificationConfigurationRepository;

    private NotificationAttributeRepository notificationAttributeRepository;

    private ConfigurationGroupViewRepository configurationGroupViewRepository;

    private RestTemplate restTemplate;

    private DiscoveryClient discoveryClient;

    private SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
    private MessageSourceAccessor accessor;
    private static final String CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY =
            "configuration.settings.group.daily.exceedMax";
    private DecimalFormat decimalFormat = new DecimalFormat(".##");

    @Inject
    public ConfigurationGroupServiceImpl(
            final @NotNull ConfigurationGroupRepository configurationGroupRepository,
            final @NotNull HolidayConfigurationRepository holidayConfigurationRepository,
            final @NotNull NotificationConfigurationRepository notificationConfigurationRepository,
            final @NotNull NotificationAttributeRepository notificationAttributeRepository,
            final @NotNull ConfigurationGroupViewRepository configurationGroupViewRepository,
            @LoadBalanced final RestTemplate restTemplate,
            @Qualifier("discoveryClient") final DiscoveryClient discoveryClient,
            MessageSource messageSource) {
        this.configurationGroupRepository = Objects.requireNonNull(configurationGroupRepository,
                "configurationGroupRepository cannot be null");
        this.holidayConfigurationRepository = Objects.requireNonNull(holidayConfigurationRepository,
                "holidayConfigurationRepository cannot be null");
        this.notificationConfigurationRepository =
                java.util.Objects.requireNonNull(notificationConfigurationRepository,
                        "notificationConfigurationRepository cannot be null");
        this.notificationAttributeRepository = java.util.Objects.requireNonNull(
                notificationAttributeRepository, "notificationAttributeRepository cannot be null");
        this.configurationGroupViewRepository =
                java.util.Objects.requireNonNull(configurationGroupViewRepository,
                        "configurationGroupViewRepository cannot be null");
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
        this.accessor = new MessageSourceAccessor(messageSource);
    }

    @Override
    public ConfigurationGroupDTO createConfigurationGroup(
            ConfigurationGroupDTO configurationGroupDTO) throws ConfigurationException {
        //Create configuration Validation
      configurationGroupValidation(configurationGroupDTO);
        
        ConfigurationGroupDTO result = null;

        // --ConfigurationGroup preparation
        ConfigurationGroup configurationGroup = prepareConfigurationGroup(configurationGroupDTO);

        // --TimesheetConfiguration preparation
        prepareTimesheetConfiguration(configurationGroupDTO, configurationGroup);

        // --HolidayConfiguration preparation
        prepareHolidayConfiguration(configurationGroup);

        // --NotificationConfiguration preparation
        prepareNotificationConfiguration(configurationGroupDTO, configurationGroup);

        configurationGroup = configurationGroupRepository.save(configurationGroup);

        if (Objects.nonNull(configurationGroup)) {
            result = mapConfigurationGroupToDTO(configurationGroup);
            prepareHolidayConfigurationDTO(configurationGroup, result);
            result.setNotificationAttribute(
                    getNotificationAttributesAndValues(result.getUserGroupCategory(),
                            configurationGroup.getNotificationConfiguration()));
            result.getConfigurationGroupLocation().forEach(configuratonGroupLocation -> {
                if (configuratonGroupLocation.getOfficeId() == 0) {
                    configuratonGroupLocation.setOfficeName("All");
                }
            });
        }
        return result;
    }

  @Override
  public ConfigurationGroupDTO updateConfigurationGroup(
      ConfigurationGroupDTO configurationGroupDTO) {
    ConfigurationGroup configurationGroup =
        configurationGroupRepository.findOne(configurationGroupDTO.getConfigurationGroupId());
    if (Objects.isNull(configurationGroup)) {
      throw new ConfigurationNotFoundException(
          configurationGroupDTO.getConfigurationGroupId().toString());
    }
    setConfigurationRelatedIdAsNull(configurationGroupDTO);

    if (Objects.isNull(configurationGroupDTO.getOtMaxHours()) ) {
      configurationGroupDTO.setOtMinHours(null);
    }
    if (Objects.isNull(configurationGroupDTO.getDtMaxHours())) {
      configurationGroupDTO.setDtMinHours(null);
    }
    ConfigurationGroupDTO configGroupDTO = createConfigurationGroup(configurationGroupDTO);
    // --to set the EffectiveFlagEnum.N for old, Since this reference for
    // future.
    configurationGroup.setEffectiveFlag(EffectiveFlagEnum.N);
    configurationGroup.setEffectiveEndDate(ZonedDateTime.now());
    configurationGroupRepository.saveAndFlush(configurationGroup);
    return configGroupDTO;
  }

    @Transactional(readOnly = true)
    @Override
    public Page<ConfigurationGroupViewDTO> getAllConfigurationGroupsView(Pageable pageable) {
        Pageable pageableRequest = pageable;

        if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
            pageableRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.Direction.DESC, "createdDate");
        }
        Page<ConfigurationGroupView> configurationGroupView =
                configurationGroupViewRepository.findAll(pageableRequest);
        List<ConfigurationGroupViewDTO> result = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(configurationGroupView.getContent())) {
            for (ConfigurationGroupView configurationGroup : configurationGroupView.getContent()) {
                ConfigurationGroupViewDTO configurationGroupViewDTO =
                        ConfigurationGroupMapper.INSTANCE
                                .configurationGroupViewToConfigurationGroupGroupViewDTO(
                                        configurationGroup);
                result.add(configurationGroupViewDTO);
            }
        }
        return new PageImpl<>(result, pageable, configurationGroupView.getTotalElements());
    }

    @Transactional(readOnly = true)
    @Override
    public ConfigurationGroupDTO getConfigurationGroup(UUID id)
            throws ConfigurationNotFoundException {

        ConfigurationGroupDTO configurationGroupDTO = null;
        ConfigurationGroup configurationGroup = configurationGroupRepository.findOne(id);
        if (Objects.nonNull(configurationGroup)) {
            configurationGroupDTO = mapConfigurationGroupToDTO(configurationGroup);
            prepareHolidayConfigurationDTO(configurationGroup, configurationGroupDTO);
            configurationGroupDTO.setNotificationAttribute(
                    getNotificationAttributesAndValues(configurationGroupDTO.getUserGroupCategory(),
                            configurationGroup.getNotificationConfiguration()));
        } else {
            throw new ConfigurationNotFoundException(id.toString());
        }
        return configurationGroupDTO;
    }

    @Override
    public void deleteConfigurationGroup(UUID id) throws ConfigurationNotFoundException {
        ConfigurationGroup configurationGroup =
                configurationGroupRepository.findOne(id);
        if (Objects.isNull(configurationGroup)) {
            throw new ConfigurationNotFoundException(id.toString());
        }
        configurationGroup.setActiveFlag(ActiveFlagEnum.N);
        configurationGroupRepository.saveAndFlush(configurationGroup);
    }

    @Override
    public List<HolidayConfigurationDTO> createHolidayConfiguration(
    		UUID configGroupId,List<HolidayConfigurationDTO> holidayConfigurationDTOs)
            throws ConfigurationException {
                
        ConfigurationGroup configurationGroup =
                configurationGroupRepository.findOne(configGroupId);
        
        List<HolidayConfiguration> holidayConfigurations = ConfigurationGroupMapper.INSTANCE
                .holidayConfigurationDTOsToHolidayConfiguration(holidayConfigurationDTOs);

        holidayConfigurations.forEach(holidayConfiguration -> {
            if (Objects.nonNull(holidayConfiguration)) {
                prepareAuditFieldForHolidayConfig(holidayConfiguration);
                holidayConfiguration.setConfigurationGroup(configurationGroup);
            }
        });

        holidayConfigurationRepository.save(holidayConfigurations);
        List<HolidayConfigurationDTO> result = ConfigurationGroupMapper.INSTANCE
                .holidayConfigurationsToHolidayConfigurationDTOs(holidayConfigurations);

        if (CollectionUtils.isNotEmpty(result)) {
            result.forEach(holidayConfigurationDTO -> 
                holidayConfigurationDTO.setConfigurationGroupId(configGroupId));
        }
        return result;
    }

    @Override
    public List<HolidayConfigurationDTO> updateHolidayConfiguration(
          UUID configGroupId,List<HolidayConfigurationDTO> holidayConfigurationDTOs)
            throws ConfigurationException {
                
        ConfigurationGroup configurationGroup =
                configurationGroupRepository.findOne(configGroupId);
        
        List<HolidayConfiguration> holidayConfigurations = ConfigurationGroupMapper.INSTANCE
                .holidayConfigurationDTOsToHolidayConfiguration(holidayConfigurationDTOs);

        holidayConfigurations.forEach(holidayConfiguration -> {
            if (Objects.nonNull(holidayConfiguration)) {
                prepareAuditFieldForHolidayConfig(holidayConfiguration);
                holidayConfiguration.setConfigurationGroup(configurationGroup);
            }
        });

        holidayConfigurationRepository.save(holidayConfigurations);
        List<HolidayConfigurationDTO> result = ConfigurationGroupMapper.INSTANCE
                .holidayConfigurationsToHolidayConfigurationDTOs(holidayConfigurations);

        if (CollectionUtils.isNotEmpty(result)) {
            result.forEach(holidayConfigurationDTO -> 
                holidayConfigurationDTO.setConfigurationGroupId(configGroupId));
        }
        return result;
    }

    @Override
    public void deleteHolidayConfiguration(UUID id)
            throws ConfigurationException {
        holidayConfigurationRepository.delete(id);
    }

    @Override
    public void deleteNotificationConfiguration(UUID id)
            throws ConfigurationException {
        notificationConfigurationRepository.delete(id);
    }

    @Override
    public List<OfficeLocationDTO> getConfiguredOfficeIdsByConfigIdOrUserGroupIds(
        UUID configurationGroupId, String userGroupIds) {
      
        List<String> userGroupIdsStringList = Lists.newArrayList(Splitter.on(",").split(userGroupIds.replaceAll(" ", "")));
        List<UUID> userGroupIdList = new ArrayList<>();
        userGroupIdsStringList.forEach(uuidString -> 
            userGroupIdList.add(UUID.fromString(uuidString)));
        List<Long> configuredOfficeLocationIds = new ArrayList<>();
        String isActive = null;
        boolean isActiveConfigured = false;
        if (null == configurationGroupId) {
            isActiveConfigured = true;
        } else {
            ConfigurationGroup configGroup =
                    configurationGroupRepository.findOne(configurationGroupId);
            List<UUID> userGroupIdsList = new ArrayList<>();
            getUserGroupIdsList(userGroupIdList, configGroup, userGroupIdsList);
            if (CollectionUtils.isNotEmpty(userGroupIdsList)) {
                configuredOfficeLocationIds = configurationGroupRepository
                        .findConfiguredOfficeIdsByGroupIdAndUserGroupIds(configurationGroupId,
                            userGroupIdList);
                isActive = ACTIVE_CONFIGURED;
            } else {
                isActiveConfigured = true;
            }
        }
        if (isActiveConfigured) {
            configuredOfficeLocationIds = configurationGroupRepository
                    .findConfiguredOfficeIdsByUserGroupIds(userGroupIdList);
            isActive = INACTIVE_CONFIGURED;
        }
        return getOfficeLocationsWithAll(configuredOfficeLocationIds, isActive);
    }

    @Override
    public List<UserGroupDTO> getUserGroupByConfigurationGroup(UUID configId) throws Exception
             {
        List<UserGroupDTO> userGroupDTOs =
                new UserGroupCommand(
        				restTemplate,
        				DiscoveryClientAndAccessTokenUtil
        						.discoveryClient(
        								OfficeLocationCommand.COMMON_GROUP_KEY,
        								discoveryClient), DiscoveryClientAndAccessTokenUtil.getAccessToken()).getAllJobTitle();

        if (null != configId) {
            ConfigurationGroup configurationGroup = configurationGroupRepository.findOne(configId);
            if (Objects.nonNull(configurationGroup)
                    && Objects.nonNull(configurationGroup.getConfigurationGroupUserJobTitle())) {
                List<ConfigurationGroupUserJobTitle> configuredUserGroupTitles =
                        configurationGroup.getConfigurationGroupUserJobTitle();

                userGroupDTOs.forEach(userGroupDTO -> 
                    configuredUserGroupTitles.forEach(configuredUserGroupTitle -> {
                        if (configuredUserGroupTitle.getUserGroupId()
                                .equals(userGroupDTO.getGroupId())) {
                            userGroupDTO.setIsConfigured(ACTIVE_CONFIGURED);
                        }
                    }));
            }
        }
        return userGroupDTOs;
    }

    @Override
    public List<OfficeLocationDTO> getOfficeLocationsByConfigurationGroup(UUID configId)
             {
        List<Long> configuredOfficeLocationIds = new ArrayList<>();
        List<OfficeLocationDTO> officeLocationDTOs = new ArrayList<>();

        if (null != configId) {
            ConfigurationGroup configurationGroup = configurationGroupRepository.findOne(configId);
            List<UUID> userGroupIds = new ArrayList<>();
            configurationGroup.getConfigurationGroupUserJobTitle().forEach(userGroupId -> 
                userGroupIds.add(userGroupId.getUserGroupId()));
            List<Long> userGroupOfficeLocationIds = configurationGroupRepository
                    .findConfiguredOfficeIdsByUserGroupIds(userGroupIds);
            if (Objects.nonNull(configurationGroup)
                    && Objects.nonNull(configurationGroup.getConfigurationGroupLocation())) {
                List<ConfigurationGroupLocation> configuredOfficeLocations =
                        configurationGroup.getConfigurationGroupLocation();

                configuredOfficeLocations.forEach(configLocation -> {
                    configuredOfficeLocationIds.add(configLocation.getOfficeId());
                    userGroupOfficeLocationIds.remove(configLocation.getOfficeId());
                });
                officeLocationDTOs =
                        getOfficeLocationsWithAll(userGroupOfficeLocationIds, INACTIVE_CONFIGURED);
                officeLocationDTOs.forEach(officeLocationDTO -> {
                    if (configuredOfficeLocationIds
                            .contains((Long) officeLocationDTO.getOfficeId())) {
                        officeLocationDTO.setIsConfigured(ACTIVE_CONFIGURED);
                        officeLocationDTO.setIsActive(ACTIVE_CONFIGURED);
                    }
                });
            }
        }
        return officeLocationDTOs;
    }


    @Override
    public HolidayConfigurationDTO getHolidayConfiguration(UUID id)
            throws ConfigurationException {
        HolidayConfiguration holidayConfiguration = holidayConfigurationRepository.findOne(id);
        return ConfigurationGroupMapper.INSTANCE
                .holidayConfigurationToHolidayConfigurationDTO(holidayConfiguration);
    }

    @Override
    public List<HolidayConfigurationDTO> getConfiguredHolidayConfigurations(UUID id) {
        ConfigurationGroup configurationGroup = configurationGroupRepository.findOne(id);
        if (Objects.nonNull(configurationGroup)) {
            return populateConfigurationGroupInHolidayConfiguration(configurationGroup);
        } else {
            throw new ConfigurationException(id.toString());
        }        
    }

    @Override
    public List<NotificationAttributeDTO> getConfiguredNotificationConfigurations(UUID id) {

        List<NotificationAttributeDTO> notificationAttributeDTOs;
        ConfigurationGroup configurationGroup = configurationGroupRepository.findOne(id);
        if (Objects.nonNull(configurationGroup)) {
            notificationAttributeDTOs = getNotificationAttributesAndValues(
                    configurationGroup.getUserGroupCategory().toString(),
                    configurationGroup.getNotificationConfiguration());
        } else {
            throw new ConfigurationException(id.toString());
        }
        return notificationAttributeDTOs;
    }

    @Override
    public List<NotificationAttributeDTO> getNotificationAttributes(String userGroupCategory) {

        Pageable pageable =
                new PageRequest(0, 25, Sort.Direction.ASC, "notificationAttributeFieldSequence");
        if (EnumUtils.isValidEnum(UserGroupCategoryEnum.class, userGroupCategory)) {
            List<NotificationAttribute> notificationAttributes = notificationAttributeRepository
                    .findByUserGroupCategoryAndDependantAttributeIsNull(
                            UserGroupCategoryEnum.valueOf(userGroupCategory), pageable);

            return ConfigurationGroupMapper.INSTANCE
                    .notificationAttributesToNotificationAttributeDTOs(notificationAttributes);
        } else {
            throw new NotificationAttributeNotFoundException(userGroupCategory);
        }
    }
	
	@Override
    public List<NotificationAttributeDTO>  createNotification(UUID id,
    		List<NotificationAttributeDTO> notificationAttribute)
            throws ConfigurationException {
        ConfigurationGroup configurationGroup =
                configurationGroupRepository.findOne(id);
        List<NotificationConfiguration> notificationConfigurations = prepareNotificationConfigurations(
			notificationAttribute, configurationGroup);
        if(CollectionUtils.isNotEmpty(notificationConfigurations)) {
        	notificationConfigurationRepository.save(notificationConfigurations);
        }
        if (Objects.nonNull(configurationGroup)) {
            return getNotificationAttributesAndValues(
                    configurationGroup.getUserGroupCategory().toString(),
                    notificationConfigurations);
        } else {
            throw new ConfigurationException(id.toString());
        }
    }
	
	@Override
	public List<NotificationAttributeDTO> updateNotification(UUID id,
			List<NotificationAttributeDTO> notificationAttributeDTOs) throws ConfigurationException {
		
        ConfigurationGroup configurationGroup = configurationGroupRepository.findOne(id);
        if (Objects.nonNull(configurationGroup)) {
            List<NotificationConfiguration> notificationConfigurations =
                    getNotificationConfigurationsWithUpdatedValues(
                            configurationGroup.getNotificationConfiguration(),
                            notificationAttributeDTOs);
            notificationConfigurationRepository.save(notificationConfigurations);
            return getNotificationAttributesAndValues(
                    configurationGroup.getUserGroupCategory().toString(),
                    notificationConfigurations);
        } else {
            throw new ConfigurationException(id.toString());
        }
    }

    @Override
    public Boolean createHolidayConfigurationValidation(
            List<HolidayConfigurationDTO> holidayConfigurationDTOs) {
        if (CollectionUtils.isNotEmpty(holidayConfigurationDTOs)) {
            List<String> holidayDates = new ArrayList<>();
            holidayConfigurationDTOs.forEach(holidayConfigurationDTO -> {
                if (Objects.nonNull(holidayConfigurationDTO)
                        && StringUtils.isNotBlank(holidayConfigurationDTO.getHoliday())
                        && StringUtils
                                .isNotBlank(holidayConfigurationDTO.getHolidayDescription())) {

                    checkHolidayDateAlreadyExists(holidayDates, holidayConfigurationDTO);
                }
                checkHolidayDateNull(holidayConfigurationDTO);


            });
        }
        return true;
    }

    private List<NotificationAttributeDTO> getNotificationAttributesAndValues(
            String notificationCategory,
            List<NotificationConfiguration> notificationConfigurations) {

        List<NotificationAttributeDTO> notificationAttributeDTOs =
                getNotificationAttributes(notificationCategory);

        List<NotificationAttributeDTO> notificationAttributeDTOList = new ArrayList<>();

        Map<UUID, String> notificationConfigMap = new HashMap<>();
        notificationConfigurations.forEach(notificationConfiguration -> {
            if (Objects.nonNull(notificationConfiguration.getNotificationAttributeValue())) {
                notificationConfigMap.put(notificationConfiguration.getNotificationAttributeId(),
                        notificationConfiguration.getNotificationAttributeValue());
            }
        });

        notificationAttributeDTOs.forEach(notificationAttribute -> {

            if (Objects.nonNull(notificationConfigMap
                    .get(notificationAttribute.getNotificationAttributeId()))) {
                notificationAttribute.setNotificationAttributeValue(notificationConfigMap
                        .get(notificationAttribute.getNotificationAttributeId()));
            } else {
                notificationAttribute.setNotificationAttributeValue(UNCHECKED);
            }
            if (Objects.nonNull(notificationAttribute.getDependentNotificationAttribute())
                    && Objects.nonNull(notificationConfigMap.get(notificationAttribute
                            .getDependentNotificationAttribute().getNotificationAttributeId()))) {
                notificationAttribute.getDependentNotificationAttribute()
                        .setNotificationAttributeValue(notificationConfigMap
                                .get(notificationAttribute.getDependentNotificationAttribute()
                                        .getNotificationAttributeId()));
            }
            notificationAttributeDTOList.add(notificationAttribute);
        });
        return notificationAttributeDTOList;
    }

    private List<NotificationConfiguration> getNotificationConfigurationsWithUpdatedValues(
            List<NotificationConfiguration> notificationConfigurations,
            List<NotificationAttributeDTO> notificationAttributeDTOs) {

        Map<String, String> notificationConfigValueMap = new HashMap<>();

        notificationAttributeDTOs.forEach(notificationAttirbute -> {
            notificationConfigValueMap.put(notificationAttirbute.getNotificationAttributeId().toString(),
                    notificationAttirbute.getNotificationAttributeValue());
            if (Objects.nonNull(notificationAttirbute.getDependentNotificationAttribute())) {
                notificationConfigValueMap.put(
                        notificationAttirbute.getDependentNotificationAttribute()
                                .getNotificationAttributeId().toString(),
                        notificationAttirbute.getDependentNotificationAttribute()
                                .getNotificationAttributeValue());
            }
        });

        notificationConfigurations.forEach(notificationConfiguration -> 
            notificationConfiguration.setNotificationAttributeValue(notificationConfigValueMap
                    .get(notificationConfiguration.getNotificationAttributeId())));
        return notificationConfigurations;
    }

    private void prepareHolidayConfigurationDTO(ConfigurationGroup configurationGroup,
            ConfigurationGroupDTO configurationGroupDTO) {
        if (Objects.nonNull(configurationGroup)) {
            configurationGroupDTO.setHolidayConfiguration(
                    populateConfigurationGroupInHolidayConfiguration(configurationGroup));
        }
    }

    private List<HolidayConfigurationDTO> populateConfigurationGroupInHolidayConfiguration(
            ConfigurationGroup configurationGroup) {
        List<HolidayConfigurationDTO> holidayConfigurationDTOs =
                ConfigurationGroupMapper.INSTANCE.holidayConfigurationsToHolidayConfigurationDTOs(
                        configurationGroup.getHolidayConfiguration());
        if (CollectionUtils.isNotEmpty(holidayConfigurationDTOs)) {
            holidayConfigurationDTOs.forEach(holidayConfigurationDTO -> {
                holidayConfigurationDTO
                        .setConfigurationGroupId(configurationGroup.getConfigurationGroupId());

                String holidayDate =
                        DateConverter.convertDateToString(holidayConfigurationDTO.getHolidayDate());
                setHolidayIsActiveFlag(holidayConfigurationDTO, holidayDate);

            });
        }
        return holidayConfigurationDTOs;
    }

    private void addAllInLocations(List<OfficeLocationDTO> officeLocations,
            List<Long> configuredOfficeLocations) {
        if (!configuredOfficeLocations.isEmpty()
                && configuredOfficeLocations.contains(ALL_LOCATION_ID)) {
            officeLocations.add(new OfficeLocationDTO(ALL_LOCATION_ID, ALL_LOCATION_NAME,
                    ACTIVE_CONFIGURED, ACTIVE_CONFIGURED));
        } else {
            officeLocations.add(new OfficeLocationDTO(ALL_LOCATION_ID, ALL_LOCATION_NAME,
                    INACTIVE_CONFIGURED, INACTIVE_CONFIGURED));
        }
    }

    private List<NotificationConfiguration> prepareNotificationConfigurations(
            List<NotificationAttributeDTO> notificationAttributeData,
            ConfigurationGroup configurationGroup) {

        List<NotificationConfiguration> notificationConfigurations = new ArrayList<>();
        notificationAttributeData.forEach(notificationAttribute -> {
            if (Objects.nonNull(notificationAttribute)) {
                notificationConfigurations
                        .add(prepareNotificationConfigurationFromNotificationAttribute(
                                configurationGroup, notificationAttribute));
                if (Objects.nonNull(notificationAttribute.getDependentNotificationAttribute())) {
                    // --prepare child data
                    notificationConfigurations
                            .add(prepareNotificationConfigurationFromNotificationAttribute(
                                    configurationGroup,
                                    notificationAttribute.getDependentNotificationAttribute()));
                }
            }
        });
        return notificationConfigurations;
    }

    private void prepareNotificationConfiguration(ConfigurationGroupDTO configurationGroupDTO,
            ConfigurationGroup configurationGroup) {

        List<NotificationConfiguration> notificationConfigurations = new ArrayList<>();
        configurationGroupDTO.getNotificationAttribute().forEach(notificationAttribute -> {
            if (Objects.nonNull(notificationAttribute)) {
                NotificationConfiguration notificationConfiguration =
                        prepareNotificationConfigurationFromNotificationAttribute(
                                configurationGroup, notificationAttribute);
                if (!StringUtils.isBlank(notificationConfiguration.getNotificationAttributeValue())
                        && notificationConfiguration.getNotificationAttributeValue()
                                .equalsIgnoreCase(CHECKED)) {
                    notificationConfigurations.add(notificationConfiguration);
                }
                prepareNotificationDependentAttribute(configurationGroup,
                        notificationConfigurations, notificationAttribute);
            }
        });
        configurationGroup.setNotificationConfiguration(notificationConfigurations);
    }

    private void prepareNotificationDependentAttribute(ConfigurationGroup configurationGroup,
            List<NotificationConfiguration> notificationConfigurations,
            NotificationAttributeDTO notificationAttribute) {
        if (StringUtils.isNotBlank(notificationAttribute.getNotificationAttributeValue())
                && notificationAttribute.getNotificationAttributeValue().equalsIgnoreCase(CHECKED)
                && Objects.nonNull(notificationAttribute.getDependentNotificationAttribute())
                && StringUtils.isBlank(notificationAttribute.getDependentNotificationAttribute()
                        .getNotificationAttributeValue())) {
            throw new NotificationEmailException(StringUtils.EMPTY);
        } else {
            prepareNotificationConfigurationFromDependentAttribute(configurationGroup,
                    notificationConfigurations, notificationAttribute);
        }
    }

    /**
     * @param configurationGroup
     * @param notificationConfigurations
     * @param notificationAttribute
     */
    private void prepareNotificationConfigurationFromDependentAttribute(
            ConfigurationGroup configurationGroup,
            List<NotificationConfiguration> notificationConfigurations,
            NotificationAttributeDTO notificationAttribute) {
        NotificationConfiguration notificationConfiguration;

        if (StringUtils.isNotBlank(notificationAttribute.getNotificationAttributeValue())
                && notificationAttribute.getNotificationAttributeValue().equalsIgnoreCase(CHECKED)
                && Objects.nonNull(notificationAttribute.getDependentNotificationAttribute())
                && StringUtils.isNotBlank(notificationAttribute.getDependentNotificationAttribute()
                        .getNotificationAttributeValue())) {
            // --prepare child data
            notificationConfiguration = prepareNotificationConfigurationFromNotificationAttribute(
                    configurationGroup, notificationAttribute.getDependentNotificationAttribute());
            notificationConfigurations.add(notificationConfiguration);
        } else if (StringUtils.isNotBlank(notificationAttribute.getNotificationAttributeValue())
                && notificationAttribute.getNotificationAttributeValue().equalsIgnoreCase(UNCHECKED)
                && Objects.nonNull(notificationAttribute.getDependentNotificationAttribute())
                && StringUtils.isNotBlank(notificationAttribute.getDependentNotificationAttribute()
                        .getNotificationAttributeValue())) {
            notificationConfiguration = prepareNotificationConfigurationFromNotificationAttribute(
                    configurationGroup, notificationAttribute.getDependentNotificationAttribute());
            notificationConfigurations.remove(notificationConfiguration);
        }
    }

    private NotificationConfiguration prepareNotificationConfigurationFromNotificationAttribute(
            ConfigurationGroup configurationGroup, NotificationAttributeDTO notificationAttribute) {
        NotificationConfiguration notificationConfiguration = new NotificationConfiguration();
        notificationConfiguration
                .setNotificationAttributeId(notificationAttribute.getNotificationAttributeId());
        if (Objects.nonNull(notificationAttribute.getNotificationAttributeValue())
                && !StringUtils.isBlank(notificationAttribute.getNotificationAttributeValue())) {
            notificationConfiguration.setNotificationAttributeValue(
                    notificationAttribute.getNotificationAttributeValue().trim());
        }
        notificationConfiguration.setConfigurationGroup(configurationGroup);
        notificationConfiguration.setCreatedBy(1L);
        return notificationConfiguration;
    }

    private void prepareHolidayConfiguration(ConfigurationGroup configurationGroup) {
        List<HolidayConfiguration> holidayConfigurations = new ArrayList<>();
        configurationGroup.getHolidayConfiguration().forEach(holidayConfiguration -> {
            if (Objects.nonNull(holidayConfiguration)
                    && StringUtils.isNotBlank(holidayConfiguration.getHolidayDescription())
                    && Objects.nonNull(holidayConfiguration.getHoliday())) {
                prepareAuditFieldForHolidayConfig(holidayConfiguration);
                holidayConfiguration.setHolidayDate(
                        DateConverter.convertStringToDate(holidayConfiguration.getHoliday()));
                holidayConfigurations.add(holidayConfiguration);
                configurationGroup.setHolidayConfiguration(holidayConfigurations);
                holidayConfiguration.setConfigurationGroup(configurationGroup);
            } else {
                holidayConfigurations.remove(holidayConfiguration);
                configurationGroup.setHolidayConfiguration(holidayConfigurations);
                holidayConfiguration.setConfigurationGroup(configurationGroup);
            }
        });
    }

    private void prepareTimesheetConfiguration(ConfigurationGroupDTO configurationGroupDTO,
            ConfigurationGroup configurationGroup) {
        TimesheetHourConfiguration timesheetHourConfiguration;
        TimesheetTimeConfiguration timesheetTimeConfiguration;
        TimesheetConfiguration timesheetConfiguration = ConfigurationGroupMapper.INSTANCE
                .timesheetConfigurationDTOToTimesheetConfiguration(configurationGroupDTO);
        populateAuditDetailsForTimesheetConfiguration(timesheetConfiguration);
        setSTOTDTHours(configurationGroupDTO, timesheetConfiguration);
        // --Work input for hourly or timeStamp
        if (null != configurationGroupDTO && !StringUtils.isNotBlank(configurationGroupDTO.getTimeCalculation())) {
            timesheetConfiguration.setTimeCalculation(TimeCalculationEnum.D);
            configurationGroupDTO.setTimeCalculation("D");
        }

        if (null != configurationGroupDTO && !StringUtils.isNotBlank(configurationGroupDTO.getDefaultInput())) {
            timesheetConfiguration.setDefaultInput(WorkInputEnum.H);
            configurationGroupDTO.setDefaultInput("H");
        }

        if (null != configurationGroupDTO && StringUtils.equals(WorkInputEnum.valueOf("H").workInput(),
                configurationGroupDTO.getDefaultInput())) {
            timesheetHourConfiguration = ConfigurationGroupMapper.INSTANCE
                    .configurationGroupDTOToTimesheetHourConfiguration(configurationGroupDTO);
            timesheetHourConfiguration.setTimesheetConfiguration(timesheetConfiguration);
            timesheetConfiguration.setTimesheetHourConfiguration(timesheetHourConfiguration);
        } else if (null != configurationGroupDTO && StringUtils.equals(WorkInputEnum.valueOf("T").workInput(),
                configurationGroupDTO.getDefaultInput())) {
            timesheetTimeConfiguration = ConfigurationGroupMapper.INSTANCE
                    .configurationGroupDTOToTimesheetTimeConfiguration(configurationGroupDTO);
            timesheetTimeConfiguration.setTimesheetConfiguration(timesheetConfiguration);
            timesheetConfiguration.setTimesheetTimeConfiguration(timesheetTimeConfiguration);
        }

        timesheetConfiguration.setConfigurationGroup(configurationGroup);
        configurationGroup.setTimesheetConfiguration(timesheetConfiguration);
    }

    private void populateAuditDetailsForTimesheetConfiguration(
            TimesheetConfiguration timesheetConfiguration) {
        timesheetConfiguration.setCreatedBy(1L);
    }

    private void prepareAuditFieldForHolidayConfig(HolidayConfiguration holidayConfiguration) {
        holidayConfiguration.setCreatedBy(1L);
    }

    private ConfigurationGroup prepareConfigurationGroup(
            ConfigurationGroupDTO configurationGroupDTO) {
        ConfigurationGroup configurationGroup = ConfigurationGroupMapper.INSTANCE
                .configurationDTOToConfigurationGroup(configurationGroupDTO);
        configurationGroup.setActiveFlag(ActiveFlagEnum.Y);
        configurationGroup.setEffectiveFlag(EffectiveFlagEnum.Y);
        configurationGroup.setCreatedBy(1L);
        configurationGroup.setLastModifiedBy(1L);
        configurationGroup.getConfigurationGroupLocation().forEach(configurationGroupLocation -> {
            if (null != configurationGroupLocation) {
                configurationGroupLocation.setConfigurationGroup(configurationGroup);
                if (configurationGroupLocation.getOfficeId() == 0) {
                    configurationGroupLocation.setOfficeId(ALL_LOCATION_ID);

                }
            }
        });

        configurationGroup.getConfigurationGroupUserJobTitle()
                .forEach(configurationGroupUserJobTitle -> {
                    if (Objects.nonNull(configurationGroupUserJobTitle)) {
                        configurationGroupUserJobTitle.setConfigurationGroup(configurationGroup);
                    }
                });


        return configurationGroup;
    }

    private ConfigurationGroupDTO mapConfigurationGroupToDTO(
            ConfigurationGroup configurationGroup) {
        return ConfigurationGroupMapper.INSTANCE.configurationGroupToConfigurationGroupDTO(
                configurationGroup, configurationGroup.getConfigurationGroupLocation(),
                configurationGroup.getConfigurationGroupUserJobTitle(),
                configurationGroup.getTimesheetConfiguration(),
                configurationGroup.getHolidayConfiguration(),
                configurationGroup.getTimesheetConfiguration().getTimesheetHourConfiguration(),
                configurationGroup.getTimesheetConfiguration().getTimesheetTimeConfiguration());
    }

    private void setConfigurationRelatedIdAsNull(ConfigurationGroupDTO configurationGroupDTO) {
        configurationGroupDTO.setConfigurationGroupId(null);
        configurationGroupDTO.setConfigurationId(null);
        configurationGroupDTO.setTimesheetHourId(null);
        configurationGroupDTO.setTimesheetTimeId(null);

        if (Objects.nonNull(configurationGroupDTO.getConfigurationGroupLocation())) {
            configurationGroupDTO.getConfigurationGroupLocation()
                    .forEach(configurationGroupLocation -> 
                        configurationGroupLocation.setGroupLocationId(null));
        }

        if (Objects.nonNull(configurationGroupDTO.getConfigurationGroupUserJobTitle())) {
            configurationGroupDTO.getConfigurationGroupUserJobTitle()
                    .forEach(configurationGroupUserJobTitle -> 
                        configurationGroupUserJobTitle.setConfigurationGroupUserJobTitleId(null));
        }

        if (Objects.nonNull(configurationGroupDTO.getHolidayConfiguration())) {
            configurationGroupDTO.getHolidayConfiguration().forEach(holidayConfiguration -> {
                holidayConfiguration.setTimeSheetHolidayId(null);
                holidayConfiguration.setConfigurationGroupId(null);
            });
        }
    }

    private List<OfficeLocationDTO> getOfficeLocationsWithAll(
            List<Long> configuredOfficeLocationIds, String isActive) {
		List<OfficeLocationDTO> officeLocationDTOs = new OfficeLocationCommand(
				restTemplate,
				DiscoveryClientAndAccessTokenUtil
						.discoveryClient(
								OfficeLocationCommand.COMMON_GROUP_KEY,
								discoveryClient), DiscoveryClientAndAccessTokenUtil.getAccessToken()).execute();

        List<OfficeLocationDTO> returnOfficeLocationDTOs = new ArrayList<>();
        
        officeLocationDTOs.forEach(officeLocationDTO -> {
            if (configuredOfficeLocationIds.contains((Long) officeLocationDTO.getOfficeId())) {
                officeLocationDTO.setIsConfigured(ACTIVE_CONFIGURED);
                officeLocationDTO.setIsActive(isActive);
            }
        });

        if (isActive.equals(ACTIVE_CONFIGURED)) {
            addAllInLocations(returnOfficeLocationDTOs, configuredOfficeLocationIds);
        } else if (!configuredOfficeLocationIds.isEmpty()
                && configuredOfficeLocationIds.contains(ALL_LOCATION_ID)) {
        	returnOfficeLocationDTOs.add(new OfficeLocationDTO(ALL_LOCATION_ID, ALL_LOCATION_NAME,
                    INACTIVE_CONFIGURED, ACTIVE_CONFIGURED));
        } else {
            // -- While on Add New ConfigurationGroup
        	returnOfficeLocationDTOs.add(new OfficeLocationDTO(ALL_LOCATION_ID, ALL_LOCATION_NAME,
                    INACTIVE_CONFIGURED, INACTIVE_CONFIGURED));

        }
        returnOfficeLocationDTOs.addAll(officeLocationDTOs);
        return returnOfficeLocationDTOs;
    }

    private void setHolidayIsActiveFlag(HolidayConfigurationDTO holidayConfigurationDTO,
            String holidayDate) {
        if (Objects.isNull(holidayConfigurationDTO.getHoliday())
                && Objects.nonNull(holidayConfigurationDTO.getHolidayDate())) {
            holidayConfigurationDTO.setHoliday(holidayDate);
        }
        if (DateConverter.convertStringToDate(holidayConfigurationDTO.getHoliday())
                .before(new Date())
                && !DateUtils.isSameDay(
                        DateConverter.convertStringToDate(holidayConfigurationDTO.getHoliday()),
                        new Date())) {
            holidayConfigurationDTO.setIsActive("Y");
        }
        holidayConfigurationDTO.setHoliday(holidayDate);
    }
    
    private void getUserGroupIdsList(List<UUID> userGroupIds, ConfigurationGroup configGroup,
            List<UUID> userGroupIdsList) {
        if (Objects.nonNull(configGroup)
                && CollectionUtils.isNotEmpty(configGroup.getConfigurationGroupUserJobTitle())) {
            configGroup.getConfigurationGroupUserJobTitle().forEach(userJobTitle -> {
                if (userGroupIds.contains(userJobTitle.getUserGroupId())) {
                    userGroupIdsList.add(userJobTitle.getUserGroupId());
                }
            });
        }
    }

    /**
     * @param configurationGroupDTO
     * @param timesheetConfiguration
     */
    private void setSTOTDTHours(ConfigurationGroupDTO configurationGroupDTO,
            TimesheetConfiguration timesheetConfiguration) {
        timesheetConfiguration.setStMinHours(Double.valueOf(0));
        if (Objects.nonNull(configurationGroupDTO)
                && Objects.nonNull(configurationGroupDTO.getOtMaxHours())
                && Objects.nonNull(configurationGroupDTO.getStMaxHours())) {
            timesheetConfiguration.setOtMinHours(configurationGroupDTO.getStMaxHours());
        }
        prepareDTMinHours(configurationGroupDTO, timesheetConfiguration);
    }

    /**
     * @param configurationGroupDTO
     * @param timesheetConfiguration
     */
    private void prepareDTMinHours(ConfigurationGroupDTO configurationGroupDTO,
            TimesheetConfiguration timesheetConfiguration) {
        if (Objects.nonNull(configurationGroupDTO)
                && Objects.nonNull(configurationGroupDTO.getDtMaxHours())
                && Objects.nonNull(configurationGroupDTO.getOtMaxHours())) {
            timesheetConfiguration.setDtMinHours(configurationGroupDTO.getOtMaxHours());
        }
        if (Objects.nonNull(configurationGroupDTO)
                && Objects.isNull(configurationGroupDTO.getOtMaxHours())
                && Objects.nonNull(configurationGroupDTO.getDtMaxHours())
                && Objects.nonNull(configurationGroupDTO.getStMaxHours())) {
            timesheetConfiguration.setDtMinHours(configurationGroupDTO.getStMaxHours());
        }
    }

    /**
     * @param holidayDates
     * @param holidayConfigurationDTO
     */
    private void checkHolidayDateAlreadyExists(List<String> holidayDates,
            HolidayConfigurationDTO holidayConfigurationDTO) {
        if (!holidayDates.contains(holidayConfigurationDTO.getHoliday())) {
            holidayDates.add(holidayConfigurationDTO.getHoliday());
        } else {
            throw new HolidayConfigurationException(holidayConfigurationDTO.getHoliday());
        }
    }

    /**
     * @param holidayConfigurationDTO
     */
    private void checkHolidayDateNull(HolidayConfigurationDTO holidayConfigurationDTO) {
        if (Objects.nonNull(holidayConfigurationDTO)
                && StringUtils.isNotBlank(holidayConfigurationDTO.getHoliday())
                && StringUtils.isBlank(holidayConfigurationDTO.getHolidayDescription())) {
            throw new HolidayDescriptionConfigurationException(StringUtils.EMPTY);
        } else if (Objects.nonNull(holidayConfigurationDTO)
                && StringUtils.isBlank(holidayConfigurationDTO.getHoliday())
                && StringUtils.isNotBlank(holidayConfigurationDTO.getHolidayDescription())) {
            throw new HolidayDateConfigurationException(StringUtils.EMPTY);
        }
    }

    /**
     * 
     * Note: This method only using in configuration group validation, we are using 7 methods and
     * 390 lines. (847 to 1200) We are analysing how to avoid the more IF and ELSE IF conditions,
     * Any other ideas to avoid the more IF and ELSE, Please Share it. Now we are tying in
     * Reflection Polymorphism design pattern format, Once done we will implement.
     * 
     */

    @Override
    public Boolean configurationGroupValidation(ConfigurationGroupDTO value) {
      double otHours =
                Objects.isNull(value.getOtMaxHours()) ? Double.valueOf(0) : value.getOtMaxHours();
         double dtHours =
                Objects.isNull(value.getDtMaxHours()) ? Double.valueOf(0) : value.getDtMaxHours();
         double stHours = value.getStMaxHours() >= 1.0 ? value.getStMaxHours() : null;
        if(Objects.isNull(stHours)) {
          throw new ConfigurationGroupException(
                  accessor.getMessage("configuration.settings.group.st.daily"));
        }
        if (isMandatoryAndPaytypeCheck(value.getTimeCalculation(), stHours)) {
            throw new ConfigurationGroupException(
                    accessor.getMessage("configuration.settings.group.st"));
        }
        if (isMandatoryAndPaytypeCheck(value.getTimeCalculation(), otHours)) {
            throw new ConfigurationGroupException(
                    accessor.getMessage("configuration.settings.group.ot"));
        }
        if (isMandatoryAndPaytypeCheck(value.getTimeCalculation(), dtHours)) {
            throw new ConfigurationGroupException(
                    accessor.getMessage("configuration.settings.group.dt"));
        }
        if(DayTypes.WEEKLY.toString().equals(value.getTimeCalculation())) {
          String weekValue = hoursValidation(stHours,otHours, dtHours, 168);
          if (StringUtils.isNotBlank(weekValue)) {
            throw new ConfigurationGroupException(weekValue);
          }
        } else {
          String hoursValue = hoursValidation(stHours,otHours, dtHours, 24);
          if (StringUtils.isNoneBlank(hoursValue)) {
            throw new ConfigurationGroupException(hoursValue);
          }
        }
        
        double maxOfStOtDt = Math.max(stHours, Math.max(otHours, dtHours));
        if (Objects.nonNull(value.getOtMaxHours()) && 
        		timeDifferenceCheck(stHours, otHours)) {
            throw new ConfigurationGroupException(
                    accessor.getMessage("configuration.settings.group.otMoreThanST"));
        }
        if (Objects.nonNull(value.getDtMaxHours()) && Objects.nonNull(value.getOtMaxHours()) && 
        		timeDifferenceCheck(otHours, dtHours)) {
            throw new ConfigurationGroupException(
                    accessor.getMessage("configuration.settings.group.dtMoreThanOT"));
        }
        
        if (Objects.isNull(value.getOtMaxHours()) && Objects.nonNull(value.getDtMaxHours()) && 
        		timeDifferenceCheck(stHours, dtHours)) {
            throw new ConfigurationGroupException(
                    accessor.getMessage("configuration.settings.group.dtMoreThanST"));
        }
        
        if (checkMaxHours(stHours, otHours, dtHours, value.getMaxHours(), value.getTimeCalculation())) {
            throw new ConfigurationGroupException(
                    accessor.getMessage("configuration.settings.group.maxNotEqualSTOTDT"));
        }
        if (DayTypes.TIME_STAMP.toString().equals(value.getDefaultInput())) {
          if (timestampValidation(value)) {
            throw new ConfigurationGroupException(
                    accessor.getMessage("configuration.settings.group.weekly"));
          }
          weeklyTimeCheck(value);
          weeklyTimestampValuesCheck(value);

        } else {
          if ("RCTR".equals(value.getUserGroupCategory())) {
            if (dailyValidation(value)) {
              throw new ConfigurationGroupException(
                  accessor.getMessage("configuration.settings.group.daily"));
            }
            if (DayTypes.DAILY.toString().equals(value.getTimeCalculation())) {
              dailyRecuriterCheck(value);
            } else {
                if (maxOfStOtDt > 168) {
                  throw new ConfigurationGroupException(accessor
                          .getMessage("configuration.settings.group.maxOfSTOTDT.exceedWeek"));
              }
              if (Double.sum(value.getSundayHours(),
                      Double.sum(value.getMondayHours(),
                              Double.sum(value.getTuesdayHours(),
                                      Double.sum(value.getWednesdayHours(),
                                              Double.sum(value.getThursdayHours(),
                                                      Double.sum(value.getFridayHours(),
                                                              value.getSaturdayHours())))))) > value
                                                                      .getMaxHours()) {
                  throw new ConfigurationGroupException(accessor
                          .getMessage("configuration.settings.group.totalWeekHourExceedMax"));
              }
            }
            
          }
        }
        return true;
    }

    private boolean isMandatoryAndPaytypeCheck(String payType, double value) {
      if (DayTypes.WEEKLY.toString().equals(payType)) {
        return (value >= 0 && value <= 168) ? false : true;
      } else {
        return (value >= 0 && value <= 24) ? false : true;
      }
    }

    private boolean timeDifferenceCheck(double minValue, double maxValue) {
        return (Double.compare(minValue, maxValue) == 0 || minValue > maxValue) ? true : false;
    }

    private boolean checkMaxHours(double stHours, double otHours, double dtHours, double maxHours, String payType) {
    double maxValue =
        DayTypes.WEEKLY.toString().equals(payType) ? Math.max(stHours, Math.max(otHours, dtHours))
            : (Double.parseDouble(decimalFormat.format(Math.max(stHours, Math.max(otHours, dtHours)) * 7)));
        return Double.compare(maxValue, maxHours) == 0 ? false : true;
    }

    private boolean checkHours(double dailyHours, double maxOfStOtDt) {
      return (dailyHours >= 0 && dailyHours <= 24 && dailyHours <= maxOfStOtDt) ? false : true;
    }

    private boolean timestampValueCheck(double workHours,double breakHours, double maxOfStOtDt) {
        return (workHours - breakHours) <= maxOfStOtDt ? false : true;
    }

    private boolean timestampValidation(ConfigurationGroupDTO arg0) {
        boolean obj = false;
        List<String> objects = new ArrayList<>();
        objects.add(arg0.getBreakStartTime());
        objects.add(arg0.getBreakEndTime());
        objects.add(arg0.getMondayStartTime());
        objects.add(arg0.getMondayEndTime());
        objects.add(arg0.getTuesdayStartTime());
        objects.add(arg0.getTuesdayEndTime());
        objects.add(arg0.getWednesdayStartTime());
        objects.add(arg0.getWednesdayEndTime());
        objects.add(arg0.getThursdayStartTime());
        objects.add(arg0.getThursdayEndTime());
        objects.add(arg0.getFridayStartTime());
        objects.add(arg0.getFridayEndTime());
        for (String list : objects) {
            if (StringUtils.isBlank(list)) {
                obj = true;
                break;
            }
        }
        return obj;
    }

    private boolean dailyValidation(ConfigurationGroupDTO groupDTO) {
        boolean returnObject = false;
        List<Object> dailyList = new ArrayList<>();
        dailyList.add(groupDTO.getSundayHours());
        dailyList.add(groupDTO.getMondayHours());
        dailyList.add(groupDTO.getTuesdayHours());
        dailyList.add(groupDTO.getWednesdayHours());
        dailyList.add(groupDTO.getThursdayHours());
        dailyList.add(groupDTO.getFridayHours());
        dailyList.add(groupDTO.getSaturdayHours());
        for (Object list : dailyList) {
            if (Objects.isNull(list)) {
                returnObject = true;
                break;
            }
        }
        return returnObject;
    }

    private String hoursValidation(double stHours,double otHours,double dtHours, double hours) {
        if (Double.compare(hours, stHours) == 0
                && dtHours > 0) {
            return String.format(ConfigurationGroupServiceImpl.S_S_S, "The ST hour", hours, "and DT hour not empty");
        } else if (Double.compare(hours, stHours) == 0
                && otHours > 0) {
            return String.format(ConfigurationGroupServiceImpl.S_S_S, "The ST hour ", hours, "and OT hour not empty");
        } else if (stHours < hours
                && Double.compare(hours, otHours) == 0
                && dtHours > 0) {
            return String.format(ConfigurationGroupServiceImpl.S_S_S, "The OT hour ", hours, "and DT hour not empty");
        }
        return null;
    }

    private boolean weeklyTimeCheck(ConfigurationGroupDTO value) {
        weeklyTimeCheck(value.getBreakStartTime(), value.getBreakEndTime(),
                value.getSundayStartTime(), value.getSundayEndTime(), Days.Sunday.toString());
        weeklyTimeCheck(value.getBreakStartTime(), value.getBreakEndTime(),
                value.getMondayStartTime(), value.getMondayEndTime(), Days.Monday.toString());
        weeklyTimeCheck(value.getBreakStartTime(), value.getBreakEndTime(),
                value.getTuesdayStartTime(), value.getTuesdayEndTime(), Days.Tuesday.toString());
        weeklyTimeCheck(value.getBreakStartTime(), value.getBreakEndTime(),
                value.getWednesdayStartTime(), value.getWednesdayEndTime(),
                Days.Wednesday.toString());
        weeklyTimeCheck(value.getBreakStartTime(), value.getBreakEndTime(),
                value.getThursdayStartTime(), value.getThursdayEndTime(), Days.Thursday.toString());
        weeklyTimeCheck(value.getBreakStartTime(), value.getBreakEndTime(),
                value.getFridayStartTime(), value.getFridayEndTime(), Days.Friday.toString());
        weeklyTimeCheck(value.getBreakStartTime(), value.getBreakEndTime(),
                value.getSaturdayStartTime(), value.getSaturdayEndTime(), Days.Saturday.toString());
        return true;
    }

    private boolean weeklyTimestampValuesCheck(ConfigurationGroupDTO value) {
        double breakHours =
                DateConverter.timeDifference(value.getBreakStartTime(), value.getBreakEndTime());
        double sundayTimeStampValue =
                DateConverter.timeDifference(value.getSundayStartTime(), value.getSundayEndTime());
        double mondayTimeStampValue =
                DateConverter.timeDifference(value.getMondayStartTime(), value.getMondayEndTime());
        double tuesdayTimeStampValue = DateConverter.timeDifference(value.getTuesdayStartTime(),
                value.getTuesdayEndTime());
        double wednesdayTimeStampValue = DateConverter.timeDifference(value.getWednesdayStartTime(),
                value.getWednesdayEndTime());
        double thursdayTimeStampValue = DateConverter.timeDifference(value.getThursdayStartTime(),
                value.getThursdayEndTime());
        double fridayTimeStampValue =
                DateConverter.timeDifference(value.getFridayStartTime(), value.getFridayEndTime());
        double saturdayTimeStampValue = DateConverter.timeDifference(value.getSaturdayStartTime(),
                value.getSaturdayEndTime());
        double otHours =
            Objects.isNull(value.getOtMaxHours()) ? 0 : value.getOtMaxHours();
     double dtHours =
            Objects.isNull(value.getDtMaxHours()) ? 0 : value.getDtMaxHours();
        double maxOfStOtDt = Math.max(value.getStMaxHours(), Math.max(otHours, dtHours));
        if (timestampValueCheck(sundayTimeStampValue, breakHours, maxOfStOtDt))
            throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, Days.Sunday,
                accessor.getMessage(CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY)));
        if (timestampValueCheck(mondayTimeStampValue, breakHours, maxOfStOtDt))
            throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, Days.Monday,
                accessor.getMessage(CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY)));
        if (timestampValueCheck(tuesdayTimeStampValue, breakHours, maxOfStOtDt))
            throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, Days.Tuesday, 
                    accessor.getMessage(CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY)));
        if (timestampValueCheck(wednesdayTimeStampValue, breakHours, maxOfStOtDt))
            throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, Days.Wednesday, 
                    accessor.getMessage(CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY)));
        if (timestampValueCheck(thursdayTimeStampValue, breakHours, maxOfStOtDt))
            throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, Days.Thursday, 
                    accessor.getMessage(CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY)));
        if (timestampValueCheck(fridayTimeStampValue, breakHours, maxOfStOtDt))
            throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, Days.Friday, 
                    accessor.getMessage(CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY)));
        if (timestampValueCheck(saturdayTimeStampValue, breakHours, maxOfStOtDt))
            throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, Days.Saturday, 
                    accessor.getMessage(CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY)));
        if ((sundayTimeStampValue - breakHours) + (mondayTimeStampValue - breakHours)
                + (tuesdayTimeStampValue - breakHours) + (wednesdayTimeStampValue - breakHours)
                + (thursdayTimeStampValue - breakHours) + (fridayTimeStampValue - breakHours)
                + (saturdayTimeStampValue - breakHours) > value.getMaxHours()) {
            throw new ConfigurationGroupException(accessor
                    .getMessage("configuration.settings.group.week.timestamp.maxMoreThanTime"));
        }
        return true;
    }

    private boolean weeklyTimeCheck(String brkSrtTime, String brkEndTime, String wrkSrtTime,
            String wrkEndTime, String day) {
        try {
            Date brkStTime = timeFormat.parse(brkSrtTime);
            Date brkEdTime = timeFormat.parse(brkEndTime);
            Date wrkStTime = timeFormat.parse(wrkSrtTime);
            Date wrkEdTime = timeFormat.parse(wrkEndTime);
            
            if(!brkEdTime.after(brkStTime) || !wrkEdTime.after(wrkStTime)) {
              throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S_S,
                  "Break start and end time or", day, " start and end time is wrong"));
            }
            if ((wrkStTime.after(brkStTime) || wrkStTime.compareTo(brkStTime) == 0)
                && wrkStTime.before(brkEdTime))
                throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, day, 
                        accessor.getMessage(
                                "configuration.settings.group.week.timestamp.workStartTime")));
            if ((wrkEdTime.after(brkStTime) || wrkEdTime.compareTo(brkStTime) == 0)
                && wrkEdTime.before(brkEdTime))
                throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, day, 
                        accessor.getMessage(
                                "configuration.settings.group.week.timestamp.workEndTime")));

            checkBreakStartTime(day, brkStTime, wrkStTime, wrkEdTime);
            checkBreakEndTime(day, brkEdTime, wrkStTime, wrkEdTime);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

	private void checkBreakEndTime(String day, Date brkEdTime, Date wrkStTime,
			Date wrkEdTime) {
		if ((brkEdTime.after(wrkStTime) || brkEdTime.compareTo(wrkStTime) == 0) &&
		    brkEdTime.before(wrkEdTime)) {
		} else {
		    throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S_S,
		        accessor.getMessage("configuration.settings.group.week.timestamp.breakEndTime"),
		        day.toLowerCase(), "work times"));
		}
	}

	private void checkBreakStartTime(String day, Date brkStTime,
			Date wrkStTime, Date wrkEdTime) {
		if ((brkStTime.after(wrkStTime) || brkStTime.compareTo(wrkStTime) == 0) &&
		    brkStTime.before(wrkEdTime)) {
		} else {
		    throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S_S,
		        accessor.getMessage("configuration.settings.group.week.timestamp.breakStartTime"),
		        day.toLowerCase(), " work times"));
		}
	}
    private boolean dailyRecuriterCheck(ConfigurationGroupDTO value) {
      double otHours =
          Objects.isNull(value.getOtMaxHours()) ? 0 : value.getOtMaxHours();
   double dtHours =
          Objects.isNull(value.getDtMaxHours()) ? 0 : value.getDtMaxHours();
      double maxOfStOtDt = Math.max(value.getStMaxHours(), Math.max(otHours, dtHours));
        if (checkHours(value.getSundayHours(), maxOfStOtDt))
            throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, Days.Sunday, 
                    accessor.getMessage(CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY)));
        if (checkHours(value.getMondayHours(), maxOfStOtDt))
            throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, Days.Monday, 
                    accessor.getMessage(CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY)));
        if (checkHours(value.getTuesdayHours(), maxOfStOtDt))
            throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, Days.Tuesday, 
                    accessor.getMessage(CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY)));
        if (checkHours(value.getWednesdayHours(), maxOfStOtDt))
            throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, Days.Wednesday, 
                    accessor.getMessage(CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY)));
        if (checkHours(value.getThursdayHours(), maxOfStOtDt))
            throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, Days.Thursday, 
                    accessor.getMessage(CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY)));
        if (checkHours(value.getFridayHours(), maxOfStOtDt))
            throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, Days.Friday, 
                    accessor.getMessage(CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY)));
        if (checkHours(value.getSaturdayHours(), maxOfStOtDt))
            throw new ConfigurationGroupException(String.format(ConfigurationGroupServiceImpl.S_S, Days.Saturday, 
                    accessor.getMessage(CONFIG_GROUP_DAILY_EXCCEDMAX_ERRORKEY)));
        if (Double.sum(value.getSundayHours(), Double.sum(value.getMondayHours(), Double.sum(
                value.getTuesdayHours(),
                Double.sum(value.getWednesdayHours(), Double.sum(value.getThursdayHours(),
                        Double.sum(value.getFridayHours(), value.getSaturdayHours())))))) > value
                                .getMaxHours()) {
            throw new ConfigurationGroupException(
                    accessor.getMessage("configuration.settings.group.totalWeekHourExceedMax"));
        }
        return true;
    }

    enum Days {
        Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday
    }
    enum DayTypes {
        HOURS, TIME_STAMP, DAILY, WEEKLY
    }
    
}
