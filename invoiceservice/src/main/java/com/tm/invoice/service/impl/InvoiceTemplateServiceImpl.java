package com.tm.invoice.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import com.tm.commonapi.exception.BusinessException;
import com.tm.invoice.domain.InvoiceTemplate;
import com.tm.invoice.domain.InvoiceTemplate.Active;
import com.tm.invoice.dto.InvoiceTemplateDTO;
import com.tm.invoice.repository.InvoiceTemplateRepository;
import com.tm.invoice.service.InvoiceTemplateService;
import com.tm.invoice.service.mapper.InvoiceTemplateMapper;

@Service
public class InvoiceTemplateServiceImpl implements InvoiceTemplateService {
    
    private static final String TEMP_NOT_FOUND = "Template Not Found";
    
    private InvoiceTemplateRepository invoiceTemplateRepository;
    
    @Inject
    public InvoiceTemplateServiceImpl(InvoiceTemplateRepository invoiceTemplateRepository){
        
        this.invoiceTemplateRepository=invoiceTemplateRepository;
    }

    @Override
    public List<InvoiceTemplateDTO> getTemplateList() {
       Active activeStatus=Active.Y;
       List<InvoiceTemplateDTO> templateDTOs=new ArrayList<>();
       List<InvoiceTemplate> templateList;
       templateList = invoiceTemplateRepository.getAllActivetemplates(activeStatus);
       if (CollectionUtils.isNotEmpty(templateList)) {
           templateList.forEach(template->template.setTemplate(null));
           templateDTOs = InvoiceTemplateMapper.INSTANCE
                   .invoiceTemplatesToInvoiceTemplateDTOs(templateList);
       }
        return templateDTOs; 
    }

    @Override
    public InvoiceTemplateDTO getTemplate(Long invoiceTemplateId) {
        InvoiceTemplateDTO templateDTO;
        InvoiceTemplate template=invoiceTemplateRepository.findByInvoiceTemplateId(invoiceTemplateId);
        if(Objects.nonNull(template)){          
        templateDTO=InvoiceTemplateMapper.INSTANCE.invoiceTemplateToInvoiceTemplateDTO(template);
        }else{
            throw new BusinessException(TEMP_NOT_FOUND);
        }
        return templateDTO;
    }
}
