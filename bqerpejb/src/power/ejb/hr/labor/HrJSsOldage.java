package power.ejb.hr.labor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrJSsOldage entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_SS_OLDAGE")
public class HrJSsOldage implements java.io.Serializable {

	// Fields

	private Long detailId;
	private Long mainId;
	private String personnelCode;
	private String personelName;
	private String deptName;
	private String banzuName;
	private Double halfYearSalary;
	private Double monthBase;
	private String selfSign;
	private String memo;
	private String isUse;
	private String enterpriseCode;
	private Double deductCriterion;
	private Double monthStandard; //add by fyyang 20100722企业月标准

	// Constructors

	/** default constructor */
	public HrJSsOldage() {
	}

	/** minimal constructor */
	public HrJSsOldage(Long detailId) {
		this.detailId = detailId;
	}

	/** full constructor */
	public HrJSsOldage(Long detailId, Long mainId, String personnelCode,
			String personelName, String deptName, String banzuName,
			Double halfYearSalary, Double monthBase, String selfSign,
			String memo, String isUse, String enterpriseCode,
			Double deductCriterion,Double monthStandard) {
		this.detailId = detailId;
		this.mainId = mainId;
		this.personnelCode = personnelCode;
		this.personelName = personelName;
		this.deptName = deptName;
		this.banzuName = banzuName;
		this.halfYearSalary = halfYearSalary;
		this.monthBase = monthBase;
		this.selfSign = selfSign;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.deductCriterion = deductCriterion;
		this.monthStandard=monthStandard;
	}

	// Property accessors
	@Id
	@Column(name = "DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDetailId() {
		return this.detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	@Column(name = "MAIN_ID", precision = 10, scale = 0)
	public Long getMainId() {
		return this.mainId;
	}

	public void setMainId(Long mainId) {
		this.mainId = mainId;
	}

	@Column(name = "PERSONNEL_CODE", length = 30)
	public String getPersonnelCode() {
		return this.personnelCode;
	}

	public void setPersonnelCode(String personnelCode) {
		this.personnelCode = personnelCode;
	}

	@Column(name = "PERSONEL_NAME", length = 30)
	public String getPersonelName() {
		return this.personelName;
	}

	public void setPersonelName(String personelName) {
		this.personelName = personelName;
	}

	@Column(name = "DEPT_NAME", length = 30)
	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Column(name = "BANZU_NAME", length = 30)
	public String getBanzuName() {
		return this.banzuName;
	}

	public void setBanzuName(String banzuName) {
		this.banzuName = banzuName;
	}

	@Column(name = "HALF_YEAR_SALARY", precision = 15, scale = 4)
	public Double getHalfYearSalary() {
		return this.halfYearSalary;
	}

	public void setHalfYearSalary(Double halfYearSalary) {
		this.halfYearSalary = halfYearSalary;
	}

	@Column(name = "MONTH_BASE", precision = 15, scale = 4)
	public Double getMonthBase() {
		return this.monthBase;
	}

	public void setMonthBase(Double monthBase) {
		this.monthBase = monthBase;
	}

	@Column(name = "SELF_SIGN", length = 30)
	public String getSelfSign() {
		return this.selfSign;
	}

	public void setSelfSign(String selfSign) {
		this.selfSign = selfSign;
	}

	@Column(name = "MEMO", length = 300)
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

	@Column(name = "DEDUCT_CRITERION", precision = 15, scale = 4)
	public Double getDeductCriterion() {
		return this.deductCriterion;
	}

	public void setDeductCriterion(Double deductCriterion) {
		this.deductCriterion = deductCriterion;
	}

	@Column(name = "MONTH_STANDARD", precision = 15, scale = 4)
	public Double getMonthStandard() {
		return monthStandard;
	}

	public void setMonthStandard(Double monthStandard) {
		this.monthStandard = monthStandard;
	}

}