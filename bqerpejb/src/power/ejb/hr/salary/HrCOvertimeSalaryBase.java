package power.ejb.hr.salary;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCOvertimeSalaryBase entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_OVERTIME_SALARY_BASE")
public class HrCOvertimeSalaryBase implements java.io.Serializable {

	// Fields

	private Long overtimeSalaryBaseId;
	private Double overtimeSalaryBase;
	private Date effectStartTime;
	private Date effectEndTime;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCOvertimeSalaryBase() {
	}

	/** minimal constructor */
	public HrCOvertimeSalaryBase(Long overtimeSalaryBaseId) {
		this.overtimeSalaryBaseId = overtimeSalaryBaseId;
	}

	/** full constructor */
	public HrCOvertimeSalaryBase(Long overtimeSalaryBaseId,
			Double overtimeSalaryBase, Date effectStartTime,
			Date effectEndTime, String memo, String isUse, String enterpriseCode) {
		this.overtimeSalaryBaseId = overtimeSalaryBaseId;
		this.overtimeSalaryBase = overtimeSalaryBase;
		this.effectStartTime = effectStartTime;
		this.effectEndTime = effectEndTime;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "OVERTIME_SALARY_BASE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getOvertimeSalaryBaseId() {
		return this.overtimeSalaryBaseId;
	}

	public void setOvertimeSalaryBaseId(Long overtimeSalaryBaseId) {
		this.overtimeSalaryBaseId = overtimeSalaryBaseId;
	}

	@Column(name = "OVERTIME_SALARY_BASE", precision = 10)
	public Double getOvertimeSalaryBase() {
		return this.overtimeSalaryBase;
	}

	public void setOvertimeSalaryBase(Double overtimeSalaryBase) {
		this.overtimeSalaryBase = overtimeSalaryBase;
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