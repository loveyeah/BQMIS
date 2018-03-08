/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

/**
 * 员工销假登记Bean
 * 
 * @author liuxin
 * 
 */
public class employeeLeaveBean implements java.io.Serializable{
	/**员工ID*/
	private String empId;
	/**员工姓名*/
	private String empName;
	/**部门ID*/
	private String deptId;
	/**部门名*/
	private String deptName;
	/**请假类别ID*/
	private String leaveTypeId;
	/**请假类别*/
	private String leaveType;
	/**开始时间*/
	private String startTime;
	/**结束时间*/
	private String endTime;
	/**请假天数*/
	private String leaveDays;
	/**请假时长*/
	private String leaveTime;
	/**原因*/
	private String reason;
	/**去向*/
	private String goWhere;
	/**备注*/
	private String memo;
	/**请假Id*/
	private String vacationId;
	/**上次修改时间*/
	private String lastModifyTime;
	/**是否包含周末*/
	private String ifWeekend;
	public String getIfWeekend() {
		return ifWeekend;
	}
	public void setIfWeekend(String ifWeekend) {
		this.ifWeekend = ifWeekend;
	}
	public String getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public String getVacationId() {
		return vacationId;
	}
	public void setVacationId(String vacationId) {
		this.vacationId = vacationId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getLeaveTypeId() {
		return leaveTypeId;
	}
	public void setLeaveTypeId(String leaveTypeId) {
		this.leaveTypeId = leaveTypeId;
	}
	public String getLeaveType() {
		return leaveType;
	}
	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
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
	public String getLeaveDays() {
		return leaveDays;
	}
	public void setLeaveDays(String leaveDays) {
		this.leaveDays = leaveDays;
	}
	public String getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(String leaveTime) {
		this.leaveTime = leaveTime;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getGoWhere() {
		return goWhere;
	}
	public void setGoWhere(String goWhere) {
		this.goWhere = goWhere;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	

}
