package power.ejb.manage.plan.form;

import power.ejb.manage.plan.BpJPlanGuideMain;

@SuppressWarnings("serial")
public class BpJPlanGuideMainForm implements java.io.Serializable {
	private BpJPlanGuideMain baseInfo;
	private String planTimeString;
	private String editByName;
	private String editDate;
	private String releaseDate;

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public BpJPlanGuideMain getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(BpJPlanGuideMain baseInfo) {
		this.baseInfo = baseInfo;
	}

	public String getEditByName() {
		return editByName;
	}

	public void setEditByName(String editByName) {
		this.editByName = editByName;
	}

	public String getEditDate() {
		return editDate;
	}

	public void setEditDate(String editDate) {
		this.editDate = editDate;
	}

	public String getPlanTimeString() {
		return planTimeString;
	}

	public void setPlanTimeString(String planTimeString) {
		this.planTimeString = planTimeString;
	}
}
