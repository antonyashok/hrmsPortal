package com.tm.invoice.service;

import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.exception.BusinessException;
import com.tm.commonapi.exception.RecordNotFoundException;
import com.tm.invoice.constants.InvoiceSetupConstants;
import com.tm.invoice.domain.GlobalInvoiceSetup;
import com.tm.invoice.domain.InvoiceSetup;
import com.tm.invoice.domain.InvoiceSetupView;
import com.tm.invoice.dto.EmployeeProfileDTO;
import com.tm.invoice.dto.InvoiceSetupDTO;
import com.tm.invoice.enums.GlobalInvoiceFlag;
import com.tm.invoice.mongo.domain.InvoiceSetupActivitiesLog;
import com.tm.invoice.mongo.repository.InvoiceSetupActivitiesLogRepository;
import com.tm.invoice.repository.GlobalInvoiceRepository;
import com.tm.invoice.repository.InvoiceSetupNoteRepository;
import com.tm.invoice.repository.InvoiceSetupOptionRepository;
import com.tm.invoice.repository.InvoiceSetupRepository;
import com.tm.invoice.repository.InvoiceSetupViewRepository;
import com.tm.invoice.repository.PoContractorsViewRepository;
import com.tm.invoice.service.impl.InvoiceSetupServiceImpl;

public class InvoiceSetupServiceTest {

    private InvoiceSetupActivitiesLogRepository invoiceSetupActivitiesLogRepository;

    @Mock
    private InvoiceSetupRepository invoiceSetupRepository;

    @Mock
    private InvoiceSetupNoteRepository invoiceSetupNoteRepository;

    @Mock
    private InvoiceService invoiceService;
    
    @Mock
    private InvoiceSetupOptionRepository invoiceSetupOptionRepository;
    
    @Mock
    private PoContractorsViewRepository poContractorsViewRepository;
    
    @InjectMocks
    private InvoiceSetupServiceImpl invoiceSetupServiceImpl;
    
    @Mock
    private InvoiceSetupViewRepository invoiceSetupViewRepository;

    @Mock
    private GlobalInvoiceRepository globalInvoiceRepository;
  

    @BeforeMethod
    @BeforeTest
    public void configurationInvoiceSetupServiceTest() throws Exception {
        this.invoiceSetupActivitiesLogRepository = Mockito.mock(InvoiceSetupActivitiesLogRepository.class);
        this.invoiceSetupRepository = Mockito.mock(InvoiceSetupRepository.class);
        this.invoiceSetupNoteRepository = Mockito.mock(InvoiceSetupNoteRepository.class);
        this.invoiceService = Mockito.mock(InvoiceService.class);
        this.invoiceSetupOptionRepository = Mockito.mock(InvoiceSetupOptionRepository.class);
        this.poContractorsViewRepository= Mockito.mock(PoContractorsViewRepository.class);
        this.invoiceSetupViewRepository=Mockito.mock(InvoiceSetupViewRepository.class);
        this.globalInvoiceRepository=Mockito.mock(GlobalInvoiceRepository.class);
        invoiceSetupServiceImpl = new InvoiceSetupServiceImpl(invoiceSetupActivitiesLogRepository,
                invoiceSetupRepository,
                invoiceSetupNoteRepository,
                invoiceService,
                invoiceSetupOptionRepository,
                invoiceSetupViewRepository,
                globalInvoiceRepository);
    }
    
    @Test
	public void getAllExistingActiveSetups(){
    	
    	InvoiceSetupView invoiceSetupView = Mockito.mock(InvoiceSetupView.class);
    	List<InvoiceSetupView> invoiceSetupViewList = Arrays.asList(invoiceSetupView);
    	when(invoiceSetupViewRepository.getAllExistingActiveSetups(2l, "Global")).thenReturn(invoiceSetupViewList);
    	invoiceSetupServiceImpl.getAllExistingActiveSetups(2l);
    }
    
    @Test
   	public void populateExistingSetupDetails(){
    	UUID id = UUID.randomUUID();
    	GlobalInvoiceSetup globalInvoiceSetup = new GlobalInvoiceSetup();
    	globalInvoiceSetup.setActiveFlag(GlobalInvoiceFlag.Y);
    	globalInvoiceSetup.setInvoiceType("invoicetype");
    	globalInvoiceSetup.setInvTemplateId(2l);
    	globalInvoiceSetup.setNotesToDisplay("notes");
    	globalInvoiceSetup.setBillCycleFrequencyDay("day");
    	globalInvoiceSetup.setBillCycleFrequency("frequency");
    	globalInvoiceSetup.setBillCycleFrequencyType("type");
   	
    	when(globalInvoiceRepository.findByGlobalInvoiceSetupById(id)).thenReturn(globalInvoiceSetup);
    	invoiceSetupServiceImpl.populateExistingSetupDetails(id,"Global",2l);
    	
    	InvoiceSetup invoiceSetup = new InvoiceSetup();
    	when(invoiceSetupRepository.findByInvoiceSetupId(id)).thenReturn(invoiceSetup);
    	invoiceSetupServiceImpl.populateExistingSetupDetails(id,"Private",2l);
    }
    
//    @Test(expectedExceptions = { BusinessException.class })
    @Test
   	public void saveInvoiceSetup() throws ParseException{
    	
    	UUID id = UUID.randomUUID();
    	
    	InvoiceSetup oldSetup = new InvoiceSetup();
    	oldSetup.setInvoiceSetupName("name");
    	oldSetup.setInvoiceTypeName(InvoiceSetupConstants.REGULAR);
    	oldSetup.setInvoiceSetupId(id);
    	
    	InvoiceSetupDTO invoiceSetupDTO = new InvoiceSetupDTO();
    	invoiceSetupDTO.setPrefix("prefix");
    	invoiceSetupDTO.setStartingNumber(2);
    	invoiceSetupDTO.setSuffixType("suffix");
    	invoiceSetupDTO.setSeparator("seperator");
    	invoiceSetupDTO.setInvoiceSetupId(id);
    	invoiceSetupDTO.setInvoiceSetupName("name");
    	invoiceSetupDTO.setInvoiceTypeName(InvoiceSetupConstants.REGULAR);
    	invoiceSetupDTO.setAction(InvoiceConstants.SUBMITTED);
    	invoiceSetupDTO.setUpdatedNotes("notes");
//    	invoiceSetupDTO.setInvoiceSetupOptions("");
    	List<InvoiceSetup> invoiceSetups = Arrays.asList(oldSetup);
    	invoiceSetupDTO.setInvoiceSetupId(id);
    	when(invoiceSetupRepository.findOne(id)).thenReturn(oldSetup);
    	
    	EmployeeProfileDTO employee = new EmployeeProfileDTO();
    	when(invoiceService.getLoggedInUser()).thenReturn(employee);
    	
    	when(invoiceSetupRepository.save((InvoiceSetup)Mockito.anyObject())).thenReturn(oldSetup);
    	
    	invoiceSetupServiceImpl.saveInvoiceSetup(invoiceSetupDTO);
    }
    
    @Test
    public void getInvoiceSetup(){
    	UUID id = UUID.randomUUID();
    	InvoiceSetup invoiceSetup = new InvoiceSetup();
    	when(invoiceSetupRepository.findByEngagementId(id)).thenReturn(invoiceSetup);
    	invoiceSetupServiceImpl.getInvoiceSetup(id);
    }
    
    @Test
    public void getInvoiceSetupActivityLog(){
    	UUID id = UUID.randomUUID();
    	InvoiceSetupActivitiesLog invoiceSetupActivitiesLog = new InvoiceSetupActivitiesLog();
    	List<InvoiceSetupActivitiesLog> activityLogs = Arrays.asList(invoiceSetupActivitiesLog);
    	
    	when(invoiceSetupActivitiesLogRepository.findBySourceReferenceIdOrderByUpdatedOnDesc(id)).thenReturn(activityLogs);
    	invoiceSetupServiceImpl.getInvoiceSetupActivityLog(id);
    	
    }
    
		
}
