package com.tm.invoice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.exception.BusinessException;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.invoice.constants.InvoiceErrorConstants;
import com.tm.invoice.domain.ActivityLog;
import com.tm.invoice.domain.GlobalInvoiceSetup;
import com.tm.invoice.domain.GlobalInvoiceSetupGrid;
import com.tm.invoice.domain.GlobalInvoiceSetupOption;
import com.tm.invoice.domain.InvoiceTemplate;
import com.tm.invoice.dto.GlobalInvoiceSetupDTO;
import com.tm.invoice.dto.GlobalInvoiceSetupGridDTO;
import com.tm.invoice.dto.InvoiceTemplateDTO;
import com.tm.invoice.enums.GlobalInvoiceFlag;
import com.tm.invoice.enums.GlobalInvoiceStatus;
import com.tm.invoice.mapper.GlobalInvoiceSetupMapper;
import com.tm.invoice.mongo.repository.ActivityLogRepository;
import com.tm.invoice.repository.GlobalInvoiceRepository;
import com.tm.invoice.repository.GlobalInvoiceSetupGridRepository;
import com.tm.invoice.repository.InvoiceTemplateRepository;
import com.tm.invoice.service.GlobalInvoiceSetupService;

@Service
@Transactional
public class GlobalInvoiceSetupServiceImpl implements GlobalInvoiceSetupService {

	private static final Logger log = LoggerFactory.getLogger(GlobalInvoiceSetupServiceImpl.class);
	
    private GlobalInvoiceSetupGridRepository globalInvoiceSetupGridRepository;
    private InvoiceTemplateRepository invoiceTemplateRepository;
    private GlobalInvoiceRepository globalInvoiceRepository; 
    private ActivityLogRepository activityLogRepository;

    @Inject
    public GlobalInvoiceSetupServiceImpl(
            GlobalInvoiceSetupGridRepository globalInvoiceSetupGridRepository,
            InvoiceTemplateRepository invoiceTemplateRepository,
            GlobalInvoiceRepository globalInvoiceRepository,
            ActivityLogRepository activityLogRepository) {
        this.globalInvoiceSetupGridRepository = globalInvoiceSetupGridRepository;
        this.invoiceTemplateRepository = invoiceTemplateRepository;
        this.globalInvoiceRepository = globalInvoiceRepository;
        this.activityLogRepository = activityLogRepository;
        
    }

    @Transactional(readOnly = true)
    @Override
    public Page<GlobalInvoiceSetupGridDTO> getGlobalInvoiceSetups(String status,
            Pageable pageable) {
        Pageable pageableRequest = pageable;
        if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
            pageableRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.Direction.ASC, InvoiceConstants.CREATED_DATE_STR);
        }
        Page<GlobalInvoiceSetupGrid> globalInvoiceSetupGrids =
                globalInvoiceSetupGridRepository.findByInvoiceStatus(status, pageableRequest);
        if (Objects.nonNull(globalInvoiceSetupGrids)
                && CollectionUtils.isNotEmpty(globalInvoiceSetupGrids.getContent())) {
        	List<GlobalInvoiceSetupGridDTO> result = GlobalInvoiceSetupMapper.INSTANCE
                    .globalInvoiceSetupGridsToGlobalInvoiceSetupGridDTOs(
                            globalInvoiceSetupGrids.getContent());
            return new PageImpl<>(result, pageable, globalInvoiceSetupGrids.getTotalElements());
        }
        return new PageImpl<>(new ArrayList<>(), pageable, globalInvoiceSetupGrids.getTotalElements());
    }

       
    @Transactional(readOnly = true)
    @Override
    public List<InvoiceTemplateDTO> getTemplateDetails() {
        List<InvoiceTemplate> invoicetemplate = invoiceTemplateRepository.findAll();
        List<InvoiceTemplateDTO> result = new ArrayList<>();
        if(Objects.nonNull(invoicetemplate)&& CollectionUtils.isNotEmpty(invoicetemplate)){
            result = GlobalInvoiceSetupMapper.INSTANCE.invoiceTemplateDTOToInvoiceTemplate(invoicetemplate);
         }
        return result;
    }
    
    /*1. Billing Specialist updated from John to Greyson
    2. Delivery method has been updated from Email to USPS
    3. Template has been updated from Template 1 to Template 2
    4. Terms has been updated from 1.5% 10 Net 30 to Net 10
    5. Bill cycle has been updated from Monthly to Bi-weekly*/

    @Override
    public void saveGlobalInvoiceSetup(GlobalInvoiceSetupDTO globalInvoiceSetupDTO) {
        if (null == globalInvoiceSetupDTO) {
            throw new BusinessException(InvoiceConstants.NO_GLOBALINVOICE_SET_UP_FOUND);
        }
        validateInvoiceSetupFormat(globalInvoiceSetupDTO);
        checkExists(globalInvoiceSetupDTO);
        GlobalInvoiceSetup globalInvoiceSetup=prepareGlobalInvoiceSetup(globalInvoiceSetupDTO);
        
        for (GlobalInvoiceSetupOption globalInvoiceSetupOption : globalInvoiceSetup.getGlobalInvoiceSetupOption()) {
        	globalInvoiceSetupOption.setGlobalInvoiceSetup(globalInvoiceSetup);
		}
        globalInvoiceRepository.save(globalInvoiceSetup);
    }
    
    private void validateInvoiceSetupFormat(GlobalInvoiceSetupDTO invoiceSetupDTO) {
      String prefix = invoiceSetupDTO.getPrefix();
      Integer startingNumber = invoiceSetupDTO.getStartingNumber();
      String suffixType = invoiceSetupDTO.getSuffixType();
      String separator = invoiceSetupDTO.getSeparator();
      UUID invoiceSetupId = invoiceSetupDTO.getInvoiceSetupId();
      if(StringUtils.isNotBlank(prefix) && null != startingNumber &&  StringUtils.isNotBlank(suffixType) && StringUtils.isNotBlank(separator)) {
        List<GlobalInvoiceSetup> invoiceSetups;
        if(null != invoiceSetupId) {
          invoiceSetups = globalInvoiceRepository.getInvoiceSetupsByInvoiceNameFormat(prefix, startingNumber, suffixType, separator, invoiceSetupId);
        } else {
          invoiceSetups = globalInvoiceRepository.getInvoiceSetupsByInvoiceNameFormat(prefix, startingNumber, suffixType, separator);
        }
        if(CollectionUtils.isNotEmpty(invoiceSetups)) {
          throw new BusinessException(InvoiceErrorConstants.ERR_GLOBAL_INVOICE_SETUP_FORMAT_ALREADY_EXISTS);
        }
      }
      
    }
    
    private void checkExists(GlobalInvoiceSetupDTO globalInvoiceSetupDTO){
    	GlobalInvoiceSetup checkExists=globalInvoiceRepository.findOne(globalInvoiceSetupDTO.getInvoiceSetupId());
    	
    	if(Objects.nonNull(checkExists)){
    		GlobalInvoiceSetup globalInvoiceSetup = globalInvoiceRepository.findByInvoiceSetupName(globalInvoiceSetupDTO.getInvoiceSetupName(),globalInvoiceSetupDTO.getInvoiceSetupId());
            if (null != globalInvoiceSetup) {
            	 throw new BusinessException(InvoiceConstants.GLOBALINVOICE_SET_UP_NAME_ALREADY_EXIST);
            }
    	}
    	else{
    		GlobalInvoiceSetup globalInvoiceSetup = globalInvoiceRepository.findByInvoiceSetupName(globalInvoiceSetupDTO.getInvoiceSetupName());
            if (null != globalInvoiceSetup) {
            	 throw new BusinessException(InvoiceConstants.GLOBALINVOICE_SET_UP_NAME_EXIST);
            }
    	}
    		
    	saveGlobalInvoiceActivityLog(globalInvoiceSetupDTO,checkExists);
        if(Objects.nonNull(checkExists)){
        	globalInvoiceRepository.delete(globalInvoiceSetupDTO.getInvoiceSetupId());
        }
    }
    
    private void saveGlobalInvoiceActivityLog(GlobalInvoiceSetupDTO globalInvoiceSetupDTO,GlobalInvoiceSetup checkExists){
    	List<String> commentString=new ArrayList<>();
    	if(Objects.nonNull(checkExists)){
    		if(!globalInvoiceSetupDTO.getDelivery().equals(checkExists.getDelivery())){
    			commentString.add("Delivery method has been updated from "+checkExists.getDelivery()+" to "+globalInvoiceSetupDTO.getDelivery()+"\n");
        	}
    		if(!globalInvoiceSetupDTO.getBillCycleFrequency().equals(checkExists.getBillCycleFrequency())){
    			commentString.add("Bill cycle has been updated from "+checkExists.getBillCycleFrequency()+" to "+globalInvoiceSetupDTO.getBillCycleFrequency()+"\n");
        	}
    		if(!globalInvoiceSetupDTO.getInvTemplateId().equals(checkExists.getInvTemplateId())){
    			InvoiceTemplate invoiceTemplateOldValue=invoiceTemplateRepository.findOne(checkExists.getInvTemplateId());
    			InvoiceTemplate invoiceTemplateValue=invoiceTemplateRepository.findOne(globalInvoiceSetupDTO.getInvTemplateId());
    			commentString.add("Template has been updated from "+invoiceTemplateOldValue.getInvoiceTemplateName()+" to "+invoiceTemplateValue.getInvoiceTemplateName()+"\n");
        	}
    	}else{
    		commentString.add("Global Invoice setup has been created");
    	}
    	saveLogList(commentString,globalInvoiceSetupDTO);
    }
    private void saveLogList(List<String> commentString,GlobalInvoiceSetupDTO globalInvoiceSetupDTO){
    	if(CollectionUtils.isNotEmpty(commentString)){
    		for (String string : commentString) {
    			ActivityLog activityLog = new ActivityLog();
	        	activityLog.setId(ResourceUtil.generateUUID());
	        	activityLog.setSourceReferenceId(globalInvoiceSetupDTO.getInvoiceSetupId());
	        	activityLog.setSourceReferenceType("GlobalInvoiceSetup");
	        	activityLog.setComment(string);
	        	try{
	        		activityLogRepository.save(activityLog);
	        	}
	        	catch (Exception e) {
					log.error("Error while saveLogList() :: "+e);
				}
			}
    	}
    }
    
    private GlobalInvoiceSetup prepareGlobalInvoiceSetup(GlobalInvoiceSetupDTO globalInvoiceSetupDTO){
    	
    	checkGlobalInvoiceSetup(globalInvoiceSetupDTO);
    	
    	GlobalInvoiceSetup globalInvoiceSetup=GlobalInvoiceSetupMapper.INSTANCE.globalInvoiceSetupDTOToGlobalInvoiceSetup(globalInvoiceSetupDTO);
    	globalInvoiceSetup.setGlobalInvoiceSetupOption(
    			GlobalInvoiceSetupMapper.INSTANCE.globalInvoiceSetupOptionDTOToGlobalInvoiceSetupOption(globalInvoiceSetupDTO.getGlobalInvoiceSetupOption()));
    	
    	return globalInvoiceSetup;
    }
    
    
    private void checkGlobalInvoiceSetup(GlobalInvoiceSetupDTO globalInvoiceSetupDTO){
    	if(null==globalInvoiceSetupDTO.getInvoiceSetupId()){
    	 	throw new BusinessException(InvoiceConstants.GLOBALINVOICE_SET_UP_ID_EMPTY);
    	}
    	if(!StringUtils.isNotBlank(globalInvoiceSetupDTO.getInvoiceSetupName())){
    		 throw new BusinessException(InvoiceConstants.GLOBALINVOICE_SET_UP_NAME_EMPTY);
    	}
    	if(!StringUtils.isNotBlank(globalInvoiceSetupDTO.getInvoiceType())){
			 throw new BusinessException(InvoiceConstants.GLOBALINVOICE_SET_UP_TYPE_EMPTY);
		}
    	
    	if(null==globalInvoiceSetupDTO.getInvTemplateId()){
			 throw new BusinessException(InvoiceConstants.GLOBALINVOICE_SET_UP_TEMPLATE_EMPTY);
		}
    	if(!StringUtils.isNotBlank(globalInvoiceSetupDTO.getDelivery())){
			 throw new BusinessException(InvoiceConstants.GLOBALINVOICE_SET_UP_DELIVERY_EMPTY);
		}
    	if(!StringUtils.isNotBlank(globalInvoiceSetupDTO.getInvoiceSpecialistNotes())){
			 throw new BusinessException(InvoiceConstants.GLOBALINVOICE_SET_UP_NOTES_EMPTY);
		}
    }
    
    
    @Override
    public GlobalInvoiceSetupDTO getGlobalInvoiceSetup(String globalinvoicesetupid) {
    	GlobalInvoiceSetup invoiceSetupList =
    			globalInvoiceRepository.findByGlobalInvoiceSetupById(UUID.fromString(globalinvoicesetupid));
    	GlobalInvoiceSetupDTO globalInvoiceSetupDTO=GlobalInvoiceSetupMapper.INSTANCE.globalInvoiceSetupToGlobalInvoiceSetupDTO(invoiceSetupList);
    	globalInvoiceSetupDTO.setGlobalInvoiceSetupOption(
    			GlobalInvoiceSetupMapper.INSTANCE.globalInvoiceSetupOptionToGlobalInvoiceSetupOptionDTO(invoiceSetupList.getGlobalInvoiceSetupOption()));
    	 return globalInvoiceSetupDTO;
    }
    
    @Override
    public void updateGlobalInvoiceSetupStatus(GlobalInvoiceSetupDTO globalInvoiceSetupDTO) {
        if (null == globalInvoiceSetupDTO) {
            throw new BusinessException(InvoiceConstants.NO_GLOBALINVOICE_SET_UP_FOUND);
        }
        globalInvoiceRepository.updateGlobalInvoiceSetupStatus(
        		globalInvoiceSetupDTO.getInvoiceSetupId(),
        		globalInvoiceSetupDTO.getInvoiceStatus());
    }
    
    @Override
   	public List<GlobalInvoiceSetupDTO> getGlobalInvoiceSetup() {
   		List<GlobalInvoiceSetup> glopalSetupList = globalInvoiceRepository.findByActiveFlagAndInvoiceStatus(GlobalInvoiceFlag.Y,GlobalInvoiceStatus.Active);
   		return GlobalInvoiceSetupMapper.INSTANCE.globalInvoiceSetupListToGlobalInvoiceSetupDTOList(glopalSetupList);
   	}

}
