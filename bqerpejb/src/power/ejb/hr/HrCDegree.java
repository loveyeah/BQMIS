//package power.ejb.hr;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
///**
// * HrCDegree entity.
// * 
// * @author MyEclipse Persistence Tools
// */
//@Entity
//@Table(name = "HR_C_DEGREE", schema = "power")
//public class HrCDegree implements java.io.Serializable {
//
//	// Fields
//
//	private Long degreeId;
//	private String degreeName;
//	private String isUse;
//	private String retrieveCode;
//
//	// Constructors
//
//	/** default constructor */
//	public HrCDegree() {
//	}
//
//	/** minimal constructor */
//	public HrCDegree(Long degreeId) {
//		this.degreeId = degreeId;
//	}
//
//	/** full constructor */
//	public HrCDegree(Long degreeId, String degreeName, String isUse,
//			String retrieveCode) {
//		this.degreeId = degreeId;
//		this.degreeName = degreeName;
//		this.isUse = isUse;
//		this.retrieveCode = retrieveCode;
//	}
//
//	// Property accessors
//	@Id
//	@Column(name = "DEGREE_ID", unique = true, nullable = false, precision = 10, scale = 0)
//	public Long getDegreeId() {
//		return this.degreeId;
//	}
//
//	public void setDegreeId(Long degreeId) {
//		this.degreeId = degreeId;
//	}
//
//	@Column(name = "DEGREE_NAME", length = 100)
//	public String getDegreeName() {
//		return this.degreeName;
//	}
//
//	public void setDegreeName(String degreeName) {
//		this.degreeName = degreeName;
//	}
//
//	@Column(name = "IS_USE", length = 1)
//	public String getIsUse() {
//		return this.isUse;
//	}
//
//	public void setIsUse(String isUse) {
//		this.isUse = isUse;
//	}
//
//	@Column(name = "RETRIEVE_CODE", length = 20)
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
 * HrCDegree entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_DEGREE")
public class HrCDegree implements java.io.Serializable {

	// Fields

	private Long degreeId;
	private String degreeName;
	private String isUse;
	private String retrieveCode;
	private Long orderBy;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCDegree() {
	}

	/** minimal constructor */
	public HrCDegree(Long degreeId) {
		this.degreeId = degreeId;
	}

	/** full constructor */
	public HrCDegree(Long degreeId, String degreeName, String isUse,
			String retrieveCode, Long orderBy, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode) {
		this.degreeId = degreeId;
		this.degreeName = degreeName;
		this.isUse = isUse;
		this.retrieveCode = retrieveCode;
		this.orderBy = orderBy;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DEGREE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDegreeId() {
		return this.degreeId;
	}

	public void setDegreeId(Long degreeId) {
		this.degreeId = degreeId;
	}

	@Column(name = "DEGREE_NAME", length = 100)
	public String getDegreeName() {
		return this.degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
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

}