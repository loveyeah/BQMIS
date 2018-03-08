/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;
/**
 * 会议审批bean
 * 
 * @author chen shoujiang
 */
public class MeetApprove implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	/** 会议审批单号 */	
	private String meetId;
	/**隐藏审批单号*/
	private String meetHidId;
	/** 姓名 */	
	private String name;
	/** 部门名称 */	
	private String depName;
	/** 会议开始时间 */	
	private String startMeetDate;
	/** 会议结束时间 */	
	private String endMeetDate;
	/** 会议名称 */	
	private String meetName;
	/** 会议地点 */	
	private String meetPlace;
	/** 会场要求 */	
	private String roomNeed;
	/** 会议其他要求 */	
	private String meetOther;
	/** 会议用烟名称 */	
	private String cigName;
	/** 会议用烟价格*/	
	private String cigPrice;
	/** 会议用烟数量 */	
	private String cigNum;
	/** 会议用酒名称 */	
	private String wineName;
	/** 会议用酒价格*/	
	private String winePrice;
	/** 会议用酒数量 */	
	private String wineNum;
	/** 会议住宿-套房数量 */	
	private String tfNum;
	/** 会议住宿-套房用品 */	
	private String tfThing;
	/** 会议住宿-单间数量 */	
	private String djNum;
	/** 会议住宿-单间用品 */	
	private String djThing;
	/** 会议住宿-标间数量 */	
	private String bjNum;
	/** 会议住宿-标间用品 */	
	private String bjThing;
	/** 就餐时间 */	
	private String dinnerTime;
	/** 就餐人数 */	
	private String dinnerNum;
	/** 用餐标准 */	
	private String dinnerBz;
	/** 预计费用汇总 */	
	private String budpayInall;
	/** 实际费用汇总 */	
	private String realpayInall;
	/** 费用名称 */	
	private String payName;
	/** 费用预算 */	
	private String payBudget;
	/** 实际费用 */	
	private String payReal;
	/** 单据状态 **/
	private String dcmStatus;
	/** 差额 **/
	private String balance;
	
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getDcmStatus() {
		return dcmStatus;
	}
	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}
	public String getMeetId() {
		return meetId;
	}
	public void setMeetId(String meetId) {
		this.meetId = meetId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepName() {
		return depName;
	}
	public void setDepName(String depName) {
		this.depName = depName;
	}
	public String getStartMeetDate() {
		return startMeetDate;
	}
	public void setStartMeetDate(String startMeetDate) {
		this.startMeetDate = startMeetDate;
	}
	public String getEndMeetDate() {
		return endMeetDate;
	}
	public void setEndMeetDate(String endMeetDate) {
		this.endMeetDate = endMeetDate;
	}
	public String getMeetName() {
		return meetName;
	}
	public void setMeetName(String meetName) {
		this.meetName = meetName;
	}
	public String getMeetPlace() {
		return meetPlace;
	}
	public void setMeetPlace(String meetPlace) {
		this.meetPlace = meetPlace;
	}
	public String getRoomNeed() {
		return roomNeed;
	}
	public void setRoomNeed(String roomNeed) {
		this.roomNeed = roomNeed;
	}
	public String getMeetOther() {
		return meetOther;
	}
	public void setMeetOther(String meetOther) {
		this.meetOther = meetOther;
	}
	public String getCigName() {
		return cigName;
	}
	public void setCigName(String cigName) {
		this.cigName = cigName;
	}
	public String getCigPrice() {
		return cigPrice;
	}
	public void setCigPrice(String cigPrice) {
		this.cigPrice = cigPrice;
	}
	public String getCigNum() {
		return cigNum;
	}
	public void setCigNum(String cigNum) {
		this.cigNum = cigNum;
	}
	public String getWineName() {
		return wineName;
	}
	public void setWineName(String wineName) {
		this.wineName = wineName;
	}
	public String getWineNum() {
		return wineNum;
	}
	public void setWineNum(String wineNum) {
		this.wineNum = wineNum;
	}
	public String getTfNum() {
		return tfNum;
	}
	public void setTfNum(String tfNum) {
		this.tfNum = tfNum;
	}
	public String getTfThing() {
		return tfThing;
	}
	public void setTfThing(String tfThing) {
		this.tfThing = tfThing;
	}
	public String getDjNum() {
		return djNum;
	}
	public void setDjNum(String djNum) {
		this.djNum = djNum;
	}
	public String getDjThing() {
		return djThing;
	}
	public void setDjThing(String djThing) {
		this.djThing = djThing;
	}
	public String getBjNum() {
		return bjNum;
	}
	public void setBjNum(String bjNum) {
		this.bjNum = bjNum;
	}
	public String getBjThing() {
		return bjThing;
	}
	public void setBjThing(String bjThing) {
		this.bjThing = bjThing;
	}
	public String getDinnerTime() {
		return dinnerTime;
	}
	public void setDinnerTime(String dinnerTime) {
		this.dinnerTime = dinnerTime;
	}
	public String getDinnerNum() {
		return dinnerNum;
	}
	public void setDinnerNum(String dinnerNum) {
		this.dinnerNum = dinnerNum;
	}
	public String getDinnerBz() {
		return dinnerBz;
	}
	public void setDinnerBz(String dinnerBz) {
		this.dinnerBz = dinnerBz;
	}
	public String getBudpayInall() {
		return budpayInall;
	}
	public void setBudpayInall(String budpayInall) {
		this.budpayInall = budpayInall;
	}
	public String getRealpayInall() {
		return realpayInall;
	}
	public void setRealpayInall(String realpayInall) {
		this.realpayInall = realpayInall;
	}
	public String getPayName() {
		return payName;
	}
	public void setPayName(String payName) {
		this.payName = payName;
	}
	public String getPayBudget() {
		return payBudget;
	}
	public void setPayBudget(String payBudget) {
		this.payBudget = payBudget;
	}
	public String getPayReal() {
		return payReal;
	}
	public void setPayReal(String payReal) {
		this.payReal = payReal;
	}
	/**
	 * @return the winePrice
	 */
	public String getWinePrice() {
		return winePrice;
	}
	/**
	 * @param winePrice the winePrice to set
	 */
	public void setWinePrice(String winePrice) {
		this.winePrice = winePrice;
	}
	public String getMeetHidId() {
		return meetHidId;
	}
	public void setMeetHidId(String meetHidId) {
		this.meetHidId = meetHidId;
	}
	
	
}
