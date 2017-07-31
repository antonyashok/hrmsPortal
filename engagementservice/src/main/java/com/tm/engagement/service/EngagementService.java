package com.tm.engagement.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.tm.engagement.domain.Engagement;
import com.tm.engagement.domain.EngagementHolidaysView;
import com.tm.engagement.service.dto.EngagementDTO;
import com.tm.engagement.service.dto.EngagementViewDTO;
import com.tm.engagement.service.dto.InvoiceAttachmentsDTO;
import com.tm.engagement.service.dto.OfficeLocationDTO;
import com.tm.engagement.service.dto.ReportingMgrEngmtViewDTO;
import com.tm.engagement.service.resources.HolidayResourceDTO;

public interface EngagementService {

	Page<EngagementViewDTO> getEngagementList(Pageable pageable);

	Engagement createEngagement(EngagementDTO engagementDTO) throws ParseException;

	EngagementDTO getEngagementDetails(UUID engagementId);

	List<OfficeLocationDTO> getConfiguredOfficeLocations(UUID engagementId);
	
	List<EngagementDTO> getEngagements();
	
	Page<EngagementHolidaysView> getEngagementHolidayList(Pageable pageable);
	
	HolidayResourceDTO createEngagementHoliday(HolidayResourceDTO engagementHolidays) throws ParseException;
	
	HolidayResourceDTO getEngagementHolidayDetails(UUID engagementHolidayId);

    int updateEngagement(BigDecimal initialAmount, BigDecimal totalAmount, BigDecimal balanceAmount,
            UUID engagementId, UUID poId, String poNumber, String type,String engmntDate, String poIssueDate) throws ParseException;
    
	List<ReportingMgrEngmtViewDTO> getReportMgrengagements(String activeFlag);
	
	void updateFileDetails(String sourceReferenceId,
			List<InvoiceAttachmentsDTO> list);
	
}