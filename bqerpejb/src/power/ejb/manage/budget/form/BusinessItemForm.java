package power.ejb.manage.budget.form;

@SuppressWarnings("serial")
public class BusinessItemForm implements java.io.Serializable {
	
	private Long businessItemId;
	private String itemName;
	private Long unitId;
	private Long itemId1;
	private String itemName1;
	private Long itemId2;
	private String itemName2;
	private String unitName;
	private String adviceBudget;
	private String avgAdviceBudget;
	
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getAdviceBudget() {
		return adviceBudget;
	}
	public void setAdviceBudget(String adviceBudget) {
		this.adviceBudget = adviceBudget;
	}
	public String getAvgAdviceBudget() {
		return avgAdviceBudget;
	}
	public void setAvgAdviceBudget(String avgAdviceBudget) {
		this.avgAdviceBudget = avgAdviceBudget;
	}
	public Long getBusinessItemId() {
		return businessItemId;
	}
	public void setBusinessItemId(Long businessItemId) {
		this.businessItemId = businessItemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Long getItemId1() {
		return itemId1;
	}
	public void setItemId1(Long itemId1) {
		this.itemId1 = itemId1;
	}
	public String getItemName1() {
		return itemName1;
	}
	public void setItemName1(String itemName1) {
		this.itemName1 = itemName1;
	}
	public Long getItemId2() {
		return itemId2;
	}
	public void setItemId2(Long itemId2) {
		this.itemId2 = itemId2;
	}
	public String getItemName2() {
		return itemName2;
	}
	public void setItemName2(String itemName2) {
		this.itemName2 = itemName2;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
}