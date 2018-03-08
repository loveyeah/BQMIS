package power.ejb.manage.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CbmJAnalysisMonth entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CBM_J_ANALYSIS_MONTH")
public class CbmJAnalysisMonth implements java.io.Serializable {

	// Fields

	private Long analysisMonthId;
	private Long itemId;
	private Long centerId;
	private String dataTime;
	private Double budgetValue;
	private Double factValue;
	private Double addReduce;
	private String itemContent;
	private String itemExplain;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public CbmJAnalysisMonth() {
	}

	/** minimal constructor */
	public CbmJAnalysisMonth(Long analysisMonthId, String dataTime) {
		this.analysisMonthId = analysisMonthId;
		this.dataTime = dataTime;
	}

	/** full constructor */
	public CbmJAnalysisMonth(Long analysisMonthId, Long itemId, Long centerId,
			String dataTime, Double budgetValue, Double factValue,
			Double addReduce, String itemContent, String itemExplain,
			String isUse, String enterpriseCode) {
		this.analysisMonthId = analysisMonthId;
		this.itemId = itemId;
		this.centerId = centerId;
		this.dataTime = dataTime;
		this.budgetValue = budgetValue;
		this.factValue = factValue;
		this.addReduce = addReduce;
		this.itemContent = itemContent;
		this.itemExplain = itemExplain;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ANALYSIS_MONTH_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAnalysisMonthId() {
		return this.analysisMonthId;
	}

	public void setAnalysisMonthId(Long analysisMonthId) {
		this.analysisMonthId = analysisMonthId;
	}

	@Column(name = "ITEM_ID", precision = 10, scale = 0)
	public Long getItemId() {
		return this.itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "CENTER_ID", precision = 10, scale = 0)
	public Long getCenterId() {
		return this.centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
	}

	@Column(name = "DATA_TIME", nullable = false, length = 10)
	public String getDataTime() {
		return this.dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

	@Column(name = "BUDGET_VALUE", precision = 18, scale = 6)
	public Double getBudgetValue() {
		return this.budgetValue;
	}

	public void setBudgetValue(Double budgetValue) {
		this.budgetValue = budgetValue;
	}

	@Column(name = "FACT_VALUE", precision = 18, scale = 6)
	public Double getFactValue() {
		return this.factValue;
	}

	public void setFactValue(Double factValue) {
		this.factValue = factValue;
	}

	@Column(name = "ADD_REDUCE", precision = 18, scale = 6)
	public Double getAddReduce() {
		return this.addReduce;
	}

	public void setAddReduce(Double addReduce) {
		this.addReduce = addReduce;
	}

	@Column(name = "ITEM_CONTENT", length = 100)
	public String getItemContent() {
		return this.itemContent;
	}

	public void setItemContent(String itemContent) {
		this.itemContent = itemContent;
	}

	@Column(name = "ITEM_EXPLAIN", length = 500)
	public String getItemExplain() {
		return this.itemExplain;
	}

	public void setItemExplain(String itemExplain) {
		this.itemExplain = itemExplain;
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