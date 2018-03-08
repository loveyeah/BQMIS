package power.ejb.manage.exam;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpJCbmExecution entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_J_CBM_EXECUTION")
public class BpJCbmExecution implements java.io.Serializable {

	// Fields

	private Long executionId;
	private Long itemId;
	private String belongBlock;
	private String yearMonth;
	private Double yearBudgetValue;
//	private Double branchPlanValue;
	private Double realValue;
//	private String ifRelease;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJCbmExecution() {
	}

	/** minimal constructor */
	public BpJCbmExecution(Long executionId) {
		this.executionId = executionId;
	}

	/** full constructor */
	public BpJCbmExecution(Long executionId, Long itemId, String belongBlock,
			String yearMonth, Double yearBudgetValue,
			Double realValue, String isUse,
			String enterpriseCode) {
		this.executionId = executionId;
		this.itemId = itemId;
		this.belongBlock = belongBlock;
		this.yearMonth = yearMonth;
		this.yearBudgetValue = yearBudgetValue;
		this.realValue = realValue;
//		this.ifRelease = ifRelease;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "EXECUTION_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getExecutionId() {
		return this.executionId;
	}

	public void setExecutionId(Long executionId) {
		this.executionId = executionId;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "BELONG_BLOCK", length = 1)
	public String getBelongBlock() {
		return this.belongBlock;
	}

	public void setBelongBlock(String belongBlock) {
		this.belongBlock = belongBlock;
	}

	@Column(name = "YEAR_MONTH", length = 10)
	public String getYearMonth() {
		return this.yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Column(name = "YEAR_BUDGET_VALUE", precision = 15, scale = 4)
	public Double getYearBudgetValue() {
		return this.yearBudgetValue;
	}

	public void setYearBudgetValue(Double yearBudgetValue) {
		this.yearBudgetValue = yearBudgetValue;
	}

//	@Column(name = "BRANCH_PLAN_VALUE", precision = 15, scale = 4)
//	public Double getBranchPlanValue() {
//		return this.branchPlanValue;
//	}
//
//	public void setBranchPlanValue(Double branchPlanValue) {
//		this.branchPlanValue = branchPlanValue;
//	}

	@Column(name = "REAL_VALUE", precision = 15, scale = 4)
	public Double getRealValue() {
		return this.realValue;
	}

	public void setRealValue(Double realValue) {
		this.realValue = realValue;
	}

//	@Column(name = "IF_RELEASE", length = 1)
//	public String getIfRelease() {
//		return this.ifRelease;
//	}
//
//	public void setIfRelease(String ifRelease) {
//		this.ifRelease = ifRelease;
//	}

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