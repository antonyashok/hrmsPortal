package com.tm.invoice.mongo.repository;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.mongo.domain.ProfileImage;

@Repository
public interface ProfileImageRepository extends MongoRepository<ProfileImage, UUID> {

    @Query("{'employeeId':?0}")
    ProfileImage findProfileImageByEmployeeId(@Param(InvoiceConstants.EMPLOYEE_ID) Long employeeId);
}
