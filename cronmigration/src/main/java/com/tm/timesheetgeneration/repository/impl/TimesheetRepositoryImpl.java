package com.tm.timesheetgeneration.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.tm.timesheetgeneration.domain.Timesheet;
import com.tm.timesheetgeneration.domain.TimesheetDetails;
import com.tm.timesheetgeneration.repository.TimesheetRepositoryCustom;

@Service
public class TimesheetRepositoryImpl implements TimesheetRepositoryCustom {

	private static final String START_DATE = "startDate";
	private static final String END_DATE = "endDate";
	private static final String EMPLOYEE_ID = "employee.id";
	private static final String ENGAGEMENT_ID = "engagement.id";
	private final MongoTemplate mongoTemplate;

	@Inject
	public TimesheetRepositoryImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	// @Override
	// public List<Timesheet> getCreatedTimesheetsDetail(Date startDate, Date
	// endDate) {
	// Query query = new Query();
	// if (startDate != null && endDate != null) {
	// query = query.addCriteria(new Criteria().orOperator(
	// Criteria.where(START_DATE).lte(startDate).andOperator(Criteria.where(END_DATE).gte(startDate)),
	// Criteria.where(START_DATE).lte(endDate).andOperator(Criteria.where(END_DATE).gte(endDate))));
	// }
	// return mongoTemplate.find(query, Timesheet.class);
	// }

	@Override
	public List<Timesheet> getCreatedTimesheetsDetailByEmployeeIds(Date startDate, Date endDate,
			List<Long> employeeIds) {
		Query query = new Query();
		if (CollectionUtils.isNotEmpty(employeeIds)) {
			query = query.addCriteria(Criteria.where(EMPLOYEE_ID).in(employeeIds));
		}
		if (startDate != null && endDate != null) {
			// query = query.addCriteria(new Criteria().orOperator(
			// Criteria.where(START_DATE).lte(startDate).andOperator(Criteria.where(END_DATE).gte(startDate)),
			// Criteria.where(START_DATE).lte(endDate).andOperator(Criteria.where(END_DATE).gte(endDate))));
			query = query.addCriteria(prepareDateSearchCriteria(startDate, endDate));
		}
		return mongoTemplate.find(query, Timesheet.class);
	}

	private Criteria prepareDateSearchCriteria(Date startDate, Date endDate) {
		return new Criteria().orOperator(
				Criteria.where(START_DATE).gte(startDate).andOperator(Criteria.where(END_DATE).lte(endDate)),
				Criteria.where(START_DATE).lte(endDate).andOperator(Criteria.where(END_DATE).gte(startDate)));

	}

	@Override
	public List<Timesheet> getCreatedTimesheetsDetailByEmployeeIdAndEngagementId(Date startDate, Date endDate,
			Long employeeId, String engagementId) {
		Query query = new Query();
		if (employeeId > 0) {
			query = query.addCriteria(Criteria.where(EMPLOYEE_ID).is(employeeId));
		}
		if (Objects.nonNull(engagementId)) {
			query = query.addCriteria(Criteria.where(ENGAGEMENT_ID).is(engagementId));
		}
		if (startDate != null && endDate != null) {
			query = query.addCriteria(prepareDateSearchCriteria(startDate, endDate));
		}
		return mongoTemplate.find(query, Timesheet.class);
	}

	@Override
	public Timesheet getAllTimesheetsByEmpIdAndEngagIdAndLastBillDate(Long employeeId, String engagementId,
			Date lastBillDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("employee._id").is(employeeId).and("engagement._id").is(engagementId)
				.and(END_DATE).lte(lastBillDate));
		query.with(new Sort(Sort.Direction.DESC, END_DATE));
		query.limit(1);
		query.fields().include(END_DATE);
		return mongoTemplate.findOne(query, Timesheet.class);
	}
	
	@Override
	public List<Timesheet> getAllTimesheetsFromMaxTSEndDateToBillDateByEmpIdEngagId(Long employeeId,
			String engagementId, Date maxTimehseetEndDate, Date billDate) {
		Query query = new Query();
		Criteria dateSearchCriteria = Criteria.where(START_DATE).gte(maxTimehseetEndDate).andOperator(Criteria.where(END_DATE).lte(billDate));// prepareBillDateSearchCriteria(maxTimehseetEndDate, billDate);
		query.addCriteria(dateSearchCriteria);
		query.addCriteria(Criteria.where("employee._id").is(employeeId).and("engagement._id").is(engagementId));
				//.and("invoiceStatus").is(false);
				//.and(START_DATE).gte(maxTimehseetEndDate).lte(billDate));
		return mongoTemplate.find(query, Timesheet.class);
	}
	
	@Override
	public List<TimesheetDetails> getAllTimesheets(List<UUID> ids) {
		Query query = new Query();
		query.addCriteria(Criteria.where("timesheetId").in(ids).and("units").ne(null));
		return mongoTemplate.find(query, TimesheetDetails.class);
	}	
	 
}