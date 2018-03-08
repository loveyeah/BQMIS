package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCStudytype entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_STUDYTYPE")
public class HrCStudytype implements java.io.Serializable {

	// Fields

	private Long studyTypeCodeId;
	private String studyType;
	private Long orderBy;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;
	private String retrieveCode;

	// Constructors

	/** default constructor */
	public HrCStudytype() {
	}

	/** minimal constructor */
	public HrCStudytype(Long studyTypeCodeId) {
		this.studyTypeCodeId = studyTypeCodeId;
	}

	/** full constructor */
	public HrCStudytype(Long studyTypeCodeId, String studyType, Long orderBy,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse, String retrieveCode) {
		this.studyTypeCodeId = studyTypeCodeId;
		this.studyType = studyType;
		this.orderBy = orderBy;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.retrieveCode = retrieveCode;
	}

	// Property accessors
	@Id
	@Column(name = "STUDY_TYPE_CODE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getStudyTypeCodeId() {
		return this.studyTypeCodeId;
	}

	public void setStudyTypeCodeId(Long studyTypeCodeId) {
		this.studyTypeCodeId = studyTypeCodeId;
	}

	@Column(name = "STUDY_TYPE", length = 30)
	public String getStudyType() {
		return this.studyType;
	}

	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
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

	@Column(name = "RETRIEVE_CODE", length = 8)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

}