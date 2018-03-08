//package power.ejb.hr;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
///**
// * HrJEmpPhoto entity.
// * 
// * @author MyEclipse Persistence Tools
// */
//@Entity
//@Table(name = "HR_J_EMP_PHOTO", schema = "power")
//public class HrJEmpPhoto implements java.io.Serializable {
//
//	// Fields
//
//	private Long empId;
//	private byte[] photo;
//
//	// Constructors
//
//	/** default constructor */
//	public HrJEmpPhoto() {
//	}
//
//	/** minimal constructor */
//	public HrJEmpPhoto(Long empId) {
//		this.empId = empId;
//	}
//
//	/** full constructor */
//	public HrJEmpPhoto(Long empId, byte[] photo) {
//		this.empId = empId;
//		this.photo = photo;
//	}
//
//	// Property accessors
//	@Id
//	@Column(name = "EMP_ID", unique = true, nullable = false, precision = 10, scale = 0)
//	public Long getEmpId() {
//		return this.empId;
//	}
//
//	public void setEmpId(Long empId) {
//		this.empId = empId;
//	}
//
//	@Column(name = "PHOTO")
//	public byte[] getPhoto() {
//		return this.photo;
//	}
//
//	public void setPhoto(byte[] photo) {
//		this.photo = photo;
//	}
//
//}


// add by liuyi 091125 
package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJEmpPhoto entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_EMP_PHOTO")
public class HrJEmpPhoto implements java.io.Serializable {

	// Fields

	private Long empId;
	private byte[] photo;
	private String enterpriseCode;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String isUse;

	// Constructors

	/** default constructor */
	public HrJEmpPhoto() {
	}

	/** minimal constructor */
	public HrJEmpPhoto(Long empId) {
		this.empId = empId;
	}

	/** full constructor */
	public HrJEmpPhoto(Long empId, byte[] photo, String enterpriseCode,
			String lastModifiedBy, Date lastModifiedDate, String isUse) {
		this.empId = empId;
		this.photo = photo;
		this.enterpriseCode = enterpriseCode;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "EMP_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "PHOTO")
	public byte[] getPhoto() {
		return this.photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
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

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}