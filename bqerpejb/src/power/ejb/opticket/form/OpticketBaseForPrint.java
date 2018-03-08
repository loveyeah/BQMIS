package power.ejb.opticket.form;

import java.util.Date;

/**
 * @author sltang
 *
 */
public class OpticketBaseForPrint implements java.io.Serializable{
	/**
	 *操作票编码
	 */
	private String opticketCode;
	/**
	 *操作票名称
	 */
	private String opticketName;
	/**
	 *专业
	 */
	private String specialityName;
	/**
	 *操作任务名称
	 */
	private String operateTaskName;
	/**
	 *开始时间
	 */
	private String startTime;
	/**
	 *结束时间
	 */
	private String endTime;
	/**
	 *备注
	 */
	private String memo;
	/**
	 *操作人姓名
	 */
	private String operatorName;
	

	/**
	 *监工姓名
	 */
	private String protectorName;
	/**
	 *值长姓名
	 */
	private String classLeaderName;
	/**
	 *值班负责人姓名
	 */
	private String chargeName;
	/**
	 *操作方式Y为监护操作，N为单人操作 
	 */
	private String isSingle;
	/**
	 *操作票类别00××××××为电气倒闸操作票，01××××××为热力/水力机械操作票 
	 */
	private String opticketType;

	
	public String getOpticketType() {
		return opticketType;
	}
	public void setOpticketType(String opticketType) {
		this.opticketType = opticketType;
	}
	public String getIsSingle() {
		return isSingle;
	}
	public void setIsSingle(String isSingle) {
		this.isSingle = isSingle;
	}
	public String getOpticketCode() {
		return opticketCode;
	}
	public void setOpticketCode(String opticketCode) {
		this.opticketCode = opticketCode;
	}
	public String getOpticketName() {
		return opticketName;
	}
	public void setOpticketName(String opticketName) {
		this.opticketName = opticketName;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public String getOperateTaskName() {
		return operateTaskName;
	}
	public void setOperateTaskName(String operateTaskName) {
		this.operateTaskName = operateTaskName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getProtectorName() {
		return protectorName;
	}
	public void setProtectorName(String protectorName) {
		this.protectorName = protectorName;
	}
	public String getClassLeaderName() {
		return classLeaderName;
	}
	public void setClassLeaderName(String classLeaderName) {
		this.classLeaderName = classLeaderName;
	}
	public String getChargeName() {
		return chargeName;
	}
	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}
}
