package power.ejb.productiontec.dependabilityAnalysis.form;

import java.io.Serializable;
import java.util.Date;
@SuppressWarnings("serial")
public class PtKkxJFjregisterForm implements Serializable
{
	private Long fjId;
	private String strMonth;
	private String fjCode;
	private Date startDate;
	private Date endDate;
	private Long jzztId;
	private Double keepTime;
	private Long standbyNum;
	private Double repairMandays;
	private Double repairCost;
	private String eventCode;
	private String eventReason;
	private String eventOtherReason;
	private String entryBy;
	private Date entryDate;
	
	//辅机名称
	private String fjName;
	//开始时间
	private String startDateString;
	// 结束时间
	private String endDateString;
	// 状态名称
	private String jzztName;
	// 填写人
	private String entryName;
	// 填写日期
	private String entryDateString;
	public Long getFjId() {
		return fjId;
	}
	public void setFjId(Long fjId) {
		this.fjId = fjId;
	}
	public String getStrMonth() {
		return strMonth;
	}
	public void setStrMonth(String strMonth) {
		this.strMonth = strMonth;
	}
	public String getFjCode() {
		return fjCode;
	}
	public void setFjCode(String fjCode) {
		this.fjCode = fjCode;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getJzztId() {
		return jzztId;
	}
	public void setJzztId(Long jzztId) {
		this.jzztId = jzztId;
	}
	public Double getKeepTime() {
		return keepTime;
	}
	public void setKeepTime(Double keepTime) {
		this.keepTime = keepTime;
	}
	public Long getStandbyNum() {
		return standbyNum;
	}
	public void setStandbyNum(Long standbyNum) {
		this.standbyNum = standbyNum;
	}
	public Double getRepairMandays() {
		return repairMandays;
	}
	public void setRepairMandays(Double repairMandays) {
		this.repairMandays = repairMandays;
	}
	public Double getRepairCost() {
		return repairCost;
	}
	public void setRepairCost(Double repairCost) {
		this.repairCost = repairCost;
	}
	public String getEventCode() {
		return eventCode;
	}
	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}
	public String getEventReason() {
		return eventReason;
	}
	public void setEventReason(String eventReason) {
		this.eventReason = eventReason;
	}
	public String getEventOtherReason() {
		return eventOtherReason;
	}
	public void setEventOtherReason(String eventOtherReason) {
		this.eventOtherReason = eventOtherReason;
	}
	public String getEntryBy() {
		return entryBy;
	}
	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}
	public Date getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public String getFjName() {
		return fjName;
	}
	public void setFjName(String fjName) {
		this.fjName = fjName;
	}
	public String getStartDateString() {
		return startDateString;
	}
	public void setStartDateString(String startDateString) {
		this.startDateString = startDateString;
	}
	public String getEndDateString() {
		return endDateString;
	}
	public void setEndDateString(String endDateString) {
		this.endDateString = endDateString;
	}
	public String getJzztName() {
		return jzztName;
	}
	public void setJzztName(String jzztName) {
		this.jzztName = jzztName;
	}
	public String getEntryName() {
		return entryName;
	}
	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}
	public String getEntryDateString() {
		return entryDateString;
	}
	public void setEntryDateString(String entryDateString) {
		this.entryDateString = entryDateString;
	}
}