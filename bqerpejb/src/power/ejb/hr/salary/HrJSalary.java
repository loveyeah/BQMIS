package power.ejb.hr.salary;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJSalary entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_SALARY")
public class HrJSalary implements java.io.Serializable {

	// Fields

	private Long salaryId;
	private Long empId;
	private Date salaryMonth;
	private Double totalSalary;
	private Double adjust;
	private String modifyBy;
	private Date modifyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJSalary() {
	}

	/** minimal constructor */
	public HrJSalary(Long salaryId) {
		this.salaryId = salaryId;
	}

	/** full constructor */
	public HrJSalary(Long salaryId, Long empId, Date salaryMonth,
			Double totalSalary, Double adjust, String modifyBy,
			Date modifyDate, String isUse, String enterpriseCode) {
		this.salaryId = salaryId;
		this.empId = empId;
		this.salaryMonth = salaryMonth;
		this.totalSalary = totalSalary;
		this.adjust = adjust;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SALARY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSalaryId() {
		return this.salaryId;
	}

	public void setSalaryId(Long salaryId) {
		this.salaryId = salaryId;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "SALARY_MONTH", length = 7)
	public Date getSalaryMonth() {
		return this.salaryMonth;
	}

	public void setSalaryMonth(Date salaryMonth) {
		this.salaryMonth = salaryMonth;
	}

	@Column(name = "TOTAL_SALARY", precision = 10)
	public Double getTotalSalary() {
		return this.totalSalary;
	}

	public void setTotalSalary(Double totalSalary) {
		this.totalSalary = totalSalary;
	}

	@Column(name = "ADJUST", precision = 10)
	public Double getAdjust() {
		return this.adjust;
	}

	public void setAdjust(Double adjust) {
		this.adjust = adjust;
	}

	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
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