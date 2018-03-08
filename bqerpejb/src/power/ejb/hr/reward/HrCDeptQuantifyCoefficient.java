package power.ejb.hr.reward;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCDeptQuantifyCoefficient entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_DEPT_QUANTIFY_COEFFICIENT")
public class HrCDeptQuantifyCoefficient implements java.io.Serializable {

	// Fields

	private Long coefficientId;
	private String coefficientMonth;
	private Long deptId;
	private Double quantifyCoefficient;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCDeptQuantifyCoefficient() {
	}

	/** minimal constructor */
	public HrCDeptQuantifyCoefficient(Long coefficientId) {
		this.coefficientId = coefficientId;
	}

	/** full constructor */
	public HrCDeptQuantifyCoefficient(Long coefficientId,
			String coefficientMonth, Long deptId, Double quantifyCoefficient,
			String isUse, String enterpriseCode) {
		this.coefficientId = coefficientId;
		this.coefficientMonth = coefficientMonth;
		this.deptId = deptId;
		this.quantifyCoefficient = quantifyCoefficient;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "COEFFICIENT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCoefficientId() {
		return this.coefficientId;
	}

	public void setCoefficientId(Long coefficientId) {
		this.coefficientId = coefficientId;
	}

	@Column(name = "COEFFICIENT_MONTH", length = 10)
	public String getCoefficientMonth() {
		return this.coefficientMonth;
	}

	public void setCoefficientMonth(String coefficientMonth) {
		this.coefficientMonth = coefficientMonth;
	}

	@Column(name = "DEPT_ID", precision = 10, scale = 0)
	public Long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Column(name = "QUANTIFY_COEFFICIENT", precision = 15, scale = 4)
	public Double getQuantifyCoefficient() {
		return this.quantifyCoefficient;
	}

	public void setQuantifyCoefficient(Double quantifyCoefficient) {
		this.quantifyCoefficient = quantifyCoefficient;
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