/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.common.repository.HolidaySettingsViewRepository.java
 * Author        : Annamalai 
 * Date Created  : Mar 14, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.common.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.common.domain.HolidaySettingsView;

public interface HolidaySettingsViewRepository extends
		JpaRepository<HolidaySettingsView, UUID> {

}