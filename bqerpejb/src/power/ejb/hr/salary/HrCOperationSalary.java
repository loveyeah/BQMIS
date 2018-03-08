package power.ejb.hr.salary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCOperationSalary entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_OPERATION_SALARY")
public class HrCOperationSalary implements java.io.Serializable {

	// Fields

	private Long operationSalaryId;
	private Long startSeniority;
	private Long endSeniority;
	private Double operationSalaryPoint;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCOperationSalary() {
	}

	/** minimal constructor */
	public HrCOperationSalary(Long operationSalaryId) {
		this.operationSalaryId = operationSalaryId;
	}

	/** full constructor */
	public HrCOperationSalary(Long operationSalaryId, Long startSeniority,
			Long endSeniority, Double operationSalaryPoint, String memo,
			String isUse, String enterpriseCode) {
		this.operationSalaryId = operationSalaryId;
		this.startSeniority = startSeniority;
		this.endSeniority = endSeniority;
		this.operationSalaryPoint = operationSalaryPoint;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "OPERATION_SALARY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getOperationSalaryId() {
		return this.operationSalaryId;
	}

	public void setOperationSalaryId(Long operationSalaryId) {
		this.operationSalaryId = operationSalaryId;
	}

	@Column(name = "START_SENIORITY", precision = 2, scale = 0)
	public Long getStartSeniority() {
		return this.startSeniority;
	}

	public void setStartSeniority(Long startSeniority) {
		this.startSeniority = startSeniority;
	}

	@Column(name = "END_SENIORITY", precision = 2, scale = 0)
	public Long getEndSeniority() {
		return this.endSeniority;
	}

	public void setEndSeniority(Long endSeniority) {
		this.endSeniority = endSeniority;
	}

	@Column(name = "OPERATION_SALARY_POINT", precision = 10)
	public Double getOperationSalaryPoint() {
		return this.operationSalaryPoint;
	}

	public void setOperationSalaryPoint(Double operationSalaryPoint) {
		this.operationSalaryPoint = operationSalaryPoint;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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