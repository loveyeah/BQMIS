//package power.ejb.hr;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
///**
// * HrCEmpType entity.
// * 
// * @author MyEclipse Persistence Tools
// */
//@Entity
//@Table(name = "HR_C_EMP_TYPE")
//public class HrCEmpType implements java.io.Serializable {
//
//	// Fields
//
//	private Long empTypeId;
//	private String empTypeName;
//	private String isUse;
//	private String retrieveCode;
//
//	// Constructors
//
//	/** default constructor */
//	public HrCEmpType() {
//	}
//
//	/** minimal constructor */
//	public HrCEmpType(Long empTypeId) {
//		this.empTypeId = empTypeId;
//	}
//
//	/** full constructor */
//	public HrCEmpType(Long empTypeId, String empTypeName, String isUse,
//			String retrieveCode) {
//		this.empTypeId = empTypeId;
//		this.empTypeName = empTypeName;
//		this.isUse = isUse;
//		this.retrieveCode = retrieveCode;
//	}
//
//	// Property accessors
//	@Id
//	@Column(name = "EMP_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
//	public Long getEmpTypeId() {
//		return this.empTypeId;
//	}
//
//	public void setEmpTypeId(Long empTypeId) {
//		this.empTypeId = empTypeId;
//	}
//
//	@Column(name = "EMP_TYPE_NAME", length = 100)
//	public String getEmpTypeName() {
//		return this.empTypeName;
//	}
//
//	public void setEmpTypeName(String empTypeName) {
//		this.empTypeName = empTypeName;
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
 * HrCEmpType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_EMP_TYPE")
public class HrCEmpType implements java.io.Serializable {

	// Fields

	private Long empTypeId;
	private String empTypeName;
	private String isUse;
	private String retrieveCode;
	private Long orderBy;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCEmpType() {
	}

	/** minimal constructor */
	public HrCEmpType(Long empTypeId) {
		this.empTypeId = empTypeId;
	}

	/** full constructor */
	public HrCEmpType(Long empTypeId, String empTypeName, String isUse,
			String retrieveCode, Long orderBy, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode) {
		this.empTypeId = empTypeId;
		this.empTypeName = empTypeName;
		this.isUse = isUse;
		this.retrieveCode = retrieveCode;
		this.orderBy = orderBy;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "EMP_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEmpTypeId() {
		return this.empTypeId;
	}

	public void setEmpTypeId(Long empTypeId) {
		this.empTypeId = empTypeId;
	}

	@Column(name = "EMP_TYPE_NAME", length = 100)
	public String getEmpTypeName() {
		return this.empTypeName;
	}

	public void setEmpTypeName(String empTypeName) {
		this.empTypeName = empTypeName;
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