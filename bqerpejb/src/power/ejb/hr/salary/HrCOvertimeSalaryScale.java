package power.ejb.hr.salary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCOvertimeSalaryScale entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_OVERTIME_SALARY_SCALE")
public class HrCOvertimeSalaryScale implements java.io.Serializable {

	// Fields

	private Long overtimeSalaryScaleId;
	private String overtimeTypeId;
	private Double overtimeSalaryScale;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCOvertimeSalaryScale() {
	}

	/** minimal constructor */
	public HrCOvertimeSalaryScale(Long overtimeSalaryScaleId) {
		this.overtimeSalaryScaleId = overtimeSalaryScaleId;
	}

	/** full constructor */
	public HrCOvertimeSalaryScale(Long overtimeSalaryScaleId,
			String overtimeTypeId, Double overtimeSalaryScale, String memo,
			String isUse, String enterpriseCode) {
		this.overtimeSalaryScaleId = overtimeSalaryScaleId;
		this.overtimeTypeId = overtimeTypeId;
		this.overtimeSalaryScale = overtimeSalaryScale;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "OVERTIME_SALARY_SCALE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getOvertimeSalaryScaleId() {
		return this.overtimeSalaryScaleId;
	}

	public void setOvertimeSalaryScaleId(Long overtimeSalaryScaleId) {
		this.overtimeSalaryScaleId = overtimeSalaryScaleId;
	}

	@Column(name = "OVERTIME_TYPE_ID", length = 40)
	public String getOvertimeTypeId() {
		return this.overtimeTypeId;
	}

	public void setOvertimeTypeId(String overtimeTypeId) {
		this.overtimeTypeId = overtimeTypeId;
	}

	@Column(name = "OVERTIME_SALARY_SCALE", precision = 10)
	public Double getOvertimeSalaryScale() {
		return this.overtimeSalaryScale;
	}

	public void setOvertimeSalaryScale(Double overtimeSalaryScale) {
		this.overtimeSalaryScale = overtimeSalaryScale;
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