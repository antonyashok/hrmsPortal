package com.tm.timesheet.timesheetview.repository.impl;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.timesheet.configuration.service.dto.OfficeLocationDTO;
import com.tm.timesheet.constants.TimesheetConstants;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.service.dto.TimesheetStatusCount;
import com.tm.timesheet.timesheetview.constants.TimesheetViewConstants;
import com.tm.timesheet.timesheetview.repository.TimesheetViewRepositoryCustom;

@Service
public class TimesheetViewRepositoryImpl implements TimesheetViewRepositoryCustom {

	private static final String MMM_YYYY = "MMM-yyyy";

	private static final Logger log = Logger.getLogger(TimesheetViewRepositoryImpl.class);
			
    private final MongoTemplate mongoTemplate;

    @Inject
    public TimesheetViewRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
	public List<TimesheetStatusCount> getStatusCount(Date startDate, Date endDate, List<Long> employeeIds,
			String roleId, String searchParam, String actorType, String employeeType, String timesheetType,String office) throws ParseException {
		Aggregation statusAggregation = null;
		if (timesheetType.equals(TimesheetConstants.TIMESHEET_VERIFICATION_TYPE)) {
			statusAggregation = Aggregation.newAggregation(
					match(prepareCriteria(startDate, endDate, employeeIds, searchParam, actorType, timesheetType)),
					group(TimesheetViewConstants.STATUS).count().as(TimesheetViewConstants.COUNT),
					project(TimesheetViewConstants.COUNT).and(TimesheetViewConstants.STATUS).previousOperation());

		}else if (timesheetType.equals(TimesheetConstants.TIMESHEET_PAYROLL_TYPE)) {
			Criteria criteria = prepareCriteria(startDate, endDate, employeeIds, searchParam, actorType, timesheetType);
			//criteria.andOperator(Criteria.where(TimesheetViewConstants.STATUS).in(TimesheetConstants.VERIFIED,TimesheetConstants.APPROVED,TimesheetConstants.COMPLETED));
			Criteria criteria1 = criteria.andOperator(Criteria.where(TimesheetViewConstants.STATUS).in(TimesheetConstants.VERIFIED,TimesheetConstants.APPROVED,TimesheetConstants.COMPLETED));
			if(StringUtils.isNotBlank(office)){
				//criteria.andOperator(Criteria.where(TimesheetViewConstants.EMPLOYEE_LOCATION_ID).is(office));
				criteria1 = Criteria.where(TimesheetViewConstants.EMPLOYEE_LOCATION_ID).is(office);
			}
			
			statusAggregation = Aggregation.newAggregation(
					match(criteria1),
					group(TimesheetViewConstants.STATUS).count().as(TimesheetViewConstants.COUNT),
					project(TimesheetViewConstants.COUNT).and(TimesheetViewConstants.STATUS).previousOperation());

		} else {

			statusAggregation = Aggregation.newAggregation(
					match(prepareCriteria(startDate, endDate, employeeIds, roleId, searchParam, actorType, employeeType,
							timesheetType)),
					group(TimesheetViewConstants.STATUS).count().as(TimesheetViewConstants.COUNT),
					project(TimesheetViewConstants.COUNT).and(TimesheetViewConstants.STATUS).previousOperation());
		}
		AggregationResults<TimesheetStatusCount> countResults = mongoTemplate.aggregate(statusAggregation,
				Timesheet.class, TimesheetStatusCount.class);
		return countResults.getMappedResults();
	}

	private Criteria prepareCriteria(Date startDate, Date endDate,
			List<Long> employeeIds, String roleId, String searchParam,
			String actorType, String employeeType, String timesheetType)
            throws ParseException {
    	
    	Criteria finalCriteria;
    	ResourceUtil.escapeSpecialCharacter(searchParam);
        if (null != startDate && null != endDate) {
            Criteria dateCriteria = prepareDateSearchCriteria(startDate, endDate);
			if (StringUtils.isNotBlank(searchParam) ) {
				searchCriteria(searchParam, dateCriteria,actorType,timesheetType);
				finalCriteria =  new Criteria().andOperator(Criteria.where(roleId).in(employeeIds), dateCriteria);
			}else if(timesheetType.equals(TimesheetViewConstants.TIMESHEET_PROXY_TYPE)){
				finalCriteria =  new Criteria().andOperator(Criteria.where(TimesheetViewConstants.LOOK_UP_TYPE).ne(TimesheetViewConstants.TIMER),Criteria.where(roleId).in(employeeIds), dateCriteria);
			}else{
				finalCriteria = dateCriteria;
			}
            

        } else {
        	
        	
        	Criteria criteria = Criteria.where(roleId).in(employeeIds);
        	String recruiterType = null;
        	if(employeeType.equalsIgnoreCase(TimesheetViewConstants.TYPE_RECURTER)) {
        		recruiterType = TimesheetViewConstants.TIMESHEET_RECRUTIER_TYPE;
        	}
        	
        	if (StringUtils.isNotBlank(searchParam) ) {
        		searchCriteria(searchParam, criteria,actorType,timesheetType);
        		finalCriteria = criteria;
			} else if(timesheetType.equals(TimesheetViewConstants.TIMESHEET_PROXY_TYPE)){
				finalCriteria =  new Criteria().andOperator(Criteria.where(TimesheetViewConstants.LOOK_UP_TYPE).ne(TimesheetViewConstants.TIMER),criteria);
			} else if (timesheetType
					.equalsIgnoreCase(TimesheetViewConstants.APPROVER)
					&& null != recruiterType && recruiterType
							.equalsIgnoreCase(TimesheetViewConstants.TIMESHEET_RECRUTIER_TYPE)) { 
				finalCriteria =  new Criteria().andOperator(Criteria.where(TimesheetViewConstants.EMPLOYEE_DOT_TYPE).in(TimesheetViewConstants.TYPE_RECURTER),criteria);
			} else if(!employeeType.equalsIgnoreCase(TimesheetViewConstants.TYPE_RECURTER)) {
				finalCriteria =  new Criteria().andOperator(Criteria.where(TimesheetViewConstants.EMPLOYEE_DOT_TYPE).nin(TimesheetViewConstants.TYPE_RECURTER), criteria);
			} else {
				finalCriteria = criteria;
			}
        }
        return finalCriteria;
    }
    
    private Criteria prepareCriteria(Date startDate, Date endDate, List<Long> employeeIds, String searchParam,String actorType,String timesheetType)
            throws ParseException {
    	ResourceUtil.escapeSpecialCharacter(searchParam);
        if (null != startDate && null != endDate) {
            Criteria dateCriteria = prepareDateSearchCriteria(startDate, endDate);
			if (StringUtils.isNotBlank(searchParam) ) {
				searchCriteria(searchParam, dateCriteria,actorType,timesheetType);
			}            
            return dateCriteria;

        } else {
        	Criteria dateCriteria = new Criteria();
        	if (StringUtils.isNotBlank(searchParam) ) {
				searchCriteria(searchParam, dateCriteria,actorType,timesheetType);
			}     
            return dateCriteria;
        }
    }

    private Criteria prepareDateSearchCriteria(Date startDate, Date endDate) {
        return new Criteria()
                .orOperator(
                        Criteria.where(TimesheetViewConstants.START_DATE).gte(startDate)
                                .andOperator(Criteria.where(TimesheetViewConstants.END_DATE)
                                        .lte(endDate)),
                        Criteria.where(TimesheetViewConstants.START_DATE).lte(endDate).andOperator(
                                Criteria.where(TimesheetViewConstants.END_DATE).gte(startDate)));       
    }

    @Override
    public Page<Timesheet> getAllTimesheet(List<Long> employeeIds, String status, Date startDate,
			Date endDate, Pageable pageable, String roleId, String searchParam, String actorType, String type) {
		Query query = new Query();
		Criteria criteria = null;
		
		List<String> ninStatus = new ArrayList<>();
		ninStatus.add(TimesheetViewConstants.DISPUTE);
		ninStatus.add(TimesheetViewConstants.NOT_VERIFIED);
		ninStatus.add(TimesheetViewConstants.VERIFIED);		
		
		if (StringUtils.isNotBlank(roleId)) {
			criteria = Criteria.where(roleId).in(employeeIds);
		}
		if (actorType.equals(TimesheetViewConstants.APPROVER)) {
			ninStatus.add(TimesheetViewConstants.OVERDUE);
			ninStatus.add(TimesheetViewConstants.NOT_SUBMITTED);
			
			if(StringUtils.isBlank(type)){
				query.addCriteria(Criteria.where(TimesheetViewConstants.EMPLOYEE_DOT_TYPE).nin(TimesheetViewConstants.TYPE_RECURTER));
			}
			
			if(StringUtils.isNotBlank(type) && type.equals(TimesheetConstants.RECRUITER) && status.equals(TimesheetViewConstants.STATUS_ALL)) {
				ninStatus = new ArrayList<>();
				ninStatus.add(TimesheetViewConstants.AWAITING_APPROVAL);
				ninStatus.add(TimesheetViewConstants.APPROVED);
				query = query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).in(ninStatus));
				//query.addCriteria(Criteria.where(TimesheetViewConstants.EMPLOYEE_DOT_TYPE).in(TimesheetViewConstants.TYPE_RECURTER));
			}/*else if(StringUtils.isNotBlank(type) && type.equals(TimesheetConstants.RECRUITER)){
				query.addCriteria(Criteria.where(TimesheetViewConstants.EMPLOYEE_DOT_TYPE).in(TimesheetViewConstants.TYPE_RECURTER));
			}*/
			else if (StringUtils.isNotBlank(status) && !status.equals(TimesheetViewConstants.STATUS_ALL)) {
				query = query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).is(status));
			} else {
				query = query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).nin(ninStatus));
			}
		} else if (actorType.equals(TimesheetViewConstants.SUBMITTER)) {
			if (StringUtils.isNotBlank(status) && !status.equals(TimesheetViewConstants.STATUS_ALL)) {
				query = query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).is(status));
			} else {
				query = query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).nin(ninStatus));
			}
		} else if (StringUtils.isNotBlank(status) && !status.equals(TimesheetViewConstants.STATUS_ALL)) {
			query = query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).is(status));
		}
		ResourceUtil.escapeSpecialCharacter(searchParam);
		if (null != startDate && null != endDate) {
			if (criteria != null) {
				query.addCriteria(criteria);
			}
			query = prepareDateSearchCriteria(startDate, endDate, searchParam, query,actorType,type);
		} else {
			if (StringUtils.isNotBlank(searchParam) && actorType.equals(TimesheetViewConstants.APPROVER)) {
				searchCriteriaTeamTimesheet(searchParam, criteria);
			}else if(StringUtils.isNotBlank(searchParam) && actorType.equals(TimesheetViewConstants.SUBMITTER)){
				searchCriteriaMyTimesheet(searchParam, criteria);
			}
			query.addCriteria(criteria);
		}

		query.fields().include(TimesheetViewConstants.STATUS).include(TimesheetViewConstants.EMPLOYEE_ID)
				.include(TimesheetViewConstants.EMPLOYEE_NAME).include(TimesheetViewConstants.ENGAGEMENT_NAME)
				.include(TimesheetViewConstants.UPDATED_ON).include(TimesheetViewConstants.ENGAGEMENT_ID)
				.include(TimesheetViewConstants.LEAVE_HOURS).include(TimesheetViewConstants.WORK_HOURS)
				.include(TimesheetViewConstants.TOTAL_HOURS).include(TimesheetViewConstants.EMPLOYEE_TYPE)
				.include(TimesheetViewConstants.START_DATE).include(TimesheetViewConstants.END_DATE)
				.include(TimesheetViewConstants.PTO_HOURS).include(TimesheetViewConstants.LOOK_UP_TYPE)
				.include(TimesheetViewConstants.SOURCE);

		if (actorType.equals(TimesheetViewConstants.APPROVER)) {
			query.fields().include(TimesheetViewConstants.ST_HOURS).include(TimesheetViewConstants.OT_HOURS)
					.include(TimesheetViewConstants.DT_HOURS);
		}

		query.with(pageable);
		List<Timesheet> myList = mongoTemplate.find(query, Timesheet.class);
		long totalSize =   mongoTemplate.count(query, Timesheet.class);;
		return new PageImpl<>(myList, pageable, totalSize);
	}
    
	@Override
	public Page<Timesheet> getAllPayrollTimesheet(Long employeeId, String status, Date startDate, Date endDate,String office,
			Pageable pageable, String searchParam,String actorType,String timesheetType) {
		Query query = new Query();

		List<String> inStatus = new ArrayList<>();
		if(status.equals(TimesheetConstants.STATUS_ALL)){
			inStatus.add(TimesheetViewConstants.APPROVED);
			inStatus.add(TimesheetViewConstants.VERIFIED);
			inStatus.add(TimesheetConstants.COMPLETED);
		}else{
			inStatus.add(status);
		}
		
		Criteria criteria = Criteria.where(TimesheetViewConstants.STATUS)
				.in(inStatus);
		if (StringUtils.isNotBlank(office)) {
			query.addCriteria(Criteria.where(TimesheetViewConstants.EMPLOYEE_LOCATION_ID).is(office));
		}
		ResourceUtil.escapeSpecialCharacter(searchParam);
		if (null != startDate && null != endDate) {
			query.addCriteria(criteria);
			query = prepareDateSearchCriteria(startDate, endDate, searchParam, query,actorType,timesheetType);
		} else {
			if (StringUtils.isNotBlank(searchParam)) {
				searchCriteriaPayrollTimesheet(searchParam, criteria);
			}
			query.addCriteria(criteria);
		}

		query.fields().include(TimesheetViewConstants.STATUS).include(TimesheetViewConstants.EMPLOYEE_ID)
				.include(TimesheetViewConstants.ENGAGEMENT_ID).include(TimesheetViewConstants.EMPLOYEE_LOCATION_NAME)
				.include(TimesheetViewConstants.EMPLOYEE_NAME).include(TimesheetViewConstants.EMPLOYEE_TYPE)
				.include(TimesheetViewConstants.ENGAGEMENT_NAME).include(TimesheetViewConstants.UPDATED_ON)
				.include(TimesheetViewConstants.APPROVED_ON).include(TimesheetViewConstants.START_DATE)
				.include(TimesheetViewConstants.END_DATE).include(TimesheetViewConstants.APPROVED_ON)
				.include(TimesheetViewConstants.SOURCE).include(TimesheetViewConstants.SUBMITTED_ON)
				.include(TimesheetViewConstants.WORK_HOURS).include(TimesheetViewConstants.TOTAL_HOURS)
				.include(TimesheetViewConstants.LOOK_UP_TYPE);

		query.with(pageable);
		List<Timesheet> myList = mongoTemplate.find(query, Timesheet.class);
		long totalSize = mongoTemplate.count(query, Timesheet.class);
		return new PageImpl<>(myList, pageable, totalSize);
	}
    
	private Query prepareDateSearchCriteria(Date startDate, Date endDate, String searchParam, Query query,
			String actorType, String timesheetType) {
		Criteria criteria = prepareDateSearchCriteria(startDate, endDate);
		if (StringUtils.isNotBlank(searchParam)) {
			searchCriteria(searchParam, criteria, actorType, timesheetType);
		}else{
			query.addCriteria(Criteria.where(TimesheetViewConstants.LOOK_UP_TYPE).ne(TimesheetViewConstants.TIMER));
		}
		query.addCriteria(criteria);
		return query;
	}

	private void searchCriteria(String searchParam, Criteria criteria,String actorType,String timesheetType) {
		
		if (StringUtils.equals(timesheetType, TimesheetViewConstants.SUBMITTER_TYPE)) {
			searchCriteriaMyTimesheet(searchParam, criteria);
		} else if (StringUtils.equals(timesheetType, TimesheetViewConstants.APPROVER_TYPE)) {
			searchCriteriaTeamTimesheet(searchParam, criteria);
		} else if (StringUtils.equals(timesheetType, TimesheetViewConstants.TIMESHEET_PROXY_TYPE)) {
			searchCriteriaProxyTimesheet(searchParam, criteria);
		} else if (StringUtils.equals(timesheetType, TimesheetViewConstants.TIMESHEET_VERIFICATION_TYPE)) {
			searchCriteriaVerificationTimesheet(searchParam, criteria);
		}
		/*
		Criteria periodCriteria = Criteria.where(TimesheetViewConstants.PERIOD_SEARCH)
				.in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria searchFieldCriteria = Criteria.where(TimesheetViewConstants.SEARCH_FIELD)
				.in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria statusCriteria = Criteria.where(TimesheetViewConstants.STATUS)
				.in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria enagegementnameCriteria = Criteria.where(TimesheetViewConstants.ENGAGEMENT_NAME)
				.in(ResourceUtil.prepareSearchParam(searchParam));
		if (CommonUtils.isDouble(searchParam)) {
			Criteria totalHoursCriteria = Criteria.where(TimesheetViewConstants.TOTAL_HOURS)
					.is(Double.parseDouble(searchParam));
			Criteria workHoursCriteria = Criteria.where(TimesheetViewConstants.WORK_HOURS)
					.is(Double.parseDouble(searchParam));
			Criteria leaveHoursCriteria = Criteria.where(TimesheetViewConstants.LEAVE_HOURS)
					.is(Double.parseDouble(searchParam));
			criteria.andOperator(new Criteria().orOperator(enagegementnameCriteria, searchFieldCriteria, statusCriteria,
					totalHoursCriteria, workHoursCriteria, leaveHoursCriteria));
		} else {
			criteria.andOperator(new Criteria().orOperator(enagegementnameCriteria, searchFieldCriteria, statusCriteria,
					periodCriteria));
		}*/
	}
	
	private void searchCriteriaMyTimesheet(String searchParam, Criteria criteria) {
		Criteria statusCriteria = Criteria.where(TimesheetViewConstants.STATUS).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria enagegementnameCriteria = Criteria.where(TimesheetViewConstants.ENGAGEMENT_NAME).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria periodCriteria = Criteria.where(TimesheetViewConstants.PERIOD_SEARCH).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria updatedCriteria = Criteria.where(TimesheetViewConstants.UPDATED_ON_SEARCH).in(ResourceUtil.prepareSearchParam(searchParam));
		if(CommonUtils.isDouble(searchParam)){
			Criteria totalHoursCriteria = Criteria.where(TimesheetViewConstants.TOTAL_HOURS).is(Double.parseDouble(searchParam));
			Criteria workHoursCriteria = Criteria.where(TimesheetViewConstants.WORK_HOURS).is(Double.parseDouble(searchParam));
			Criteria ptoHoursCriteria = Criteria.where(TimesheetViewConstants.PTO_HOURS).is(Double.parseDouble(searchParam));
			criteria.andOperator(new Criteria().orOperator(enagegementnameCriteria, statusCriteria,updatedCriteria,periodCriteria, totalHoursCriteria,workHoursCriteria,ptoHoursCriteria));
		}else{
			criteria.andOperator(new Criteria().orOperator(enagegementnameCriteria, statusCriteria,updatedCriteria,periodCriteria));
		}			
	}
	
	private void searchCriteriaTeamTimesheet(String searchParam, Criteria criteria) {
		Criteria statusCriteria = Criteria.where(TimesheetViewConstants.STATUS).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria enagegementnameCriteria = Criteria.where(TimesheetViewConstants.ENGAGEMENT_NAME).in(ResourceUtil.prepareSearchParam(searchParam));

		Criteria employeenameCriteria = Criteria.where(TimesheetViewConstants.EMPLOYEE_NAME).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria periodCriteria = Criteria.where(TimesheetViewConstants.PERIOD_SEARCH).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria updatedCriteria = Criteria.where(TimesheetViewConstants.UPDATED_ON_SEARCH).in(ResourceUtil.prepareSearchParam(searchParam));
		if(CommonUtils.isDouble(searchParam)){
			Criteria totalHoursCriteria = Criteria.where(TimesheetViewConstants.TOTAL_HOURS).is(Double.parseDouble(searchParam));
			Criteria stCriteria = Criteria.where(TimesheetViewConstants.ST_HOURS).is(Double.parseDouble(searchParam));
			Criteria otCriteria = Criteria.where(TimesheetViewConstants.OT_HOURS).is(Double.parseDouble(searchParam));
			Criteria dtCriteria = Criteria.where(TimesheetViewConstants.DT_HOURS).is(Double.parseDouble(searchParam));
			Criteria ptoHoursCriteria = Criteria.where(TimesheetViewConstants.PTO_HOURS).is(Double.parseDouble(searchParam));
			criteria.andOperator(new Criteria().orOperator(enagegementnameCriteria,employeenameCriteria, statusCriteria,updatedCriteria,periodCriteria, totalHoursCriteria,stCriteria,otCriteria,dtCriteria,ptoHoursCriteria));
		}else{
			criteria.andOperator(new Criteria().orOperator(enagegementnameCriteria,employeenameCriteria, statusCriteria,updatedCriteria,periodCriteria));
		}			
	}
	
	private void searchCriteriaProxyTimesheet(String searchParam, Criteria criteria) {
		Criteria statusCriteria = Criteria.where(TimesheetViewConstants.STATUS).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria employeenameCriteria = Criteria.where(TimesheetViewConstants.EMPLOYEE_NAME).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria enagegementnameCriteria = Criteria.where(TimesheetViewConstants.ENGAGEMENT_NAME).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria hirringManagerNameCriteria = Criteria.where(TimesheetViewConstants.CLIENT_MANAGER_NAME).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria recruiterNameCriteria = Criteria.where(TimesheetViewConstants.RECRUITER_NAME).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria approverNameCriteria = Criteria.where(TimesheetViewConstants.APPROVER_NAME).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria updatedCriteria = Criteria.where(TimesheetViewConstants.UPDATED_ON_SEARCH).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria employeeTypeCriteria = Criteria.where(TimesheetViewConstants.EMPLOYEE_TYPE).in(ResourceUtil.prepareSearchParam(searchParam));
		if(CommonUtils.isDouble(searchParam)){
			Criteria totalHoursCriteria = Criteria.where(TimesheetViewConstants.TOTAL_HOURS).is(Double.parseDouble(searchParam));
			Criteria stCriteria = Criteria.where(TimesheetViewConstants.ST_HOURS).is(Double.parseDouble(searchParam));
			Criteria otCriteria = Criteria.where(TimesheetViewConstants.OT_HOURS).is(Double.parseDouble(searchParam));
			Criteria dtCriteria = Criteria.where(TimesheetViewConstants.DT_HOURS).is(Double.parseDouble(searchParam));
			criteria.andOperator(Criteria.where(TimesheetViewConstants.LOOK_UP_TYPE).ne(TimesheetViewConstants.TIMER),new Criteria().orOperator(approverNameCriteria,recruiterNameCriteria,hirringManagerNameCriteria,enagegementnameCriteria,employeenameCriteria, statusCriteria,updatedCriteria, totalHoursCriteria,stCriteria,otCriteria,dtCriteria,employeeTypeCriteria));
		}else{
			criteria.andOperator(Criteria.where(TimesheetViewConstants.LOOK_UP_TYPE).ne(TimesheetViewConstants.TIMER),new Criteria().orOperator(approverNameCriteria,recruiterNameCriteria,hirringManagerNameCriteria,enagegementnameCriteria,employeenameCriteria, statusCriteria,updatedCriteria,employeeTypeCriteria));
		}			
	}
	
	private void searchCriteriaPayrollTimesheet(String searchParam, Criteria criteria) {
		Criteria employeenameCriteria = Criteria.where(TimesheetViewConstants.EMPLOYEE_NAME).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria updatedCriteria = Criteria.where(TimesheetViewConstants.UPDATED_ON_SEARCH).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria submittedCriteria = Criteria.where(TimesheetViewConstants.SUBMITTED_ON_SEARCH).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria approvedCriteria = Criteria.where(TimesheetViewConstants.APPROVED_ON_SEARCH).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria employeeTypeCriteria = Criteria.where(TimesheetViewConstants.EMPLOYEE_TYPE).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria locationNameCriteria = Criteria.where(TimesheetViewConstants.EMPLOYEE_LOCATION_NAME).in(ResourceUtil.prepareSearchParam(searchParam));
		if(CommonUtils.isDouble(searchParam)){
			Criteria totalHoursCriteria = Criteria.where(TimesheetViewConstants.TOTAL_HOURS).is(Double.parseDouble(searchParam));
			if(StringUtils.isNumeric(searchParam)){
				Criteria employeeIdCriteria = Criteria.where(TimesheetViewConstants.EMPLOYEE_ID).is(Long.valueOf(searchParam));
				criteria.andOperator(new Criteria().orOperator(employeeIdCriteria,locationNameCriteria,employeenameCriteria, updatedCriteria, totalHoursCriteria,submittedCriteria,approvedCriteria,employeeTypeCriteria));
			}else{
				criteria.andOperator(new Criteria().orOperator(locationNameCriteria,employeenameCriteria, updatedCriteria, totalHoursCriteria,submittedCriteria,approvedCriteria,employeeTypeCriteria));
			}
		}
		else{
			criteria.andOperator(new Criteria().orOperator(locationNameCriteria,employeenameCriteria, updatedCriteria,employeeTypeCriteria,submittedCriteria,approvedCriteria));
		}			
	}
	
	private void searchCriteriaVerificationTimesheet(String searchParam, Criteria criteria) {
		Criteria statusCriteria = Criteria.where(TimesheetViewConstants.STATUS).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria employeenameCriteria = Criteria.where(TimesheetViewConstants.EMPLOYEE_NAME).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria timesheetSourceCriteria = Criteria.where(TimesheetViewConstants.TIMESHEET_SOURCE).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria locationNameCriteria = Criteria.where(TimesheetViewConstants.EMPLOYEE_LOCATION_NAME).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria updatedCriteria = Criteria.where(TimesheetViewConstants.UPDATED_ON_SEARCH).in(ResourceUtil.prepareSearchParam(searchParam));
		Criteria submittedByCriteria = Criteria.where(TimesheetViewConstants.SUBMITTED_NAME).in(ResourceUtil.prepareSearchParam(searchParam));
		if(CommonUtils.isDouble(searchParam)){
			Criteria totalHoursCriteria = Criteria.where(TimesheetViewConstants.TOTAL_HOURS).is(Double.parseDouble(searchParam));
			criteria.andOperator(new Criteria().orOperator(timesheetSourceCriteria,submittedByCriteria,locationNameCriteria,employeenameCriteria, statusCriteria,updatedCriteria,totalHoursCriteria));
		}else{
			criteria.andOperator(new Criteria().orOperator(timesheetSourceCriteria,submittedByCriteria,locationNameCriteria,employeenameCriteria, statusCriteria,updatedCriteria));
		}			
	}
	
    @Override
	public List<Timesheet> getTimesheetsDetail(Long employeeId,UUID engagementId, Date startDate, Date endDate) {
		Query query = new Query();
		query = query.addCriteria(Criteria.where(TimesheetViewConstants.EMPLOYEE_ID).is(employeeId));
		
        if (null != engagementId) {
            query = query.addCriteria(Criteria.where(TimesheetViewConstants.ENGAGEMENT_ID).is(engagementId.toString()));
		}
		if (startDate != null && endDate != null) {
			query = query.addCriteria(new Criteria().orOperator(              
    				Criteria.where(TimesheetViewConstants.START_DATE).lte(startDate).andOperator(Criteria.where(TimesheetViewConstants.END_DATE).gte(startDate)),           
    				Criteria.where(TimesheetViewConstants.START_DATE).lte(endDate).andOperator(Criteria.where(TimesheetViewConstants.END_DATE).gte(endDate))));
		}
		return mongoTemplate.find(query, Timesheet.class);
	}
    
    @Override
	public List<Timesheet> getTimesheetsDetailTimeoff(Long employeeId,UUID engagementId, Date startDate, Date endDate) {
		Query query = new Query();
		List<String> ninStatus = new ArrayList<>();
		ninStatus.add(TimesheetViewConstants.APPROVED);
		query = query.addCriteria(Criteria.where(TimesheetViewConstants.EMPLOYEE_ID).is(employeeId));
		query = query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).in(ninStatus));
        if (null != engagementId) {
            query = query.addCriteria(Criteria.where(TimesheetViewConstants.ENGAGEMENT_ID).is(engagementId.toString()));
		}
		if (startDate != null && endDate != null) {
			query = query.addCriteria(new Criteria().orOperator(              
    				Criteria.where(TimesheetViewConstants.START_DATE).lte(startDate).andOperator(Criteria.where(TimesheetViewConstants.END_DATE).gte(startDate)),           
    				Criteria.where(TimesheetViewConstants.START_DATE).lte(endDate).andOperator(Criteria.where(TimesheetViewConstants.END_DATE).gte(endDate))));
		}
		return mongoTemplate.find(query, Timesheet.class);
	}  
    
    @Override
    public Timesheet getPreviousTimesheetForSubmitter(Long employeeId, String engagementId,
            Date startDate) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where(TimesheetViewConstants.EMPLOYEE_ID).is(employeeId));
        if (null != engagementId) {
            query = query.addCriteria(Criteria.where(TimesheetViewConstants.ENGAGEMENT_ID).is(engagementId));
        }
        query.with(new Sort(Sort.Direction.DESC,TimesheetViewConstants.START_DATE));
        query = query.addCriteria(Criteria.where(TimesheetViewConstants.START_DATE).lt(startDate));        
        query.limit(1);
        return mongoTemplate.findOne(query, Timesheet.class);        
    }

    @Override
    public Timesheet getNextTimesheetForSubmitter(Long employeeId, String engagementId,
            Date endDate) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where(TimesheetViewConstants.EMPLOYEE_ID).is(employeeId));
        if (null != engagementId) {
            query = query.addCriteria(Criteria.where(TimesheetViewConstants.ENGAGEMENT_ID).is(engagementId));
        }
        query = query.addCriteria(Criteria.where(TimesheetViewConstants.START_DATE).gt(endDate));
        query.with(new Sort(Sort.Direction.ASC,TimesheetViewConstants.START_DATE));
        query.limit(1);
        return mongoTemplate.findOne(query, Timesheet.class);     
    }

    @Override
    public Timesheet getPreviousTimesheetForApprover(Long employeeId, String engagementId,
            Date startDate) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where(TimesheetViewConstants.EMPLOYEE_ID).is(employeeId));
        if (null != engagementId) {
            query = query.addCriteria(Criteria.where(TimesheetViewConstants.ENGAGEMENT_ID).is(engagementId));
        }
        List<String> ninStatus = new ArrayList<>();
		ninStatus.add(TimesheetViewConstants.APPROVED);
		ninStatus.add(TimesheetViewConstants.REJECTED);
		ninStatus.add(TimesheetViewConstants.AWAITING_APPROVAL);
        query = query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).in(ninStatus));
        query = query.addCriteria(Criteria.where(TimesheetViewConstants.START_DATE).lt(startDate));
        query.with(new Sort(Sort.Direction.DESC,TimesheetViewConstants.START_DATE));
        query.limit(1);
        return mongoTemplate.findOne(query, Timesheet.class);        
    }

    @Override
    public Timesheet getNextTimesheetForApprover(Long employeeId, String engagementId,
            Date endDate) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where(TimesheetViewConstants.EMPLOYEE_ID).is(employeeId));
        if (null != engagementId) {
            query = query.addCriteria(Criteria.where(TimesheetViewConstants.ENGAGEMENT_ID).is(engagementId));
        }
        List<String> ninStatus = new ArrayList<>();
		ninStatus.add(TimesheetViewConstants.APPROVED);
		ninStatus.add(TimesheetViewConstants.REJECTED);
		ninStatus.add(TimesheetViewConstants.AWAITING_APPROVAL);
        query = query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).in(ninStatus));
        query = query.addCriteria(Criteria.where(TimesheetViewConstants.START_DATE).gt(endDate));
        query.with(new Sort(Sort.Direction.ASC,TimesheetViewConstants.START_DATE));
        query.limit(1);
        return mongoTemplate.findOne(query, Timesheet.class);     
    }
    
   @Override
    public Page<Timesheet> getAllTimesheetForAccountManager(Long employeeId, String status, Date startDate,
			Date endDate, Pageable pageable, String roleId, String searchParam, String actorType) {
		Query query = new Query();
		if (StringUtils.isNotBlank(roleId)) {
			query.addCriteria(Criteria.where(roleId).is(employeeId));
		}
		
		List<String> ninStatus = new ArrayList<>();
		ninStatus.add(TimesheetViewConstants.APPROVED);
		ninStatus.add(TimesheetViewConstants.AWAITING_APPROVAL);
		
		if (actorType.equals(TimesheetViewConstants.APPROVER)) {
			if (StringUtils.isNotBlank(status) && !status.equals(TimesheetViewConstants.STATUS_ALL)) {
				 query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).is(status));
			} else {
				  query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).nin(ninStatus));
			}
		} else if (StringUtils.isNotBlank(status) && status.equals(TimesheetViewConstants.STATUS_ALL)) {
			 query
					.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).in(TimesheetViewConstants.NOT_SUBMITTED,
							TimesheetViewConstants.OVERDUE, TimesheetViewConstants.VERIFIED,
							TimesheetViewConstants.NOT_VERIFIED, TimesheetViewConstants.DISPUTE));
		}
		ResourceUtil.escapeSpecialCharacter(searchParam);
		if (null != startDate && null != endDate) {
			query = prepareDateSearchCriteria(startDate, endDate, searchParam, query,actorType,actorType);
		} else {
			Criteria criteria = new Criteria();
			if (StringUtils.isNotBlank(searchParam)) {
				searchCriteriaProxyTimesheet(searchParam, criteria);
			}else{
				criteria = Criteria.where(TimesheetViewConstants.LOOK_UP_TYPE).ne(TimesheetViewConstants.TIMER);
			}
			query.addCriteria(criteria);
		}
 		query.fields().include(TimesheetViewConstants.STATUS).include(TimesheetViewConstants.EMPLOYEE_ID)
				.include(TimesheetViewConstants.EMPLOYEE_NAME).include(TimesheetViewConstants.ENGAGEMENT_NAME)
				.include(TimesheetViewConstants.UPDATED_ON).include(TimesheetViewConstants.ENGAGEMENT_ID)
				.include(TimesheetViewConstants.LEAVE_HOURS).include(TimesheetViewConstants.WORK_HOURS)
				.include(TimesheetViewConstants.TOTAL_HOURS).include(TimesheetViewConstants.EMPLOYEE_TYPE)
				.include(TimesheetViewConstants.START_DATE).include(TimesheetViewConstants.END_DATE)
				.include(TimesheetViewConstants.PTO_HOURS).include(TimesheetViewConstants.LOOK_UP_TYPE)
				.include(TimesheetViewConstants.CLIENT_MANAGER_NAME).include(TimesheetViewConstants.RECRUITER_NAME)
				.include(TimesheetViewConstants.REPORTING_MANAGER_NAME).include(TimesheetViewConstants.ST_HOURS)
				.include(TimesheetViewConstants.OT_HOURS).include(TimesheetViewConstants.DT_HOURS)
				.include(TimesheetViewConstants.SOURCE).include(TimesheetViewConstants.HIRING_MANAGER_NAME);

		if (actorType.equals(TimesheetViewConstants.APPROVER)) {
			query.fields().include(TimesheetViewConstants.ST_HOURS).include(TimesheetViewConstants.OT_HOURS)
					.include(TimesheetViewConstants.DT_HOURS);
		}

		query.with(pageable);
		List<Timesheet> myList = mongoTemplate.find(query, Timesheet.class);
		long totalSize = mongoTemplate.count(query, Timesheet.class);
		return new PageImpl<>(myList, pageable, totalSize);
	}

    @Override
	public Page<Timesheet> getAllTimesheetForAccountManager(Long employeeId, String status, Date startDate, Date endDate,String office,
			Pageable pageable, String searchParam,String actorType) {
		Query query = new Query();
		Criteria criteria = Criteria.where(TimesheetViewConstants.STATUS).is(status);
 
		if(StringUtils.isNotBlank(office)){
			query.addCriteria(Criteria.where(TimesheetViewConstants.EMPLOYEE_LOCATION_NAME).is(office));
		}
		if (null != startDate && null != endDate) {
			query.addCriteria(criteria);
			query = prepareDateSearchCriteria(startDate, endDate, searchParam, query,actorType,actorType);
		} else {
			if (StringUtils.isNotBlank(searchParam)) {
				searchCriteriaProxyTimesheet(searchParam, criteria);
			}
			query.addCriteria(criteria);
		}
		query.fields().include(TimesheetViewConstants.STATUS).include(TimesheetViewConstants.EMPLOYEE_ID).include(TimesheetViewConstants.ENGAGEMENT_ID)
				.include(TimesheetViewConstants.EMPLOYEE_LOCATION_NAME).include(TimesheetViewConstants.EMPLOYEE_NAME)
				.include(TimesheetViewConstants.EMPLOYEE_TYPE).include(TimesheetViewConstants.ENGAGEMENT_NAME)
				.include(TimesheetViewConstants.UPDATED_ON).include(TimesheetViewConstants.APPROVED_ON)
				.include(TimesheetViewConstants.START_DATE).include(TimesheetViewConstants.END_DATE)
				.include(TimesheetViewConstants.SUBMITTED_ON).include(TimesheetViewConstants.WORK_HOURS)
				.include(TimesheetViewConstants.TOTAL_HOURS).include(TimesheetViewConstants.LOOK_UP_TYPE)
				.include(TimesheetViewConstants.CLIENT_MANAGER_NAME).include(TimesheetViewConstants.RECRUITER_NAME)
				.include(TimesheetViewConstants.REPORTING_MANAGER_NAME).include(TimesheetViewConstants.ST_HOURS)
				.include(TimesheetViewConstants.OT_HOURS).include(TimesheetViewConstants.DT_HOURS)
				.include(TimesheetViewConstants.SOURCE);

		query.with(pageable);
		List<Timesheet> myList = mongoTemplate.find(query, Timesheet.class);
		long totalSize = mongoTemplate.count(query, Timesheet.class);
		return new PageImpl<>(myList, pageable, totalSize);
	}
    
    @Override
    public Page<Timesheet> getAllTimesheetForVerification(Long employeeId, String status, Date startDate,
			Date endDate, Pageable pageable, String roleId, String searchParam, String actorType) {
		Query query = new Query();
		Criteria criteria = null;
		if (StringUtils.isNotBlank(roleId)) {
			//criteria = Criteria.where(roleId).is(employeeId);
		}
		
		List<String> ninStatus = new ArrayList<>();
		ninStatus.add(TimesheetViewConstants.NOT_SUBMITTED);
		ninStatus.add(TimesheetViewConstants.OVERDUE);
		ninStatus.add(TimesheetViewConstants.REJECTED);
		ninStatus.add(TimesheetViewConstants.AWAITING_APPROVAL);
		ninStatus.add(TimesheetViewConstants.APPROVED);
		
		if (actorType.equals(TimesheetViewConstants.APPROVER)) {
			if (StringUtils.isNotBlank(status) && !status.equals(TimesheetViewConstants.STATUS_ALL)) {
				query = query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).is(status));
			} else {
				query = query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).nin(ninStatus));
			}
		} else if (StringUtils.isNotBlank(status) && status.equals(TimesheetViewConstants.STATUS_ALL)) {
			query = query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).in(TimesheetViewConstants.DISPUTE,
					TimesheetViewConstants.VERIFIED, TimesheetViewConstants.NOT_VERIFIED));
		}
		ResourceUtil.escapeSpecialCharacter(searchParam);
		if (null != startDate && null != endDate) {
			if (criteria != null) {
				query.addCriteria(criteria);
			}
			query = prepareDateSearchCriteria(startDate, endDate, searchParam, query,actorType,actorType);
		} else {
			if (StringUtils.isNotBlank(searchParam)) {
				searchCriteriaVerificationTimesheet(searchParam, criteria);
			}
			if (criteria != null) {
			query.addCriteria(criteria);
			}
		}

		query.fields().include(TimesheetViewConstants.STATUS).include(TimesheetViewConstants.EMPLOYEE_ID)
				.include(TimesheetViewConstants.EMPLOYEE_NAME).include(TimesheetViewConstants.ENGAGEMENT_NAME)
				.include(TimesheetViewConstants.UPDATED_ON).include(TimesheetViewConstants.ENGAGEMENT_ID)
				.include(TimesheetViewConstants.LEAVE_HOURS).include(TimesheetViewConstants.WORK_HOURS)
				.include(TimesheetViewConstants.TOTAL_HOURS).include(TimesheetViewConstants.EMPLOYEE_TYPE)
				.include(TimesheetViewConstants.START_DATE).include(TimesheetViewConstants.END_DATE)
				.include(TimesheetViewConstants.PTO_HOURS).include(TimesheetViewConstants.LOOK_UP_TYPE)
				.include(TimesheetViewConstants.CLIENT_MANAGER_NAME).include(TimesheetViewConstants.RECRUITER_NAME)
				.include(TimesheetViewConstants.REPORTING_MANAGER_NAME).include(TimesheetViewConstants.SOURCE)
				.include(TimesheetViewConstants.SUBMITTED_NAME).include(TimesheetViewConstants.UPDATED_ON)
				.include(TimesheetViewConstants.EMPLOYEE_LOCATION_NAME);

		if (actorType.equals(TimesheetViewConstants.APPROVER)) {
			query.fields().include(TimesheetViewConstants.ST_HOURS).include(TimesheetViewConstants.OT_HOURS)
					.include(TimesheetViewConstants.DT_HOURS);
		}

		query.with(pageable);
		List<Timesheet> myList = mongoTemplate.find(query, Timesheet.class);
		long totalSize = mongoTemplate.count(query, Timesheet.class);
		return new PageImpl<>(myList, pageable, totalSize);
	}
    
    
    @Override
	public Page<Timesheet> getAllTimesheetForVerification(Long employeeId, String status, Date startDate, Date endDate,String office,
			Pageable pageable, String searchParam,String actorType) {
		Query query = new Query();

		Criteria criteria = Criteria.where(TimesheetViewConstants.STATUS).is(status);

		if(StringUtils.isNotBlank(office)){
			query.addCriteria(Criteria.where(TimesheetViewConstants.EMPLOYEE_LOCATION_NAME).is(office));
		}
		ResourceUtil.escapeSpecialCharacter(searchParam);
		if (null != startDate && null != endDate) {
			query.addCriteria(criteria);
			query = prepareDateSearchCriteria(startDate, endDate, searchParam, query,actorType,actorType);
		} else {
			if (StringUtils.isNotBlank(searchParam)) {
				searchCriteriaVerificationTimesheet(searchParam, criteria);
			}
			query.addCriteria(criteria);
		}

		query.fields().include(TimesheetViewConstants.STATUS).include(TimesheetViewConstants.EMPLOYEE_ID).include(TimesheetViewConstants.ENGAGEMENT_ID)
				.include(TimesheetViewConstants.EMPLOYEE_LOCATION_NAME).include(TimesheetViewConstants.EMPLOYEE_NAME)
				.include(TimesheetViewConstants.EMPLOYEE_TYPE).include(TimesheetViewConstants.ENGAGEMENT_NAME)
				.include(TimesheetViewConstants.UPDATED_ON).include(TimesheetViewConstants.APPROVED_ON)
				.include(TimesheetViewConstants.START_DATE).include(TimesheetViewConstants.END_DATE)
				.include(TimesheetViewConstants.APPROVED_ON).include(TimesheetViewConstants.SOURCE)
				.include(TimesheetViewConstants.SUBMITTED_ON).include(TimesheetViewConstants.WORK_HOURS)
				.include(TimesheetViewConstants.TOTAL_HOURS).include(TimesheetViewConstants.LOOK_UP_TYPE)
				.include(TimesheetViewConstants.CLIENT_MANAGER_NAME).include(TimesheetViewConstants.RECRUITER_NAME)
				.include(TimesheetViewConstants.REPORTING_MANAGER_NAME).include(TimesheetViewConstants.SOURCE)
				.include(TimesheetViewConstants.SUBMITTED_NAME).include(TimesheetViewConstants.UPDATED_ON)
				.include(TimesheetViewConstants.EMPLOYEE_LOCATION_NAME);

		query.with(pageable);
		List<Timesheet> myList = mongoTemplate.find(query, Timesheet.class);
		long totalSize = mongoTemplate.count(query, Timesheet.class);
		return new PageImpl<>(myList, pageable, totalSize);
	}
    
    @Override
	public List<Timesheet> getTimesheetsDetailByStatus(Long employeeId, Date startDate, Date endDate,UUID engagementId) {
    	
    	List<String> ninStatus = new ArrayList<>();
		ninStatus.add(TimesheetViewConstants.AWAITING_APPROVAL);
		ninStatus.add(TimesheetViewConstants.APPROVED);
		
		Query query = new Query();
		query = query.addCriteria(Criteria.where(TimesheetViewConstants.EMPLOYEE_ID).is(employeeId));
		query = query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).in(ninStatus));
		if (null != engagementId) {
            query = query.addCriteria(Criteria.where(TimesheetViewConstants.ENGAGEMENT_ID).is(engagementId.toString()));
		}
		if (startDate != null && endDate != null) {
			query = query.addCriteria(new Criteria().orOperator(              
    				Criteria.where(TimesheetViewConstants.START_DATE).lte(startDate).andOperator(Criteria.where(TimesheetViewConstants.END_DATE).gte(startDate)),           
    				Criteria.where(TimesheetViewConstants.START_DATE).lte(endDate).andOperator(Criteria.where(TimesheetViewConstants.END_DATE).gte(endDate))));
		}
		return mongoTemplate.find(query, Timesheet.class);
	}

	@Override
	public List<OfficeLocationDTO> getOfficeLocations(Long employeeId,String roleId,String employeeType) {
		List<OfficeLocationDTO> list = null;
		/*List<OfficeLocationDTO> list = null;
		Criteria roleCriteria = Criteria.where(roleId).is(employeeId);		
		 Aggregation locationAggregation = Aggregation.newAggregation(
	                match(roleCriteria),
	                group("employee.locationId"),
	                project("employee.locationId").and("employee.locationName")
	                        .previousOperation());
	        AggregationResults<Timesheet> countResults = mongoTemplate
	                .aggregate(locationAggregation, Timesheet.class, Timesheet.class);*/
		return list;
	}

    @Override
    public List<Timesheet> getTimesheetsReports(List<Long> employeeIds, String status, String startDate,
            String endDate, UUID projectId, String month, String year) {
        Query query = new Query();
        if(CollectionUtils.isNotEmpty(employeeIds)) {
            query = query.addCriteria(Criteria.where(TimesheetViewConstants.EMPLOYEE_ID).in(employeeIds));
        }
        if(null != projectId) {
            query = query.addCriteria(Criteria.where(TimesheetViewConstants.ENGAGEMENT_ID).is(projectId.toString()));
        }
        
        if(StringUtils.isNotBlank(month) && StringUtils.isNotBlank(year)) {
            LocalDate startDateOfWeek = getStartDate(month, year);
            LocalDate endDateOfWeek = getEndDate(month, year);
            
			query = query.addCriteria(Criteria.where(TimesheetViewConstants.START_DATE).gte(startDateOfWeek)
					.andOperator(Criteria.where(TimesheetViewConstants.END_DATE).lte(endDateOfWeek)));
        }
        
        if(StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {          
                Date startDateUtil = CommonUtils.convertStringToDate(startDate);
                Date endDateUtil = CommonUtils.convertStringToDate(endDate);
                query = query.addCriteria(prepareDateSearchCriteria(startDateUtil, endDateUtil));         
        }
        if(StringUtils.isNotBlank(status)) {
            query = query.addCriteria(Criteria.where(TimesheetViewConstants.STATUS).is(status));
        }
        query.with(new Sort(Sort.Direction.ASC,TimesheetViewConstants.START_DATE));
        //query.fields().include(TimesheetViewConstants.ID).include(TimesheetViewConstants.EMPLOYEE_ID);
        return mongoTemplate.find(query, Timesheet.class);
    }
    
    public static LocalDate getStartDate(String month, String year) {
    	SimpleDateFormat formatter = new SimpleDateFormat(TimesheetViewRepositoryImpl.MMM_YYYY);
    	String dateInString = month+"-"+year;
    	LocalDate startDateOfWeek = null;
    	Date date = null;
		try {
			date = formatter.parse(dateInString);
            LocalDate startDay =  new java.sql.Date(date.getTime()).toLocalDate();
            startDateOfWeek = startDay;
            while(startDateOfWeek.getDayOfWeek() != DayOfWeek.SUNDAY) {
            	startDateOfWeek = startDateOfWeek.minusDays(1); 
            }
		} catch (ParseException e) {
			log.error("Error while getTimesheetsReports() :: "+e);
		}        	
    	return startDateOfWeek;
    }
    
    public static LocalDate getEndDate(String month, String year) {
    	SimpleDateFormat formatter = new SimpleDateFormat(TimesheetViewRepositoryImpl.MMM_YYYY);
    	String dateInString = month+"-"+year;
    	LocalDate endDateOfWeek = null;
    	Date date = null;
		try {
			date = formatter.parse(dateInString);
            LocalDate startDay =  new java.sql.Date(date.getTime()).toLocalDate();
            LocalDate startDateOfWeek = startDay;
            while(startDateOfWeek.getDayOfWeek() != DayOfWeek.SUNDAY) {
            	startDateOfWeek = startDateOfWeek.minusDays(1); 
            }
            LocalDate lastDayOfMonth = startDay.with(lastDayOfMonth());
            endDateOfWeek = lastDayOfMonth;
            while(endDateOfWeek.getDayOfWeek() != DayOfWeek.SATURDAY) {
            	endDateOfWeek = endDateOfWeek.plusDays(1); 
            }
		} catch (ParseException e) {
			log.error("Error while getTimesheetsReports() :: "+e);
		}        	
    	return endDateOfWeek;
    }
    
    public static String getStartDateFromLocalDate(String month, String year) throws ParseException {
    	DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	SimpleDateFormat formatter = new SimpleDateFormat(TimesheetViewRepositoryImpl.MMM_YYYY);
    	String dateInString = month+"-"+year;
    	Date date = formatter.parse(dateInString);
        LocalDate firstOfMonth =  new java.sql.Date(date.getTime()).toLocalDate();
    	return firstOfMonth.format(dtFormatter);
    }
    
    public static String getEndDateFromLocalDate(String month, String year) throws ParseException {
    	DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	SimpleDateFormat formatter = new SimpleDateFormat(TimesheetViewRepositoryImpl.MMM_YYYY);
    	String dateInString = month+"-"+year;
    	Date date = formatter.parse(dateInString);
    	LocalDate startDay =  new java.sql.Date(date.getTime()).toLocalDate();
    	LocalDate lastDayOfMonth = startDay.with(lastDayOfMonth());
    	return lastDayOfMonth.format(dtFormatter);
    }
}
