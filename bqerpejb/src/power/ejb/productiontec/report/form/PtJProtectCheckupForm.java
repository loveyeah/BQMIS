package power.ejb.productiontec.report.form;

import java.io.Serializable;
import java.util.Date;
@SuppressWarnings("serial")
public class PtJProtectCheckupForm implements Serializable
{
	private Long chekupId;
	private String strMonth;
	private String protectEqu;
	private String protectDevice;
	private Date lastCheckDate;
	private Date planFinishDate;
	private Date factFinishDate;
	private String finishThing;
	private String notFinishReason;
	private String hasProblem;
	private String approveBy;
	private String checkBy;
	private String entryBy;
	private Date entryDate;
	// 被保护设备名称
	private String protectEquName;
	// 保护装置型号名称
	private String protectDeviceName;
	// 上次定检完成时间
	private String lastCheckDateString;
	// 本次计划完成时间
	private String planFinishDateString;
	// 实际定检完成时间
	private String factFinishDateString;
	// 填写时间
	private String entryDateString;
	// 批准人
	private String approveName;
	// 审核人 
	private String checkName;
	// 填写人 
	private String entryName;
	public PtJProtectCheckupForm()
	{
		this.protectEquName = "";
		this.protectDeviceName = "";
		this.lastCheckDateString = "";
		this.planFinishDateString = "";
		this.factFinishDateString = "";
		this.finishThing = "";
		this.notFinishReason = "";
		this.hasProblem = "";
	}
	public Long getChekupId() {
		return chekupId;
	}
	public void setChekupId(Long chekupId) {
		this.chekupId = chekupId;
	}
	public String getStrMonth() {
		return strMonth;
	}
	public void setStrMonth(String strMonth) {
		this.strMonth = strMonth;
	}
	public String getProtectEqu() {
		return protectEqu;
	}
	public void setProtectEqu(String protectEqu) {
		this.protectEqu = protectEqu;
	}
	public String getProtectDevice() {
		return protectDevice;
	}
	public void setProtectDevice(String protectDevice) {
		this.protectDevice = protectDevice;
	}
	public Date getLastCheckDate() {
		return lastCheckDate;
	}
	public void setLastCheckDate(Date lastCheckDate) {
		this.lastCheckDate = lastCheckDate;
	}
	public Date getPlanFinishDate() {
		return planFinishDate;
	}
	public void setPlanFinishDate(Date planFinishDate) {
		this.planFinishDate = planFinishDate;
	}
	public Date getFactFinishDate() {
		return factFinishDate;
	}
	public void setFactFinishDate(Date factFinishDate) {
		this.factFinishDate = factFinishDate;
	}
	public String getFinishThing() {
		return finishThing;
	}
	public void setFinishThing(String finishThing) {
		this.finishThing = finishThing;
	}
	public String getNotFinishReason() {
		return notFinishReason;
	}
	public void setNotFinishReason(String notFinishReason) {
		this.notFinishReason = notFinishReason;
	}
	public String getHasProblem() {
		return hasProblem;
	}
	public void setHasProblem(String hasProblem) {
		this.hasProblem = hasProblem;
	}
	public String getApproveBy() {
		return approveBy;
	}
	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}
	public String getCheckBy() {
		return checkBy;
	}
	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
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
	public String getProtectEquName() {
		return protectEquName;
	}
	public void setProtectEquName(String protectEquName) {
		this.protectEquName = protectEquName;
	}
	public String getProtectDeviceName() {
		return protectDeviceName;
	}
	public void setProtectDeviceName(String protectDeviceName) {
		this.protectDeviceName = protectDeviceName;
	}
	public String getLastCheckDateString() {
		return lastCheckDateString;
	}
	public void setLastCheckDateString(String lastCheckDateString) {
		this.lastCheckDateString = lastCheckDateString;
	}
	public String getPlanFinishDateString() {
		return planFinishDateString;
	}
	public void setPlanFinishDateString(String planFinishDateString) {
		this.planFinishDateString = planFinishDateString;
	}
	public String getFactFinishDateString() {
		return factFinishDateString;
	}
	public void setFactFinishDateString(String factFinishDateString) {
		this.factFinishDateString = factFinishDateString;
	}
	public String getEntryDateString() {
		return entryDateString;
	}
	public void setEntryDateString(String entryDateString) {
		this.entryDateString = entryDateString;
	}
	public String getApproveName() {
		return approveName;
	}
	public void setApproveName(String approveName) {
		this.approveName = approveName;
	}
	public String getCheckName() {
		return checkName;
	}
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	public String getEntryName() {
		return entryName;
	}
	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}
}