package power.ejb.productiontec.report.form;

import java.io.Serializable;
import java.util.Date;
@SuppressWarnings("serial")
public class QuickProtectCountForm implements Serializable
{
	private Long chekupId;
	private String strMonth;
	private String protectEqu;
	private String protectDevice;
	private String protectType;
	private Date exitDate;
	private Date resumeDate;
	private String exitReason;
	private String checkBy;
	private String approveBy;
	private String entryBy;
	private Date entryDate;
	
	// 被保护设备
	private String protectEquName;
	//保护型号
	private String protectDeviceName;
	//保护种类
	private String protectTypeName;
	//退出时间
	private String exitDateString;
	// 恢复时间
	private String resumeDateString;
	// 填写时间
	private String entryDateString;
	// 批准人
	private String approveName;
	// 审核人 
	private String checkName;
	// 填写人 
	private String entryName;
	public QuickProtectCountForm()
	{
		this.exitReason = "";
		this.entryName = "";
		this.checkName = "";
		this.approveName = "";
		this.entryDateString = "";
		this.resumeDateString = "";
		this.exitDateString = "";
		this.protectTypeName = "";
		this.protectDeviceName = "";
		this.protectEquName = "";
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
	public String getProtectType() {
		return protectType;
	}
	public void setProtectType(String protectType) {
		this.protectType = protectType;
	}
	public Date getExitDate() {
		return exitDate;
	}
	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
	}
	public Date getResumeDate() {
		return resumeDate;
	}
	public void setResumeDate(Date resumeDate) {
		this.resumeDate = resumeDate;
	}
	public String getExitReason() {
		return exitReason;
	}
	public void setExitReason(String exitReason) {
		this.exitReason = exitReason;
	}
	public String getCheckBy() {
		return checkBy;
	}
	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}
	public String getApproveBy() {
		return approveBy;
	}
	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
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
	public String getProtectTypeName() {
		return protectTypeName;
	}
	public void setProtectTypeName(String protectTypeName) {
		this.protectTypeName = protectTypeName;
	}
	public String getExitDateString() {
		return exitDateString;
	}
	public void setExitDateString(String exitDateString) {
		this.exitDateString = exitDateString;
	}
	public String getResumeDateString() {
		return resumeDateString;
	}
	public void setResumeDateString(String resumeDateString) {
		this.resumeDateString = resumeDateString;
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