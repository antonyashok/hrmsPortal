package com.tm.invoice.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tm.commonapi.constants.InvoiceConstants;
import com.tm.invoice.domain.InvoiceSetup;
import com.tm.invoice.domain.InvoiceSetupNote;

public interface InvoiceSetupNoteRepository extends
		JpaRepository<InvoiceSetupNote, Long> {

	@Query("select notes from InvoiceSetupNote notes where notes.invoiceSetup.invoiceSetupId=:invoiceSetupId")
	List<InvoiceSetupNote> getNotesByInvoiceSetupId(
			@Param(InvoiceConstants.INVOICESETUP_ID) UUID invoiceSetupId);

	void deleteByInvoiceSetup(InvoiceSetup invoiceSetup);
}
