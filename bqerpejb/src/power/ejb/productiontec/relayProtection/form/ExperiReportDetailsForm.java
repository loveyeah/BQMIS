package power.ejb.productiontec.relayProtection.form;

import power.ejb.productiontec.relayProtection.PtJdbhJSybgjl;

public class ExperiReportDetailsForm implements java.io.Serializable
{
	// 继电试验报告记录
	private PtJdbhJSybgjl pjjs;
	// 试验报告名称
	private String jdsybgName;
	// 试验项目编号
	private String syxmId;
	// 试验项目名称
	private String syxmName;
	//试验点名称
	private String sydName;
	//仪器仪表名称
	private String regulatorName;
	// 合格下限
	private String minimum;
	// 合格上限
	private String maximum;
	public PtJdbhJSybgjl getPjjs() {
		return pjjs;
	}
	public void setPjjs(PtJdbhJSybgjl pjjs) {
		this.pjjs = pjjs;
	}
	public String getJdsybgName() {
		return jdsybgName;
	}
	public void setJdsybgName(String jdsybgName) {
		this.jdsybgName = jdsybgName;
	}
	public String getSyxmId() {
		return syxmId;
	}
	public void setSyxmId(String syxmId) {
		this.syxmId = syxmId;
	}
	public String getSyxmName() {
		return syxmName;
	}
	public void setSyxmName(String syxmName) {
		this.syxmName = syxmName;
	}
	public String getSydName() {
		return sydName;
	}
	public void setSydName(String sydName) {
		this.sydName = sydName;
	}
	public String getRegulatorName() {
		return regulatorName;
	}
	public void setRegulatorName(String regulatorName) {
		this.regulatorName = regulatorName;
	}
	public String getMinimum() {
		return minimum;
	}
	public void setMinimum(String minimum) {
		this.minimum = minimum;
	}
	public String getMaximum() {
		return maximum;
	}
	public void setMaximum(String maximum) {
		this.maximum = maximum;
	}
}