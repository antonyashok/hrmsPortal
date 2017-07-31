package com.tm.invoice.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.domain.PurchaseOrder;
import com.tm.invoice.enums.ActiveFlag;
import com.tm.invoice.repository.PurchaseOrderRepositoryCustom;

public class PurchaseOrderRepositoryImpl implements
		PurchaseOrderRepositoryCustom {

	public static final String EQUAL_OPERATOR = "equal";
	public static final String LESS_THAN_OPERATOR = "lt";
	public static final String LESS_THAN_OR_EQUAL_OPERATOR = "le";
	public static final String GREATER_THAN_OPERATOR = "gt";
	public static final String GREATER_THAN_OR_EQUAL_OPERATOR = "ge";
	public static final String LIKE_OPERATOR = "like";

	private EntityManagerFactory emf;

	@PersistenceUnit
	public void setEntityManagerFactory(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Override
	public List<PurchaseOrder> getPODetailsByCriteria(UUID engagementId,String searchParam,ActiveFlag active,
			Pageable pageable,int totalSize) {
		List<Predicate> predicates = new ArrayList<>();
		EntityManager entityManager = this.emf.createEntityManager();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> initialCriteria = builder
				.createQuery(Object[].class);
		Root<PurchaseOrder> poRoot = initialCriteria
				.from(PurchaseOrder.class);
		populateSortProperty(initialCriteria, builder, poRoot, pageable);
		List<Object[]> list = new ArrayList<>();
		CriteriaQuery<Object[]> commonCriteria = getCommonCriteria(builder,
                poRoot, initialCriteria);
		   if (StringUtils.isNotBlank(searchParam)){
		            list = getCriteriaResult(entityManager, commonCriteria, builder,
		                    predicates, searchParam, poRoot, pageable,totalSize,engagementId);
		        }
		List<Map<String, Object>> dependentPOs = mapTheResponse(list);
		entityManager.close();
		if (CollectionUtils.isNotEmpty(dependentPOs)) {
			return populatePurchaseOrderList(dependentPOs);
		}
		return new ArrayList<>();
	}

	private void populateSortProperty(CriteriaQuery<Object[]> initialCriteria,
			CriteriaBuilder builder, Root<PurchaseOrder> poRoot,
			Pageable pageable) {
		String sort = pageable.getSort().toString();
		String[] parts = sort.split("\\:");
		String property = parts[0];
		String direction = parts[1].trim();
		if (StringUtils.isNotBlank(direction)
				&& InvoiceConstants.ASC.equalsIgnoreCase(direction)) {
			initialCriteria.orderBy(builder.asc(poRoot.get(property)));
		}
		if (StringUtils.isNotBlank(direction)
				&& InvoiceConstants.DESC.equalsIgnoreCase(direction)) {
			initialCriteria.orderBy(builder.desc(poRoot.get(property)));
		}
	}

	
	private CriteriaQuery<Object[]> getCommonCriteria(CriteriaBuilder builder,
            Root<PurchaseOrder> poRoot,
            CriteriaQuery<Object[]> initialCriteria) {
        return initialCriteria.select(
                builder.array(poRoot.get(InvoiceConstants.PO_NUMBER),
                        poRoot.get(InvoiceConstants.PURCHASE_ORDER_ID),
                        poRoot.get(InvoiceConstants.START_DATE),
                        poRoot.get(InvoiceConstants.END_DATE),
                        poRoot.get(InvoiceConstants.PO_AMOUNT),
                        poRoot.get(InvoiceConstants.ACTIVE_FLG),
                        poRoot.get(InvoiceConstants.LAST_UPDATED_ON),
                        poRoot.get(InvoiceConstants.ENGAGEMENTID),
                        poRoot.get(InvoiceConstants.PO_TYPE)
                     )).distinct(
                true);
    }
	
	private List<Object[]> getCriteriaResult(EntityManager entityManager,
			CriteriaQuery<Object[]> initialCriteria, CriteriaBuilder builder,
			List<Predicate> predicates, String searchParam,
			Root<PurchaseOrder> poRoot, Pageable pageable,int totalSize,UUID engagementId) {
		Expression<String> exp1 = builder.concat(
				poRoot.<String> get(InvoiceConstants.PO_NUMBER), " ");
		exp1 = builder.concat(exp1, poRoot.get(InvoiceConstants.PO_AMOUNT));
		Expression<String> exp2 = builder.concat(
				poRoot.get(InvoiceConstants.PO_AMOUNT), " ");
		exp2 = builder.concat(exp2, poRoot.get(InvoiceConstants.PO_TYPE));
		Expression<String> startDateStringExpr = builder.function(
				"DATE_FORMAT", String.class,
				poRoot.get(InvoiceConstants.START_DATE),
				builder.literal("'%d/%m/%Y %r'"));
		Expression<String> endDateStringExpr = builder.function("DATE_FORMAT",
				String.class, poRoot.get(InvoiceConstants.END_DATE),
				builder.literal("'%d/%m/%Y %r'"));
		  predicates.add(builder.equal(poRoot.get(InvoiceConstants.ENGAGEMENTID),engagementId));
		predicates.add(builder.or(
				builder.like(exp1, "%" + searchParam + "%"),
				builder.like(exp2, "%" + searchParam + "%"),
				builder.like(builder.lower(startDateStringExpr), "%"
						+ searchParam.toLowerCase() + "%"),
				builder.like(builder.lower(endDateStringExpr), "%"
						+ searchParam.toLowerCase() + "%")));
		initialCriteria.where(predicates.get(0),predicates.get(1));
		totalSize=entityManager.createQuery(initialCriteria).getResultList().size();
		return entityManager
				.createQuery(initialCriteria)
				.setFirstResult(
						pageable.getPageNumber() * pageable.getPageSize())
				.setMaxResults(pageable.getPageSize()).getResultList();
	}

	private List<Map<String, Object>> mapTheResponse(List<Object[]> list) {
		List<Map<String, Object>> dependentPOs = new ArrayList<>();
		for (Object[] result : list) {
			Map<String, Object> det = new HashMap<>();
			det.put(InvoiceConstants.PO_NUMBER, result[0]);
			det.put(InvoiceConstants.PURCHASE_ORDER_ID, result[1]);
			det.put(InvoiceConstants.START_DATE, result[2]);
			det.put(InvoiceConstants.END_DATE, result[3]);
			det.put(InvoiceConstants.PO_AMOUNT, result[4]);
			det.put(InvoiceConstants.ACTIVE_FLG, result[5]);
			det.put(InvoiceConstants.LAST_UPDATED_ON, result[6]);
			det.put(InvoiceConstants.ENGAGEMENTID, result[7]);
			det.put(InvoiceConstants.PO_TYPE,result[8]);
			dependentPOs.add(det);
		}
		return dependentPOs;
	}

	private List<PurchaseOrder> populatePurchaseOrderList(
			List<Map<String, Object>> dependentPOs) {
		List<PurchaseOrder> poList = new ArrayList<>();
		dependentPOs.forEach(po -> {
			final ObjectMapper mapper = new ObjectMapper();
			final PurchaseOrder purchaseOrder = mapper.convertValue(po,
					PurchaseOrder.class);
			poList.add(purchaseOrder);
		});
		return poList;

	}

}
