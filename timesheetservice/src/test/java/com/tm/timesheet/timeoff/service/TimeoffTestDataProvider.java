package com.tm.timesheet.timeoff.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.testng.annotations.DataProvider;

import com.tm.timesheet.domain.AuditFields;
import com.tm.timesheet.domain.Timesheet;
import com.tm.timesheet.timeoff.domain.PtoAvailable;
import com.tm.timesheet.timeoff.domain.PtoAvailableView;
import com.tm.timesheet.timeoff.domain.Timeoff;
import com.tm.timesheet.timeoff.domain.TimeoffActivityLog;
import com.tm.timesheet.timeoff.domain.TimeoffRequestDetail;
import com.tm.timesheet.timeoff.service.dto.EmployeeProfileDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffRequestDetailDTO;
import com.tm.timesheet.timeoff.service.dto.TimeoffStatus;


public class TimeoffTestDataProvider {

	private static final String UI_DATE_FORMAT_REQUEST = "MM/dd/yyyy";
	private static final String INVALID_DATE_FORMAT = "Invalid date format";
	private static String timeoffId = "000181d4-2b11-8702-0035-111f1f15f771";
	private static String timesheedId = "000181d4-2b11-8702-0035-111f1f15f761";
	
	@DataProvider(name = "getAllTimeoffWithDate")
	public static Iterator<Object[]> getAllTimeoffWithDate() throws ParseException {

		String group = "test-group";

		List<Timeoff> configGroups = new ArrayList<>();
		TimeoffDTO timeoffDTO = prepareTimeoffDTOData();
		Timeoff timeoff = prepareTimeoffData(timeoffDTO);

		configGroups.add(timeoff);

		Pageable pageable = null;
		Page<Timeoff> pageConfigGroup = new PageImpl<>(configGroups, pageable, 5);
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { pageConfigGroup, pageable, group });
		return testData.iterator();
	}
	
	private static TimeoffDTO prepareTimeoffDTOData() {
		TimeoffDTO timeoffDTO = new TimeoffDTO();
		timeoffDTO.setPtoTypeName("Sick");
		timeoffDTO.setStartDate("2014/05/05");
		timeoffDTO.setEndDate("2017/05/05");
		timeoffDTO.setTotalHours("8");
		timeoffDTO.setStatus("SUBMITTED");
		timeoffDTO.setLastUpdated("2017/05/05");
		return timeoffDTO;
	}
	
	private static Timeoff prepareTimeoffData(TimeoffDTO timeoffDTO) throws ParseException {
		Timeoff timeoff = new Timeoff();
		AuditFields auditField = new AuditFields();
		auditField.setOn(convertStringToISODate(timeoffDTO.getLastUpdated()));
		timeoff.setPtoTypeName(timeoffDTO.getPtoTypeName());
		timeoff.setStartDate(convertStringToISODate(timeoffDTO.getStartDate()));
		timeoff.setEndDate(convertStringToISODate(timeoffDTO.getEndDate()));
		timeoff.setTotalHours(Double.parseDouble(timeoffDTO.getTotalHours()));
		timeoff.setStatus(timeoffDTO.getStatus());
		timeoff.setStartDate(new Date("01/04/2017"));
		timeoff.setEndDate(new Date("01/05/2017"));
		//timeoff.setUpdated(auditField);
		
		List<TimeoffRequestDetail> timeoffRequestDetailList=new ArrayList<TimeoffRequestDetail>();
		TimeoffRequestDetail timeoffRequestDetail=new TimeoffRequestDetail();
		timeoffRequestDetail.setRequestedDate(new Date());
		timeoffRequestDetail.setRequestedHours("8");
		timeoffRequestDetail.setStatus("SUBMITTED");
		timeoffRequestDetailList.add(timeoffRequestDetail);
		timeoff.setPtoRequestDetail(timeoffRequestDetailList);
		return timeoff;
	}
	
	public static Date convertStringToISODate(String date) throws ParseException {
		DateFormat df = new SimpleDateFormat(UI_DATE_FORMAT_REQUEST);
		Date convertUtilDate = null;
		if (StringUtils.isNotBlank(date)) {
			try {
				convertUtilDate = df.parse(date);
			} catch (ParseException e) {
				e.getMessage();
			}
		}
		return convertUtilDate;
		/*
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = formatter.parse(dates);
		return date;*/
	}
	
	@DataProvider(name = "getTimeoffStatusCountWithDateParam")
	public static Iterator<Object[]> getTimeoffStatusCountWithDateParam() throws ParseException {

		String group = "test-group";

		TimeoffStatus timeoffStatus = new TimeoffStatus();

		timeoffStatus.setApprovalCount((long) 1);
		timeoffStatus.setAwaitingApprovalCount((long) 1);
		timeoffStatus.setRejectedCount((long) 1);
		timeoffStatus.setTotalCount((long) 3);

		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { timeoffStatus, group });
		return testData.iterator();
	}
	
	private static TimeoffDTO prepareTimeoffPTODTOData() {
		TimeoffDTO timeoffDTO = new TimeoffDTO();
		timeoffDTO.setTimeoffId(UUID.fromString(timeoffId));
		timeoffDTO.setPtoTypeName("PTO");
		timeoffDTO.setStartDate("2014-05-05");
		timeoffDTO.setEndDate("2017-05-05");
		timeoffDTO.setTotalHours("8");
		timeoffDTO.setStatus("SUBMITTED");
		timeoffDTO.setLastUpdated("2017-05-05");
		return timeoffDTO;
	}
	
	@DataProvider(name = "createTimeOff")
	public static Iterator<Object[]> createTimeoff() throws ParseException {
		TimeoffDTO timeoff = new TimeoffDTO();
		timeoff.setEmployeeId("1");
		timeoff.setEmployeeName("Test employeename");
		timeoff.setReportingManagerId("1");
		timeoff.setReportingManagerName("Test reportmanager");
		timeoff.setPtoTypeId("c22e8658-e2f1-11e6-bf01-fe55135034f3");
		timeoff.setPtoTypeName("PTO");
		timeoff.setStartDate("02/10/2017");
		timeoff.setEndDate("02/13/2017");
		timeoff.setTotalHours("8");
		timeoff.setComments("commentsdata");
		timeoff.setTimeoffId(UUID.fromString(timeoffId));
		timeoff.setStatus("Awaiting Approval");
		timeoff.setTimesheetId("000181d4-2b11-8702-0035-511f1f15f772");
		
		AuditFields createdAuditFields = new AuditFields();
		createdAuditFields.setBy(1l);
		createdAuditFields.setEmail("testmail@mail.com");
		createdAuditFields.setName("testname");
		//timeoff.setCreated(createdAuditFields);
		List<TimeoffRequestDetailDTO> timeoffRequestDetailDTOs = new ArrayList<TimeoffRequestDetailDTO>();
		TimeoffRequestDetailDTO timeoffRequestDetailDTOFirst = new TimeoffRequestDetailDTO();
		timeoffRequestDetailDTOFirst.setRequestedHours("8");
		timeoffRequestDetailDTOFirst.setRequestedDate("02/10/2017");
		timeoffRequestDetailDTOFirst.setWeekOffStatus(false);
		timeoffRequestDetailDTOFirst.setStatus("Active");
		TimeoffRequestDetailDTO timeoffRequestDetailDTOSecond = new TimeoffRequestDetailDTO();
		timeoffRequestDetailDTOSecond.setRequestedHours("8");
		timeoffRequestDetailDTOSecond.setRequestedDate("02/13/2017");
		timeoffRequestDetailDTOSecond.setWeekOffStatus(true);
		timeoffRequestDetailDTOSecond.setStatus("Active");
		timeoffRequestDetailDTOs.add(timeoffRequestDetailDTOFirst);
		timeoffRequestDetailDTOs.add(timeoffRequestDetailDTOSecond);
		timeoff.setPtoRequestDetailDTO(timeoffRequestDetailDTOs);
		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] { timeoff,prepareTimeoffData(timeoff) });
		return testData.iterator();
	}
	
	
	@DataProvider(name = "getMyPtoTimeoffDetails")
    public static Iterator<Object[]> getMyPtoTimeoffDetails()  {
    	
    	/*EmployeeProfileDTO employeeProfileDTO=new EmployeeProfileDTO();
    	employeeProfileDTO.setEmployeeId(30l);
    	employeeProfileDTO.setFirstName("");
    	employeeProfileDTO.setLastName("");
    	employeeProfileDTO.setReportingManagerName("");*/
    	
    	List<PtoAvailable> ptoAvailableList=new ArrayList<PtoAvailable>();
    	PtoAvailable ptoAvailable=new PtoAvailable();
    	
    	ptoAvailable.setPtoAvailableId("1");
    	ptoAvailable.setEmployeeId(30l);
    	ptoAvailable.setAllotedHours(0d);
    	ptoAvailable.setAvailedHours(0d);
    	ptoAvailable.setRequestedHours(0d);
    	ptoAvailable.setApprovedHours(0d);
    	ptoAvailable.setDraftHours(0d);
    	ptoAvailableList.add(ptoAvailable);
    	
    	Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] { ptoAvailableList });
        return testData.iterator();
    }
    
    @DataProvider(name = "getMyTimeoffHolidays")
	public static Iterator<Object[]> getMyTimeoffHolidays() throws ParseException{

		String group = "test-group";

		List<Timeoff> configGroups = new ArrayList<>();
		TimeoffDTO timeoffDTO = prepareTimeoffDTOData();
		Timeoff timeoff = prepareTimeoffData(timeoffDTO);

		configGroups.add(timeoff);

		Set<Object[]> testData = new LinkedHashSet<Object[]>();
		testData.add(new Object[] {configGroups});
		return testData.iterator();
	}
	
	
	@DataProvider(name = "getMyTeamTimeoff")
    public static Iterator<Object[]> getMyTeamTimeoff() throws ParseException {

    	String group = "test-group";
        
        List<Timeoff> configGroups = new ArrayList<>();
        TimeoffDTO timeoffDTO =
                prepareTimeoffDTOData();
        Timeoff timeoff =
        		prepareTimeoffData(timeoffDTO);
        
        configGroups.add(timeoff);

        Pageable pageable = null;
        Page<Timeoff> pageConfigGroup = new PageImpl<>(configGroups, pageable, 5);
        Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {
                pageConfigGroup, pageable,group});
        return testData.iterator();
    }
    
    
    @DataProvider(name = "getMyTeamTimeoffStatusCount")
    public static Iterator<Object[]> getMyTeamTimeoffStatusCount() throws ParseException {

    	String group = "test-group";
    	
    	TimeoffStatus timeoffStatus=new TimeoffStatus(); 

    	timeoffStatus.setApprovalCount((long) 1);
    	timeoffStatus.setAwaitingApprovalCount((long) 1);
    	timeoffStatus.setRejectedCount((long) 1);
    	timeoffStatus.setTotalCount((long) 3);
    	
    	Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timeoffStatus,group});
        return testData.iterator();
    }
    
    
    @DataProvider(name = "GetMyTeamPtoTimeoffDetails")
    public static Iterator<Object[]> GetMyTeamPtoTimeoffDetails() throws ParseException  {
    	
    	/*EmployeeProfileDTO employeeProfileDTO=new EmployeeProfileDTO();
    	employeeProfileDTO.setEmployeeId(30l);
    	employeeProfileDTO.setFirstName("");
    	employeeProfileDTO.setLastName("");
    	employeeProfileDTO.setReportingManagerName("");*/
    	
    	 TimeoffDTO timeoffDTO =
                 prepareTimeoffDTOData();
         Timeoff timeoff =
         		prepareTimeoffData(timeoffDTO);
    	
    	List<PtoAvailable> ptoAvailableList=new ArrayList<PtoAvailable>();
    	PtoAvailable ptoAvailable=new PtoAvailable();
    	
    	ptoAvailable.setPtoAvailableId("1");
    	ptoAvailable.setEmployeeId(30l);
    	ptoAvailable.setAllotedHours(0d);
    	ptoAvailable.setAvailedHours(0d);
    	ptoAvailable.setRequestedHours(0d);
    	ptoAvailable.setApprovedHours(0d);
    	ptoAvailable.setDraftHours(0d);
    	ptoAvailableList.add(ptoAvailable);
    	
    	Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] { timeoff,ptoAvailableList});
        return testData.iterator();
    }
    
    
    @DataProvider(name = "getMyTeamTimeoffAvaliableList")
    public static Iterator<Object[]> getMyTeamTimeoffAvaliableList() throws ParseException {

    	String group = "test-group";
    	
    	List<PtoAvailableView> ptoAvaliables=new ArrayList<PtoAvailableView>();
    	PtoAvailableView ptoAvaliable=new PtoAvailableView(); 

    	ptoAvaliable.setPtoAvaliableId("asdas");
    	ptoAvaliable.setAllotedHours(Double.valueOf(0));
    	ptoAvaliable.setDraftHours(Double.valueOf(0));
    	ptoAvaliable.setRequestedHours(Double.valueOf(0));
    	ptoAvaliable.setApprovedHours(Double.valueOf(0));
    	ptoAvaliable.setAvailedHours(Double.valueOf(0));
    	
    	
    	ptoAvaliables.add(ptoAvaliable);
    	
    	 Pageable pageable = null;
    	 Page<PtoAvailableView> pageConfigGroup = new PageImpl<>(ptoAvaliables, pageable, 5);
    	
    	Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {pageConfigGroup,pageable});
        return testData.iterator();
    }
    
    
    @DataProvider(name = "getMyTimeoff")
    public static Iterator<Object[]> getMyTimeoff() throws ParseException {

    	String group = "test-group";
    	
    	 TimeoffDTO timeoffDTO =
                 prepareTimeoffDTOData();
         Timeoff timeoff =
         		prepareTimeoffData(timeoffDTO);
    	
    	List<TimeoffActivityLog> timeoffActivityLogs = getTimeoffActivityLogDTO();
    	Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timeoff,timeoffActivityLogs});
        return testData.iterator();
    }
    
    
    private static List<TimeoffActivityLog> getTimeoffActivityLogDTO(){
		TimeoffActivityLog timeoffActivityLog = new TimeoffActivityLog();
		timeoffActivityLog.setEmployeeName("All In All");
		timeoffActivityLog.setId(UUID.fromString("000181d4-2b11-8702-0035-111f1f3ccec9"));
		timeoffActivityLog.setTimeoffId(UUID.fromString("000181d4-2b11-8702-0035-111f1f15f771"));
		List<TimeoffActivityLog> timeoffActivityLogList = new ArrayList<>();
		timeoffActivityLogList.add(timeoffActivityLog);
		return timeoffActivityLogList;
	}
    
    @DataProvider(name = "deleteMyTimeoff")
    public static Iterator<Object[]> deleteMyTimeoff() throws ParseException {

    	String group = "test-group";
    	
    	 TimeoffDTO timeoffDTO =
                 prepareTimeoffPTODTOData();
         Timeoff timeoff =
         		prepareTimeoffData(timeoffDTO);
    	
    	Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timeoff});
        return testData.iterator();
    }
    
    @DataProvider(name = "updateTimeoffStatus")
    public static Iterator<Object[]> updateTimeoffStatus() throws ParseException {

    	 TimeoffDTO timeoffDTO =
                 prepareTimeoffPTODTOData();
    	 
    	 List<TimeoffDTO> timeoffDTOs=new ArrayList<TimeoffDTO>();
    	 timeoffDTOs.add(timeoffDTO);
    	 
    	 Timeoff timeoff =
          		prepareTimeoffData(timeoffDTO);
    	 
    	Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timeoffDTOs,timeoff});
        return testData.iterator();
    }
    

    @DataProvider(name = "getTimeoffDates")
    public static Iterator<Object[]> getTimeoffDates() throws ParseException  {
    	
    	String startDate = "02/01/2017";

		String endDate = "02/07/2017";
    	
		List<Timesheet> timesheetList=getTimeSheetList();
    	Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {startDate,endDate,timesheetList });
        return testData.iterator();
    }
    
    @DataProvider(name = "getMyTeamPtoTimeoffDetails")
    public static Iterator<Object[]> getMyTeamPtoTimeoffDetails() throws ParseException{
    	String employeeId = "101";
    	String engagementId = "000181d4-2b11-8702-0035-111f1f15f661";
		TimeoffDTO timeoffDTO = prepareTimeoffDTOData();
		Timeoff timeoff = prepareTimeoffData(timeoffDTO);
    	Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {employeeId, timeoff, engagementId});
        return testData.iterator();
    }
    
    @DataProvider(name = "createPTOAvailabel")
    public static Iterator<Object[]> createPTOAvailabel(){
    	Long employeeId = 101L;
    	String engagementId =  "000181d4-2b11-8702-0035-111f1f15f661";
    	PtoAvailable ptoAvailable =  getPtoAvailable().get(0);
    	Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {employeeId, engagementId, ptoAvailable});
        return testData.iterator();
    }
    
    @DataProvider(name = "removeAndUpdateTimesheetAppliedTimeoff")
    public static Iterator<Object[]> removeAndUpdateTimesheetAppliedTimeoff() throws ParseException{
    	TimeoffDTO timeoffDTO =  prepareTimeoffDTOData();
        Timeoff timeoff = prepareTimeoffData(timeoffDTO);
    	Set<Object[]> testData = new LinkedHashSet<Object[]>();
    	PtoAvailable ptoAvailable =  getPtoAvailable().get(0);
        testData.add(new Object[] { timeoff, ptoAvailable});
        return testData.iterator();
    }
    
    @DataProvider(name = "getProAccural")
    public static Iterator<Object[]> getProAccural(){
    	List<PtoAvailable> ptoAvailableList = getPtoAvailable();
    	Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] { ptoAvailableList});
        return testData.iterator();
    }
    
    @DataProvider(name = "updaeTimesheetTimeoffStatus")
    public static Iterator<Object[]> updaeTimesheetTimeoffStatus() throws ParseException{
    	TimeoffDTO timeoffDTO =  prepareTimeoffDTOData();
        Timeoff timeoff = prepareTimeoffData(timeoffDTO);
        EmployeeProfileDTO employeeProfileDTO = getEmployeeProfileDTO();
    	Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {employeeProfileDTO, timeoffDTO, timeoff});
        return testData.iterator();
    }
    
    @DataProvider(name = "testCreateTimeoff")
    public static Iterator<Object[]> testCreateTimeoff(){
    	List<Timesheet> timesheetList = getTimeSheetList();
    	Set<Object[]> testData = new LinkedHashSet<Object[]>();
        testData.add(new Object[] {timesheetList.get(0)});
        return testData.iterator();
    }
    
    
    private static List<Timesheet> getTimeSheetList(){
		Timesheet timesheet = new Timesheet();
		
		timesheet.setId(UUID.fromString(timesheedId));
		timesheet.setStartDate(new Date());
		timesheet.setEndDate(new Date());
		List<Timesheet> timesheetList = new ArrayList<>();
		timesheetList.add(timesheet);
		return timesheetList;
	}
    
	private static List<PtoAvailable> getPtoAvailable(){
		PtoAvailable ptoAvailable = new PtoAvailable();
		ptoAvailable.setAllotedHours(0.0);
		ptoAvailable.setApprovedHours(0.0);
		ptoAvailable.setAvailedHours(0.0);
		ptoAvailable.setBalanceHours(0.0);
		ptoAvailable.setDraftHours(0.0);
		ptoAvailable.setRequestedHours(0.0);
	 
		List<PtoAvailable> ptoAvailableList = new ArrayList<>();
		ptoAvailableList.add(ptoAvailable);
		return ptoAvailableList;
	}
	
	private static EmployeeProfileDTO getEmployeeProfileDTO(){
		EmployeeProfileDTO employeeProfileDTO = new EmployeeProfileDTO();
		employeeProfileDTO.setEmployeeId(101L);
		employeeProfileDTO.setFirstName("SMI");
		employeeProfileDTO.setLastName("SMI2"); 
		employeeProfileDTO.setReportingManagerId(102L);
		employeeProfileDTO.setReportingManagerName("Joburds");
		return employeeProfileDTO;
	}
}
