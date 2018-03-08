/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;
/**
 * 员工调动单申报bean
 * 
 * @author chen shoujiang
 */
public class EmpMoveDeclare implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	/**岗位调动单ID */
	private String stationRemoveId;
	/** 岗位调动类别ID */
	private String stationMoveTypeId;
	/**岗位调动类别名称 */
	private String stationMoveType;
	/** 
	/** 中文姓名 */	
	private String chsName;
	/** 员工id*/
	private String empId;
	/** 员工Code*/
	private String empCode;
	/**调动日期*/
	private String removeDate;
	/** 起薪日期 */	
	private String doDate2;
	/** 调动通知单号 */	
	private String requisition;
	/** 调动前部门ID*/
	private String bfdeptId;
	/** 调动前部门名称 */	
	private String bfDeptName;
	/** 调动后部门id */	
	private String afDeptId;
	/** 调动后部门 */	
	private String afDeptName;
	/** 调动前岗位id */	
	private String bfStationId;
	/** 调动前岗位 */	
	private String bfStationName;
	/** 调动后岗位id */	
	private String afStationId;
	/** 调动后岗位 */	
	private String afStationName;
	/** 调动前岗位级别id */	
	private String bfStationLevel;
	/** 调动前岗位级别 */	
	private String bfStationLevelName;
	/** 调动后岗位级别id */	
	private String afStationLevel;
	/** 调动后岗位级别 */	
	private String afStationLevelName;
	/** 单据状态 */	
	private String dcmState;
	/** 原因*/	
	private String reason;
	/** 备注 */	
	private String memo;
	/**执行岗级*/
	private String checkStationGrade;
	/** 标准岗级 */
	private String stationGrade;
	/** 薪级 */
	private String salaryLevel;
	private String lastModifiedDate;
	
	
	private String oldSalaryLevel;
	// add by liuyi 20100617 调动后薪级 调动前后薪点 岗位级别
	private String newSalaryLevel;
	private String oldSalaryPoint;
	private String newSalaryPoint;
	private String oldStationGrade;
	private String newStationGrade;
	
	private String insertdate;

	private String beforeBanZu;//add by sychen 20100722
	private String afterBanZu;//add by sychen 20100722
	private String printDate;//add by sychen 20100723
	private String beforeBanZuId;//add by sychen 20100723
	private String afterBanZuId;//add by sychen 20100723
	
	
	public String getInsertdate() {
		return insertdate;
	}
	public void setInsertdate(String insertdate) {
		this.insertdate = insertdate;
	}
	public String getCheckStationGrade() {
		return checkStationGrade;
	}
	public void setCheckStationGrade(String checkStationGrade) {
		this.checkStationGrade = checkStationGrade;
	}
	public String getStationGrade() {
		return stationGrade;
	}
	public void setStationGrade(String stationGrade) {
		this.stationGrade = stationGrade;
	}
	public String getChsName() {
		return chsName;
	}
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}
	public String getRemoveDate() {
		return removeDate;
	}
	public void setRemoveDate(String removeDate) {
		this.removeDate = removeDate;
	}
	public String getDoDate2() {
		return doDate2;
	}
	public void setDoDate2(String doDate2) {
		this.doDate2 = doDate2;
	}
	public String getRequisition() {
		return requisition;
	}
	public void setRequisition(String requisition) {
		this.requisition = requisition;
	}

	public String getDcmState() {
		return dcmState;
	}
	public void setDcmState(String dcmState) {
		this.dcmState = dcmState;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getAfDeptName() {
		return afDeptName;
	}
	public void setAfDeptName(String afDeptName) {
		this.afDeptName = afDeptName;
	}
	public String getBfStationName() {
		return bfStationName;
	}
	public void setBfStationName(String bfStationName) {
		this.bfStationName = bfStationName;
	}
	public String getAfStationName() {
		return afStationName;
	}
	public void setAfStationName(String afStationName) {
		this.afStationName = afStationName;
	}
	public String getBfStationLevelName() {
		return bfStationLevelName;
	}
	public void setBfStationLevelName(String bfStationLevelName) {
		this.bfStationLevelName = bfStationLevelName;
	}
	public String getAfStationLevelName() {
		return afStationLevelName;
	}
	public void setAfStationLevelName(String afStationLevelName) {
		this.afStationLevelName = afStationLevelName;
	}
	public String getBfDeptName() {
		return bfDeptName;
	}
	public void setBfDeptName(String bfDeptName) {
		this.bfDeptName = bfDeptName;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getBfdeptId() {
		return bfdeptId;
	}
	public void setBfdeptId(String bfdeptId) {
		this.bfdeptId = bfdeptId;
	}
	public String getAfDeptId() {
		return afDeptId;
	}
	public void setAfDeptId(String afDeptId) {
		this.afDeptId = afDeptId;
	}
	public String getBfStationId() {
		return bfStationId;
	}
	public void setBfStationId(String bfStationId) {
		this.bfStationId = bfStationId;
	}
	public String getAfStationId() {
		return afStationId;
	}
	public void setAfStationId(String afStationId) {
		this.afStationId = afStationId;
	}
	public String getBfStationLevel() {
		return bfStationLevel;
	}
	public void setBfStationLevel(String bfStationLevel) {
		this.bfStationLevel = bfStationLevel;
	}
	public String getAfStationLevel() {
		return afStationLevel;
	}
	public void setAfStationLevel(String afStationLevel) {
		this.afStationLevel = afStationLevel;
	}
	public String getStationRemoveId() {
		return stationRemoveId;
	}
	public void setStationRemoveId(String stationRemoveId) {
		this.stationRemoveId = stationRemoveId;
	}
	public String getStationMoveTypeId() {
		return stationMoveTypeId;
	}
	public void setStationMoveTypeId(String stationMoveTypeId) {
		this.stationMoveTypeId = stationMoveTypeId;
	}
	public String getSalaryLevel() {
		return salaryLevel;
	}
	public void setSalaryLevel(String salaryLevel) {
		this.salaryLevel = salaryLevel;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getStationMoveType() {
		return stationMoveType;
	}
	public void setStationMoveType(String stationMoveType) {
		this.stationMoveType = stationMoveType;
	}
	public String getOldSalaryPoint() {
		return oldSalaryPoint;
	}
	public void setOldSalaryPoint(String oldSalaryPoint) {
		this.oldSalaryPoint = oldSalaryPoint;
	}
	public String getNewSalaryPoint() {
		return newSalaryPoint;
	}
	public void setNewSalaryPoint(String newSalaryPoint) {
		this.newSalaryPoint = newSalaryPoint;
	}
	public String getNewSalaryLevel() {
		return newSalaryLevel;
	}
	public void setNewSalaryLevel(String newSalaryLevel) {
		this.newSalaryLevel = newSalaryLevel;
	}
	public String getOldSalaryLevel() {
		return oldSalaryLevel;
	}
	public void setOldSalaryLevel(String oldSalaryLevel) {
		this.oldSalaryLevel = oldSalaryLevel;
	}
	public String getOldStationGrade() {
		return oldStationGrade;
	}
	public void setOldStationGrade(String oldStationGrade) {
		this.oldStationGrade = oldStationGrade;
	}
	public String getNewStationGrade() {
		return newStationGrade;
	}
	public void setNewStationGrade(String newStationGrade) {
		this.newStationGrade = newStationGrade;
	}
	public String getBeforeBanZu() {
		return beforeBanZu;
	}
	public void setBeforeBanZu(String beforeBanZu) {
		this.beforeBanZu = beforeBanZu;
	}
	public String getAfterBanZu() {
		return afterBanZu;
	}
	public void setAfterBanZu(String afterBanZu) {
		this.afterBanZu = afterBanZu;
	}
	
	public String getPrintDate() {
		return printDate;
	}
	public void setPrintDate(String printDate) {
		this.printDate = printDate;
	}
	public String getBeforeBanZuId() {
		return beforeBanZuId;
	}
	public void setBeforeBanZuId(String beforeBanZuId) {
		this.beforeBanZuId = beforeBanZuId;
	}
	public String getAfterBanZuId() {
		return afterBanZuId;
	}
	public void setAfterBanZuId(String afterBanZuId) {
		this.afterBanZuId = afterBanZuId;
	}

	
}
