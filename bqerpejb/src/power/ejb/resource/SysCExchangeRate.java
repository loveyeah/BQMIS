package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SysCExchangeRate entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_C_EXCHANGE_RATE")
public class SysCExchangeRate implements java.io.Serializable {

	// Fields

	private Long exchangeRateId;
	private Long oriCurrencyId;
	private Long dstCurrencyId;
	private Double rate;
	private Date effectiveDate;
	private Date discontinueDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public SysCExchangeRate() {
	}

	/** minimal constructor */
	public SysCExchangeRate(Long exchangeRateId, Double rate,
			Date effectiveDate, Date discontinueDate, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.exchangeRateId = exchangeRateId;
		this.rate = rate;
		this.effectiveDate = effectiveDate;
		this.discontinueDate = discontinueDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}
	
	/** full constructor */
	public SysCExchangeRate(Long exchangeRateId, Long oriCurrencyId,
			Long dstCurrencyId, Double rate, Date effectiveDate,
			Date discontinueDate, String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.exchangeRateId = exchangeRateId;
		this.oriCurrencyId = oriCurrencyId;
		this.dstCurrencyId = dstCurrencyId;
		this.rate = rate;
		this.effectiveDate = effectiveDate;
		this.discontinueDate = discontinueDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "EXCHANGE_RATE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getExchangeRateId() {
		return this.exchangeRateId;
	}

	public void setExchangeRateId(Long exchangeRateId) {
		this.exchangeRateId = exchangeRateId;
	}

	@Column(name = "ORI_CURRENCY_ID", precision = 10, scale = 0)
	public Long getOriCurrencyId() {
		return this.oriCurrencyId;
	}

	public void setOriCurrencyId(Long oriCurrencyId) {
		this.oriCurrencyId = oriCurrencyId;
	}

	@Column(name = "DST_CURRENCY_ID", precision = 10, scale = 0)
	public Long getDstCurrencyId() {
		return this.dstCurrencyId;
	}

	public void setDstCurrencyId(Long dstCurrencyId) {
		this.dstCurrencyId = dstCurrencyId;
	}

	@Column(name = "RATE", nullable = false, precision = 10, scale = 3)
	public Double getRate() {
		return this.rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
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

}