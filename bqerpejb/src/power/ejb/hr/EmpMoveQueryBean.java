/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.util.Date;

/**
 * 员工调动查询Bean
 * 
 * @author 黄维杰
 * @version 1.0
 */
public class EmpMoveQueryBean implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    // 人员基本信息表
    /** 中文名 */
    private String chsName;
    /** 员工编码 */
    private String empCode;
    
    // 部门设置表
    /** 所属部门名称或调动前部门名称 */
    private String deptNameFirst;
    /** 借调部门名称或调动后部门名称 */
    private String deptNameSecond;
    
    // 岗位设置表
    /** 所属岗位名称或调动前岗位名称 */
    private String stationNameBefore;
    /** 调动后岗位名称 */
    private String stationNameAfter;
    
    // 岗位调动单
    /** 调动时间 */
    private String removeDate;
    /** 执行时间 */
    private String doDate;
    /** 岗位调动单备注 */
    private String memoRemove;
    /** 起薪时间 */
    private String do2Date;
    /** 通知单编号 */
    private String requisitionNo;
    /** 单据状态 */
    private String dcmState;
    
    // 员工借调登记
    /** 员工借调ID */
    private String hrJEmployeeborrowInId;
    /** 开始日期 */
    private String startDate;
    /** 结束日期 */
    private String endDate;
    /** 是否已回 */
    private String ifBack;
    /** 员工借调登记备注 */
    private String memoBorrow;
    /** 单据状态 */
    private String dcmStatus;

    // 岗位级别设置表
    /** 调动前岗位级别名称 */
    private String stationLevelNameBefore;
    /** 调动后岗位级别名称 */
    private String stationLevelNameAfter;

    /** 行数计数 */
    private Integer cntRow;

    /**
     * @return the deptNameFirst
     */
    public String getDeptNameFirst() {
        return deptNameFirst;
    }
    /**
     * @param deptNameFirst the deptNameFirst to set
     */
    public void setDeptNameFirst(String deptNameFirst) {
        this.deptNameFirst = deptNameFirst;
    }
    /**
     * @return the deptNameSecond
     */
    public String getDeptNameSecond() {
        return deptNameSecond;
    }
    /**
     * @param deptNameSecond the deptNameSecond to set
     */
    public void setDeptNameSecond(String deptNameSecond) {
        this.deptNameSecond = deptNameSecond;
    }
    /**
     * @return the stationNameBefore
     */
    public String getStationNameBefore() {
        return stationNameBefore;
    }
    /**
     * @param stationNameBefore the stationNameBefore to set
     */
    public void setStationNameBefore(String stationNameBefore) {
        this.stationNameBefore = stationNameBefore;
    }
    /**
     * @return the stationNameAfter
     */
    public String getStationNameAfter() {
        return stationNameAfter;
    }
    /**
     * @param stationNameAfter the stationNameAfter to set
     */
    public void setStationNameAfter(String stationNameAfter) {
        this.stationNameAfter = stationNameAfter;
    }
    /**
     * @return the removeDate
     */
    public String getRemoveDate() {
        return removeDate;
    }
    /**
     * @param removeDate the removeDate to set
     */
    public void setRemoveDate(String removeDate) {
        this.removeDate = removeDate;
    }
    /**
     * @return the doDate
     */
    public String getDoDate() {
        return doDate;
    }
    /**
     * @param doDate the doDate to set
     */
    public void setDoDate(String doDate) {
        this.doDate = doDate;
    }
    /**
     * @return the memoRemove
     */
    public String getMemoRemove() {
        return memoRemove;
    }
    /**
     * @param memoRemove the memoRemove to set
     */
    public void setMemoRemove(String memoRemove) {
        this.memoRemove = memoRemove;
    }
    /**
     * @return the do2Date
     */
    public String getDo2Date() {
        return do2Date;
    }
    /**
     * @param do2Date the do2Date to set
     */
    public void setDo2Date(String do2Date) {
        this.do2Date = do2Date;
    }
    /**
     * @return the requisitionNo
     */
    public String getRequisitionNo() {
        return requisitionNo;
    }
    /**
     * @param requisitionNo the requisitionNo to set
     */
    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }
    /**
     * @return the dcmState
     */
    public String getDcmState() {
        return dcmState;
    }
    /**
     * @param dcmState the dcmState to set
     */
    public void setDcmState(String dcmState) {
        this.dcmState = dcmState;
    }
    /**
     * @return the hrJEmployeeborrowInId
     */
    public String getHrJEmployeeborrowInId() {
        return hrJEmployeeborrowInId;
    }
    /**
     * @param hrJEmployeeborrowInId the hrJEmployeeborrowInId to set
     */
    public void setHrJEmployeeborrowInId(String hrJEmployeeborrowInId) {
        this.hrJEmployeeborrowInId = hrJEmployeeborrowInId;
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
     * @return the ifBack
     */
    public String getIfBack() {
        return ifBack;
    }
    /**
     * @param ifBack the ifBack to set
     */
    public void setIfBack(String ifBack) {
        this.ifBack = ifBack;
    }
    /**
     * @return the memoBorrow
     */
    public String getMemoBorrow() {
        return memoBorrow;
    }
    /**
     * @param memoBorrow the memoBorrow to set
     */
    public void setMemoBorrow(String memoBorrow) {
        this.memoBorrow = memoBorrow;
    }
    /**
     * @return the dcmStatus
     */
    public String getDcmStatus() {
        return dcmStatus;
    }
    /**
     * @param dcmStatus the dcmStatus to set
     */
    public void setDcmStatus(String dcmStatus) {
        this.dcmStatus = dcmStatus;
    }
    /**
     * @return the stationLevelNameBefore
     */
    public String getStationLevelNameBefore() {
        return stationLevelNameBefore;
    }
    /**
     * @param stationLevelNameBefore the stationLevelNameBefore to set
     */
    public void setStationLevelNameBefore(String stationLevelNameBefore) {
        this.stationLevelNameBefore = stationLevelNameBefore;
    }
    /**
     * @return the stationLevelNameAfter
     */
    public String getStationLevelNameAfter() {
        return stationLevelNameAfter;
    }
    /**
     * @param stationLevelNameAfter the stationLevelNameAfter to set
     */
    public void setStationLevelNameAfter(String stationLevelNameAfter) {
        this.stationLevelNameAfter = stationLevelNameAfter;
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
     * @return the empCode
     */
    public String getEmpCode() {
        return empCode;
    }
    /**
     * @param empCode the empCode to set
     */
    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }
    /**
     * @return the cntRow
     */
    public Integer getCntRow() {
        return cntRow;
    }
    /**
     * @param cntRow the cntRow to set
     */
    public void setCntRow(Integer cntRow) {
        this.cntRow = cntRow;
    }

}
