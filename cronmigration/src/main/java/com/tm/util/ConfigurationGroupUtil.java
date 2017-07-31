package com.tm.util;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.tm.timesheet.configuration.domain.ConfigurationGroup;

public class ConfigurationGroupUtil {

	private ConfigurationGroupUtil() {}
	
    public static ConfigurationGroup getLatestRecuiterTsConfig(
            List<ConfigurationGroup> recTSConfigListForGroup,
            List<ConfigurationGroup> recTSConfigListForOfficeId) {

    	ConfigurationGroup latestRecruiterTsConfig = null;

        if (CollectionUtils.isNotEmpty(recTSConfigListForOfficeId)
                && CollectionUtils.isNotEmpty(recTSConfigListForGroup)) {
            Date effectiveDateForOfficeId = recTSConfigListForOfficeId.get(0).getEffectiveEndDate();
            Date effectiveDateForGroupConf = recTSConfigListForGroup.get(0).getEffectiveEndDate();

            if (effectiveDateForGroupConf.compareTo(effectiveDateForOfficeId) > 0) {
                latestRecruiterTsConfig = recTSConfigListForGroup.get(0);
            } else {
                latestRecruiterTsConfig = recTSConfigListForOfficeId.get(0);
            }

        } else {
			if (CollectionUtils.isNotEmpty(recTSConfigListForOfficeId)) {
				latestRecruiterTsConfig = recTSConfigListForOfficeId.get(0);
			} else if (CollectionUtils.isNotEmpty(recTSConfigListForGroup)) {
				latestRecruiterTsConfig = recTSConfigListForGroup.get(0);
			}
		}
        return latestRecruiterTsConfig;
    }

}
