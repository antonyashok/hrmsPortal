package com.tm.timesheet.timesheetview.repository.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tm.timesheet.timeoff.domain.Timeoff;
import com.tm.timesheet.timesheetview.repository.TimeoffViewRepositoryCustom;



@Service
@Transactional
public class TimeoffViewRepositoryImpl implements TimeoffViewRepositoryCustom {

    MongoTemplate mongoTemplate;


    private static final String PTO_REQUEST_DATE = "ptoRequestDetail.requestedDate";

    @Inject
    public TimeoffViewRepositoryImpl(@NotNull final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Timeoff> getTimeoffComments(Long employeeId, Date startDate, Date endDate) {
        Query query = new Query();
        query = query.addCriteria(Criteria.where("employeeId").is(employeeId));
        query = query.addCriteria(Criteria.where("ptoRequestDetail.comments").exists(true));
        if (startDate != null && endDate != null) {
            query = query.addCriteria(Criteria.where(PTO_REQUEST_DATE).gte(startDate).lte(endDate));
        }
        return mongoTemplate.find(query, Timeoff.class);
    }



}
