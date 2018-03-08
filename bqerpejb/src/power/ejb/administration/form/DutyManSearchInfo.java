/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.administration.form;

import java.io.Serializable;


/**
 * DutyManSearchInfo 值班人员查询.
 * 
 * @author zhaomingjian
 */
@SuppressWarnings("serial")
public class DutyManSearchInfo implements Serializable {
	//fields
	/**值班人员名*/
    private String dutyManName;
    /**值班日期*/
    private String dutyDate;
    /**值班时间*/
    private String dutyTime;
    /**位置*/
    private String position;
    /**工作类型名*/
    private String dutyTypeName;
    /**子工作类型名*/
    private String subWorkTypeName;
    /**替代人名*/
    private String replaceManName;
    /**请假人名*/
    private String leaveManName;
    /**请假原因*/
    private String reason;
    
    //properties accessor
    /**
     * @return dutyManName 值班人员名
     */
	public String getDutyManName() {
		return dutyManName;
	}
	/**
	 * 
	 * @param dutyManName 值班人员名
	 */
	public void setDutyManName(String dutyManName) {
		this.dutyManName = dutyManName;
	}
	/**
	 * 
	 * @return dutyDate 值班日期
	 */
	public String getDutyDate() {
		return dutyDate;
	}
	/**
	 * 
	 * @param dutyDate 值班日期
	 */
	public void setDutyDate(String dutyDate) {
		this.dutyDate = dutyDate;
	}
	/**
	 * 
	 * @return dutyTime 值班时间
	 */
	public String getDutyTime() {
		return dutyTime;
	}
	/**
	 * 
	 * @param dutyTime  值班时间
	 */
	public void setDutyTime(String dutyTime) {
		this.dutyTime = dutyTime;
	}
	/**
	 * 
	 * @return  position 位置
	 */
	public String getPosition() {
		return position;
	}
	/**
	 * 
	 * @param position  位置
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	/**
	 * 
	 * @return   dutyTypeName  工作类型名
	 */
	public String getDutyTypeName() {
		return dutyTypeName;
	}
	/**
	 * 
	 * @param dutyTypeName  工作类型名
	 */
	public void setDutyTypeName(String dutyTypeName) {
		this.dutyTypeName = dutyTypeName;
	}
	/**
	 * 
	 * @return subWorkTypeName  子工作类型名
	 */
	public String getSubWorkTypeName() {
		return subWorkTypeName;
	}
	/**
	 * 
	 * @param subWorkTypeName  子工作类型名
	 */
	public void setSubWorkTypeName(String subWorkTypeName) {
		this.subWorkTypeName = subWorkTypeName;
	}
	/**
	 * 
	 * @return  replaceManName  替代人名
	 */
	public String getReplaceManName() {
		return replaceManName;
	}
	/**
	 * 
	 * @param replaceManName  替代人名
	 */
	public void setReplaceManName(String replaceManName) {
		this.replaceManName = replaceManName;
	}
	/**
	 * 
	 * @return leaveManName  请假人
	 */
	public String getLeaveManName() {
		return leaveManName;
	}
	/**
	 * 
	 * @param leaveManName  请假人
	 */
	public void setLeaveManName(String leaveManName) {
		this.leaveManName = leaveManName;
	}
	/**
	 * 
	 * @return  reason 请假原因
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * 
	 * @param reason  请假原因
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
}
