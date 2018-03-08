package power.ejb.manage.budget.form;

import power.ejb.manage.budget.CbmCMasterItem;

public class DeptControlForm {
	// 预算控制维护
	private CbmCMasterItem ccm;
	// 部门编码
	private String depCode;
	// 部门名称
	private String depName;
	// 指标变么
	private String itemCode;
	// 指标名称
	private String itemName;

	public CbmCMasterItem getCcm() {
		return ccm;
	}

	public void setCcm(CbmCMasterItem ccm) {
		this.ccm = ccm;
	}

	public String getDepCode() {
		return depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
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