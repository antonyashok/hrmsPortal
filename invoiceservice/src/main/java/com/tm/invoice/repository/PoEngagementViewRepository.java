package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.invoice.domain.PoEngagementView;

public interface PoEngagementViewRepository extends JpaRepository<PoEngagementView, UUID> {

    List<PoEngagementView> findByCustomerId(Long customerId);

}
