package power.ejb.manage.stat.form;

@SuppressWarnings("serial")
public class SmallReportForm implements java.io.Serializable {
	private String itemCode;
	private String itemName;
	private String unitCode;
	private String unitName;
	private String dataDate;
	private Double dataValue;
	private Long rowDatatypeId;
	private String itemAlias;
	private String dataType;
	private Long orderBy;
	private String isItem;
	private String compluteMethod;
	private String isIgnoreZero;
	

	public String getIsIgnoreZero() {
		return isIgnoreZero;
	}

	public void setIsIgnoreZero(String isIgnoreZero) {
		this.isIgnoreZero = isIgnoreZero;
	}

	public String getIsItem() {
		return isItem;
	}

	public void setIsItem(String isItem) {
		this.isItem = isItem;
	}

	public Long getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
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

	public Long getRowDatatypeId() {
		return rowDatatypeId;
	}

	public void setRowDatatypeId(Long rowDatatypeId) {
		this.rowDatatypeId = rowDatatypeId;
	}

	public String getItemAlias() {
		return itemAlias;
	}

	public void setItemAlias(String itemAlias) {
		this.itemAlias = itemAlias;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getCompluteMethod() {
		return compluteMethod;
	}

	public void setCompluteMethod(String compluteMethod) {
		this.compluteMethod = compluteMethod;
	}
}
