/*******************************************************************************
 * <pre>
 *
 * File          : com.tm.timesheet.configuration.repository.ConfigurationGroupUserJobTitleRepository.java
 * Author        : Annamalai L
 * Date Created  : Jan 20, 2017
 * 
 * Warning: Unauthorized reproduction or distribution of this program,
 * or any portion of it, may result in severe civil and criminal penalties,
 * and will be prosecuted to the maximum extent possible under the law.s
 * 
 * </pre>
 *******************************************************************************/
package com.tm.timesheet.configuration.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.timesheet.configuration.domain.ConfigurationGroupUserJobTitle;

public interface ConfigurationGroupUserJobTitleRepository extends
        JpaRepository<ConfigurationGroupUserJobTitle, UUID> {
  
}