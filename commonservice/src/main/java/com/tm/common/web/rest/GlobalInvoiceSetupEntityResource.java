package com.tm.common.web.rest;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tm.common.authority.RequiredAuthority;
import com.tm.common.service.GlobalInvoiceSetupService;
import com.tm.common.service.dto.GlobalInvoiceSetupAttributeDTO;
import com.tm.commonapi.security.AuthoritiesConstants;


@RestController
public class GlobalInvoiceSetupEntityResource {

  private GlobalInvoiceSetupService globalInvoiceSetupService;

  @Inject
  public GlobalInvoiceSetupEntityResource(GlobalInvoiceSetupService globalInvoiceSetupService) {
    this.globalInvoiceSetupService = globalInvoiceSetupService;
  }

  @RequestMapping(value = "/gis/entity-attributes/lookup",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @RequiredAuthority({ AuthoritiesConstants.SUPER_ADMIN, AuthoritiesConstants.PROFILE_VIEW })
  public ResponseEntity<GlobalInvoiceSetupAttributeDTO> getEntityAttributeLookup() {
    return new ResponseEntity<>(globalInvoiceSetupService.findAllAttributes(), HttpStatus.OK);
  }


}
