package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCLanguage entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_LANGUAGE")
public class HrCLanguage implements java.io.Serializable {

	// Fields

	private Long languageCodeId;
	private String languageName;
	private String retrieveCode;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String insertby;
	private Date insertdate;
	private Long orderBy;

	// Constructors

	/** default constructor */
	public HrCLanguage() {
	}

	/** minimal constructor */
	public HrCLanguage(Long languageCodeId) {
		this.languageCodeId = languageCodeId;
	}

	/** full constructor */
	public HrCLanguage(Long languageCodeId, String languageName,
			String retrieveCode, String enterpriseCode, String isUse,
			String lastModifiedBy, Date lastModifiedDate, String insertby,
			Date insertdate, Long orderBy) {
		this.languageCodeId = languageCodeId;
		this.languageName = languageName;
		this.retrieveCode = retrieveCode;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.insertby = insertby;
		this.insertdate = insertdate;
		this.orderBy = orderBy;
	}

	// Property accessors
	@Id
	@Column(name = "LANGUAGE_CODE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getLanguageCodeId() {
		return this.languageCodeId;
	}

	public void setLanguageCodeId(Long languageCodeId) {
		this.languageCodeId = languageCodeId;
	}

	@Column(name = "LANGUAGE_NAME", length = 20)
	public String getLanguageName() {
		return this.languageName;
	}

	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}

	@Column(name = "RETRIEVE_CODE", length = 8)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "INSERTBY", length = 16)
	public String getInsertby() {
		return this.insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTDATE", length = 7)
	public Date getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

}