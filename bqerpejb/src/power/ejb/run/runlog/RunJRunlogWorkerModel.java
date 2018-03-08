package power.ejb.run.runlog;

import java.util.Date;

@SuppressWarnings("serial")

public class RunJRunlogWorkerModel implements java.io.Serializable{
	private Long runlogWorkerId;
	private Long runLogid;
	private String woWorktype;
	private String bookedEmployee;
	private String workerName;
	private String operateBy;
	private String operateTime;
	private String operateMemo;
	private String isUse;
	private String enterpriseCode;
	private String specialCode;
	private String specialName;
	private Long shiftId;
	private String shiftName;
	private String runLogno;
	public Long getRunlogWorkerId() {
		return runlogWorkerId;
	}
	public void setRunlogWorkerId(Long runlogWorkerId) {
		this.runlogWorkerId = runlogWorkerId;
	}
	public Long getRunLogid() {
		return runLogid;
	}
	public void setRunLogid(Long runLogid) {
		this.runLogid = runLogid;
	}
	public String getWoWorktype() {
		return woWorktype;
	}
	public void setWoWorktype(String woWorktype) {
		this.woWorktype = woWorktype;
	}
	public String getBookedEmployee() {
		return bookedEmployee;
	}
	public void setBookedEmployee(String bookedEmployee) {
		this.bookedEmployee = bookedEmployee;
	}
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
	public String getOperateBy() {
		return operateBy;
	}
	public void setOperateBy(String operateBy) {
		this.operateBy = operateBy;
	}
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	public String getOperateMemo() {
		return operateMemo;
	}
	public void setOperateMemo(String operateMemo) {
		this.operateMemo = operateMemo;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public String getSpecialCode() {
		return specialCode;
	}
	public void setSpecialCode(String specialCode) {
		this.specialCode = specialCode;
	}
	public String getSpecialName() {
		return specialName;
	}
	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}
	public Long getShiftId() {
		return shiftId;
	}
	public void setShiftId(Long shiftId) {
		this.shiftId = shiftId;
	}
	public String getShiftName() {
		return shiftName;
	}
	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}
	public String getRunLogno() {
		return runLogno;
	}
	public void setRunLogno(String runLogno) {
		this.runLogno = runLogno;
	}
}
