package com.tm.engagement.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tm.engagement.domain.WeekPlan;

@Repository
public interface WeekPlanRepository extends JpaRepository<WeekPlan, UUID> {

}
