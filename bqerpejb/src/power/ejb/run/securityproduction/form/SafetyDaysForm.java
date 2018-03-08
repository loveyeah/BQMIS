package power.ejb.run.securityproduction.form;

import java.io.Serializable;

import power.ejb.run.securityproduction.SpJSafetyDaysrecord;

@SuppressWarnings("serial")
public class SafetyDaysForm implements Serializable {
	private SpJSafetyDaysrecord safetyDaysRecord;
	private String startDate;
	private String endDate;
	private String recordTime;
	private String recorderName;
	public SpJSafetyDaysrecord getSafetyDaysRecord() {
		return safetyDaysRecord;
	}
	public void setSafetyDaysRecord(SpJSafetyDaysrecord safetyDaysRecord) {
		this.safetyDaysRecord = safetyDaysRecord;
	}
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
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	public String getRecorderName() {
		return recorderName;
	}
	public void setRecorderName(String recorderName) {
		this.recorderName = recorderName;
	}

}
