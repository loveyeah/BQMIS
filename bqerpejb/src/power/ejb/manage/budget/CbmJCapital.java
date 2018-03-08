package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmJCapital entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_J_CAPITAL")
public class CbmJCapital implements java.io.Serializable {

	// Fields

	private Long capitalId;
	private String budgetTime;
	private Long workFlowNo;
	private String workFlowStatus;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmJCapital() {
	}

	/** minimal constructor */
	public CbmJCapital(Long capitalId) {
		this.capitalId = capitalId;
	}

	/** full constructor */
	public CbmJCapital(Long capitalId, String budgetTime, Long workFlowNo,
			String workFlowStatus, String isUse, String enterpriseCode) {
		this.capitalId = capitalId;
		this.budgetTime = budgetTime;
		this.workFlowNo = workFlowNo;
		this.workFlowStatus = workFlowStatus;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "CAPITAL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCapitalId() {
		return this.capitalId;
	}

	public void setCapitalId(Long capitalId) {
		this.capitalId = capitalId;
	}

	@Column(name = "BUDGET_TIME", length = 10)
	public String getBudgetTime() {
		return this.budgetTime;
	}

	public void setBudgetTime(String budgetTime) {
		this.budgetTime = budgetTime;
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