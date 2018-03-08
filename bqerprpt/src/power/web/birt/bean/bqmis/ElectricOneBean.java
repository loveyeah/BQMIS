/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.bean.bqmis;

import java.util.List;

/**
 * 电气一种票数据
 * @author LiuYingwen
 *
 */
public class ElectricOneBean {
	
	/** 工作票号 */
    private String workticketNo = "";    
    /** 工作负责人 */
    private String chargeBy = "";
    /** 工作监护人 */
    private String workWacth = "";
    /** 班组 */
    private String equAttributeName = "";
    /** 机组名 */
    private String machineClass = "";
    /** 工作班成员 */
    private String membersOne = "";
    private String membersTwo = "";
    private String membersThree = "";
    /** 工作班成员数 */
    private String memberCount = "";
    /** 工作地点 */
    private String workSpace = "";
    /** 工作内容 */
    private String workticketContentOne = "";
    private String workticketContentTwo = "";
    /** 计划开始时间 */
    private String planStartDate = "";
    /** 计划结束时间 */
    private String planEndDate= "";
    /** 电气一种票附图 */
    private String workticketMap = "";
    /** 安全措施 */
    private String safetyOne = "";
    private String safetyTwo = "";
    private String safetyThree = "";
    /** 执行情况 */
    private String executeOne = "";
    private String executeTwo = "";
    private String executeThree = "";
    /** 号码 */
    private String number = "";
    /** 号码组数 */
    private String numberCount = "";
    /** 工作票签发人（签名）*/
    private String signBy1 = "";
    /** 工作票签发日期 */
    private String signDate = "";
    /** 点检签发人（签名）*/
    private String checkSignBy = "";
    /** 点检签发日期 */
    private String checkDate = "";
    /** 工作地点保留带电部分和补充措施 */
    private String supply = "";
    /** 工作票接收人（签名）*/
    private String receiveMan = "";
    /** 收到工作票时间 */
    private String acceptDate= "";
    /** 接票值班负责人 */
    private String approveMan4 = "";
    /** 工作许可人签名 补充措施 */
    private String admissionMan = "";
    /** 值班负责人签名 补充措施 */
    private String watchCharge = "";  
//    /** 批准工作开始时间 */
//    private String approvedStartDate = "";
    /** 批准工作结束时间 */
    private String approvedFinishDate = "";
    /** 值长 批准 */
    private String approveMan5 ="";
    /** 许可工作时间 */
    private String admissionDate = "";
    /** 工作许可人 许可 */
    private String approveMan7 = "";
    /** 工作负责人(许可) */
    private String chargeBy7 = "";
    /** 原工作负责人 */
    private String oldChargeBy = "";
    /** 现工作负责人 */
    private String newChargeBy = "";
    /** 变更时间 */
    private String modifyDate = "";
    /** 变更 工作许可人 */
    private String approveManChangeCharge = "";  
    /** 变更 工作票签发人 */
    private String modifySignMan = "";
    /** 变更 值班负责人 */
    private String watchCharge2 = "";
    /** 变更 工作票延期 */
    private String newApprovedFinishDate = "";  
    /** 已延期负责人 */
    private String approveManDelay = ""; 
    /** 延期 运行值班负责人 */
    private String runWatchCharge = "";
    /** 工作票延期值长 */
    private String watcher = "";
    /** 11 允许试运时间 */
    private String testRunDate1 = "";
    private String testRunDate2 = "";
    private String testRunDate3 = "";
    /** 11 工作许可人 */
    private String runLicensor1 = "";
    private String runLicensor2 = "";
    private String runLicensor3 = "";
    /** 11 工作负责人 */
    private String runChargeBy1 = "";
    private String runChargeBy2 = "";
    private String runChargeBy3 = "";
    /** 12 允许恢复工作时间 */
    private String recoveryWorkDate1 = "";
    private String recoveryWorkDate2 = "";
    private String recoveryWorkDate3 = "";
    /** 12 工作许可人 */
    private String recoveryLicensor1 = "";
    private String recoveryLicensor2 = "";
    private String recoveryLicensor3 = "";
    /** 12 工作负责人 */
    private String recoveryChargeBy1 = "";
    private String recoveryChargeBy2 = "";
    private String recoveryChargeBy3 = "";
    /** 全部工作结束时间 */
    private String actualEndDate = "";
    /** 工作负责人(结束) */
    private String chargeBy8 = "";
    /** 点检验收人(结束) */
    private String checkBy = "";
    /** 已结束 工作许可人 */
    private String approveMan8 = ""; 
    /** 接地线共?lineCount 组 */
    private String lineCount = ""; 
    /** 已拆除组数 */
    private String removeCount = "";
    /** 未拆除组数 */
    private String remainCount = "";
    /** 未拆除编号 */
    private String remainNumber = "";
    /** 结束 值班负责人 */
    private String watchCharge3 = "";
    /** 备注 */
    private String workticketMemoOne = "";
    private String workticketMemoTwo = "";
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
    /** 分页 */
    private boolean fuYeFlag = true;
    /** 是否终结标志 */
    private boolean contentFlg = true;
    /** 是否作废标志 */
    private boolean delete = true;
    /** 安措标题 */
    private String title10 = "";
    /** 安措标题 */
    private String title101 = "";
    /** 安措标题 */
    private String title11 = "";
    /** 安措标题 */
    private String title111 = "";
    /** 安措标题 */
    private String title12 = "";
    /** 安措标题 */
    private String title121 = "";
    /** 安措标题 */
    private String title13 = "";    
    
    /** 分页 */
    private String safetyOneFuYe = "";
    private String safetyOneFlag = "";
    private String executeOneFuYe = "";
    private String safetyTwoFuYe = "";
    private String safetyTwoFlag = "";
    private String executeTwoFuYe = "";
    private String safetyThreeFuYe = "";
    private String executeThreeFuYe = "";
    private String supplyFuYe = "";
    /** 工作票状态 */
    private String worktickStauts = "";
    /** 工作负责人(延期) */
    private String chargeByDelay = "";
    /** 电气一种票附页数据 */
    private List<ElectricOneDetailBean> electricOneDetailList;

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
	 * @return the machineClass
	 */
	public String getMachineClass() {
		return machineClass;
	}

	/**
	 * @param machineClass the machineClass to set
	 */
	public void setMachineClass(String machineClass) {
		this.machineClass = machineClass;
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
	 * @return the membersThree
	 */
	public String getMembersThree() {
		return membersThree;
	}

	/**
	 * @param membersThree the membersThree to set
	 */
	public void setMembersThree(String membersThree) {
		this.membersThree = membersThree;
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
	 * @return the workticketMap
	 */
	public String getWorkticketMap() {
		return workticketMap;
	}

	/**
	 * @param workticketMap the workticketMap to set
	 */
	public void setWorkticketMap(String workticketMap) {
		this.workticketMap = workticketMap;
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
	 * @return the executeOne
	 */
	public String getExecuteOne() {
		return executeOne;
	}

	/**
	 * @param executeOne the executeOne to set
	 */
	public void setExecuteOne(String executeOne) {
		this.executeOne = executeOne;
	}

	/**
	 * @return the executeTwo
	 */
	public String getExecuteTwo() {
		return executeTwo;
	}

	/**
	 * @param executeTwo the executeTwo to set
	 */
	public void setExecuteTwo(String executeTwo) {
		this.executeTwo = executeTwo;
	}

	/**
	 * @return the executeThree
	 */
	public String getExecuteThree() {
		return executeThree;
	}

	/**
	 * @param executeThree the executeThree to set
	 */
	public void setExecuteThree(String executeThree) {
		this.executeThree = executeThree;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the numberCount
	 */
	public String getNumberCount() {
		return numberCount;
	}

	/**
	 * @param numberCount the numberCount to set
	 */
	public void setNumberCount(String numberCount) {
		this.numberCount = numberCount;
	}

	public String getSignBy1() {
		return signBy1;
	}

	public void setSignBy1(String signBy1) {
		this.signBy1 = signBy1;
	}

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

	/**
	 * @return the supply
	 */
	public String getSupply() {
		return supply;
	}

	/**
	 * @param supply the supply to set
	 */
	public void setSupply(String supply) {
		this.supply = supply;
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
	 * @return the approveMan4
	 */
	public String getApproveMan4() {
		return approveMan4;
	}

	/**
	 * @param approveMan4 the approveMan4 to set
	 */
	public void setApproveMan4(String approveMan4) {
		this.approveMan4 = approveMan4;
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

//	/**
//	 * @return the approvedStartDate
//	 */
//	public String getApprovedStartDate() {
//		return approvedStartDate;
//	}
//
//	/**
//	 * @param approvedStartDate the approvedStartDate to set
//	 */
//	public void setApprovedStartDate(String approvedStartDate) {
//		this.approvedStartDate = approvedStartDate;
//	}

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
	 * @return the approveMan5
	 */
	public String getApproveMan5() {
		return approveMan5;
	}

	/**
	 * @param approveMan5 the approveMan5 to set
	 */
	public void setApproveMan5(String approveMan5) {
		this.approveMan5 = approveMan5;
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
	 * @return the approveMan7
	 */
	public String getApproveMan7() {
		return approveMan7;
	}

	/**
	 * @param approveMan7 the approveMan7 to set
	 */
	public void setApproveMan7(String approveMan7) {
		this.approveMan7 = approveMan7;
	}

	/**
	 * @return the oldChargeBy
	 */
	public String getOldChargeBy() {
		return oldChargeBy;
	}

	/**
	 * @param oldChargeBy the oldChargeBy to set
	 */
	public void setOldChargeBy(String oldChargeBy) {
		this.oldChargeBy = oldChargeBy;
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
	 * @return the approveManChangeCharge
	 */
//	public String getApproveManChangeCharge() {
//		return approveManChangeCharge;
//	}
//
//	/**
//	 * @param approveManChangeCharge the approveManChangeCharge to set
//	 */
//	public void setApproveManChangeCharge(String approveManChangeCharge) {
//		this.approveManChangeCharge = approveManChangeCharge;
//	}

	/**
	 * @return the modifySignMan
	 */
	public String getModifySignMan() {
		return modifySignMan;
	}

	/**
	 * @param modifySignMan the modifySignMan to set
	 */
	public void setModifySignMan(String modifySignMan) {
		this.modifySignMan = modifySignMan;
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
	 * @return the approveManDelay
	 */
	public String getApproveManDelay() {
		return approveManDelay;
	}

	/**
	 * @param approveManDelay the approveManDelay to set
	 */
	public void setApproveManDelay(String approveManDelay) {
		this.approveManDelay = approveManDelay;
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
	 * @return the approveMan8
	 */
	public String getApproveMan8() {
		return approveMan8;
	}

	/**
	 * @param approveMan8 the approveMan8 to set
	 */
	public void setApproveMan8(String approveMan8) {
		this.approveMan8 = approveMan8;
	}

	/**
	 * @return the lineCount
	 */
	public String getLineCount() {
		return lineCount;
	}

	/**
	 * @param lineCount the lineCount to set
	 */
	public void setLineCount(String lineCount) {
		this.lineCount = lineCount;
	}

	/**
	 * @return the workticketMemoOne
	 */
	public String getWorkticketMemoOne() {
		return workticketMemoOne;
	}

	/**
	 * @param workticketMemoOne the workticketMemoOne to set
	 */
	public void setWorkticketMemoOne(String workticketMemoOne) {
		this.workticketMemoOne = workticketMemoOne;
	}

	/**
	 * @return the workticketMemoTwo
	 */
	public String getWorkticketMemoTwo() {
		return workticketMemoTwo;
	}

	/**
	 * @param workticketMemoTwo the workticketMemoTwo to set
	 */
	public void setWorkticketMemoTwo(String workticketMemoTwo) {
		this.workticketMemoTwo = workticketMemoTwo;
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
	 * @return the title10
	 */
	public String getTitle10() {
		return title10;
	}

	/**
	 * @param title10 the title10 to set
	 */
	public void setTitle10(String title10) {
		this.title10 = title10;
	}

	/**
	 * @return the title101
	 */
	public String getTitle101() {
		return title101;
	}

	/**
	 * @param title101 the title101 to set
	 */
	public void setTitle101(String title101) {
		this.title101 = title101;
	}

	/**
	 * @return the title11
	 */
	public String getTitle11() {
		return title11;
	}

	/**
	 * @param title11 the title11 to set
	 */
	public void setTitle11(String title11) {
		this.title11 = title11;
	}

	/**
	 * @return the title111
	 */
	public String getTitle111() {
		return title111;
	}

	/**
	 * @param title111 the title111 to set
	 */
	public void setTitle111(String title111) {
		this.title111 = title111;
	}

	/**
	 * @return the title12
	 */
	public String getTitle12() {
		return title12;
	}

	/**
	 * @param title12 the title12 to set
	 */
	public void setTitle12(String title12) {
		this.title12 = title12;
	}

	/**
	 * @return the title121
	 */
	public String getTitle121() {
		return title121;
	}

	/**
	 * @param title121 the title121 to set
	 */
	public void setTitle121(String title121) {
		this.title121 = title121;
	}

	/**
	 * @return the title13
	 */
	public String getTitle13() {
		return title13;
	}

	/**
	 * @param title13 the title13 to set
	 */
	public void setTitle13(String title13) {
		this.title13 = title13;
	}

	/**
	 * @return the safetyOneFuYe
	 */
	public String getSafetyOneFuYe() {
		return safetyOneFuYe;
	}

	/**
	 * @param safetyOneFuYe the safetyOneFuYe to set
	 */
	public void setSafetyOneFuYe(String safetyOneFuYe) {
		this.safetyOneFuYe = safetyOneFuYe;
	}

	/**
	 * @return the safetyOneFlag
	 */
	public String getSafetyOneFlag() {
		return safetyOneFlag;
	}

	/**
	 * @param safetyOneFlag the safetyOneFlag to set
	 */
	public void setSafetyOneFlag(String safetyOneFlag) {
		this.safetyOneFlag = safetyOneFlag;
	}

	/**
	 * @return the executeOneFuYe
	 */
	public String getExecuteOneFuYe() {
		return executeOneFuYe;
	}

	/**
	 * @param executeOneFuYe the executeOneFuYe to set
	 */
	public void setExecuteOneFuYe(String executeOneFuYe) {
		this.executeOneFuYe = executeOneFuYe;
	}

	/**
	 * @return the safetyTwoFuYe
	 */
	public String getSafetyTwoFuYe() {
		return safetyTwoFuYe;
	}

	/**
	 * @param safetyTwoFuYe the safetyTwoFuYe to set
	 */
	public void setSafetyTwoFuYe(String safetyTwoFuYe) {
		this.safetyTwoFuYe = safetyTwoFuYe;
	}

	/**
	 * @return the safetyTwoFlag
	 */
	public String getSafetyTwoFlag() {
		return safetyTwoFlag;
	}

	/**
	 * @param safetyTwoFlag the safetyTwoFlag to set
	 */
	public void setSafetyTwoFlag(String safetyTwoFlag) {
		this.safetyTwoFlag = safetyTwoFlag;
	}

	/**
	 * @return the executeTwoFuYe
	 */
	public String getExecuteTwoFuYe() {
		return executeTwoFuYe;
	}

	/**
	 * @param executeTwoFuYe the executeTwoFuYe to set
	 */
	public void setExecuteTwoFuYe(String executeTwoFuYe) {
		this.executeTwoFuYe = executeTwoFuYe;
	}

	/**
	 * @return the worktickStauts
	 */
	public String getWorktickStauts() {
		return worktickStauts;
	}

	/**
	 * @param worktickStauts the worktickStauts to set
	 */
	public void setWorktickStauts(String worktickStauts) {
		this.worktickStauts = worktickStauts;
	}

	/**
	 * @return the electricOneDetailList
	 */
	public List<ElectricOneDetailBean> getElectricOneDetailList() {
		return electricOneDetailList;
	}

	/**
	 * @param electricOneDetailList the electricOneDetailList to set
	 */
	public void setElectricOneDetailList(
			List<ElectricOneDetailBean> electricOneDetailList) {
		this.electricOneDetailList = electricOneDetailList;
	}

	/**
	 * @return the contentFlg
	 */
	public boolean isContentFlg() {
		return contentFlg;
	}

	/**
	 * @param contentFlg the contentFlg to set
	 */
	public void setContentFlg(boolean contentFlg) {
		this.contentFlg = contentFlg;
	}

	/**
	 * @return the chargeBy7
	 */
	public String getChargeBy7() {
		return chargeBy7;
	}

	/**
	 * @param chargeBy7 the chargeBy7 to set
	 */
	public void setChargeBy7(String chargeBy7) {
		this.chargeBy7 = chargeBy7;
	}

	/**
	 * @return the chargeBy8
	 */
	public String getChargeBy8() {
		return chargeBy8;
	}

	/**
	 * @param chargeBy8 the chargeBy8 to set
	 */
	public void setChargeBy8(String chargeBy8) {
		this.chargeBy8 = chargeBy8;
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
	 * @return the receiveMan
	 */
	public String getReceiveMan() {
		return receiveMan;
	}

	/**
	 * @param receiveMan the receiveMan to set
	 */
	public void setReceiveMan(String receiveMan) {
		this.receiveMan = receiveMan;
	}

	/**
	 * @return the approveManChangeCharge
	 */
	public String getApproveManChangeCharge() {
		return approveManChangeCharge;
	}

	/**
	 * @param approveManChangeCharge the approveManChangeCharge to set
	 */
	public void setApproveManChangeCharge(String approveManChangeCharge) {
		this.approveManChangeCharge = approveManChangeCharge;
	}

	/**
	 * @return the watchCharge2
	 */
	public String getWatchCharge2() {
		return watchCharge2;
	}

	/**
	 * @param watchCharge2 the watchCharge2 to set
	 */
	public void setWatchCharge2(String watchCharge2) {
		this.watchCharge2 = watchCharge2;
	}

	/**
	 * @return the runWatchCharge
	 */
	public String getRunWatchCharge() {
		return runWatchCharge;
	}

	/**
	 * @param runWatchCharge the runWatchCharge to set
	 */
	public void setRunWatchCharge(String runWatchCharge) {
		this.runWatchCharge = runWatchCharge;
	}

	/**
	 * @return the testRunDate1
	 */
	public String getTestRunDate1() {
		return testRunDate1;
	}

	/**
	 * @param testRunDate1 the testRunDate1 to set
	 */
	public void setTestRunDate1(String testRunDate1) {
		this.testRunDate1 = testRunDate1;
	}

	/**
	 * @return the testRunDate2
	 */
	public String getTestRunDate2() {
		return testRunDate2;
	}

	/**
	 * @param testRunDate2 the testRunDate2 to set
	 */
	public void setTestRunDate2(String testRunDate2) {
		this.testRunDate2 = testRunDate2;
	}

	/**
	 * @return the testRunDate3
	 */
	public String getTestRunDate3() {
		return testRunDate3;
	}

	/**
	 * @param testRunDate3 the testRunDate3 to set
	 */
	public void setTestRunDate3(String testRunDate3) {
		this.testRunDate3 = testRunDate3;
	}

	/**
	 * @return the runLicensor1
	 */
	public String getRunLicensor1() {
		return runLicensor1;
	}

	/**
	 * @param runLicensor1 the runLicensor1 to set
	 */
	public void setRunLicensor1(String runLicensor1) {
		this.runLicensor1 = runLicensor1;
	}

	/**
	 * @return the runLicensor2
	 */
	public String getRunLicensor2() {
		return runLicensor2;
	}

	/**
	 * @param runLicensor2 the runLicensor2 to set
	 */
	public void setRunLicensor2(String runLicensor2) {
		this.runLicensor2 = runLicensor2;
	}

	/**
	 * @return the runLicensor3
	 */
	public String getRunLicensor3() {
		return runLicensor3;
	}

	/**
	 * @param runLicensor3 the runLicensor3 to set
	 */
	public void setRunLicensor3(String runLicensor3) {
		this.runLicensor3 = runLicensor3;
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
	 * @return the recoveryWorkDate1
	 */
	public String getRecoveryWorkDate1() {
		return recoveryWorkDate1;
	}

	/**
	 * @param recoveryWorkDate1 the recoveryWorkDate1 to set
	 */
	public void setRecoveryWorkDate1(String recoveryWorkDate1) {
		this.recoveryWorkDate1 = recoveryWorkDate1;
	}

	/**
	 * @return the recoveryWorkDate2
	 */
	public String getRecoveryWorkDate2() {
		return recoveryWorkDate2;
	}

	/**
	 * @param recoveryWorkDate2 the recoveryWorkDate2 to set
	 */
	public void setRecoveryWorkDate2(String recoveryWorkDate2) {
		this.recoveryWorkDate2 = recoveryWorkDate2;
	}

	/**
	 * @return the recoveryWorkDate3
	 */
	public String getRecoveryWorkDate3() {
		return recoveryWorkDate3;
	}

	/**
	 * @param recoveryWorkDate3 the recoveryWorkDate3 to set
	 */
	public void setRecoveryWorkDate3(String recoveryWorkDate3) {
		this.recoveryWorkDate3 = recoveryWorkDate3;
	}

	/**
	 * @return the recoveryLicensor1
	 */
	public String getRecoveryLicensor1() {
		return recoveryLicensor1;
	}

	/**
	 * @param recoveryLicensor1 the recoveryLicensor1 to set
	 */
	public void setRecoveryLicensor1(String recoveryLicensor1) {
		this.recoveryLicensor1 = recoveryLicensor1;
	}

	/**
	 * @return the recoveryLicensor2
	 */
	public String getRecoveryLicensor2() {
		return recoveryLicensor2;
	}

	/**
	 * @param recoveryLicensor2 the recoveryLicensor2 to set
	 */
	public void setRecoveryLicensor2(String recoveryLicensor2) {
		this.recoveryLicensor2 = recoveryLicensor2;
	}

	/**
	 * @return the recoveryLicensor3
	 */
	public String getRecoveryLicensor3() {
		return recoveryLicensor3;
	}

	/**
	 * @param recoveryLicensor3 the recoveryLicensor3 to set
	 */
	public void setRecoveryLicensor3(String recoveryLicensor3) {
		this.recoveryLicensor3 = recoveryLicensor3;
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
	 * @return the removeCount
	 */
	public String getRemoveCount() {
		return removeCount;
	}

	/**
	 * @param removeCount the removeCount to set
	 */
	public void setRemoveCount(String removeCount) {
		this.removeCount = removeCount;
	}

	/**
	 * @return the remainCount
	 */
	public String getRemainCount() {
		return remainCount;
	}

	/**
	 * @param remainCount the remainCount to set
	 */
	public void setRemainCount(String remainCount) {
		this.remainCount = remainCount;
	}

	/**
	 * @return the remainNumber
	 */
	public String getRemainNumber() {
		return remainNumber;
	}

	/**
	 * @param remainNumber the remainNumber to set
	 */
	public void setRemainNumber(String remainNumber) {
		this.remainNumber = remainNumber;
	}

	/**
	 * @return the watchCharge3
	 */
	public String getWatchCharge3() {
		return watchCharge3;
	}

	/**
	 * @param watchCharge3 the watchCharge3 to set
	 */
	public void setWatchCharge3(String watchCharge3) {
		this.watchCharge3 = watchCharge3;
	}

	/**
	 * @return the safetyThreeFuYe
	 */
	public String getSafetyThreeFuYe() {
		return safetyThreeFuYe;
	}

	/**
	 * @param safetyThreeFuYe the safetyThreeFuYe to set
	 */
	public void setSafetyThreeFuYe(String safetyThreeFuYe) {
		this.safetyThreeFuYe = safetyThreeFuYe;
	}

	/**
	 * @return the executeThreeFuYe
	 */
	public String getExecuteThreeFuYe() {
		return executeThreeFuYe;
	}

	/**
	 * @param executeThreeFuYe the executeThreeFuYe to set
	 */
	public void setExecuteThreeFuYe(String executeThreeFuYe) {
		this.executeThreeFuYe = executeThreeFuYe;
	}

	/**
	 * @return the supplyFuYe
	 */
	public String getSupplyFuYe() {
		return supplyFuYe;
	}

	/**
	 * @param supplyFuYe the supplyFuYe to set
	 */
	public void setSupplyFuYe(String supplyFuYe) {
		this.supplyFuYe = supplyFuYe;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

}
   
    