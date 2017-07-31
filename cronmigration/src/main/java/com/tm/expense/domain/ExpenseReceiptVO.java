package com.tm.expense.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.springframework.http.ResponseEntity;

@Entity
@Table(name = "expense_receipt")
public class ExpenseReceiptVO extends AbstractAuditingEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8842707130988158632L;

	@Id
	@Column(name = "receipts_id")
	@Type(type = "uuid-char")
	private UUID receiptsUUID;

	@Column(name = "source")
	private String source;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "vendor_name")
	private String vendorName;

	@Column(name = "amount")
	private BigDecimal amount;

	@Column(name = "receipt_date")
	private String receiptDate;

	@Transient
	private transient ResponseEntity<byte[]> imageThumb;

	@Transient
	private String contentType;

	@Transient
	private String uploadDate;

	@Transient
	private String currencySymbol;

	@NotNull
	@Column(name = "status")
	private String status;

	@Type(type = "uuid-char")
	@Column(name = "expense_report_id")
	private UUID expenseReportUUID;

	@Column(name = "title")
	private String title;

	@Column(name = "expense_report_date")
	private String expenseReportDate;

	@Column(name = "ocr")
	private Boolean ocr = true;

	@Column(name = "ocr_status")
	private boolean ocrStatus;

	@Column(name = "currency_id")
	private Long currencyName;

	@Column(name = "original_file_name")
	private String originalFileName;

	@Column(name = "is_archived")
	private Boolean isArchived = false;

	@Column(name = "converted_amount")
	private BigDecimal convertedAmount;

	/**
	 * @return the ocrStatus
	 */
	public boolean isOcrStatus() {
		return ocrStatus;
	}

	/**
	 * @param ocrStatus
	 *            the ocrStatus to set
	 */
	public void setOcrStatus(boolean ocrStatus) {
		this.ocrStatus = ocrStatus;
	}

	/**
	 * @return the currencyName
	 */
	public Long getCurrencyName() {
		return currencyName;
	}

	/**
	 * @param currencyName
	 *            the currencyName to set
	 */
	public void setCurrencyName(Long currencyName) {
		this.currencyName = currencyName;
	}

	/**
	 * @return the ocr
	 */
	public Boolean getOcr() {
		return ocr;
	}

	/**
	 * @return the uploadDate
	 */
	public String getUploadDate() {
		return uploadDate;
	}

	/**
	 * @param uploadDate
	 *            the uploadDate to set
	 */
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Boolean getIsArchived() {
		return isArchived;
	}

	public void setIsArchived(Boolean isArchived) {
		this.isArchived = isArchived;
	}

	/**
	 * @return the currencySymbol
	 */
	public String getCurrencySymbol() {
		return currencySymbol;
	}

	/**
	 * @param currencySymbol
	 *            the currencySymbol to set
	 */
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	/**
	 * @return the receiptsUUID
	 */
	public UUID getReceiptsUUID() {
		return receiptsUUID;
	}

	/**
	 * @param receiptsUUID
	 *            the receiptsUUID to set
	 */
	public void setReceiptsUUID(UUID receiptsUUID) {
		this.receiptsUUID = receiptsUUID;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the vendorName
	 */
	public String getVendorName() {
		return vendorName;
	}

	/**
	 * @param vendorName
	 *            the vendorName to set
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the expenseReportDate
	 */
	public String getExpenseReportDate() {
		return expenseReportDate;
	}

	/**
	 * @param expenseReportDate
	 *            the expenseReportDate to set
	 */
	public void setExpenseReportDate(String expenseReportDate) {
		this.expenseReportDate = expenseReportDate;
	}

	/**
	 * @return the ocr
	 */
	public Boolean isOcr() {
		return ocr;
	}

	/**
	 * @param ocr
	 *            the ocr to set
	 */
	public void setOcr(Boolean ocr) {
		this.ocr = ocr;
	}

	/**
	 * @return the imageThumb
	 */
	public ResponseEntity<byte[]> getImageThumb() {
		return imageThumb;
	}

	/**
	 * @param imageThumb
	 *            the imageThumb to set
	 */
	public void setImageThumb(ResponseEntity<byte[]> imageThumb) {
		this.imageThumb = imageThumb;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the originalFileName
	 */
	public String getOriginalFileName() {
		return originalFileName;
	}

	/**
	 * @param originalFileName
	 *            the originalFileName to set
	 */
	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	/**
	 * @return the receiptDate
	 */
	public String getReceiptDate() {
		return receiptDate;
	}

	/**
	 * @param receiptDate
	 *            the receiptDate to set
	 */
	public void setReceiptDate(String receiptDate) {
		this.receiptDate = receiptDate;
	}

	public UUID getExpenseReportUUID() {
		return expenseReportUUID;
	}

	public void setExpenseReportUUID(UUID expenseReportUUID) {
		this.expenseReportUUID = expenseReportUUID;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the convertedAmount
	 */
	public BigDecimal getConvertedAmount() {
		return convertedAmount;
	}

	/**
	 * @param convertedAmount
	 *            the convertedAmount to set
	 */
	public void setConvertedAmount(BigDecimal convertedAmount) {
		this.convertedAmount = convertedAmount;
	}

}
