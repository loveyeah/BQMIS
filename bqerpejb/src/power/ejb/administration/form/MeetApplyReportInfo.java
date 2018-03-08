/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.administration.form;

import power.ejb.administration.AdJMeet;
/**
 * 会务申请上报bean
 * @author huangweijie
 * 
 */
public class MeetApplyReportInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	// Fields
	/** 会议审批单bean */
	private AdJMeet meetInfo;
	/** 人员编码表.姓名 */
	private String workerName;
	/** 部门编码表.申请部门 */
	private String deptName;
	/** 会议开始时间 */
	private String meetStartTime;
	/** 会议结束时间 */
	private String meetEndTime;
	/** 会议就餐时间 */
	private String dinnerTime;
	/** 上次修改时间 */
	private String modifyTime;
	/**
	 * @return the meetInfo
	 */
	public AdJMeet getMeetInfo() {
		return meetInfo;
	}
	/**
	 * @param meetInfo the meetInfo to set
	 */
	public void setMeetInfo(AdJMeet meetInfo) {
		this.meetInfo = meetInfo;
	}
	/**
	 * @return the workerName
	 */
	public String getWorkerName() {
		return workerName;
	}
	/**
	 * @param workerName the workerName to set
	 */
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	/**
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	/**
	 * @return the meetStartTime
	 */
	public String getMeetStartTime() {
		return meetStartTime;
	}
	/**
	 * @param meetStartTime the meetStartTime to set
	 */
	public void setMeetStartTime(String meetStartTime) {
		this.meetStartTime = meetStartTime;
	}
	/**
	 * @return the meetEndTime
	 */
	public String getMeetEndTime() {
		return meetEndTime;
	}
	/**
	 * @param meetEndTime the meetEndTime to set
	 */
	public void setMeetEndTime(String meetEndTime) {
		this.meetEndTime = meetEndTime;
	}
	/**
	 * @return the dinnerTime
	 */
	public String getDinnerTime() {
		return dinnerTime;
	}
	/**
	 * @param dinnerTime the dinnerTime to set
	 */
	public void setDinnerTime(String dinnerTime) {
		this.dinnerTime = dinnerTime;
	}
	/**
	 * @return the modifyTime
	 */
	public String getModifyTime() {
		return modifyTime;
	}
	/**
	 * @param modifyTime the modifyTime to set
	 */
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
}