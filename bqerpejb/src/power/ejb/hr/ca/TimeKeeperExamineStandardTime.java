/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import power.ear.comm.ejb.PageObject;

/**
 * 职工考勤记录查询bean
 * 
 * @author zhouxu
 * @version 1.0
 */

public class TimeKeeperExamineStandardTime implements java.io.Serializable{
    /** 员工考勤信息 */
    private PageObject pobj;
    /** 上午上班时间 */
    private String amBeginTime;
    /** 上午下班时间 */
    private String amEndTime;
    /** 下午上班时间 */
    private String pmBeginTime;
    /** 下午下班时间 */
    private String pmEndTime;

    /**
     * @return the pobj
     */
    public PageObject getPobj() {
        return pobj;
    }

    /**
     * @param pobj
     *            the pobj to set
     */
    public void setPobj(PageObject pobj) {
        this.pobj = pobj;
    }

    /**
     * @return the amBeginTime
     */
    public String getAmBeginTime() {
        return amBeginTime;
    }

    /**
     * @param amBeginTime
     *            the amBeginTime to set
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
     * @param amEndTime
     *            the amEndTime to set
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
     * @param pmBeginTime
     *            the pmBeginTime to set
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
     * @param pmEndTime
     *            the pmEndTime to set
     */
    public void setPmEndTime(String pmEndTime) {
        this.pmEndTime = pmEndTime;
    }

}
