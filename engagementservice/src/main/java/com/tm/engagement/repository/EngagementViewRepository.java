package com.tm.engagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tm.engagement.domain.EngagementView;
@Repository
public interface EngagementViewRepository extends JpaRepository<EngagementView, UUID> {

	EngagementView findByEngagementId(UUID engagementId);

    List<EngagementView> findByCustomerId(Long customerId); 
}
