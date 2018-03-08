package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PurJQuotation entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PUR_J_QUOTATION")
public class PurJQuotation implements java.io.Serializable {

	// Fields

	private Long quotationId;
	private Long materialId;
	private Long supplier;
	private Long unitId;
	private Long quotationCurrency;
	private Double quotedQty;
	private Double quotedPrice;
	private Date effectiveDate;
	private Date discontinueDate;
	private String memo;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;

	// Constructors

	/** default constructor */
	public PurJQuotation() {
	}

	/** minimal constructor */
	public PurJQuotation(Long quotationId, Long materialId, Long unitId,
			Double quotedQty, Double quotedPrice, Date effectiveDate,
			Date discontinueDate, String enterpriseCode, String isUse,
			String lastModifiedBy, Date lastModifiedDate) {
		this.quotationId = quotationId;
		this.materialId = materialId;
		this.unitId = unitId;
		this.quotedQty = quotedQty;
		this.quotedPrice = quotedPrice;
		this.effectiveDate = effectiveDate;
		this.discontinueDate = discontinueDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

	/** full constructor */
	public PurJQuotation(Long quotationId, Long materialId, Long supplier,
			Long unitId, Long quotationCurrency, Double quotedQty,
			Double quotedPrice, Date effectiveDate, Date discontinueDate,
			String memo, String enterpriseCode, String isUse,
			String lastModifiedBy, Date lastModifiedDate) {
		this.quotationId = quotationId;
		this.materialId = materialId;
		this.supplier = supplier;
		this.unitId = unitId;
		this.quotationCurrency = quotationCurrency;
		this.quotedQty = quotedQty;
		this.quotedPrice = quotedPrice;
		this.effectiveDate = effectiveDate;
		this.discontinueDate = discontinueDate;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

	// Property accessors
	@Id
	@Column(name = "QUOTATION_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getQuotationId() {
		return this.quotationId;
	}

	public void setQuotationId(Long quotationId) {
		this.quotationId = quotationId;
	}

	@Column(name = "MATERIAL_ID", nullable = false, precision = 10, scale = 0)
	public Long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	@Column(name = "SUPPLIER", precision = 10, scale = 0)
	public Long getSupplier() {
		return this.supplier;
	}

	public void setSupplier(Long supplier) {
		this.supplier = supplier;
	}

	@Column(name = "UNIT_ID", nullable = false, precision = 10, scale = 0)
	public Long getUnitId() {
		return this.unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	@Column(name = "QUOTATION_CURRENCY", precision = 10, scale = 0)
	public Long getQuotationCurrency() {
		return this.quotationCurrency;
	}

	public void setQuotationCurrency(Long quotationCurrency) {
		this.quotationCurrency = quotationCurrency;
	}

	@Column(name = "QUOTED_QTY", nullable = false, precision = 10, scale = 3)
	public Double getQuotedQty() {
		return this.quotedQty;
	}

	public void setQuotedQty(Double quotedQty) {
		this.quotedQty = quotedQty;
	}

	@Column(name = "QUOTED_PRICE", nullable = false, precision = 18, scale = 5)
	public Double getQuotedPrice() {
		return this.quotedPrice;
	}

	public void setQuotedPrice(Double quotedPrice) {
		this.quotedPrice = quotedPrice;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECTIVE_DATE", nullable = false, length = 7)
	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DISCONTINUE_DATE", nullable = false, length = 7)
	public Date getDiscontinueDate() {
		return this.discontinueDate;
	}

	public void setDiscontinueDate(Date discontinueDate) {
		this.discontinueDate = discontinueDate;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

}