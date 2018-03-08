package power.ejb.manage.budget.form;

import power.ejb.manage.budget.CbmCCenterItem;

public class DeptTopicItemForm {
	// 预算部门指标维护
	private CbmCCenterItem ccc;
	// 指标编码
	private String itemCode;
	// 指标名称
	private String itemName;

	public CbmCCenterItem getCcc() {
		return ccc;
	}

	public void setCcc(CbmCCenterItem ccc) {
		this.ccc = ccc;
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
}