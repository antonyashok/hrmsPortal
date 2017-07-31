/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.repository.impl.TimesheetDetailsRepositoryImpl.java
 * Author        : Annamalai
 * Date Created  : Mar 15, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.TimesheetDetails;
import com.tm.timesheet.repository.TimesheetDetailsRepositoryCustom;

@Service
public class TimesheetDetailsRepositoryImpl implements TimesheetDetailsRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    private static final String TIMESHEETID = "timesheetId";
    @Inject
    public TimesheetDetailsRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

	@Override
	public List<TimesheetDetails> getAllTimesheetDetailsByTimesheetIds(
			List<UUID> timesheetIds) {
		Query query = new Query();
		query = query.addCriteria(Criteria.where(TIMESHEETID).in(timesheetIds));
		return mongoTemplate.find(query, TimesheetDetails.class);
	}
	
	@Override
	public UUID updateTimesheetdetails(UUID timesheetId, Date timesheetDate, String taskName, double hours) {
		Query query = new Query();
		query = query.addCriteria(Criteria.where(TIMESHEETID).is(timesheetId).and("timesheetDate").is(timesheetDate)
				.and("taskName").is(taskName));
		if (CollectionUtils.isNotEmpty(mongoTemplate.find(query, TimesheetDetails.class))) {
			mongoTemplate.updateMulti(query, new Update().set("hours", (Double) hours), TimesheetDetails.class);
			return timesheetId;
		} else {
			/*
			 * TimesheetDetails timesheetDetails = new TimesheetDetails();
			 * timesheetDetails.setHours(hours);
			 * timesheetDetails.setTaskName(taskName);
			 * timesheetDetails.setTimesheetDate(timesheetDate);
			 * timesheetDetails.setTimesheetId(timesheetId);
			 * mongoTemplate.save(timesheetDetails);
			 */
			return null;
		}
	}

	@Override
	public List<TimesheetDetails> getTimesheetDetails(UUID timesheetId, Date timesheetDate, String taskName) {
		Query query = new Query();
		query = query.addCriteria(Criteria.where(TIMESHEETID).is(timesheetId).and(TimesheetConstants.TIMESHEET_DATE).lte(timesheetDate).gte(timesheetDate).and("taskName").is(taskName));
		return  mongoTemplate.find(query, TimesheetDetails.class);
	}
    
}
