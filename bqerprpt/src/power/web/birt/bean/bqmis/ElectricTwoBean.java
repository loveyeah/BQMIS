/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.birt.bean.bqmis;

import java.util.List;

/**
 * 电气二种票数据
 * @author LiuYingwen
 *
 */
public class ElectricTwoBean {
	
    /** 工作票号 */
    private String workticketNo = "";
    /** 机组名 */
    private String machineClass = "";
    /** 工作负责人 */
    private String chargeBy = "";
    /** 工作监护人 */
    private String workWacth = "";
    /** 班组 */
    private String equAttributeName = "";
    /** 部门 */
    private String chargeDept = ""; 
    /** 工作班成员 */
    private String members = "";
    /** 工作班成员数 */
    private String memberCount = "";
//  	/** 工作点名称及设备双重名称 */
//    private String placeAndMachineName = "";
    /** 工作地点 */
    private String workPlace = "";
    /** 工作内容 */
    private String workTask = "";
    /** 计划开始时间 */
    private String planStartDate = "";
    /** 计划结束时间 */
    private String planEndDate = "";
    /** 工作条件(停电不停电) */
    private String workCondition = "";
    /** 注意事项(安全措施) */
    private String safetyOne = "";
    private String safetyTwo = "";
    /** 已执行(安全措施) */
    private String executeOne = "";
    private String executeTwo = "";
    /** 号码 */
    private String number = "";
    /** 号码组数 */
    private String numberCount = "";
    /** 工作票签发人(签名) */
	private String signMan = "";		
	/** 收到工作票时间年 */
	private String acceptDate="";
	/** 值班负责人 接票 */
	private String approveMan4 ="";
    /** 工作地点保留带电部分和补充措施 */
    private String supply = "";
    /** 工作许可人签名 保留安措 */
    private String admissionMan ="";
	/** 值班负责人签名 保留安措 */
    private String watchCharge= "" ;
    /** 工作票签发人 */
    private String approveMan3 ="";	
    /** 工作票签发时间 */
    private String signDate ="";
    /** 点检签发人（签名）*/
    private String checkSignBy = "";
    /** 点检签发日期 */
    private String checkDate = "";
    /** 许可开工时间 */
    private String admissionDate = "";
    /** 单元长 */
    private String startUnitManager = "";
    /** 工作开始许可人(值班员) */
    private String approveMan7 = "";
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
    /** 工作结束时间 */
    private String actualEndDate = "";
    /** 工作结束许可人(值班员) */
    private String approveMan8 = "";
    /** 单元长 */
    private String endUnitManager = "";
    /** 已拆除组数 */
    private String removeCount = "";
    /** 备注 */
    private String workticketMemoOne = "";
    private String workticketMemoTwo = "";
    /** 危险点内容 */
    private String dangerContent = "";
    /** 点检验收人 */
    private String checkBy = "";
    /** 电气二种票附页数据 */
    private List<ElectricTwoDetailBean> electricTwoDetailList;
    /** 分页Flag */
    private boolean fuYeFlag = true;
    /** 是否终结标志 */
    private boolean contentFlg = true;
    /** 是否作废标志 */
    private boolean delete = true;
    /** 工作内容分页内容 */
    private String workPlaceTwo = "";
    /** 安措标题 */
    private String title21="";
    private String title211="";
    private String title22="";
    private String title221="";
    
    /** 工作负责人(许可) */
    private String chargeBy7 = "";
    /** 工作负责人(结束) */
    private String chargeBy8 = "";
    
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
//	/**
//	 * @return the placeAndMachineName
//	 */
//	public String getPlaceAndMachineName() {
//		return placeAndMachineName;
//	}
//	/**
//	 * @param placeAndMachineName the placeAndMachineName to set
//	 */
//	public void setPlaceAndMachineName(String placeAndMachineName) {
//		this.placeAndMachineName = placeAndMachineName;
//	}
	/**
	 * @return the workTask
	 */
	public String getWorkTask() {
		return workTask;
	}
	/**
	 * @param workTask the workTask to set
	 */
	public void setWorkTask(String workTask) {
		this.workTask = workTask;
	}
	/**
	 * @return the workPlace
	 */
	public String getWorkPlace() {
		return workPlace;
	}
	/**
	 * @param workPlace the workPlace to set
	 */
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
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
	 * @return the workCondition
	 */
	public String getWorkCondition() {
		return workCondition;
	}
	/**
	 * @param workCondition the workCondition to set
	 */
	public void setWorkCondition(String workCondition) {
		this.workCondition = workCondition;
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
	 * @return the approveMan3
	 */
	public String getApproveMan3() {
		return approveMan3;
	}
	/**
	 * @param approveMan3 the approveMan3 to set
	 */
	public void setApproveMan3(String approveMan3) {
		this.approveMan3 = approveMan3;
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
	 * @return the startUnitManager
	 */
	public String getStartUnitManager() {
		return startUnitManager;
	}
	/**
	 * @param startUnitManager the startUnitManager to set
	 */
	public void setStartUnitManager(String startUnitManager) {
		this.startUnitManager = startUnitManager;
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
	 * @return the endUnitManager
	 */
	public String getEndUnitManager() {
		return endUnitManager;
	}
	/**
	 * @param endUnitManager the endUnitManager to set
	 */
	public void setEndUnitManager(String endUnitManager) {
		this.endUnitManager = endUnitManager;
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
	 * @return the electricTwoDetailList
	 */
	public List<ElectricTwoDetailBean> getElectricTwoDetailList() {
		return electricTwoDetailList;
	}
	/**
	 * @param electricTwoDetailList the electricTwoDetailList to set
	 */
	public void setElectricTwoDetailList(
			List<ElectricTwoDetailBean> electricTwoDetailList) {
		this.electricTwoDetailList = electricTwoDetailList;
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
	 * @return the contentFlag
	 */
	public boolean isContentFlg() {
		return contentFlg;
	}
	/**
	 * @param contentFlag the contentFlag to set
	 */
	public void setContentFlg(boolean contentFlg) {
		this.contentFlg = contentFlg;
	}
	/**
	 * @return the workPlaceTwo
	 */
	public String getWorkPlaceTwo() {
		return workPlaceTwo;
	}
	/**
	 * @param workPlaceTwo the workPlaceTwo to set
	 */
	public void setWorkPlaceTwo(String workPlaceTwo) {
		this.workPlaceTwo = workPlaceTwo;
	}
	/**
	 * @return the title21
	 */
	public String getTitle21() {
		return title21;
	}
	/**
	 * @param title21 the title21 to set
	 */
	public void setTitle21(String title21) {
		this.title21 = title21;
	}
	/**
	 * @return the title211
	 */
	public String getTitle211() {
		return title211;
	}
	/**
	 * @param title211 the title211 to set
	 */
	public void setTitle211(String title211) {
		this.title211 = title211;
	}
	/**
	 * @return the title22
	 */
	public String getTitle22() {
		return title22;
	}
	/**
	 * @param title22 the title22 to set
	 */
	public void setTitle22(String title22) {
		this.title22 = title22;
	}
	/**
	 * @return the title221
	 */
	public String getTitle221() {
		return title221;
	}
	/**
	 * @param title221 the title221 to set
	 */
	public void setTitle221(String title221) {
		this.title221 = title221;
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
	public String getWorkWacth() {
		return workWacth;
	}
	public void setWorkWacth(String workWacth) {
		this.workWacth = workWacth;
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
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
    
}
    
