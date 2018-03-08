package power.ejb.hr.salary.form;

@SuppressWarnings("serial")
public class SalaryWageForm implements java.io.Serializable{

	private Long wageId;
	private Long empId;
	private String chsName;
	private String salaryMonth;
	private Double basisWage;
	private Double ageWage;
	private Double operationWage;
	private Double remainWage;
	private Double pointWage;
	private Double overtimeWage;
	private Double deductionWage;
	private Double totalWage;
	private String wageMemo;
	private Double monthBasic;
	private Double coefficient;
	private Double monthAwardCut;
	private Double monthAward;
	private String monthAwardMemo;
	private Double award1Basic;
	private Double award1coeff;
	private Double bigAwardOneCut;
	private Double bigAwardOne;
	private Double award2Basic;
	private Double award2coeff;
	private Double bigAwardTwoCut;
	private Double bigAwardTwo;
	private String bigAwardMemo;
	private String deptName;//add by sychen 20100803
	private String banZhu;//add by mgxia 20100909
	
	//add by sychen 20100805
	private Double others;
	private Double individualAwardsOne;
	private Double individualAwardsTwo;
	private Double monthAwards;
	private Double bigAwards;
	private Double totalIncome;
	private Long   deptId;
	private String newEmpCode;
	public Long getWageId() {
		return wageId;
	}
	public void setWageId(Long wageId) {
		this.wageId = wageId;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	public String getChsName() {
		return chsName;
	}
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}
	public String getSalaryMonth() {
		return salaryMonth;
	}
	public void setSalaryMonth(String salaryMonth) {
		this.salaryMonth = salaryMonth;
	}
	public Double getBasisWage() {
		return basisWage;
	}
	public void setBasisWage(Double basisWage) {
		this.basisWage = basisWage;
	}
	public Double getAgeWage() {
		return ageWage;
	}
	public void setAgeWage(Double ageWage) {
		this.ageWage = ageWage;
	}
	public Double getOperationWage() {
		return operationWage;
	}
	public void setOperationWage(Double operationWage) {
		this.operationWage = operationWage;
	}
	public Double getRemainWage() {
		return remainWage;
	}
	public void setRemainWage(Double remainWage) {
		this.remainWage = remainWage;
	}
	public Double getPointWage() {
		return pointWage;
	}
	public void setPointWage(Double pointWage) {
		this.pointWage = pointWage;
	}
	public Double getOvertimeWage() {
		return overtimeWage;
	}
	public void setOvertimeWage(Double overtimeWage) {
		this.overtimeWage = overtimeWage;
	}
	public Double getDeductionWage() {
		return deductionWage;
	}
	public void setDeductionWage(Double deductionWage) {
		this.deductionWage = deductionWage;
	}
	public Double getTotalWage() {
		return totalWage;
	}
	public void setTotalWage(Double totalWage) {
		this.totalWage = totalWage;
	}
	public String getWageMemo() {
		return wageMemo;
	}
	public void setWageMemo(String wageMemo) {
		this.wageMemo = wageMemo;
	}
	public Double getMonthBasic() {
		return monthBasic;
	}
	public void setMonthBasic(Double monthBasic) {
		this.monthBasic = monthBasic;
	}
	public Double getCoefficient() {
		return coefficient;
	}
	public void setCoefficient(Double coefficient) {
		this.coefficient = coefficient;
	}
	public Double getMonthAwardCut() {
		return monthAwardCut;
	}
	public void setMonthAwardCut(Double monthAwardCut) {
		this.monthAwardCut = monthAwardCut;
	}
	public Double getMonthAward() {
		return monthAward;
	}
	public void setMonthAward(Double monthAward) {
		this.monthAward = monthAward;
	}
	public String getMonthAwardMemo() {
		return monthAwardMemo;
	}
	public void setMonthAwardMemo(String monthAwardMemo) {
		this.monthAwardMemo = monthAwardMemo;
	}
	public Double getAward1Basic() {
		return award1Basic;
	}
	public void setAward1Basic(Double award1Basic) {
		this.award1Basic = award1Basic;
	}
	public Double getAward1coeff() {
		return award1coeff;
	}
	public void setAward1coeff(Double award1coeff) {
		this.award1coeff = award1coeff;
	}
	public Double getBigAwardOneCut() {
		return bigAwardOneCut;
	}
	public void setBigAwardOneCut(Double bigAwardOneCut) {
		this.bigAwardOneCut = bigAwardOneCut;
	}
	public Double getBigAwardOne() {
		return bigAwardOne;
	}
	public void setBigAwardOne(Double bigAwardOne) {
		this.bigAwardOne = bigAwardOne;
	}
	public Double getAward2Basic() {
		return award2Basic;
	}
	public void setAward2Basic(Double award2Basic) {
		this.award2Basic = award2Basic;
	}
	public Double getAward2coeff() {
		return award2coeff;
	}
	public void setAward2coeff(Double award2coeff) {
		this.award2coeff = award2coeff;
	}
	public Double getBigAwardTwoCut() {
		return bigAwardTwoCut;
	}
	public void setBigAwardTwoCut(Double bigAwardTwoCut) {
		this.bigAwardTwoCut = bigAwardTwoCut;
	}
	public Double getBigAwardTwo() {
		return bigAwardTwo;
	}
	public void setBigAwardTwo(Double bigAwardTwo) {
		this.bigAwardTwo = bigAwardTwo;
	}
	public String getBigAwardMemo() {
		return bigAwardMemo;
	}
	public void setBigAwardMemo(String bigAwardMemo) {
		this.bigAwardMemo = bigAwardMemo;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Double getOthers() {
		return others;
	}
	public void setOthers(Double others) {
		this.others = others;
	}
	public Double getIndividualAwardsOne() {
		return individualAwardsOne;
	}
	public void setIndividualAwardsOne(Double individualAwardsOne) {
		this.individualAwardsOne = individualAwardsOne;
	}
	public Double getIndividualAwardsTwo() {
		return individualAwardsTwo;
	}
	public void setIndividualAwardsTwo(Double individualAwardsTwo) {
		this.individualAwardsTwo = individualAwardsTwo;
	}
	public Double getMonthAwards() {
		return monthAwards;
	}
	public void setMonthAwards(Double monthAwards) {
		this.monthAwards = monthAwards;
	}
	public Double getBigAwards() {
		return bigAwards;
	}
	public void setBigAwards(Double bigAwards) {
		this.bigAwards = bigAwards;
	}
	public Double getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(Double totalIncome) {
		this.totalIncome = totalIncome;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getNewEmpCode() {
		return newEmpCode;
	}
	public void setNewEmpCode(String newEmpCode) {
		this.newEmpCode = newEmpCode;
	}
	public String getBanZhu() {
		return banZhu;
	}
	public void setBanZhu(String banZhu) {
		this.banZhu = banZhu;
	}

}
