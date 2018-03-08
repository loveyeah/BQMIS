package power.ejb.hr.ca.cacommon;

public class EmpAttendanceForm
{
	// 人员id
	private String empId;
	// 人员名
	private String empName;
	// 部门id
	private String deptId;
	// 部门名
	private String deptName;
	// 考勤部门Id
	private String attendanceDeptId;
	// 考勤部门名
	private String attendanceDeptName;
	// 月份
	private String month;
	// 病假sum
	private String sickLeavlCount;
	// 旷工sum
	private String absentCount;
	// 事假sum
	private String eventCount;
	// 晚上加班sum
	private String eveningAddCount;
	// 双休日加班 sum 
	private String weekendAddCount;
	// 节假日加班 sum 
	private String holidayAddCount;
	// 其他请假 sum
	private String otherTimeCount;
	
	public String getOtherTimeCount() {
		return otherTimeCount;
	}

	public void setOtherTimeCount(String otherTimeCount) {
		this.otherTimeCount = otherTimeCount;
	}

	public EmpAttendanceForm()
	{
		this.empId = null;
		this.empName = null;
		this.deptId = null;
		this.deptName = null;
		this.attendanceDeptId = null;
		this.attendanceDeptName = null;
		this.month = null;
		this.sickLeavlCount = null;
		this.absentCount = null;
		this.eventCount = null;
		this.eveningAddCount = null;
		this.weekendAddCount = null;
		this.holidayAddCount = null;
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


	public String getAttendanceDeptId() {
		return attendanceDeptId;
	}


	public void setAttendanceDeptId(String attendanceDeptId) {
		this.attendanceDeptId = attendanceDeptId;
	}


	public String getAttendanceDeptName() {
		return attendanceDeptName;
	}


	public void setAttendanceDeptName(String attendanceDeptName) {
		this.attendanceDeptName = attendanceDeptName;
	}


	public String getMonth() {
		return month;
	}


	public void setMonth(String month) {
		this.month = month;
	}


	public String getSickLeavlCount() {
		return sickLeavlCount;
	}


	public void setSickLeavlCount(String sickLeavlCount) {
		this.sickLeavlCount = sickLeavlCount;
	}


	public String getAbsentCount() {
		return absentCount;
	}


	public void setAbsentCount(String absentCount) {
		this.absentCount = absentCount;
	}


	public String getEventCount() {
		return eventCount;
	}


	public void setEventCount(String eventCount) {
		this.eventCount = eventCount;
	}


	public String getEveningAddCount() {
		return eveningAddCount;
	}


	public void setEveningAddCount(String eveningAddCount) {
		this.eveningAddCount = eveningAddCount;
	}


	public String getWeekendAddCount() {
		return weekendAddCount;
	}


	public void setWeekendAddCount(String weekendAddCount) {
		this.weekendAddCount = weekendAddCount;
	}


	public String getHolidayAddCount() {
		return holidayAddCount;
	}


	public void setHolidayAddCount(String holidayAddCount) {
		this.holidayAddCount = holidayAddCount;
	}

}