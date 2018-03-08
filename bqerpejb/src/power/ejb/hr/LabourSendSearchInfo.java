/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;
/**
 * 劳务派遣查询Bean
 * @author lichensheng
 * @version 1.0
 */
public class LabourSendSearchInfo implements java.io.Serializable{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/** 劳务派遣合同ID */
	Long borrowcontractid;
	/** 劳动合同编号 */
	String workContractNo;
	/** 签字日期 */
	String signatureDate;
	/** 协作单位ID */
	Long cooperateUnitId;
	/** 协作单位名称 */
	String cooperateUnit;
	/** 开始日期 */
	String startDate;
	/** 结束日期 */
	String endDate;
	/** 劳务内容 */
	String contractContent;
	/** 合同附件 */
	Long fileCount;
	/** 单据状态 */
	String dcmStatus;
	/** 备注 */
	String note;
	
	/** 调动类型* */
	String transferType;
	
	
	/** 人员一览Bean */
	/** 员工外借ID */
	Long employeeborrowid;
	/** 员工编码 */
	String empCode;
	/** 中文名 */
	String chsName;
	/** 部门名称 */
	String deptName;
	/** 岗位名称 */
	String stationName;
	/** 停薪日期 */
	String stopPayDate;
	/** 起薪日期 */
	String startPayDate;
	/** 是否回来 */
	String ifBack;
	/** 备注 */
	String memo;
	
	public Long getEmployeeborrowid() {
		return employeeborrowid;
	}
	public void setEmployeeborrowid(Long employeeborrowid) {
		this.employeeborrowid = employeeborrowid;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getChsName() {
		return chsName;
	}
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getStopPayDate() {
		return stopPayDate;
	}
	public void setStopPayDate(String stopPayDate) {
		this.stopPayDate = stopPayDate;
	}
	public String getStartPayDate() {
		return startPayDate;
	}
	public void setStartPayDate(String startPayDate) {
		this.startPayDate = startPayDate;
	}
	public String getIfBack() {
		return ifBack;
	}
	public void setIfBack(String ifBack) {
		this.ifBack = ifBack;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Long getBorrowcontractid() {
		return borrowcontractid;
	}
	public void setBorrowcontractid(Long borrowcontractid) {
		this.borrowcontractid = borrowcontractid;
	}
	public String getWorkContractNo() {
		return workContractNo;
	}
	public void setWorkContractNo(String workContractNo) {
		this.workContractNo = workContractNo;
	}
	public String getSignatureDate() {
		return signatureDate;
	}
	public void setSignatureDate(String signatureDate) {
		this.signatureDate = signatureDate;
	}
	public Long getCooperateUnitId() {
		return cooperateUnitId;
	}
	public void setCooperateUnitId(Long cooperateUnitId) {
		this.cooperateUnitId = cooperateUnitId;
	}
	public String getCooperateUnit() {
		return cooperateUnit;
	}
	public void setCooperateUnit(String cooperateUnit) {
		this.cooperateUnit = cooperateUnit;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getContractContent() {
		return contractContent;
	}
	public void setContractContent(String contractContent) {
		this.contractContent = contractContent;
	}
	public Long getFileCount() {
		return fileCount;
	}
	public void setFileCount(Long fileCount) {
		this.fileCount = fileCount;
	}
	public String getDcmStatus() {
		return dcmStatus;
	}
	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getTransferType() {
		return transferType;
	}
	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

}
