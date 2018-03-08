package power.ejb.run.protectinoutapply.form;

public class ProAppInfoForm {
	/** 申请人*/
	private String applyName;
	/** 申请部门*/
	private String applyDeptName;
	/** 申请时间*/
	private String applyDate;
	/** 设备名称*/
	private String equName;
	/** 申请开工时间*/
	private String applyStartDate;
	/** 申请完工时间*/
	private String applyEndDate;
	/** 状态名称*/
	private String statusName;
	//----------------------------
	
	/**  执行人  */
	private String executeByName;
	
	/** 监护人 */
	private String keeperName;
	
	/** 许可人 */
	private String permitByName;
	
	/**  审核人 */
	private String checkupByName;
	
	/** 保护投入时间 */
	private String strProtectInDate;
	
	/** 保护退出时间 */
	private String strProtectOutDate;
	
	/** 批准开工时间 */
	private String strApproveStartDate;
	
	/** 批准完工时间 */
	private String strApproveEndDate;
	
	private String memo;
	private Long statusId;
	private Long workFlowNo;
	private String checkupBy;
	private String permitBy;
	private String keeper;
	private String executeBy;
	private String equEffect;
	private String outSafety;
	private String protectName;
	private String protectReason;
	private String equCode;
	private String applyDept;
	private Long applyId;
	private String protectNo;
	private String applyBy;
	public String getApplyName() {
		return applyName;
	}
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}
	public String getApplyDeptName() {
		return applyDeptName;
	}
	public void setApplyDeptName(String applyDeptName) {
		this.applyDeptName = applyDeptName;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getEquName() {
		return equName;
	}
	public void setEquName(String equName) {
		this.equName = equName;
	}
	public String getApplyStartDate() {
		return applyStartDate;
	}
	public void setApplyStartDate(String applyStartDate) {
		this.applyStartDate = applyStartDate;
	}
	public String getApplyEndDate() {
		return applyEndDate;
	}
	public void setApplyEndDate(String applyEndDate) {
		this.applyEndDate = applyEndDate;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getExecuteByName() {
		return executeByName;
	}
	public void setExecuteByName(String executeByName) {
		this.executeByName = executeByName;
	}
	public String getKeeperName() {
		return keeperName;
	}
	public void setKeeperName(String keeperName) {
		this.keeperName = keeperName;
	}
	public String getPermitByName() {
		return permitByName;
	}
	public void setPermitByName(String permitByName) {
		this.permitByName = permitByName;
	}
	public String getCheckupByName() {
		return checkupByName;
	}
	public void setCheckupByName(String checkupByName) {
		this.checkupByName = checkupByName;
	}
	public String getStrProtectInDate() {
		return strProtectInDate;
	}
	public void setStrProtectInDate(String strProtectInDate) {
		this.strProtectInDate = strProtectInDate;
	}
	public String getStrProtectOutDate() {
		return strProtectOutDate;
	}
	public void setStrProtectOutDate(String strProtectOutDate) {
		this.strProtectOutDate = strProtectOutDate;
	}
	public String getStrApproveStartDate() {
		return strApproveStartDate;
	}
	public void setStrApproveStartDate(String strApproveStartDate) {
		this.strApproveStartDate = strApproveStartDate;
	}
	public String getStrApproveEndDate() {
		return strApproveEndDate;
	}
	public void setStrApproveEndDate(String strApproveEndDate) {
		this.strApproveEndDate = strApproveEndDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public Long getWorkFlowNo() {
		return workFlowNo;
	}
	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}
	public String getCheckupBy() {
		return checkupBy;
	}
	public void setCheckupBy(String checkupBy) {
		this.checkupBy = checkupBy;
	}
	public String getPermitBy() {
		return permitBy;
	}
	public void setPermitBy(String permitBy) {
		this.permitBy = permitBy;
	}
	public String getKeeper() {
		return keeper;
	}
	public void setKeeper(String keeper) {
		this.keeper = keeper;
	}
	public String getExecuteBy() {
		return executeBy;
	}
	public void setExecuteBy(String executeBy) {
		this.executeBy = executeBy;
	}
	public String getEquEffect() {
		return equEffect;
	}
	public void setEquEffect(String equEffect) {
		this.equEffect = equEffect;
	}
	public String getOutSafety() {
		return outSafety;
	}
	public void setOutSafety(String outSafety) {
		this.outSafety = outSafety;
	}
	public String getProtectName() {
		return protectName;
	}
	public void setProtectName(String protectName) {
		this.protectName = protectName;
	}
	public String getProtectReason() {
		return protectReason;
	}
	public void setProtectReason(String protectReason) {
		this.protectReason = protectReason;
	}
	public String getEquCode() {
		return equCode;
	}
	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}
	public String getApplyDept() {
		return applyDept;
	}
	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}
	public Long getApplyId() {
		return applyId;
	}
	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}
	public String getProtectNo() {
		return protectNo;
	}
	public void setProtectNo(String protectNo) {
		this.protectNo = protectNo;
	}
	public String getApplyBy() {
		return applyBy;
	}
	public void setApplyBy(String applyBy) {
		this.applyBy = applyBy;
	}
}
