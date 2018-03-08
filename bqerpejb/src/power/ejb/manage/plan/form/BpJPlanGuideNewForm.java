package power.ejb.manage.plan.form;

import power.ejb.manage.plan.BpJPlanGuideNew;

@SuppressWarnings("serial")
public class BpJPlanGuideNewForm implements java.io.Serializable {
	private BpJPlanGuideNew guideNew;
	private String editDate;
	private String editByName;
	private String referDepName;
	private String mainDepName;
	private String guideCode;

	public BpJPlanGuideNew getGuideNew() {
		return guideNew;
	}

	public void setGuideNew(BpJPlanGuideNew guideNew) {
		this.guideNew = guideNew;
	}

	public String getEditDate() {
		return editDate;
	}

	public void setEditDate(String editDate) {
		this.editDate = editDate;
	}

	public String getEditByName() {
		return editByName;
	}

	public void setEditByName(String editByName) {
		this.editByName = editByName;
	}

	public String getReferDepName() {
		return referDepName;
	}

	public void setReferDepName(String referDepName) {
		this.referDepName = referDepName;
	}

	public String getMainDepName() {
		return mainDepName;
	}

	public void setMainDepName(String mainDepName) {
		this.mainDepName = mainDepName;
	}

	public String getGuideCode() {
		return guideCode;
	}

	public void setGuideCode(String guideCode) {
		this.guideCode = guideCode;
	}
}
