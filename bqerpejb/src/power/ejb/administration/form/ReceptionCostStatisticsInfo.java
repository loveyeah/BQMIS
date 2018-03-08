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
public class ReceptionCostStatisticsInfo {
    /** 审批单号 */
    private String applyId;
    /** 申请部门名 */
    private String deptName;
    /** 申请人名 */
    private String name;
    /** 接待日期 */
    private String meetDate;
    /** 就餐费用 */
    private String repastTotal;
    /** 住宿费用 */
    private String roomTotal;
    /** 标准支出 */
    private String payoutBz;
    /** 实际支出 */
    private String payout;
    /** 超支 */
    private String balance;

    /**
     * 审批单号
     * @return the applyId
     */
    public String getApplyId() {
        return applyId;
    }

    /**
     * 审批单号
     * @param applyId
     *            the applyId to set
     */
    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    /**
     * 申请部门
     * @return the deptName
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * 申请部门
     * @param deptName
     *            the deptName to set
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**
     * 申请人
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * 申请人
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 接待日期
     * @return the meetDate
     */
    public String getMeetDate() {
        return meetDate;
    }

    /**
     * 接待日期
     * @param meetDate
     *            the meetDate to set
     */
    public void setMeetDate(String meetDate) {
        this.meetDate = meetDate;
    }

    /**
     * 就餐费用
     * @return the repastTotal
     */
    public String getRepastTotal() {
        return repastTotal;
    }

    /**
     * 就餐费用
     * @param repastTotal
     *            the repastTotal to set
     */
    public void setRepastTotal(String repastTotal) {
        this.repastTotal = repastTotal;
    }

    /**
     * 住宿费用
     * @return the roomTotal
     */
    public String getRoomTotal() {
        return roomTotal;
    }

    /**
     * 住宿费用
     * @param roomTotal
     *            the roomTotal to set
     */
    public void setRoomTotal(String roomTotal) {
        this.roomTotal = roomTotal;
    }

    /**
     * 标准支出
     * @return the payoutBz
     */
    public String getPayoutBz() {
        return payoutBz;
    }

    /**
     * 标准支出
     * @param payoutBz
     *            the payoutBz to set
     */
    public void setPayoutBz(String payoutBz) {
        this.payoutBz = payoutBz;
    }

    /**
     * 实际支出
     * @return the payout
     */
    public String getPayout() {
        return payout;
    }

    /**
     * 实际支出
     * @param payout
     *            the payout to set
     */
    public void setPayout(String payout) {
        this.payout = payout;
    }

    /**
     * 超支
     * @return the balance
     */
    public String getBalance() {
        return balance;
    }

    /**
     * 超支
     * @param balance
     *            the balance to set
     */
    public void setBalance(String balance) {
        this.balance = balance;
    }

}
