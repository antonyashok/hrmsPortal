package com.tm.common.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tm.common.authority.RequiredAuthority;
import com.tm.common.resource.assemeblers.LookUpViewResourceAssembler;
import com.tm.common.service.LookUpViewService;
import com.tm.common.service.dto.LookUpViewDTO;
import com.tm.commonapi.security.AuthoritiesConstants;


@RestController
public class LookUpResource {

    private LookUpViewService lookUpViewService;

    private LookUpViewResourceAssembler lookUpViewResourceAssembler;

    @Inject
    public LookUpResource(LookUpViewService lookUpViewService,
            LookUpViewResourceAssembler lookUpViewResourceAssembler) {
        this.lookUpViewService = lookUpViewService;
        this.lookUpViewResourceAssembler = lookUpViewResourceAssembler;
    }

    @RequestMapping(value = "/invoiceSetup/lookup", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
    public ResponseEntity<List<LookUpViewDTO>> getLookupValues(
            @RequestParam String entityName) {
        return new ResponseEntity<>(lookUpViewResourceAssembler.toLookUpResource(lookUpViewService.findAllInvoiceSetupAttributes(entityName)),
                HttpStatus.OK);
    }

}
