/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

/**
 * 部门考勤登记Bean
 * 
 * @author jincong
 * @version 1.0
 */
public class DeptAttendance implements java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	// 人员基本信息表
	/** 人员ID */
	private String empId;
	/** 中文名 */
	private String chsName;
	
	// 部门设置表
	/** 部门ID */
	private String deptId;
	/** 部门名称 */
	private String deptName;
	
	// 考勤登记表
	/** 上午上班时间 */
	private String amBeginTime;
	/** 上午下班时间 */
	private String amEndTime;
	/** 下午上班时间 */
	private String pmBeginTime;
	/** 下午下班时间 */
	private String pmEndTime;
	/** 运行班类别ID */
	private String workShiftId;
	/** 假别ID */
	private String vacationTypeId;
	/** 加班类别ID */
	private String overTimeId;
	/** 休息 */
	private String restType;
	/** 旷工 */
	private String absentWork;
	/** 出差 */
	private String evectionType;
	/** 外借 */
	private String outWork;
	/** 备注 */
	private String memo;
	/** 上次修改日期 */
	private String lastModifiyDate;
	
	// 考勤审核表
	/** 考勤部门 */
	private String attendanceDept;
	/** 审核人1 */
	private String deptCharge1;
	/** 审核日期1 */
	private String checkedDate1;
	/** 审核人2 */
	private String deptCharge2;
	/** 审核日期2 */
	private String checkedDate2;
	
	// 考勤部门维护
	/** 考勤类别 */
	private String attendDeptType;
	/** 上级审核部门 */
	private String topCheckDeptId;
	/** 考勤部门名称 */
	private String attendanceDeptName;
	/** 考勤部门ID */
	private String attendanceDeptId;
	/** 考勤登记人 */
	private String attendWriterId;
	/** 考勤审核人 */
	private String attendCheckerId;
	
	// 考勤标准维护表
	/** 上午上班时间 */
	private String standardAmBeginTime;
	/** 上午下班时间 */
	private String standardAmEndTime;
	/** 下午上班时间 */
	private String standardPmBeginTime;
	/** 下午下班时间 */
	private String standardPmEndTime;
	
	// 请假登记表 
	/** 开始时间 */
	private String startTime;
	/** 结束时间 */
	private String endTime;
	
	/** 出勤标识 */
	private String attendanceFlag;
	/** 早退标识 */
	private String leaveEarlyFlag;
	/** 迟到标识 */
	private String lateWorkFlag;
	/** 存在标识 */
	private String existFlag;
	/** grid能否编辑标识 */
	private boolean gridEditAble;
	
	// add by liuyi 20100202
	// 加班时间id overtime_time_id
	private String overtimeTimeId;
	//病假时间Id sick_leave_time_id
	private String sickLeaveTimeId;
	//事假时间Id event_time_id
	private String eventTimeId;
	// 旷工时间Id absent_time_id
	private String absentTimeId;
	// 其他假时间Id other_time_id
	private String otherTimeId;
	
	public String getOtherTimeId() {
		return otherTimeId;
	}
	public void setOtherTimeId(String otherTimeId) {
		this.otherTimeId = otherTimeId;
	}
	public String getOvertimeTimeId() {
		return overtimeTimeId;
	}
	public void setOvertimeTimeId(String overtimeTimeId) {
		this.overtimeTimeId = overtimeTimeId;
	}
	public String getSickLeaveTimeId() {
		return sickLeaveTimeId;
	}
	public void setSickLeaveTimeId(String sickLeaveTimeId) {
		this.sickLeaveTimeId = sickLeaveTimeId;
	}
	public String getEventTimeId() {
		return eventTimeId;
	}
	public void setEventTimeId(String eventTimeId) {
		this.eventTimeId = eventTimeId;
	}
	public String getAbsentTimeId() {
		return absentTimeId;
	}
	public void setAbsentTimeId(String absentTimeId) {
		this.absentTimeId = absentTimeId;
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
	 * @return the amBeginTime
	 */
	public String getAmBeginTime() {
		return amBeginTime;
	}
	/**
	 * @param amBeginTime the amBeginTime to set
	 */
	public void setAmBeginTime(String amBeginTime) {
		this.amBeginTime = amBeginTime;
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
	 * @return the pmBeginTime
	 */
	public String getPmBeginTime() {
		return pmBeginTime;
	}
	/**
	 * @param pmBeginTime the pmBeginTime to set
	 */
	public void setPmBeginTime(String pmBeginTime) {
		this.pmBeginTime = pmBeginTime;
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
	 * @return the workShiftId
	 */
	public String getWorkShiftId() {
		return workShiftId;
	}
	/**
	 * @param workShiftId the workShiftId to set
	 */
	public void setWorkShiftId(String workShiftId) {
		this.workShiftId = workShiftId;
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
	 * @return the overTimeId
	 */
	public String getOverTimeId() {
		return overTimeId;
	}
	/**
	 * @param overTimeId the overTimeId to set
	 */
	public void setOverTimeId(String overTimeId) {
		this.overTimeId = overTimeId;
	}
	/**
	 * @return the restType
	 */
	public String getRestType() {
		return restType;
	}
	/**
	 * @param restType the restType to set
	 */
	public void setRestType(String restType) {
		this.restType = restType;
	}
	/**
	 * @return the absentWork
	 */
	public String getAbsentWork() {
		return absentWork;
	}
	/**
	 * @param absentWork the absentWork to set
	 */
	public void setAbsentWork(String absentWork) {
		this.absentWork = absentWork;
	}
	/**
	 * @return the evectionType
	 */
	public String getEvectionType() {
		return evectionType;
	}
	/**
	 * @param evectionType the evectionType to set
	 */
	public void setEvectionType(String evectionType) {
		this.evectionType = evectionType;
	}
	/**
	 * @return the outWork
	 */
	public String getOutWork() {
		return outWork;
	}
	/**
	 * @param outWork the outWork to set
	 */
	public void setOutWork(String outWork) {
		this.outWork = outWork;
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
	/**
	 * @return the attendanceDept
	 */
	public String getAttendanceDept() {
		return attendanceDept;
	}
	/**
	 * @param attendanceDept the attendanceDept to set
	 */
	public void setAttendanceDept(String attendanceDept) {
		this.attendanceDept = attendanceDept;
	}
	/**
	 * @return the deptCharge1
	 */
	public String getDeptCharge1() {
		return deptCharge1;
	}
	/**
	 * @param deptCharge1 the deptCharge1 to set
	 */
	public void setDeptCharge1(String deptCharge1) {
		this.deptCharge1 = deptCharge1;
	}
	/**
	 * @return the checkedDate1
	 */
	public String getCheckedDate1() {
		return checkedDate1;
	}
	/**
	 * @param checkedDate1 the checkedDate1 to set
	 */
	public void setCheckedDate1(String checkedDate1) {
		this.checkedDate1 = checkedDate1;
	}
	/**
	 * @return the deptCharge2
	 */
	public String getDeptCharge2() {
		return deptCharge2;
	}
	/**
	 * @param deptCharge2 the deptCharge2 to set
	 */
	public void setDeptCharge2(String deptCharge2) {
		this.deptCharge2 = deptCharge2;
	}
	/**
	 * @return the checkedDate2
	 */
	public String getCheckedDate2() {
		return checkedDate2;
	}
	/**
	 * @param checkedDate2 the checkedDate2 to set
	 */
	public void setCheckedDate2(String checkedDate2) {
		this.checkedDate2 = checkedDate2;
	}
	/**
	 * @return the attendDeptType
	 */
	public String getAttendDeptType() {
		return attendDeptType;
	}
	/**
	 * @param attendDeptType the attendDeptType to set
	 */
	public void setAttendDeptType(String attendDeptType) {
		this.attendDeptType = attendDeptType;
	}
	/**
	 * @return the topCheckDeptId
	 */
	public String getTopCheckDeptId() {
		return topCheckDeptId;
	}
	/**
	 * @param topCheckDeptId the topCheckDeptId to set
	 */
	public void setTopCheckDeptId(String topCheckDeptId) {
		this.topCheckDeptId = topCheckDeptId;
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
	 * @return the attendWriterId
	 */
	public String getAttendWriterId() {
		return attendWriterId;
	}
	/**
	 * @param attendWriterId the attendWriterId to set
	 */
	public void setAttendWriterId(String attendWriterId) {
		this.attendWriterId = attendWriterId;
	}
	/**
	 * @return the attendCheckerId
	 */
	public String getAttendCheckerId() {
		return attendCheckerId;
	}
	/**
	 * @param attendCheckerId the attendCheckerId to set
	 */
	public void setAttendCheckerId(String attendCheckerId) {
		this.attendCheckerId = attendCheckerId;
	}
	/**
	 * @return the existFlag
	 */
	public String getExistFlag() {
		return existFlag;
	}
	/**
	 * @param existFlag the existFlag to set
	 */
	public void setExistFlag(String existFlag) {
		this.existFlag = existFlag;
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
	 * @return the gridEditAble
	 */
	public boolean isGridEditAble() {
		return gridEditAble;
	}
	/**
	 * @param gridEditAble the gridEditAble to set
	 */
	public void setGridEditAble(boolean gridEditAble) {
		this.gridEditAble = gridEditAble;
	}
	/**
	 * @return the standardAmBeginTime
	 */
	public String getStandardAmBeginTime() {
		return standardAmBeginTime;
	}
	/**
	 * @param standardAmBeginTime the standardAmBeginTime to set
	 */
	public void setStandardAmBeginTime(String standardAmBeginTime) {
		this.standardAmBeginTime = standardAmBeginTime;
	}
	/**
	 * @return the standardAmEndTime
	 */
	public String getStandardAmEndTime() {
		return standardAmEndTime;
	}
	/**
	 * @param standardAmEndTime the standardAmEndTime to set
	 */
	public void setStandardAmEndTime(String standardAmEndTime) {
		this.standardAmEndTime = standardAmEndTime;
	}
	/**
	 * @return the standardPmBeginTime
	 */
	public String getStandardPmBeginTime() {
		return standardPmBeginTime;
	}
	/**
	 * @param standardPmBeginTime the standardPmBeginTime to set
	 */
	public void setStandardPmBeginTime(String standardPmBeginTime) {
		this.standardPmBeginTime = standardPmBeginTime;
	}
	/**
	 * @return the standardPmEndTime
	 */
	public String getStandardPmEndTime() {
		return standardPmEndTime;
	}
	/**
	 * @param standardPmEndTime the standardPmEndTime to set
	 */
	public void setStandardPmEndTime(String standardPmEndTime) {
		this.standardPmEndTime = standardPmEndTime;
	}
	/**
	 * @return the attendanceFlag
	 */
	public String getAttendanceFlag() {
		return attendanceFlag;
	}
	/**
	 * @param attendanceFlag the attendanceFlag to set
	 */
	public void setAttendanceFlag(String attendanceFlag) {
		this.attendanceFlag = attendanceFlag;
	}
	/**
	 * @return the leaveEarlyFlag
	 */
	public String getLeaveEarlyFlag() {
		return leaveEarlyFlag;
	}
	/**
	 * @param leaveEarlyFlag the leaveEarlyFlag to set
	 */
	public void setLeaveEarlyFlag(String leaveEarlyFlag) {
		this.leaveEarlyFlag = leaveEarlyFlag;
	}
	/**
	 * @return the lateWorkFlag
	 */
	public String getLateWorkFlag() {
		return lateWorkFlag;
	}
	/**
	 * @param lateWorkFlag the lateWorkFlag to set
	 */
	public void setLateWorkFlag(String lateWorkFlag) {
		this.lateWorkFlag = lateWorkFlag;
	}
}
