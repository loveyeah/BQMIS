/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;

/**
 * 考勤员审核全画面内容bean
 * 
 * @author zhouxu
 */

public class TimeKeeperExamineForm implements java.io.Serializable{

    /** 定义store */
    private StoreObject store;
    /** 定义开始日期 */
    private String strStartDate;
    /** 定义结束日期 */
    private String strEndDate;
    /** 定义审核人 */
    private String strExamine;
    /** 定义考勤人 */
    private String strAttendance;
    /** 是否审核 */
    private String checkFlag;
    /** 考勤部门名称 */
    private String strExamineDeptName;
    /** 考勤部门id */
    private String strExamineDeptId;
    /** 父部门及子部门考勤人审核人store */
    private PageObject deptAllStore;
    /** 节假日信息 */
    private List<Map<String, Object>> workOrRestList;
    /** 节假日颜色 */
    private String strColor;
    /** 登陆用户ID */
    private String loginEmpId;
    /** 登陆用户姓名 */
    private String loginEmpName;
    /** 下属考勤部门审核与否flg （add by wjHuang） */
    private String sonCheckFlag;
    /** 未审核子部门 */
    private String unCheckDeptName;

    /**
     * @return the store
     */
    public StoreObject getStore() {
        return store;
    }

    /**
     * @param store
     *            the store to set
     */
    public void setStore(StoreObject store) {
        this.store = store;
    }

    /**
     * @return the strStartDate
     */
    public String getStrStartDate() {
        return strStartDate;
    }

    /**
     * @param strStartDate
     *            the strStartDate to set
     */
    public void setStrStartDate(String strStartDate) {
        this.strStartDate = strStartDate;
    }

    /**
     * @return the strEndDate
     */
    public String getStrEndDate() {
        return strEndDate;
    }

    /**
     * @param strEndDate
     *            the strEndDate to set
     */
    public void setStrEndDate(String strEndDate) {
        this.strEndDate = strEndDate;
    }

    /**
     * @return the strExamine
     */
    public String getStrExamine() {
        return strExamine;
    }

    /**
     * @param strExamine
     *            the strExamine to set
     */
    public void setStrExamine(String strExamine) {
        this.strExamine = strExamine;
    }

    /**
     * @return the strAttendance
     */
    public String getStrAttendance() {
        return strAttendance;
    }

    /**
     * @param strAttendance
     *            the strAttendance to set
     */
    public void setStrAttendance(String strAttendance) {
        this.strAttendance = strAttendance;
    }

    /**
     * @return the checkFlag
     */
    public String getCheckFlag() {
        return checkFlag;
    }

    /**
     * @param checkFlag
     *            the checkFlag to set
     */
    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }

    /**
     * @return the strExamineDeptName
     */
    public String getStrExamineDeptName() {
        return strExamineDeptName;
    }

    /**
     * @param strExamineDeptName
     *            the strExamineDeptName to set
     */
    public void setStrExamineDeptName(String strExamineDeptName) {
        this.strExamineDeptName = strExamineDeptName;
    }

    /**
     * @return the strExamineDeptId
     */
    public String getStrExamineDeptId() {
        return strExamineDeptId;
    }

    /**
     * @param strExamineDeptId
     *            the strExamineDeptId to set
     */
    public void setStrExamineDeptId(String strExamineDeptId) {
        this.strExamineDeptId = strExamineDeptId;
    }

    /**
     * @return the deptAllStore
     */
    public PageObject getDeptAllStore() {
        return deptAllStore;
    }

    /**
     * @param deptAllStore
     *            the deptAllStore to set
     */
    public void setDeptAllStore(PageObject deptAllStore) {
        this.deptAllStore = deptAllStore;
    }

    /**
     * @return the workOrRestList
     */
    public List<Map<String, Object>> getWorkOrRestList() {
        return workOrRestList;
    }

    /**
     * @param workOrRestList
     *            the workOrRestList to set
     */
    public void setWorkOrRestList(List<Map<String, Object>> workOrRestList) {
        this.workOrRestList = workOrRestList;
    }

    /**
     * @return the strColor
     */
    public String getStrColor() {
        return strColor;
    }

    /**
     * @param strColor
     *            the strColor to set
     */
    public void setStrColor(String strColor) {
        this.strColor = strColor;
    }

    /**
     * @return the loginEmpId
     */
    public String getLoginEmpId() {
        return loginEmpId;
    }

    /**
     * @param loginEmpId
     *            the loginEmpId to set
     */
    public void setLoginEmpId(String loginEmpId) {
        this.loginEmpId = loginEmpId;
    }

    /**
     * @return the loginEmpName
     */
    public String getLoginEmpName() {
        return loginEmpName;
    }

    /**
     * @param loginEmpName
     *            the loginEmpName to set
     */
    public void setLoginEmpName(String loginEmpName) {
        this.loginEmpName = loginEmpName;
    }

    /**
     * @return the sonCheckFlag
     */
    public String getSonCheckFlag() {
        return sonCheckFlag;
    }

    /**
     * @param sonCheckFlag
     *            the sonCheckFlag to set
     */
    public void setSonCheckFlag(String sonCheckFlag) {
        this.sonCheckFlag = sonCheckFlag;
    }

    /**
     * @return the unCheckDeptName
     */
    public String getUnCheckDeptName() {
        return unCheckDeptName;
    }

    /**
     * @param unCheckDeptName
     *            the unCheckDeptName to set
     */
    public void setUnCheckDeptName(String unCheckDeptName) {
        this.unCheckDeptName = unCheckDeptName;
    }

}
