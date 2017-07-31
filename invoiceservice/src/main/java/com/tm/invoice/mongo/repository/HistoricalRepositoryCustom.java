package com.tm.invoice.mongo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.invoice.mongo.domain.Historical;

public interface HistoricalRepositoryCustom {

  public Page<Historical> getHistoricals(Long billingSpecialistId, Pageable pageable); 
  
}
