package power.ejb.hr.salary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCSalaryLevel entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_SALARY_LEVEL")
public class HrCSalaryLevel implements java.io.Serializable {

	// Fields

	private Long salaryLevel;
	private String salaryLevelName;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCSalaryLevel() {
	}

	/** minimal constructor */
	public HrCSalaryLevel(Long salaryLevel) {
		this.salaryLevel = salaryLevel;
	}

	/** full constructor */
	public HrCSalaryLevel(Long salaryLevel, String salaryLevelName,
			String isUse, String enterpriseCode) {
		this.salaryLevel = salaryLevel;
		this.salaryLevelName = salaryLevelName;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SALARY_LEVEL", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSalaryLevel() {
		return this.salaryLevel;
	}

	public void setSalaryLevel(Long salaryLevel) {
		this.salaryLevel = salaryLevel;
	}

	@Column(name = "SALARY_LEVEL_NAME", length = 30)
	public String getSalaryLevelName() {
		return this.salaryLevelName;
	}

	public void setSalaryLevelName(String salaryLevelName) {
		this.salaryLevelName = salaryLevelName;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}