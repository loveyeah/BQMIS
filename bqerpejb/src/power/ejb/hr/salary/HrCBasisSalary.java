package power.ejb.hr.salary;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCBasisSalary entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_BASIS_SALARY")
public class HrCBasisSalary implements java.io.Serializable {

	// Fields

	private Long basisSalaryId;
	private Double basisSalary;
	private Date effectStartTime;
	private Date effectEndTime;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCBasisSalary() {
	}

	/** minimal constructor */
	public HrCBasisSalary(Long basisSalaryId) {
		this.basisSalaryId = basisSalaryId;
	}

	/** full constructor */
	public HrCBasisSalary(Long basisSalaryId, Double basisSalary,
			Date effectStartTime, Date effectEndTime, String memo,
			String isUse, String enterpriseCode) {
		this.basisSalaryId = basisSalaryId;
		this.basisSalary = basisSalary;
		this.effectStartTime = effectStartTime;
		this.effectEndTime = effectEndTime;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BASIS_SALARY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBasisSalaryId() {
		return this.basisSalaryId;
	}

	public void setBasisSalaryId(Long basisSalaryId) {
		this.basisSalaryId = basisSalaryId;
	}

	@Column(name = "BASIS_SALARY", precision = 10)
	public Double getBasisSalary() {
		return this.basisSalary;
	}

	public void setBasisSalary(Double basisSalary) {
		this.basisSalary = basisSalary;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECT_START_TIME", length = 7)
	public Date getEffectStartTime() {
		return this.effectStartTime;
	}

	public void setEffectStartTime(Date effectStartTime) {
		this.effectStartTime = effectStartTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECT_END_TIME", length = 7)
	public Date getEffectEndTime() {
		return this.effectEndTime;
	}

	public void setEffectEndTime(Date effectEndTime) {
		this.effectEndTime = effectEndTime;
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