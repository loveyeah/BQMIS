/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import javax.ejb.Stateless;

/**
 * 新进/离职员工统计bean
 * 
 * @author zhouxu
 * @version 1.0
 */
@Stateless
public class HrJTotalBean {
    /**
     * 所属部门
     */
    private String deptName;
    /**
     * 岗位名称
     */
    private String stationName;
    /**
     * 岗位标准人数
     */
    private String stationNum;
    /**
     * 现人数
     */
    private String nowNum;
    /**
     * 新进人数
     */
    private String newNum;
    /**
     * 离职人数
     */
    private String dimissionNum;
    /**
     * 行数计数 
     */
    private Integer cntRow;

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
     * @return the stationName
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * @param stationName
     *            the stationName to set
     */
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    /**
     * @return the stationNum
     */
    public String getStationNum() {
        return stationNum;
    }

    /**
     * @param stationNum
     *            the stationNum to set
     */
    public void setStationNum(String stationNum) {
        this.stationNum = stationNum;
    }

    /**
     * @return the nowNum
     */
    public String getNowNum() {
        return nowNum;
    }

    /**
     * @param nowNum
     *            the nowNum to set
     */
    public void setNowNum(String nowNum) {
        this.nowNum = nowNum;
    }

    /**
     * @return the newNum
     */
    public String getNewNum() {
        return newNum;
    }

    /**
     * @param newNum
     *            the newNum to set
     */
    public void setNewNum(String newNum) {
        this.newNum = newNum;
    }

    /**
     * @return the dimissionNum
     */
    public String getDimissionNum() {
        return dimissionNum;
    }

    /**
     * @param dimissionNum
     *            the dimissionNum to set
     */
    public void setDimissionNum(String dimissionNum) {
        this.dimissionNum = dimissionNum;
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
