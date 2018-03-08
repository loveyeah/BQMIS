package power.ejb.equ.planrepair.form;

import power.ejb.equ.planrepair.EquJPlanRepair;

@SuppressWarnings("serial")
public class EquPlanRepairForm implements java.io.Serializable{
	private EquJPlanRepair prepair;
	private String planStartTime;
	private String planEndTime;
	private String equName;
	private String specialityName;
	private String contentName;
	private String chargeByName;
	public EquJPlanRepair getPrepair() {
		return prepair;
	}
	public void setPrepair(EquJPlanRepair prepair) {
		this.prepair = prepair;
	}
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
	public String getEquName() {
		return equName;
	}
	public void setEquName(String equName) {
		this.equName = equName;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public String getChargeByName() {
		return chargeByName;
	}
	public void setChargeByName(String chargeByName) {
		this.chargeByName = chargeByName;
	}
	
}
