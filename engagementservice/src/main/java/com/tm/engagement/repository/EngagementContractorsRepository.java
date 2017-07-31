package com.tm.engagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tm.engagement.domain.EngagementContractors;
@Repository
public interface EngagementContractorsRepository extends JpaRepository<EngagementContractors, UUID> {

	List<EngagementContractors> findByEngagementId(UUID engagementId);

}
