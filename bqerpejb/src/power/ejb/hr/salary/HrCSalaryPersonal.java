package power.ejb.hr.salary;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCSalaryPersonal entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_SALARY_PERSONAL")
public class HrCSalaryPersonal implements java.io.Serializable {

	// Fields

	private Long salaryPersonalId;
	private Long empId;
	private Long deptId;
	private Date workingFrom;
	private Date joinFrom;
	private Long runningAge;
	private Double remainSalary;
	private Double pointSalaryAdjust;
	private String memo;
	private String isUse;
	private String enterpriseCode;
	private Double monthAward;
	private  Double  salaryPoint;//  add by wpzhu 20100708
	private  Long  quentityId;
	private  String runAgeFlag;
	private  Date  lastJoinRuntime;
	
	@Column(name = "RUNAGE_FLAG", length = 1)
	public String getRunAgeFlag() {
		return runAgeFlag;
	}

	public void setRunAgeFlag(String runAgeFlag) {
		this.runAgeFlag = runAgeFlag;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_JION_RUNTIME", length = 7)
	public Date getLastJoinRuntime() {
		return lastJoinRuntime;
	}

	public void setLastJoinRuntime(Date lastJoinRuntime) {
		this.lastJoinRuntime = lastJoinRuntime;
	}

	// Constructors
	@Column(name = "SALARY_POINT", precision = 10, scale = 2)
	public Double getSalaryPoint() {
		return salaryPoint;
	}

	public void setSalaryPoint(Double salaryPoint) {
		this.salaryPoint = salaryPoint;
	}
     
	@Column(name = "QUANTIFY_ID",  precision = 10, scale = 0)
	public Long getQuentityId() {
		return quentityId;
	}

	public void setQuentityId(Long quentityId) {
		this.quentityId = quentityId;
	}

	/** default constructor */
	public HrCSalaryPersonal() {
	}

	/** minimal constructor */
	public HrCSalaryPersonal(Long salaryPersonalId) {
		this.salaryPersonalId = salaryPersonalId;
	}
	// Property accessors
	@Id
	@Column(name = "SALARY_PERSONAL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSalaryPersonalId() {
		return this.salaryPersonalId;
	}

	public void setSalaryPersonalId(Long salaryPersonalId) {
		this.salaryPersonalId = salaryPersonalId;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "DEPT_ID", precision = 10, scale = 0)
	public Long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "WORKING_FROM", length = 7)
	public Date getWorkingFrom() {
		return this.workingFrom;
	}

	public void setWorkingFrom(Date workingFrom) {
		this.workingFrom = workingFrom;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "JOIN_FROM", length = 7)
	public Date getJoinFrom() {
		return this.joinFrom;
	}

	public void setJoinFrom(Date joinFrom) {
		this.joinFrom = joinFrom;
	}

	@Column(name = "RUNNING_AGE", precision = 2, scale = 0)
	public Long getRunningAge() {
		return this.runningAge;
	}

	public void setRunningAge(Long runningAge) {
		this.runningAge = runningAge;
	}

	@Column(name = "REMAIN_SALARY", precision = 10)
	public Double getRemainSalary() {
		return this.remainSalary;
	}

	public void setRemainSalary(Double remainSalary) {
		this.remainSalary = remainSalary;
	}

	@Column(name = "POINT_SALARY_ADJUST", precision = 10, scale = 4)
	public Double getPointSalaryAdjust() {
		return this.pointSalaryAdjust;
	}

	public void setPointSalaryAdjust(Double pointSalaryAdjust) {
		this.pointSalaryAdjust = pointSalaryAdjust;
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

	@Column(name = "MONTH_AWARD", precision = 10)
	public Double getMonthAward() {
		return monthAward;
	}

	public void setMonthAward(Double monthAward) {
		this.monthAward = monthAward;
	}
}