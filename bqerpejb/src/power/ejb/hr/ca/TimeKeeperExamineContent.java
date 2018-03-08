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

public class TimeKeeperExamineContent implements java.io.Serializable{
    /** 部门id */
    private String deptId;
    /** 部门名称 */
    private String deptName;
    /** 考勤部门 id */
    private String attendanceDeptId;
    /** 人员id */
    private String empId;
    /** 人员姓名 */
    private String empName;
    /** 考勤日期 */
    private String atendanceDate;
    /** 标记 */
    private String mark;
    /** 休息 */
    private String rest;
    /** 出勤 */
    private String work;
    /** 旷工 */
    private String absent;
    /** 出差 */
    private String evection;
    /** 外借 */
    private String out;
    /** 迟到 */
    private String late;
    /** 早退 */
    private String leave;
    /** 加班类别id */
    private String overtimeTypeId;
    /** 假别id */
    private String vacationTypeId;
    /** 运行班id */
    private String workShiftId;
    /** 运行班津贴 */
    private Double workShitFee;
    /** 最后一次修改时间 */
    private String lastModifiyDate;

    /**
     * @return the deptId
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * @param deptId
     *            the deptId to set
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
     * @param deptName
     *            the deptName to set
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * @return the attendanceDeptId
     */
    public String getAttendanceDeptId() {
        return attendanceDeptId;
    }

    /**
     * @param attendanceDeptId
     *            the attendanceDeptId to set
     */
    public void setAttendanceDeptId(String attendanceDeptId) {
        this.attendanceDeptId = attendanceDeptId;
    }

    /**
     * @return the empId
     */
    public String getEmpId() {
        return empId;
    }

    /**
     * @param empId
     *            the empId to set
     */
    public void setEmpId(String empId) {
        this.empId = empId;
    }

    /**
     * @return the empName
     */
    public String getEmpName() {
        return empName;
    }

    /**
     * @param empName
     *            the empName to set
     */
    public void setEmpName(String empName) {
        this.empName = empName;
    }

    /**
     * @return the atendanceDate
     */
    public String getAtendanceDate() {
        return atendanceDate;
    }

    /**
     * @param atendanceDate
     *            the atendanceDate to set
     */
    public void setAtendanceDate(String atendanceDate) {
        this.atendanceDate = atendanceDate;
    }

    /**
     * @return the mark
     */
    public String getMark() {
        return mark;
    }

    /**
     * @param mark
     *            the mark to set
     */
    public void setMark(String mark) {
        this.mark = mark;
    }

    /**
     * @return the rest
     */
    public String getRest() {
        return rest;
    }

    /**
     * @param rest
     *            the rest to set
     */
    public void setRest(String rest) {
        this.rest = rest;
    }

    /**
     * @return the work
     */
    public String getWork() {
        return work;
    }

    /**
     * @param work
     *            the work to set
     */
    public void setWork(String work) {
        this.work = work;
    }

    /**
     * @return the absent
     */
    public String getAbsent() {
        return absent;
    }

    /**
     * @param absent
     *            the absent to set
     */
    public void setAbsent(String absent) {
        this.absent = absent;
    }

    /**
     * @return the evection
     */
    public String getEvection() {
        return evection;
    }

    /**
     * @param evection
     *            the evection to set
     */
    public void setEvection(String evection) {
        this.evection = evection;
    }

    /**
     * @return the out
     */
    public String getOut() {
        return out;
    }

    /**
     * @param out
     *            the out to set
     */
    public void setOut(String out) {
        this.out = out;
    }

    /**
     * @return the late
     */
    public String getLate() {
        return late;
    }

    /**
     * @param late
     *            the late to set
     */
    public void setLate(String late) {
        this.late = late;
    }

    /**
     * @return the leave
     */
    public String getLeave() {
        return leave;
    }

    /**
     * @param leave
     *            the leave to set
     */
    public void setLeave(String leave) {
        this.leave = leave;
    }

    /**
     * 获取
     * 
     * @return overtimeTypeId
     */
    public String getOvertimeTypeId() {
        return overtimeTypeId;
    }

    /**
     * 设置
     * 
     * @param overtimeTypeId
     */
    public void setOvertimeTypeId(String overtimeTypeId) {
        this.overtimeTypeId = overtimeTypeId;
    }

    /**
     * 获取
     * 
     * @return vacationTypeId
     */
    public String getVacationTypeId() {
        return vacationTypeId;
    }

    /**
     * 设置
     * 
     * @param vacationTypeId
     */
    public void setVacationTypeId(String vacationTypeId) {
        this.vacationTypeId = vacationTypeId;
    }

    /**
     * 获取
     * 
     * @return workShiftId
     */
    public String getWorkShiftId() {
        return workShiftId;
    }

    /**
     * 设置
     * 
     * @param workShiftId
     */
    public void setWorkShiftId(String workShiftId) {
        this.workShiftId = workShiftId;
    }

    /**
     * @return the workShitFee
     */
    public Double getWorkShitFee() {
        return workShitFee;
    }

    /**
     * @param workShitFee
     *            the workShitFee to set
     */
    public void setWorkShitFee(Double workShitFee) {
        this.workShitFee = workShitFee;
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
