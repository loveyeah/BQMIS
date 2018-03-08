/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

/**
 * 合同台帐查询Bean
 * 
 * @author jincong
 * @version 1.0
 */
public class ContractQuery implements java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	// 人员基本信息表
	/** 中文名 */
	private String chsName;
	/** 员工编码 */
	private String empCode;
	/** 档案编号 */
	private String archivesId;
	/** 性别 */
	private String sex;
	/** 出生日期 */
	private String birthday;
	/** 参加工作时间 */
	private String workDate;
	/** 入职时间 */
	private String missionDate;
	
	// 劳动合同登记表
	/** 人员ID */
	private String empId;
	/** 劳动合同编号 */
	private String workContractNo;
	/** 劳动合同续签标志 */
	private String contractContinueMark;
	/** 劳动合同签字日期 */
	private String workSignDate;
	/** 开始日期 */
	private String startDate;
	/** 结束日期 */
	private String endDate;
	/** 试用期开始日期 */
	private String tryoutStartDate;
	/** 试用期终止日期 */
	private String tryoutEndDate;
	/** 备注 */
	private String memoContract;
	/** 劳动合同签定ID */
	private String workContractIdContract;
	/** 甲方地址 */
	private String firstAddress;
	/** 是否劳动合同正在执行 */
	private String ifExecute;
	
	// 劳动合同附件
	/** 劳动合同ID */
	private String workContractIdInvoice;
	/** 变更前劳动合同ID */
	private String workContractIdInvoiceBefore;
	/** 变更后劳动合同ID */
	private String workContractIdInvoiceAfter;
	/** 附件来源 */
	private String fileOriger;
	/** 变更前附件来源 */
	private String fileOrigerBefore;
	/** 变更后附件来源 */
	private String fileOrigerAfter;
	
	// 劳动合同有效期维护
	/** 劳动合同有效期 */
	private String contractTerm;
	/** 变更前劳动合同有效期 */
	private String contractTermBefore;
	/** 变更后劳动合同有效期 */
	private String contractTermAfter;
	
	// 部门设置表
	/** 部门名称 */
	private String deptName;
	/** 甲方部门名称或变更前部门名称 */
	private String deptNameFirst;
	/** 乙方部门名称或变更后部门名称 */
	private String deptNameSecond;
	
	// 岗位设置表
	/** 岗位名称 */
	private String stationName;
	/** 变更前岗位名称 */
	private String stationNameBefore;
	/** 变更后岗位名称 */
	private String stationNameAfter;
	
	// 劳动合同变更登记表
	/** 变更时间 */
	private String changeDate;
	/** 变更前合同起始时间 */
	private String oldStartTime;
	/** 变更后合同起始时间 */
	private String newStartTime;
	/** 变更前合同终止时间 */
	private String oldEndTime;
	/** 变更后合同终止时间 */
	private String newEndTime;
	/** 变更原因 */
	private String changeReason;
	/** 备注 */
	private String memoChange;
	/** 劳动合同变更ID */
	private String contractChangedId;
	
	// 劳动合同解除终止登记表
	/** 实际解除终止日期 */
	private String realEndTime;
	/** 劳动合同终止类别 */
	private String contractStopType;
	/** 劳动合同解除原因 */
	private String releaseReason;
	/** 开始日期 */
	private String startDateStop;
	/** 结束日期 */
	private String endDateStop;
	/** 档案转移去向 */
	private String dossierDirection;
	/** 社会保险关系转移去向 */
	private String societyInsuranceDirection;
	/** 备注 */
	private String memoStop;
	/** 劳动合同解除终止ID */
	private String contractStopId;
	
	/** 账票用行数计数 */
    private Integer cntRow;
	
	/**
	 * @return the chsName
	 */
	public String getChsName() {
		return chsName;
	}
	/**
	 * @param chsName the chsName to set
	 */
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}
	/**
	 * @return the empCode
	 */
	public String getEmpCode() {
		return empCode;
	}
	/**
	 * @param empCode the empCode to set
	 */
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	/**
	 * @return the archivesId
	 */
	public String getArchivesId() {
		return archivesId;
	}
	/**
	 * @param archivesId the archivesId to set
	 */
	public void setArchivesId(String archivesId) {
		this.archivesId = archivesId;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return the birthday
	 */
	public String getBirthday() {
		return birthday;
	}
	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	/**
	 * @return the workDate
	 */
	public String getWorkDate() {
		return workDate;
	}
	/**
	 * @param workDate the workDate to set
	 */
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	/**
	 * @return the missionDate
	 */
	public String getMissionDate() {
		return missionDate;
	}
	/**
	 * @param missionDate the missionDate to set
	 */
	public void setMissionDate(String missionDate) {
		this.missionDate = missionDate;
	}
	/**
	 * @return the empId
	 */
	public String getEmpId() {
		return empId;
	}
	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	/**
	 * @return the workContractNo
	 */
	public String getWorkContractNo() {
		return workContractNo;
	}
	/**
	 * @param workContractNo the workContractNo to set
	 */
	public void setWorkContractNo(String workContractNo) {
		this.workContractNo = workContractNo;
	}
	/**
	 * @return the contractContinueMark
	 */
	public String getContractContinueMark() {
		return contractContinueMark;
	}
	/**
	 * @param contractContinueMark the contractContinueMark to set
	 */
	public void setContractContinueMark(String contractContinueMark) {
		this.contractContinueMark = contractContinueMark;
	}
	/**
	 * @return the workSignDate
	 */
	public String getWorkSignDate() {
		return workSignDate;
	}
	/**
	 * @param workSignDate the workSignDate to set
	 */
	public void setWorkSignDate(String workSignDate) {
		this.workSignDate = workSignDate;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the tryoutStartDate
	 */
	public String getTryoutStartDate() {
		return tryoutStartDate;
	}
	/**
	 * @param tryoutStartDate the tryoutStartDate to set
	 */
	public void setTryoutStartDate(String tryoutStartDate) {
		this.tryoutStartDate = tryoutStartDate;
	}
	/**
	 * @return the tryoutEndDate
	 */
	public String getTryoutEndDate() {
		return tryoutEndDate;
	}
	/**
	 * @param tryoutEndDate the tryoutEndDate to set
	 */
	public void setTryoutEndDate(String tryoutEndDate) {
		this.tryoutEndDate = tryoutEndDate;
	}
	/**
	 * @return the memoContract
	 */
	public String getMemoContract() {
		return memoContract;
	}
	/**
	 * @param memoContract the memoContract to set
	 */
	public void setMemoContract(String memoContract) {
		this.memoContract = memoContract;
	}
	/**
	 * @return the workContractIdContract
	 */
	public String getWorkContractIdContract() {
		return workContractIdContract;
	}
	/**
	 * @param workContractIdContract the workContractIdContract to set
	 */
	public void setWorkContractIdContract(String workContractIdContract) {
		this.workContractIdContract = workContractIdContract;
	}
	/**
	 * @return the firstAddress
	 */
	public String getFirstAddress() {
		return firstAddress;
	}
	/**
	 * @param firstAddress the firstAddress to set
	 */
	public void setFirstAddress(String firstAddress) {
		this.firstAddress = firstAddress;
	}
	/**
	 * @return the ifExecute
	 */
	public String getIfExecute() {
		return ifExecute;
	}
	/**
	 * @param ifExecute the ifExecute to set
	 */
	public void setIfExecute(String ifExecute) {
		this.ifExecute = ifExecute;
	}
	/**
	 * @return the workContractIdInvoice
	 */
	public String getWorkContractIdInvoice() {
		return workContractIdInvoice;
	}
	/**
	 * @param workContractIdInvoice the workContractIdInvoice to set
	 */
	public void setWorkContractIdInvoice(String workContractIdInvoice) {
		this.workContractIdInvoice = workContractIdInvoice;
	}
	/**
	 * @return the workContractIdInvoiceBefore
	 */
	public String getWorkContractIdInvoiceBefore() {
		return workContractIdInvoiceBefore;
	}
	/**
	 * @param workContractIdInvoiceBefore the workContractIdInvoiceBefore to set
	 */
	public void setWorkContractIdInvoiceBefore(String workContractIdInvoiceBefore) {
		this.workContractIdInvoiceBefore = workContractIdInvoiceBefore;
	}
	/**
	 * @return the workContractIdInvoiceAfter
	 */
	public String getWorkContractIdInvoiceAfter() {
		return workContractIdInvoiceAfter;
	}
	/**
	 * @param workContractIdInvoiceAfter the workContractIdInvoiceAfter to set
	 */
	public void setWorkContractIdInvoiceAfter(String workContractIdInvoiceAfter) {
		this.workContractIdInvoiceAfter = workContractIdInvoiceAfter;
	}
	/**
	 * @return the contractTerm
	 */
	public String getContractTerm() {
		return contractTerm;
	}
	/**
	 * @param contractTerm the contractTerm to set
	 */
	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
	}
	/**
	 * @return the contractTermBefore
	 */
	public String getContractTermBefore() {
		return contractTermBefore;
	}
	/**
	 * @param contractTermBefore the contractTermBefore to set
	 */
	public void setContractTermBefore(String contractTermBefore) {
		this.contractTermBefore = contractTermBefore;
	}
	/**
	 * @return the contractTermAfter
	 */
	public String getContractTermAfter() {
		return contractTermAfter;
	}
	/**
	 * @param contractTermAfter the contractTermAfter to set
	 */
	public void setContractTermAfter(String contractTermAfter) {
		this.contractTermAfter = contractTermAfter;
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
	 * @return the deptNameFirst
	 */
	public String getDeptNameFirst() {
		return deptNameFirst;
	}
	/**
	 * @param deptNameFirst the deptNameFirst to set
	 */
	public void setDeptNameFirst(String deptNameFirst) {
		this.deptNameFirst = deptNameFirst;
	}
	/**
	 * @return the deptNameSecond
	 */
	public String getDeptNameSecond() {
		return deptNameSecond;
	}
	/**
	 * @param deptNameSecond the deptNameSecond to set
	 */
	public void setDeptNameSecond(String deptNameSecond) {
		this.deptNameSecond = deptNameSecond;
	}
	/**
	 * @return the stationName
	 */
	public String getStationName() {
		return stationName;
	}
	/**
	 * @param stationName the stationName to set
	 */
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	/**
	 * @return the stationNameBefore
	 */
	public String getStationNameBefore() {
		return stationNameBefore;
	}
	/**
	 * @param stationNameBefore the stationNameBefore to set
	 */
	public void setStationNameBefore(String stationNameBefore) {
		this.stationNameBefore = stationNameBefore;
	}
	/**
	 * @return the stationNameAfter
	 */
	public String getStationNameAfter() {
		return stationNameAfter;
	}
	/**
	 * @param stationNameAfter the stationNameAfter to set
	 */
	public void setStationNameAfter(String stationNameAfter) {
		this.stationNameAfter = stationNameAfter;
	}
	/**
	 * @return the changeDate
	 */
	public String getChangeDate() {
		return changeDate;
	}
	/**
	 * @param changeDate the changeDate to set
	 */
	public void setChangeDate(String changeDate) {
		this.changeDate = changeDate;
	}
	/**
	 * @return the oldStartTime
	 */
	public String getOldStartTime() {
		return oldStartTime;
	}
	/**
	 * @param oldStartTime the oldStartTime to set
	 */
	public void setOldStartTime(String oldStartTime) {
		this.oldStartTime = oldStartTime;
	}
	/**
	 * @return the newStartTime
	 */
	public String getNewStartTime() {
		return newStartTime;
	}
	/**
	 * @param newStartTime the newStartTime to set
	 */
	public void setNewStartTime(String newStartTime) {
		this.newStartTime = newStartTime;
	}
	/**
	 * @return the oldEndTime
	 */
	public String getOldEndTime() {
		return oldEndTime;
	}
	/**
	 * @param oldEndTime the oldEndTime to set
	 */
	public void setOldEndTime(String oldEndTime) {
		this.oldEndTime = oldEndTime;
	}
	/**
	 * @return the newEndTime
	 */
	public String getNewEndTime() {
		return newEndTime;
	}
	/**
	 * @param newEndTime the newEndTime to set
	 */
	public void setNewEndTime(String newEndTime) {
		this.newEndTime = newEndTime;
	}
	/**
	 * @return the changeReason
	 */
	public String getChangeReason() {
		return changeReason;
	}
	/**
	 * @param changeReason the changeReason to set
	 */
	public void setChangeReason(String changeReason) {
		this.changeReason = changeReason;
	}
	/**
	 * @return the memoChange
	 */
	public String getMemoChange() {
		return memoChange;
	}
	/**
	 * @param memoChange the memoChange to set
	 */
	public void setMemoChange(String memoChange) {
		this.memoChange = memoChange;
	}
	/**
	 * @return the contractChangedId
	 */
	public String getContractChangedId() {
		return contractChangedId;
	}
	/**
	 * @param contractChangedId the contractChangedId to set
	 */
	public void setContractChangedId(String contractChangedId) {
		this.contractChangedId = contractChangedId;
	}
	/**
	 * @return the realEndTime
	 */
	public String getRealEndTime() {
		return realEndTime;
	}
	/**
	 * @param realEndTime the realEndTime to set
	 */
	public void setRealEndTime(String realEndTime) {
		this.realEndTime = realEndTime;
	}
	/**
	 * @return the contractStopType
	 */
	public String getContractStopType() {
		return contractStopType;
	}
	/**
	 * @param contractStopType the contractStopType to set
	 */
	public void setContractStopType(String contractStopType) {
		this.contractStopType = contractStopType;
	}
	/**
	 * @return the releaseReason
	 */
	public String getReleaseReason() {
		return releaseReason;
	}
	/**
	 * @param releaseReason the releaseReason to set
	 */
	public void setReleaseReason(String releaseReason) {
		this.releaseReason = releaseReason;
	}
	/**
	 * @return the startDateStop
	 */
	public String getStartDateStop() {
		return startDateStop;
	}
	/**
	 * @param startDateStop the startDateStop to set
	 */
	public void setStartDateStop(String startDateStop) {
		this.startDateStop = startDateStop;
	}
	/**
	 * @return the endDateStop
	 */
	public String getEndDateStop() {
		return endDateStop;
	}
	/**
	 * @param endDateStop the endDateStop to set
	 */
	public void setEndDateStop(String endDateStop) {
		this.endDateStop = endDateStop;
	}
	/**
	 * @return the dossierDirection
	 */
	public String getDossierDirection() {
		return dossierDirection;
	}
	/**
	 * @param dossierDirection the dossierDirection to set
	 */
	public void setDossierDirection(String dossierDirection) {
		this.dossierDirection = dossierDirection;
	}
	/**
	 * @return the societyInsuranceDirection
	 */
	public String getSocietyInsuranceDirection() {
		return societyInsuranceDirection;
	}
	/**
	 * @param societyInsuranceDirection the societyInsuranceDirection to set
	 */
	public void setSocietyInsuranceDirection(String societyInsuranceDirection) {
		this.societyInsuranceDirection = societyInsuranceDirection;
	}
	/**
	 * @return the memoStop
	 */
	public String getMemoStop() {
		return memoStop;
	}
	/**
	 * @param memoStop the memoStop to set
	 */
	public void setMemoStop(String memoStop) {
		this.memoStop = memoStop;
	}
	/**
	 * @return the contractStopId
	 */
	public String getContractStopId() {
		return contractStopId;
	}
	/**
	 * @param contractStopId the contractStopId to set
	 */
	public void setContractStopId(String contractStopId) {
		this.contractStopId = contractStopId;
	}
	/**
	 * @return the fileOriger
	 */
	public String getFileOriger() {
		return fileOriger;
	}
	/**
	 * @param fileOriger the fileOriger to set
	 */
	public void setFileOriger(String fileOriger) {
		this.fileOriger = fileOriger;
	}
	/**
	 * @return the fileOrigerBefore
	 */
	public String getFileOrigerBefore() {
		return fileOrigerBefore;
	}
	/**
	 * @param fileOrigerBefore the fileOrigerBefore to set
	 */
	public void setFileOrigerBefore(String fileOrigerBefore) {
		this.fileOrigerBefore = fileOrigerBefore;
	}
	/**
	 * @return the fileOrigerAfter
	 */
	public String getFileOrigerAfter() {
		return fileOrigerAfter;
	}
	/**
	 * @param fileOrigerAfter the fileOrigerAfter to set
	 */
	public void setFileOrigerAfter(String fileOrigerAfter) {
		this.fileOrigerAfter = fileOrigerAfter;
	}
	/**
	 * @return the cntRow
	 */
	public Integer getCntRow() {
		return cntRow;
	}
	/**
	 * @param cntRow the cntRow to set
	 */
	public void setCntRow(Integer cntRow) {
		this.cntRow = cntRow;
	}
	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
