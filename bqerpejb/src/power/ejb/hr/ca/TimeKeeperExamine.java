/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.io.Serializable;

/**
 * 考勤员审核bean
 * 
 * @author zhouxu
 */
@SuppressWarnings("serial")
public class TimeKeeperExamine implements Serializable {
    /** 审核部门 */
    private String examineADept;
    /** 审核年份 */
    private String attendanceYear;
    /** 审核月份 */
    private String attendanceMonth;
    /** 审核人1 */
    private String examineMan1;
    /** 考勤登记人名 */
    private String man1ChsName;
    /** 审核日期1 */
    private String examineDate1;
    /** 审核人2 */
    private String examineMan2;
    /** 考勤审核人名 */
    private String man2ChsName;
    /** 审核日期2 */
    private String examineDate2;
    /** 考勤类别 */
    private String examineType;
    /** 上级审核部门 */
    private String examineDeptTop;
    /** 考勤部门名称 */
    private String examineBDeptName;
    /** 考勤部门ID */
    private String examineBDeptId;
    /** 考勤登记人 */
    private String examineRegisterMan;
    /** 考勤审核人 */
    private String examineMan;
    /** 上次修改时间 */
    private String lastModifiyDate;
    /** 可撤销审核FLG */
    private String cancelFlag;
    /** 部门审核层次 */
    private int level;

    /**
     * @return the examineADept
     */
    public String getExamineADept() {
        return examineADept;
    }

    /**
     * @param examineADept
     *            the examineADept to set
     */
    public void setExamineADept(String examineADept) {
        this.examineADept = examineADept;
    }

    /**
     * @return the attendanceYear
     */
    public String getAttendanceYear() {
        return attendanceYear;
    }

    /**
     * @param attendanceYear
     *            the attendanceYear to set
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
     * @param attendanceMonth
     *            the attendanceMonth to set
     */
    public void setAttendanceMonth(String attendanceMonth) {
        this.attendanceMonth = attendanceMonth;
    }

    /**
     * @return the examineMan1
     */
    public String getExamineMan1() {
        return examineMan1;
    }

    /**
     * @param examineMan1
     *            the examineMan1 to set
     */
    public void setExamineMan1(String examineMan1) {
        this.examineMan1 = examineMan1;
    }

    /**
     * @return the examineDate1
     */
    public String getExamineDate1() {
        return examineDate1;
    }

    /**
     * @param examineDate1
     *            the examineDate1 to set
     */
    public void setExamineDate1(String examineDate1) {
        this.examineDate1 = examineDate1;
    }

    /**
     * @return the examineMan2
     */
    public String getExamineMan2() {
        return examineMan2;
    }

    /**
     * @param examineMan2
     *            the examineMan2 to set
     */
    public void setExamineMan2(String examineMan2) {
        this.examineMan2 = examineMan2;
    }

    /**
     * @return the examineDate2
     */
    public String getExamineDate2() {
        return examineDate2;
    }

    /**
     * @param examineDate2
     *            the examineDate2 to set
     */
    public void setExamineDate2(String examineDate2) {
        this.examineDate2 = examineDate2;
    }

    /**
     * @return the examineType
     */
    public String getExamineType() {
        return examineType;
    }

    /**
     * @param examineType
     *            the examineType to set
     */
    public void setExamineType(String examineType) {
        this.examineType = examineType;
    }

    /**
     * @return the examineDeptTop
     */
    public String getExamineDeptTop() {
        return examineDeptTop;
    }

    /**
     * @param examineDeptTop
     *            the examineDeptTop to set
     */
    public void setExamineDeptTop(String examineDeptTop) {
        this.examineDeptTop = examineDeptTop;
    }

    /**
     * @return the examineBDeptName
     */
    public String getExamineBDeptName() {
        return examineBDeptName;
    }

    /**
     * @param examineBDeptName
     *            the examineBDeptName to set
     */
    public void setExamineBDeptName(String examineBDeptName) {
        this.examineBDeptName = examineBDeptName;
    }

    /**
     * @return the examineBDeptId
     */
    public String getExamineBDeptId() {
        return examineBDeptId;
    }

    /**
     * @param examineBDeptId
     *            the examineBDeptId to set
     */
    public void setExamineBDeptId(String examineBDeptId) {
        this.examineBDeptId = examineBDeptId;
    }

    /**
     * @return the examineRegisterMan
     */
    public String getExamineRegisterMan() {
        return examineRegisterMan;
    }

    /**
     * @param examineRegisterMan
     *            the examineRegisterMan to set
     */
    public void setExamineRegisterMan(String examineRegisterMan) {
        this.examineRegisterMan = examineRegisterMan;
    }

    /**
     * @return the man1ChsName
     */
    public String getMan1ChsName() {
        return man1ChsName;
    }

    /**
     * @param man1ChsName
     *            the man1ChsName to set
     */
    public void setMan1ChsName(String man1ChsName) {
        this.man1ChsName = man1ChsName;
    }

    /**
     * @return the examineMan
     */
    public String getExamineMan() {
        return examineMan;
    }

    /**
     * @param examineMan
     *            the examineMan to set
     */
    public void setExamineMan(String examineMan) {
        this.examineMan = examineMan;
    }

    /**
     * @return the man2ChsName
     */
    public String getMan2ChsName() {
        return man2ChsName;
    }

    /**
     * @param man2ChsName
     *            the man2ChsName to set
     */
    public void setMan2ChsName(String man2ChsName) {
        this.man2ChsName = man2ChsName;
    }

    /**
     * @return the cancelFlag
     */
    public String getCancelFlag() {
        return cancelFlag;
    }

    /**
     * @param cancelFlag
     *            the cancelFlag to set
     */
    public void setCancelFlag(String cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    /**
     * @return the lastModifiyDate
     */
    public String getLastModifiyDate() {
        return lastModifiyDate;
    }

    /**
     * @param lastModifiyDate
     *            the lastModifiyDate to set
     */
    public void setLastModifiyDate(String lastModifiyDate) {
        this.lastModifiyDate = lastModifiyDate;
    }

    /**
     * 获取
     * 
     * @return level
     */
    public int getLevel() {
        return level;
    }

    /**
     * 设置
     * 
     * @param level
     */
    public void setLevel(int level) {
        this.level = level;
    }

}
