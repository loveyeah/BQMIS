package power.ejb.manage.plan.form;

import java.io.Serializable;

import power.ejb.manage.plan.BpJPlanGuideDetail;

@SuppressWarnings("serial")
public class BpJPlanGuideDetailForm implements Serializable {
	private BpJPlanGuideDetail baseInfo;
	private String mainDepName;
	private String referDepName;
	private String targetDepName;
	private String ifCompleteName;
	private String ifCheckName;
	private String checkStatusName;

	public String getIfCompleteName() {
		return ifCompleteName;
	}

	public void setIfCompleteName(String ifCompleteName) {
		this.ifCompleteName = ifCompleteName;
	}

	public String getIfCheckName() {
		return ifCheckName;
	}

	public void setIfCheckName(String ifCheckName) {
		this.ifCheckName = ifCheckName;
	}

	public String getCheckStatusName() {
		return checkStatusName;
	}

	public void setCheckStatusName(String checkStatusName) {
		this.checkStatusName = checkStatusName;
	}

	public BpJPlanGuideDetail getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(BpJPlanGuideDetail baseInfo) {
		this.baseInfo = baseInfo;
	}

	public String getMainDepName() {
		return mainDepName;
	}

	public void setMainDepName(String mainDepName) {
		this.mainDepName = mainDepName;
	}

	public String getReferDepName() {
		return referDepName;
	}

	public void setReferDepName(String referDepName) {
		this.referDepName = referDepName;
	}

	public String getTargetDepName() {
		return targetDepName;
	}

	public void setTargetDepName(String targetDepName) {
		this.targetDepName = targetDepName;
	}
}
