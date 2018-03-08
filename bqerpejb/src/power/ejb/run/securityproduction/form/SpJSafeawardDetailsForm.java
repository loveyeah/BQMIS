package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJSafeawardDetails;

public class SpJSafeawardDetailsForm {
	private SpJSafeawardDetails awardDetailsInfo;
	private String encourageManName;
	private String encourageManDep;

	public SpJSafeawardDetails getAwardDetailsInfo() {
		return awardDetailsInfo;
	}

	public void setAwardDetailsInfo(SpJSafeawardDetails awardDetailsInfo) {
		this.awardDetailsInfo = awardDetailsInfo;
	}

	public String getEncourageManName() {
		return encourageManName;
	}

	public void setEncourageManName(String encourageManName) {
		this.encourageManName = encourageManName;
	}

	public String getEncourageManDep() {
		return encourageManDep;
	}

	public void setEncourageManDep(String encourageManDep) {
		this.encourageManDep = encourageManDep;
	}
}
