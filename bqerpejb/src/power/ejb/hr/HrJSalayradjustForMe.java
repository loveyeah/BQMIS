/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.hr;

import java.util.Date;

/**
 * 薪酬变动单申报bean
 * @author wujiao
 * 
 */
public class HrJSalayradjustForMe implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	// Fields
	/** 薪酬变动ID */
	private Long salayradjustid;
	/** 员工姓名 */
	private String chsName;
	/** 起薪日期 */
	private String doDate;
	/** 部门名称 */
	private String deptName;
	/** 变更前执行岗级 */
	private Long oldCheckStationGrade;
    /** 变更后执行岗级 */
	private Long newCheckStationGrade;
	/** 变更前标准岗级 */
	private Long oldStationGrade;
	/** 变更后标准岗级 */
	private Long newStationGrade;
	/** 变更前薪级 */
	private Long oldSalaryGrade;
	/** 变更后薪级 */
	private Long newSalaryGrade;
    /** 变动类别 */
	private String adjustType;
	/** 岗薪变化类别 */
	private String stationChangeType;
	/** 原因 */
	private String reason;
	/** 单据状态 */
	private String dcmState;
	/** 备注 */
	private String memo;
	/** 修改时间,排它用 */
	private String updateTime;
	/**
	 * @return the salayradjustid
	 */
	public Long getSalayradjustid() {
		return salayradjustid;
	}
	/**
	 * @param salayradjustid the salayradjustid to set
	 */
	public void setSalayradjustid(Long salayradjustid) {
		this.salayradjustid = salayradjustid;
	}
	/**
	 * @return the chsName
	 */
	public String getChsName() {
		return chsName;
	}
	/**
	 * @param chsName the chsName to set
	 */
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}
	/**
	 * @return the doDate
	 */
	public String getDoDate() {
		return doDate;
	}
	/**
	 * @param doDate the doDate to set
	 */
	public void setDoDate(String doDate) {
		this.doDate = doDate;
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
	 * @return the oldCheckStationGrade
	 */
	public Long getOldCheckStationGrade() {
		return oldCheckStationGrade;
	}
	/**
	 * @param oldCheckStationGrade the oldCheckStationGrade to set
	 */
	public void setOldCheckStationGrade(Long oldCheckStationGrade) {
		this.oldCheckStationGrade = oldCheckStationGrade;
	}
	/**
	 * @return the newCheckStationGrade
	 */
	public Long getNewCheckStationGrade() {
		return newCheckStationGrade;
	}
	/**
	 * @param newCheckStationGrade the newCheckStationGrade to set
	 */
	public void setNewCheckStationGrade(Long newCheckStationGrade) {
		this.newCheckStationGrade = newCheckStationGrade;
	}
	/**
	 * @return the oldStationGrade
	 */
	public Long getOldStationGrade() {
		return oldStationGrade;
	}
	/**
	 * @param oldStationGrade the oldStationGrade to set
	 */
	public void setOldStationGrade(Long oldStationGrade) {
		this.oldStationGrade = oldStationGrade;
	}
	/**
	 * @return the newStationGrade
	 */
	public Long getNewStationGrade() {
		return newStationGrade;
	}
	/**
	 * @param newStationGrade the newStationGrade to set
	 */
	public void setNewStationGrade(Long newStationGrade) {
		this.newStationGrade = newStationGrade;
	}
	/**
	 * @return the oldSalaryGrade
	 */
	public Long getOldSalaryGrade() {
		return oldSalaryGrade;
	}
	/**
	 * @param oldSalaryGrade the oldSalaryGrade to set
	 */
	public void setOldSalaryGrade(Long oldSalaryGrade) {
		this.oldSalaryGrade = oldSalaryGrade;
	}
	/**
	 * @return the newSalaryGrade
	 */
	public Long getNewSalaryGrade() {
		return newSalaryGrade;
	}
	/**
	 * @param newSalaryGrade the newSalaryGrade to set
	 */
	public void setNewSalaryGrade(Long newSalaryGrade) {
		this.newSalaryGrade = newSalaryGrade;
	}
	/**
	 * @return the adjustType
	 */
	public String getAdjustType() {
		return adjustType;
	}
	/**
	 * @param adjustType the adjustType to set
	 */
	public void setAdjustType(String adjustType) {
		this.adjustType = adjustType;
	}
	/**
	 * @return the stationChangeType
	 */
	public String getStationChangeType() {
		return stationChangeType;
	}
	/**
	 * @param stationChangeType the stationChangeType to set
	 */
	public void setStationChangeType(String stationChangeType) {
		this.stationChangeType = stationChangeType;
	}
	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return the dcmState
	 */
	public String getDcmState() {
		return dcmState;
	}
	/**
	 * @param dcmState the dcmState to set
	 */
	public void setDcmState(String dcmState) {
		this.dcmState = dcmState;
	}
	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
    
}
