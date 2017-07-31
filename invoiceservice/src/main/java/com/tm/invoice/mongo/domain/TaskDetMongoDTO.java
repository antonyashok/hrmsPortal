package com.tm.invoice.mongo.domain;

import java.util.List;

import com.tm.commonapi.web.core.data.BaseDTO;

public class TaskDetMongoDTO extends BaseDTO {

    private String taskName;
    private List<RateDetMongo> rateDetails;

    public String getTaskName() {
        return taskName;
    }

    public List<RateDetMongo> getRateDetails() {
        return rateDetails;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setRateDetails(List<RateDetMongo> rateDetails) {
        this.rateDetails = rateDetails;
    }


}
