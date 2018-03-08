package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJSafeaward;

public class SpJSafeawardForm {
	private SpJSafeaward awardInfo;
	// 奖励时间
	private String encourageDateString;
	// 批准人
	private String approvalByName;
	// 填写人
	private String completeByName;

	public SpJSafeaward getAwardInfo() {
		return awardInfo;
	}

	public void setAwardInfo(SpJSafeaward awardInfo) {
		this.awardInfo = awardInfo;
	}

	public String getEncourageDateString() {
		return encourageDateString;
	}

	public void setEncourageDateString(String encourageDateString) {
		this.encourageDateString = encourageDateString;
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
