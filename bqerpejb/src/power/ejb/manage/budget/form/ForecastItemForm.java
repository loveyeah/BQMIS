package power.ejb.manage.budget.form;

public class ForecastItemForm implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7512752542L;
	private Long modelItemId;
	private Long daddyItemId;
	private String modelItemCode;
	private String modelItemName;
	private String modelType;
	private String isItem;
	private Long unitId;
	private String comeFrom;
	private Long modelOrder;
	private Long displayNo;
	private String modelItemExplain;
	private String isUse;
	private String enterpriseCode;

	private Long forecastId;
	private String forecastTime;
	private Double forecastValue;

	// 单位名称
	private String unitName;

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Long getModelItemId() {
		return modelItemId;
	}

	public void setModelItemId(Long modelItemId) {
		this.modelItemId = modelItemId;
	}

	public Long getDaddyItemId() {
		return daddyItemId;
	}

	public void setDaddyItemId(Long daddyItemId) {
		this.daddyItemId = daddyItemId;
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

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getIsItem() {
		return isItem;
	}

	public void setIsItem(String isItem) {
		this.isItem = isItem;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public String getComeFrom() {
		return comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	public Long getModelOrder() {
		return modelOrder;
	}

	public void setModelOrder(Long modelOrder) {
		this.modelOrder = modelOrder;
	}

	public Long getDisplayNo() {
		return displayNo;
	}

	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}

	public String getModelItemExplain() {
		return modelItemExplain;
	}

	public void setModelItemExplain(String modelItemExplain) {
		this.modelItemExplain = modelItemExplain;
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

	public Long getForecastId() {
		return forecastId;
	}

	public void setForecastId(Long forecastId) {
		this.forecastId = forecastId;
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
}