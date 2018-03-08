package power.ejb.manage.stat.form;

import power.ejb.manage.stat.BpCInputReportItem;

@SuppressWarnings("serial")
public class InputReprotItemForm implements java.io.Serializable {
	private BpCInputReportItem model;
	private String itemName;
	private Double dataValue;
	private Double sdataValue;
	private String unitName;
	private String date;
	private String itemCode;

	public Double getDataValue() {
		return dataValue;
	}

	public void setDataValue(Double dataValue) {
		this.dataValue = dataValue;
	}

	public Double getSdataValue() {
		return sdataValue;
	}

	public void setSdataValue(Double sdataValue) {
		this.sdataValue = sdataValue;
	}

	public BpCInputReportItem getModel() {
		return model;
	}

	public void setModel(BpCInputReportItem model) {
		this.model = model;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
}
