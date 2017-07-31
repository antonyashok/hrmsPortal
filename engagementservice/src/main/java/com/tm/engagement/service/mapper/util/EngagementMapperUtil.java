package com.tm.engagement.service.mapper.util;

import java.util.Date;

import com.tm.commonapi.web.rest.util.CommonUtils;

public class EngagementMapperUtil {

        public static Date validateDateFields(String date) {
            if (null != date) {
                return CommonUtils.convertStringToDate(date);
            } else {
                return null;
            }
        }

        public static String validateStringFields(Date date) {
            if (null == date) {
                return null;
            }
            return CommonUtils.getFormattedDate(date);
        }

        private EngagementMapperUtil() {

        }
    }

