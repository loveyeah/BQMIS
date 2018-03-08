package power.ejb.workticket.form;

import java.util.Date;

import power.ejb.workticket.business.RunJWorktickethis;

@SuppressWarnings("serial")
public class workticketHisInfo implements java.io.Serializable{
private RunJWorktickethis model;
private String oldChargeByName;
private String newChargeByName;
private String strOldApprovedFinishDate;
private String strNewApprovedFinishDate;
private String approveByName;
private String  strApproveDate;
private String dutyChargeByName;
private String fireByName;
public RunJWorktickethis getModel() {
	return model;
}
public void setModel(RunJWorktickethis model) {
	this.model = model;
}
public String getOldChargeByName() {
	return oldChargeByName;
}
public void setOldChargeByName(String oldChargeByName) {
	this.oldChargeByName = oldChargeByName;
}
public String getNewChargeByName() {
	return newChargeByName;
}
public void setNewChargeByName(String newChargeByName) {
	this.newChargeByName = newChargeByName;
}
public String getStrOldApprovedFinishDate() {
	return strOldApprovedFinishDate;
}
public void setStrOldApprovedFinishDate(String strOldApprovedFinishDate) {
	this.strOldApprovedFinishDate = strOldApprovedFinishDate;
}
public String getStrNewApprovedFinishDate() {
	return strNewApprovedFinishDate;
}
public void setStrNewApprovedFinishDate(String strNewApprovedFinishDate) {
	this.strNewApprovedFinishDate = strNewApprovedFinishDate;
}
public String getApproveByName() {
	return approveByName;
}
public void setApproveByName(String approveByName) {
	this.approveByName = approveByName;
}
public String getStrApproveDate() {
	return strApproveDate;
}
public void setStrApproveDate(String strApproveDate) {
	this.strApproveDate = strApproveDate;
}
public String getDutyChargeByName() {
	return dutyChargeByName;
}
public void setDutyChargeByName(String dutyChargeByName) {
	this.dutyChargeByName = dutyChargeByName;
}
public String getFireByName() {
	return fireByName;
}
public void setFireByName(String fireByName) {
	this.fireByName = fireByName;
}
}
