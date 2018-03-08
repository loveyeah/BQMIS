package power.ejb.productiontec.relayProtection.form;

import power.ejb.productiontec.relayProtection.PtJdbhJBhzzdzqk;
import power.ejb.productiontec.relayProtection.PtJdbhJBhzztz;

public class ProtectDevMotionForm implements java.io.Serializable
{
	// 继电保护装置动作
	private PtJdbhJBhzzdzqk pjj;
	// 装置名称
	private String deviceName;
	// 动作时间 
	private String actDate;
	// 责任部门名称
	private String chargeDeptName;
	// 填报人姓名
	private String fillName;
	// 填报时间
	private String fillDate;
	public PtJdbhJBhzzdzqk getPjj() {
		return pjj;
	}
	public void setPjj(PtJdbhJBhzzdzqk pjj) {
		this.pjj = pjj;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getActDate() {
		return actDate;
	}
	public void setActDate(String actDate) {
		this.actDate = actDate;
	}
	public String getChargeDeptName() {
		return chargeDeptName;
	}
	public void setChargeDeptName(String chargeDeptName) {
		this.chargeDeptName = chargeDeptName;
	}
	public String getFillName() {
		return fillName;
	}
	public void setFillName(String fillName) {
		this.fillName = fillName;
	}
	public String getFillDate() {
		return fillDate;
	}
	public void setFillDate(String fillDate) {
		this.fillDate = fillDate;
	}
	
}