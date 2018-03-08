package power.ejb.opticket.form;

import java.util.Date;

import power.ejb.opticket.RunJOpticket;

public class OpInfoForm implements java.io.Serializable{
	private String opticketCode;
	private String opticketType;
	private Long operateTaskId;
	private String appendixAddr;
	private String opticketStatus;
	private String planStartTime;
	private String planEndTime;
	private String operateTaskName;
	private String specialityCode;
	private String memo;
	private String operatorMan;
	private String isSingle;
	private String operatorName;
	private String opticketName;
	private String workTicketNo;
	private String applyNo;
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public String getWorkTicketNo() {
		return workTicketNo;
	}
	public void setWorkTicketNo(String workTicketNo) {
		this.workTicketNo = workTicketNo;
	}
	public String getOpticketName() {
		return opticketName;
	}
	public void setOpticketName(String opticketName) {
		this.opticketName = opticketName;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getOpticketCode() {
		return opticketCode;
	}
	public void setOpticketCode(String opticketCode) {
		this.opticketCode = opticketCode;
	}
	public String getOpticketType() {
		return opticketType;
	}
	public void setOpticketType(String opticketType) {
		this.opticketType = opticketType;
	}
	public Long getOperateTaskId() {
		return operateTaskId;
	}
	public void setOperateTaskId(Long operateTaskId) {
		this.operateTaskId = operateTaskId;
	}
	public String getAppendixAddr() {
		return appendixAddr;
	}
	public void setAppendixAddr(String appendixAddr) {
		this.appendixAddr = appendixAddr;
	}
	public String getOpticketStatus() {
		return opticketStatus;
	}
	public void setOpticketStatus(String opticketStatus) {
		this.opticketStatus = opticketStatus;
	}
	public String getPlanStartTime() {
		return planStartTime;
	}
	public void setPlanStartTime(String planStartTime) {
		this.planStartTime = planStartTime;
	}
	public String getPlanEndTime() {
		return planEndTime;
	}
	public void setPlanEndTime(String planEndTime) {
		this.planEndTime = planEndTime;
	}
	public String getOperateTaskName() {
		return operateTaskName;
	}
	public void setOperateTaskName(String operateTaskName) {
		this.operateTaskName = operateTaskName;
	}
	public String getSpecialityCode() {
		return specialityCode;
	}
	public void setSpecialityCode(String specialityCode) {
		this.specialityCode = specialityCode;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getOperatorMan() {
		return operatorMan;
	}
	public void setOperatorMan(String operatorMan) {
		this.operatorMan = operatorMan;
	}
	public String getIsSingle() {
		return isSingle;
	}
	public void setIsSingle(String isSingle) {
		this.isSingle = isSingle;
	}
}
