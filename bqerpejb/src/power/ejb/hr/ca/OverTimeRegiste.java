package power.ejb.hr.ca;

public class OverTimeRegiste implements java.io.Serializable{
	private String empId;
	private String empName;
	/**所属部门*/
	private String department;
	/**部门ID*/
	private String deptId;
	/**月累计加班时间*/
	private String sumOverTime;
	private String actualTime;
	/**审批状态*/
	private String 	approvalStates;
	/**标准出勤时间*/
	private String standardTime;
	/**总加班天数*/
	private String sumDays;
	/**换休时间*/
	private String exchangHours;
	/**上次修改时间*/
	private String lastModifyTime;
	/**出勤年份*/
	private String attendanceYear;
	/**出勤月份*/
	private String attendanceMonth;
	public String getLastModifyTime() {
		return lastModifyTime;
	}
	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}
	public String getExchangHours() {
		return exchangHours;
	}
	public void setExchangHours(String exchangHours) {
		this.exchangHours = exchangHours;
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
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getSumOverTime() {
		return sumOverTime;
	}
	public void setSumOverTime(String sumOverTime) {
		this.sumOverTime = sumOverTime;
	}
	public String getActualTime() {
		return actualTime;
	}
	public void setActualTime(String actualTime) {
		this.actualTime = actualTime;
	}
	public String getApprovalStates() {
		return approvalStates;
	}
	public void setApprovalStates(String approvalStates) {
		this.approvalStates = approvalStates;
	}
	public String getStandardTime() {
		return standardTime;
	}
	public void setStandardTime(String standardTime) {
		this.standardTime = standardTime;
	}
	public String getSumDays() {
		return sumDays;
	}
	public void setSumDays(String sumDays) {
		this.sumDays = sumDays;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getAttendanceYear() {
		return attendanceYear;
	}
	public void setAttendanceYear(String attendanceYear) {
		this.attendanceYear = attendanceYear;
	}
	public String getAttendanceMonth() {
		return attendanceMonth;
	}
	public void setAttendanceMonth(String attendanceMonth) {
		this.attendanceMonth = attendanceMonth;
	}

}
