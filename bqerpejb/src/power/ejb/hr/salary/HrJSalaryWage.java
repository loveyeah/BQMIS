package power.ejb.hr.salary;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJSalaryWage entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_SALARY_WAGE", schema = "POWER")
public class HrJSalaryWage implements java.io.Serializable {

	// Fields

	private Long wageId;
	private Long empId;
	private Date salaryMonth;
	private Double basisWage;
	private Double ageWage;
	private Double operationWage;
	private Double remainWage;
	private Double pointWage;
	private Double overtimeWage;
	private Double deductionWage;
	private Double totalWage;
	private String wageMemo;
//	private Double monthAwardCut;
//	private Double monthAward;
//	private String monthAwardMemo;
//	private Double bigAwardOneCut;
//	private Double bigAwardOne;
//	private Double bigAwardTwoCut;
//	private Double bigAwardTwo;
//	private String bigAwardMemo;
	private String enterpriseCode;
	
//	private Double coefficient;

	// Constructors
	
	//add by sychen 20100805
	private Double others;
	private Double individualAwardsOne;
	private Double individualAwardsTwo;
	private Double monthAwards;
	private Double bigAwards;
	private Double totalIncome;
	

	/** default constructor */
	public HrJSalaryWage() {
	}

	/** minimal constructor */
	public HrJSalaryWage(Long wageId) {
		this.wageId = wageId;
	}

	/** full constructor */
	public HrJSalaryWage(Long wageId, Long empId, Date salaryMonth,
			Double basisWage, Double ageWage, Double operationWage,
			Double remainWage, Double pointWage, Double overtimeWage,
			Double deductionWage, Double totalWage, String wageMemo,
			Double monthAwardCut, Double monthAward, String monthAwardMemo,
			Double bigAwardOneCut, Double bigAwardOne, Double bigAwardTwoCut,
			Double bigAwardTwo, String bigAwardMemo, String enterpriseCode) {
		this.wageId = wageId;
		this.empId = empId;
		this.salaryMonth = salaryMonth;
		this.basisWage = basisWage;
		this.ageWage = ageWage;
		this.operationWage = operationWage;
		this.remainWage = remainWage;
		this.pointWage = pointWage;
		this.overtimeWage = overtimeWage;
		this.deductionWage = deductionWage;
		this.totalWage = totalWage;
		this.wageMemo = wageMemo;
//		this.monthAwardCut = monthAwardCut;
//		this.monthAward = monthAward;
//		this.monthAwardMemo = monthAwardMemo;
//		this.bigAwardOneCut = bigAwardOneCut;
//		this.bigAwardOne = bigAwardOne;
//		this.bigAwardTwoCut = bigAwardTwoCut;
//		this.bigAwardTwo = bigAwardTwo;
//		this.bigAwardMemo = bigAwardMemo;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "WAGE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getWageId() {
		return this.wageId;
	}

	public void setWageId(Long wageId) {
		this.wageId = wageId;
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

	@Column(name = "BASIS_WAGE", precision = 15, scale = 4)
	public Double getBasisWage() {
		return this.basisWage;
	}

	public void setBasisWage(Double basisWage) {
		this.basisWage = basisWage;
	}

	@Column(name = "AGE_WAGE", precision = 15, scale = 4)
	public Double getAgeWage() {
		return this.ageWage;
	}

	public void setAgeWage(Double ageWage) {
		this.ageWage = ageWage;
	}

	@Column(name = "OPERATION_WAGE", precision = 15, scale = 4)
	public Double getOperationWage() {
		return this.operationWage;
	}

	public void setOperationWage(Double operationWage) {
		this.operationWage = operationWage;
	}

	@Column(name = "REMAIN_WAGE", precision = 15, scale = 4)
	public Double getRemainWage() {
		return this.remainWage;
	}

	public void setRemainWage(Double remainWage) {
		this.remainWage = remainWage;
	}

	@Column(name = "POINT_WAGE", precision = 15, scale = 4)
	public Double getPointWage() {
		return this.pointWage;
	}

	public void setPointWage(Double pointWage) {
		this.pointWage = pointWage;
	}

	@Column(name = "OVERTIME_WAGE", precision = 15, scale = 4)
	public Double getOvertimeWage() {
		return this.overtimeWage;
	}

	public void setOvertimeWage(Double overtimeWage) {
		this.overtimeWage = overtimeWage;
	}

	@Column(name = "DEDUCTION_WAGE", precision = 15, scale = 4)
	public Double getDeductionWage() {
		return this.deductionWage;
	}

	public void setDeductionWage(Double deductionWage) {
		this.deductionWage = deductionWage;
	}

	@Column(name = "TOTAL_WAGE", precision = 15, scale = 4)
	public Double getTotalWage() {
		return this.totalWage;
	}

	public void setTotalWage(Double totalWage) {
		this.totalWage = totalWage;
	}

	@Column(name = "WAGE_MEMO", length = 500)
	public String getWageMemo() {
		return this.wageMemo;
	}

	public void setWageMemo(String wageMemo) {
		this.wageMemo = wageMemo;
	}

//	@Column(name = "MONTH_AWARD_CUT", precision = 15, scale = 4)
//	public Double getMonthAwardCut() {
//		return this.monthAwardCut;
//	}
//
//	public void setMonthAwardCut(Double monthAwardCut) {
//		this.monthAwardCut = monthAwardCut;
//	}
//
//	@Column(name = "MONTH_AWARD", precision = 15, scale = 4)
//	public Double getMonthAward() {
//		return this.monthAward;
//	}
//
//	public void setMonthAward(Double monthAward) {
//		this.monthAward = monthAward;
//	}
//
//	@Column(name = "MONTH_AWARD_MEMO", length = 500)
//	public String getMonthAwardMemo() {
//		return this.monthAwardMemo;
//	}
//
//	public void setMonthAwardMemo(String monthAwardMemo) {
//		this.monthAwardMemo = monthAwardMemo;
//	}
//
//	@Column(name = "BIG_AWARD_ONE_CUT", precision = 15, scale = 4)
//	public Double getBigAwardOneCut() {
//		return this.bigAwardOneCut;
//	}
//
//	public void setBigAwardOneCut(Double bigAwardOneCut) {
//		this.bigAwardOneCut = bigAwardOneCut;
//	}
//
//	@Column(name = "BIG_AWARD_ONE", precision = 15, scale = 4)
//	public Double getBigAwardOne() {
//		return this.bigAwardOne;
//	}
//
//	public void setBigAwardOne(Double bigAwardOne) {
//		this.bigAwardOne = bigAwardOne;
//	}
//
//	@Column(name = "BIG_AWARD_TWO_CUT", precision = 15, scale = 4)
//	public Double getBigAwardTwoCut() {
//		return this.bigAwardTwoCut;
//	}
//
//	public void setBigAwardTwoCut(Double bigAwardTwoCut) {
//		this.bigAwardTwoCut = bigAwardTwoCut;
//	}
//
//	@Column(name = "BIG_AWARD_TWO", precision = 15, scale = 4)
//	public Double getBigAwardTwo() {
//		return this.bigAwardTwo;
//	}
//
//	public void setBigAwardTwo(Double bigAwardTwo) {
//		this.bigAwardTwo = bigAwardTwo;
//	}
//
//	@Column(name = "BIG_AWARD_MEMO", length = 500)
//	public String getBigAwardMemo() {
//		return this.bigAwardMemo;
//	}
//
//	public void setBigAwardMemo(String bigAwardMemo) {
//		this.bigAwardMemo = bigAwardMemo;
//	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "OTHERS", precision = 15, scale = 4)
	public Double getOthers() {
		return others;
	}

	public void setOthers(Double others) {
		this.others = others;
	}

	@Column(name = "INDIVIDUAL_AWARDS_ONE", precision = 15, scale = 4)
	public Double getIndividualAwardsOne() {
		return individualAwardsOne;
	}

	public void setIndividualAwardsOne(Double individualAwardsOne) {
		this.individualAwardsOne = individualAwardsOne;
	}

	@Column(name = "INDIVIDUAL_AWARDS_TWO", precision = 15, scale = 4)
	public Double getIndividualAwardsTwo() {
		return individualAwardsTwo;
	}

	public void setIndividualAwardsTwo(Double individualAwardsTwo) {
		this.individualAwardsTwo = individualAwardsTwo;
	}

	@Column(name = "MONTH_AWARDS", precision = 15, scale = 4)
	public Double getMonthAwards() {
		return monthAwards;
	}

	public void setMonthAwards(Double monthAwards) {
		this.monthAwards = monthAwards;
	}

	@Column(name = "BIG_AWARDS", precision = 15, scale = 4)
	public Double getBigAwards() {
		return bigAwards;
	}

	public void setBigAwards(Double bigAwards) {
		this.bigAwards = bigAwards;
	}

	@Column(name = "TOTAL_INCOME", precision = 15, scale = 4)
	public Double getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}
	
//	@Column(name = "COEFFICIENT", precision = 10, scale = 4)
//	public Double getCoefficient() {
//		return coefficient;
//	}
//
//	public void setCoefficient(Double coefficient) {
//		this.coefficient = coefficient;
//	}

}