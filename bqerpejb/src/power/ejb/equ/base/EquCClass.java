package power.ejb.equ.base;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCClass entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_C_CLASS")
public class EquCClass implements java.io.Serializable {

	// Fields

	private Long equClassId;
	private String classCode;
	private String className;
	private String classLevel;
	private String remark;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCClass() {
	}

	/** minimal constructor */
	public EquCClass(Long equClassId, String classCode) {
		this.equClassId = equClassId;
		this.classCode = classCode;
	}

	/** full constructor */
	public EquCClass(Long equClassId, String classCode, String className,
			String classLevel, String remark, String enterpriseCode,
			String isUse) {
		this.equClassId = equClassId;
		this.classCode = classCode;
		this.className = className;
		this.classLevel = classLevel;
		this.remark = remark;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "EQU_CLASS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEquClassId() {
		return this.equClassId;
	}

	public void setEquClassId(Long equClassId) {
		this.equClassId = equClassId;
	}

	@Column(name = "CLASS_CODE", nullable = false, length = 3)
	public String getClassCode() {
		return this.classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	@Column(name = "CLASS_NAME", length = 100)
	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Column(name = "CLASS_LEVEL", length = 2)
	public String getClassLevel() {
		return this.classLevel;
	}

	public void setClassLevel(String classLevel) {
		this.classLevel = classLevel;
	}

	@Column(name = "REMARK", length = 200)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
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

}