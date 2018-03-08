package power.ejb.hr.salary;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCSalaryPoint entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_SALARY_POINT")
public class HrCSalaryPoint implements java.io.Serializable {

	// Fields

	private Long salaryPointId;
	private Double salaryPoint;
	private Date effectStartTime;
	private Date effectEndTime;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCSalaryPoint() {
	}

	/** minimal constructor */
	public HrCSalaryPoint(Long salaryPointId) {
		this.salaryPointId = salaryPointId;
	}

	/** full constructor */
	public HrCSalaryPoint(Long salaryPointId, Double salaryPoint,
			Date effectStartTime, Date effectEndTime, String memo,
			String isUse, String enterpriseCode) {
		this.salaryPointId = salaryPointId;
		this.salaryPoint = salaryPoint;
		this.effectStartTime = effectStartTime;
		this.effectEndTime = effectEndTime;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SALARY_POINT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSalaryPointId() {
		return this.salaryPointId;
	}

	public void setSalaryPointId(Long salaryPointId) {
		this.salaryPointId = salaryPointId;
	}

	@Column(name = "SALARY_POINT", precision = 10)
	public Double getSalaryPoint() {
		return this.salaryPoint;
	}

	public void setSalaryPoint(Double salaryPoint) {
		this.salaryPoint = salaryPoint;
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