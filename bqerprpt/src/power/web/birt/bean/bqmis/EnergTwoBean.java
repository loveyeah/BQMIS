/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.web.birt.bean.bqmis;

import java.util.List;

/**
 * 热控、热力机械二种票数据
 * @author LiuYingwen
 *
 */
public class EnergTwoBean {
	
	/** 工作票号 */
    private String workticketNo = "";
    /** 工作负责人 */
    private String chargeBy = "";
    /** 工作监护人 */
    private String workWacth = "";
    /** 机组 */
    private String equAttributeName = "";
    /** 班组 */
    private String chargeDept = "";
    
    /** 工作班成员 */
    private String members = "";
    private String membersOne = "";
    private String membersTwo = "";
    /** 工作班成员数 */
    private String memberCount = "";
    /** 工作地点 */
    private String workSpace = "";
    /** 工作内容 */
    private String workticketContent = "";
    private String workticketContentOne = "";
    private String workticketContentTwo = "";
    /** 计划开始时间 */
    private String planStartDate = "";
    /** 计划结束时间 */
    private String planEndDate = "";
    /** 工作票签发人 */
    private String signMan = "";
    /** 工作票接收人 */
    private String acceptBy = "";
    /** 签发时间 */
    private String signDate = "";
    /** 接收时间 */
    private String acceptDate = "";
    /** 点检签发人（签名）*/
    private String checkSignBy = "";
    /** 点检签发日期 */
    private String checkDate = "";
    /** 许可工作时间 */
    private String admissionDate = "";
    /** 工作许可人 */
    private String admissionMan = "";
    /** 值班负责人 */
    private String watchCharge = "";
    /** 许可 工作负责人 */
    private String admissionChargeBy = "";
    /** 实际结束时间 */
    private String actualEndDate = "";
    /** 结束 工作许可人 */
    private String actualEndMan = "";
    /** 结束 工作负责人 */
    private String endChargeBy = "";
    /** 结束 点检验收人 */
    private String checkBy = "";
    /** 备注 */
    private String workticketMemo = "";
    /** 危险点内容 */
    private String dangerContent = "";
    /** 工作票考核人 */
    private String checkMan = "";
    /** 考核情况 */
    private String checkStatus = "";
    /** 考核时间 */
    private String checkDateYear = "";
    private String checkDateMonth = "";
    private String checkDateDay = "";
    /** 电气二种票附页数据 */
    private List<EnergDetailBean> energDetailList;
    /** 分页Flag */
    private boolean fuYeFlag = true;
    /** 是否终结标志 */
    private boolean isContentFlg=true;
    /** 是否作废标志 */
    private boolean delete = true;
    /** 危险点列表 */
    private List<WorkticketDanger> dangerList;
    /** 后增加危险点 */
    private List<WorkticketDanger> AddDangerList;
    /** 工作票状态 */
    private String worktickStauts = "";
	/**
	 * @return the workticketNo
	 */
	public String getWorkticketNo() {
		return workticketNo;
	}
	/**
	 * @param workticketNo the workticketNo to set
	 */
	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}
	/**
	 * @return the chargeBy
	 */
	public String getChargeBy() {
		return chargeBy;
	}
	/**
	 * @param chargeBy the chargeBy to set
	 */
	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}
	/**
	 * @return the workWacth
	 */
	public String getWorkWacth() {
		return workWacth;
	}
	/**
	 * @param workWacth the workWacth to set
	 */
	public void setWorkWacth(String workWacth) {
		this.workWacth = workWacth;
	}
	/**
	 * @return the equAttributeName
	 */
	public String getEquAttributeName() {
		return equAttributeName;
	}
	/**
	 * @param equAttributeName the equAttributeName to set
	 */
	public void setEquAttributeName(String equAttributeName) {
		this.equAttributeName = equAttributeName;
	}
	/**
	 * @return the chargeDept
	 */
	public String getChargeDept() {
		return chargeDept;
	}
	/**
	 * @param chargeDept the chargeDept to set
	 */
	public void setChargeDept(String chargeDept) {
		this.chargeDept = chargeDept;
	}
	/**
	 * @return the members
	 */
	public String getMembers() {
		return members;
	}
	/**
	 * @param members the members to set
	 */
	public void setMembers(String members) {
		this.members = members;
	}
	/**
	 * @return the membersOne
	 */
	public String getMembersOne() {
		return membersOne;
	}
	/**
	 * @param membersOne the membersOne to set
	 */
	public void setMembersOne(String membersOne) {
		this.membersOne = membersOne;
	}
	/**
	 * @return the membersTwo
	 */
	public String getMembersTwo() {
		return membersTwo;
	}
	/**
	 * @param membersTwo the membersTwo to set
	 */
	public void setMembersTwo(String membersTwo) {
		this.membersTwo = membersTwo;
	}
	/**
	 * @return the memberCount
	 */
	public String getMemberCount() {
		return memberCount;
	}
	/**
	 * @param memberCount the memberCount to set
	 */
	public void setMemberCount(String memberCount) {
		this.memberCount = memberCount;
	}
	/**
	 * @return the workSpace
	 */
	public String getWorkSpace() {
		return workSpace;
	}
	/**
	 * @param workSpace the workSpace to set
	 */
	public void setWorkSpace(String workSpace) {
		this.workSpace = workSpace;
	}
	/**
	 * @return the workticketContent
	 */
	public String getWorkticketContent() {
		return workticketContent;
	}
	/**
	 * @param workticketContent the workticketContent to set
	 */
	public void setWorkticketContent(String workticketContent) {
		this.workticketContent = workticketContent;
	}
	/**
	 * @return the workticketContentOne
	 */
	public String getWorkticketContentOne() {
		return workticketContentOne;
	}
	/**
	 * @param workticketContentOne the workticketContentOne to set
	 */
	public void setWorkticketContentOne(String workticketContentOne) {
		this.workticketContentOne = workticketContentOne;
	}
	/**
	 * @return the workticketContentTwo
	 */
	public String getWorkticketContentTwo() {
		return workticketContentTwo;
	}
	/**
	 * @param workticketContentTwo the workticketContentTwo to set
	 */
	public void setWorkticketContentTwo(String workticketContentTwo) {
		this.workticketContentTwo = workticketContentTwo;
	}
	/**
	 * @return the planStartDate
	 */
	public String getPlanStartDate() {
		return planStartDate;
	}
	/**
	 * @param planStartDate the planStartDate to set
	 */
	public void setPlanStartDate(String planStartDate) {
		this.planStartDate = planStartDate;
	}
	/**
	 * @return the planEndDate
	 */
	public String getPlanEndDate() {
		return planEndDate;
	}
	/**
	 * @param planEndDate the planEndDate to set
	 */
	public void setPlanEndDate(String planEndDate) {
		this.planEndDate = planEndDate;
	}
	/**
	 * @return the signMan
	 */
	public String getSignMan() {
		return signMan;
	}
	/**
	 * @param signMan the signMan to set
	 */
	public void setSignMan(String signMan) {
		this.signMan = signMan;
	}
	/**
	 * @return the acceptBy
	 */
	public String getAcceptBy() {
		return acceptBy;
	}
	/**
	 * @param acceptBy the acceptBy to set
	 */
	public void setAcceptBy(String acceptBy) {
		this.acceptBy = acceptBy;
	}
	/**
	 * @return the signDate
	 */
	public String getSignDate() {
		return signDate;
	}
	/**
	 * @param signDate the signDate to set
	 */
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	/**
	 * @return the acceptDate
	 */
	public String getAcceptDate() {
		return acceptDate;
	}
	/**
	 * @param acceptDate the acceptDate to set
	 */
	public void setAcceptDate(String acceptDate) {
		this.acceptDate = acceptDate;
	}
	/**
	 * @return the checkSignBy
	 */
	public String getCheckSignBy() {
		return checkSignBy;
	}
	/**
	 * @param checkSignBy the checkSignBy to set
	 */
	public void setCheckSignBy(String checkSignBy) {
		this.checkSignBy = checkSignBy;
	}
	/**
	 * @return the checkDate
	 */
	public String getCheckDate() {
		return checkDate;
	}
	/**
	 * @param checkDate the checkDate to set
	 */
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	/**
	 * @return the admissionDate
	 */
	public String getAdmissionDate() {
		return admissionDate;
	}
	/**
	 * @param admissionDate the admissionDate to set
	 */
	public void setAdmissionDate(String admissionDate) {
		this.admissionDate = admissionDate;
	}
	/**
	 * @return the admissionMan
	 */
	public String getAdmissionMan() {
		return admissionMan;
	}
	/**
	 * @param admissionMan the admissionMan to set
	 */
	public void setAdmissionMan(String admissionMan) {
		this.admissionMan = admissionMan;
	}
	/**
	 * @return the watchCharge
	 */
	public String getWatchCharge() {
		return watchCharge;
	}
	/**
	 * @param watchCharge the watchCharge to set
	 */
	public void setWatchCharge(String watchCharge) {
		this.watchCharge = watchCharge;
	}
	/**
	 * @return the admissionChargeBy
	 */
	public String getAdmissionChargeBy() {
		return admissionChargeBy;
	}
	/**
	 * @param admissionChargeBy the admissionChargeBy to set
	 */
	public void setAdmissionChargeBy(String admissionChargeBy) {
		this.admissionChargeBy = admissionChargeBy;
	}
	/**
	 * @return the actualEndDate
	 */
	public String getActualEndDate() {
		return actualEndDate;
	}
	/**
	 * @param actualEndDate the actualEndDate to set
	 */
	public void setActualEndDate(String actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	/**
	 * @return the actualEndMan
	 */
	public String getActualEndMan() {
		return actualEndMan;
	}
	/**
	 * @param actualEndMan the actualEndMan to set
	 */
	public void setActualEndMan(String actualEndMan) {
		this.actualEndMan = actualEndMan;
	}
	/**
	 * @return the endChargeBy
	 */
	public String getEndChargeBy() {
		return endChargeBy;
	}
	/**
	 * @param endChargeBy the endChargeBy to set
	 */
	public void setEndChargeBy(String endChargeBy) {
		this.endChargeBy = endChargeBy;
	}
	/**
	 * @return the checkBy
	 */
	public String getCheckBy() {
		return checkBy;
	}
	/**
	 * @param checkBy the checkBy to set
	 */
	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}
	/**
	 * @return the workticketMemo
	 */
	public String getWorkticketMemo() {
		return workticketMemo;
	}
	/**
	 * @param workticketMemo the workticketMemo to set
	 */
	public void setWorkticketMemo(String workticketMemo) {
		this.workticketMemo = workticketMemo;
	}
	/**
	 * @return the dangerContent
	 */
	public String getDangerContent() {
		return dangerContent;
	}
	/**
	 * @param dangerContent the dangerContent to set
	 */
	public void setDangerContent(String dangerContent) {
		this.dangerContent = dangerContent;
	}
	/**
	 * @return the checkMan
	 */
	public String getCheckMan() {
		return checkMan;
	}
	/**
	 * @param checkMan the checkMan to set
	 */
	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}
	/**
	 * @return the checkStatus
	 */
	public String getCheckStatus() {
		return checkStatus;
	}
	/**
	 * @param checkStatus the checkStatus to set
	 */
	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}
	/**
	 * @return the checkDateYear
	 */
	public String getCheckDateYear() {
		return checkDateYear;
	}
	/**
	 * @param checkDateYear the checkDateYear to set
	 */
	public void setCheckDateYear(String checkDateYear) {
		this.checkDateYear = checkDateYear;
	}
	/**
	 * @return the checkDateMonth
	 */
	public String getCheckDateMonth() {
		return checkDateMonth;
	}
	/**
	 * @param checkDateMonth the checkDateMonth to set
	 */
	public void setCheckDateMonth(String checkDateMonth) {
		this.checkDateMonth = checkDateMonth;
	}
	/**
	 * @return the checkDateDay
	 */
	public String getCheckDateDay() {
		return checkDateDay;
	}
	/**
	 * @param checkDateDay the checkDateDay to set
	 */
	public void setCheckDateDay(String checkDateDay) {
		this.checkDateDay = checkDateDay;
	}
	/**
	 * @return the energDetailList
	 */
	public List<EnergDetailBean> getEnergDetailList() {
		return energDetailList;
	}
	/**
	 * @param energDetailList the energDetailList to set
	 */
	public void setEnergDetailList(List<EnergDetailBean> energDetailList) {
		this.energDetailList = energDetailList;
	}
	/**
	 * @return the fuYeFlag
	 */
	public boolean isFuYeFlag() {
		return fuYeFlag;
	}
	/**
	 * @param fuYeFlag the fuYeFlag to set
	 */
	public void setFuYeFlag(boolean fuYeFlag) {
		this.fuYeFlag = fuYeFlag;
	}
	/**
	 * @return the isContentFlg
	 */
	public boolean isContentFlg() {
		return isContentFlg;
	}
	/**
	 * @param isContentFlg the isContentFlg to set
	 */
	public void setContentFlg(boolean isContentFlg) {
		this.isContentFlg = isContentFlg;
	}
	/**
	 * @return the dangerList
	 */
	public List<WorkticketDanger> getDangerList() {
		return dangerList;
	}
	/**
	 * @param dangerList the dangerList to set
	 */
	public void setDangerList(List<WorkticketDanger> dangerList) {
		this.dangerList = dangerList;
	}
	/**
	 * @return the addDangerList
	 */
	public List<WorkticketDanger> getAddDangerList() {
		return AddDangerList;
	}
	/**
	 * @param addDangerList the addDangerList to set
	 */
	public void setAddDangerList(List<WorkticketDanger> addDangerList) {
		AddDangerList = addDangerList;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public String getWorktickStauts() {
		return worktickStauts;
	}
	public void setWorktickStauts(String worktickStauts) {
		this.worktickStauts = worktickStauts;
	}

}
