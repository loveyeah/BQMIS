package power.ejb.hr.salary.form;

import power.ejb.hr.salary.HrCSalaryPoint;

public class SalaryPointForm {
	private HrCSalaryPoint spoint;
	private String startTime;
	private String endTime;
	private String status;

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
	public HrCSalaryPoint getSpoint() {
		return spoint;
	}
	public void setSpoint(HrCSalaryPoint spoint) {
		this.spoint = spoint;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
