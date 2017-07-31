package com.tm.invoice.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.commonapi.web.rest.util.CommonUtils;
import com.tm.commonapi.web.rest.util.DiscoveryClientAndAccessTokenUtil;
import com.tm.invoice.domain.PoContractorsView;
import com.tm.invoice.domain.PurchaseOrder;
import com.tm.invoice.domain.PurchaseOrder.Potype;
import com.tm.invoice.dto.PoContractorsViewDTO;
import com.tm.invoice.dto.PurchaseOrderDTO;
import com.tm.invoice.engagement.dto.EngagementDTO;
import com.tm.invoice.enums.ActiveFlag;
import com.tm.invoice.exception.InvoiceEngagementRequestException;
import com.tm.invoice.exception.ParentEndAndStartLessTimeExceedException;
import com.tm.invoice.exception.PoAlreadyExistException;
import com.tm.invoice.exception.PoNotExistException;
import com.tm.invoice.exception.ProjectStartAndEndDateBetweenPoDateException;
import com.tm.invoice.exception.PurchaseOrderUpdateException;
import com.tm.invoice.exception.StartAndEndLessTimeExceedException;
import com.tm.invoice.exception.StartDateAndEndDateExceedException;
import com.tm.invoice.repository.PoContractorsViewRepository;
import com.tm.invoice.repository.PurchaseOrderRepository;
import com.tm.invoice.service.InvoiceAttachmentService;
import com.tm.invoice.service.PurchaseOrderService;
import com.tm.invoice.service.mapper.PoContractorsViewMapper;
import com.tm.invoice.service.mapper.PurchaseOrderViewMapper;
import com.tm.invoice.service.resttemplate.CustomerProfileRestTemplate;
import com.tm.invoice.util.InvoiceCommonUtils;

@Service
@Transactional
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    public static final String ENGAGEMENT_GROUP_KEY = "ENGAGEMENTMANAGEMENT";

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    private static final String ENGAGEMENT_ID_IS_REQUIRED = "Engagement is Required";

    private static final String ENGAGEMENT_DATA_IS_NOT_AVAILABLE =
            "Engagement data is not available";

    private PurchaseOrderRepository purchaseOrderRepository;

    private InvoiceAttachmentService invoiceAttachmentsService;

    private DiscoveryClient discoveryClient;

    private RestTemplate restTemplate;

    private static final String REVENUE = "Revenue";
    private static final String EXPENSE = "Expense";

    private static final String EXCEP_UPDATE_PO =
            "Update po detail action is restricted for invoice generated PO";

    private static final String PARENT_NOT_EXIST = "_(Parent Not Exist)";
    
    private PoContractorsViewRepository poContractorsViewRepository;

    @Inject
    public PurchaseOrderServiceImpl(@NotNull final PurchaseOrderRepository purchaseOrderRepository,
            @LoadBalanced final RestTemplate restTemplate,
            @Qualifier("discoveryClient") final DiscoveryClient discoveryClient,
            InvoiceAttachmentService invoiceAttachmentsService,
            PoContractorsViewRepository poContractorsViewRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
        this.invoiceAttachmentsService = invoiceAttachmentsService;
        this.poContractorsViewRepository=poContractorsViewRepository;
    }


    @Override
    public void createPO(PurchaseOrderDTO purchaseOrderDTO) {
        EngagementDTO engagementDTO = getEngagementDetails(purchaseOrderDTO.getEngagementId());
        poDateValidation(purchaseOrderDTO, engagementDTO);
        validateData(purchaseOrderDTO);
        updateChildPODetails(engagementDTO, purchaseOrderDTO);
        PurchaseOrder po =
                PurchaseOrderViewMapper.INSTANCE.purchaseOrderDTOToPurchaseOrder(purchaseOrderDTO);
        CommonUtils.populateAuditDetailsForSave(po);
        po.setPoNumber(purchaseOrderDTO.getPoNumber().trim());
        PurchaseOrder purchaseOrder = purchaseOrderRepository.save(po);
        invoiceAttachmentsService.updateFileDetails(po.getPurchaseOrderId().toString(),
                purchaseOrderDTO.getPurcheaseOrderAttachements());
        purchaseOrderDTO.setPurchaseOrderId(purchaseOrder.getPurchaseOrderId());
        updateEngagementDetails(purchaseOrderDTO, engagementDTO);
        if (purchaseOrderDTO.getPurchaseOrderType().equals(Potype.REVENUE)) {
            updateEngagementDetails(engagementDTO.getEngagementId(), REVENUE,
                    engagementDTO.getRevenuePurchaseOrderId(), engagementDTO.getRevenuePoNumber(),
                    engagementDTO.getInitialRevenueAmount(), engagementDTO.getTotalRevenueAmount(),
                    engagementDTO.getBalanceRevenueAmount(), engagementDTO.getEngmtEndDate(),
                    engagementDTO.getRevenuePoIssueDate());
        }
        if (purchaseOrderDTO.getPurchaseOrderType().equals(Potype.EXPENSE)) {
            updateEngagementDetails(engagementDTO.getEngagementId(), EXPENSE,
                    engagementDTO.getExpensePurchaseOrderId(), engagementDTO.getExpensePoNumber(),
                    engagementDTO.getInitialExpenseAmount(), engagementDTO.getTotalExpenseAmount(),
                    engagementDTO.getBalanceExpenseAmount(), engagementDTO.getEngmtEndDate(),engagementDTO.getExpensePoIssueDate());
        }

    }

    private void updateChildPODetails(EngagementDTO engagementDTO,
            PurchaseOrderDTO purchaseOrderDTO) {
        PurchaseOrder purchaseOrderDetail;
        if (purchaseOrderDTO.getPurchaseOrderType().equals(Potype.REVENUE)
                && null != engagementDTO.getRevenuePurchaseOrderId()) {
            purchaseOrderDetail =
                    purchaseOrderRepository.findOne(engagementDTO.getRevenuePurchaseOrderId());
            purchaseOrderDTO.setParentPurchaseOrderId(engagementDTO.getRevenuePurchaseOrderId());
            handlePOException(purchaseOrderDTO, purchaseOrderDetail,engagementDTO.getRevenuePurchaseOrderId());
        }
        if (purchaseOrderDTO.getPurchaseOrderType().equals(Potype.EXPENSE)
                && null != engagementDTO.getExpensePurchaseOrderId()) {
            purchaseOrderDetail =
                    purchaseOrderRepository.findOne(engagementDTO.getExpensePurchaseOrderId());
            purchaseOrderDTO.setParentPurchaseOrderId(engagementDTO.getExpensePurchaseOrderId());
            handlePOException(purchaseOrderDTO, purchaseOrderDetail,engagementDTO.getRevenuePurchaseOrderId());
        }

    }

    private void updateEngagementDetails(PurchaseOrderDTO purchaseOrderDTO,
            EngagementDTO engagementDTO) {
        if (purchaseOrderDTO.getPurchaseOrderType().equals(Potype.REVENUE)) {
            if (null == engagementDTO.getRevenuePurchaseOrderId()) {
                engagementDTO.setTotalRevenueAmount(BigDecimal.valueOf(0.0));
                engagementDTO.setBalanceRevenueAmount(BigDecimal.valueOf(0.0));
                engagementDTO.setRevenuePurchaseOrderId(purchaseOrderDTO.getPurchaseOrderId());
                engagementDTO.setInitialRevenueAmount(purchaseOrderDTO.getPurchaseOrderAmount());
                engagementDTO.setRevenuePoNumber(purchaseOrderDTO.getPoNumber());
                engagementDTO.setRevenuePoIssueDate(InvoiceCommonUtils.convertDateToString(new Date()));
            }
            BigDecimal totalAmount = engagementDTO.getTotalRevenueAmount()
                    .add(purchaseOrderDTO.getPurchaseOrderAmount());
            BigDecimal balanceAmount = engagementDTO.getBalanceRevenueAmount()
                    .add(purchaseOrderDTO.getPurchaseOrderAmount());
            engagementDTO.setTotalRevenueAmount(totalAmount);
            engagementDTO.setBalanceRevenueAmount(balanceAmount);
        } else if (purchaseOrderDTO.getPurchaseOrderType().equals(Potype.EXPENSE)) {
            if (null == engagementDTO.getExpensePurchaseOrderId()) {
                engagementDTO.setTotalExpenseAmount(BigDecimal.valueOf(0.0));
                engagementDTO.setBalanceExpenseAmount(BigDecimal.valueOf(0.0));
                engagementDTO.setExpensePurchaseOrderId(purchaseOrderDTO.getPurchaseOrderId());
                engagementDTO.setInitialExpenseAmount(purchaseOrderDTO.getPurchaseOrderAmount());
                engagementDTO.setExpensePoNumber(purchaseOrderDTO.getPoNumber());
                engagementDTO.setExpensePoIssueDate(InvoiceCommonUtils.convertDateToString(new Date()));
            }
            engagementDTO.setTotalExpenseAmount(engagementDTO.getTotalExpenseAmount()
                    .add(purchaseOrderDTO.getPurchaseOrderAmount()));
            engagementDTO.setBalanceExpenseAmount(engagementDTO.getBalanceExpenseAmount()
                    .add(purchaseOrderDTO.getPurchaseOrderAmount()));
        }
        updateEngagementDate(engagementDTO,purchaseOrderDTO);
    }

    private Integer updateEngagementDetails(UUID engagementId, String type, UUID poId,
            String poNumber, BigDecimal initialAmount, BigDecimal totalAmount,
            BigDecimal balanceAmount, String engmtDate, String issueDate) {
        CustomerProfileRestTemplate customerProfileRestTemplate = new CustomerProfileRestTemplate(
                restTemplate, DiscoveryClientAndAccessTokenUtil
                        .discoveryClient(ENGAGEMENT_GROUP_KEY, discoveryClient),
                DiscoveryClientAndAccessTokenUtil.getAccessToken(), null);
        Integer updateValue = customerProfileRestTemplate.updateEngagementDetails(engagementId,
                type, poId, poNumber, initialAmount, totalAmount, balanceAmount, engmtDate,issueDate);
        if (0 > updateValue) {
            log.error("Engagement Id not Found");
            throw new InvoiceEngagementRequestException(ENGAGEMENT_ID_IS_REQUIRED);

        }
        return updateValue;
    }

    @Override
    public EngagementDTO getEngagementDetails(UUID engagementId) {
        CustomerProfileRestTemplate customerProfileRestTemplate = new CustomerProfileRestTemplate(
                restTemplate, DiscoveryClientAndAccessTokenUtil
                        .discoveryClient(ENGAGEMENT_GROUP_KEY, discoveryClient),
                DiscoveryClientAndAccessTokenUtil.getAccessToken(), null);
        EngagementDTO engagementDTO =
                customerProfileRestTemplate.getEngagementDetails(engagementId);
        if (Objects.nonNull(engagementDTO)) {
            if (Objects.isNull(engagementDTO.getEngagementId())) {
                log.error("Engagement Id not Found");
                throw new InvoiceEngagementRequestException(ENGAGEMENT_ID_IS_REQUIRED);
            }
        } else {
            throw new InvoiceEngagementRequestException(ENGAGEMENT_DATA_IS_NOT_AVAILABLE);
        }
        return engagementDTO;
    }

    private void validateData(PurchaseOrderDTO purchaseOrderDTO) {
        PurchaseOrder purchaseOrderDetail;
        if (null != purchaseOrderDTO.getPurchaseOrderId()) {
            purchaseOrderDetail =
                    purchaseOrderRepository.findByPoNumberAndByEngagementIdAndByPurchaseOrderId(
                            purchaseOrderDTO.getPoNumber().trim(),
                            purchaseOrderDTO.getPurchaseOrderId(),
                            purchaseOrderDTO.getEngagementId());
        } else {
            purchaseOrderDetail = purchaseOrderRepository.findByPoNumberAndByEngagementId(
                    purchaseOrderDTO.getPoNumber().trim(), purchaseOrderDTO.getEngagementId());
        }
        if (null != purchaseOrderDetail) {
            throw new PoAlreadyExistException(purchaseOrderDTO.getPoNumber().trim());
        }
        if (InvoiceCommonUtils.convertStringToDate(purchaseOrderDTO.getStartDate())
                .after(InvoiceCommonUtils.convertStringToDate(purchaseOrderDTO.getEndDate()))) {
            throw new StartAndEndLessTimeExceedException(purchaseOrderDTO.getPoNumber());
        }
    }

    private void poDateValidation(PurchaseOrderDTO purchaseOrderDTO, EngagementDTO engagementDTO) {
        Date engagementStartDate =
                InvoiceCommonUtils.convertStringToDate(engagementDTO.getEngmtStartDate());
        Date poStartDate = InvoiceCommonUtils.convertStringToDate(purchaseOrderDTO.getStartDate());
        Date poEndDate = InvoiceCommonUtils.convertStringToDate(purchaseOrderDTO.getEndDate());
        if (poStartDate.after(poEndDate)) {
            throw new StartDateAndEndDateExceedException("");
        }
        if (engagementStartDate.after(poStartDate)) {
            throw new ProjectStartAndEndDateBetweenPoDateException(
                    engagementDTO.getEngmtStartDate() + " and project end date "
                            + engagementDTO.getEngmtEndDate());
        }

    }

    private void handlePOException(PurchaseOrderDTO purchaseOrderDTO,
            PurchaseOrder purchaseOrderDetail, UUID parentId) {
        if (null == purchaseOrderDetail) {
            throw new PoNotExistException(
                    parentId+ PARENT_NOT_EXIST);
        }

        if (purchaseOrderDetail.getEndDate()
                .after(InvoiceCommonUtils.convertStringToDate(purchaseOrderDTO.getStartDate()))
                || InvoiceCommonUtils.convertDateToString(purchaseOrderDetail.getEndDate())
                        .equals(purchaseOrderDTO.getStartDate())) {
            throw new ParentEndAndStartLessTimeExceedException(purchaseOrderDTO.getPoNumber());
        }

    }


    @Override
    public void updatePO(PurchaseOrderDTO purchaseOrderDTO) {
        EngagementDTO engagementDTO = getEngagementDetails(purchaseOrderDTO.getEngagementId());
        if (null != purchaseOrderDTO.getInvoiceGeneratedDate()) {
            throw new PurchaseOrderUpdateException(EXCEP_UPDATE_PO);
        }
        PurchaseOrder beforeUpdate;
        if (null != purchaseOrderDTO.getPurchaseOrderId()) {
            if (purchaseOrderDTO.getPurchaseOrderType().equals(Potype.REVENUE)) {
                engagementDTO.setRevenuePoNumber(purchaseOrderDTO.getPoNumber());
            } else if (purchaseOrderDTO.getPurchaseOrderType().equals(Potype.EXPENSE)) {
                engagementDTO.setExpensePoNumber(purchaseOrderDTO.getPoNumber());
            }
            beforeUpdate = purchaseOrderRepository
                    .findByPurchaseOrderId(purchaseOrderDTO.getPurchaseOrderId());
            if (!beforeUpdate.getPurchaseOrderAmount()
                    .equals(purchaseOrderDTO.getPurchaseOrderAmount())) {
                updateEngagementAmountValue(engagementDTO, purchaseOrderDTO, beforeUpdate);
            }
        }
        poDateValidation(purchaseOrderDTO, engagementDTO);
        validateData(purchaseOrderDTO);
        PurchaseOrder po =
                PurchaseOrderViewMapper.INSTANCE.purchaseOrderDTOToPurchaseOrder(purchaseOrderDTO);
        CommonUtils.populateAuditDetailsForSave(po);
        po.setPoNumber(purchaseOrderDTO.getPoNumber().trim());
        PurchaseOrder purchaseOrder = purchaseOrderRepository.save(po);
        invoiceAttachmentsService.updateFileDetails(po.getPurchaseOrderId().toString(),
                purchaseOrderDTO.getPurcheaseOrderAttachements());
        purchaseOrderDTO.setPurchaseOrderId(purchaseOrder.getPurchaseOrderId());
        updateEngagementDate(engagementDTO,purchaseOrderDTO);
        if (purchaseOrderDTO.getPurchaseOrderType().equals(Potype.REVENUE)) {
            updateEngagementDetails(engagementDTO.getEngagementId(), REVENUE,
                    engagementDTO.getRevenuePurchaseOrderId(), engagementDTO.getRevenuePoNumber(),
                    engagementDTO.getInitialRevenueAmount(), engagementDTO.getTotalRevenueAmount(),
                    engagementDTO.getBalanceRevenueAmount(), engagementDTO.getEngmtEndDate(),engagementDTO.getRevenuePoIssueDate());
        }
        if (purchaseOrderDTO.getPurchaseOrderType().equals(Potype.EXPENSE)) {
            updateEngagementDetails(engagementDTO.getEngagementId(), EXPENSE,
                    engagementDTO.getExpensePurchaseOrderId(), engagementDTO.getExpensePoNumber(),
                    engagementDTO.getInitialExpenseAmount(), engagementDTO.getTotalExpenseAmount(),
                    engagementDTO.getBalanceExpenseAmount(), engagementDTO.getEngmtEndDate(),engagementDTO.getExpensePoIssueDate());
        }
       
    }

    private void updateEngagementAmountValue(EngagementDTO engagementDTO,
            PurchaseOrderDTO purchaseOrderDTO, PurchaseOrder beforeUpdate) {
        MathContext mc = new MathContext(12);
        BigDecimal oldPOAmount = beforeUpdate.getPurchaseOrderAmount();
        BigDecimal newPOAmount = purchaseOrderDTO.getPurchaseOrderAmount();
        if (purchaseOrderDTO.getPurchaseOrderType().equals(Potype.REVENUE)) {
            if (engagementDTO.getRevenuePurchaseOrderId()
                    .equals(purchaseOrderDTO.getPurchaseOrderId())) {
                engagementDTO.setInitialRevenueAmount(purchaseOrderDTO.getPurchaseOrderAmount());
            }
            BigDecimal oldTotalAmountValue = engagementDTO.getTotalRevenueAmount();
            BigDecimal newTotalAmountValue = oldTotalAmountValue.subtract(oldPOAmount, mc);
            BigDecimal oldBalanceAmountValue = engagementDTO.getBalanceRevenueAmount();
            BigDecimal newBalanceAmountValue = oldBalanceAmountValue.subtract(oldPOAmount, mc);
            engagementDTO.setTotalRevenueAmount(newTotalAmountValue.add(newPOAmount, mc));
            engagementDTO.setBalanceRevenueAmount(newBalanceAmountValue.add(newPOAmount, mc));
        } else if (purchaseOrderDTO.getPurchaseOrderType().equals(Potype.EXPENSE)) {
            if (engagementDTO.getExpensePurchaseOrderId()
                    .equals(purchaseOrderDTO.getPurchaseOrderId())) {
                engagementDTO.setInitialExpenseAmount(purchaseOrderDTO.getPurchaseOrderAmount());
            }
            BigDecimal oldTotalAmountValue = engagementDTO.getTotalExpenseAmount();
            BigDecimal newTotalAmountValue = oldTotalAmountValue.subtract(oldPOAmount, mc);
            BigDecimal oldBalanceAmountValue = engagementDTO.getBalanceExpenseAmount();
            BigDecimal newBalanceAmountValue = oldBalanceAmountValue.subtract(oldPOAmount, mc);
            engagementDTO.setTotalExpenseAmount(newTotalAmountValue.add(newPOAmount, mc));
            engagementDTO.setBalanceExpenseAmount(newBalanceAmountValue.add(newPOAmount, mc));
        }
    }
    
    void updateEngagementDate(EngagementDTO engagementDTO, PurchaseOrderDTO purchaseOrderDTO){
        Date engagementEndDate =
                InvoiceCommonUtils.convertStringToDate(engagementDTO.getEngmtEndDate());
        Date poEndDate = InvoiceCommonUtils.convertStringToDate(purchaseOrderDTO.getEndDate());
        if (engagementEndDate.before(poEndDate)) {
            engagementDTO.setEngmtEndDate(purchaseOrderDTO.getEndDate());
        }
    }

    @Override
    public Page<PurchaseOrderDTO> getAllPODetails(UUID engagementId, String searchParam,
            String activeFlagValue, Pageable pageable) {
        Pageable pageRequest = pageable;
        ActiveFlag activeFlag = ActiveFlag.Y;
        List<PurchaseOrder> purchaseOrders;
        int totalSize = 0;
        if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
            pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.Direction.ASC, "auditDetails.createDate");
        }
        if (activeFlagValue.equals(InvoiceConstants.N_STR)) {
            activeFlag = ActiveFlag.N;
        }
        if (StringUtils.isNotBlank(searchParam)) {
            purchaseOrders = purchaseOrderRepository.getPODetailsByCriteria(engagementId,
                    searchParam, activeFlag, pageRequest, totalSize);
        } else {
            totalSize = purchaseOrderRepository
                    .findByEngagementIdAndByActive(engagementId, activeFlag).size();
            purchaseOrders = purchaseOrderRepository.findByEngagementIdAndByActive(engagementId,
                    activeFlag, pageRequest);
        }
        List<PurchaseOrderDTO> purchaseOrderDetailsDTOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(purchaseOrders)) {
            purchaseOrderDetailsDTOList = PurchaseOrderViewMapper.INSTANCE
                    .purchaseOrdersToPurchaseOrderDTOs(purchaseOrders);
        }
        return new PageImpl<>(purchaseOrderDetailsDTOList, pageRequest, totalSize);
    }


    @Override
    public Page<PoContractorsViewDTO> getAllContractorDetailsByEngagementId(UUID engagementId,
            Pageable pageable) {
        Pageable pageRequest = null;
        int totalSize = 0;
        List<PoContractorsViewDTO> poContractorsViewDTOs = new ArrayList<>();
        if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
            pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.Direction.DESC, "contractStartDate");
        }
        Page<PoContractorsView> poContractorsViewPage =
                poContractorsViewRepository.findByEngagementId(engagementId, pageRequest);
        if (Objects.nonNull(poContractorsViewPage.getContent())) {
            List<PoContractorsView> poContractorsViews = poContractorsViewPage.getContent();
            totalSize = poContractorsViewPage.getContent().size();
            poContractorsViewDTOs = PoContractorsViewMapper.INSTANCE
                    .poContractorsViewsToPoContractorsViewDTOs(poContractorsViews);
        }
        return new PageImpl<>(poContractorsViewDTOs, pageRequest, totalSize);

    }

    @Override
    public Page<PoContractorsViewDTO> getAllContractorDetailsByContractorName(UUID engagementId,
            String searchParam, Pageable pageable) {
        Pageable pageRequest = null;
        Page<PoContractorsView> poContractorsViewPage;
        int totalSize = 0;
        List<PoContractorsViewDTO> poContractorsViewDTOs = new ArrayList<>();
        if (Objects.nonNull(pageable) && Objects.isNull(pageable.getSort())) {
            pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.Direction.DESC, "contractStartDate");
        }
        if (StringUtils.isNotBlank(searchParam)) {
            poContractorsViewPage = poContractorsViewRepository.findByContractorName(engagementId,
                    searchParam.trim(), pageRequest);
        } else {
            poContractorsViewPage =
                    poContractorsViewRepository.findByEngagementId(engagementId, pageRequest);
        }
        if (Objects.nonNull(poContractorsViewPage.getContent())) {
            List<PoContractorsView> poContractorsViews = poContractorsViewPage.getContent();
            totalSize = poContractorsViewPage.getContent().size();
            poContractorsViewDTOs = PoContractorsViewMapper.INSTANCE
                    .poContractorsViewsToPoContractorsViewDTOs(poContractorsViews);
        }
        return new PageImpl<>(poContractorsViewDTOs, pageRequest, totalSize);
    }

    @Override
    public PurchaseOrderDTO getPurchaseOrder(UUID purchaseOrderId) {
        PurchaseOrder purchaseOrderDetail = purchaseOrderRepository.findOne(purchaseOrderId);
        if (null == purchaseOrderDetail) {
            throw new PoNotExistException(purchaseOrderId.toString());
        }
        return PurchaseOrderViewMapper.INSTANCE
                .purchaseOrderFieldToPurchaseOrderDTOField(purchaseOrderDetail);

    }
}
