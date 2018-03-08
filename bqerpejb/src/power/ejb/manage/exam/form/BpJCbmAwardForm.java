package power.ejb.manage.exam.form;

public class BpJCbmAwardForm {
	
	/** 指标ID */
	private String itemId;
	/** 指标名称 */
	private String itemName;
	private String unitName;
	private String planValue;
	private String realValue;
	private String completeInfo;
	private String DeptName;
	private String level;
	private String value;
	private String affiliatedId;
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void itemName(String itemName) {
		this.itemName = itemName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getPlanValue() {
		return planValue;
	}
	public void setPlanValue(String planValue) {
		this.planValue = planValue;
	}
	public String getRealValue() {
		return realValue;
	}
	public void setRealValue(String realValue) {
		this.realValue = realValue;
	}
	public String getCompleteInfo() {
		return completeInfo;
	}
	public void setCompleteInfo(String completeInfo) {
		this.completeInfo = completeInfo;
	}
	public String getDeptName() {
		return DeptName;
	}
	public void setDeptName(String deptName) {
		DeptName = deptName;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAffiliatedId() {
		return affiliatedId;
	}
	public void setAffiliatedId(String affiliatedId) {
		this.affiliatedId = affiliatedId;
	}

}
