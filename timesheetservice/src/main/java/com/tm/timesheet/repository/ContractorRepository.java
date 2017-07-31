package com.tm.timesheet.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tm.timesheet.domain.Contractor;

public interface ContractorRepository extends PagingAndSortingRepository<Contractor, Long> {

    Page<Contractor> findByProjectId(String projectId, Pageable pageable);
    
    List<Contractor> findByProjectIdOrderByName(String projectId);
   
}
