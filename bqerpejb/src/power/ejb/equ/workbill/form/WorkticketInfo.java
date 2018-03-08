package power.ejb.equ.workbill.form;

import power.ejb.workticket.business.RunJWorktickets;
@SuppressWarnings("serial")
public class WorkticketInfo implements java.io.Serializable{
	private RunJWorktickets model;
	private String statusName;
	private String chargeByName;
	private String deptName;
	private String planStartDate;
	private String planEndDate;
	private String sourceName;
	private String workticketContentAndLocationName;
	public RunJWorktickets getModel() {
		return model;
	}
	public void setModel(RunJWorktickets model) {
		this.model = model;
	}
	public String getChargeByName() {
		return chargeByName;
	}
	public void setChargeByName(String chargeByName) {
		this.chargeByName = chargeByName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getPlanStartDate() {
		return planStartDate;
	}
	public void setPlanStartDate(String planStartDate) {
		this.planStartDate = planStartDate;
	}
	public String getPlanEndDate() {
		return planEndDate;
	}
	public void setPlanEndDate(String planEndDate) {
		this.planEndDate = planEndDate;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getWorkticketContentAndLocationName() {
		return workticketContentAndLocationName;
	}
	public void setWorkticketContentAndLocationName(
			String workticketContentAndLocationName) {
		this.workticketContentAndLocationName = workticketContentAndLocationName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}