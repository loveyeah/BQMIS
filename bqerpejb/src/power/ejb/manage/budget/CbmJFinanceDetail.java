package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmJFinanceDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_J_FINANCE_DETAIL")
public class CbmJFinanceDetail implements java.io.Serializable {

	// Fields

	private Long financeDetailId;
	private Long financeId;
	private String loanName;
	private Double lastLoan;
	private Double newLoan;
	private Double repayment;
	private Double balance;
	private Double interest;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmJFinanceDetail() {
	}

	/** minimal constructor */
	public CbmJFinanceDetail(Long financeDetailId, Long financeId) {
		this.financeDetailId = financeDetailId;
		this.financeId = financeId;
	}

	/** full constructor */
	public CbmJFinanceDetail(Long financeDetailId, Long financeId,
			String loanName, Double lastLoan, Double newLoan, Double repayment,
			Double balance, Double interest, String memo, String isUse,
			String enterpriseCode) {
		this.financeDetailId = financeDetailId;
		this.financeId = financeId;
		this.loanName = loanName;
		this.lastLoan = lastLoan;
		this.newLoan = newLoan;
		this.repayment = repayment;
		this.balance = balance;
		this.interest = interest;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "FINANCE_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getFinanceDetailId() {
		return this.financeDetailId;
	}

	public void setFinanceDetailId(Long financeDetailId) {
		this.financeDetailId = financeDetailId;
	}

	@Column(name = "FINANCE_ID", nullable = false, precision = 10, scale = 0)
	public Long getFinanceId() {
		return this.financeId;
	}

	public void setFinanceId(Long financeId) {
		this.financeId = financeId;
	}

	@Column(name = "LOAN_NAME", length = 80)
	public String getLoanName() {
		return this.loanName;
	}

	public void setLoanName(String loanName) {
		this.loanName = loanName;
	}

	@Column(name = "LAST_LOAN", precision = 15, scale = 4)
	public Double getLastLoan() {
		return this.lastLoan;
	}

	public void setLastLoan(Double lastLoan) {
		this.lastLoan = lastLoan;
	}

	@Column(name = "NEW_LOAN", precision = 15, scale = 4)
	public Double getNewLoan() {
		return this.newLoan;
	}

	public void setNewLoan(Double newLoan) {
		this.newLoan = newLoan;
	}

	@Column(name = "REPAYMENT", precision = 15, scale = 4)
	public Double getRepayment() {
		return this.repayment;
	}

	public void setRepayment(Double repayment) {
		this.repayment = repayment;
	}

	@Column(name = "BALANCE", precision = 15, scale = 4)
	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Column(name = "INTEREST", precision = 15, scale = 4)
	public Double getInterest() {
		return this.interest;
	}

	public void setInterest(Double interest) {
		this.interest = interest;
	}

	@Column(name = "MEMO", length = 200)
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