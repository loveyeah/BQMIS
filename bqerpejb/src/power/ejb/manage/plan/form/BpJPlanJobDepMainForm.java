package power.ejb.manage.plan.form;

import power.ejb.manage.plan.BpJPlanJobDepMain;

@SuppressWarnings("serial")
public class BpJPlanJobDepMainForm implements java.io.Serializable {
	private BpJPlanJobDepMain baseInfo;
	private String editByName;
	private String editDepName;
	private String editDate;
	private String ifComplete;
	private String completeData;
	private String jobContent;
	private String completeDesc;
	private Long finishPlan;
	private Long unfinished;
	private Long partfinished;
	private String orderBy;
	private String level1DeptName;//add by sychen 20100528

	public BpJPlanJobDepMain getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(BpJPlanJobDepMain baseInfo) {
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

	public String getEditDepName() {
		return editDepName;
	}

	public void setEditDepName(String editDepName) {
		this.editDepName = editDepName;
	}

	public String getIfComplete() {
		return ifComplete;
	}

	public void setIfComplete(String ifComplete) {
		this.ifComplete = ifComplete;
	}

	public String getJobContent() {
		return jobContent;
	}

	public void setJobContent(String jobContent) {
		this.jobContent = jobContent;
	}

	public String getCompleteDesc() {
		return completeDesc;
	}

	public void setCompleteDesc(String completeDesc) {
		this.completeDesc = completeDesc;
	}

	public Long getFinishPlan() {
		return finishPlan;
	}

	public void setFinishPlan(Long finishPlan) {
		this.finishPlan = finishPlan;
	}

	public Long getUnfinished() {
		return unfinished;
	}

	public void setUnfinished(Long unfinished) {
		this.unfinished = unfinished;
	}

	public Long getPartfinished() {
		return partfinished;
	}

	public void setPartfinished(Long partfinished) {
		this.partfinished = partfinished;
	}

	public String getCompleteData() {
		return completeData;
	}

	public void setCompleteData(String completeData) {
		this.completeData = completeData;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getLevel1DeptName() {
		return level1DeptName;
	}

	public void setLevel1DeptName(String level1DeptName) {
		this.level1DeptName = level1DeptName;
	}

}
