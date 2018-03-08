/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;

import java.util.Date;

/**
 * 来宾接待审批单bean
 * 部门编码表bean
 * 人员编码表bean
 * 
 * 
 * @author liugonglei
 * @version 1.0
 */

public class ReceptionApplyInfo {
	// Fields
	// 来宾接待审批单表
	// 序号
	private String id;
	// 接待审批单号
	private String applyId;
	// 填表日期
	private String logDate;
	// 接待日期
	private String meetDate;
	// 填表日期
	private Date newLogDate;
	// 接待日期
	private Date newMeetDate;
	// 接待说明
	private String meetNote;
	// 就餐人数
	private String repastNum;
	// 就餐标准
	private String repastBz;
	// 就餐安排
	private String repastPlan;
	// 住宿人数
	private String roomNum;
	// 住宿标准
	private String roomBz;
	// 住宿安排
	private String roomPlan;
	// 标准金额
	private String payoutBz;
	// 实际金额
	private String payout;
	// 差额
	private String balance;
	// 其他
	private String other;
	// 修改时间
	private Long updateTime;
	
	// 部门编码表
	// 部门名称
	private String depName;
	
	// 人员编码表
	// 名称
	private String name;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the logDate
	 */
	public String getLogDate() {
		return logDate;
	}

	/**
	 * @param logDate the logDate to set
	 */
	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}

	/**
	 * @return the meetDate
	 */
	public String getMeetDate() {
		return meetDate;
	}

	/**
	 * @param meetDate the meetDate to set
	 */
	public void setMeetDate(String meetDate) {
		this.meetDate = meetDate;
	}

	/**
	 * @return the newLogDate
	 */
	public Date getNewLogDate() {
		return newLogDate;
	}

	/**
	 * @param newLogDate the newLogDate to set
	 */
	public void setNewLogDate(Date newLogDate) {
		this.newLogDate = newLogDate;
	}

	/**
	 * @return the newMeetDate
	 */
	public Date getNewMeetDate() {
		return newMeetDate;
	}

	/**
	 * @param newMeetDate the newMeetDate to set
	 */
	public void setNewMeetDate(Date newMeetDate) {
		this.newMeetDate = newMeetDate;
	}

	/**
	 * @return the meetNote
	 */
	public String getMeetNote() {
		return meetNote;
	}

	/**
	 * @param meetNote the meetNote to set
	 */
	public void setMeetNote(String meetNote) {
		this.meetNote = meetNote;
	}

	/**
	 * @return the repastNum
	 */
	public String getRepastNum() {
		return repastNum;
	}

	/**
	 * @param repastNum the repastNum to set
	 */
	public void setRepastNum(String repastNum) {
		this.repastNum = repastNum;
	}

	/**
	 * @return the repastBz
	 */
	public String getRepastBz() {
		return repastBz;
	}

	/**
	 * @param repastBz the repastBz to set
	 */
	public void setRepastBz(String repastBz) {
		this.repastBz = repastBz;
	}

	/**
	 * @return the repastPlan
	 */
	public String getRepastPlan() {
		return repastPlan;
	}

	/**
	 * @param repastPlan the repastPlan to set
	 */
	public void setRepastPlan(String repastPlan) {
		this.repastPlan = repastPlan;
	}

	/**
	 * @return the roomNum
	 */
	public String getRoomNum() {
		return roomNum;
	}

	/**
	 * @param roomNum the roomNum to set
	 */
	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	/**
	 * @return the roomBz
	 */
	public String getRoomBz() {
		return roomBz;
	}

	/**
	 * @param roomBz the roomBz to set
	 */
	public void setRoomBz(String roomBz) {
		this.roomBz = roomBz;
	}

	/**
	 * @return the roomPlan
	 */
	public String getRoomPlan() {
		return roomPlan;
	}

	/**
	 * @param roomPlan the roomPlan to set
	 */
	public void setRoomPlan(String roomPlan) {
		this.roomPlan = roomPlan;
	}

	/**
	 * @return the payoutBz
	 */
	public String getPayoutBz() {
		return payoutBz;
	}

	/**
	 * @param payoutBz the payoutBz to set
	 */
	public void setPayoutBz(String payoutBz) {
		this.payoutBz = payoutBz;
	}

	/**
	 * @return the payout
	 */
	public String getPayout() {
		return payout;
	}

	/**
	 * @param payout the payout to set
	 */
	public void setPayout(String payout) {
		this.payout = payout;
	}

	/**
	 * @return the balance
	 */
	public String getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(String balance) {
		this.balance = balance;
	}

	/**
	 * @return the other
	 */
	public String getOther() {
		return other;
	}

	/**
	 * @param other the other to set
	 */
	public void setOther(String other) {
		this.other = other;
	}

	/**
	 * @return the updateTime
	 */
	public Long getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the depName
	 */
	public String getDepName() {
		return depName;
	}

	/**
	 * @param depName the depName to set
	 */
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the applyId
	 */
	public String getApplyId() {
		return applyId;
	}

	/**
	 * @param applyId the applyId to set
	 */
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
}
