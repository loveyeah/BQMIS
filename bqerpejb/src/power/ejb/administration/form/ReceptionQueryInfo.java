/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.administration.form;

/**
 * 接待审批查询bean
 * @author zhengzhipeng
 * 
 */
public class ReceptionQueryInfo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	// Fields
	/** 审批单号 */
	private String applyId;
	/** 申请部门 */
	private String applyDeptName;
	/** 申请人 */
	private String applyManName;
	/** 登记日期 */
	private String logDate;
    /** 接待日期 */
	private String meetDate;
	/** 接待说明 */
	private String meetNote;
	/** 就餐人数 */
	private String repastNum;
	/** 就餐标准 */
	private String repastBz;
	/** 就餐安排 */
	private String repastPlan;
    /** 住宿人数 */
	private String roomNum;
	/** 住宿标准 */
	private String roomBz;
	/** 住宿安排 */
	private String roomPlan;
	/** 标准支出 */
	private String payoutBz;
	/** 实际支出 */
	private String payout;
    /** 差额 */
	private String balance;
	/** 其他 */
	private String other;
	/** 单据状态 */
	private String dcmStatus;
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
	 * @return the applyDeptName
	 */
	public String getApplyDeptName() {
		return applyDeptName;
	}
	/**
	 * @param applyDeptName the applyDeptName to set
	 */
	public void setApplyDeptName(String applyDeptName) {
		this.applyDeptName = applyDeptName;
	}
	/**
	 * @return the applyManName
	 */
	public String getApplyManName() {
		return applyManName;
	}
	/**
	 * @param applyManName the applyManName to set
	 */
	public void setApplyManName(String applyManName) {
		this.applyManName = applyManName;
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
	 * @return the dcmStatus
	 */
	public String getDcmStatus() {
		return dcmStatus;
	}
	/**
	 * @param dcmStatus the dcmStatus to set
	 */
	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}
}
