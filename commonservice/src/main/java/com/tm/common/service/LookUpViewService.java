package com.tm.common.service;

import java.util.List;

import com.tm.common.service.dto.LookUpViewDTO;

public interface LookUpViewService {

    List<LookUpViewDTO> findAllInvoiceSetupAttributes(String entityName);

}
