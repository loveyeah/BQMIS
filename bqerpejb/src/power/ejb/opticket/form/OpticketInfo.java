package power.ejb.opticket.form;

import power.ejb.opticket.RunJOpticket;

@SuppressWarnings("serial")
public class OpticketInfo implements java.io.Serializable{
	private RunJOpticket opticket;
	private String operatorName;
	private String protectorName;
	private String chargeName;
	private String classLeader;
	private String createrName;
	private String planStartDate;
	private String planEndDate;
	private String startTime;
	private String endTime;
	private String createDate;
	private String specialityName;
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public RunJOpticket getOpticket() {
		return opticket;
	}
	public void setOpticket(RunJOpticket opticket) {
		this.opticket = opticket;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getProtectorName() {
		return protectorName;
	}
	public void setProtectorName(String protectorName) {
		this.protectorName = protectorName;
	}
	public String getClassLeader() {
		return classLeader;
	}
	public void setClassLeader(String classLeader) {
		this.classLeader = classLeader;
	}
	public String getPlanStartDate() {
		return planStartDate;
	}
	public void setPlanStartDate(String planStartDate) {
		this.planStartDate = planStartDate;
	}
	public String getPlanEndDate() {
		return planEndDate;
	}
	public void setPlanEndDate(String planEndDate) {
		this.planEndDate = planEndDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	public String getChargeName() {
		return chargeName;
	}
	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}
	
}
