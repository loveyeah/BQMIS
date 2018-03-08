package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCPolitics entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_POLITICS")
public class HrCPolitics implements java.io.Serializable {

	// Fields

	private Long politicsId;
	private String politicsName;
	private String isUse;
	private String retrieveCode;
	private Long orderBy;
	private String enterpriseCode;
	private String lastModifiedBy;
	private Date lastModifiedDate;

	// Constructors

	/** default constructor */
	public HrCPolitics() {
	}

	/** minimal constructor */
	public HrCPolitics(Long politicsId) {
		this.politicsId = politicsId;
	}

	/** full constructor */
	public HrCPolitics(Long politicsId, String politicsName, String isUse,
			String retrieveCode, Long orderBy, String enterpriseCode,
			String lastModifiedBy, Date lastModifiedDate) {
		this.politicsId = politicsId;
		this.politicsName = politicsName;
		this.isUse = isUse;
		this.retrieveCode = retrieveCode;
		this.orderBy = orderBy;
		this.enterpriseCode = enterpriseCode;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

	// Property accessors
	@Id
	@Column(name = "POLITICS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPoliticsId() {
		return this.politicsId;
	}

	public void setPoliticsId(Long politicsId) {
		this.politicsId = politicsId;
	}

	@Column(name = "POLITICS_NAME", length = 100)
	public String getPoliticsName() {
		return this.politicsName;
	}

	public void setPoliticsName(String politicsName) {
		this.politicsName = politicsName;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "RETRIEVE_CODE", length = 20)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
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

}