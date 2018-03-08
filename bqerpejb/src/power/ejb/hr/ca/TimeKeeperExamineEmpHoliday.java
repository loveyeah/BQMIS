/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

/**
 * 考勤员请假bean
 * 
 * @author zhouxu
 */

public class TimeKeeperExamineEmpHoliday implements java.io.Serializable{
    /** 假别id */
    private String vacationTypeId;
    /** 是否包括周末 */
    private String ifWeekEnd;
    /** 开始时间 */
    private String strStartDate;
    /** 结束时间 */
    private String strEndDate;

    /**
     * @return the vacationTypeId
     */
    public String getVacationTypeId() {
        return vacationTypeId;
    }

    /**
     * @param vacationTypeId
     *            the vacationTypeId to set
     */
    public void setVacationTypeId(String vacationTypeId) {
        this.vacationTypeId = vacationTypeId;
    }

    /**
     * @return the ifWeekEnd
     */
    public String getIfWeekEnd() {
        return ifWeekEnd;
    }

    /**
     * @param ifWeekEnd
     *            the ifWeekEnd to set
     */
    public void setIfWeekEnd(String ifWeekEnd) {
        this.ifWeekEnd = ifWeekEnd;
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

}
