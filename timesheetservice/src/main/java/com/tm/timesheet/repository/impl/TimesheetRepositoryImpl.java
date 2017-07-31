/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.repository.impl.TimesheetRepositoryImpl.java
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

import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.repository.TimesheetRepositoryCustom;
import com.tm.timesheet.service.dto.TimesheetStatusCount;

@Service
public class TimesheetRepositoryImpl implements TimesheetRepositoryCustom {

    private final MongoTemplate mongoTemplate;
    
    private static final String TIMESHEETID = "id";
	private static final String START_DATE = "startDate";
	private static final String END_DATE = "endDate";
	private static final String STATUS = "status";


    @Inject
    public TimesheetRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

	@Override
	public List<TimesheetStatusCount> getStatusCount(Date startDate,
			Date endDate, Long employeeId, String roleId) throws ParseException {
		return Collections.<TimesheetStatusCount> emptyList();
	}

	@Override
	public Page<Timesheet> getAllTimesheet(Long employeeId, String status,
			Date startDate, Date endDate, Pageable pageable, String roleId) {
		return null;
	}

	@Override
	public List<Timesheet> getAllTimesheetbyIds(List<UUID> timesheetIds) {
		Query query = new Query();
		query = query.addCriteria(Criteria.where(TIMESHEETID).in(timesheetIds));
		return mongoTemplate.find(query, Timesheet.class);
	}
	
	@Override
	public List<Timesheet> getCreatedTimesheetsDetail(Date startDate, Date endDate) {
		Query query = new Query();
		if (startDate != null && endDate != null) {
			query = query.addCriteria(new Criteria().orOperator(
					Criteria.where(START_DATE).lte(startDate).andOperator(Criteria.where(END_DATE).gte(startDate)),
					Criteria.where(START_DATE).lte(endDate).andOperator(Criteria.where(END_DATE).gte(endDate))));
		}
		return mongoTemplate.find(query, Timesheet.class);
	}

    @Override
    public List<Timesheet> getTimesheetsForFileuploadProcess(UUID engagementId, String engagementName,
            long employeeNumber, String employeeName) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("engagement.id").is(engagementId.toString()).and("engagement.name").is(engagementName)
                .and("employee.id").is(employeeNumber).and("employee.name").is(employeeName));
        query.fields().include("id");
        return mongoTemplate.find(query, Timesheet.class);
    }
    
    @Override
    public void getTimesheetsForFileuploadProcessUpdate(UUID timesheetId) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("id").is(timesheetId));
        mongoTemplate.updateMulti(query, new Update().set("status", "Not Submitted"), Timesheet.class);
    }

	@Override
	public Long getMyTeamTimesheetsCountByStatus(String status, Long userId, boolean isApproval) {

		Query query = new Query();
		String userIdCriteria = "employee._id";
		if (isApproval) {
			userIdCriteria = "employee.reportingManagerId";
		}
		query = query.addCriteria(Criteria.where(userIdCriteria).is(userId).and(STATUS).is(status));

		return mongoTemplate.count(query, Timesheet.class);
	}

	@Override
	public Date getTimesheetEndDateByStatusAndEmployeeId(String status, Long employeeId, Date date) {

		Query query = new Query();
		query = query.addCriteria(Criteria.where("employee._id").is(employeeId).and(STATUS).is(status).and(START_DATE)
				.lte(date).and(END_DATE).gte(date));
		
		Timesheet timesheet = mongoTemplate.findOne(query, Timesheet.class);

		if (timesheet != null) {
			return timesheet.getEndDate();
		}
		
		return null;
	}
}
