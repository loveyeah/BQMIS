/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.web.birt.bean.bqmis;

import java.util.List;

/**
 * 电气、热力机械倒闸操作票数据
 * 
 * @author LiuYingwen
 */
public class OperateTicketBean {

	/* 操作票名称 */
	private String opticketName = "";
	/* 检修单位 */
	private String repairUnit = "";
	/* 检修班组 */
	private String repairDept = "";
	/* 工作地点和内容 */
	private String workContent = "";
	/* 专业 */
	private String specialityName = "";
	/* 操作票号 */
	private String opticketCode = "";
	/* 起始时间 */
	private String startTime = "";
	/* 终止时间 */
	private String endTime = "";
	/* 执行任务 */
	private String operateTaskName = "";
	/* 备注 */
	private String memo = "";
	/* 操作人 */
	private String operatorName = "";
	/* 监护人 (工作监护人) */
	private String chargeBy = "";
	/* 值班负责人 */
	private String chargeName = "";
	/* 值长 */
	private String watchMan = "";
	/* 监护操作 */
	private String select1 = "";
	/* 单人操作 */
	private String select2 = "";
	/* 安措列表 */
	private List<OperateDetailBean> operateDetailList;

	/**
	 * 操作票名称
	 * 
	 * @return the opticketName
	 */
	public String getOpticketName() {
		return opticketName;
	}

	/**
	 * 操作票名称
	 * 
	 * @param opticketName
	 *            the opticketName to set
	 */
	public void setOpticketName(String opticketName) {
		this.opticketName = opticketName;
	}

	/**
	 * 专业
	 * 
	 * @return the specialityName
	 */
	public String getSpecialityName() {
		return specialityName;
	}

	/**
	 * 专业
	 * 
	 * @param specialityName
	 *            the specialityName to set
	 */
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	/**
	 * 操作票号
	 * 
	 * @return the opticketCode
	 */
	public String getOpticketCode() {
		return opticketCode;
	}

	/**
	 * 操作票号
	 * 
	 * @param opticketCode
	 *            the opticketCode to set
	 */
	public void setOpticketCode(String opticketCode) {
		this.opticketCode = opticketCode;
	}

	/**
	 * 起始时间
	 * 
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * 起始时间
	 * 
	 * @param startTime
	 *            the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * 终止时间
	 * 
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}

	/**
	 * 终止时间
	 * 
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * 执行任务
	 * 
	 * @return the operateTaskName
	 */
	public String getOperateTaskName() {
		return operateTaskName;
	}

	/**
	 * 执行任务
	 * 
	 * @param operateTaskName
	 *            the operateTaskName to set
	 */
	public void setOperateTaskName(String operateTaskName) {
		this.operateTaskName = operateTaskName;
	}

	/**
	 * 备注
	 * 
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 备注
	 * 
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 操作人
	 * 
	 * @return the operatorName
	 */
	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * 操作人
	 * 
	 * @param operatorName
	 *            the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * 监护人
	 * 
	 * @return the chargeBy
	 */
	public String getChargeBy() {
		return chargeBy;
	}

	/**
	 * 监护人
	 * 
	 * @param chargeBy
	 *            the chargeBy to set
	 */
	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	/**
	 * 值长
	 * 
	 * @return the watchMan
	 */
	public String getWatchMan() {
		return watchMan;
	}

	/**
	 * 值长
	 * 
	 * @param watchMan
	 *            the watchMan to set
	 */
	public void setWatchMan(String watchMan) {
		this.watchMan = watchMan;
	}

	/**
	 * 安措列表
	 * 
	 * @return the operateDetailList
	 */
	public List<OperateDetailBean> getOperateDetailList() {
		return operateDetailList;
	}

	/**
	 * 安措列表
	 * 
	 * @param operateDetailList
	 *            the operateDetailList to set
	 */
	public void setOperateDetailList(List<OperateDetailBean> operateDetailList) {
		this.operateDetailList = operateDetailList;
	}

	/**
	 * @return the repairUnit
	 */
	public String getRepairUnit() {
		return repairUnit;
	}

	/**
	 * @param repairUnit
	 *            the repairUnit to set
	 */
	public void setRepairUnit(String repairUnit) {
		this.repairUnit = repairUnit;
	}

	/**
	 * @return the repairDept
	 */
	public String getRepairDept() {
		return repairDept;
	}

	/**
	 * @param repairDept
	 *            the repairDept to set
	 */
	public void setRepairDept(String repairDept) {
		this.repairDept = repairDept;
	}

	/**
	 * @return the workContent
	 */
	public String getWorkContent() {
		return workContent;
	}

	/**
	 * @param workContent
	 *            the workContent to set
	 */
	public void setWorkContent(String workContent) {
		this.workContent = workContent;
	}

	/**
	 * @return the chargeName
	 */
	public String getChargeName() {
		return chargeName;
	}

	/**
	 * @param chargeName
	 *            the chargeName to set
	 */
	public void setChargeName(String chargeName) {
		this.chargeName = chargeName;
	}

	/**
	 * @return the select1
	 */
	public String getSelect1() {
		return select1;
	}

	/**
	 * @param select1 the select1 to set
	 */
	public void setSelect1(String select1) {
		this.select1 = select1;
	}

	/**
	 * @return the select2
	 */
	public String getSelect2() {
		return select2;
	}

	/**
	 * @param select2 the select2 to set
	 */
	public void setSelect2(String select2) {
		this.select2 = select2;
	}

}