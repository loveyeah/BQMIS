package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJSafemeetingAttend;

public class SpJSafemeetingAttendForm {
	private SpJSafemeetingAttend atnInfo;
	private String atnName;
	private String atnDep;

	public SpJSafemeetingAttend getAtnInfo() {
		return atnInfo;
	}

	public void setAtnInfo(SpJSafemeetingAttend atnInfo) {
		this.atnInfo = atnInfo;
	}

	public String getAtnName() {
		return atnName;
	}

	public void setAtnName(String atnName) {
		this.atnName = atnName;
	}

	public String getAtnDep() {
		return atnDep;
	}

	public void setAtnDep(String atnDep) {
		this.atnDep = atnDep;
	}
}
