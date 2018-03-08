package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJSafemeetingAbsence;

public class SpJSafemeetingAbsenceForm {
	private SpJSafemeetingAbsence absInfo;
	private String absName;
	private String absDep;

	public SpJSafemeetingAbsence getAbsInfo() {
		return absInfo;
	}

	public void setAbsInfo(SpJSafemeetingAbsence absInfo) {
		this.absInfo = absInfo;
	}

	public String getAbsName() {
		return absName;
	}

	public void setAbsName(String absName) {
		this.absName = absName;
	}

	public String getAbsDep() {
		return absDep;
	}

	public void setAbsDep(String absDep) {
		this.absDep = absDep;
	}

}
