package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJSafepunish;

public class SpJSafepunishForm {
	private SpJSafepunish punishInfo;
	private String punishDateString;
	// 部门
	private String punishDepName;
	// 批准人
	private String approvalByName;
	// 填报人
	private String completeByName;

	public SpJSafepunish getPunishInfo() {
		return punishInfo;
	}

	public void setPunishInfo(SpJSafepunish punishInfo) {
		this.punishInfo = punishInfo;
	}

	public String getPunishDateString() {
		return punishDateString;
	}

	public void setPunishDateString(String punishDateString) {
		this.punishDateString = punishDateString;
	}

	public String getPunishDepName() {
		return punishDepName;
	}

	public void setPunishDepName(String punishDepName) {
		this.punishDepName = punishDepName;
	}

	public String getApprovalByName() {
		return approvalByName;
	}

	public void setApprovalByName(String approvalByName) {
		this.approvalByName = approvalByName;
	}

	public String getCompleteByName() {
		return completeByName;
	}

	public void setCompleteByName(String completeByName) {
		this.completeByName = completeByName;
	}
}
