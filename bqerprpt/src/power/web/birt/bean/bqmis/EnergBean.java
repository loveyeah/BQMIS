/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.web.birt.bean.bqmis;

import java.util.List;

/**
 * 热力机械，热控票Bean
 *
 * @author LiuYingwen
 * @version 1.0
 */
public class EnergBean {

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
    /** 需要退出热工保护或自动装置名称 */
    private String needEquipmentName = "";
    /** 安全措施 */
    private String safetyOne = "";
    private String safetyTwo = "";
    private String safetyThree = "";
    private String safetyFour = "";
    private String safetyFive = "";
    /** 措施执行情况 */
    private String executeRightOne = "";
    private String executeRightTwo = "";
    private String executeRightThree = "";
    private String executeRightFour = "";
    private String executeRightFive = "";
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
    /** 补充的安全措施 */
    private String safetyContentRepair = "";
    /** 补充的安全措施情况 */
    private String safetyContentRight = "";
    /** 值长（值班负责人） */
    private String watcher = "";
    /** 批准工作开始时间 */
    private String approvedStartDate = "";
    /** 批准工作结束时间 */
    private String approvedFinishDate = "";
    private String approvedMan = "";
    /** 许可工作时间 */
    private String admissionDate = "";
    /** 工作许可人 */
    private String admissionMan = "";
    /** 运行值班负责人 */
    private String unitChargeBy = "";
    /** 许可负责人 */
    private String admissionChargeBy = "";
    /** 变更时间 */
    private String modifyDate = "";
    /** 现工作负责人 */
    private String newChargeBy = "";
    private String modifyMan = "";
    /** 变更 运行班负责人 */
    private String runWatchChargeBy2 = "";
    /** 延长批准结束时间 */
    private String newApprovedFinishDate = "";
    /** 延长 值长 */
    private String newApprovedFinishMan = "";
    /** 延长 运行班负责人 */
    private String runWatchChargeBy3 = "";
    /** 延长 工作负责人 */
    private String chargeByDelay = "";
    /** 交回 */
    private String dealBackDateOne = "";
    private String dealBackDateTwo = "";
    private String dealBackDateThree = "";
    private String dealBackManOne = "";
    private String dealBackManTwo = "";
    private String dealBackManThree = "";
    private String runChargeBy1 = "";
    private String runChargeBy2 = "";
    private String runChargeBy3 = "";

    /** 恢复 */
    private String resumeDateOne = "";
    private String resumeDateTwo = "";
    private String resumeDateThree = "";
    private String resumeManOne = "";
    private String resumeManTwo = "";
    private String resumeManThree = "";
    private String recoveryChargeBy1 = "";
    private String recoveryChargeBy2 = "";
    private String recoveryChargeBy3 = "";

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
    /** 安措附页数据 */
    private List<EnergDetailBean> energDetailList;
    /** 安措附页数据 */
    private List<EnergDetailBean> energDetailAddList;
    /** 分页Flag */
    private boolean fuYeFlag = true;
    /** 是否终结标志 */
    private boolean isContentFlg=true;
    /** 是否作废标志 */
    private boolean delete = true;
    /**专业 add by drdu 091231*/
    private String repairSpecail = "";
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
	 * @return the safetyOne
	 */
	public String getSafetyOne() {
		return safetyOne;
	}
	/**
	 * @param safetyOne the safetyOne to set
	 */
	public void setSafetyOne(String safetyOne) {
		this.safetyOne = safetyOne;
	}
	/**
	 * @return the safetyTwo
	 */
	public String getSafetyTwo() {
		return safetyTwo;
	}
	/**
	 * @param safetyTwo the safetyTwo to set
	 */
	public void setSafetyTwo(String safetyTwo) {
		this.safetyTwo = safetyTwo;
	}
	/**
	 * @return the safetyThree
	 */
	public String getSafetyThree() {
		return safetyThree;
	}
	/**
	 * @param safetyThree the safetyThree to set
	 */
	public void setSafetyThree(String safetyThree) {
		this.safetyThree = safetyThree;
	}
	/**
	 * @return the safetyFour
	 */
	public String getSafetyFour() {
		return safetyFour;
	}
	/**
	 * @param safetyFour the safetyFour to set
	 */
	public void setSafetyFour(String safetyFour) {
		this.safetyFour = safetyFour;
	}
	/**
	 * @return the safetyFive
	 */
	public String getSafetyFive() {
		return safetyFive;
	}
	/**
	 * @param safetyFive the safetyFive to set
	 */
	public void setSafetyFive(String safetyFive) {
		this.safetyFive = safetyFive;
	}
	/**
	 * @return the executeRightOne
	 */
	public String getExecuteRightOne() {
		return executeRightOne;
	}
	/**
	 * @param executeRightOne the executeRightOne to set
	 */
	public void setExecuteRightOne(String executeRightOne) {
		this.executeRightOne = executeRightOne;
	}
	/**
	 * @return the executeRightTwo
	 */
	public String getExecuteRightTwo() {
		return executeRightTwo;
	}
	/**
	 * @param executeRightTwo the executeRightTwo to set
	 */
	public void setExecuteRightTwo(String executeRightTwo) {
		this.executeRightTwo = executeRightTwo;
	}
	/**
	 * @return the executeRightThree
	 */
	public String getExecuteRightThree() {
		return executeRightThree;
	}
	/**
	 * @param executeRightThree the executeRightThree to set
	 */
	public void setExecuteRightThree(String executeRightThree) {
		this.executeRightThree = executeRightThree;
	}
	/**
	 * @return the executeRightFour
	 */
	public String getExecuteRightFour() {
		return executeRightFour;
	}
	/**
	 * @param executeRightFour the executeRightFour to set
	 */
	public void setExecuteRightFour(String executeRightFour) {
		this.executeRightFour = executeRightFour;
	}
	/**
	 * @return the executeRightFive
	 */
	public String getExecuteRightFive() {
		return executeRightFive;
	}
	/**
	 * @param executeRightFive the executeRightFive to set
	 */
	public void setExecuteRightFive(String executeRightFive) {
		this.executeRightFive = executeRightFive;
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
	 * @return the safetyContentRepair
	 */
	public String getSafetyContentRepair() {
		return safetyContentRepair;
	}
	/**
	 * @param safetyContentRepair the safetyContentRepair to set
	 */
	public void setSafetyContentRepair(String safetyContentRepair) {
		this.safetyContentRepair = safetyContentRepair;
	}
	/**
	 * @return the safetyContentRight
	 */
	public String getSafetyContentRight() {
		return safetyContentRight;
	}
	/**
	 * @param safetyContentRight the safetyContentRight to set
	 */
	public void setSafetyContentRight(String safetyContentRight) {
		this.safetyContentRight = safetyContentRight;
	}
	/**
	 * @return the watcher
	 */
	public String getWatcher() {
		return watcher;
	}
	/**
	 * @param watcher the watcher to set
	 */
	public void setWatcher(String watcher) {
		this.watcher = watcher;
	}
	/**
	 * @return the unitChargeBy
	 */
	public String getUnitChargeBy() {
		return unitChargeBy;
	}
	/**
	 * @param unitChargeBy the unitChargeBy to set
	 */
	public void setUnitChargeBy(String unitChargeBy) {
		this.unitChargeBy = unitChargeBy;
	}
	/**
	 * @return the approvedStartDate
	 */
	public String getApprovedStartDate() {
		return approvedStartDate;
	}
	/**
	 * @param approvedStartDate the approvedStartDate to set
	 */
	public void setApprovedStartDate(String approvedStartDate) {
		this.approvedStartDate = approvedStartDate;
	}
	/**
	 * @return the approvedFinishDate
	 */
	public String getApprovedFinishDate() {
		return approvedFinishDate;
	}
	/**
	 * @param approvedFinishDate the approvedFinishDate to set
	 */
	public void setApprovedFinishDate(String approvedFinishDate) {
		this.approvedFinishDate = approvedFinishDate;
	}
	/**
	 * @return the approvedMan
	 */
	public String getApprovedMan() {
		return approvedMan;
	}
	/**
	 * @param approvedMan the approvedMan to set
	 */
	public void setApprovedMan(String approvedMan) {
		this.approvedMan = approvedMan;
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
	 * @return the modifyDate
	 */
	public String getModifyDate() {
		return modifyDate;
	}
	/**
	 * @param modifyDate the modifyDate to set
	 */
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	/**
	 * @return the newChargeBy
	 */
	public String getNewChargeBy() {
		return newChargeBy;
	}
	/**
	 * @param newChargeBy the newChargeBy to set
	 */
	public void setNewChargeBy(String newChargeBy) {
		this.newChargeBy = newChargeBy;
	}
	/**
	 * @return the modifyMan
	 */
	public String getModifyMan() {
		return modifyMan;
	}
	/**
	 * @param modifyMan the modifyMan to set
	 */
	public void setModifyMan(String modifyMan) {
		this.modifyMan = modifyMan;
	}
	/**
	 * @return the newApprovedFinishDate
	 */
	public String getNewApprovedFinishDate() {
		return newApprovedFinishDate;
	}
	/**
	 * @param newApprovedFinishDate the newApprovedFinishDate to set
	 */
	public void setNewApprovedFinishDate(String newApprovedFinishDate) {
		this.newApprovedFinishDate = newApprovedFinishDate;
	}
	/**
	 * @return the newApprovedFinishMan
	 */
	public String getNewApprovedFinishMan() {
		return newApprovedFinishMan;
	}
	/**
	 * @param newApprovedFinishMan the newApprovedFinishMan to set
	 */
	public void setNewApprovedFinishMan(String newApprovedFinishMan) {
		this.newApprovedFinishMan = newApprovedFinishMan;
	}
	/**
	 * @return the dealBackDateOne
	 */
	public String getDealBackDateOne() {
		return dealBackDateOne;
	}
	/**
	 * @param dealBackDateOne the dealBackDateOne to set
	 */
	public void setDealBackDateOne(String dealBackDateOne) {
		this.dealBackDateOne = dealBackDateOne;
	}
	/**
	 * @return the dealBackDateTwo
	 */
	public String getDealBackDateTwo() {
		return dealBackDateTwo;
	}
	/**
	 * @param dealBackDateTwo the dealBackDateTwo to set
	 */
	public void setDealBackDateTwo(String dealBackDateTwo) {
		this.dealBackDateTwo = dealBackDateTwo;
	}
	/**
	 * @return the dealBackDateThree
	 */
	public String getDealBackDateThree() {
		return dealBackDateThree;
	}
	/**
	 * @param dealBackDateThree the dealBackDateThree to set
	 */
	public void setDealBackDateThree(String dealBackDateThree) {
		this.dealBackDateThree = dealBackDateThree;
	}
	/**
	 * @return the dealBackManOne
	 */
	public String getDealBackManOne() {
		return dealBackManOne;
	}
	/**
	 * @param dealBackManOne the dealBackManOne to set
	 */
	public void setDealBackManOne(String dealBackManOne) {
		this.dealBackManOne = dealBackManOne;
	}
	/**
	 * @return the dealBackManTwo
	 */
	public String getDealBackManTwo() {
		return dealBackManTwo;
	}
	/**
	 * @param dealBackManTwo the dealBackManTwo to set
	 */
	public void setDealBackManTwo(String dealBackManTwo) {
		this.dealBackManTwo = dealBackManTwo;
	}
	/**
	 * @return the dealBackManThree
	 */
	public String getDealBackManThree() {
		return dealBackManThree;
	}
	/**
	 * @param dealBackManThree the dealBackManThree to set
	 */
	public void setDealBackManThree(String dealBackManThree) {
		this.dealBackManThree = dealBackManThree;
	}
	/**
	 * @return the resumeDateOne
	 */
	public String getResumeDateOne() {
		return resumeDateOne;
	}
	/**
	 * @param resumeDateOne the resumeDateOne to set
	 */
	public void setResumeDateOne(String resumeDateOne) {
		this.resumeDateOne = resumeDateOne;
	}
	/**
	 * @return the resumeDateTwo
	 */
	public String getResumeDateTwo() {
		return resumeDateTwo;
	}
	/**
	 * @param resumeDateTwo the resumeDateTwo to set
	 */
	public void setResumeDateTwo(String resumeDateTwo) {
		this.resumeDateTwo = resumeDateTwo;
	}
	/**
	 * @return the resumeDateThree
	 */
	public String getResumeDateThree() {
		return resumeDateThree;
	}
	/**
	 * @param resumeDateThree the resumeDateThree to set
	 */
	public void setResumeDateThree(String resumeDateThree) {
		this.resumeDateThree = resumeDateThree;
	}
	/**
	 * @return the resumeManOne
	 */
	public String getResumeManOne() {
		return resumeManOne;
	}
	/**
	 * @param resumeManOne the resumeManOne to set
	 */
	public void setResumeManOne(String resumeManOne) {
		this.resumeManOne = resumeManOne;
	}
	/**
	 * @return the resumeManTwo
	 */
	public String getResumeManTwo() {
		return resumeManTwo;
	}
	/**
	 * @param resumeManTwo the resumeManTwo to set
	 */
	public void setResumeManTwo(String resumeManTwo) {
		this.resumeManTwo = resumeManTwo;
	}
	/**
	 * @return the resumeManThree
	 */
	public String getResumeManThree() {
		return resumeManThree;
	}
	/**
	 * @param resumeManThree the resumeManThree to set
	 */
	public void setResumeManThree(String resumeManThree) {
		this.resumeManThree = resumeManThree;
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
	 * @return the isContentFlag
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
	public String getWorkWacth() {
		return workWacth;
	}
	public void setWorkWacth(String workWacth) {
		this.workWacth = workWacth;
	}
	/**
	 * @return the needEquipmentName
	 */
	public String getNeedEquipmentName() {
		return needEquipmentName;
	}
	/**
	 * @param needEquipmentName the needEquipmentName to set
	 */
	public void setNeedEquipmentName(String needEquipmentName) {
		this.needEquipmentName = needEquipmentName;
	}
	/**
	 * @return the runChargeBy1
	 */
	public String getRunChargeBy1() {
		return runChargeBy1;
	}
	/**
	 * @param runChargeBy1 the runChargeBy1 to set
	 */
	public void setRunChargeBy1(String runChargeBy1) {
		this.runChargeBy1 = runChargeBy1;
	}
	/**
	 * @return the runChargeBy2
	 */
	public String getRunChargeBy2() {
		return runChargeBy2;
	}
	/**
	 * @param runChargeBy2 the runChargeBy2 to set
	 */
	public void setRunChargeBy2(String runChargeBy2) {
		this.runChargeBy2 = runChargeBy2;
	}
	/**
	 * @return the runChargeBy3
	 */
	public String getRunChargeBy3() {
		return runChargeBy3;
	}
	/**
	 * @param runChargeBy3 the runChargeBy3 to set
	 */
	public void setRunChargeBy3(String runChargeBy3) {
		this.runChargeBy3 = runChargeBy3;
	}
	/**
	 * @return the recoveryChargeBy1
	 */
	public String getRecoveryChargeBy1() {
		return recoveryChargeBy1;
	}
	/**
	 * @param recoveryChargeBy1 the recoveryChargeBy1 to set
	 */
	public void setRecoveryChargeBy1(String recoveryChargeBy1) {
		this.recoveryChargeBy1 = recoveryChargeBy1;
	}
	/**
	 * @return the recoveryChargeBy2
	 */
	public String getRecoveryChargeBy2() {
		return recoveryChargeBy2;
	}
	/**
	 * @param recoveryChargeBy2 the recoveryChargeBy2 to set
	 */
	public void setRecoveryChargeBy2(String recoveryChargeBy2) {
		this.recoveryChargeBy2 = recoveryChargeBy2;
	}
	/**
	 * @return the recoveryChargeBy3
	 */
	public String getRecoveryChargeBy3() {
		return recoveryChargeBy3;
	}
	/**
	 * @param recoveryChargeBy3 the recoveryChargeBy3 to set
	 */
	public void setRecoveryChargeBy3(String recoveryChargeBy3) {
		this.recoveryChargeBy3 = recoveryChargeBy3;
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
	 * @return the runWatchChargeBy2
	 */
	public String getRunWatchChargeBy2() {
		return runWatchChargeBy2;
	}
	/**
	 * @param runWatchChargeBy2 the runWatchChargeBy2 to set
	 */
	public void setRunWatchChargeBy2(String runWatchChargeBy2) {
		this.runWatchChargeBy2 = runWatchChargeBy2;
	}
	/**
	 * @return the runWatchChargeBy3
	 */
	public String getRunWatchChargeBy3() {
		return runWatchChargeBy3;
	}
	/**
	 * @param runWatchChargeBy3 the runWatchChargeBy3 to set
	 */
	public void setRunWatchChargeBy3(String runWatchChargeBy3) {
		this.runWatchChargeBy3 = runWatchChargeBy3;
	}
	/**
	 * @return the chargeByDelay
	 */
	public String getChargeByDelay() {
		return chargeByDelay;
	}
	/**
	 * @param chargeByDelay the chargeByDelay to set
	 */
	public void setChargeByDelay(String chargeByDelay) {
		this.chargeByDelay = chargeByDelay;
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
	 * @return the energDetailAddList
	 */
	public List<EnergDetailBean> getEnergDetailAddList() {
		return energDetailAddList;
	}
	/**
	 * @param energDetailAddList the energDetailAddList to set
	 */
	public void setEnergDetailAddList(List<EnergDetailBean> energDetailAddList) {
		this.energDetailAddList = energDetailAddList;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
	public String getRepairSpecail() {
		return repairSpecail;
	}
	public void setRepairSpecail(String repairSpecail) {
		this.repairSpecail = repairSpecail;
	}
}
