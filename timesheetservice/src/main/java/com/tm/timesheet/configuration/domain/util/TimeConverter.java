package com.tm.timesheet.configuration.domain.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.apache.commons.lang3.StringUtils;

@Converter
public class TimeConverter implements AttributeConverter<String, Time> {
    private SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");

    private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    public Time convertToDatabaseColumn(String attribute) {
        try {
                return StringUtils.isBlank(attribute) ? null : new Time(formatter.parse(attribute).getTime());
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public String convertToEntityAttribute(Time dbData) {
        try {
            return Objects.nonNull(dbData) ? formatter.format(dateFormat.parse(dbData.toString()))
                    : "0";
        } catch (ParseException e) {
            return null;
        }
    }
}
