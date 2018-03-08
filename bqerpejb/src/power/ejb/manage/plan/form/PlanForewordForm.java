package power.ejb.manage.plan.form;

import power.ejb.manage.plan.BpJPlanForeword;

public class PlanForewordForm {
	private String editName;
	private BpJPlanForeword baseInfo;
	private String editDateString;

	public String getEditDateString() {
		return editDateString;
	}

	public void setEditDateString(String editDateString) {
		this.editDateString = editDateString;
	}

	public String getEditName() {
		return editName;
	}

	public void setEditName(String editName) {
		this.editName = editName;
	}

	public BpJPlanForeword getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(BpJPlanForeword baseInfo) {
		this.baseInfo = baseInfo;
	}

}
