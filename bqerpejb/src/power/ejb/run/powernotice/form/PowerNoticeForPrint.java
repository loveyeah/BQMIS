package power.ejb.run.powernotice.form;

import java.util.Date;

@SuppressWarnings("serial")
public class PowerNoticeForPrint implements java.io.Serializable{

	//联系单号
	private String noticeNo;
	//联系班组
	private String  deptName;
	//联系班长
	private String monitorName;
	//联系时间
	private String contactDate;
	//联系内容
	private String contactContent;
	//完成时间
	private String finishDate;
	//电气班长
	private String electricMonitor;
	//确认班长
	private String validateMonitor;
	

	private String noticeSort;
	
	private String memo;

	private String approveBy;

	private String approveDate;

	private String receiveTeam;
	
	public String getNoticeNo() {
		return noticeNo;
	}
	public void setNoticeNo(String noticeNo) {
		this.noticeNo = noticeNo;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getMonitorName() {
		return monitorName;
	}
	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}
	public String getContactDate() {
		return contactDate;
	}
	public void setContactDate(String contactDate) {
		this.contactDate = contactDate;
	}
	public String getContactContent() {
		return contactContent;
	}
	public void setContactContent(String contactContent) {
		this.contactContent = contactContent;
	}
	public String getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(String finishDate) {
		this.finishDate = finishDate;
	}
	public String getElectricMonitor() {
		return electricMonitor;
	}
	public void setElectricMonitor(String electricMonitor) {
		this.electricMonitor = electricMonitor;
	}
	public String getValidateMonitor() {
		return validateMonitor;
	}
	public void setValidateMonitor(String validateMonitor) {
		this.validateMonitor = validateMonitor;
	}
	public String getNoticeSort() {
		return noticeSort;
	}
	public void setNoticeSort(String noticeSort) {
		this.noticeSort = noticeSort;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getApproveBy() {
		return approveBy;
	}
	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}
	public String getApproveDate() {
		return approveDate;
	}
	public void setApproveDate(String approveDate) {
		this.approveDate = approveDate;
	}
	public String getReceiveTeam() {
		return receiveTeam;
	}
	public void setReceiveTeam(String receiveTeam) {
		this.receiveTeam = receiveTeam;
	}
	
}
