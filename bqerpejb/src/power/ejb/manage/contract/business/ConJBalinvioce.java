package power.ejb.manage.contract.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConJBalinvioce entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_J_BALINVIOCE")
public class ConJBalinvioce implements java.io.Serializable {

	// Fields

	private Long invoiceNo;
	private Long balanceId;
	private String invoiceCode;
	private String invoiceId;
	private Date invoiceDate;
	private String invoiceName;
	private Double invoicePrice;
	private String drawerBy;
	private String operateBy;
	private String memo;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public ConJBalinvioce() {
	}

	/** minimal constructor */
	public ConJBalinvioce(Long invoiceNo, Long balanceId,
			String enterpriseCode, String isUse) {
		this.invoiceNo = invoiceNo;
		this.balanceId = balanceId;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public ConJBalinvioce(Long invoiceNo, Long balanceId, String invoiceCode,
			String invoiceId, Date invoiceDate, String invoiceName,
			Double invoicePrice, String drawerBy, String operateBy,
			String memo, String enterpriseCode, String isUse) {
		this.invoiceNo = invoiceNo;
		this.balanceId = balanceId;
		this.invoiceCode = invoiceCode;
		this.invoiceId = invoiceId;
		this.invoiceDate = invoiceDate;
		this.invoiceName = invoiceName;
		this.invoicePrice = invoicePrice;
		this.drawerBy = drawerBy;
		this.operateBy = operateBy;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "INVOICE_NO", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(Long invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	@Column(name = "BALANCE_ID", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getBalanceId() {
		return this.balanceId;
	}

	public void setBalanceId(Long balanceId) {
		this.balanceId = balanceId;
	}

	@Column(name = "INVOICE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public String getInvoiceCode() {
		return this.invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	@Column(name = "INVOICE_ID", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "INVOICE_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getInvoiceDate() {
		return this.invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	@Column(name = "INVOICE_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 300)
	public String getInvoiceName() {
		return this.invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	@Column(name = "INVOICE_PRICE", unique = false, nullable = true, insertable = true, updatable = true, precision = 15, scale = 4)
	public Double getInvoicePrice() {
		return this.invoicePrice;
	}

	public void setInvoicePrice(Double invoicePrice) {
		this.invoicePrice = invoicePrice;
	}

	@Column(name = "DRAWER_BY", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public String getDrawerBy() {
		return this.drawerBy;
	}

	public void setDrawerBy(String drawerBy) {
		this.drawerBy = drawerBy;
	}

	@Column(name = "OPERATE_BY", unique = false, nullable = true, insertable = true, updatable = true, length = 16)
	public String getOperateBy() {
		return this.operateBy;
	}

	public void setOperateBy(String operateBy) {
		this.operateBy = operateBy;
	}

	@Column(name = "MEMO", unique = false, nullable = true, insertable = true, updatable = true, length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTERPRISE_CODE", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}