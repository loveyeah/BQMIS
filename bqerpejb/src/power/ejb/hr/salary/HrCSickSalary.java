package power.ejb.hr.salary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCSickSalary entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_SICK_SALARY")
public class HrCSickSalary implements java.io.Serializable {

	// Fields

	private Long sickSalaryId;
	private Long factWorkyearBottom;
	private Long factWorkyearTop;
	private Long localWorkageBottom;
	private Long localWorkageTop;
	private Double salaryScale;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCSickSalary() {
	}

	/** minimal constructor */
	public HrCSickSalary(Long sickSalaryId) {
		this.sickSalaryId = sickSalaryId;
	}

	/** full constructor */
	public HrCSickSalary(Long sickSalaryId, Long factWorkyearBottom,
			Long factWorkyearTop, Long localWorkageBottom,
			Long localWorkageTop, Double salaryScale, String memo,
			String isUse, String enterpriseCode) {
		this.sickSalaryId = sickSalaryId;
		this.factWorkyearBottom = factWorkyearBottom;
		this.factWorkyearTop = factWorkyearTop;
		this.localWorkageBottom = localWorkageBottom;
		this.localWorkageTop = localWorkageTop;
		this.salaryScale = salaryScale;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SICK_SALARY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSickSalaryId() {
		return this.sickSalaryId;
	}

	public void setSickSalaryId(Long sickSalaryId) {
		this.sickSalaryId = sickSalaryId;
	}

	@Column(name = "FACT_WORKYEAR_BOTTOM", precision = 2, scale = 0)
	public Long getFactWorkyearBottom() {
		return this.factWorkyearBottom;
	}

	public void setFactWorkyearBottom(Long factWorkyearBottom) {
		this.factWorkyearBottom = factWorkyearBottom;
	}

	@Column(name = "FACT_WORKYEAR_TOP", precision = 2, scale = 0)
	public Long getFactWorkyearTop() {
		return this.factWorkyearTop;
	}

	public void setFactWorkyearTop(Long factWorkyearTop) {
		this.factWorkyearTop = factWorkyearTop;
	}

	@Column(name = "LOCAL_WORKAGE_BOTTOM", precision = 2, scale = 0)
	public Long getLocalWorkageBottom() {
		return this.localWorkageBottom;
	}

	public void setLocalWorkageBottom(Long localWorkageBottom) {
		this.localWorkageBottom = localWorkageBottom;
	}

	@Column(name = "LOCAL_WORKAGE_TOP", precision = 2, scale = 0)
	public Long getLocalWorkageTop() {
		return this.localWorkageTop;
	}

	public void setLocalWorkageTop(Long localWorkageTop) {
		this.localWorkageTop = localWorkageTop;
	}

	@Column(name = "SALARY_SCALE", precision = 10, scale = 4)
	public Double getSalaryScale() {
		return this.salaryScale;
	}

	public void setSalaryScale(Double salaryScale) {
		this.salaryScale = salaryScale;
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