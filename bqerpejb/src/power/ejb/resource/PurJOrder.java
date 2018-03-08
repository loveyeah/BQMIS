package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PurJOrder entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PUR_J_ORDER")
public class PurJOrder implements java.io.Serializable {

	// Fields

	private Long id;
	private String purNo;
	private String project;
	private String buyer;
	private String contractNo;
	private Long supplier;
	private Date dueDate;
	private Date releaseDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Long currencyId;
	private Double taxRate;
	private String memo;
	private Long wfNo;
	private String purStatus;
	private String enterpriseCode;
	private String isUse;

	//发票号  add by bjxu  091209
	private String invoiceNo;
	// Constructors

	/** default constructor */
	public PurJOrder() {
	}

	/** minimal constructor */
	public PurJOrder(Long id, String purNo, String buyer, Date dueDate,
			String lastModifiedBy, Date lastModifiedDate, String purStatus,
			String enterpriseCode, String isUse) {
		this.id = id;
		this.purNo = purNo;
		this.buyer = buyer;
		this.dueDate = dueDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.purStatus = purStatus;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public PurJOrder(Long id, String purNo, String project, String buyer,
			String contractNo, Long supplier, Date dueDate, Date releaseDate,
			String lastModifiedBy, Date lastModifiedDate, Long currencyId,
			Double taxRate, String memo, Long wfNo, String purStatus,
			String enterpriseCode, String isUse) {
		this.id = id;
		this.purNo = purNo;
		this.project = project;
		this.buyer = buyer;
		this.contractNo = contractNo;
		this.supplier = supplier;
		this.dueDate = dueDate;
		this.releaseDate = releaseDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.currencyId = currencyId;
		this.taxRate = taxRate;
		this.memo = memo;
		this.wfNo = wfNo;
		this.purStatus = purStatus;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "PUR_NO", nullable = false, length = 20)
	public String getPurNo() {
		return this.purNo;
	}

	public void setPurNo(String purNo) {
		this.purNo = purNo;
	}

	@Column(name = "PROJECT", length = 10)
	public String getProject() {
		return this.project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	@Column(name = "BUYER", nullable = false, length = 16)
	public String getBuyer() {
		return this.buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	@Column(name = "CONTRACT_NO", length = 30)
	public String getContractNo() {
		return this.contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	@Column(name = "SUPPLIER", precision = 10, scale = 0)
	public Long getSupplier() {
		return this.supplier;
	}

	public void setSupplier(Long supplier) {
		this.supplier = supplier;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DUE_DATE", nullable = false, length = 7)
	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "RELEASE_DATE", length = 7)
	public Date getReleaseDate() {
		return this.releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Column(name = "LAST_MODIFIED_BY", nullable = false, length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", nullable = false, length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "CURRENCY_ID", precision = 10, scale = 0)
	public Long getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	@Column(name = "TAX_RATE", precision = 18, scale = 5)
	public Double getTaxRate() {
		return this.taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "WF_NO", precision = 22, scale = 0)
	public Long getWfNo() {
		return this.wfNo;
	}

	public void setWfNo(Long wfNo) {
		this.wfNo = wfNo;
	}

	@Column(name = "PUR_STATUS", nullable = false, length = 1)
	public String getPurStatus() {
		return this.purStatus;
	}

	public void setPurStatus(String purStatus) {
		this.purStatus = purStatus;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", nullable = false, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	
	@Column(name = "INVOICE_NO ",length = 50)
	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

}