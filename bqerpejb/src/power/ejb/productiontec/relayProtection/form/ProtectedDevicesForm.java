package power.ejb.productiontec.relayProtection.form;

import power.ejb.productiontec.relayProtection.PtJdbhJBbhsbtz;

public class ProtectedDevicesForm implements java.io.Serializable
{
	// 被保护设备台帐
	private PtJdbhJBbhsbtz pjjb;
	// 设备名称
	private String equName;
	// 出厂日期
	private String outFactoryDate;
	// 负责人姓名 
	private String chargeName;
	public PtJdbhJBbhsbtz getPjjb() {
		return pjjb;
	}
	public void setPjjb(PtJdbhJBbhsbtz pjjb) {
		this.pjjb = pjjb;
	}
	public String getEquName() {
		return equName;
	}
	public void setEquName(String equName) {
		this.equName = equName;
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