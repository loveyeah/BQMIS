/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

/**
 * 考勤员审核内容bean
 * 
 * @author zhouxu
 */

public class TimeKeeperExamineContentNew implements java.io.Serializable{
    /** 部门id */
    private String deptId;
    /** 部门名称 */
    private String deptName;
    /** 考勤日期 */
    private String atendanceDate;
    /** 人员id */
    private String empId;
    /** 人员姓名 */
    private String empName;
    /** 考勤部门 id */
    private String attendanceDeptId;
    /** 考勤部门名称*/
    private String attendanceDeptName;
    /** 出勤 */
    private String work;
    /** 运行班id */
    private String workShiftId;
    /** 加班类别id */
    private String overtimeTypeId;
    /** 加班时间id */
    private String overtimeTimeId;
    /** 旷工 */
    private String absentTimeId;
    /** 假别id */
    private String vacationTypeId;
    /**请假时间Id*/
    private String otherTimeId;
    /** 换休 */
    private String changeRest;
    /** 年休 */
    private String yearRest;
    /** 出差 */
    private String evectionType;
    /** 外借 */
    private String outWork;
    
    /** 标记 */
    private String mark;
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
	public String getAtendanceDate() {
		return atendanceDate;
	}
	public void setAtendanceDate(String atendanceDate) {
		this.atendanceDate = atendanceDate;
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
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getWorkShiftId() {
		return workShiftId;
	}
	public void setWorkShiftId(String workShiftId) {
		this.workShiftId = workShiftId;
	}
	public String getOvertimeTypeId() {
		return overtimeTypeId;
	}
	public void setOvertimeTypeId(String overtimeTypeId) {
		this.overtimeTypeId = overtimeTypeId;
	}
	public String getOvertimeTimeId() {
		return overtimeTimeId;
	}
	public void setOvertimeTimeId(String overtimeTimeId) {
		this.overtimeTimeId = overtimeTimeId;
	}
	public String getAbsentTimeId() {
		return absentTimeId;
	}
	public void setAbsentTimeId(String absentTimeId) {
		this.absentTimeId = absentTimeId;
	}
	public String getVacationTypeId() {
		return vacationTypeId;
	}
	public void setVacationTypeId(String vacationTypeId) {
		this.vacationTypeId = vacationTypeId;
	}
	public String getOtherTimeId() {
		return otherTimeId;
	}
	public void setOtherTimeId(String otherTimeId) {
		this.otherTimeId = otherTimeId;
	}
	public String getChangeRest() {
		return changeRest;
	}
	public void setChangeRest(String changeRest) {
		this.changeRest = changeRest;
	}
	public String getYearRest() {
		return yearRest;
	}
	public void setYearRest(String yearRest) {
		this.yearRest = yearRest;
	}
	public String getEvectionType() {
		return evectionType;
	}
	public void setEvectionType(String evectionType) {
		this.evectionType = evectionType;
	}
	public String getOutWork() {
		return outWork;
	}
	public void setOutWork(String outWork) {
		this.outWork = outWork;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
    
}
