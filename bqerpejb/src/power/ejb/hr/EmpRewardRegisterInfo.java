/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

/**
 * 员工奖惩登记 entity.
 * 
 * @author zhaozhijie
 */
public class EmpRewardRegisterInfo  implements java.io.Serializable {

	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 员工姓名 */
	private String empName;
	/** 员工工号 */
	private String empCode;
	/** 所属部门 */
	private String deptName;
	/** 奖惩类别名称 */
	private String rewardsPunish;
	/** 奖惩类别 */
	private String rewardsPunishType;
	/** 奖惩日期 */
	private String rewardsDate;
	/** 奖惩原因 */
	private String rewardsReason;
	/** 备注 */
	private String memo;
	/** 职工奖惩ID */
	private String rewardsPunishID;
	/** 奖惩类别ID */
	private String rewardsPunishId;
	/** 人员ID */
	private String empID;
	/** 修改时间 */
	private String lastModifyDate;
	/** 判断新增，更新 */
	private String flag = "0";

	/**
	 * @return 判断新增，更新，删除
	 */
	public String getFlag() {
		return flag;
	}
	/**
	 * @param 判断新增，更新，删除
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
	/**
	 * @return 修改时间
	 */
	public String getLastModifyDate() {
		return lastModifyDate;
	}
	/**
	 * @param 修改时间
	 */
	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	/**
	 * @return 员工姓名
	 */
	public String getEmpName() {
		return empName;
	}
	/**
	 * @param 员工姓名
	 */
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	/**
	 * @return 员工工号
	 */
	public String getEmpCode() {
		return empCode;
	}
	/**
	 * @param 员工工号
	 */
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	/**
	 * @return 所属部门
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 * @param 所属部门
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	/**
	 * @return 奖惩类别名称
	 */
	public String getRewardsPunish() {
		return rewardsPunish;
	}
	/**
	 * @param 奖惩类别名称
	 */
	public void setRewardsPunish(String rewardsPunish) {
		this.rewardsPunish = rewardsPunish;
	}
	/**
	 * @return 奖惩类别
	 */
	public String getRewardsPunishType() {
		return rewardsPunishType;
	}
	/**
	 * @param 奖惩类别
	 */
	public void setRewardsPunishType(String rewardsPunishType) {
		this.rewardsPunishType = rewardsPunishType;
	}
	/**
	 * @return 奖惩日期
	 */
	public String getRewardsDate() {
		return rewardsDate;
	}
	/**
	 * @param 奖惩日期
	 */
	public void setRewardsDate(String rewardsDate) {
		this.rewardsDate = rewardsDate;
	}
	/**
	 * @return 奖惩原因
	 */
	public String getRewardsReason() {
		return rewardsReason;
	}
	/**
	 * @param 奖惩原因
	 */
	public void setRewardsReason(String rewardsReason) {
		this.rewardsReason = rewardsReason;
	}
	/**
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param 备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * @return 人员ID
	 */
	public String getEmpID() {
		return empID;
	}
	/**
	 * @param 人员ID
	 */
	public void setEmpID(String empID) {
		this.empID = empID;
	}
	/**
	 * @return 职工奖惩ID
	 */
	public String getRewardsPunishID() {
		return rewardsPunishID;
	}
	/**
	 * @param 职工奖惩ID
	 */
	public void setRewardsPunishID(String rewardsPunishID) {
		this.rewardsPunishID = rewardsPunishID;
	}
	/**
	 * @return 奖惩类别ID
	 */
	public String getRewardsPunishId() {
		return rewardsPunishId;
	}
	/**
	 * @param 奖惩类别ID
	 */
	public void setRewardsPunishId(String rewardsPunishId) {
		this.rewardsPunishId = rewardsPunishId;
	}

}
