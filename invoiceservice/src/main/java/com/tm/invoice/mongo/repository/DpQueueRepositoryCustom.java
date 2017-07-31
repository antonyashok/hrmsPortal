package com.tm.invoice.mongo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.invoice.mongo.domain.DpQueue;

public interface DpQueueRepositoryCustom {
	
	public Page<DpQueue> getDpQueues(Long billToClienttId, String status, Pageable pageable);
	
	public List<DpQueue> getDpQueues(List<UUID> dpQueueIds);
	
	public List<DpQueue> getNotGenereatedDpQueues(Long billToClientId);

}
