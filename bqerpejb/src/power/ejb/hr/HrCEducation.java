//package power.ejb.hr;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
///**
// * HrCEducation entity.
// * 
// * @author MyEclipse Persistence Tools
// */
//@Entity
//@Table(name = "HR_C_EDUCATION", schema = "power" )
//public class HrCEducation implements java.io.Serializable {
//
//	// Fields
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 3568998277927153343L;
//	private Long educationId;
//	private String educationName;
//	private String isUse;
//	private String retrieveCode;
//
//	// Constructors
//
//	/** default constructor */
//	public HrCEducation() {
//	}
//
//	/** minimal constructor */
//	public HrCEducation(Long educationId) {
//		this.educationId = educationId;
//	}
//
//	/** full constructor */
//	public HrCEducation(Long educationId, String educationName, String isUse,
//			String retrieveCode) {
//		this.educationId = educationId;
//		this.educationName = educationName;
//		this.isUse = isUse;
//		this.retrieveCode = retrieveCode;
//	}
//
//	// Property accessors
//	@Id
//	@Column(name = "EDUCATION_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
//	public Long getEducationId() {
//		return this.educationId;
//	}
//
//	public void setEducationId(Long educationId) {
//		this.educationId = educationId;
//	}
//
//	@Column(name = "EDUCATION_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
//	public String getEducationName() {
//		return this.educationName;
//	}
//
//	public void setEducationName(String educationName) {
//		this.educationName = educationName;
//	}
//
//	@Column(name = "IS_USE", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
//	public String getIsUse() {
//		return this.isUse;
//	}
//
//	public void setIsUse(String isUse) {
//		this.isUse = isUse;
//	}
//
//	@Column(name = "RETRIEVE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
//	public String getRetrieveCode() {
//		return this.retrieveCode;
//	}
//
//	public void setRetrieveCode(String retrieveCode) {
//		this.retrieveCode = retrieveCode;
//	}
//
//}


// add by liuyi 091124
package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCEducation entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_EDUCATION")
public class HrCEducation implements java.io.Serializable {

	// Fields

	private Long educationId;
	private String educationName;
	private String isUse;
	private String retrieveCode;
	private Long orderBy;
	private String enterpriseCode;
	private String lastModifiedBy;
	private Date lastModifiedDate;

	// Constructors

	/** default constructor */
	public HrCEducation() {
	}

	/** minimal constructor */
	public HrCEducation(Long educationId) {
		this.educationId = educationId;
	}

	/** full constructor */
	public HrCEducation(Long educationId, String educationName, String isUse,
			String retrieveCode, Long orderBy, String enterpriseCode,
			String lastModifiedBy, Date lastModifiedDate) {
		this.educationId = educationId;
		this.educationName = educationName;
		this.isUse = isUse;
		this.retrieveCode = retrieveCode;
		this.orderBy = orderBy;
		this.enterpriseCode = enterpriseCode;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
	}

	// Property accessors
	@Id
	@Column(name = "EDUCATION_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEducationId() {
		return this.educationId;
	}

	public void setEducationId(Long educationId) {
		this.educationId = educationId;
	}

	@Column(name = "EDUCATION_NAME", length = 100)
	public String getEducationName() {
		return this.educationName;
	}

	public void setEducationName(String educationName) {
		this.educationName = educationName;
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