package power.ejb.run.protectinoutapply.form;

import java.util.Date;

import power.ejb.run.protectinoutapply.RunJProtectinoutapply;

@SuppressWarnings("serial")
public class ProtectInOutApplyInfo implements java.io.Serializable{

	private RunJProtectinoutapply power;
	/** 申请人*/
	private String applyName;
	/** 申请部门*/
	private String applyDeptName;
	/** 申请时间*/
	private String applyDate;
	/** 设备名称*/
	private String equName;
	/** 申请开工时间*/
	private String applyStartDate;
	/** 申请完工时间*/
	private String applyEndDate;
	/** 状态名称*/
	private String statusName;
	//----------------------------
	
	/**  执行人  */
	private String executeByName;
	
	/** 监护人 */
	private String keeperName;
	
	/** 许可人 */
	private String permitByName;
	
	/**  审核人 */
	private String checkupByName;
	
	/** 保护投入时间 */
	private String strProtectInDate;
	
	/** 保护退出时间 */
	private String strProtectOutDate;
	
	/** 批准开工时间 */
	private String strApproveStartDate;
	
	/** 批准完工时间 */
	private String strApproveEndDate;
	
	
//	private String isIn;
//	
//	
//	private String isSelect;
//	
//	
//	private String relativeNo;
	
//	public String getIsIn() {
//		return isIn;
//	}
//	public void setIsIn(String isIn) {
//		this.isIn = isIn;
//	}
	public RunJProtectinoutapply getPower() {
		return power;
	}
	public void setPower(RunJProtectinoutapply power) {
		this.power = power;
	}
	public String getApplyName() {
		return applyName;
	}
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	public String getApplyDeptName() {
		return applyDeptName;
	}
	public void setApplyDeptName(String applyDeptName) {
		this.applyDeptName = applyDeptName;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getEquName() {
		return equName;
	}
	public void setEquName(String equName) {
		this.equName = equName;
	}
	public String getApplyStartDate() {
		return applyStartDate;
	}
	public void setApplyStartDate(String applyStartDate) {
		this.applyStartDate = applyStartDate;
	}
	public String getApplyEndDate() {
		return applyEndDate;
	}
	public void setApplyEndDate(String applyEndDate) {
		this.applyEndDate = applyEndDate;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getExecuteByName() {
		return executeByName;
	}
	public void setExecuteByName(String executeByName) {
		this.executeByName = executeByName;
	}
	public String getKeeperName() {
		return keeperName;
	}
	public void setKeeperName(String keeperName) {
		this.keeperName = keeperName;
	}
	public String getPermitByName() {
		return permitByName;
	}
	public void setPermitByName(String permitByName) {
		this.permitByName = permitByName;
	}
	public String getCheckupByName() {
		return checkupByName;
	}
	public void setCheckupByName(String checkupByName) {
		this.checkupByName = checkupByName;
	}
	public String getStrProtectInDate() {
		return strProtectInDate;
	}
	public void setStrProtectInDate(String strProtectInDate) {
		this.strProtectInDate = strProtectInDate;
	}
	public String getStrProtectOutDate() {
		return strProtectOutDate;
	}
	public void setStrProtectOutDate(String strProtectOutDate) {
		this.strProtectOutDate = strProtectOutDate;
	}
	public String getStrApproveStartDate() {
		return strApproveStartDate;
	}
	public void setStrApproveStartDate(String strApproveStartDate) {
		this.strApproveStartDate = strApproveStartDate;
	}

	public String getStrApproveEndDate() {
		return strApproveEndDate;
	}
	public void setStrApproveEndDate(String strApproveEndDate) {
		this.strApproveEndDate = strApproveEndDate;
	}
//	public String getIsSelect() {
//		return isSelect;
//	}
//	public void setIsSelect(String isSelect) {
//		this.isSelect = isSelect;
//	}
//	public String getRelativeNo() {
//		return relativeNo;
//	}
//	public void setRelativeNo(String relativeNo) {
//		this.relativeNo = relativeNo;
//	}

}
