package power.ejb.run.securityproduction.form;

import java.io.Serializable;

import power.ejb.run.securityproduction.SpJSafetyAnnualplan;

public class SafetyAnnualPlanForm implements Serializable {
	private SpJSafetyAnnualplan safetyAnnualplan;
	private String depName;
	private String fillName;
	private String fillTime;
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public String getFillName() {
		return fillName;
	}
	public void setFillName(String fillName) {
		this.fillName = fillName;
	}
	public String getFillTime() {
		return fillTime;
	}
	public void setFillTime(String fillTime) {
		this.fillTime = fillTime;
	}
	public SpJSafetyAnnualplan getSafetyAnnualplan() {
		return safetyAnnualplan;
	}
	public void setSafetyAnnualplan(SpJSafetyAnnualplan safetyAnnualplan) {
		this.safetyAnnualplan = safetyAnnualplan;
	}

}
