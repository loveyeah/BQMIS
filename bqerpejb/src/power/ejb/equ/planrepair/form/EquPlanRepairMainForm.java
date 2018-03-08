package power.ejb.equ.planrepair.form;

import power.ejb.equ.planrepair.EquJPlanRepairMain;

@SuppressWarnings("serial")
public class EquPlanRepairMainForm implements java.io.Serializable{
	private EquJPlanRepairMain prMain;
	private String planStartTime;
	private String planEndTime;
	private String fillByName;
	private String planTypeName;
	private String fareSoruceName;
	private String fillDate;
	private String statusName;
	
	public String getPlanStartTime() {
		return planStartTime;
	}
	public void setPlanStartTime(String planStartTime) {
		this.planStartTime = planStartTime;
	}
	public String getPlanEndTime() {
		return planEndTime;
	}
	public void setPlanEndTime(String planEndTime) {
		this.planEndTime = planEndTime;
	}
	public String getFillByName() {
		return fillByName;
	}
	public void setFillByName(String fillByName) {
		this.fillByName = fillByName;
	}
	public String getPlanTypeName() {
		return planTypeName;
	}
	public void setPlanTypeName(String planTypeName) {
		this.planTypeName = planTypeName;
	}
	public String getFareSoruceName() {
		return fareSoruceName;
	}
	public void setFareSoruceName(String fareSoruceName) {
		this.fareSoruceName = fareSoruceName;
	}
	public String getFillDate() {
		return fillDate;
	}
	public void setFillDate(String fillDate) {
		this.fillDate = fillDate;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public EquJPlanRepairMain getPrMain() {
		return prMain;
	}
	public void setPrMain(EquJPlanRepairMain prMain) {
		this.prMain = prMain;
	}
	
}
