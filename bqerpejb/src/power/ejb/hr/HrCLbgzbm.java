package power.ejb.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCLbgzbm entity.
 *
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_LBGZBM")
public class HrCLbgzbm implements java.io.Serializable {

	// Fields

	private Long lbWorkId;
	private String lbWorkName;
	private String ifLbSpecialKind;
	private String retrieveCode;
	private String enterpriseCode;
	private Long lastModifiedBy;
	private Date lastModifiedDate;
	private String isUse;

	// Constructors

	/** default constructor */
	public HrCLbgzbm() {
	}

	/** minimal constructor */
	public HrCLbgzbm(Long lbWorkId) {
		this.lbWorkId = lbWorkId;
	}

	/** full constructor */
	public HrCLbgzbm(Long lbWorkId, String lbWorkName, String ifLbSpecialKind,
			String retrieveCode, String enterpriseCode, Long lastModifiedBy,
			Date lastModifiedDate, String isUse) {
		this.lbWorkId = lbWorkId;
		this.lbWorkName = lbWorkName;
		this.ifLbSpecialKind = ifLbSpecialKind;
		this.retrieveCode = retrieveCode;
		this.enterpriseCode = enterpriseCode;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "LB_WORK_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getLbWorkId() {
		return this.lbWorkId;
	}

	public void setLbWorkId(Long lbWorkId) {
		this.lbWorkId = lbWorkId;
	}

	@Column(name = "LB_WORK_NAME", length = 20)
	public String getLbWorkName() {
		return this.lbWorkName;
	}

	public void setLbWorkName(String lbWorkName) {
		this.lbWorkName = lbWorkName;
	}

	@Column(name = "IF_LB_SPECIAL_KIND", length = 1)
	public String getIfLbSpecialKind() {
		return this.ifLbSpecialKind;
	}

	public void setIfLbSpecialKind(String ifLbSpecialKind) {
		this.ifLbSpecialKind = ifLbSpecialKind;
	}

	@Column(name = "RETRIEVE_CODE", length = 20)
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

	@Column(name = "LAST_MODIFIED_BY", precision = 10, scale = 0)
	public Long getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(Long lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}