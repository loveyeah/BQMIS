package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJSafetrainingAttend;

public class SpJSafetrainingAttendForm {
	private SpJSafetrainingAttend atnInfo;
	private String workerName;
	private String deptName;
	
	public SpJSafetrainingAttend getAtnInfo() {
		return atnInfo;
	}
	public void setAtnInfo(SpJSafetrainingAttend atnInfo) {
		this.atnInfo = atnInfo;
	}
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

}
