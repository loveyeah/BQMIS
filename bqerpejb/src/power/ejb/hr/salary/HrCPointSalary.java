package power.ejb.hr.salary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCPointSalary entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_POINT_SALARY")
public class HrCPointSalary implements java.io.Serializable {

	// Fields

	private Long pointSalaryId;
	private Long checkStationGrade;
	private Long salaryLevel;
	private Double jobPoint;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCPointSalary() {
	}

	/** minimal constructor */
	public HrCPointSalary(Long pointSalaryId) {
		this.pointSalaryId = pointSalaryId;
	}

	/** full constructor */
	public HrCPointSalary(Long pointSalaryId, Long checkStationGrade,
			Long salaryLevel, Double jobPoint, String memo, String isUse,
			String enterpriseCode) {
		this.pointSalaryId = pointSalaryId;
		this.checkStationGrade = checkStationGrade;
		this.salaryLevel = salaryLevel;
		this.jobPoint = jobPoint;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "POINT_SALARY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPointSalaryId() {
		return this.pointSalaryId;
	}

	public void setPointSalaryId(Long pointSalaryId) {
		this.pointSalaryId = pointSalaryId;
	}

	@Column(name = "CHECK_STATION_GRADE", precision = 10, scale = 0)
	public Long getCheckStationGrade() {
		return this.checkStationGrade;
	}

	public void setCheckStationGrade(Long checkStationGrade) {
		this.checkStationGrade = checkStationGrade;
	}

	@Column(name = "SALARY_LEVEL", precision = 10, scale = 0)
	public Long getSalaryLevel() {
		return this.salaryLevel;
	}

	public void setSalaryLevel(Long salaryLevel) {
		this.salaryLevel = salaryLevel;
	}

	@Column(name = "JOB_POINT", precision = 10)
	public Double getJobPoint() {
		return this.jobPoint;
	}

	public void setJobPoint(Double jobPoint) {
		this.jobPoint = jobPoint;
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