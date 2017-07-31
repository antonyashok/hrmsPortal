package com.tm.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.tm.common.domain.LookUpView;
import com.tm.common.repository.LookUpViewRepository;
import com.tm.common.service.LookUpViewService;
import com.tm.common.service.dto.LookUpViewDTO;
import com.tm.common.service.mapper.LookUpViewMapper;

@Service
public class LookUpViewServiceImpl implements LookUpViewService {

    private LookUpViewRepository lookUpViewRepository;

    public LookUpViewServiceImpl(LookUpViewRepository lookUpViewRepository) {

        this.lookUpViewRepository = lookUpViewRepository;
    }

    @Override
    public List<LookUpViewDTO> findAllInvoiceSetupAttributes(String entityName) {
        List<LookUpView> results = lookUpViewRepository.findByEntityName(entityName);
        if (null != results) {
            return LookUpViewMapper.INSTANCE.mapLookUpViewListToLookUpViewDTOList(results);
        }
        return new ArrayList<>();
    }

}
