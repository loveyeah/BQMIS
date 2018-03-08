package power.ejb.workticket.form;

import power.ejb.workticket.business.RunJWorktickets;

@SuppressWarnings("serial")
public class WorkticketInfo implements java.io.Serializable {
	private RunJWorktickets model;
	private String statusName;
	private String chargeByName;
	private String deptName;
	private String planStartDate;
	private String planEndDate;
	private String approveEndDate;
	private String blockName;
	private String isEmergencyText;
	private String sourceName;
	private String watcherName;
	private String fireTickerExeByName;
	private String delayToDate;
	//add by fyyang 090319
	private String workticketTypeName; //票种类名称
	private String repairSpecailName; //检修专业名称
	private String recieveSpecailName; //接收专业名称
	
	
	//add by fyyang 090112
	private String dangerTypeName;
	
	 private String repairSpecail; // add by liuyi 091116
	 private String receiveSpecail;// add by liuyi 091116
	public RunJWorktickets getModel() {
		return model;
	}
	public void setModel(RunJWorktickets model) {
		this.model = model;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
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
	public String getApproveEndDate() {
		return approveEndDate;
	}
	public void setApproveEndDate(String approveEndDate) {
		this.approveEndDate = approveEndDate;
	}
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}
	public String getIsEmergencyText() {
		return isEmergencyText;
	}
	public void setIsEmergencyText(String isEmergencyText) {
		this.isEmergencyText = isEmergencyText;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getWatcherName() {
		return watcherName;
	}
	public void setWatcherName(String watcherName) {
		this.watcherName = watcherName;
	}
	public String getFireTickerExeByName() {
		return fireTickerExeByName;
	}
	public void setFireTickerExeByName(String fireTickerExeByName) {
		this.fireTickerExeByName = fireTickerExeByName;
	}
	public String getDelayToDate() {
		return delayToDate;
	}
	public void setDelayToDate(String delayToDate) {
		this.delayToDate = delayToDate;
	}
	public String getDangerTypeName() {
		return dangerTypeName;
	}
	public void setDangerTypeName(String dangerTypeName) {
		this.dangerTypeName = dangerTypeName;
	}
	public String getWorkticketTypeName() {
		return workticketTypeName;
	}
	public void setWorkticketTypeName(String workticketTypeName) {
		this.workticketTypeName = workticketTypeName;
	}
	public String getRepairSpecailName() {
		return repairSpecailName;
	}
	public void setRepairSpecailName(String repairSpecailName) {
		this.repairSpecailName = repairSpecailName;
	}
	public String getRecieveSpecailName() {
		return recieveSpecailName;
	}
	public void setRecieveSpecailName(String recieveSpecailName) {
		this.recieveSpecailName = recieveSpecailName;
	}
	public String getRepairSpecail() {
		return repairSpecail;
	}
	public void setRepairSpecail(String repairSpecail) {
		this.repairSpecail = repairSpecail;
	}
	public String getReceiveSpecail() {
		return receiveSpecail;
	}
	public void setReceiveSpecail(String receiveSpecail) {
		this.receiveSpecail = receiveSpecail;
	}

}
