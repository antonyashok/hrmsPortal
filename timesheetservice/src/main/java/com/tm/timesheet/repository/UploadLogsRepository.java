package com.tm.timesheet.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tm.timesheet.domain.UploadLogs;

@Repository
public interface UploadLogsRepository extends MongoRepository<UploadLogs, UUID> {

	List<UploadLogs> findByOriginalFileName(String originalfilename);
}
