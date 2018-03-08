package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmJFinance entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_J_FINANCE")
public class CbmJFinance implements java.io.Serializable {

	// Fields

	private Long financeId;
	private String budgetTime;
	private String financeType;
	private Long workFlowNo;
	private String workFlowStatus;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmJFinance() {
	}

	/** minimal constructor */
	public CbmJFinance(Long financeId) {
		this.financeId = financeId;
	}

	/** full constructor */
	public CbmJFinance(Long financeId, String budgetTime, String financeType,
			Long workFlowNo, String workFlowStatus, String isUse,
			String enterpriseCode) {
		this.financeId = financeId;
		this.budgetTime = budgetTime;
		this.financeType = financeType;
		this.workFlowNo = workFlowNo;
		this.workFlowStatus = workFlowStatus;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "FINANCE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getFinanceId() {
		return this.financeId;
	}

	public void setFinanceId(Long financeId) {
		this.financeId = financeId;
	}

	@Column(name = "BUDGET_TIME", length = 10)
	public String getBudgetTime() {
		return this.budgetTime;
	}

	public void setBudgetTime(String budgetTime) {
		this.budgetTime = budgetTime;
	}

	@Column(name = "FINANCE_TYPE", length = 1)
	public String getFinanceType() {
		return this.financeType;
	}

	public void setFinanceType(String financeType) {
		this.financeType = financeType;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "WORK_FLOW_STATUS", length = 1)
	public String getWorkFlowStatus() {
		return this.workFlowStatus;
	}

	public void setWorkFlowStatus(String workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
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