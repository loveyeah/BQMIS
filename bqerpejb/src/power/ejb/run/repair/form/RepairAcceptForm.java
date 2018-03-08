package power.ejb.run.repair.form;

public class RepairAcceptForm {
	
	private Long acceptId;
	private String repairProjectName;
	private String startEndTime;
	private String completion;
	private String memo;
	private String planStartEndDate;
	
	public Long getAcceptId() {
		return acceptId;
	}
	public void setAcceptId(Long acceptId) {
		this.acceptId = acceptId;
	}
	public String getRepairProjectName() {
		return repairProjectName;
	}
	public void setRepairProjectName(String repairProjectName) {
		this.repairProjectName = repairProjectName;
	}
	public String getStartEndTime() {
		return startEndTime;
	}
	public void setStartEndTime(String startEndTime) {
		this.startEndTime = startEndTime;
	}
	public String getCompletion() {
		return completion;
	}
	public void setCompletion(String completion) {
		this.completion = completion;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPlanStartEndDate() {
		return planStartEndDate;
	}
	public void setPlanStartEndDate(String planStartEndDate) {
		this.planStartEndDate = planStartEndDate;
	}
}
