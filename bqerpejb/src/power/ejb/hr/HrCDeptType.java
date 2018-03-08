package power.ejb.hr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCDeptType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_DEPT_TYPE", schema = "LANWAN")
public class HrCDeptType implements java.io.Serializable {

	// Fields

	private Long deptTypeId;
	private String deptTypeName;
	private String isUse;
	private String retrieveCode;

	// Constructors

	/** default constructor */
	public HrCDeptType() {
	}

	/** minimal constructor */
	public HrCDeptType(Long deptTypeId) {
		this.deptTypeId = deptTypeId;
	}

	/** full constructor */
	public HrCDeptType(Long deptTypeId, String deptTypeName, String isUse,
			String retrieveCode) {
		this.deptTypeId = deptTypeId;
		this.deptTypeName = deptTypeName;
		this.isUse = isUse;
		this.retrieveCode = retrieveCode;
	}

	// Property accessors
	@Id
	@Column(name = "DEPT_TYPE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDeptTypeId() {
		return this.deptTypeId;
	}

	public void setDeptTypeId(Long deptTypeId) {
		this.deptTypeId = deptTypeId;
	}

	@Column(name = "DEPT_TYPE_NAME", length = 100)
	public String getDeptTypeName() {
		return this.deptTypeName;
	}

	public void setDeptTypeName(String deptTypeName) {
		this.deptTypeName = deptTypeName;
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

}