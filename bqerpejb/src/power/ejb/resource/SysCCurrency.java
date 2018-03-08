package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SysCCurrency entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_C_CURRENCY")
public class SysCCurrency implements java.io.Serializable {

	// Fields

	private Long currencyId;
	private String currencyNo;
	private String currencyName;
	private String currencyDesc;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public SysCCurrency() {
	}

	/** minimal constructor */
	public SysCCurrency(Long currencyId, String currencyNo,
			String currencyName, String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.currencyId = currencyId;
		this.currencyNo = currencyNo;
		this.currencyName = currencyName;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public SysCCurrency(Long currencyId, String currencyNo,
			String currencyName, String currencyDesc, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.currencyId = currencyId;
		this.currencyNo = currencyNo;
		this.currencyName = currencyName;
		this.currencyDesc = currencyDesc;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "CURRENCY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	@Column(name = "CURRENCY_NO", nullable = false, length = 10)
	public String getCurrencyNo() {
		return this.currencyNo;
	}

	public void setCurrencyNo(String currencyNo) {
		this.currencyNo = currencyNo;
	}

	@Column(name = "CURRENCY_NAME", nullable = false, length = 100)
	public String getCurrencyName() {
		return this.currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	@Column(name = "CURRENCY_DESC", length = 200)
	public String getCurrencyDesc() {
		return this.currencyDesc;
	}

	public void setCurrencyDesc(String currencyDesc) {
		this.currencyDesc = currencyDesc;
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