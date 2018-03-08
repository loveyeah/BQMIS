package power.ejb.productiontec.chemistry.form;

import java.util.Date;

import power.ejb.productiontec.chemistry.PtHxjdJRlsbjcqk;

@SuppressWarnings("serial")
public class ThermalEquCheckForm implements java.io.Serializable{

	private String startDate;
	private String endDate;
	private String examineName;
	private String chargeName;
	private String fillName;
	private String depName;
	private String fillDate;
	private String deviceName;
	private PtHxjdJRlsbjcqk equ;
	
	private String equName;
	private String repairDate;
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getExamineName() {
		return examineName;
	}
	public void setExamineName(String examineName) {
		this.examineName = examineName;
	}
	public String getChargeName() {
		return chargeName;
	}
	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}
	public String getFillName() {
		return fillName;
	}
	public void setFillName(String fillName) {
		this.fillName = fillName;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public String getFillDate() {
		return fillDate;
	}
	public void setFillDate(String fillDate) {
		this.fillDate = fillDate;
	}
	public PtHxjdJRlsbjcqk getEqu() {
		return equ;
	}
	public void setEqu(PtHxjdJRlsbjcqk equ) {
		this.equ = equ;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getEquName() {
		return equName;
	}
	public void setEquName(String equName) {
		this.equName = equName;
	}
	public String getRepairDate() {
		return repairDate;
	}
	public void setRepairDate(String repairDate) {
		this.repairDate = repairDate;
	}
	
	
}
