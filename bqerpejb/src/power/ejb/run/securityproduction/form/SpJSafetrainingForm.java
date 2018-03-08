package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJSafetraining;

public class SpJSafetrainingForm {
	
	private SpJSafetraining baseInfo;
	private String speakerName;
	private String trainingTime;
	private String deptName;
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getTrainingTime() {
		return trainingTime;
	}
	public void setTrainingTime(String trainingTime) {
		this.trainingTime = trainingTime;
	}
	public SpJSafetraining getBaseInfo() {
		return baseInfo;
	}
	public void setBaseInfo(SpJSafetraining baseInfo) {
		this.baseInfo = baseInfo;
	}
	public String getSpeakerName() {
		return speakerName;
	}
	public void setSpeakerName(String speakerName) {
		this.speakerName = speakerName;
	}

}
