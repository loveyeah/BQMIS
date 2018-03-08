/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;

/**
 * OndutyInfo entity.
 * 
 * @author zhouxu
 */
public class MeetCostStatisticsInfo {
    /** 会议申请单号 */
    private String meetId;
    /** 申请部门名 */
    private String deptName;
    /** 申请人名 */
    private String name;
    /** 就餐费用 */
    private String repastTotal;
    /** 用烟费用 */
    private String cigTotal;
    /** 用酒费用 */
    private String wineTotal;
    /** 住宿费用 */
    private String roomTotal;
    /** 实际费用 */
    private String payout;
    /** 预算费用 */
    private String budpayInall;
    /** 超支 */
    private String balance;
    /** 会议名称 */
    private String meetName;
    /** 会议开始时间 */
    private String startMeetDate;
    /**
     * @return the meetId
     */
    public String getMeetId() {
        return meetId;
    }
    /**
     * @param meetId the meetId to set
     */
    public void setMeetId(String meetId) {
        this.meetId = meetId;
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
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the repastTotal
     */
    public String getRepastTotal() {
        return repastTotal;
    }
    /**
     * @param repastTotal the repastTotal to set
     */
    public void setRepastTotal(String repastTotal) {
        this.repastTotal = repastTotal;
    }
    /**
     * @return the cigTotal
     */
    public String getCigTotal() {
        return cigTotal;
    }
    /**
     * @param cigTotal the cigTotal to set
     */
    public void setCigTotal(String cigTotal) {
        this.cigTotal = cigTotal;
    }
    /**
     * @return the wineTotal
     */
    public String getWineTotal() {
        return wineTotal;
    }
    /**
     * @param wineTotal the wineTotal to set
     */
    public void setWineTotal(String wineTotal) {
        this.wineTotal = wineTotal;
    }
    /**
     * @return the roomTotal
     */
    public String getRoomTotal() {
        return roomTotal;
    }
    /**
     * @param roomTotal the roomTotal to set
     */
    public void setRoomTotal(String roomTotal) {
        this.roomTotal = roomTotal;
    }
    /**
     * @return the payout
     */
    public String getPayout() {
        return payout;
    }
    /**
     * @param payout the payout to set
     */
    public void setPayout(String payout) {
        this.payout = payout;
    }
    /**
     * @return the balance
     */
    public String getBalance() {
        return balance;
    }
    /**
     * @param balance the balance to set
     */
    public void setBalance(String balance) {
        this.balance = balance;
    }
    /**
     * @return the meetName
     */
    public String getMeetName() {
        return meetName;
    }
    /**
     * @param meetName the meetName to set
     */
    public void setMeetName(String meetName) {
        this.meetName = meetName;
    }
    /**
     * @return the startMeetDate
     */
    public String getStartMeetDate() {
        return startMeetDate;
    }
    /**
     * @param startMeetDate the startMeetDate to set
     */
    public void setStartMeetDate(String startMeetDate) {
        this.startMeetDate = startMeetDate;
    }
    /**
     * @return the budpayInall
     */
    public String getBudpayInall() {
        return budpayInall;
    }
    /**
     * @param budpayInall the budpayInall to set
     */
    public void setBudpayInall(String budpayInall) {
        this.budpayInall = budpayInall;
    }

}