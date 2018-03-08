/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;

import java.io.Serializable;

/**
 * 接待费用管理.
 * 
 * @author wangyun
 */
public class ReceptionChargeInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 来宾接待审批单表 */
	/** 序列号 */
	private Long id;
	/** 申请人 */
	private String name;
	/** 申请部门 */
	private String depName;
	/** 填表日期 */
	private String logDate;
	/** 接待日期 */
	private String meetDate;
	/** 就餐人数 */
	private Long repastNum;
	/** 住宿人数 */
	private Long roomNum;
	/** 接待说明 */
	private String meetNote;
	/** 就餐标准 */
	private Double repastBz;
	/** 住宿标准 */
	private Double roomBz;
	/** 就餐安排 */
	private String repastPlan;
	/** 住宿安排 */
	private String roomPlan;
	/** 其他 */
	private String other;
	/** 标准金额 */
	private Double payoutBz;
	/** 实际金额 */
	private Double payout;
	/** 差额 */
	private Double balance;
	/** 修改时间 */
	private String updateTime;
	/** 审批单号 */
	private String applyId;

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

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the depName
	 */
	public String getDepName() {
		return depName;
	}

	/**
	 * @param depName
	 *            the depName to set
	 */
	public void setDepName(String depName) {
		this.depName = depName;
	}

	/**
	 * @return the logDate
	 */
	public String getLogDate() {
		return logDate;
	}

	/**
	 * @param logDate
	 *            the logDate to set
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
	 * @param meetDate
	 *            the meetDate to set
	 */
	public void setMeetDate(String meetDate) {
		this.meetDate = meetDate;
	}

	/**
	 * @return the repastNum
	 */
	public Long getRepastNum() {
		return repastNum;
	}

	/**
	 * @param repastNum
	 *            the repastNum to set
	 */
	public void setRepastNum(Long repastNum) {
		this.repastNum = repastNum;
	}

	/**
	 * @return the roomNum
	 */
	public Long getRoomNum() {
		return roomNum;
	}

	/**
	 * @param roomNum
	 *            the roomNum to set
	 */
	public void setRoomNum(Long roomNum) {
		this.roomNum = roomNum;
	}

	/**
	 * @return the meetNote
	 */
	public String getMeetNote() {
		return meetNote;
	}

	/**
	 * @param meetNote
	 *            the meetNote to set
	 */
	public void setMeetNote(String meetNote) {
		this.meetNote = meetNote;
	}

	/**
	 * @return the repastPlan
	 */
	public String getRepastPlan() {
		return repastPlan;
	}

	/**
	 * @param repastPlan
	 *            the repastPlan to set
	 */
	public void setRepastPlan(String repastPlan) {
		this.repastPlan = repastPlan;
	}

	/**
	 * @return the roomPlan
	 */
	public String getRoomPlan() {
		return roomPlan;
	}

	/**
	 * @param roomPlan
	 *            the roomPlan to set
	 */
	public void setRoomPlan(String roomPlan) {
		this.roomPlan = roomPlan;
	}

	/**
	 * @return the other
	 */
	public String getOther() {
		return other;
	}

	/**
	 * @param other
	 *            the other to set
	 */
	public void setOther(String other) {
		this.other = other;
	}

	/**
	 * @return the repastBz
	 */
	public Double getRepastBz() {
		return repastBz;
	}

	/**
	 * @param repastBz the repastBz to set
	 */
	public void setRepastBz(Double repastBz) {
		this.repastBz = repastBz;
	}

	/**
	 * @return the roomBz
	 */
	public Double getRoomBz() {
		return roomBz;
	}

	/**
	 * @param roomBz the roomBz to set
	 */
	public void setRoomBz(Double roomBz) {
		this.roomBz = roomBz;
	}

	/**
	 * @return the payoutBz
	 */
	public Double getPayoutBz() {
		return payoutBz;
	}

	/**
	 * @param payoutBz the payoutBz to set
	 */
	public void setPayoutBz(Double payoutBz) {
		this.payoutBz = payoutBz;
	}

	/**
	 * @return the payout
	 */
	public Double getPayout() {
		return payout;
	}

	/**
	 * @param payout the payout to set
	 */
	public void setPayout(Double payout) {
		this.payout = payout;
	}

	/**
	 * @return the balance
	 */
	public Double getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}

	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
