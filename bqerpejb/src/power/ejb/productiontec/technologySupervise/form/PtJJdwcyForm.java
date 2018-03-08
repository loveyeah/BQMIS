package power.ejb.productiontec.technologySupervise.form;

import power.ejb.productiontec.technologySupervise.PtJJdwcy;

public class PtJJdwcyForm {
	private PtJJdwcy model;
	private String workerName;
	private String deptName;
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
	public PtJJdwcy getModel() {
		return model;
	}
	public void setModel(PtJJdwcy model) {
		this.model = model;
	}
}
