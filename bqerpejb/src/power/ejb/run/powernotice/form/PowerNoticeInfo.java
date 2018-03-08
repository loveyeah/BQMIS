package power.ejb.run.powernotice.form;

import power.ejb.run.powernotice.RunCPowerNotice;

@SuppressWarnings("serial")
public class PowerNoticeInfo implements java.io.Serializable{

	private RunCPowerNotice power;
	private String  deptName;
	private String monitorName;
    private String modifyMan;
    private String busiStateName;
    private String contactDate;
    private String modifyDate;
    private String receiveTeam;
	
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	

	public String getBusiStateName() {
		return busiStateName;
	}
	public void setBusiStateName(String busiStateName) {
		this.busiStateName = busiStateName;
	}
	
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public RunCPowerNotice getPower() {
		return power;
	}
	public void setPower(RunCPowerNotice power) {
		this.power = power;
	}
	public String getMonitorName() {
		return monitorName;
	}
	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}
	public String getModifyMan() {
		return modifyMan;
	}
	public void setModifyMan(String modifyMan) {
		this.modifyMan = modifyMan;
	}
	public String getContactDate() {
		return contactDate;
	}
	public void setContactDate(String contactDate) {
		this.contactDate = contactDate;
	}
	public String getReceiveTeam() {
		return receiveTeam;
	}
	public void setReceiveTeam(String receiveTeam) {
		this.receiveTeam = receiveTeam;
	}
}
