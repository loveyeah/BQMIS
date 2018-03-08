package power.ejb.manage.budget.form;

import java.io.Serializable;
@SuppressWarnings("serial")
public class CostForm implements Serializable
{
	private Long costId;
	private String analyseDate;
	private Long itemId;
	private Double factValue;
	private String memo;
	private String isUse;
	
	// 指标名称
	private String itemName;
	private String comeFrom;
	private String itemType;
	public String getComeFrom() {
		return comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Long getCostId() {
		return costId;
	}

	public void setCostId(Long costId) {
		this.costId = costId;
	}

	public String getAnalyseDate() {
		return analyseDate;
	}

	public void setAnalyseDate(String analyseDate) {
		this.analyseDate = analyseDate;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Double getFactValue() {
		return factValue;
	}

	public void setFactValue(Double factValue) {
		this.factValue = factValue;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}