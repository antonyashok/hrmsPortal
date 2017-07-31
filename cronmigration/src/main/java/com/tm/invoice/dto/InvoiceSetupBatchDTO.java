package com.tm.invoice.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class InvoiceSetupBatchDTO implements Serializable {
	
	private static final long serialVersionUID = 2617939201277266747L;
	
	private String name ;	
	private List<InvoiceDTO> invoiceDTOList;	
	private LocalDate runCronDate;

	public List<InvoiceDTO> getInvoiceDTOList() {
		return invoiceDTOList;
	}

	public void setInvoiceDTOList(List<InvoiceDTO> invoiceDTOList) {
		this.invoiceDTOList = invoiceDTOList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public LocalDate getRunCronDate() {
        return runCronDate;
    }

    public void setRunCronDate(LocalDate runCronDate) {
        this.runCronDate = runCronDate;
    }	

}
