package power.ejb.run.securityproduction.safesupervise;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJViolationRule entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_VIOLATION_RULE")
public class SpJViolationRule implements java.io.Serializable {

	// Fields

	private Long ruleId;
	private String empCode;
	private String deptCode;
	private Date examineDate;
	private Double examineMoney;
	private String phenomenon;
	private String checkBy;
	private Date entryDate;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJViolationRule() {
	}

	/** minimal constructor */
	public SpJViolationRule(Long ruleId) {
		this.ruleId = ruleId;
	}

	/** full constructor */
	public SpJViolationRule(Long ruleId, String empCode, String deptCode,
			Date examineDate, Double examineMoney, String phenomenon,
			String checkBy, Date entryDate, String lastModifiedBy,
			Date lastModifiedDate, String isUse, String enterpriseCode) {
		this.ruleId = ruleId;
		this.empCode = empCode;
		this.deptCode = deptCode;
		this.examineDate = examineDate;
		this.examineMoney = examineMoney;
		this.phenomenon = phenomenon;
		this.checkBy = checkBy;
		this.entryDate = entryDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "RULE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRuleId() {
		return this.ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	@Column(name = "EMP_CODE", length = 30)
	public String getEmpCode() {
		return this.empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	@Column(name = "DEPT_CODE", length = 20)
	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EXAMINE_DATE", length = 7)
	public Date getExamineDate() {
		return this.examineDate;
	}

	public void setExamineDate(Date examineDate) {
		this.examineDate = examineDate;
	}

	@Column(name = "EXAMINE_MONEY", precision = 10)
	public Double getExamineMoney() {
		return this.examineMoney;
	}

	public void setExamineMoney(Double examineMoney) {
		this.examineMoney = examineMoney;
	}

	@Column(name = "PHENOMENON", length = 400)
	public String getPhenomenon() {
		return this.phenomenon;
	}

	public void setPhenomenon(String phenomenon) {
		this.phenomenon = phenomenon;
	}

	@Column(name = "CHECK_BY", length = 30)
	public String getCheckBy() {
		return this.checkBy;
	}

	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 20)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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