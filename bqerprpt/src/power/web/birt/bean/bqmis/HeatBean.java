/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.web.birt.bean.bqmis;

import java.util.List;

/**
 *  动火一种、二种票数据
 * @author LiuYingwen
 *
 */
public class HeatBean {

	/** 工作票号 */
    private String workticketNo = "";
    /** 动火部门 */
    private String dhDept = "";
    /** 机组 */
    private String equAttributeName = "";
    /** 班组 */
    private String chargeDept = "";
    /** 动火工作负责人 */
    private String fireChargeBy = "";
    /** 动火地点 */
    private String workSpace = "";
    /** 设备名称 */
    private String machineName = "";
    /** 动火工作内容 */
    private String fireWorkContent = ""; 
    /** 申请开始时间 */
    private String planStartDate = "";
    /** 申请结束时间 */
    private String planEndDate = "";
    /** 检修应采取的安全措施 */
    private String repairNeedSafetyContent = "";
    /** 运行应采取的安全措施 */
    private String safetyNeedSafetyContent = "";
    /** 消防队应采取的安全措施 */
    private String fireNeedSafetyContent = "";
    /** 动火工作票签发人 */
    private String fireSignMan = "";
    /** 消防部门负责人 */
    private String fireChargeDept = "";
    /** 安监部门负责人 */
    private String safetyChargeDept = "";
    /** 厂领导 */
    private String factoryManager = "";
    /** 运行班长 */
    private String runMonitor = "";
    /** 值长 */
    private String watcher = "";   
    /** 测量结果 */
    private String measureResult = "";
    /** 取样测量地点 */
    private String measureSpace = "";
    /** 使用仪器 */
    private String userMachine = "";
    /** 可燃气体(粉尘浓度) */
    private String combustibleGas = "";
    /** 测量值 */
    private String measureValue1 = "";
    private String measureValue2 = "";
    private String measureValue3 = "";
    private String measureValue4 = "";
    private String measureValue5 = "";
    /** 测量时间 */
    private String measureTime1 = "";
    private String measureTime2 = "";
    private String measureTime3 = "";
    private String measureTime4 = "";
    private String measureTime5 = "";
    /** 测量人 */
    private String surveyor1 = "";
    private String surveyor2 = "";
    private String surveyor3 = "";
    private String surveyor4 = "";
    private String surveyor5 = "";
    /** 运行补充措施 */
    private String runSupplySafetyContent = "";
    /** 检修补充措施 */
    private String repairSupplySafetyContent = "";
    /** 消防队补充措施 */
    private String fireSupplySafetyContent = "";
    /** 工作负责人 */
    private String chargeBy = "";
    /** 消防监护人 (安全措施)*/
    private String fireWatcherMan1 = "";
    /** 运行许可人 */
    private String approveMan = "";
    /** 消防监护人 (符合要求)*/
    private String fireWatcherMan2 = "";
    /** 许可工作时间 */
    private String admissionDate = "";
    /** 允许 值长签字 */
    private String fireWatcherStaMan = "";
    /** 允许 动火执行人签字 */
    private String fireExeStaMan = "";
    /** 结束动火时间 */
    private String fireEndDate = "";
    /** 结束 动火执行人签字 */
    private String fireExeEndMan = "";
    /** 结束 消防监护人签字*/
    private String fireWatcherMan3 = "";
    /** 结束 动火工作负责人签字 */
    private String fireEndChargeBy = "";
    /** 结束 值长签字 */
    private String fireWatcherEndMan = "";
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
    /** 运行许可人签字 */
    private String runAdmissionMan  = ""; 
    /** 运行应采取的安全措施： */
    private String securityTitle41 = "";
    /** 检修应采取的安全措施： */
    private String securityTitle42 = "";
    /** 消防队应采取的安全措施： */
    private String securityTitle43 = "";
    /** 运行补充措施： */
    private String securityTitle44 = "";
    /** 检修补充措施： */
    private String securityTitle45 = "";
    /** 消防队补充措施： */
    private String securityTitle46 = "";
    /** 动火许可人 结束 */
    private String fireEndAdmissionMan="";
    /** 附表显示标识 */
    private boolean safetyTableFlg=true;
    /** 附表显示列表 */
    private List<String> safetyList;
    /** 是否终结标志 */
    private boolean contentFlg=true;
    /** 是否作废标志 */
    private boolean delete = true;
    /** 附页工作内容 */
    private String fireWorkContentExtra="";
    /** 动火负责人签字 */
    private String fireChargeBySign="";
    /** 动火负责人 */
    private String fireAdmissionChargeBy = "";
    /** 值长审批 (动火2票) */
    private String dutyCharge = "";
    
   
	
	public String getDutyCharge() {
		return dutyCharge;
	}
	public void setDutyCharge(String dutyCharge) {
		this.dutyCharge = dutyCharge;
	}
	/**
	 * @return the fireChargeBySign
	 */
	public String getFireChargeBySign() {
		return fireChargeBySign;
	}
	/**
	 * @param fireChargeBySign the fireChargeBySign to set
	 */
	public void setFireChargeBySign(String fireChargeBySign) {
		this.fireChargeBySign = fireChargeBySign;
	}
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
	 * @return the fireChargeBy
	 */
	public String getFireChargeBy() {
		return fireChargeBy;
	}
	/**
	 * @param fireChargeBy the fireChargeBy to set
	 */
	public void setFireChargeBy(String fireChargeBy) {
		this.fireChargeBy = fireChargeBy;
	}
	/**
	 * @return the fireAdmissionChargeBy
	 */
	public String getFireAdmissionChargeBy() {
		return fireAdmissionChargeBy;
	}
	/**
	 * @param fireAdmissionChargeBy the fireAdmissionChargeBy to set
	 */
	public void setFireAdmissionChargeBy(String fireAdmissionChargeBy) {
		this.fireAdmissionChargeBy = fireAdmissionChargeBy;
	}
	/**
	 * @return the fireEndChargeBy
	 */
	public String getFireEndChargeBy() {
		return fireEndChargeBy;
	}
	/**
	 * @param fireEndChargeBy the fireEndChargeBy to set
	 */
	public void setFireEndChargeBy(String fireEndChargeBy) {
		this.fireEndChargeBy = fireEndChargeBy;
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
	 * @return the machineName
	 */
	public String getMachineName() {
		return machineName;
	}
	/**
	 * @param machineName the machineName to set
	 */
	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}
	/**
	 * @return the fireWorkContent
	 */
	public String getFireWorkContent() {
		return fireWorkContent;
	}
	/**
	 * @param fireWorkContent the fireWorkContent to set
	 */
	public void setFireWorkContent(String fireWorkContent) {
		this.fireWorkContent = fireWorkContent;
	}
	/**
	 * @return the measureResult
	 */
	public String getMeasureResult() {
		return measureResult;
	}
	/**
	 * @param measureResult the measureResult to set
	 */
	public void setMeasureResult(String measureResult) {
		this.measureResult = measureResult;
	}
	/**
	 * @return the measureSpace
	 */
	public String getMeasureSpace() {
		return measureSpace;
	}
	/**
	 * @param measureSpace the measureSpace to set
	 */
	public void setMeasureSpace(String measureSpace) {
		this.measureSpace = measureSpace;
	}
	/**
	 * @return the userMachine
	 */
	public String getUserMachine() {
		return userMachine;
	}
	/**
	 * @param userMachine the userMachine to set
	 */
	public void setUserMachine(String userMachine) {
		this.userMachine = userMachine;
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
	 * @return the safetyNeedSafetyContent
	 */
	public String getSafetyNeedSafetyContent() {
		return safetyNeedSafetyContent;
	}
	/**
	 * @param safetyNeedSafetyContent the safetyNeedSafetyContent to set
	 */
	public void setSafetyNeedSafetyContent(String safetyNeedSafetyContent) {
		this.safetyNeedSafetyContent = safetyNeedSafetyContent;
	}
	/**
	 * @return the repairNeedSafetyContent
	 */
	public String getRepairNeedSafetyContent() {
		return repairNeedSafetyContent;
	}
	/**
	 * @param repairNeedSafetyContent the repairNeedSafetyContent to set
	 */
	public void setRepairNeedSafetyContent(String repairNeedSafetyContent) {
		this.repairNeedSafetyContent = repairNeedSafetyContent;
	}
	/**
	 * @return the fireNeedSafetyContent
	 */
	public String getFireNeedSafetyContent() {
		return fireNeedSafetyContent;
	}
	/**
	 * @param fireNeedSafetyContent the fireNeedSafetyContent to set
	 */
	public void setFireNeedSafetyContent(String fireNeedSafetyContent) {
		this.fireNeedSafetyContent = fireNeedSafetyContent;
	}
	/**
	 * @return the runSupplySafetyContent
	 */
	public String getRunSupplySafetyContent() {
		return runSupplySafetyContent;
	}
	/**
	 * @param runSupplySafetyContent the runSupplySafetyContent to set
	 */
	public void setRunSupplySafetyContent(String runSupplySafetyContent) {
		this.runSupplySafetyContent = runSupplySafetyContent;
	}
	/**
	 * @return the repairSupplySafetyContent
	 */
	public String getRepairSupplySafetyContent() {
		return repairSupplySafetyContent;
	}
	/**
	 * @param repairSupplySafetyContent the repairSupplySafetyContent to set
	 */
	public void setRepairSupplySafetyContent(String repairSupplySafetyContent) {
		this.repairSupplySafetyContent = repairSupplySafetyContent;
	}
	/**
	 * @return the fireSignMan
	 */
	public String getFireSignMan() {
		return fireSignMan;
	}
	/**
	 * @param fireSignMan the fireSignMan to set
	 */
	public void setFireSignMan(String fireSignMan) {
		this.fireSignMan = fireSignMan;
	}
	/**
	 * @return the fireChargeDept
	 */
	public String getFireChargeDept() {
		return fireChargeDept;
	}
	/**
	 * @param fireChargeDept the fireChargeDept to set
	 */
	public void setFireChargeDept(String fireChargeDept) {
		this.fireChargeDept = fireChargeDept;
	}
	/**
	 * @return the safetyChargeDept
	 */
	public String getSafetyChargeDept() {
		return safetyChargeDept;
	}
	/**
	 * @param safetyChargeDept the safetyChargeDept to set
	 */
	public void setSafetyChargeDept(String safetyChargeDept) {
		this.safetyChargeDept = safetyChargeDept;
	}
	public String getFactoryManager() {
		return factoryManager;
	}
	public void setFactoryManager(String factoryManager) {
		this.factoryManager = factoryManager;
	}
	/**
	 * @return the approveMan
	 */
	public String getApproveMan() {
		return approveMan;
	}
	/**
	 * @param approveMan the approveMan to set
	 */
	public void setApproveMan(String approveMan) {
		this.approveMan = approveMan;
	}
	/**
	 * @return the fireEndDate
	 */
	public String getFireEndDate() {
		return fireEndDate;
	}
	/**
	 * @param fireEndDate the fireEndDate to set
	 */
	public void setFireEndDate(String fireEndDate) {
		this.fireEndDate = fireEndDate;
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
	 * @return the runAdmissionMan
	 */
	public String getRunAdmissionMan() {
		return runAdmissionMan;
	}
	/**
	 * @param runAdmissionMan the runAdmissionMan to set
	 */
	public void setRunAdmissionMan(String runAdmissionMan) {
		this.runAdmissionMan = runAdmissionMan;
	}
	/**
	 * @return the securityTitle41
	 */
	public String getSecurityTitle41() {
		return securityTitle41;
	}
	/**
	 * @param securityTitle41 the securityTitle41 to set
	 */
	public void setSecurityTitle41(String securityTitle41) {
		this.securityTitle41 = securityTitle41;
	}
	/**
	 * @return the securityTitle42
	 */
	public String getSecurityTitle42() {
		return securityTitle42;
	}
	/**
	 * @param securityTitle42 the securityTitle42 to set
	 */
	public void setSecurityTitle42(String securityTitle42) {
		this.securityTitle42 = securityTitle42;
	}
	/**
	 * @return the securityTitle44
	 */
	public String getSecurityTitle44() {
		return securityTitle44;
	}
	/**
	 * @param securityTitle44 the securityTitle44 to set
	 */
	public void setSecurityTitle44(String securityTitle44) {
		this.securityTitle44 = securityTitle44;
	}
	/**
	 * @return the securityTitle45
	 */
	public String getSecurityTitle45() {
		return securityTitle45;
	}
	/**
	 * @param securityTitle45 the securityTitle44 to set
	 */
	public void setSecurityTitle45(String securityTitle45) {
		this.securityTitle45 = securityTitle45;
	}
	/**
	 * @return the fireEndAdmissionMan
	 */
	public String getFireEndAdmissionMan() {
		return fireEndAdmissionMan;
	}
	/**
	 * @param fireEndAdmissionMan the fireEndAdmissionMan to set
	 */
	public void setFireEndAdmissionMan(String fireEndAdmissionMan) {
		this.fireEndAdmissionMan = fireEndAdmissionMan;
	}
	/**
	 * @return the safetyTableFlg
	 */
	public boolean getSafetyTableFlg() {
		return safetyTableFlg;
	}
	/**
	 * @param safetyTableFlg the safetyTableFlg to set
	 */
	public void setSafetyTableFlg(boolean safetyTableFlg) {
		this.safetyTableFlg = safetyTableFlg;
	}
	/**
	 * @return the safetyList
	 */
	public List<String> getSafetyList() {
		return safetyList;
	}
	/**
	 * @param safetyList the safetyList to set
	 */
	public void setSafetyList(List<String> safetyList) {
		this.safetyList = safetyList;
	}
	public String getMeasureValue1() {
		return measureValue1;
	}
	public void setMeasureValue1(String measureValue1) {
		this.measureValue1 = measureValue1;
	}
	public String getMeasureValue2() {
		return measureValue2;
	}
	public void setMeasureValue2(String measureValue2) {
		this.measureValue2 = measureValue2;
	}
	public String getMeasureValue3() {
		return measureValue3;
	}
	public void setMeasureValue3(String measureValue3) {
		this.measureValue3 = measureValue3;
	}
	public String getMeasureValue4() {
		return measureValue4;
	}
	public void setMeasureValue4(String measureValue4) {
		this.measureValue4 = measureValue4;
	}
	public String getMeasureValue5() {
		return measureValue5;
	}
	public void setMeasureValue5(String measureValue5) {
		this.measureValue5 = measureValue5;
	}
	public String getMeasureTime1() {
		return measureTime1;
	}
	public void setMeasureTime1(String measureTime1) {
		this.measureTime1 = measureTime1;
	}
	public String getMeasureTime2() {
		return measureTime2;
	}
	public void setMeasureTime2(String measureTime2) {
		this.measureTime2 = measureTime2;
	}
	public String getMeasureTime3() {
		return measureTime3;
	}
	public void setMeasureTime3(String measureTime3) {
		this.measureTime3 = measureTime3;
	}
	public String getMeasureTime4() {
		return measureTime4;
	}
	public void setMeasureTime4(String measureTime4) {
		this.measureTime4 = measureTime4;
	}
	public String getMeasureTime5() {
		return measureTime5;
	}
	public void setMeasureTime5(String measureTime5) {
		this.measureTime5 = measureTime5;
	}
	public String getSurveyor1() {
		return surveyor1;
	}
	public void setSurveyor1(String surveyor1) {
		this.surveyor1 = surveyor1;
	}
	public String getSurveyor2() {
		return surveyor2;
	}
	public void setSurveyor2(String surveyor2) {
		this.surveyor2 = surveyor2;
	}
	public String getSurveyor3() {
		return surveyor3;
	}
	public void setSurveyor3(String surveyor3) {
		this.surveyor3 = surveyor3;
	}
	public String getSurveyor4() {
		return surveyor4;
	}
	public void setSurveyor4(String surveyor4) {
		this.surveyor4 = surveyor4;
	}
	public String getSurveyor5() {
		return surveyor5;
	}
	public void setSurveyor5(String surveyor5) {
		this.surveyor5 = surveyor5;
	}
	/**
	 * @return the securityTitle43
	 */
	public String getSecurityTitle43() {
		return securityTitle43;
	}
	/**
	 * @param securityTitle43 the securityTitle43 to set
	 */
	public void setSecurityTitle43(String securityTitle43) {
		this.securityTitle43 = securityTitle43;
	}
	/**
	 * @return the securityTitle46
	 */
	public String getSecurityTitle46() {
		return securityTitle46;
	}
	/**
	 * @param securityTitle46 the securityTitle46 to set
	 */
	public void setSecurityTitle46(String securityTitle46) {
		this.securityTitle46 = securityTitle46;
	}
	/**
	 * @return the fireSupplySafetyContent
	 */
	public String getFireSupplySafetyContent() {
		return fireSupplySafetyContent;
	}
	/**
	 * @param fireSupplySafetyContent the fireSupplySafetyContent to set
	 */
	public void setFireSupplySafetyContent(String fireSupplySafetyContent) {
		this.fireSupplySafetyContent = fireSupplySafetyContent;
	}
	/**
	 * @return the dhDept
	 */
	public String getDhDept() {
		return dhDept;
	}
	/**
	 * @param dhDept the dhDept to set
	 */
	public void setDhDept(String dhDept) {
		this.dhDept = dhDept;
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
	 * @return the fireWorkContentExtra
	 */
	public String getFireWorkContentExtra() {
		return fireWorkContentExtra;
	}
	/**
	 * @param fireWorkContentExtra the fireWorkContentExtra to set
	 */
	public void setFireWorkContentExtra(String fireWorkContentExtra) {
		this.fireWorkContentExtra = fireWorkContentExtra;
	}
	/**
	 * @return the fireExeStaMan
	 */
	public String getFireExeStaMan() {
		return fireExeStaMan;
	}
	/**
	 * @param fireExeStaMan the fireExeStaMan to set
	 */
	public void setFireExeStaMan(String fireExeStaMan) {
		this.fireExeStaMan = fireExeStaMan;
	}
	/**
	 * @return the fireExeEndMan
	 */
	public String getFireExeEndMan() {
		return fireExeEndMan;
	}
	/**
	 * @param fireExeEndMan the fireExeEndMan to set
	 */
	public void setFireExeEndMan(String fireExeEndMan) {
		this.fireExeEndMan = fireExeEndMan;
	}
	/**
	 * @return the fireWatcherStaMan
	 */
	public String getFireWatcherStaMan() {
		return fireWatcherStaMan;
	}
	/**
	 * @param fireWatcherStaMan the fireWatcherStaMan to set
	 */
	public void setFireWatcherStaMan(String fireWatcherStaMan) {
		this.fireWatcherStaMan = fireWatcherStaMan;
	}
	/**
	 * @return the fireWatcherEndMan
	 */
	public String getFireWatcherEndMan() {
		return fireWatcherEndMan;
	}
	/**
	 * @param fireWatcherEndMan the fireWatcherEndMan to set
	 */
	public void setFireWatcherEndMan(String fireWatcherEndMan) {
		this.fireWatcherEndMan = fireWatcherEndMan;
	}
	public String getCombustibleGas() {
		return combustibleGas;
	}
	public void setCombustibleGas(String combustibleGas) {
		this.combustibleGas = combustibleGas;
	}
	public String getFireWatcherMan1() {
		return fireWatcherMan1;
	}
	public void setFireWatcherMan1(String fireWatcherMan1) {
		this.fireWatcherMan1 = fireWatcherMan1;
	}
	public String getFireWatcherMan2() {
		return fireWatcherMan2;
	}
	public void setFireWatcherMan2(String fireWatcherMan2) {
		this.fireWatcherMan2 = fireWatcherMan2;
	}
	public String getRunMonitor() {
		return runMonitor;
	}
	public void setRunMonitor(String runMonitor) {
		this.runMonitor = runMonitor;
	}
	public String getFireWatcherMan3() {
		return fireWatcherMan3;
	}
	public void setFireWatcherMan3(String fireWatcherMan3) {
		this.fireWatcherMan3 = fireWatcherMan3;
	}
	public boolean isDelete() {
		return delete;
	}
	public void setDelete(boolean delete) {
		this.delete = delete;
	}
    
}
