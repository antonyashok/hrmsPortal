package com.tm.timesheet.configuration.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.timesheet.configuration.domain.NotificationAttribute;
import com.tm.timesheet.configuration.domain.ConfigurationGroup.UserGroupCategoryEnum;

public interface NotificationAttributeRepository extends JpaRepository<NotificationAttribute, UUID> {
    
    List<NotificationAttribute> findByUserGroupCategory(UserGroupCategoryEnum notificationCategory);
    
    List<NotificationAttribute> findByUserGroupCategoryAndDependantAttributeIsNull(
            UserGroupCategoryEnum userGroupCategory, Pageable pageable);
}
