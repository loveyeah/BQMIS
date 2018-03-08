package power.ejb.workticket.form;

import power.ejb.workticket.business.RunJWorkticketActors;

public class WorkticketActors implements java.io.Serializable{
	private RunJWorkticketActors actor;
	private String deptName;
	private String actortypename;
	public String getActortypename() {
		return actortypename;
	}
	public void setActortypename(String actortypename) {
		this.actortypename = actortypename;
	}
	public RunJWorkticketActors getActor() {
		return actor;
	}
	public void setActor(RunJWorkticketActors actor) {
		this.actor = actor;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}
