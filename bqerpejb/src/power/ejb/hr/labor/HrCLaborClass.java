package power.ejb.hr.labor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCLaborClass entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_LABOR_CLASS")
public class HrCLaborClass implements java.io.Serializable {

	// Fields

	private Long laborClassId;
	private String laborClassCode;
	private String laborClassName;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public HrCLaborClass() {
	}

	/** minimal constructor */
	public HrCLaborClass(Long laborClassId) {
		this.laborClassId = laborClassId;
	}

	/** full constructor */
	public HrCLaborClass(Long laborClassId, String laborClassCode,
			String laborClassName, String enterpriseCode, String isUse) {
		this.laborClassId = laborClassId;
		this.laborClassCode = laborClassCode;
		this.laborClassName = laborClassName;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "LABOR_CLASS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getLaborClassId() {
		return this.laborClassId;
	}

	public void setLaborClassId(Long laborClassId) {
		this.laborClassId = laborClassId;
	}

	@Column(name = "LABOR_CLASS_CODE", length = 20)
	public String getLaborClassCode() {
		return this.laborClassCode;
	}

	public void setLaborClassCode(String laborClassCode) {
		this.laborClassCode = laborClassCode;
	}

	@Column(name = "LABOR_CLASS_NAME", length = 50)
	public String getLaborClassName() {
		return this.laborClassName;
	}

	public void setLaborClassName(String laborClassName) {
		this.laborClassName = laborClassName;
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