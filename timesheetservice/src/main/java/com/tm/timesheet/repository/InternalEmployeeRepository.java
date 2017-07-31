package com.tm.timesheet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tm.timesheet.domain.InternalEmployee;

public interface InternalEmployeeRepository extends PagingAndSortingRepository<InternalEmployee, Long> {    
    
    @Query("select e from InternalEmployee e order by e.name")
    List<InternalEmployee> getAllEmployees();
}
