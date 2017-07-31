package com.tm.timesheet.configuration.service.dto;

public class StatusDTO {

    private int code;
    private String status;

    public StatusDTO() {}

    public StatusDTO(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
