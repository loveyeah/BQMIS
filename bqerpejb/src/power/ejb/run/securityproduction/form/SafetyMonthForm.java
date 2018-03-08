package power.ejb.run.securityproduction.form;

import power.ejb.run.securityproduction.SpJSafetyMonth;

@SuppressWarnings("serial")
public class SafetyMonthForm implements java.io.Serializable{
	private SpJSafetyMonth safetymonth;
	private String workName;
	private String filldateString;
	private String monthString;
	
	public SpJSafetyMonth getSafetymonth() {
		return safetymonth;
	}
	public void setSafetymonth(SpJSafetyMonth safetymonth) {
		this.safetymonth = safetymonth;
	}
	public String getWorkName() {
		return workName;
	}
	public void setWorkName(String workName) {
		this.workName = workName;
	}
	public String getFilldateString() {
		return filldateString;
	}
	public void setFilldateString(String filldateString) {
		this.filldateString = filldateString;
	}
	public String getMonthString() {
		return monthString;
	}
	public void setMonthString(String monthString) {
		this.monthString = monthString;
	}
	
	
}
