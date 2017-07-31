package com.tm.invoice.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.exception.BusinessException;
import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.invoice.constants.InvoiceErrorConstants;
import com.tm.invoice.constants.InvoiceSetupConstants;
import com.tm.invoice.domain.GlobalInvoiceSetup;
import com.tm.invoice.domain.GlobalInvoiceSetupOption;
import com.tm.invoice.domain.InvoiceSetup;
import com.tm.invoice.domain.InvoiceSetup.ActiveFlag;
import com.tm.invoice.domain.InvoiceSetupNote;
import com.tm.invoice.domain.InvoiceSetupOption;
import com.tm.invoice.domain.InvoiceSetupOption.InvoiceFlag;
import com.tm.invoice.domain.InvoiceSetupView;
import com.tm.invoice.dto.EmployeeProfileDTO;
import com.tm.invoice.dto.InvoiceSetupDTO;
import com.tm.invoice.dto.InvoiceSetupOptionDTO;
import com.tm.invoice.enums.GlobalInvoiceFlag;
import com.tm.invoice.mongo.domain.InvoiceSetupActivitiesLog;
import com.tm.invoice.mongo.dto.InvoiceSetupActivitiesLogDTO;
import com.tm.invoice.mongo.repository.InvoiceSetupActivitiesLogRepository;
import com.tm.invoice.repository.GlobalInvoiceRepository;
import com.tm.invoice.repository.InvoiceSetupNoteRepository;
import com.tm.invoice.repository.InvoiceSetupOptionRepository;
import com.tm.invoice.repository.InvoiceSetupRepository;
import com.tm.invoice.repository.InvoiceSetupViewRepository;
import com.tm.invoice.service.InvoiceService;
import com.tm.invoice.service.InvoiceSetupService;
import com.tm.invoice.service.mapper.InvoiceSetupActivitiesLogMapper;
import com.tm.invoice.service.mapper.InvoiceSetupMapper;

@Service
@Transactional
public class InvoiceSetupServiceImpl implements InvoiceSetupService {

    public static final String DATE_FORMAT = "MM/dd/yyyy";

    private static final String EMPLOYEE_NOT_FOUND = "Employee not found";

    private static final String GLOBAL_TYP = "Global";

    private static final String PRIVATE_TYP = "Private";

    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    private InvoiceSetupActivitiesLogRepository invoiceSetupActivitiesLogRepository;

    private InvoiceSetupRepository invoiceSetupRepository;

    private InvoiceSetupNoteRepository invoiceSetupNoteRepository;

    private InvoiceService invoiceService;

    private InvoiceSetupOptionRepository invoiceSetupOptionRepository;

    private InvoiceSetupViewRepository invoiceSetupViewRepository;

    private GlobalInvoiceRepository globalInvoiceRepository;

    @Inject
    public InvoiceSetupServiceImpl(
            InvoiceSetupActivitiesLogRepository invoiceSetupActivitiesLogRepository,
            InvoiceSetupRepository invoiceSetupRepository,
            InvoiceSetupNoteRepository invoiceSetupNoteRepository, InvoiceService invoiceService,
            InvoiceSetupOptionRepository invoiceSetupOptionRepository,
            InvoiceSetupViewRepository invoiceSetupViewRepository,
            GlobalInvoiceRepository globalInvoiceRepository) {
        this.invoiceSetupActivitiesLogRepository = invoiceSetupActivitiesLogRepository;
        this.invoiceSetupRepository = invoiceSetupRepository;
        this.invoiceSetupNoteRepository = invoiceSetupNoteRepository;
        this.invoiceService = invoiceService;
        this.invoiceSetupOptionRepository = invoiceSetupOptionRepository;
        this.invoiceSetupViewRepository = invoiceSetupViewRepository;
        this.globalInvoiceRepository = globalInvoiceRepository;
    }

    @Override
    public List<InvoiceSetupView> getAllExistingActiveSetups(Long customerId) {
        return invoiceSetupViewRepository.getAllExistingActiveSetups(customerId, GLOBAL_TYP);
    }

    @Override
    public InvoiceSetupDTO populateExistingSetupDetails(UUID invoiceSetupId, String invoiceType,
            Long customerId) {
        InvoiceSetupDTO invoiceSetupDTO = new InvoiceSetupDTO();
        if (null == invoiceSetupId) {
            return invoiceSetupDTO;
        } else {
            if (invoiceType.equals(GLOBAL_TYP)) {
                GlobalInvoiceSetup globalInvoiceSetup =
                        globalInvoiceRepository.findByGlobalInvoiceSetupById(invoiceSetupId);
                if (Objects.nonNull(globalInvoiceSetup)) {
                    invoiceSetupDTO = populateGlobalSetupDetails(invoiceSetupDTO,
                            globalInvoiceSetup, customerId);
                    invoiceSetupDTO.setSetupPresent(true);
                }
            } else if (invoiceType.equals(PRIVATE_TYP)) {
                InvoiceSetup invoiceSetup =
                        invoiceSetupRepository.findByInvoiceSetupId(invoiceSetupId);
                if (Objects.nonNull(invoiceSetup)) {
                    invoiceSetupDTO =
                            populatePrivateSetupDetails(invoiceSetupDTO, invoiceSetup, customerId);
                    invoiceSetupDTO.setSetupPresent(true);
                }
            }
            return invoiceSetupDTO;
        }
    }

    private InvoiceSetupDTO populatePrivateSetupDetails(InvoiceSetupDTO invoiceSetupDTO,
            InvoiceSetup invoiceSetup, Long customerId) {
        invoiceSetupDTO.setActiveFlag(invoiceSetup.getActiveFlag());
        invoiceSetupDTO.setDeliveryModeLookUpName(invoiceSetup.getDeliveryModeLookUpName());
        invoiceSetupDTO.setInvoiceDescription(invoiceSetup.getInvoiceDescription());
        if (CollectionUtils.isNotEmpty(invoiceSetup.getInvoiceSetupOptions())) {
            invoiceSetupDTO.setInvoiceSetupOptions(
                    InvoiceSetupMapper.INSTANCE.invoiceSetupOptionsToInvoiceSetupOptionDTOs(
                            invoiceSetup.getInvoiceSetupOptions()));
        }
        invoiceSetupDTO.setInvoiceTypeName(invoiceSetup.getInvoiceTypeName());
        invoiceSetupDTO.setInvoiceTemplateId(invoiceSetup.getInvoiceTemplateId());
        invoiceSetupDTO.setNotes(invoiceSetup.getNotes());
        invoiceSetupDTO.setBillCycleDay(invoiceSetup.getBillCycleDay());
        invoiceSetupDTO.setBillCycleName(invoiceSetup.getBillCycleName());
        invoiceSetupDTO.setBillCycleStartEndDetail(invoiceSetup.getBillCycleStartEndDetail());
        invoiceSetupDTO.setBillToOrganizationId(customerId);
        return invoiceSetupDTO;
    }

    private InvoiceSetupDTO populateGlobalSetupDetails(InvoiceSetupDTO invoiceSetupDTO,
            GlobalInvoiceSetup globalInvoiceSetup, Long customerId) {
        List<GlobalInvoiceSetupOption> options = new ArrayList<>();
        if (globalInvoiceSetup.getActiveFlag().equals(GlobalInvoiceFlag.Y)) {
            invoiceSetupDTO.setActiveFlag(ActiveFlag.Y);
        } else {
            invoiceSetupDTO.setActiveFlag(ActiveFlag.N);
        }
        invoiceSetupDTO.setDeliveryModeLookUpName(globalInvoiceSetup.getDelivery());
        invoiceSetupDTO.setInvoiceDescription(globalInvoiceSetup.getInvoiceDescription());
        if (CollectionUtils.isNotEmpty(globalInvoiceSetup.getGlobalInvoiceSetupOption())) {
            options = globalInvoiceSetup.getGlobalInvoiceSetupOption();
        }
        List<InvoiceSetupOptionDTO> privateOptions = new ArrayList<>();
        options.forEach(option -> {
            InvoiceSetupOptionDTO privateoption = new InvoiceSetupOptionDTO();
            privateoption.setId(option.getId().toString());
            if (option.getValue().equals(GlobalInvoiceFlag.Y)) {
                privateoption.setValue(InvoiceFlag.Y);
            } else {
                privateoption.setValue(InvoiceFlag.N);
            }
            privateOptions.add(privateoption);
        });
        invoiceSetupDTO.setInvoiceSetupOptions(privateOptions);
        invoiceSetupDTO.setInvoiceTypeName(globalInvoiceSetup.getInvoiceType());
        invoiceSetupDTO.setInvoiceTemplateId(globalInvoiceSetup.getInvTemplateId());
        invoiceSetupDTO.setNotes(globalInvoiceSetup.getNotesToDisplay());
        invoiceSetupDTO.setBillCycleDay(globalInvoiceSetup.getBillCycleFrequencyDay());
        invoiceSetupDTO.setBillCycleName(globalInvoiceSetup.getBillCycleFrequency());
        invoiceSetupDTO.setBillCycleStartEndDetail(globalInvoiceSetup.getBillCycleFrequencyType());
        invoiceSetupDTO.setBillToOrganizationId(customerId);
        return invoiceSetupDTO;
    }

    @Override
    @Transactional
    public InvoiceSetupDTO saveInvoiceSetup(InvoiceSetupDTO invoiceSetupDTO) throws ParseException {
        InvoiceSetup updatedInvoiceSetup;
        validateInvoiceSetupFormat(invoiceSetupDTO);
        validateInvoiceSetupName(invoiceSetupDTO);
        InvoiceSetup oldSetup = new InvoiceSetup();
        if (null != invoiceSetupDTO.getInvoiceSetupId()) {
            oldSetup = invoiceSetupRepository.findOne(invoiceSetupDTO.getInvoiceSetupId());
        }
        updatedInvoiceSetup =
                validateBilCyclesAndupdateInvoiceSetupStatus(invoiceSetupDTO, oldSetup);
        InvoiceSetup response =
                invoiceSetupRepository.findOne(updatedInvoiceSetup.getInvoiceSetupId());
        if (null != response) {
            return InvoiceSetupMapper.INSTANCE.invoiceSetupToInvoiceSetupDTO(response);
        } else {
            throw new BusinessException(InvoiceConstants.NO_INVOICE_SET_UP_FOUND);
        }
    }

    private void validateInvoiceSetupFormat(InvoiceSetupDTO invoiceSetupDTO) {
        String prefix = invoiceSetupDTO.getPrefix();
        Integer startingNumber = invoiceSetupDTO.getStartingNumber();
        String suffixType = invoiceSetupDTO.getSuffixType();
        String separator = invoiceSetupDTO.getSeparator();
        UUID invoiceSetupId = invoiceSetupDTO.getInvoiceSetupId();
        if (StringUtils.isNotBlank(prefix) && null != startingNumber
                && StringUtils.isNotBlank(suffixType) && StringUtils.isNotBlank(separator)) {
            List<InvoiceSetup> invoiceSetups;
            if (null != invoiceSetupId) {
                invoiceSetups = invoiceSetupRepository.getInvoiceSetupsByInvoiceNameFormat(prefix,
                        startingNumber, suffixType, separator, invoiceSetupId);
            } else {
                invoiceSetups = invoiceSetupRepository.getInvoiceSetupsByInvoiceNameFormat(prefix,
                        startingNumber, suffixType, separator);
            }
            if (CollectionUtils.isNotEmpty(invoiceSetups)) {
                throw new BusinessException(
                        InvoiceErrorConstants.ERR_INVOICE_SETUP_FORMAT_ALREADY_EXISTS);
            }
        }

    }

    private void validateInvoiceSetupName(InvoiceSetupDTO invoiceSetupDTO) {
        String invoiceSetupName = invoiceSetupDTO.getInvoiceSetupName();
        InvoiceSetup oldSetup = new InvoiceSetup();
        if (null != invoiceSetupDTO.getInvoiceSetupId()) {
            oldSetup = invoiceSetupRepository.findOne(invoiceSetupDTO.getInvoiceSetupId());
        }
        List<InvoiceSetup> details;
        if (null != invoiceSetupDTO.getInvoiceSetupId()
                && !(oldSetup.getInvoiceSetupName().equals(invoiceSetupName))) {
            details = invoiceSetupRepository.getSetupBySetupName(invoiceSetupName);
        }
        if (null == invoiceSetupDTO.getInvoiceSetupId()) {
            details = invoiceSetupRepository.getSetupBySetupName(invoiceSetupName);
            if (!CollectionUtils.isEmpty(details)) {
                throw new BusinessException(InvoiceErrorConstants.ERR_INVOICE_SETUP_NAME);
            }
        }
    }

    private InvoiceSetup validateBilCyclesAndupdateInvoiceSetupStatus(
            InvoiceSetupDTO invoiceSetupDTO, InvoiceSetup oldSetup) throws ParseException {
        String invoiceTypeName = invoiceSetupDTO.getInvoiceTypeName();
        InvoiceSetup invoiceSetup =
                InvoiceSetupMapper.INSTANCE.invoiceSetupDTOToInvoiceSetup(invoiceSetupDTO);
        InvoiceSetup updatedInvoiceSetup;
        updatedInvoiceSetup = validateInvoiceType(invoiceTypeName, invoiceSetupDTO, invoiceSetup);
        if (null != invoiceSetupDTO.getInvoiceSetupId()) {
            if (!invoiceTypeName.equals(oldSetup.getInvoiceTypeName())) {
                String comment = oldSetup.getInvoiceTypeName() + " "
                        + InvoiceSetupConstants.INVOICE_TYPE_ACTIVITY_LOG + invoiceTypeName;
                saveInvoiceSetupActivityLog(comment, updatedInvoiceSetup.getInvoiceSetupId());
            }
        } else {
            String comment =
                    invoiceTypeName + " " + InvoiceSetupConstants.INVOICE_TYPE_ADDED_ACTIVITY_LOG;
            saveInvoiceSetupActivityLog(comment, updatedInvoiceSetup.getInvoiceSetupId());
        }
        return updatedInvoiceSetup;
    }

    private InvoiceSetup validateInvoiceType(String invoiceTypeName,
            InvoiceSetupDTO invoiceSetupDTO, InvoiceSetup invoiceSetup) throws ParseException {
        InvoiceSetup updatedInvoiceSetup = new InvoiceSetup();
        updatedInvoiceSetup = saveSetup(invoiceSetup, invoiceSetupDTO);
        return updatedInvoiceSetup;
    }

    private InvoiceSetup saveSetup(InvoiceSetup invoiceSetup, InvoiceSetupDTO invoiceSetupDTO)
            throws ParseException {
        String activityStatus = null;
        EmployeeProfileDTO employee = getLoggedUser();
        if (InvoiceConstants.SUBMITTED.equals(invoiceSetupDTO.getAction())) {
            invoiceSetup.setStatus(InvoiceConstants.ACTIVE);
            activityStatus = InvoiceConstants.INVOICE_SETUP_ACTIVATED;
        } else if (InvoiceSetupConstants.UPDATE.equals(invoiceSetupDTO.getAction())) {
            activityStatus = InvoiceSetupConstants.INVOICE_SETUP_UPDATED;
        }
        populateAuditDetailsForInvoiceSetup(invoiceSetupDTO, invoiceSetup, employee);
        InvoiceSetup updatedInvoiceSetup = invoiceSetupRepository.save(invoiceSetup);
        if (null != invoiceSetupDTO.getUpdatedNotes()) {
            saveInvoiceSetupNotes(invoiceSetupDTO, employee, updatedInvoiceSetup);
        }
        if (null != invoiceSetupDTO.getInvoiceSetupOptions()) {
            saveInvoiceSetupOptions(invoiceSetupDTO, updatedInvoiceSetup);
        }
        saveInvoiceSetupActivityLog(activityStatus, updatedInvoiceSetup.getInvoiceSetupId());
        return invoiceSetupRepository.findOne(invoiceSetup.getInvoiceSetupId());
    }

    private void saveInvoiceSetupOptions(InvoiceSetupDTO invoiceSetupDTO,
            InvoiceSetup updatedInvoiceSetup) {
        List<InvoiceSetupOption> options =
                InvoiceSetupMapper.INSTANCE.invoiceSetupOptionDTOsToInvoiceSetupOptions(
                        invoiceSetupDTO.getInvoiceSetupOptions());
        options.forEach(option -> option.setInvoiceSetup(updatedInvoiceSetup));
        invoiceSetupOptionRepository.save(options);
    }

    private void populateAuditDetailsForInvoiceSetup(InvoiceSetupDTO invoiceSetupDTO,
            InvoiceSetup invoiceSetup, EmployeeProfileDTO employee) {
        if (null == invoiceSetupDTO.getInvoiceSetupId()) {
            invoiceSetup.setCreatedBy(employee.getEmployeeId());
            invoiceSetup.setCreatedDate(new Date());
        } else {
            invoiceSetup.setLastModifiedBy(employee.getEmployeeId());
            invoiceSetup.setLastModifiedDate(new Date());
        }
    }

    private void saveInvoiceSetupNotes(InvoiceSetupDTO invoiceSetupDTO, EmployeeProfileDTO employee,
            InvoiceSetup updatedInvoiceSetup) {
        InvoiceSetupNote invoiceSetupNote = new InvoiceSetupNote();
        invoiceSetupNote.setNotes(invoiceSetupDTO.getUpdatedNotes());
        invoiceSetupNote.setInvoiceSetup(updatedInvoiceSetup);
        invoiceSetupNote.setCreatedBy(employee.getEmployeeId());
        invoiceSetupNote.setNotesCreatedBy(employee.getFirstName() + " " + employee.getLastName());
        invoiceSetupNote.setCreatedDate(new Date());
        invoiceSetupNote.setInvoiceNotesDate(new Date());
        invoiceSetupNoteRepository.save(invoiceSetupNote);
    }

    private void saveInvoiceSetupActivityLog(String commentString, UUID invoiceSetupId)
            throws ParseException {
        InvoiceSetupActivitiesLog activityLog = new InvoiceSetupActivitiesLog();
        activityLog.setSourceReferenceId(invoiceSetupId);
        activityLog.setSourceReferenceType("InvoiceSetup");
        activityLog.setComment(commentString + " " + "on" + " "
                + CommonUtils.convertDateFormatForScreenshots(new Date()));
        invoiceSetupActivitiesLogRepository.save(activityLog);
    }

    private EmployeeProfileDTO getLoggedUser() {
        EmployeeProfileDTO employee = invoiceService.getLoggedInUser();
        if (null == employee) {
            throw new BusinessException(EMPLOYEE_NOT_FOUND);
        }
        return employee;
    }


    @Override
    public InvoiceSetupDTO getInvoiceSetup(UUID engagementId) {
        InvoiceSetup invoiceSetup = invoiceSetupRepository.findByEngagementId(engagementId);
        InvoiceSetupDTO invoiceSetupDTO = new InvoiceSetupDTO();
        if (null != invoiceSetup) {
            invoiceSetupDTO =
                    InvoiceSetupMapper.INSTANCE.invoiceSetupToInvoiceSetupDTO(invoiceSetup);
            invoiceSetupDTO.setSetupPresent(true);
        } else {
            invoiceSetupDTO.setSetupPresent(false);
        }
        return invoiceSetupDTO;
    }

    @Override
    public List<InvoiceSetupActivitiesLogDTO> getInvoiceSetupActivityLog(UUID sourceReferenceId) {
        List<InvoiceSetupActivitiesLog> activityLogs = invoiceSetupActivitiesLogRepository
                .findBySourceReferenceIdOrderByUpdatedOnDesc(sourceReferenceId);
        if (CollectionUtils.isNotEmpty(activityLogs)) {
            return InvoiceSetupActivitiesLogMapper.INSTANCE
                    .invoiceSetupActivitiesLogsToInvoiceSetupActivitiesLogDTOs(activityLogs);
        }
        return new ArrayList<>();
    }

}
