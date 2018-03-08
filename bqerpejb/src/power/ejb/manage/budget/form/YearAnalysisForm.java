package power.ejb.manage.budget.form;

public class YearAnalysisForm {
	private Long analysisYearId;
	private Long itemId;
	private Long centerId;
	private String dataTime;
	private Double totalFact;
	private Double yearBudget;
	private Double percentValue;
	private String itemContent;
	private String itemExplain;
	private String isUse;
	private String enterpriseCode;

	// 科目名称
	private String finaceName;
	// 指标别名
	private String itemAlias;

	private String complatePercent;

	public Long getAnalysisYearId() {
		return analysisYearId;
	}

	public void setAnalysisYearId(Long analysisYearId) {
		this.analysisYearId = analysisYearId;
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

	public Double getTotalFact() {
		return totalFact;
	}

	public void setTotalFact(Double totalFact) {
		this.totalFact = totalFact;
	}

	public Double getYearBudget() {
		return yearBudget;
	}

	public void setYearBudget(Double yearBudget) {
		this.yearBudget = yearBudget;
	}

	public Double getPercentValue() {
		return percentValue;
	}

	public void setPercentValue(Double percentValue) {
		this.percentValue = percentValue;
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

	public String getComplatePercent() {
		return complatePercent;
	}

	public void setComplatePercent(String complatePercent) {
		this.complatePercent = complatePercent;
	}
}