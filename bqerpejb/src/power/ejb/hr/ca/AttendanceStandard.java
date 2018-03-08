/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;



/**
 * 考勤标准维护bean
 * 
 * @author chen shoujiang
 */
public class AttendanceStandard implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	// 考勤标准ID
	private String attendancestandardid;
	// 考勤年份
	private String attendanceYear;
	// 考勤月份
	private String attendanceMonth;
	// 考勤部门ID
	private String attendanceDeptId;
	// 考勤部门名称
	private String attendanceDeptName;
	// 开始日期
	private String startDate;
	// 结束日期
	private String endDate;
	// 标准天数
	private String standardDay;
	// 上午上班时间
	private String amBegingTime;
	// 上午下班时间
	private String amEndTime;
	// 下午上班时间
	private String pmBegingTime;
	// 下午下班时间
	private String pmEndTime;
	// 标准出勤时间
	private String standardTime;
	// 上次修改日期
	private String lastModifiyDate;
	/**
	 * @return the attendancestandardid
	 */
	public String getAttendancestandardid() {
		return attendancestandardid;
	}
	/**
	 * @param attendancestandardid the attendancestandardid to set
	 */
	public void setAttendancestandardid(String attendancestandardid) {
		this.attendancestandardid = attendancestandardid;
	}
	/**
	 * @return the attendanceYear
	 */
	public String getAttendanceYear() {
		return attendanceYear;
	}
	/**
	 * @param attendanceYear the attendanceYear to set
	 */
	public void setAttendanceYear(String attendanceYear) {
		this.attendanceYear = attendanceYear;
	}
	/**
	 * @return the attendanceMonth
	 */
	public String getAttendanceMonth() {
		return attendanceMonth;
	}
	/**
	 * @param attendanceMonth the attendanceMonth to set
	 */
	public void setAttendanceMonth(String attendanceMonth) {
		this.attendanceMonth = attendanceMonth;
	}
	/**
	 * @return the attendanceDeptId
	 */
	public String getAttendanceDeptId() {
		return attendanceDeptId;
	}
	/**
	 * @param attendanceDeptId the attendanceDeptId to set
	 */
	public void setAttendanceDeptId(String attendanceDeptId) {
		this.attendanceDeptId = attendanceDeptId;
	}
	/**
	 * @return the attendanceDeptName
	 */
	public String getAttendanceDeptName() {
		return attendanceDeptName;
	}
	/**
	 * @param attendanceDeptName the attendanceDeptName to set
	 */
	public void setAttendanceDeptName(String attendanceDeptName) {
		this.attendanceDeptName = attendanceDeptName;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the standardDay
	 */
	public String getStandardDay() {
		return standardDay;
	}
	/**
	 * @param standardDay the standardDay to set
	 */
	public void setStandardDay(String standardDay) {
		this.standardDay = standardDay;
	}
	/**
	 * @return the amBegingTime
	 */
	public String getAmBegingTime() {
		return amBegingTime;
	}
	/**
	 * @param amBegingTime the amBegingTime to set
	 */
	public void setAmBegingTime(String amBegingTime) {
		this.amBegingTime = amBegingTime;
	}
	/**
	 * @return the amEndTime
	 */
	public String getAmEndTime() {
		return amEndTime;
	}
	/**
	 * @param amEndTime the amEndTime to set
	 */
	public void setAmEndTime(String amEndTime) {
		this.amEndTime = amEndTime;
	}
	/**
	 * @return the pmBegingTime
	 */
	public String getPmBegingTime() {
		return pmBegingTime;
	}
	/**
	 * @param pmBegingTime the pmBegingTime to set
	 */
	public void setPmBegingTime(String pmBegingTime) {
		this.pmBegingTime = pmBegingTime;
	}
	/**
	 * @return the pmEndTime
	 */
	public String getPmEndTime() {
		return pmEndTime;
	}
	/**
	 * @param pmEndTime the pmEndTime to set
	 */
	public void setPmEndTime(String pmEndTime) {
		this.pmEndTime = pmEndTime;
	}
	/**
	 * @return the standardTime
	 */
	public String getStandardTime() {
		return standardTime;
	}
	/**
	 * @param standardTime the standardTime to set
	 */
	public void setStandardTime(String standardTime) {
		this.standardTime = standardTime;
	}
	/**
	 * @return the lastModifiyDate
	 */
	public String getLastModifiyDate() {
		return lastModifiyDate;
	}
	/**
	 * @param lastModifiyDate the lastModifiyDate to set
	 */
	public void setLastModifiyDate(String lastModifiyDate) {
		this.lastModifiyDate = lastModifiyDate;
	}

}
