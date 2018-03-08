package power.ejb.manage.stat.form;

@SuppressWarnings("serial")
public class StatItemEntry implements java.io.Serializable {
	private String itemCode;
	private String itemName;
	private String unitCode;
	private String unitName;
	private String dataDate;
	private Double dataValue;
	private String dataType;
	private String itemBaseName;
	
	// add by liuyi 20100513 班组描述
	private String banzuDesc;

	public String getBanzuDesc() {
		return banzuDesc;
	}

	public void setBanzuDesc(String banzuDesc) {
		this.banzuDesc = banzuDesc;
	}

	public String getItemBaseName() {
		return itemBaseName;
	}

	public void setItemBaseName(String itemBaseName) {
		this.itemBaseName = itemBaseName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}

	public Double getDataValue() {
		return dataValue;
	}

	public void setDataValue(Double dataValue) {
		this.dataValue = dataValue;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}
