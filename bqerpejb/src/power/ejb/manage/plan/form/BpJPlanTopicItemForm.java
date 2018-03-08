package power.ejb.manage.plan.form;

@SuppressWarnings("serial")
public class BpJPlanTopicItemForm implements java.io.Serializable {
	private Long topicItemId;
	private Long reportId;
	private String itemCode;
	private Double planValue;
	private Double lastValue;
	private String itemName;
	private String unitName;

	public Long getTopicItemId() {
		return topicItemId;
	}

	public void setTopicItemId(Long topicItemId) {
		this.topicItemId = topicItemId;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Double getPlanValue() {
		return planValue;
	}

	public void setPlanValue(Double planValue) {
		this.planValue = planValue;
	}

	public Double getLastValue() {
		return lastValue;
	}

	public void setLastValue(Double lastValue) {
		this.lastValue = lastValue;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

}
