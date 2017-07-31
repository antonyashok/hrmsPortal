package com.tm.timesheet.timesheetview.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.timesheet.domain.TimesheetDetails;
import com.tm.timesheet.timesheetview.constants.TimesheetViewConstants;
import com.tm.timesheet.timesheetview.repository.TimesheetDetailsViewRepositoryCustom;

@Service
public class TimesheetDetailsViewRepositoryImpl implements TimesheetDetailsViewRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Inject
    public TimesheetDetailsViewRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<TimesheetDetails> getTimesheetComments(UUID id) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where(TimesheetViewConstants.TIMESHEETID).is(id));
        query = query.addCriteria(Criteria.where("comments").exists(true));
        query.fields().include(TimesheetViewConstants.TIMESHEETID).include("taskName")
                .include("timesheetDate").include("comments").include("hours");
        query.with(new Sort(Sort.Direction.ASC, "timesheetDate"));
        return mongoTemplate.find(query, TimesheetDetails.class);        
    }

    @Override
    public List<TimesheetDetails> findByTimesheetId(List<UUID> ids) {
        Query query = new Query();
        Criteria criteria = Criteria.where("timesheetId").in(
                ids);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, TimesheetDetails.class);
    }

    @Override
    public List<TimesheetDetails> findByTimesheetId(List<UUID> ids, String startDate,
            String endDate) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where(TimesheetViewConstants.TIMESHEETID).in(ids));
        if(null != startDate && null != endDate) {
            Date startDateUtil = CommonUtils.convertStringToDate(startDate);
            Date endDateUtil = CommonUtils.convertStringToDate(endDate);
            query = query.addCriteria(Criteria.where("timesheetDate").gte(startDateUtil).lte(endDateUtil));
        }
        query.with(new Sort(Sort.Direction.ASC, "timesheetDate"));
        return mongoTemplate.find(query, TimesheetDetails.class);   
    }


}
