package com.tm.commonapi.exception;

import java.io.IOException;

public class FileUploadException extends RuntimeException {

    private static final long serialVersionUID = -7457665314842947384L;

    public FileUploadException(String exceptionMessage) {
        super(exceptionMessage);
    }

    public FileUploadException(String timesheetId, IOException e) {
        super(timesheetId, e);
    }

}
