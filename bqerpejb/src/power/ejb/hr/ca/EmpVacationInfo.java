/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

/**
 * 员工请假登记 entity.
 * 
 * @author zhaozhijie
 */
public class EmpVacationInfo  implements java.io.Serializable {

	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 请假id */
	private String vacationId;
	/** 审批状态 */
	private String signState;
	/** 员工姓名 */
	private String empName;
	/** 员工ID */
	private String empId;
	/** 所属部门 */
	private String deptName;
	/** 所属部门ID */
	private String deptId;
	/** 考勤部门id */
	private String attendanceDeptId;
	/** 考勤部门名称 */
	private String attendanceDeptName;
	/** 请假开始时间 */
	private String startTime;
	/** 请假结束时间 */
	private String endTime;
	/** 请假类别 */
	private String vacationType;
	/** 假别ID */
	private String vacationTypeId;
	/** 是否包括周末 */
	private String ifWeekend;
	/** 请假天数 */
	private String vacationDays;
	/** 请假时长 */
	private String vacationHours;
	/** 原因 */
	private String reason;
	/** 去向 */
	private String whither;
	/** 备注 */
	private String memo;
	/** 上次修改时间 */
	private String lastModifyDate;
	/** 重复数 */
	private long repeat;
	/**
	 * @return 重复数
	 */
	public long getRepeat() {
		return repeat;
	}
	/**
	 * @param 重复数
	 */
	public void setRepeat(long repeat) {
		this.repeat = repeat;
	}
	/**
	 * @return 审批状态
	 */
	public String getSignState() {
		return signState;
	}
	/**
	 * @param 审批状态
	 */
	public void setSignState(String signState) {
		this.signState = signState;
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
	 * @return  员工ID
	 */
	public String getEmpId() {
		return empId;
	}
	/**
	 * @param  员工ID
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
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
	 * @return 所属部门ID
	 */
	public String getDeptId() {
		return deptId;
	}
	/**
	 * @param 所属部门ID
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * @return 请假开始时间
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param 请假开始时间
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return 请假结束时间
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param 请假结束时间
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return 请假类别
	 */
	public String getVacationType() {
		return vacationType;
	}
	/**
	 * @param 请假类别
	 */
	public void setVacationType(String vacationType) {
		this.vacationType = vacationType;
	}
	/**
	 * @return 假别ID
	 */
	public String getVacationTypeId() {
		return vacationTypeId;
	}
	/**
	 * @param 假别ID
	 */
	public void setVacationTypeId(String vacationTypeId) {
		this.vacationTypeId = vacationTypeId;
	}
	/**
	 * @return 是否包括周末
	 */
	public String getIfWeekend() {
		return ifWeekend;
	}
	/**
	 * @param 是否包括周末
	 */
	public void setIfWeekend(String ifWeekend) {
		this.ifWeekend = ifWeekend;
	}
	/**
	 * @return 请假天数
	 */
	public String getVacationDays() {
		return vacationDays;
	}
	/**
	 * @param 请假天数
	 */
	public void setVacationDays(String vacationDays) {
		this.vacationDays = vacationDays;
	}
	/**
	 * @return 请假时长
	 */
	public String getVacationHours() {
		return vacationHours;
	}
	/**
	 * @param 请假时长
	 */
	public void setVacationHours(String vacationHours) {
		this.vacationHours = vacationHours;
	}
	/**
	 * @return 原因
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * @param 原因
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * @return 去向
	 */
	public String getWhither() {
		return whither;
	}
	/**
	 * @param 去向
	 */
	public void setWhither(String whither) {
		this.whither = whither;
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
	 * @return 上次修改时间
	 */
	public String getLastModifyDate() {
		return lastModifyDate;
	}
	/**
	 * @param 上次修改时间
	 */
	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	/**
	 * @return 请假id
	 */
	public String getVacationId() {
		return vacationId;
	}
	/**
	 * @param 请假id
	 */
	public void setVacationId(String vacationId) {
		this.vacationId = vacationId;
	}
	/**
	 * @return 考勤部门id
	 */
	public String getAttendanceDeptId() {
		return attendanceDeptId;
	}
	/**
	 * @param 考勤部门id
	 */
	public void setAttendanceDeptId(String attendanceDeptId) {
		this.attendanceDeptId = attendanceDeptId;
	}
	/**
	 * @return 考勤部门名称
	 */
	public String getAttendanceDeptName() {
		return attendanceDeptName;
	}
	/**
	 * @param 考勤部门名称
	 */
	public void setAttendanceDeptName(String attendanceDeptName) {
		this.attendanceDeptName = attendanceDeptName;
	}

}
