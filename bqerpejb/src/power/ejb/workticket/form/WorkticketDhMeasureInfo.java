package power.ejb.workticket.form;

import java.util.Date;

@SuppressWarnings("serial")
public class WorkticketDhMeasureInfo implements java.io.Serializable{
	//create by fyyang 090310 动火票测量页面列表
	private String measureDataId;
	private String workticketNo;
	private String measureData;
	private String measureMan;
	private String measureDate;
    private String measureManName;
	public String getMeasureDataId() {
		return measureDataId;
	}
	public void setMeasureDataId(String measureDataId) {
		this.measureDataId = measureDataId;
	}
	public String getWorkticketNo() {
		return workticketNo;
	}
	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}
	public String getMeasureData() {
		return measureData;
	}
	public void setMeasureData(String measureData) {
		this.measureData = measureData;
	}
	public String getMeasureMan() {
		return measureMan;
	}
	public void setMeasureMan(String measureMan) {
		this.measureMan = measureMan;
	}
	public String getMeasureDate() {
		return measureDate;
	}
	public void setMeasureDate(String measureDate) {
		this.measureDate = measureDate;
	}
	public String getMeasureManName() {
		return measureManName;
	}
	public void setMeasureManName(String measureManName) {
		this.measureManName = measureManName;
	}
	
}
