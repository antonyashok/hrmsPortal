package com.tm.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.tm.common.domain.GlobalInvoiceSetupEntityAttribute;
import com.tm.common.repository.GlobalInvoiceSetupEntityAttributeRepository;
import com.tm.common.service.GlobalInvoiceSetupService;
import com.tm.common.service.dto.EntityAttributeInfoDTO;
import com.tm.common.service.dto.GlobalInvoiceSetupAttributeDTO;
import com.tm.common.service.mapper.GlobalInvoiceSetupMapper;

@Service
public class GlobalInvoiceSetupServiceImpl implements GlobalInvoiceSetupService {

  private GlobalInvoiceSetupEntityAttributeRepository globalInvoiceSetupEntityAttributeRepository;
  private final String OPTIONS_STR = "OPTIONS";
  private final String DELIVERY_STR = "DELIVERY";
  private final String TERMS_STR = "TERMS";
  private final String PAY_CURRENCY_STR = "PAY_CURRENCY";
  private final String LINE_OF_BUSINESS_STR = "LINE_OF_BUSINESS";

  @Inject
  public GlobalInvoiceSetupServiceImpl(
      GlobalInvoiceSetupEntityAttributeRepository globalInvoiceSetupEntityAttributeRepository) {
    this.globalInvoiceSetupEntityAttributeRepository = globalInvoiceSetupEntityAttributeRepository;
  }

  @Override
  public GlobalInvoiceSetupAttributeDTO findAllAttributes() {
    List<GlobalInvoiceSetupEntityAttribute> attributes =
        globalInvoiceSetupEntityAttributeRepository.findAll();
    return parseEntityAttributes(attributes);
  }

  private GlobalInvoiceSetupAttributeDTO parseEntityAttributes(
      List<GlobalInvoiceSetupEntityAttribute> attributes) {
    GlobalInvoiceSetupAttributeDTO globalInvoiceSetupAttributeDTO =
        new GlobalInvoiceSetupAttributeDTO();
    globalInvoiceSetupAttributeDTO.setDeliveries(new ArrayList<>());
    globalInvoiceSetupAttributeDTO.setLineOfBusinesses(new ArrayList<>());
    globalInvoiceSetupAttributeDTO.setOptions(new ArrayList<>());
    globalInvoiceSetupAttributeDTO.setPayCurrencies(new ArrayList<>());
    globalInvoiceSetupAttributeDTO.setTerms(new ArrayList<>());
    for (GlobalInvoiceSetupEntityAttribute attribute : attributes) {
      String attributeName = attribute.getAttributeName();
      EntityAttributeInfoDTO attributeInfoDTO = GlobalInvoiceSetupMapper.INSTANCE
          .mapGlobalInvoiceSetupEntityAttributeToEntityAttributeInfoDTO(attribute);
      if (attributeName.equals(DELIVERY_STR)) {
        globalInvoiceSetupAttributeDTO.getDeliveries().add(attributeInfoDTO);
      } else if (attributeName.equals(OPTIONS_STR)) {
        globalInvoiceSetupAttributeDTO.getOptions().add(attributeInfoDTO);
      } else if (attributeName.equals(TERMS_STR)) {
        globalInvoiceSetupAttributeDTO.getTerms().add(attributeInfoDTO);
      } else if (attributeName.equals(LINE_OF_BUSINESS_STR)) {
        globalInvoiceSetupAttributeDTO.getLineOfBusinesses().add(attributeInfoDTO);
      } else if (attributeName.equals(PAY_CURRENCY_STR)) {
        globalInvoiceSetupAttributeDTO.getPayCurrencies().add(attributeInfoDTO);
      }
    }
    return globalInvoiceSetupAttributeDTO;
  }

}
