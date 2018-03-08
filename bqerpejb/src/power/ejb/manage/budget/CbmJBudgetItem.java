package power.ejb.manage.budget;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CbmJBudgetItem entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_J_BUDGET_ITEM")
public class CbmJBudgetItem implements java.io.Serializable {

	// Fields

	private Long budgetItemId;
//	private Long itemId;
	private Long budgetMakeId;
	private Double forecastBudget;
	private Double adviceBudget;
	private Double budgetAdd;
//	private Double ensureBudget;
	private Double factHappen;
	private Double financeHappen;
	private String budgetBasis;
	private String enterpriseCode;
	private String modifyBy;
	private Date modifyDate;
	private String isUse;
	private Long centerItemId;

	// Constructors

	/** default constructor */
	public CbmJBudgetItem() {
	}



	/** full constructor */
	public CbmJBudgetItem(Long budgetItemId, Long itemId, Long budgetMakeId,
			Double forecastBudget, Double adviceBudget, Double budgetAdd,
			Double ensureBudget, Double factHappen, Double financeHappen,
			String budgetBasis, String enterpriseCode, String modifyBy,
			Date modifyDate,String isUse) {
		this.budgetItemId = budgetItemId;
		
		this.budgetMakeId = budgetMakeId;
		this.forecastBudget = forecastBudget;
		this.adviceBudget = adviceBudget;
		this.budgetAdd = budgetAdd;
	
		this.factHappen = factHappen;
		this.financeHappen = financeHappen;
		this.budgetBasis = budgetBasis;
		this.enterpriseCode = enterpriseCode;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
		this.isUse=isUse;
	}

	// Property accessors
	@Id
	@Column(name = "BUDGET_ITEM_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBudgetItemId() {
		return this.budgetItemId;
	}

	public void setBudgetItemId(Long budgetItemId) {
		this.budgetItemId = budgetItemId;
	}

	

	@Column(name = "BUDGET_MAKE_ID", precision = 10, scale = 0)
	public Long getBudgetMakeId() {
		return this.budgetMakeId;
	}

	public void setBudgetMakeId(Long budgetMakeId) {
		this.budgetMakeId = budgetMakeId;
	}

	@Column(name = "FORECAST_BUDGET", precision = 15, scale = 4)
	public Double getForecastBudget() {
		return this.forecastBudget;
	}

	public void setForecastBudget(Double forecastBudget) {
		this.forecastBudget = forecastBudget;
	}

	@Column(name = "ADVICE_BUDGET", precision = 25, scale = 4)
	public Double getAdviceBudget() {
		return this.adviceBudget;
	}

	public void setAdviceBudget(Double adviceBudget) {
		this.adviceBudget = adviceBudget;
	}

	@Column(name = "BUDGET_ADD", precision = 15, scale = 4)
	public Double getBudgetAdd() {
		return this.budgetAdd;
	}

	public void setBudgetAdd(Double budgetAdd) {
		this.budgetAdd = budgetAdd;
	}



	@Column(name = "FACT_HAPPEN", precision = 18, scale = 6)
	public Double getFactHappen() {
		return this.factHappen;
	}

	public void setFactHappen(Double factHappen) {
		this.factHappen = factHappen;
	}

	@Column(name = "FINANCE_HAPPEN", precision = 18, scale = 6)
	public Double getFinanceHappen() {
		return this.financeHappen;
	}

	public void setFinanceHappen(Double financeHappen) {
		this.financeHappen = financeHappen;
	}

	@Column(name = "BUDGET_BASIS", length = 200)
	public String getBudgetBasis() {
		return this.budgetBasis;
	}

	public void setBudgetBasis(String budgetBasis) {
		this.budgetBasis = budgetBasis;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
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
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}


	@Column(name = "CENTER_ITEM_ID", precision = 10, scale = 0)
	public Long getCenterItemId() {
		return centerItemId;
	}



	public void setCenterItemId(Long centerItemId) {
		this.centerItemId = centerItemId;
	}

}