package power.ejb.manage.budget.form;

public class MonthAnalysisForm {
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

	// 科目名称
	private String finaceName;
	// 指标别名
	private String itemAlias;
	//add by ltong 分层
	private String zbbmtxCode;

	public Long getAnalysisMonthId() {
		return analysisMonthId;
	}

	public void setAnalysisMonthId(Long analysisMonthId) {
		this.analysisMonthId = analysisMonthId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getCenterId() {
		return centerId;
	}

	public void setCenterId(Long centerId) {
		this.centerId = centerId;
	}

	public String getDataTime() {
		return dataTime;
	}

	public void setDataTime(String dataTime) {
		this.dataTime = dataTime;
	}

	public Double getBudgetValue() {
		return budgetValue;
	}

	public void setBudgetValue(Double budgetValue) {
		this.budgetValue = budgetValue;
	}

	public Double getFactValue() {
		return factValue;
	}

	public void setFactValue(Double factValue) {
		this.factValue = factValue;
	}

	public Double getAddReduce() {
		return addReduce;
	}

	public void setAddReduce(Double addReduce) {
		this.addReduce = addReduce;
	}

	public String getItemContent() {
		return itemContent;
	}

	public void setItemContent(String itemContent) {
		this.itemContent = itemContent;
	}

	public String getItemExplain() {
		return itemExplain;
	}

	public void setItemExplain(String itemExplain) {
		this.itemExplain = itemExplain;
	}

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public String getFinaceName() {
		return finaceName;
	}

	public void setFinaceName(String finaceName) {
		this.finaceName = finaceName;
	}

	public String getItemAlias() {
		return itemAlias;
	}

	public void setItemAlias(String itemAlias) {
		this.itemAlias = itemAlias;
	}

	/**
	 * @return the zbbmtxCode
	 */
	public String getZbbmtxCode() {
		return zbbmtxCode;
	}

	/**
	 * @param zbbmtxCode the zbbmtxCode to set
	 */
	public void setZbbmtxCode(String zbbmtxCode) {
		this.zbbmtxCode = zbbmtxCode;
	}
}