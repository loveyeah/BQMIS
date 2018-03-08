package power.ejb.workticket.form;

import power.ejb.workticket.RunCWorktickSafety;



public class WorticketSafety implements java.io.Serializable  {
	private RunCWorktickSafety safe;
	private String workticketTypeName;
	private String markcardTypeName;
	public RunCWorktickSafety getSafe() {
		return safe;
	}
	public void setSafe(RunCWorktickSafety safe) {
		this.safe = safe;
	}
	public String getWorkticketTypeName() {
		return workticketTypeName;
	}
	public void setWorkticketTypeName(String workticketTypeName) {
		this.workticketTypeName = workticketTypeName;
	}
	public String getMarkcardTypeName() {
		return markcardTypeName;
	}
	public void setMarkcardTypeName(String markcardTypeName) {
		this.markcardTypeName = markcardTypeName;
	}
	
}
