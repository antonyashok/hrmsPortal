package com.tm.timesheet.timeoff.repository.impl;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tm.commonapi.web.rest.util.ResourceUtil;
import com.tm.timesheet.timeoff.domain.Timeoff;
import com.tm.timesheet.timeoff.repository.TimeoffRepositoryCustom;

@Service
@Transactional
public class TimeoffRepositoryImpl implements TimeoffRepositoryCustom {

	private static final String EMPLOYEE_NAME = "employeeName";

	private static final String TOTAL_HOURS = "totalHours";

	private static final String ENGAGEMENT_NAME = "engagementName";

	private static final String PTO_TYPE_NAME = "ptoTypeName";

	private static final String END_DATE_STR = "endDateStr";

	private static final String START_DATE_STR = "startDateStr";

	private static final String LAST_UPDATED_DATE_STR = "lastUpdatedDateStr";
	
	private static final String EMPTY_UUID = "00000000-0000-0000-0000-000000000000";

	MongoTemplate mongoTemplate;

	private static final String EMPLOYEE_ID = "employeeId";
	private static final String STATUS = "status";
	private static final String PTO_REQUEST_DATE = "ptoRequestDetail.requestedDate";

	@Inject
	public TimeoffRepositoryImpl(@NotNull final MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Page<Timeoff> getMyTimeoffList(Long employeeId, String[] status,
			Date startDate, Date endDate, Pageable pageable, String searchParam) {
		Long timeoffStatusCnt;
		Query query = new Query();
		List<String> statuses = Arrays.asList(status);
		query = query.addCriteria(Criteria.where(EMPLOYEE_ID).is(employeeId)
				.and(STATUS).in(statuses));
		if (startDate != null && endDate != null) {
			query = query.addCriteria(Criteria.where(PTO_REQUEST_DATE)
					.gte(startDate).lte(endDate));
		}
		ResourceUtil.escapeSpecialCharacter(searchParam);
		if (StringUtils.isNotBlank(searchParam)) {
			searchCriteria(status, searchParam, query);
		}
		query.with(pageable);
		List<Timeoff> myList = mongoTemplate.find(query, Timeoff.class);
		timeoffStatusCnt = mongoTemplate.count(query, Timeoff.class);
		return new PageImpl<>(myList, pageable, timeoffStatusCnt);
	}

	private void searchCriteria(String[] status, String searchParam, Query query) {
		if (StringUtils.isNotBlank(searchParam)) {
			if (status.length > 1) {
				new Criteria();
				Criteria updatedDateCriteria = Criteria.where(
						TimeoffRepositoryImpl.LAST_UPDATED_DATE_STR).in(
						ResourceUtil.prepareSearchParam(searchParam));
				new Criteria();
				Criteria startdateCriteria = Criteria.where(
						TimeoffRepositoryImpl.START_DATE_STR).in(
						ResourceUtil.prepareSearchParam(searchParam));
				new Criteria();
				Criteria enddateCriteria = Criteria.where(
						TimeoffRepositoryImpl.END_DATE_STR).in(
						ResourceUtil.prepareSearchParam(searchParam));
				new Criteria();
				Criteria statusCriteria = Criteria.where(STATUS).in(
						ResourceUtil.prepareSearchParam(searchParam));
				new Criteria();
				Criteria ptoTypecriteria = Criteria.where(
						TimeoffRepositoryImpl.PTO_TYPE_NAME).in(
						ResourceUtil.prepareSearchParam(searchParam));
				new Criteria();
				Criteria engagementCriteria = Criteria.where(
						TimeoffRepositoryImpl.ENGAGEMENT_NAME).in(
						ResourceUtil.prepareSearchParam(searchParam));
				if (isDouble(searchParam)) {
					new Criteria();
					Criteria totalHoursCriteria = Criteria.where(
							TimeoffRepositoryImpl.TOTAL_HOURS).is(
							Double.parseDouble(searchParam));
					query.addCriteria(new Criteria().orOperator(statusCriteria,
							ptoTypecriteria, totalHoursCriteria,
							startdateCriteria, enddateCriteria,
							updatedDateCriteria, engagementCriteria));
				} else {
					query.addCriteria(new Criteria().orOperator(statusCriteria,
							ptoTypecriteria, startdateCriteria,
							enddateCriteria, updatedDateCriteria,
							engagementCriteria));
				}
			} else {
				new Criteria();
				Criteria updatedDateCriteria = Criteria.where(
						TimeoffRepositoryImpl.LAST_UPDATED_DATE_STR).in(
						ResourceUtil.prepareSearchParam(searchParam));
				new Criteria();
				Criteria startdateCriteria = Criteria.where(
						TimeoffRepositoryImpl.START_DATE_STR).in(
						ResourceUtil.prepareSearchParam(searchParam));
				new Criteria();
				Criteria enddateCriteria = Criteria.where(
						TimeoffRepositoryImpl.END_DATE_STR).in(
						ResourceUtil.prepareSearchParam(searchParam));
				new Criteria();
				Criteria ptoTypecriteria = Criteria.where(
						TimeoffRepositoryImpl.PTO_TYPE_NAME).in(
						ResourceUtil.prepareSearchParam(searchParam));
				new Criteria();
				Criteria engagementCriteria = Criteria.where(
						TimeoffRepositoryImpl.ENGAGEMENT_NAME).in(
						ResourceUtil.prepareSearchParam(searchParam));
				if (isDouble(searchParam)) {
					new Criteria();
					Criteria totalHoursCriteria = Criteria.where(
							TimeoffRepositoryImpl.TOTAL_HOURS).is(
							Double.parseDouble(searchParam));
					query.addCriteria(new Criteria().orOperator(
							ptoTypecriteria, totalHoursCriteria,
							startdateCriteria, enddateCriteria,
							updatedDateCriteria, engagementCriteria));
				} else {
					query.addCriteria(new Criteria().orOperator(
							ptoTypecriteria, startdateCriteria,
							enddateCriteria, updatedDateCriteria,
							engagementCriteria));
				}
			}
		}
	}

	private boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public Long getTimeoffStatusCountWithDate(String status, Long employeeId,
			Date startDate, Date endDate, String searchParam) {
		Query query = new Query();
		query = query.addCriteria(Criteria.where(EMPLOYEE_ID).is(employeeId)
				.and(STATUS).in(status));
		if (startDate != null && endDate != null) {
			query = query.addCriteria(Criteria.where(PTO_REQUEST_DATE)
					.gte(startDate).lte(endDate));
		}
		ResourceUtil.escapeSpecialCharacter(searchParam);
		if (StringUtils.isNotBlank(searchParam)) {
			String[] statusAllArray = new String[1];
			statusAllArray[0] = status;
			searchCriteria(statusAllArray, searchParam, query);
		}

		return mongoTemplate.count(query, Timeoff.class);
	}

	/*@Override
	public List<Timeoff> timeoffList(Long employeeId, String[] status,
			Date startDate, Date endDate, String engagementId) {
		Query query = new Query();
		List<String> statuses = Arrays.asList(status);
		query = query.addCriteria(Criteria.where(EMPLOYEE_ID).is(employeeId)
				.and(STATUS).in(statuses));
		if (startDate != null && endDate != null) {
			query = query.addCriteria(Criteria.where(PTO_REQUEST_DATE)
					.gte(startDate).lte(endDate));
		}
		return mongoTemplate.find(query, Timeoff.class);
	}*/
	
	
	@Override
	public List<Timeoff> timeoffList(Long employeeId, String[] status,
			Date startDate, Date endDate, String engagementId) {
		Query query = new Query();
		List<String> statuses = Arrays.asList(status);
		query = query.addCriteria(Criteria.where(EMPLOYEE_ID).is(employeeId)
				.and(STATUS).in(statuses));
	//	query = query.addCriteria(Criteria.where(EMPLOYEE_ID).is(employeeId));
		if (Objects.nonNull(engagementId) && !UUID.fromString(EMPTY_UUID).equals(engagementId) && StringUtils.isNotEmpty(engagementId)) {
			query = query.addCriteria(Criteria.where("engagementId").is(UUID.fromString(engagementId)));
			//query = query.addCriteria(new Criteria().andOperator(Criteria.where("engagementId").is(UUID.fromString(engagementId))));
		}
		//query = query.addCriteria(new Criteria().andOperator(Criteria.where(STATUS).in(statuses)));
		/*if (Objects.nonNull(engagementId) || !UUID.fromString(EMPTY_UUID).equals(engagementId)) {
			query = query.addCriteria(Criteria.where(engagementId).is(UUID.fromString(engagementId)));
		}*/
		if (startDate != null && endDate != null) {
			query = query.addCriteria(Criteria.where(PTO_REQUEST_DATE)
					.gte(startDate).lte(endDate));
		}
		return mongoTemplate.find(query, Timeoff.class);
	}

	@Override
	public Timeoff getTimeoffByRequestDate(Long employeeId, String[] status,
			Date requestDate, String ptoType) {
		Query query = new Query();
		List<String> statuses = Arrays.asList(status);
		query = query.addCriteria(Criteria.where(EMPLOYEE_ID).is(employeeId)
				.and(STATUS).in(statuses)
				.and(TimeoffRepositoryImpl.PTO_TYPE_NAME).in(ptoType)
				.and(PTO_REQUEST_DATE).is(requestDate));
		return mongoTemplate.findOne(query, Timeoff.class);
	}
	
	@Override
	public List<Timeoff> getMobileTimeoffByRequestDate(Long employeeId, String[] status,
			Date requestDate, String ptoType,String engagementId) {
		Query query = new Query();
		List<String> statuses = Arrays.asList(status);
		query = query.addCriteria(Criteria.where(EMPLOYEE_ID).is(employeeId)
				.and(STATUS).in(statuses)
				.and(TimeoffRepositoryImpl.PTO_TYPE_NAME).is(ptoType));
				//.and(PTO_REQUEST_DATE).is(requestDate));
		if (Objects.nonNull(engagementId) && !UUID.fromString(EMPTY_UUID).equals(engagementId) && StringUtils.isNotEmpty(engagementId)) {
			query = query.addCriteria(Criteria.where("engagementId").is(UUID.fromString(engagementId)));
		}
		return mongoTemplate.find(query, Timeoff.class);
	}

	public Page<Timeoff> getMyTeamTimeoff(Long reportingManagerId,
			String[] status, Date startDate, Date endDate, Pageable pageable,
			String searchParam) throws ParseException {
		Query query = new Query();
		List<String> statuses = Arrays.asList(status);
		query = query.addCriteria(Criteria.where("reportingManagerId")
				.is(reportingManagerId).and(STATUS).in(statuses));
		if (startDate != null && endDate != null) {
			query = query.addCriteria(Criteria.where(PTO_REQUEST_DATE)
					.gte(startDate).lte(endDate));
		}
		ResourceUtil.escapeSpecialCharacter(searchParam);
		if (StringUtils.isNotBlank(searchParam)) {
			searchTeamCriteria(status, searchParam, query);
		}
		query.with(pageable);
		List<Timeoff> myList = mongoTemplate.find(query, Timeoff.class);
		long count = mongoTemplate.count(query, Timeoff.class);

		return new PageImpl<>(myList, pageable, count);
	}

	private void searchTeamCriteria(String[] status, String searchParam,
			Query query) {
		if (status.length > 1) {
			new Criteria();
			Criteria employeeCriteria = Criteria.where(
					TimeoffRepositoryImpl.EMPLOYEE_NAME).in(
					ResourceUtil.prepareSearchParam(searchParam));
			new Criteria();
			Criteria updatedDateCriteria = Criteria.where(
					TimeoffRepositoryImpl.LAST_UPDATED_DATE_STR).in(
					ResourceUtil.prepareSearchParam(searchParam));
			new Criteria();
			Criteria startdateCriteria = Criteria.where(
					TimeoffRepositoryImpl.START_DATE_STR).in(
					ResourceUtil.prepareSearchParam(searchParam));
			new Criteria();
			Criteria enddateCriteria = Criteria.where(
					TimeoffRepositoryImpl.END_DATE_STR).in(
					ResourceUtil.prepareSearchParam(searchParam));
			new Criteria();
			Criteria statusCriteria = Criteria.where(STATUS).in(
					ResourceUtil.prepareSearchParam(searchParam));
			new Criteria();
			Criteria ptoTypecriteria = Criteria.where(
					TimeoffRepositoryImpl.PTO_TYPE_NAME).in(
					ResourceUtil.prepareSearchParam(searchParam));
			new Criteria();
			Criteria engagementcriteria = Criteria.where(
					TimeoffRepositoryImpl.ENGAGEMENT_NAME).in(
					ResourceUtil.prepareSearchParam(searchParam));
			if (isDouble(searchParam)) {
				new Criteria();
				Criteria totalHoursCriteria = Criteria.where(
						TimeoffRepositoryImpl.TOTAL_HOURS).is(
						Double.parseDouble(searchParam));
				query.addCriteria(new Criteria().orOperator(statusCriteria,
						ptoTypecriteria, totalHoursCriteria, startdateCriteria,
						enddateCriteria, updatedDateCriteria, employeeCriteria,
						engagementcriteria));
			} else {
				query.addCriteria(new Criteria().orOperator(statusCriteria,
						ptoTypecriteria, startdateCriteria, enddateCriteria,
						updatedDateCriteria, employeeCriteria,
						engagementcriteria));
			}
		} else {
			new Criteria();
			Criteria employeeCriteria = Criteria.where(
					TimeoffRepositoryImpl.EMPLOYEE_NAME).in(
					ResourceUtil.prepareSearchParam(searchParam));
			new Criteria();
			Criteria updatedDateCriteria = Criteria.where(
					TimeoffRepositoryImpl.LAST_UPDATED_DATE_STR).in(
					ResourceUtil.prepareSearchParam(searchParam));
			new Criteria();
			Criteria startdateCriteria = Criteria.where(
					TimeoffRepositoryImpl.START_DATE_STR).in(
					ResourceUtil.prepareSearchParam(searchParam));
			new Criteria();
			Criteria enddateCriteria = Criteria.where(
					TimeoffRepositoryImpl.END_DATE_STR).in(
					ResourceUtil.prepareSearchParam(searchParam));
			new Criteria();
			Criteria ptoTypecriteria = Criteria.where(
					TimeoffRepositoryImpl.PTO_TYPE_NAME).in(
					ResourceUtil.prepareSearchParam(searchParam));
			new Criteria();
			Criteria engagementcriteria = Criteria.where(
					TimeoffRepositoryImpl.ENGAGEMENT_NAME).in(
					ResourceUtil.prepareSearchParam(searchParam));
			if (isDouble(searchParam)) {
				new Criteria();
				Criteria totalHoursCriteria = Criteria.where(
						TimeoffRepositoryImpl.TOTAL_HOURS).is(
						Double.parseDouble(searchParam));
				query.addCriteria(new Criteria().orOperator(ptoTypecriteria,
						totalHoursCriteria, startdateCriteria, enddateCriteria,
						updatedDateCriteria, employeeCriteria,
						engagementcriteria));
			} else {
				query.addCriteria(new Criteria().orOperator(ptoTypecriteria,
						startdateCriteria, enddateCriteria,
						updatedDateCriteria, employeeCriteria,
						engagementcriteria));
			}
		}
	}

	public Long getMyTeamTimeoffStatusCountWithDate(String status,
			Long reportingManagerId, Date startDate, Date endDate,
			String searchParam) throws ParseException {
		Query query = new Query();
		query = query.addCriteria(Criteria.where("reportingManagerId")
				.is(reportingManagerId).and(STATUS).in(status));
		if (startDate != null && endDate != null) {
			query = query.addCriteria(Criteria.where(PTO_REQUEST_DATE)
					.gte(startDate).lte(endDate));
		}
		ResourceUtil.escapeSpecialCharacter(searchParam);
		if (StringUtils.isNotBlank(searchParam)) {
			String[] statusAllArray = new String[1];
			statusAllArray[0] = status;
			searchTeamCriteria(statusAllArray, searchParam, query);
		}
		return mongoTemplate.count(query, Timeoff.class);
	}

	public void updateTimeoffStatus(String status, List<UUID> ids) {
		Query query = new Query();
		query = query.addCriteria(Criteria.where("id").in(ids));
		mongoTemplate.updateMulti(query, new Update().set(STATUS, status),
				Timeoff.class);
	}

	public void updateTimeoffStatus(String lastUpdatedDate, String employeeId,
			UUID timeoffId, String status, String userName, Long userId) {
		Query query = new Query();

		query = query.addCriteria(Criteria.where("id").is(timeoffId));
		mongoTemplate.updateFirst(
				query,
				new Update()
						.set(STATUS, status)
						.set("updated.on", new Date())
						.set(TimeoffRepositoryImpl.LAST_UPDATED_DATE_STR,
								lastUpdatedDate).set("updated.name", userName)
						.set("updated.by", userId), Timeoff.class);

		mongoTemplate.upsert(new Query(Criteria.where("id").is(timeoffId)),
				new Update().set("ptoRequestDetail.0.status", status),
				Timeoff.class);
	}

	@Override
	public UUID fileUploadTimeoffDetailsUpdate(UUID timesheetId,
			UUID engagementId, long employeeId, String employeeName,
			Date requestedDate, double totalHours, String ptoName) {
		Query query = new Query();
		query = query.addCriteria(Criteria
				.where("ptoRequestDetail.timesheetId").is(timesheetId)
				.and("engagementId").is(engagementId).and(EMPLOYEE_ID)
				.is(employeeId).and(TimeoffRepositoryImpl.EMPLOYEE_NAME)
				.is(employeeName).and(PTO_REQUEST_DATE).is(requestedDate)
				.and(TimeoffRepositoryImpl.PTO_TYPE_NAME).is(ptoName));
		List<Timeoff> timeoffs = mongoTemplate.find(query, Timeoff.class);
		if (CollectionUtils.isNotEmpty(timeoffs)) {
			mongoTemplate.updateMulti(
					query,
					new Update().set("status", "Awaiting Approval").set(
							"ptoRequestDetail.requestedHours",
							(Double) totalHours), Timeoff.class);
			return timesheetId;
		} else {
			/*
			 * Timeoff timeoff = new Timeoff(); //
			 * timeoff.setTotalHours(totalHours);
			 * timeoff.setEmployeeId(employeeId);
			 * timeoff.setEmployeeName(employeeName);
			 * timeoff.setEngagementId(engagementId);
			 * timeoff.setPtoTypeName(ptoName); TimeoffRequestDetail
			 * timeoffRequestDetail = new TimeoffRequestDetail();
			 * timeoffRequestDetail.setTimesheetId(timesheetId);
			 * timeoffRequestDetail.setRequestedDate(requestedDate);
			 * timeoffRequestDetail.setRequestedHours(String.valueOf(totalHours)
			 * ); List<TimeoffRequestDetail> requestDetails = new ArrayList<>();
			 * requestDetails.add(timeoffRequestDetail);
			 * timeoff.setPtoRequestDetail(requestDetails);
			 * mongoTemplate.save(timeoff);
			 */
			return null;
		}
	}
	
	@Override
	public List<Timeoff> findByPtoTypeNameAndPtoRequestDetailTimesheetId(String ptoTypeName, UUID timesheetId) {
		Query query = new Query();
		query = query.addCriteria(Criteria.where(TimeoffRepositoryImpl.PTO_TYPE_NAME).is(ptoTypeName)
				.and("ptoRequestDetail.timesheetId").is(timesheetId));
		return mongoTemplate.find(query, Timeoff.class);
	}
	
	@Override
	public List<Timeoff> findByPtoRequestDetailTimesheetId(UUID timesheetId) {
		Query query = new Query();
		query = query.addCriteria(Criteria.where("ptoRequestDetail.timesheetId").is(timesheetId));
		return mongoTemplate.find(query, Timeoff.class);
	}
	
	
	
	
	

}