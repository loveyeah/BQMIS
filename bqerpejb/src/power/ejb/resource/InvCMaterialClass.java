package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvCMaterialClass entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_C_MATERIAL_CLASS")
public class InvCMaterialClass implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long maertialClassId;
	private String parentClassNo;
	private String classNo;
	private String className;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public InvCMaterialClass() {
	}

	/** full constructor */
	public InvCMaterialClass(Long maertialClassId, String parentClassNo,
			String classNo, String className, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.maertialClassId = maertialClassId;
		this.parentClassNo = parentClassNo;
		this.classNo = classNo;
		this.className = className;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "MAERTIAL_CLASS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMaertialClassId() {
		return this.maertialClassId;
	}

	public void setMaertialClassId(Long maertialClassId) {
		this.maertialClassId = maertialClassId;
	}

	@Column(name = "PARENT_CLASS_NO", nullable = false, length = 30)
	public String getParentClassNo() {
		return this.parentClassNo;
	}

	public void setParentClassNo(String parentClassNo) {
		this.parentClassNo = parentClassNo;
	}

	@Column(name = "CLASS_NO", nullable = false, length = 30)
	public String getClassNo() {
		return this.classNo;
	}

	public void setClassNo(String classNo) {
		this.classNo = classNo;
	}

	@Column(name = "CLASS_NAME", nullable = false, length = 100)
	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
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