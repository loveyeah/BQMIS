/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

/**
 * 合同变更管理Bean
 * 
 * @author jincong
 * @version 1.0
 */
public class ContractChangeManage implements java.io.Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	// 部门设置表
	/** 部门名称 */
	private String deptName;
	/** 变更后部门名称 */
	private String newDeptName;
	/** 甲方部门名称 */
	private String deptNameFirst;
	
	// 岗位设置表
	/** 岗位名称 */
	private String stationName;
	/** 岗位名称 */
	private String newStationName;
	/** 岗位ID */
	private String stationId;
	
	// 劳动合同登记表
	/** 人员ID */
	private String empId;
	/** 劳动合同签定ID */
	private String workContractId;
	/** 劳动合同编号 */
	private String workContractNo;
	/** 甲方地址 */
	private String firstAddress;
	/** 劳动合同续签标志 */
	private String contractContinueMark;
	/** 劳动合同签字日期 */
	private String workSignDate;
	/** 开始日期 */
	private String startDate;
	/** 结束日期 */
	private String endDate;
	/** 劳动合同有效期ID */
	private String contractTermId;
	/** 部门ID */
	private String deptId;
	/** 甲方部门ID */
	private String firstDeptId;
	/** 上次修改日期 */
	private String lastModifiedDate;
	
	// 劳动合同变更登记表
	/** 劳动合同变更ID */
	private String contractChangeId;
	/** 变更后部门ID */
	private String newDeptId;
	/** 变更后岗位 */
	private String newStationId;
	/** 变更后劳动合同有效期ID */
	private String newContractTermId;
	/** 变更时间 */
	private String changeDate;
	/** 变更后开始日期 */
	private String newStartDate;
	/** 变更后结束日期 */
	private String newEndDate;
	/** 变更原因 */
	private String changeReason;
	/** 备注 */
	private String memo;
	/** 上次修改日期 */
	private String lastModifiedDateChange;
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
	 * @return the newDeptName
	 */
	public String getNewDeptName() {
		return newDeptName;
	}
	/**
	 * @param newDeptName the newDeptName to set
	 */
	public void setNewDeptName(String newDeptName) {
		this.newDeptName = newDeptName;
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
	 * @return the stationId
	 */
	public String getStationId() {
		return stationId;
	}
	/**
	 * @param stationId the stationId to set
	 */
	public void setStationId(String stationId) {
		this.stationId = stationId;
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
	 * @return the workContractId
	 */
	public String getWorkContractId() {
		return workContractId;
	}
	/**
	 * @param workContractId the workContractId to set
	 */
	public void setWorkContractId(String workContractId) {
		this.workContractId = workContractId;
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
	 * @return the contractTermId
	 */
	public String getContractTermId() {
		return contractTermId;
	}
	/**
	 * @param contractTermId the contractTermId to set
	 */
	public void setContractTermId(String contractTermId) {
		this.contractTermId = contractTermId;
	}
	/**
	 * @return the deptId
	 */
	public String getDeptId() {
		return deptId;
	}
	/**
	 * @param deptId the deptId to set
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * @return the firstDeptId
	 */
	public String getFirstDeptId() {
		return firstDeptId;
	}
	/**
	 * @param firstDeptId the firstDeptId to set
	 */
	public void setFirstDeptId(String firstDeptId) {
		this.firstDeptId = firstDeptId;
	}
	/**
	 * @return the lastModifiedDate
	 */
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	/**
	 * @return the contractChangeId
	 */
	public String getContractChangeId() {
		return contractChangeId;
	}
	/**
	 * @param contractChangeId the contractChangeId to set
	 */
	public void setContractChangeId(String contractChangeId) {
		this.contractChangeId = contractChangeId;
	}
	/**
	 * @return the newDeptId
	 */
	public String getNewDeptId() {
		return newDeptId;
	}
	/**
	 * @param newDeptId the newDeptId to set
	 */
	public void setNewDeptId(String newDeptId) {
		this.newDeptId = newDeptId;
	}
	/**
	 * @return the newStationId
	 */
	public String getNewStationId() {
		return newStationId;
	}
	/**
	 * @param newStationId the newStationId to set
	 */
	public void setNewStationId(String newStationId) {
		this.newStationId = newStationId;
	}
	/**
	 * @return the newContractTermId
	 */
	public String getNewContractTermId() {
		return newContractTermId;
	}
	/**
	 * @param newContractTermId the newContractTermId to set
	 */
	public void setNewContractTermId(String newContractTermId) {
		this.newContractTermId = newContractTermId;
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
	 * @return the newStartDate
	 */
	public String getNewStartDate() {
		return newStartDate;
	}
	/**
	 * @param newStartDate the newStartDate to set
	 */
	public void setNewStartDate(String newStartDate) {
		this.newStartDate = newStartDate;
	}
	/**
	 * @return the newEndDate
	 */
	public String getNewEndDate() {
		return newEndDate;
	}
	/**
	 * @param newEndDate the newEndDate to set
	 */
	public void setNewEndDate(String newEndDate) {
		this.newEndDate = newEndDate;
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
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * @return the lastModifiedDateChange
	 */
	public String getLastModifiedDateChange() {
		return lastModifiedDateChange;
	}
	/**
	 * @param lastModifiedDateChange the lastModifiedDateChange to set
	 */
	public void setLastModifiedDateChange(String lastModifiedDateChange) {
		this.lastModifiedDateChange = lastModifiedDateChange;
	}
	/**
	 * @return the newStationName
	 */
	public String getNewStationName() {
		return newStationName;
	}
	/**
	 * @param newStationName the newStationName to set
	 */
	public void setNewStationName(String newStationName) {
		this.newStationName = newStationName;
	}
}
