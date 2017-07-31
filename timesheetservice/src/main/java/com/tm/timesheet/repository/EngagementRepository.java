package com.tm.timesheet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.timesheet.domain.EngagementDetail;

public interface EngagementRepository extends JpaRepository<EngagementDetail, String> {

}
