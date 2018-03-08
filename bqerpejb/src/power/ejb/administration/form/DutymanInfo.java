/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.administration.form;

import java.io.Serializable;


/**
 * DutyManInfo bean.
 * 
 * @author sufeiyu
 */
@SuppressWarnings("serial")
public class DutymanInfo implements Serializable {
/**值班人员登记表**/
	
	/**序号**/
	private Long personId;
	/**工作类别**/
	private String worktypeCode;
	/**子工作类别code**/
	private String subWorktypeCode;
	/**值班人**/
	private String dutyman;
	/**值别**/
	private String dutytype;
    /**值班日期**/
    private String dutytime;
    /**岗位**/
    private String position;
    /**值别名**/
    private String dutytypeName;
    /**子工作类别名**/
    private String subWorktypeName;
    /**替班人员**/
    private String replaceman;
    /**缺勤人员**/
    private String leaveman;
    /**缺勤原因**/
    private String reason;
    /**修改时间**/    
    private String updateTime;
	
	/**
	 * @return the worktypeCode
	 */
	public String getWorktypeCode() {
		return worktypeCode;
	}
	/**
	 * @param worktypeCode the worktypeCode to set
	 */
	public void setWorktypeCode(String worktypeCode) {
		this.worktypeCode = worktypeCode;
	}
	/**
	 * @return the subWorktypeCode
	 */
	public String getSubWorktypeCode() {
		return subWorktypeCode;
	}
	/**
	 * @param subWorktypeCode the subWorktypeCode to set
	 */
	public void setSubWorktypeCode(String subWorktypeCode) {
		this.subWorktypeCode = subWorktypeCode;
	}
	/**
	 * @return the dutyman
	 */
	public String getDutyman() {
		return dutyman;
	}
	/**
	 * @param dutyman the dutyman to set
	 */
	public void setDutyman(String dutyman) {
		this.dutyman = dutyman;
	}
	/**
	 * @return the dutytype
	 */
	public String getDutytype() {
		return dutytype;
	}
	/**
	 * @param dutytype the dutytype to set
	 */
	public void setDutytype(String dutytype) {
		this.dutytype = dutytype;
	}
	/**
	 * @return the dutytime
	 */
	public String getDutytime() {
		return dutytime;
	}
	/**
	 * @param dutytime the dutytime to set
	 */
	public void setDutytime(String dutytime) {
		this.dutytime = dutytime;
	}
	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	/**
	 * @return the dutytypeName
	 */
	public String getDutytypeName() {
		return dutytypeName;
	}
	/**
	 * @param dutytypeName the dutytypeName to set
	 */
	public void setDutytypeName(String dutytypeName) {
		this.dutytypeName = dutytypeName;
	}
	/**
	 * @return the subWorktypeName
	 */
	public String getSubWorktypeName() {
		return subWorktypeName;
	}
	/**
	 * @param subWorktypeName the subWorktypeName to set
	 */
	public void setSubWorktypeName(String subWorktypeName) {
		this.subWorktypeName = subWorktypeName;
	}
	/**
	 * @return the replaceman
	 */
	public String getReplaceman() {
		return replaceman;
	}
	/**
	 * @param replaceman the replaceman to set
	 */
	public void setReplaceman(String replaceman) {
		this.replaceman = replaceman;
	}
	/**
	 * @return the leaveman
	 */
	public String getLeaveman() {
		return leaveman;
	}
	/**
	 * @param leaveman the leaveman to set
	 */
	public void setLeaveman(String leaveman) {
		this.leaveman = leaveman;
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
	/**
	 * @return the personId
	 */
	public Long getPersonId() {
		return personId;
	}
	/**
	 * @param personId the personId to set
	 */
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
}
