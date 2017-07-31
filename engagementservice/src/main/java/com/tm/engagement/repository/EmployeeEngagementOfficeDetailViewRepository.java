package com.tm.engagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.engagement.domain.EmployeeEngagementOfficeDetailView;

public interface EmployeeEngagementOfficeDetailViewRepository
        extends JpaRepository<EmployeeEngagementOfficeDetailView, Long> {

    List<EmployeeEngagementOfficeDetailView> findByEngagementId(UUID engagementId);

}
