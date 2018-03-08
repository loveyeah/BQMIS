package power.ejb.productiontec.relayProtection.form;

import power.ejb.productiontec.relayProtection.PtJdbhJBhzztz;

public class ProtectEquForm implements java.io.Serializable
{
	// 继电保护装置台帐
	private PtJdbhJBhzztz ptjd;
	// 装置名称
	private String equName;
	// 被保护设备名称 
	private String protectedDeviceName;
	// 出厂日期
	private String outFactoryDate;
	// 负责人姓名
	private String chargeName;
	public PtJdbhJBhzztz getPtjd() {
		return ptjd;
	}
	public void setPtjd(PtJdbhJBhzztz ptjd) {
		this.ptjd = ptjd;
	}
	public String getEquName() {
		return equName;
	}
	public void setEquName(String equName) {
		this.equName = equName;
	}
	public String getProtectedDeviceName() {
		return protectedDeviceName;
	}
	public void setProtectedDeviceName(String protectedDeviceName) {
		this.protectedDeviceName = protectedDeviceName;
	}
	public String getOutFactoryDate() {
		return outFactoryDate;
	}
	public void setOutFactoryDate(String outFactoryDate) {
		this.outFactoryDate = outFactoryDate;
	}
	public String getChargeName() {
		return chargeName;
	}
	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}
}