package com.tm.invoice.web.rest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tm.commonapi.security.AuthoritiesConstants;
import com.tm.commonapi.security.RequiredAuthority;
import com.tm.invoice.dto.ActivityLogDTO;
import com.tm.invoice.exception.ActivityLogNotFoundException;
import com.tm.invoice.service.ActivityLogService;

@RestController
@RequestMapping("/activities")
public class ActivityLogResource {

  private ActivityLogService activityLogService;

  @Inject
  public ActivityLogResource(ActivityLogService activityLogService) {
    this.activityLogService = activityLogService;
  }

  @RequestMapping(value = "/{sourceReferenceId}", method = RequestMethod.GET)
  @RequiredAuthority({ AuthoritiesConstants.FINANCE_MANAGER,AuthoritiesConstants.FINANCE_REPRESENTATIVE })
  public ResponseEntity<List<ActivityLogDTO>> getActivityLog(@PathVariable("sourceReferenceId") UUID sourceReferenceId) {
    List<ActivityLogDTO> activityLogDTOs = activityLogService.getActivityLog(sourceReferenceId);
    return Optional.ofNullable(activityLogDTOs)
        .map(result -> new ResponseEntity<>(activityLogDTOs, HttpStatus.OK))
        .orElseThrow(() -> new ActivityLogNotFoundException());
  }

}
