package com.tm.timesheetgeneration.reader;

import static org.mockito.Mockito.when;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tm.common.domain.ContractorEmployeeEngagementView;
import com.tm.common.repository.ContractorEmployeeEngagementViewRepository;
import com.tm.timesheetgeneration.reader.ContractorReader;
import com.tm.timesheetgeneration.service.dto.ContractorEngagementBatchDTO;
import com.tm.util.DateUtil;

public class ContractorReaderTest{

	@Mock
	private ContractorEmployeeEngagementViewRepository contractorEmployeeEnagementViewRepository;
	
	@InjectMocks
	ContractorReader conRead = new ContractorReader();
	
	ItemReader<ContractorEngagementBatchDTO> contractorItemReader;
	
	@BeforeMethod
	private void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		this.contractorEmployeeEnagementViewRepository = Mockito.mock(ContractorEmployeeEngagementViewRepository.class);
		contractorItemReader = new ContractorReader();
		conRead.setEmployeeContractorEnagementViewRepository(contractorEmployeeEnagementViewRepository);

	}
	
	@Test
	private void contractorReader() throws UnexpectedInputException, ParseException, NonTransientResourceException, Exception{
	
		Date liveDate = new Date();
		Calendar scalendar = Calendar.getInstance(); 
		scalendar.setTime(liveDate); 
		scalendar.add(Calendar.DATE, -9);
		liveDate = scalendar.getTime();
		
		conRead.fromId = 0;
		conRead.toId = 10;
		conRead.applicationLiveDate = liveDate;
		conRead.weekEndDate = DateUtil.convertStringToISODate("05/20/2017");
		conRead.weekStartDate = DateUtil.convertStringToISODate("05/14/2017");
		conRead.weekStartDay = "Sun";
		conRead.weekEndDay = "Sat";
		conRead.startDayofWeek = DayOfWeek.SUNDAY;
		
		Pageable pageable = new PageRequest(0, 10);
		
		ContractorEmployeeEngagementView contEmpEngView = new ContractorEmployeeEngagementView();
		contEmpEngView.setClientManagerId(12454L);
		
		List<ContractorEmployeeEngagementView> conEmpEngViewList = new ArrayList<>();
		conEmpEngViewList.add(contEmpEngView);
		Page<ContractorEmployeeEngagementView> poInvoiceSetupDetailsView = new PageImpl<>(conEmpEngViewList, pageable, conEmpEngViewList.size()+1);
		ContractorEmployeeEngagementView conEmpView = new ContractorEmployeeEngagementView();
		when(contractorEmployeeEnagementViewRepository.getPageableContractorEngagementByStartDay(pageable, ContractorEmployeeEngagementView.day.valueOf(conRead.weekStartDay),
				DateUtil.convertStringToDate("05/14/2017"),
				DateUtil.convertStringToDate("05/20/2017"))).thenReturn(poInvoiceSetupDetailsView);
		//conRead.read();
	}
}
