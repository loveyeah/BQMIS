package power.ejb.manage.stat.form;

public class dailyReportForm {

	private String itemName;

	private String unitName;

	private String RValue;

	private String YValue;

	private String JValue;

	private String NValue;
	
	private String secondName; 

	public String getSecondName() {
		return secondName;
	}

	public void setSecondName(String secondName) {
		this.secondName = secondName;
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

	public String getRValue() {
		return RValue;
	}

	public void setRValue(String value) {
		RValue = value;
	}

	public String getYValue() {
		return YValue;
	}

	public void setYValue(String value) {
		YValue = value;
	}

	public String getJValue() {
		return JValue;
	}

	public void setJValue(String value) {
		JValue = value;
	}

	public String getNValue() {
		return NValue;
	}

	public void setNValue(String value) {
		NValue = value;
	}

}
