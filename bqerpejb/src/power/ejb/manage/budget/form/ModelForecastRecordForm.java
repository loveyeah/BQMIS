package power.ejb.manage.budget.form;

public class ModelForecastRecordForm {
	private Long forecastId;
	private Long modelItemId;
	private String forecastTime;
	private Double forecastValue;
	private String enterpriseCode;

	private String modelType;
	// 指标编码
	private String modelItemCode;
	// 指标名称
	private String modelItemName;
	// 单位名称
	private String unitName;
	// 正向利润预测
	private Double firstValue;
	// 反向电量预测
	private Double secondValue;
	// 反向固本预测
	private Double thirdValue;
	// 反向变本预测
	private Double forthValue;

	private Double averageValue;

	private Long displayNo;

	public Long getForecastId() {
		return forecastId;
	}

	public void setForecastId(Long forecastId) {
		this.forecastId = forecastId;
	}

	public Long getModelItemId() {
		return modelItemId;
	}

	public void setModelItemId(Long modelItemId) {
		this.modelItemId = modelItemId;
	}

	public String getForecastTime() {
		return forecastTime;
	}

	public void setForecastTime(String forecastTime) {
		this.forecastTime = forecastTime;
	}

	public Double getForecastValue() {
		return forecastValue;
	}

	public void setForecastValue(Double forecastValue) {
		this.forecastValue = forecastValue;
	}

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	public String getModelItemCode() {
		return modelItemCode;
	}

	public void setModelItemCode(String modelItemCode) {
		this.modelItemCode = modelItemCode;
	}

	public String getModelItemName() {
		return modelItemName;
	}

	public void setModelItemName(String modelItemName) {
		this.modelItemName = modelItemName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Double getFirstValue() {
		return firstValue;
	}

	public void setFirstValue(Double firstValue) {
		this.firstValue = firstValue;
	}

	public Double getSecondValue() {
		return secondValue;
	}

	public void setSecondValue(Double secondValue) {
		this.secondValue = secondValue;
	}

	public Double getThirdValue() {
		return thirdValue;
	}

	public void setThirdValue(Double thirdValue) {
		this.thirdValue = thirdValue;
	}

	public Double getForthValue() {
		return forthValue;
	}

	public void setForthValue(Double forthValue) {
		this.forthValue = forthValue;
	}

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public Long getDisplayNo() {
		return displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	public Double getAverageValue() {
		return averageValue;
	}

	public void setAverageValue(Double averageValue) {
		this.averageValue = averageValue;
	}
}