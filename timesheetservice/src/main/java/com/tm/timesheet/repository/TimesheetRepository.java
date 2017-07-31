/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.repository.TimesheetRepository.java
 * Author        : Antony Ashok A
 * Date Created  : Mar 11, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tm.timesheet.domain.Timesheet;


@Repository
public interface TimesheetRepository
        extends MongoRepository<Timesheet, UUID> {
	
	
}
