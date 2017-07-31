package com.tm.invoice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.invoice.domain.InvoiceTemplate;
import com.tm.invoice.domain.InvoiceTemplate.Active;

public interface InvoiceTemplateRepository extends
		JpaRepository<InvoiceTemplate, Long> {

	@Query("select invoiceTemplateId,invoiceTemplateName FROM InvoiceTemplate")
	List<InvoiceTemplate> getInvoicetemplateDetails();

	@Query("SELECT template FROM InvoiceTemplate template where template.activeStatus=:activeStatus")
	List<InvoiceTemplate> getAllActivetemplates(
			@Param("activeStatus") Active activeStatus);

	InvoiceTemplate findByInvoiceTemplateId(Long invoiceTemplateId);

}
