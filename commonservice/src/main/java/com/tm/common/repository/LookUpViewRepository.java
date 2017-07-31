package com.tm.common.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.common.domain.LookUpView;

public interface LookUpViewRepository extends JpaRepository<LookUpView, UUID> {

    List<LookUpView> findByEntityName(String entityName);

}
