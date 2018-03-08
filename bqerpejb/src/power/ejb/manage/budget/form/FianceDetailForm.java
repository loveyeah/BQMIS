package power.ejb.manage.budget.form;

public class FianceDetailForm implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 95254654545L;
	private Long financeId;
	private String budgetTime;
	private String financeType;
	private Long workFlowNo;
	private String workFlowStatus;

	private Long financeDetailId;
	private String loanName;
	private Double lastLoan;
	private Double newLoan;
	private Double repayment;
	private Double balance;
	private Double interest;
	private String memo;

	private String isUse;
	private String enterpriseCode;

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public Long getFinanceId() {
		return financeId;
	}

	public void setFinanceId(Long financeId) {
		this.financeId = financeId;
	}

	public String getBudgetTime() {
		return budgetTime;
	}

	public void setBudgetTime(String budgetTime) {
		this.budgetTime = budgetTime;
	}

	public String getFinanceType() {
		return financeType;
	}

	public void setFinanceType(String financeType) {
		this.financeType = financeType;
	}

	public Long getWorkFlowNo() {
		return workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	public String getWorkFlowStatus() {
		return workFlowStatus;
	}

	public void setWorkFlowStatus(String workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
	}

	public Long getFinanceDetailId() {
		return financeDetailId;
	}

	public void setFinanceDetailId(Long financeDetailId) {
		this.financeDetailId = financeDetailId;
	}

	public String getLoanName() {
		return loanName;
	}

	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}

	public Double getLastLoan() {
		return lastLoan;
	}

	public void setLastLoan(Double lastLoan) {
		this.lastLoan = lastLoan;
	}

	public Double getNewLoan() {
		return newLoan;
	}

	public void setNewLoan(Double newLoan) {
		this.newLoan = newLoan;
	}

	public Double getRepayment() {
		return repayment;
	}

	public void setRepayment(Double repayment) {
		this.repayment = repayment;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getInterest() {
		return interest;
	}

	public void setInterest(Double interest) {
		this.interest = interest;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
}