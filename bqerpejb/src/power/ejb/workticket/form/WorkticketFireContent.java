package power.ejb.workticket.form;

import power.ejb.workticket.business.RunJWorkticketFireContent;

public class WorkticketFireContent implements java.io.Serializable{
	private RunJWorkticketFireContent fire;
	private String firecontentName;
	public RunJWorkticketFireContent getFire() {
		return fire;
	}
	public void setFire(RunJWorkticketFireContent fire) {
		this.fire = fire;
	}
	public String getFirecontentName() {
		return firecontentName;
	}
	public void setFirecontentName(String firecontentName) {
		this.firecontentName = firecontentName;
	}
	

}
