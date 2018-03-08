package power.ejb.productiontec.chemistry.form;

import power.ejb.productiontec.chemistry.PtHxjdJZxybybzb;

public class ChemistryReportForm implements java.io.Serializable
{
	// 化学在线仪表月报主表
	private PtHxjdJZxybybzb phj;
	// 化学在线仪表月报主表  机组名
	private String deviceName;
	// 化学在线仪表月报主表 月份
	private String month;
	// 化学在线仪表月报主表 填报人姓名
	private String fillName;
	// 化学在线仪表月报主表 填报时间
	private String fillDate;
	// 化学在线仪器月报 月报编号
	private String zxybybId;
	// 化学在线仪器月报  仪表ID
	private String meterId;
	// 化学在线仪器月报  必投台数
	private String mustThrowNum;
	// 化学在线仪器月报 配备台数
	private String  equipNum;
	// 化学在线仪器月报  投运台数
	private String throwNum;
	// 化学在线仪器月报  配备率
	private String equipRate;
	// 化学在线仪器月报 投入率
	private String throwRate;
	// 化学在线仪器月报  抄表率
	private String searchRate;
	// 化学在线仪表维护 仪表名称
	private String meterName;
	public PtHxjdJZxybybzb getPhj() {
		return phj;
	}
	public void setPhj(PtHxjdJZxybybzb phj) {
		this.phj = phj;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
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
	public String getZxybybId() {
		return zxybybId;
	}
	public void setZxybybId(String zxybybId) {
		this.zxybybId = zxybybId;
	}
	public String getMeterId() {
		return meterId;
	}
	public void setMeterId(String meterId) {
		this.meterId = meterId;
	}
	public String getMustThrowNum() {
		return mustThrowNum;
	}
	public void setMustThrowNum(String mustThrowNum) {
		this.mustThrowNum = mustThrowNum;
	}
	public String getEquipNum() {
		return equipNum;
	}
	public void setEquipNum(String equipNum) {
		this.equipNum = equipNum;
	}
	public String getThrowNum() {
		return throwNum;
	}
	public void setThrowNum(String throwNum) {
		this.throwNum = throwNum;
	}
	public String getEquipRate() {
		return equipRate;
	}
	public void setEquipRate(String equipRate) {
		this.equipRate = equipRate;
	}
	public String getThrowRate() {
		return throwRate;
	}
	public void setThrowRate(String throwRate) {
		this.throwRate = throwRate;
	}
	public String getSearchRate() {
		return searchRate;
	}
	public void setSearchRate(String searchRate) {
		this.searchRate = searchRate;
	}
	public String getMeterName() {
		return meterName;
	}
	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}
	
}