package power.ejb.hr.ca;

public class DeptleaveStatisticsQueryInfo implements java.io.Serializable{

	/** 请假id */
	private String vacationId;
	/** 部门id */
	private String deptId;
	/** 部门名称 */
	private String deptName;
	/** 员工id */
	private String empId;
	/** 员工名称 */
	private String chsName;
	/** 假别ID */
	private String vacationTypeId;
	/** 假别 */
	private String vacationType;
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	/** 请假天数 */
	private String vacationDays;
	/** 请假时长 */
	private String vacationTime;
	/** 原因 */
	private String reason;
	/** 去向 */
	private String whither;
	/** 备注 */
	private String memo;
	/** 是否销假 */
	private String ifClear;
	/** 销假时间 */
	private String clearDate;
	/** 审批状态 */
	private String signState;
	/** 行数计数 */
    private Integer cntRow;
	/**
	 * @return the deptId
	 */
	public String getDeptId() {
		return deptId;
	}
	/**
	 * @param deptId the deptId to set
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
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
	 * @return the empId
	 */
	public String getEmpId() {
		return empId;
	}
	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
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
	 * @return the vacationTypeId
	 */
	public String getVacationTypeId() {
		return vacationTypeId;
	}
	/**
	 * @param vacationTypeId the vacationTypeId to set
	 */
	public void setVacationTypeId(String vacationTypeId) {
		this.vacationTypeId = vacationTypeId;
	}
	/**
	 * @return the vacationType
	 */
	public String getVacationType() {
		return vacationType;
	}
	/**
	 * @param vacationType the vacationType to set
	 */
	public void setVacationType(String vacationType) {
		this.vacationType = vacationType;
	}
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the vacationDays
	 */
	public String getVacationDays() {
		return vacationDays;
	}
	/**
	 * @param vacationDays the vacationDays to set
	 */
	public void setVacationDays(String vacationDays) {
		this.vacationDays = vacationDays;
	}
	/**
	 * @return the vacationTime
	 */
	public String getVacationTime() {
		return vacationTime;
	}
	/**
	 * @param vacationTime the vacationTime to set
	 */
	public void setVacationTime(String vacationTime) {
		this.vacationTime = vacationTime;
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
	 * @return the whither
	 */
	public String getWhither() {
		return whither;
	}
	/**
	 * @param whither the whither to set
	 */
	public void setWhither(String whither) {
		this.whither = whither;
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
	 * @return the ifClear
	 */
	public String getIfClear() {
		return ifClear;
	}
	/**
	 * @param ifClear the ifClear to set
	 */
	public void setIfClear(String ifClear) {
		this.ifClear = ifClear;
	}
	/**
	 * @return the clearDate
	 */
	public String getClearDate() {
		return clearDate;
	}
	/**
	 * @param clearDate the clearDate to set
	 */
	public void setClearDate(String clearDate) {
		this.clearDate = clearDate;
	}
	/**
	 * @return the signState
	 */
	public String getSignState() {
		return signState;
	}
	/**
	 * @param signState the signState to set
	 */
	public void setSignState(String signState) {
		this.signState = signState;
	}
	/**
	 * @return the vacationId
	 */
	public String getVacationId() {
		return vacationId;
	}
	/**
	 * @param vacationId the vacationId to set
	 */
	public void setVacationId(String vacationId) {
		this.vacationId = vacationId;
	}
	/**
	 * @return the cntRow
	 */
	public Integer getCntRow() {
		return cntRow;
	}
	/**
	 * @param cntRow the cntRow to set
	 */
	public void setCntRow(Integer cntRow) {
		this.cntRow = cntRow;
	}
}
